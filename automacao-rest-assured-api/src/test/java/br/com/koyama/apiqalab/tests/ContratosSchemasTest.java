package br.com.koyama.apiqalab.tests;

import org.junit.jupiter.api.Test;

import br.com.koyama.apiqalab.base.BaseTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
class ContratosSchemasTest extends BaseTest {

	    @Test
		void deveValidarContratoJsonDoHealth() {
			given()
	        .when()
	            .get("/contratos/json/health")
        .then()
            .statusCode(200)
            .body("data.status", equalTo("UP"))
            .body(matchesJsonSchemaInClasspath("schemas/health.schema.json"))
		;
	}
    @Test
    void deveValidarContratoJsonDoCliente() {
        given()
        .when()
            .get("/contratos/json/cliente")
        .then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/cliente.schema.json"))
    	    ;
    }
	    @Test
    void deveValidarXML() {
    	   given()
    	   .when()
    	       .get("contratos/xml/cliente")
    	   .then()
    	       .statusCode(200)
    	       .body(matchesXsdInClasspath("schemas/cliente.xsd"))
    	   ;
    }
    
    @Test
    void deveValidarFuncional() {
    	given()
    	.when()
    	    .get("/contratos/diferenca")
    	.then()
    	    .statusCode(200)
    	    .body("data.validacao_funcional.objetivo", equalTo("Validar comportamento, regra de negocio e valores."))
    	    .body("data.validacao_funcional.exemplo", equalTo("Verificar se data.status e igual a UP."))
    	    .body("data.validacao_contratual.objetivo", equalTo("Validar estrutura, campos obrigatorios e tipos."))
    	    .body("data.validacao_contratual.exemplo", equalTo("Verificar se success e boolean e data.status e string."))


    	;
    }

}
