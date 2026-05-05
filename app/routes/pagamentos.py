from decimal import Decimal

from fastapi import APIRouter

from app.schemas.fatura import FaturaStatus
from app.schemas.pagamento import PagamentoCreate, PagamentoStatus
from app.services.storage import next_id, read_database, write_database
from app.utils.exceptions import ApiError
from app.utils.responses import success_response

router = APIRouter(prefix="/pagamentos", tags=["Pagamentos"])


def _find_fatura(database: dict, fatura_id: int) -> dict | None:
    return next(
        (fatura for fatura in database["faturas"] if fatura["id"] == fatura_id),
        None,
    )


def _to_money(value) -> Decimal:
    return Decimal(str(value)).quantize(Decimal("0.01"))


@router.post("")
def criar_pagamento(payload: PagamentoCreate):
    database = read_database()
    fatura = _find_fatura(database, payload.fatura_id)

    if fatura is None:
        raise ApiError(
            status_code=404,
            message="Fatura não encontrada.",
            error="NOT_FOUND",
        )

    if fatura["status"] == FaturaStatus.PAGA.value:
        raise ApiError(
            status_code=409,
            message="Fatura já foi paga e não pode ser paga novamente.",
            error="BUSINESS_RULE_VIOLATION",
        )

    if _to_money(payload.valor) != _to_money(fatura["valor"]):
        raise ApiError(
            status_code=400,
            message="Valor do pagamento deve ser igual ao valor da fatura.",
            error="VALIDATION_ERROR",
        )

    novo_pagamento = {
        "id": next_id(database["pagamentos"]),
        "fatura_id": payload.fatura_id,
        "valor": float(payload.valor),
        "status": PagamentoStatus.APROVADO.value,
    }

    fatura["status"] = FaturaStatus.PAGA.value
    database["pagamentos"].append(novo_pagamento)
    write_database(database)

    return success_response(data=novo_pagamento)
