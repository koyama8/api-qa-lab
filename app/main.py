from fastapi import FastAPI, Header
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse
from fastapi.staticfiles import StaticFiles

from pathlib import Path

from app.routes import auth, cartoes, clientes, faturas, pagamentos
from app.services.storage import reset_database
from app.utils.error_handlers import register_exception_handlers
from app.utils.exceptions import ApiError
from app.utils.responses import success_response

BASE_DIR = Path(__file__).resolve().parent
STATIC_DIR = BASE_DIR / "static"

app = FastAPI(
    title="Pay Lab",
    description=(
        "API de estudo inspirada em contexto bancário para testes manuais, "
        "REST Assured, Cypress e CI/CD."
    ),
    version="1.0.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)

register_exception_handlers(app)

app.mount("/static", StaticFiles(directory=STATIC_DIR), name="static")


@app.get("/", include_in_schema=False)
def home():
    return FileResponse(STATIC_DIR / "index.html")


@app.get("/health", tags=["Sistema"])
def health():
    return success_response(data={"status": "UP", "service": "Pay Lab"})


@app.post("/reset", tags=["Sistema"])
def reset():
    data = reset_database()
    return success_response(data=data)


@app.get("/headers", tags=["Sistema"])
def validar_headers(
    x_canal: str | None = Header(
        default=None,
        alias="x-canal",
        description="Canal que está chamando a API, como bruno ou rest-assured.",
    ),
    x_api_version: str = Header(
        default="1",
        alias="x-api-version",
        description="Versão esperada da API para estudo de headers.",
    ),
    x_request_id: str | None = Header(
        default=None,
        alias="x-request-id",
        description="Identificador da requisição enviado pelo cliente.",
    ),
    accept: str | None = Header(
        default=None,
        alias="accept",
        description="Formato aceito pelo cliente.",
    ),
):
    if x_api_version != "1":
        raise ApiError(
            status_code=400,
            message="Header x-api-version inválido. Use o valor 1.",
            error="VALIDATION_ERROR",
        )

    canal = x_canal or "nao-informado"
    request_id = x_request_id or "sem-request-id"

    return success_response(
        data={
            "canal": canal,
            "api_version": x_api_version,
            "request_id": request_id,
            "accept": accept,
        },
        headers={
            "X-Pay-Lab": "API-LAB-QAKOYAMA",
            "X-API-Version": x_api_version,
            "X-Canal": canal,
            "X-Request-ID": request_id,
        },
    )


app.include_router(auth.router)
app.include_router(clientes.router)
app.include_router(cartoes.router)
app.include_router(faturas.router)
app.include_router(pagamentos.router)
