package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HeadersTest extends BaseTest {

	@Test
	public void deveRetornar200ComHeadersValidos() {
        given()
            .header("x-canal", "bruno")
            .header("x-api-version", "1")
            .header("x-request-id", "bruno-req-001")
            .header("Accept", "application/json")
        .when()
            .get("/headers")
        .then() 
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.api_version", equalTo("1"))
            .header("X-API-Version", "1")
        ;
	}
	
	@Test
	public void deveRetornar400QuandoVersaoHeaderForInvalida() {
	    given()
	        .header("x-canal", "bruno")
	        .header("x-api-version", "2")
	        .header("x-request-id", "bruno-req-001")
	        .header("Accept", "application/json")
	    .when()
	        .get("/headers")
	    .then()
	        .statusCode(400)
	        .body("success", equalTo(false))
	        .body("message", equalTo("Header x-api-version inválido. Use o valor 1."))
	        .body("error", equalTo("VALIDATION_ERROR"));
	}
}
