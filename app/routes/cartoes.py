from fastapi import APIRouter, Query

from app.schemas.cartao import CartaoCreate, CartaoStatus
from app.services.storage import next_id, read_database, write_database
from app.utils.exceptions import ApiError
from app.utils.responses import success_response

router = APIRouter(prefix="/cartoes", tags=["Cartões"])


def _find_cartao(database: dict, cartao_id: int) -> dict | None:
    return next(
        (cartao for cartao in database["cartoes"] if cartao["id"] == cartao_id),
        None,
    )


def _cliente_existe(database: dict, cliente_id: int) -> bool:
    return any(cliente["id"] == cliente_id for cliente in database["clientes"])


@router.get("")
def listar_cartoes(
    cliente_id: int | None = Query(default=None, gt=0, description="Filtra cartões pelo ID do cliente."),
    status: CartaoStatus | None = Query(default=None, description="Filtra cartões por status."),
):
    database = read_database()
    cartoes = database["cartoes"]

    if cliente_id is not None:
        cartoes = [
            cartao for cartao in cartoes if cartao["cliente_id"] == cliente_id
        ]

    if status is not None:
        cartoes = [cartao for cartao in cartoes if cartao["status"] == status.value]

    return success_response(
        data=cartoes,
        headers={"X-Total-Count": str(len(cartoes))},
    )


@router.get("/{cartao_id}")
def buscar_cartao(cartao_id: int):
    database = read_database()
    cartao = _find_cartao(database, cartao_id)

    if cartao is None:
        raise ApiError(
            status_code=404,
            message="Cartão não encontrado.",
            error="NOT_FOUND",
        )

    return success_response(data=cartao)


@router.post("", status_code=201)
def criar_cartao(payload: CartaoCreate):
    database = read_database()

    if not _cliente_existe(database, payload.cliente_id):
        raise ApiError(
            status_code=404,
            message="Cliente não encontrado para criar o cartão.",
            error="NOT_FOUND",
        )

    novo_cartao = {
        "id": next_id(database["cartoes"]),
        "cliente_id": payload.cliente_id,
        "numero_masked": payload.numero_masked,
        "limite": float(payload.limite),
        "status": CartaoStatus.ATIVO.value,
    }

    database["cartoes"].append(novo_cartao)
    write_database(database)

    return success_response(data=novo_cartao, status_code=201)


@router.put("/{cartao_id}/bloquear")
def bloquear_cartao(cartao_id: int):
    database = read_database()
    cartao = _find_cartao(database, cartao_id)

    if cartao is None:
        raise ApiError(404, "Cartão não encontrado.", "NOT_FOUND")

    if cartao["status"] == CartaoStatus.CANCELADO.value:
        raise ApiError(
            status_code=409,
            message="Cartão cancelado não pode ser bloqueado.",
            error="BUSINESS_RULE_VIOLATION",
        )

    if cartao["status"] == CartaoStatus.BLOQUEADO.value:
        raise ApiError(
            status_code=409,
            message="Cartão já está bloqueado.",
            error="BUSINESS_RULE_VIOLATION",
        )

    cartao["status"] = CartaoStatus.BLOQUEADO.value
    write_database(database)

    return success_response(data=cartao)


@router.put("/{cartao_id}/desbloquear")
def desbloquear_cartao(cartao_id: int):
    database = read_database()
    cartao = _find_cartao(database, cartao_id)

    if cartao is None:
        raise ApiError(404, "Cartão não encontrado.", "NOT_FOUND")

    if cartao["status"] == CartaoStatus.CANCELADO.value:
        raise ApiError(
            status_code=409,
            message="Cartão cancelado não pode ser desbloqueado.",
            error="BUSINESS_RULE_VIOLATION",
        )

    if cartao["status"] == CartaoStatus.ATIVO.value:
        raise ApiError(
            status_code=409,
            message="Cartão já está ativo.",
            error="BUSINESS_RULE_VIOLATION",
        )

    cartao["status"] = CartaoStatus.ATIVO.value
    write_database(database)

    return success_response(data=cartao)


@router.put("/{cartao_id}/cancelar")
def cancelar_cartao(cartao_id: int):
    database = read_database()
    cartao = _find_cartao(database, cartao_id)

    if cartao is None:
        raise ApiError(404, "Cartão não encontrado.", "NOT_FOUND")

    if cartao["status"] == CartaoStatus.CANCELADO.value:
        raise ApiError(
            status_code=409,
            message="Cartão já está cancelado.",
            error="BUSINESS_RULE_VIOLATION",
        )

    cartao["status"] = CartaoStatus.CANCELADO.value
    write_database(database)

    return success_response(data=cartao)
