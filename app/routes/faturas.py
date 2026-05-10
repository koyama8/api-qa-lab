from decimal import Decimal

from fastapi import APIRouter, Query, Response

from app.schemas.fatura import FaturaCreate, FaturaStatus, FaturaUpdate
from app.services.storage import next_id, read_database, write_database
from app.utils.exceptions import ApiError
from app.utils.responses import success_response

router = APIRouter(tags=["Faturas"])


def _find_fatura(database: dict, fatura_id: int) -> dict | None:
    return next(
        (fatura for fatura in database["faturas"] if fatura["id"] == fatura_id),
        None,
    )


def _cliente_existe(database: dict, cliente_id: int) -> bool:
    return any(cliente["id"] == cliente_id for cliente in database["clientes"])


def _find_cartao(database: dict, cartao_id: int) -> dict | None:
    return next(
        (cartao for cartao in database["cartoes"] if cartao["id"] == cartao_id),
        None,
    )


def _validar_cliente_e_cartao(database: dict, cliente_id: int, cartao_id: int) -> None:
    if not _cliente_existe(database, cliente_id):
        raise ApiError(
            status_code=404,
            message="Cliente não encontrado para a fatura.",
            error="NOT_FOUND",
        )

    cartao = _find_cartao(database, cartao_id)
    if cartao is None:
        raise ApiError(
            status_code=404,
            message="Cartão não encontrado para a fatura.",
            error="NOT_FOUND",
        )

    if cartao["cliente_id"] != cliente_id:
        raise ApiError(
            status_code=400,
            message="Cartão não pertence ao cliente informado.",
            error="VALIDATION_ERROR",
        )


def _to_float(value: Decimal) -> float:
    return float(value)


@router.get("/faturas")
def listar_faturas(
    cliente_id: int | None = Query(default=None, gt=0, description="Filtra faturas pelo ID do cliente."),
    cartao_id: int | None = Query(default=None, gt=0, description="Filtra faturas pelo ID do cartão."),
    status: FaturaStatus | None = Query(default=None, description="Filtra faturas por status."),
):
    database = read_database()
    faturas = database["faturas"]

    if cliente_id is not None:
        faturas = [
            fatura for fatura in faturas if fatura["cliente_id"] == cliente_id
        ]

    if cartao_id is not None:
        faturas = [fatura for fatura in faturas if fatura["cartao_id"] == cartao_id]

    if status is not None:
        faturas = [fatura for fatura in faturas if fatura["status"] == status.value]

    return success_response(
        data=faturas,
        headers={"X-Total-Count": str(len(faturas))},
    )


@router.get("/faturas/{fatura_id}")
def buscar_fatura(fatura_id: int):
    database = read_database()
    fatura = _find_fatura(database, fatura_id)

    if fatura is None:
        raise ApiError(
            status_code=404,
            message="Fatura não encontrada.",
            error="NOT_FOUND",
        )

    return success_response(data=fatura)


@router.post("/faturas", status_code=201)
def criar_fatura(payload: FaturaCreate):
    database = read_database()
    _validar_cliente_e_cartao(database, payload.cliente_id, payload.cartao_id)

    nova_fatura = {
        "id": next_id(database["faturas"]),
        "cliente_id": payload.cliente_id,
        "cartao_id": payload.cartao_id,
        "valor": _to_float(payload.valor),
        "status": payload.status.value,
        "vencimento": payload.vencimento.isoformat(),
    }

    database["faturas"].append(nova_fatura)
    write_database(database)

    return success_response(data=nova_fatura, status_code=201)


@router.put("/faturas/{fatura_id}")
def atualizar_fatura(fatura_id: int, payload: FaturaUpdate):
    database = read_database()
    fatura = _find_fatura(database, fatura_id)

    if fatura is None:
        raise ApiError(
            status_code=404,
            message="Fatura não encontrada.",
            error="NOT_FOUND",
        )

    updates = payload.model_dump(exclude_unset=True)
    cliente_id = updates.get("cliente_id", fatura["cliente_id"])
    cartao_id = updates.get("cartao_id", fatura["cartao_id"])
    _validar_cliente_e_cartao(database, cliente_id, cartao_id)

    if "valor" in updates:
        updates["valor"] = _to_float(updates["valor"])

    if "status" in updates:
        updates["status"] = updates["status"].value

    if "vencimento" in updates:
        updates["vencimento"] = updates["vencimento"].isoformat()

    fatura.update(updates)
    write_database(database)

    return success_response(data=fatura)


@router.delete("/faturas/{fatura_id}", status_code=204)
def deletar_fatura(fatura_id: int):
    database = read_database()
    fatura = _find_fatura(database, fatura_id)

    if fatura is None:
        raise ApiError(
            status_code=404,
            message="Fatura não encontrada.",
            error="NOT_FOUND",
        )

    if fatura["status"] == FaturaStatus.PAGA.value:
        raise ApiError(
            status_code=409,
            message="Fatura paga não pode ser deletada.",
            error="BUSINESS_RULE_VIOLATION",
        )

    database["faturas"].remove(fatura)
    write_database(database)

    return Response(status_code=204)


@router.get("/clientes/{cliente_id}/faturas")
def listar_faturas_do_cliente(cliente_id: int):
    database = read_database()

    if not _cliente_existe(database, cliente_id):
        raise ApiError(
            status_code=404,
            message="Cliente não encontrado.",
            error="NOT_FOUND",
        )

    faturas = [
        fatura for fatura in database["faturas"] if fatura["cliente_id"] == cliente_id
    ]

    return success_response(data=faturas)
