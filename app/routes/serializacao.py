from xml.etree import ElementTree

from fastapi import APIRouter, Body, Request
from fastapi.responses import Response

from app.schemas.cliente import ClienteCreate
from app.utils.exceptions import ApiError
from app.utils.responses import success_response

router = APIRouter(prefix="/serializacao", tags=["Serializacao"])


@router.post(
    "/json/map",
    summary="Serializar Map para JSON",
    description=(
        "Endpoint didatico para estudar envio de Map no REST Assured. "
        "No Java, um Map<String, Object> pode ser enviado no body e serializado como JSON."
    ),
)
def serializar_map_json(
    payload: dict = Body(
        ...,
        examples=[
            {
                "origem": "rest-assured",
                "ativo": True,
                "quantidade": 3,
            }
        ],
    )
):
    return success_response(
        data={
            "tipo": "MAP_JSON",
            "recebido": payload,
        }
    )


@router.post(
    "/json/cliente",
    summary="Serializar objeto Java para JSON",
    description=(
        "Endpoint didatico para estudar envio de objeto Java no REST Assured. "
        "Use uma payload class, como ClientePayload, para gerar o JSON."
    ),
)
def serializar_objeto_json(payload: ClienteCreate):
    return success_response(
        data={
            "tipo": "OBJETO_JSON",
            "cliente": {
                "nome": payload.nome,
                "email": str(payload.email),
                "cpf": payload.cpf,
            },
        }
    )


@router.get(
    "/json/cliente",
    summary="Desserializar resposta JSON para objeto",
    description=(
        "Endpoint didatico para estudar resposta JSON sendo convertida para objeto Java."
    ),
)
def desserializar_json_cliente():
    return success_response(
        data={
            "id": 1,
            "nome": "Cliente Serializacao",
            "email": "serializacao@email.com",
            "cpf": "12345678900",
            "ativo": True,
        }
    )


@router.post(
    "/xml/cliente",
    summary="Serializar XML",
    description=(
        "Endpoint didatico para estudar envio de body XML e resposta XML. "
        "Envie um XML com as tags nome, email e cpf."
    ),
    response_class=Response,
    responses={
        200: {
            "description": "XML recebido e retornado com sucesso.",
            "content": {
                "application/xml": {
                    "example": (
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        "<cliente><nome>Cliente XML</nome>"
                        "<email>xml@email.com</email>"
                        "<cpf>12345678900</cpf><origem>XML</origem></cliente>"
                    )
                }
            },
        },
        400: {
            "description": "XML invalido.",
            "content": {
                "application/json": {
                    "example": {
                        "success": False,
                        "message": "XML invalido no corpo da requisicao.",
                        "error": "VALIDATION_ERROR",
                    }
                }
            },
        },
    },
    openapi_extra={
        "requestBody": {
            "content": {
                "application/xml": {
                    "example": (
                        "<cliente><nome>Cliente XML</nome>"
                        "<email>xml@email.com</email>"
                        "<cpf>12345678900</cpf></cliente>"
                    )
                }
            },
            "required": True,
        }
    },
)
async def serializar_xml_cliente(request: Request):
    body = await request.body()

    try:
        root = ElementTree.fromstring(body)
    except ElementTree.ParseError as exception:
        raise ApiError(
            status_code=400,
            message="XML invalido no corpo da requisicao.",
            error="VALIDATION_ERROR",
        ) from exception

    nome = root.findtext("nome") or ""
    email = root.findtext("email") or ""
    cpf = root.findtext("cpf") or ""

    if not nome or not email or not cpf:
        raise ApiError(
            status_code=400,
            message="XML deve conter nome, email e cpf.",
            error="VALIDATION_ERROR",
        )

    xml = (
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        "<cliente>"
        f"<nome>{nome}</nome>"
        f"<email>{email}</email>"
        f"<cpf>{cpf}</cpf>"
        "<origem>XML</origem>"
        "</cliente>"
    )
    return Response(content=xml, media_type="application/xml")


@router.get(
    "/xml/cliente",
    summary="Desserializar resposta XML para objeto",
    description=(
        "Endpoint didatico para estudar resposta XML sendo lida ou convertida no Java."
    ),
    response_class=Response,
    responses={
        200: {
            "description": "XML para leitura no REST Assured.",
            "content": {
                "application/xml": {
                    "example": (
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        "<cliente><id>1</id><nome>Cliente XML</nome>"
                        "<email>xml@email.com</email><cpf>12345678900</cpf>"
                        "<ativo>true</ativo></cliente>"
                    )
                }
            },
        }
    },
)
def desserializar_xml_cliente():
    xml = (
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        "<cliente>"
        "<id>1</id>"
        "<nome>Cliente XML</nome>"
        "<email>xml@email.com</email>"
        "<cpf>12345678900</cpf>"
        "<ativo>true</ativo>"
        "</cliente>"
    )
    return Response(content=xml, media_type="application/xml")
