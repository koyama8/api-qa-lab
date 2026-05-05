from fastapi import APIRouter

from app.services.storage import read_database
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


@router.get("/faturas")
def listar_faturas():
    database = read_database()
    return success_response(data=database["faturas"])


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
