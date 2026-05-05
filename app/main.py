from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse
from fastapi.staticfiles import StaticFiles

from pathlib import Path

from app.routes import auth, cartoes, clientes, faturas, pagamentos
from app.services.storage import reset_database
from app.utils.error_handlers import register_exception_handlers
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


app.include_router(auth.router)
app.include_router(clientes.router)
app.include_router(cartoes.router)
app.include_router(faturas.router)
app.include_router(pagamentos.router)
