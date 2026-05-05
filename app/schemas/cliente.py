from pydantic import BaseModel, EmailStr, Field, field_validator, model_validator

from app.utils.validators import normalize_cpf


class ClienteCreate(BaseModel):
    nome: str = Field(..., min_length=1, examples=["Cliente Pay Lab"])
    email: EmailStr = Field(..., examples=["matheus@email.com"])
    cpf: str = Field(..., min_length=11, max_length=14, examples=["12345678900"])

    @field_validator("nome")
    @classmethod
    def nome_nao_deve_ser_vazio(cls, value: str) -> str:
        value = value.strip()
        if not value:
            raise ValueError("Nome é obrigatório.")
        return value

    @field_validator("cpf")
    @classmethod
    def cpf_deve_ser_valido(cls, value: str) -> str:
        return normalize_cpf(value)


class ClienteUpdate(BaseModel):
    nome: str | None = Field(default=None, min_length=1)
    email: EmailStr | None = None
    cpf: str | None = Field(default=None, min_length=11, max_length=14)
    ativo: bool | None = None

    @field_validator("nome")
    @classmethod
    def nome_nao_deve_ser_vazio(cls, value: str | None) -> str | None:
        if value is None:
            return value

        value = value.strip()
        if not value:
            raise ValueError("Nome é obrigatório.")
        return value

    @field_validator("cpf")
    @classmethod
    def cpf_deve_ser_valido(cls, value: str | None) -> str | None:
        if value is None:
            return value
        return normalize_cpf(value)

    @model_validator(mode="after")
    def deve_ter_ao_menos_um_campo(self):
        if (
            self.nome is None
            and self.email is None
            and self.cpf is None
            and self.ativo is None
        ):
            raise ValueError("Informe ao menos um campo para atualizar o cliente.")
        return self
