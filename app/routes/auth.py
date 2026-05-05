from fastapi import APIRouter

from app.schemas.auth import LoginRequest
from app.utils.exceptions import ApiError
from app.utils.responses import success_response

router = APIRouter(tags=["Auth"])

VALID_USER = "admin"
VALID_PASSWORD = "123456"
FAKE_TOKEN = "fake-token-qa-lab-123456"


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
