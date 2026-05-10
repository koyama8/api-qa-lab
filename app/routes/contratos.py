from fastapi import APIRouter
from fastapi.responses import Response

from app.utils.responses import success_response

router = APIRouter(prefix="/contratos", tags=["Contratos"])


@router.get(
    "/json/health",
    summary="Contrato JSON do health",
    description=(
        "Endpoint didatico para validar contrato JSON. "
        "Use este retorno com um arquivo JSON Schema em resources."
    ),
)
def contrato_json_health():
    return success_response(
        data={
            "status": "UP",
            "service": "Pay Lab",
            "version": "1.0.0",
        }
    )


@router.get(
    "/json/cliente",
    summary="Contrato JSON de cliente",
    description=(
        "Endpoint didatico para validar contrato JSON de um objeto cliente. "
        "O foco aqui e validar tipos, campos obrigatorios e estrutura."
    ),
)
def contrato_json_cliente():
    return success_response(
        data={
            "id": 1,
            "nome": "Cliente Pay Lab",
            "email": "cliente@email.com",
            "cpf": "12345678900",
            "ativo": True,
        }
    )


@router.get(
    "/xml/cliente",
    summary="Contrato XML de cliente",
    description=(
        "Endpoint didatico para validar contrato XML com arquivo XSD. "
        "Use o header Accept: application/xml."
    ),
    responses={
        200: {
            "description": "XML de cliente para estudo de contrato.",
            "content": {
                "application/xml": {
                    "example": (
                        "<cliente><id>1</id><nome>Cliente Pay Lab</nome>"
                        "<email>cliente@email.com</email><cpf>12345678900</cpf>"
                        "<ativo>true</ativo></cliente>"
                    )
                }
            },
        }
    },
)
def contrato_xml_cliente():
    xml = (
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        "<cliente>"
        "<id>1</id>"
        "<nome>Cliente Pay Lab</nome>"
        "<email>cliente@email.com</email>"
        "<cpf>12345678900</cpf>"
        "<ativo>true</ativo>"
        "</cliente>"
    )
    return Response(content=xml, media_type="application/xml")


@router.get(
    "/diferenca",
    summary="Diferenca entre validacao funcional e contratual",
    description=(
        "Endpoint didatico para comparar validacao funcional com validacao "
        "contratual. A funcional olha regra e valor; a contratual olha estrutura."
    ),
)
def diferenca_validacao_funcional_contratual():
    return success_response(
        data={
            "validacao_funcional": {
                "objetivo": "Validar comportamento, regra de negocio e valores.",
                "exemplo": "Verificar se data.status e igual a UP.",
            },
            "validacao_contratual": {
                "objetivo": "Validar estrutura, campos obrigatorios e tipos.",
                "exemplo": "Verificar se success e boolean e data.status e string.",
            },
        }
    )
