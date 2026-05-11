package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;

public class SerializacaoDesserializacaoTest extends BaseTest {

    // TODO: implementar teste POST /serializacao/json/map serializando Map para JSON
    // TODO: validar status 200, success=true e data.tipo=MAP_JSON
    // TODO: validar que os campos enviados no Map voltam em data.recebido

    // TODO: implementar teste POST /serializacao/json/cliente serializando objeto ClientePayload para JSON
    // TODO: validar status 200, success=true e data.tipo=OBJETO_JSON
    // TODO: validar nome, email e cpf retornados no body

    // TODO: implementar teste GET /serializacao/json/cliente desserializando resposta JSON para objeto Java
    // TODO: criar uma classe de resposta futuramente, se quiser praticar response.as(...)
    // TODO: comparar campos do objeto desserializado com os valores esperados

    // TODO: implementar teste POST /serializacao/xml/cliente serializando XML
    // TODO: enviar body XML com Content-Type application/xml
    // TODO: validar status 200 e Content-Type application/xml

    // TODO: implementar teste GET /serializacao/xml/cliente desserializando resposta XML
    // TODO: ler XML com XmlPath ou response.asString()
    // TODO: validar tags id, nome, email, cpf e ativo
}
