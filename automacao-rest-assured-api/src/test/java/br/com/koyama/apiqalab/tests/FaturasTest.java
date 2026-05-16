package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;
import br.com.koyama.apiqalab.payloads.FaturaPayload;
import br.com.koyama.apiqalab.utils.ResetUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FaturasTest extends BaseTest {

    @BeforeEach
    void resetarDados() {
        ResetUtils.resetarMassaDeDados();
    }

	@Test
	void deveRetornar200AoListarFatura() {
		given()
		 .when()
		     .get("/faturas")
		 .then()
		     .statusCode(200)
		;
	}
	
	@Test
	void deveRetornar200AoBuscarFaturaPorId() {
		given()
		 .when()
		     .get("/faturas/1")
		 .then()
		     .statusCode(200)
		;
	}
	
	@Test
	void deveRetornar404AoBuscarFaturaNaoEncontrada() {
		given()
		 .when()
		     .get("/faturas/999")
		 .then() 
		     .statusCode(404)
		;
	}
	
	@Test
	void deveRetornar201AoCriarFatura() {
		given()
		 .body(faturaPayload())
		.when()
		  .post("/faturas")
		.then()
		  .statusCode(201)
		;
	}

	@Test
	void deveRetornar200AoAtualizarFatura() {
		given()
		  .body(atualizarFatura())
	    .when()
		     .put("/faturas/1")
		.then() 
		     .statusCode(200)
		;
	}
	
	@Test
	void deveRetornar200AoAtualizarValorDaFatura() {
		given()
		  .body(atualizarValorFatura())
		.when()
           .put("/faturas/1")
        .then() 
           .statusCode(200)
		;
	}
	
	@Test
	void deveRetornar204AoDeletarFatura() {
		given()
		 .when() 
		     .delete("/faturas/2")
		 .then()
		     .statusCode(204)
		;
	}
	
	@Test
	void deveRetornar404AoDeletarFaturaNaoEncontrada() {
		given()
		 .when()
		     .delete("/faturas/999")
		 .then()
		     .statusCode(404)
		     .body("message", equalTo("Fatura não encontrada."))
		;
	}
	
	@Test
	void deveRetornar200AoBuscarFaturasPorStatusComQueryParam() {
		given()
		    .queryParam("status", "ABERTA")
		.when()
	        .get("/faturas")	
	    .then() 
	        .statusCode(200)		
		;
	}
	
	@Test
	void deveRetornar200AoBuscarFaturasPorClienteECartaoComQueryParam() {
		given()
		    .queryParam("cliente_id", 1)
		    .queryParam("cartao_id", 1)
		.when()    
		    .get("/faturas")
		.then()
		    .statusCode(200)
		    .body("success", equalTo(true))
		    .body("message", equalTo("Operação realizada com sucesso"))
		;
	}
	
	@Test
	void deveRetornar404AoBuscarFaturasInexistentesPorClienteComQueryParam() {
		given()
		    .queryParam("cliente_id", 5)
		    
		.when()
		    .get("/faturas")
		.then()
		    .statusCode(404)
		    .body("message", equalTo("Nenhuma fatura encontrada para os filtros informados."))
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
	
	private FaturaPayload atualizarValorFatura() {
		FaturaPayload body = new FaturaPayload();
		body.setValor(new BigDecimal(8000));
		return body;
	}
}
