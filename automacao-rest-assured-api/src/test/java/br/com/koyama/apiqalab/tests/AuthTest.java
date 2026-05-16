package br.com.koyama.apiqalab.tests;

import org.junit.jupiter.api.Test;

import br.com.koyama.apiqalab.base.BaseTest;
import br.com.koyama.apiqalab.payloads.LoginPayload;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class AuthTest extends BaseTest {


	@Test
	void deveRetornar200NoLoginValido() {
		given()
		  .body(criarLoginValido())
		.when()
		  .post("/login")
		.then()
		  .statusCode(200)
		;
	}

	@Test
	void deveRetornarTokenNoLoginValido() {
	    given()
	      .body(criarLoginValido())
	    .when()
	      .post("/login")
	    .then()
	      .statusCode(200)
	      .body("data.token", equalTo("fake-token-qa-lab-123456"))
	    ;
	}


	@Test
	void deveRetornar401NoLoginInvalido() {
		given()
		  .body(criarLoginInvalido())
		.when()
		  .post("/login")
		.then()
		  .statusCode(401)
		;
	}


	@Test
	void deveRetornarMensagemNoLoginInvalido() {
		 given()
		   .body(criarLoginInvalido())
		 .when()
		   .post("/login")
		 .then()
		   .statusCode(401)
		   .body("message", equalTo("Usuário ou senha inválidos."))
		 ;
	}

	@Test
	void deveRetornar200NoLoginValidoComContentTypeEAcceptJson() {
         given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(criarLoginValido())
         .when()
             .post("/login")
         .then()
             .statusCode(200)
             .body("success", equalTo(true))
             .body("data.token", equalTo("fake-token-qa-lab-123456"))
         ;
	}

	@Test
	void deveRetornar401NoLoginInvalidoComContentTypeEAcceptJson() {
		given()
		    .contentType(ContentType.JSON)
		    .accept(ContentType.JSON)
		    .body(criarLoginInvalido())
		.when()
		    .post("/login")
		.then()
		    .statusCode(401)
		    .body("message", equalTo("Usuário ou senha inválidos."))
		;
	}




	private LoginPayload criarLoginValido() {
      LoginPayload body = new LoginPayload();
      body.setUsuario("admin");
      body.setSenha("123456");
      return body;
	}

	private LoginPayload criarLoginInvalido() {
      LoginPayload body = new LoginPayload();
      body.setUsuario("admin");
      body.setSenha("12345");
      return body;
	}
}
