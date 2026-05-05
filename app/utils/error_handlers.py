from fastapi import FastAPI, Request
from fastapi.exceptions import RequestValidationError
from starlette.exceptions import HTTPException as StarletteHTTPException

from app.utils.exceptions import ApiError
from app.utils.responses import error_response


def register_exception_handlers(app: FastAPI) -> None:
    @app.exception_handler(ApiError)
    async def api_error_handler(_request: Request, exception: ApiError):
        return error_response(
            message=exception.message,
            error=exception.error,
            status_code=exception.status_code,
        )

    @app.exception_handler(RequestValidationError)
    async def validation_error_handler(
        _request: Request,
        exception: RequestValidationError,
    ):
        return error_response(
            message=_validation_message(exception),
            error="VALIDATION_ERROR",
            status_code=400,
        )

    @app.exception_handler(StarletteHTTPException)
    async def http_error_handler(_request: Request, exception: StarletteHTTPException):
        status_code = exception.status_code

        if status_code == 404:
            message = "Endpoint não encontrado."
            error = "NOT_FOUND"
        elif status_code == 405:
            message = "Método não permitido para este endpoint."
            error = "METHOD_NOT_ALLOWED"
        else:
            message = str(exception.detail)
            error = "HTTP_ERROR"

        return error_response(message=message, error=error, status_code=status_code)


def _validation_message(exception: RequestValidationError) -> str:
    first_error = exception.errors()[0]
    error_type = str(first_error.get("type", ""))
    field_name = _field_name(first_error.get("loc", []))

    if error_type == "json_invalid":
        return "JSON inválido no corpo da requisição."

    if error_type == "missing":
        return f"Campo obrigatório ausente: {field_name}."

    if field_name == "email":
        return "E-mail inválido. Informe um endereço de e-mail válido."

    if field_name == "cpf":
        return "CPF inválido. Informe 11 dígitos numéricos."

    if "greater_than" in error_type:
        return f"Campo {field_name} deve ser maior que zero."

    message = str(first_error.get("msg", "Dados inválidos."))
    return message.replace("Value error, ", "")


def _field_name(location) -> str:
    ignored = {"body", "query", "path"}
    parts = [str(part) for part in location if part not in ignored]
    return parts[-1] if parts else "campo"
