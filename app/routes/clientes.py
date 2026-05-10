from fastapi import APIRouter, Query, Response

from app.schemas.cliente import ClienteCreate, ClienteUpdate
from app.services.storage import next_id, read_database, write_database
from app.utils.exceptions import ApiError
from app.utils.responses import success_response

router = APIRouter(prefix="/clientes", tags=["Clientes"])


def _find_cliente(database: dict, cliente_id: int) -> dict | None:
    return next(
        (cliente for cliente in database["clientes"] if cliente["id"] == cliente_id),
        None,
    )


@router.get("")
def listar_clientes(
    ativo: bool | None = Query(default=None, description="Filtra clientes ativos ou inativos."),
    nome: str | None = Query(default=None, description="Filtra clientes por parte do nome."),
):
    database = read_database()
    clientes = database["clientes"]

    if ativo is not None:
        clientes = [cliente for cliente in clientes if cliente["ativo"] == ativo]

    if nome:
        nome_normalizado = nome.lower()
        clientes = [
            cliente
            for cliente in clientes
            if nome_normalizado in cliente["nome"].lower()
        ]

    return success_response(
        data=clientes,
        headers={"X-Total-Count": str(len(clientes))},
    )


@router.get("/{cliente_id}")
def buscar_cliente(cliente_id: int):
    database = read_database()
    cliente = _find_cliente(database, cliente_id)

    if cliente is None:
        raise ApiError(
            status_code=404,
            message="Cliente não encontrado.",
            error="NOT_FOUND",
        )

    return success_response(data=cliente)


@router.post("", status_code=201)
def criar_cliente(payload: ClienteCreate):
    database = read_database()
    novo_cliente = {
        "id": next_id(database["clientes"]),
        "nome": payload.nome,
        "email": str(payload.email),
        "cpf": payload.cpf,
        "ativo": True,
    }

    database["clientes"].append(novo_cliente)
    write_database(database)

    return success_response(data=novo_cliente, status_code=201)


@router.put("/{cliente_id}")
def atualizar_cliente(cliente_id: int, payload: ClienteUpdate):
    database = read_database()
    cliente = _find_cliente(database, cliente_id)

    if cliente is None:
        raise ApiError(
            status_code=404,
            message="Cliente não encontrado.",
            error="NOT_FOUND",
        )

    updates = payload.model_dump(exclude_unset=True)
    if "email" in updates:
        updates["email"] = str(updates["email"])

    cliente.update(updates)
    write_database(database)

    return success_response(data=cliente)


@router.delete("/{cliente_id}", status_code=204)
def deletar_cliente(cliente_id: int):
    database = read_database()
    cliente = _find_cliente(database, cliente_id)

    if cliente is None:
        raise ApiError(
            status_code=404,
            message="Cliente não encontrado.",
            error="NOT_FOUND",
        )

    database["clientes"].remove(cliente)
    write_database(database)

    return Response(status_code=204)
