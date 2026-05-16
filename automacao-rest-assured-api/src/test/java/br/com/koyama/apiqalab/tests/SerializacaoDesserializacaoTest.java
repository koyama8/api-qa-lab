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
class SerializacaoDesserializacaoTest extends BaseTest {

		@Test
		void deveSerializarMapParaJsonComSucesso() {
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

		@Test
		void deveSerializarObjetoJavaParaJsonComSucesso() {
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

		@Test
		void deveDesserializarJsonParaObjetoJava() {
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
	
	    @Test
		void deveSerializarXmlClienteComSucesso() {
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
	
	     @Test
     void deveDesserializarXmlCliente() {
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
