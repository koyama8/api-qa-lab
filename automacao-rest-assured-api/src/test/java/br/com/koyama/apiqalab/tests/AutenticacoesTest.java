package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class AutenticacoesTest extends BaseTest {

	@Test
	void deveRetornar200AoAcessarApiPublica() {
		given()
	    .when()	
		    .get("/auth/publica")
		.then()
		    .statusCode(200)
		    .body("message", equalTo("Operação realizada com sucesso"))
		    .body("data.descricao", equalTo("Acesso publico sem autenticacao."))
	 ;
	}

    @Test
	void deveRetornar200ComApiKeyValida() {
	    given()
	        .header("x-api-key", "qa-lab-api-key-123")
	    .when() 
	        .get("/auth/api-key")
	    .then()
	        .statusCode(200)
	        .body("message", equalTo("Operação realizada com sucesso"))
	        .body("data.tipo", equalTo("API_KEY"))
	        .body("data.autenticado", equalTo(true))
	        .body("data.api_key", equalTo("valida"));
	}
    
    @Test
    void deveRetornar401APIAusente() {
    	given()
    	.when()
    	    .get("/auth/api-key")
    	.then()
    	    .statusCode(401)
    	    .body("message", equalTo("API Key ausente ou invalida."))
    	;
    }
    
    @Test
    void deveRetornar401APIKeyInvalida() {
    	given()
    	    .header("x-api-key", "123456")
    	.when()
    	    .get("/auth/api-key")
    	.then()
    	    .statusCode(401)
    	    .body("message", equalTo("API Key ausente ou invalida."))
    	;
    }
    
    @Test
    void deveRetornar200ComBasicAuthValido() {
    	given()
            .auth().preemptive().basic("admin", "123456")
    	.when()
    	    .get("/auth/basic")
    	.then()
    	    .statusCode(200)
    	;
    }
    
    @Test
    void deveRetornar401ComBasicAuthInvalido() {
    	given()
    	   .auth().preemptive().basic("admin", "123")
    	.when()
    	   .get("/auth/basic")
    	.then() 
    	   .statusCode(401)
    	   .body("message", equalTo("Credenciais Basic Auth invalidas."))
    	   
    	;
    }
    @Test
    void deveRetornar200ComBearerTokenValido() {
    	given()
    	   .auth().oauth2("fake-token-qa-lab-123456")
    	.when() 
    	   .get("/auth/bearer")
    	.then()
    	   .statusCode(200)
    	   .body("message", equalTo("Operação realizada com sucesso"))
    	   .body("data.tipo", equalTo("BEARER"))
    	;
    }
    @Test
    void deveRetornar401ComBearerTokenAusente() {
    	given()
    	.when()
    	    .get("/auth/bearer")
    	.then()
    	    .statusCode(401)
    	    .body("message", equalTo("Bearer Token ausente ou invalido."))
    	;
    }
    @Test
    void deveRetornar401ComBearerTokenInvalido() {
    	given()
    	    .auth().oauth2("fake-token-qa-lab")
    	.when() 
    	    .get("/auth/bearer")
    	.then() 
    	    .statusCode(401)
    	    .body("message", equalTo("Bearer Token ausente ou invalido."))
    	;
    }
}
