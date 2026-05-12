package br.com.koyama.apiqalab.tests;

import org.junit.jupiter.api.Test;

import br.com.koyama.apiqalab.base.BaseTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
public class ContratosSchemasTest extends BaseTest {

    
    // TODO: implementar validacao de schema JSON no endpoint GET /contratos/json/health.
    @Test
	public void deveValidarContratoJsonDoHealth() {
		given()
        .when()
            .get("http://127.0.0.1:8000/contratos/json/health")
        .then()
            .statusCode(200)
            .body("data.status", equalTo("UP"))
            .body(matchesJsonSchemaInClasspath("schemas/health.schema.json"))
		;
	}

    // TODO: implementar validacao de schema JSON no endpoint GET /contratos/json/cliente.
    @Test
    public void deveValidarContratoJsonDoCliente() {
    	    given()
    	    .when()
            .get("http://127.0.0.1:8000/contratos/json/cliente")   
        .then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/cliente.schema.json"))
    	    ;
    }
    // TODO: implementar validacao de schema XML no endpoint GET /contratos/xml/cliente.
    @Test
    public void deveValidarXML() {
    	   given()
    	   .when()
    	       .get("contratos/xml/cliente")
    	   .then()
    	       .statusCode(200)
    	       .body(matchesXsdInClasspath("schemas/cliente.xsd"))
    	   ;
    }
    
    @Test
    public void deveValidarFuncional() {
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
