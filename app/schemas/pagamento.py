from decimal import Decimal
from enum import Enum

from pydantic import BaseModel, Field


class PagamentoStatus(str, Enum):
    APROVADO = "APROVADO"
    RECUSADO = "RECUSADO"


class PagamentoCreate(BaseModel):
    fatura_id: int = Field(..., gt=0, examples=[1])
    valor: Decimal = Field(..., gt=Decimal("0"), examples=[1200.50])
