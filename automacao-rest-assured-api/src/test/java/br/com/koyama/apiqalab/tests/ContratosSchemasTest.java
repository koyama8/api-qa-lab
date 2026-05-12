package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;

public class ContratosSchemasTest extends BaseTest {

    // Validacao de esquema / contrato

    // TODO: implementar validacao de schema JSON no endpoint GET /contratos/json/health.
    // Arquivo sugerido: schemas/health.schema.json
    // Ideia de estudo:
    // - chamar o endpoint /contratos/json/health
    // - validar status 200
    // - validar content type JSON
    // - validar o body usando o arquivo health.schema.json

    // TODO: implementar validacao de schema JSON no endpoint GET /contratos/json/cliente.
    // Arquivo sugerido: schemas/cliente.schema.json
    // Ideia de estudo:
    // - chamar o endpoint /contratos/json/cliente
    // - validar status 200
    // - validar content type JSON
    // - validar se o contrato exige id, nome, email, cpf e ativo dentro de data

    // TODO: implementar validacao de schema XML no endpoint GET /contratos/xml/cliente.
    // Arquivo sugerido: schemas/cliente.xsd
    // Ideia de estudo:
    // - chamar o endpoint /contratos/xml/cliente
    // - enviar Accept application/xml
    // - validar status 200
    // - validar content type XML
    // - validar o XML usando o arquivo cliente.xsd

    // TODO: estudar uso de arquivos em src/test/resources/schemas.
    // No REST Assured, os arquivos em resources ficam disponiveis no classpath.

    // TODO: comparar validacao funcional com validacao contratual usando GET /contratos/diferenca.
    // Funcional valida regra, comportamento e valor esperado.
    // Exemplo: validar que data.status e igual a UP.
    // Contratual valida estrutura, campos obrigatorios e tipos.
    // Exemplo: validar que success e boolean, message e string, data e objeto.

    // TODO: quando implementar JSON Schema, pesquisar:
    // matchesJsonSchemaInClasspath("schemas/health.schema.json")

    // TODO: quando implementar XML Schema, pesquisar:
    // matchesXsdInClasspath("schemas/cliente.xsd")
}
