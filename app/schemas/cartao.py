from enum import Enum

from pydantic import BaseModel, Field, field_validator


class CartaoStatus(str, Enum):
    ATIVO = "ATIVO"
    BLOQUEADO = "BLOQUEADO"
    CANCELADO = "CANCELADO"


class CartaoCreate(BaseModel):
    cliente_id: int = Field(..., gt=0, examples=[1])
    numero_masked: str = Field(
        ...,
        min_length=4,
        examples=["**** **** **** 1234"],
    )
    limite: float = Field(..., gt=0, examples=[5000])

    @field_validator("numero_masked")
    @classmethod
    def numero_masked_nao_deve_ser_vazio(cls, value: str) -> str:
        value = value.strip()
        if not value:
            raise ValueError("Número mascarado do cartão é obrigatório.")
        return value
