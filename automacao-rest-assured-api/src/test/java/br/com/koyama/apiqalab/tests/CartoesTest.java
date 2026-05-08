package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;
import br.com.koyama.apiqalab.payloads.CartaoPayload;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class CartoesTest extends BaseTest {

	@Test
	public void deveRetornar200AoBuscarCartoes() {
		given()
		 .when()
		     .get("/cartoes")
		 .then()
		     .statusCode(200)
		;
	}
    @Test
	public void deveRetornar200AoBuscarCartaoExistente() {
		given()
		 .when()
		     .get("/cartoes/1")
		 .then()
		     .statusCode(200)
		;
	}
    
    @Test
    public void deveRetornar404AoBuscarCartaoInexistente() {
    	given()
    	 .when()
    	     .get("/cartoes/4")
    	 .then()
    	     .statusCode(404)
    	     .body("message", equalTo("Cartão não encontrado."))
    	;
    }
    @Test
    public void deveRetornar201CartaoValido() {
        given()
         .body(cartaoValido())
        .when()
          .post("/cartoes")
        .then()
          .statusCode(201)
          .body("message", equalTo("Operação realizada com sucesso"))
         
         ;
    }
    
    @Test
    public void deveRetornar200CartaoBloqueado() {
    	given()
    	 .when()
    	     .put("/cartoes/1/bloquear")
    	 .then()    
    	     .statusCode(200)
    	     .body("message", equalTo("Operação realizada com sucesso"))
    	;
    }
    
    // TODO: implementar teste PUT /cartoes/{id}/desbloquear retornando 200
    @Test
    public void deveRetornar200DesbloquearCartao() {
    	given()
    	  .when()
    	     .put("/cartoes/1/desbloquear")
    	  .then()
    	     .statusCode(200)
    	     .body("message", equalTo("Operação realizada com sucesso"))
    	     .body("data.status", equalTo("ATIVO"))
    	;
    }
    // TODO: implementar teste PUT /cartoes/{id}/cancelar retornando 200
    @Test
    public void deveRetornar200CartaoCancelado() {
    	given()
    	  .when()
    	     .put("/cartoes/1/cancelar")
    	  .then()
    	     .statusCode(200)
    	     .body("message", equalTo("Operação realizada com sucesso"))
        ;
    }

    @Test
    public void deveRetornar409CartaoCanceladoNaoPodeSerRetornado() {
    	given()
    	  .when()
    	     .put("/cartoes/3/bloquear")
    	  .then()
    	     .statusCode(409)
    	     .body("message", equalTo("Cartão cancelado não pode ser bloqueado."))
    	  ;
    }
    
    private CartaoPayload cartaoValido() {
    	CartaoPayload body = new CartaoPayload();
    	body.setClienteId(1);
    	body.setNumeroMasked("**** **** **** 12346");
    	body.setLimite(new BigDecimal(5000));
        return body;
    }
    
 
}
