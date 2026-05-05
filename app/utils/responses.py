from typing import Any

from fastapi.responses import JSONResponse

DEFAULT_SUCCESS_MESSAGE = "Operação realizada com sucesso"


def success_response(
    data: Any = None,
    message: str = DEFAULT_SUCCESS_MESSAGE,
    status_code: int = 200,
) -> JSONResponse:
    return JSONResponse(
        status_code=status_code,
        content={
            "success": True,
            "message": message,
            "data": data if data is not None else {},
        },
    )


def error_response(message: str, error: str, status_code: int) -> JSONResponse:
    return JSONResponse(
        status_code=status_code,
        content={
            "success": False,
            "message": message,
            "error": error,
        },
    )
