from datetime import date
from decimal import Decimal
from enum import Enum

from pydantic import BaseModel, Field, model_validator


class FaturaStatus(str, Enum):
    ABERTA = "ABERTA"
    FECHADA = "FECHADA"
    PAGA = "PAGA"


class FaturaCreate(BaseModel):
    cliente_id: int = Field(..., gt=0, examples=[1])
    cartao_id: int = Field(..., gt=0, examples=[1])
    valor: Decimal = Field(..., gt=Decimal("0"), examples=[1200.50])
    status: FaturaStatus = Field(default=FaturaStatus.ABERTA, examples=["ABERTA"])
    vencimento: date = Field(..., examples=["2026-06-10"])


class FaturaUpdate(BaseModel):
    cliente_id: int | None = Field(default=None, gt=0)
    cartao_id: int | None = Field(default=None, gt=0)
    valor: Decimal | None = Field(default=None, gt=Decimal("0"))
    status: FaturaStatus | None = None
    vencimento: date | None = None

    @model_validator(mode="after")
    def deve_ter_ao_menos_um_campo(self):
        if (
            self.cliente_id is None
            and self.cartao_id is None
            and self.valor is None
            and self.status is None
            and self.vencimento is None
        ):
            raise ValueError("Informe ao menos um campo para atualizar a fatura.")
        return self
