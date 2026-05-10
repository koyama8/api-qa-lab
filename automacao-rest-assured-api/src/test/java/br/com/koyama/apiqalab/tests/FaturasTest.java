package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;
import br.com.koyama.apiqalab.payloads.FaturaPayload;
import br.com.koyama.apiqalab.utils.ResetUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class FaturasTest extends BaseTest {

	@Test
	public void deveRetornar200AoListarFatura() {
		given()
		 .when()
		     .get("/faturas")
		 .then()
		     .statusCode(200)
		;
	}
	
	@Test
	public void deveRetornar200FaturaPorID() {
		given()
		 .when()
		     .get("/faturas/5")
		 .then()
		     .statusCode(200)
		;
	}
	
	@Test
	public void deveRetornar404FaturaNaoEncontrada() {
		given()
		 .when()
		     .get("/faturas/5")
		 .then() 
		     .statusCode(404)
		;
	}
	
	@Test
	public void deveRetornar200CriarFatura() {
		given()
		 .body(faturaPayload())
		.when()
		  .post("/faturas")
		.then()
		  .statusCode(201)
		;
	}

	@Test
	public void deveRetornar200AtualizarFatura() {
		given()
		  .body(atualizarFatura())
	    .when()
		     .put("/faturas/4")
		.then() 
		     .statusCode(200)
		;
	}
	
	@Test
	public void deveRetornar200AtualizarValorFatura() {
		given()
		  .body(atualizarValorFatura())
		.when()
           .put("/faturas/4")
        .then() 
           .statusCode(200)
		;
	}
	
	@Test
	public void deveRetornar204DeletarFatura() {
		given()
		 .when() 
		     .delete("/faturas/2")
		 .then()
		     .statusCode(204)
		;
	}
	
	@Test
	public void deveRetornar404DeleteFaturaNaoEncontrada() {
		given()
		 .when()
		     .delete("/faturas/2")
		 .then()
		     .statusCode(404)
		     .body("message", equalTo("Fatura não encontrada."))
		;
	}
	

	private FaturaPayload faturaPayload() {
		FaturaPayload body = new FaturaPayload();
		body.setClienteId(1);
		body.setCartaoId(1);
		body.setValor(new BigDecimal(5000));
		body.setStatus("ABERTA");
		body.setVencimento("2026-07-10");
		return body;
	}
	
	private FaturaPayload atualizarFatura() {
		FaturaPayload body = new FaturaPayload();
		body.setClienteId(1);
		body.setCartaoId(1);
		body.setValor(new BigDecimal(5700));
		body.setStatus("FECHADA");
		body.setVencimento("2026-07-10");
		return body;
	}
	
	private FaturaPayload atualizarCartaoFatura() {
		FaturaPayload body = new FaturaPayload();
		body.setCartaoId(5);
		return body;
	}
	
	private FaturaPayload atualizarValorFatura() {
		FaturaPayload body = new FaturaPayload();
		body.setValor(new BigDecimal(8000));
		return body;
	}
}
