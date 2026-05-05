from pydantic import BaseModel, Field, field_validator


class LoginRequest(BaseModel):
    usuario: str = Field(..., min_length=1, examples=["admin"])
    senha: str = Field(..., min_length=1, examples=["123456"])

    @field_validator("usuario", "senha")
    @classmethod
    def nao_deve_ser_vazio(cls, value: str) -> str:
        value = value.strip()
        if not value:
            raise ValueError("Campo obrigatório não pode ser vazio.")
        return value
