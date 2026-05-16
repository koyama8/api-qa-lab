package br.com.koyama.apiqalab.tests;

import org.junit.jupiter.api.Test;

import br.com.koyama.apiqalab.base.BaseTest;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class HealthTest extends BaseTest {

	   @Test
	    void deveRetornarApiDisponivel() {
	       given()
	        .when()
	             .get("/health")
	        .then()
	             .statusCode(200)
	             .body("success", equalTo(true))
                 .body("data.status", equalTo("UP"))
	             ;
	    }
}
