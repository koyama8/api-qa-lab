import asyncio
import hashlib
import time
from pathlib import Path

from fastapi import APIRouter, File, Query, UploadFile
from fastapi.responses import FileResponse

from app.utils.responses import success_response

BASE_DIR = Path(__file__).resolve().parents[1]
SAMPLE_FILE = BASE_DIR / "static" / "downloads" / "pay-lab-estudo.txt"
UPLOAD_DIR = BASE_DIR / "database" / "uploads"

router = APIRouter(tags=["Arquivos e Comportamento"])


def _file_metadata(path: Path, content_type: str) -> dict:
    content = path.read_bytes()
    return {
        "nome": path.name,
        "content_type": content_type,
        "tamanho_bytes": len(content),
        "extensao": path.suffix,
        "sha256": hashlib.sha256(content).hexdigest(),
    }


@router.post(
    "/arquivos/upload",
    summary="Upload de arquivo",
    description=(
        "Endpoint didatico para estudar upload multipart/form-data no REST Assured. "
        "Envie um arquivo no campo `arquivo`."
    ),
)
async def upload_arquivo(arquivo: UploadFile = File(...)):
    UPLOAD_DIR.mkdir(parents=True, exist_ok=True)

    content = await arquivo.read()
    safe_name = Path(arquivo.filename or "arquivo-upload.txt").name
    destination = UPLOAD_DIR / safe_name
    destination.write_bytes(content)

    return success_response(
        data={
            "nome": safe_name,
            "content_type": arquivo.content_type or "application/octet-stream",
            "tamanho_bytes": len(content),
            "extensao": Path(safe_name).suffix,
            "salvo": True,
        }
    )


@router.get(
    "/arquivos/download",
    summary="Download de arquivo",
    description=(
        "Endpoint didatico para estudar download de arquivo, headers de resposta "
        "e validacao de conteudo."
    ),
)
def download_arquivo():
    return FileResponse(
        path=SAMPLE_FILE,
        media_type="text/plain",
        filename=SAMPLE_FILE.name,
    )


@router.get(
    "/arquivos/metadados",
    summary="Metadados do arquivo",
    description=(
        "Endpoint didatico para validar metadados do arquivo usado no download, "
        "como nome, Content-Type, tamanho, extensao e hash."
    ),
)
def metadados_arquivo():
    metadata = _file_metadata(SAMPLE_FILE, "text/plain")
    metadata["download_url"] = "/arquivos/download"
    return success_response(data=metadata)


@router.get(
    "/comportamento/delay",
    summary="Delay para estudo de tempo maximo",
    description=(
        "Endpoint didatico para estudar tempo de resposta e timeout. "
        "Use `segundos` entre 0 e 5."
    ),
)
async def delay_resposta(
    segundos: float = Query(
        default=1,
        ge=0,
        le=5,
        description="Tempo de espera artificial antes da resposta.",
        examples=[1],
    )
):
    start = time.perf_counter()
    await asyncio.sleep(segundos)
    elapsed_ms = round((time.perf_counter() - start) * 1000, 2)

    return success_response(
        data={
            "delay_solicitado_segundos": segundos,
            "tempo_aproximado_ms": elapsed_ms,
        }
    )
