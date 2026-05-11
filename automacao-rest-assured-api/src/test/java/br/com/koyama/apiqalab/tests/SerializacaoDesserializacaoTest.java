package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;

import br.com.koyama.apiqalab.responses.ClienteResponse;
import io.restassured.path.xml.XmlPath;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class SerializacaoDesserializacaoTest extends BaseTest {

    // TODO: implementar teste POST /serializacao/json/map serializando Map para JSON
    // TODO: validar status 200, success=true e data.tipo=MAP_JSON
    // TODO: validar que os campos enviados no Map voltam em data.recebido
	
	@Test
	public void deveSerializarMapParaJsonComSucesso() {
		Map<String, Object> body = new HashMap<>();
	    body.put("origem", "bruno");
		body.put("ativo", true);
		body.put("quantidade", 3);
		
		given()
		    .body(body)
		.when() 
		    .post("/serializacao/json/map")
		 .then()
		    .statusCode(200)
		    .body("data.tipo", equalTo("MAP_JSON"))
		    .body("data.recebido.origem", equalTo("bruno"))
		    .body("data.recebido.ativo", equalTo(true))
		    .body("data.recebido.quantidade", equalTo(3))
		;
	}

    // TODO: implementar teste POST /serializacao/json/cliente serializando objeto ClientePayload para JSON
    // TODO: validar status 200, success=true e data.tipo=OBJETO_JSON
    // TODO: validar nome, email e cpf retornados no body
	
	@Test
	public void deveSerializarObjetoJavaParaJsonComSucesso() {
		ClienteResponse body = new ClienteResponse();
		body.setNome("Cliente Serializacao");
		body.setEmail("serializacao@email.com");
		body.setCpf("12345678900");
		
		given()
		    .body(body)
		.when()
		    .post("/serializacao/json/cliente")
		.then()
		    .statusCode(200)
		    .body("success", equalTo(true))
		    .body("data.cliente.nome", equalTo("Cliente Serializacao"))
		    .body("data.cliente.email",equalTo("serializacao@email.com"))
		    .body("data.cliente.cpf", equalTo("12345678900"))
		;
	}

    // TODO: implementar teste GET /serializacao/json/cliente desserializando resposta JSON para objeto Java
    // TODO: criar uma classe de resposta futuramente, se quiser praticar response.as(...)
    // TODO: comparar campos do objeto desserializado com os valores esperados
	@Test
	public void deveDesserializarJsonParaObjetoJava() {
	    ClienteResponse cliente =
	        given()
	        .when()
	            .get("/serializacao/json/cliente")
	        .then()
	            .statusCode(200)
	            .extract()
	            .jsonPath()
	            .getObject("data", ClienteResponse.class);


	    assertEquals(1, cliente.getId());
	    assertEquals("Cliente Serializacao", cliente.getNome());
	    assertEquals("serializacao@email.com", cliente.getEmail());
	    assertEquals("12345678900", cliente.getCpf());
	    assertEquals(true, cliente.getAtivo());
	}
	
    // TODO: implementar teste POST /serializacao/xml/cliente serializando XML
    // TODO: enviar body XML com Content-Type application/xml
    // TODO: validar status 200 e Content-Type application/xml
    @Test
	public void deveSerializarXmlClienteComSucesso() {
		String bodyXML = """
				<cliente>
                  <nome>Cliente XML</nome>
                  <email>xml@email.com</email>
                  <cpf>12345678900</cpf>
                  <origem>XML</origem>
                </cliente>
				""";
		
		   given()
		      .contentType("application/xml")
		      .accept("application/xml")
		      .body(bodyXML)
		   .when()
		      .post("/serializacao/xml/cliente")
		   .then()
		      .statusCode(200)
		   ;
	}
	
    // TODO: implementar teste GET /serializacao/xml/cliente desserializando resposta XML
    // TODO: ler XML com XmlPath ou response.asString()
    // TODO: validar tags id, nome, email, cpf e ativo
     @Test
     public void deveDesserializarXmlCliente() {
    	    String responseXml = 
    	    		given()
    	    		  .accept("application/xml")
    	    		.when()
    	    		  .get("/serializacao/xml/cliente")
    	    		.then()
    	    		  .statusCode(200)
    	    		  .extract()
    	    		  .asString();
    	    

    	    XmlPath xmlPath = new XmlPath(responseXml);

    	    assertEquals(1, xmlPath.getInt("cliente.id"));
    	    assertEquals("Cliente XML", xmlPath.getString("cliente.nome"));
    	    assertEquals("xml@email.com", xmlPath.getString("cliente.email"));
    	    assertEquals("12345678900", xmlPath.getString("cliente.cpf"));
    	    assertEquals(true, xmlPath.getBoolean("cliente.ativo"));
    	    
    	    

     }
}
