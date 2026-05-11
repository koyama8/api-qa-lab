package br.com.koyama.apiqalab.tests;

import org.junit.jupiter.api.Test;

import br.com.koyama.apiqalab.base.BaseTest;
import br.com.koyama.apiqalab.payloads.ClientePayload;
import br.com.koyama.apiqalab.utils.ResetUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ClientesTest extends BaseTest {

	@Test
	public void deveRetornar200AoBuscarClientes() {
		given()
		 .when()
		  .get("/clientes")
		 .then()
		  .statusCode(200)
		;
	}

	@Test
	public void deveRetornar200AoBuscarClienteExistente() {
		given()
		 .when()
		  .get("/clientes/1")
		 .then()
		  .statusCode(200)
		 ;
	}
	
	@Test
	public void deveRetornar404ClienteInexistente() {
		given()
		 .when()
		   .get("/clientes/999")
		 .then()
		   .statusCode(404)
		;
	}

	@Test
	public void deveRetornar201AoCadastrarClienteValido() {
		given()
		   .body(clienteValido())
		 .when()
		   .post("/clientes")
		 .then()
		   .statusCode(201)
		;
	}

	@Test
	public void deveRetornar400EmailInvalido() {
		ClientePayload body = new ClientePayload();
		body.setNome("Cliente Pay Lab");
		body.setEmail("matheus");
		body.setCpf("12345678900");
		
		given()
          .body(body)
         .when()
          .post("/clientes")
         .then()  
          .statusCode(400)
          .body("message", equalTo("E-mail inválido. Informe um endereço de e-mail válido."))
		;
	}
	
    @Test
	public void deveRetornar400CPFInvalido() {
		ClientePayload body = new ClientePayload();
		body.setNome("Cliente Pay Lab");
		body.setEmail("matheus@email.com");
		body.setCpf("abc");
		
		given()
		 .body(body)
		.when()
		 .post("/clientes")
		.then()
		 .statusCode(400)
		 .body("message", equalTo("CPF inválido. Informe 11 dígitos numéricos."))
		 ;
	}

    @Test
	public void deveRetornar200AoAtualizarClienteValido() {

	    ClientePayload body = new ClientePayload();
	    body.setNome("Cliente Atualizado");
	    body.setEmail("cliente.atualizado@qa.com");
	    body.setCpf("52998224725");

	    given()
	        .body(body)
	    .when()
	        .put("/clientes/1")
	    .then()
	        .statusCode(200);
	}

    @Test
	public void deveRetornar404AoDeletarClienteNaoEncontrado() {
		given()
		 .when()
		   .delete("/clientes/4")
		 .then()
		   .statusCode(404)
		   .body("message", equalTo("Cliente não encontrado."))
		;
	}
    
    @Test
    public void deveRetornar204AoDeletarClienteValido() {
      ResetUtils.resetarMassaDeDados();
    	  given()
    	   .when()
    	     .delete("/clientes/1")
    	   .then() 
    	     .statusCode(204)
    	  ;
    }
    
    @Test
    public void deveRetornar200AoBuscarClienteExistenteComPathParam() {
        given()
            .pathParam("clienteId", 1)
        .when()
            .get("/clientes/{clienteId}")
        .then()
            .statusCode(200);
    }
    
    @Test
    public void deveRetornar200AoBuscarClientesAtivosComQueryParam() {
    	   given()
    	       .queryParam("ativo", false)
    	   .when()
	       .get("/clientes")
	   .then() 
	       .statusCode(200)
    	   ;
    }
    
    @Test
    public void deveRetornar200AoBuscarClientesFalseComQuery() {
    	    given()
    	        .queryParam("ativo", false)
    	    .when()
    	        .get("/clientes")
    	    .then()
    	        .statusCode(200)
    	        .body("message", equalTo("Operação realizada com sucesso"))
    	    ;
    }
    
    @Test
    public void deveRetornar200AoBuscarClientesPorNomeComQueryParam() {
    	   given()
    	       .queryParam("nome", "Ana")
    	   .when()
    	       .get("/clientes")
    	   .then() 	   
           .statusCode(200) 
           .body("message", equalTo("Operação realizada com sucesso"))
    	   ;
    }
    
    @Test
    public void deveRetornar404AoBuscarClientesInexistentePorNomeComQueryParam() {
    	   given()
    	       .queryParam("nome", "Matheus")
    	   .when()
    	       .get("/clientes")
    	   .then()
    	       .statusCode(404)
    	       .body("message", equalTo("Nenhum cliente encontrado para os filtros informados."))
    	   ;
    }

    
    

	private ClientePayload clienteValido() {
		ClientePayload body = new ClientePayload();
		body.setNome("Cliente Pay Lab");
		body.setEmail("matheus@email.com");
		body.setCpf("12345678900");
		return body;
	}
   
}
