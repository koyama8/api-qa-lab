package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;
import br.com.koyama.apiqalab.payloads.CartaoPayload;
import br.com.koyama.apiqalab.utils.ResetUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartoesTest extends BaseTest {

    @BeforeEach
    public void resetarDados() {
        ResetUtils.resetarMassaDeDados();
    }

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
    public void deveRetornar201AoCadastrarCartaoValido() {
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
    public void deveRetornar200AoBloquearCartao() {
    	given()
    	 .when()
    	     .put("/cartoes/1/bloquear")
    	 .then()    
    	     .statusCode(200)
    	     .body("message", equalTo("Operação realizada com sucesso"))
    	;
    }
    
    @Test
    public void deveRetornar200AoDesbloquearCartao() {
    	given()
    	  .when()
             .put("/cartoes/2/desbloquear")
    	  .then()
    	     .statusCode(200)
    	     .body("message", equalTo("Operação realizada com sucesso"))
    	     .body("data.status", equalTo("ATIVO"))
    	;
    }

    @Test
    public void deveRetornar200AoCancelarCartao() {
    	given()
    	  .when()
    	     .put("/cartoes/1/cancelar")
    	  .then()
    	     .statusCode(200)
    	     .body("message", equalTo("Operação realizada com sucesso"))
        ;
    }

    @Test
    public void deveRetornar409AoBloquearCartaoCancelado() {
    	given()
    	  .when()
    	     .put("/cartoes/3/bloquear")
    	  .then()
    	     .statusCode(409)
    	     .body("message", equalTo("Cartão cancelado não pode ser bloqueado."))
    	  ;
    }
    
    @Test
    public void deveRetornar200AoBuscarCartoesAtivosComQueryParam() {
    	   given()
    	       .queryParam("status", "ATIVO")
    	   .when() 
    	       .get("/cartoes")
    	   .then()    
    	       .statusCode(200)
   	       .body("message", equalTo("Operação realizada com sucesso"))

    	   ;
    }
    
    @Test
    public void deveRetornar200AoBuscarCartoesBloqueadosComQueryParam() {
    	   given()
    	       .queryParam("status", "BLOQUEADO")
    	   .when()
    	        .get("/cartoes")
    	   .then() 
    	        .statusCode(200)
    	        .body("message", equalTo("Operação realizada com sucesso"))
    	   ;
    }
    
    @Test
    public void deveRetornar200AoBuscarCartoesDoClienteComQueryParam() {
    	    given()
                .queryParam("cliente_id", 1)
    	    .when()
    	        .get("/cartoes")
    	    .then()
    	        .statusCode(200)
                .body("success", equalTo(true))
    	    ;
    }
        
    @Test
    public void deveRetornar200AoBuscarNumeroCartaoDoClienteComQueryParam() {
    	    given()
    	        .queryParam("cliente_id", 2)
    	    .when()
            .get("/cartoes")
         .then()
            .statusCode(200)
            .body("data[0].numero_masked", equalTo("**** **** **** 5555"))
            .body("data[0].limite", equalTo(1000.0F))
    	    ;
    }
    
    @Test
    public void deveRetornar404AoBuscarCartoesInexistentesPorClienteComQueryParam() {
    	    given()
    	        .queryParam("cliente_id", 5)
    	    .when()    
    	        .get("/cartoes") 
    	    .then()
    	        .statusCode(404)
    	        .body("message", equalTo("Nenhum cartão encontrado para os filtros informados."))    
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
