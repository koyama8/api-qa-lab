from fastapi import APIRouter, Depends, Header
from fastapi.security import HTTPAuthorizationCredentials, HTTPBasic, HTTPBasicCredentials, HTTPBearer

from app.schemas.auth import LoginRequest
from app.utils.exceptions import ApiError
from app.utils.responses import success_response

router = APIRouter(tags=["Auth"])
basic_security = HTTPBasic(auto_error=False)
bearer_security = HTTPBearer(auto_error=False)

VALID_USER = "admin"
VALID_PASSWORD = "123456"
FAKE_TOKEN = "fake-token-qa-lab-123456"
VALID_API_KEY = "qa-lab-api-key-123"


@router.post("/login")
def login(payload: LoginRequest):
    if payload.usuario != VALID_USER or payload.senha != VALID_PASSWORD:
        raise ApiError(
            status_code=401,
            message="Usuário ou senha inválidos.",
            error="UNAUTHORIZED",
        )

    return success_response(
        data={
            "token": FAKE_TOKEN,
            "token_type": "Bearer",
            "expires_in": 3600,
        }
    )


@router.get(
    "/auth/publica",
    tags=["Autenticacoes"],
    summary="Acesso publico",
    description=(
        "Endpoint didatico para estudar acesso a API publica. "
        "Nao exige token, API Key ou Basic Auth."
    ),
)
def acesso_publico():
    return success_response(
        data={
            "tipo": "PUBLICA",
            "autenticado": False,
            "descricao": "Acesso publico sem autenticacao.",
        }
    )


@router.get(
    "/auth/api-key",
    tags=["Autenticacoes"],
    summary="Autenticacao com API Key",
    description=(
        "Endpoint didatico para estudar autenticacao por chave. "
        "Envie o header `x-api-key` com o valor `qa-lab-api-key-123`."
    ),
    responses={
        401: {
            "description": "API Key ausente ou invalida.",
            "content": {
                "application/json": {
                    "example": {
                        "success": False,
                        "message": "API Key ausente ou invalida.",
                        "error": "UNAUTHORIZED",
                    }
                }
            },
        }
    },
)
def acesso_api_key(
    x_api_key: str | None = Header(
        default=None,
        alias="x-api-key",
        description="Chave de acesso esperada para estudo de API Key.",
        examples=[VALID_API_KEY],
    )
):
    if x_api_key != VALID_API_KEY:
        raise ApiError(
            status_code=401,
            message="API Key ausente ou invalida.",
            error="UNAUTHORIZED",
        )

    return success_response(
        data={
            "tipo": "API_KEY",
            "autenticado": True,
            "api_key": "valida",
        }
    )


@router.get(
    "/auth/basic",
    tags=["Autenticacoes"],
    summary="Autenticacao Basic Auth",
    description=(
        "Endpoint didatico para estudar Basic Auth. "
        "Use usuario `admin` e senha `123456`."
    ),
    responses={
        401: {
            "description": "Credenciais Basic Auth invalidas.",
            "content": {
                "application/json": {
                    "example": {
                        "success": False,
                        "message": "Credenciais Basic Auth invalidas.",
                        "error": "UNAUTHORIZED",
                    }
                }
            },
        }
    },
)
def acesso_basic(
    credentials: HTTPBasicCredentials | None = Depends(basic_security),
):
    if (
        credentials is None
        or credentials.username != VALID_USER
        or credentials.password != VALID_PASSWORD
    ):
        raise ApiError(
            status_code=401,
            message="Credenciais Basic Auth invalidas.",
            error="UNAUTHORIZED",
        )

    return success_response(
        data={
            "tipo": "BASIC",
            "autenticado": True,
            "usuario": credentials.username,
        }
    )


@router.get(
    "/auth/bearer",
    tags=["Autenticacoes"],
    summary="Autenticacao Bearer Token",
    description=(
        "Endpoint didatico para estudar Bearer Token. "
        "Envie o header `Authorization` com "
        "`Bearer fake-token-qa-lab-123456`."
    ),
    responses={
        401: {
            "description": "Bearer Token ausente ou invalido.",
            "content": {
                "application/json": {
                    "example": {
                        "success": False,
                        "message": "Bearer Token ausente ou invalido.",
                        "error": "UNAUTHORIZED",
                    }
                }
            },
        }
    },
)
def acesso_bearer(
    credentials: HTTPAuthorizationCredentials | None = Depends(bearer_security),
):
    if (
        credentials is None
        or credentials.scheme.lower() != "bearer"
        or credentials.credentials != FAKE_TOKEN
    ):
        raise ApiError(
            status_code=401,
            message="Bearer Token ausente ou invalido.",
            error="UNAUTHORIZED",
        )

    return success_response(
        data={
            "tipo": "BEARER",
            "autenticado": True,
            "token": "valido",
        }
    )
