package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.Matchers.lessThan;
import java.io.File;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ArquivosComportamentoTest extends BaseTest {

    // Arquivos e comportamento avancado

 	@Test
	public void deveRetornar200AoFazerUploadDeArquivo() {
	    File arquivo = new File("src/test/resources/payloads/upload-estudo.txt");

	    given()
	        .contentType("multipart/form-data")
	        .accept("application/json")
	        .multiPart("arquivo", arquivo)
	    .when()
	        .post("/arquivos/upload")
	    .then()
	        .statusCode(200)
	        .body("message", equalTo("Operação realizada com sucesso"))
	        .body("data.nome", equalTo("upload-estudo.txt"))
	        .body("data.tamanho_bytes", equalTo(103))
	        .body("data.extensao", equalTo(".txt"))
	        .body("data.salvo", equalTo(true));
	}

	
    @Test
    public void deveRetornar200AoFazerDownloadDeArquivo() {
    	Response response =
    			given()
    			    .accept("text/plain")
    			.when()
    			    .get("/arquivos/download")
    			.then()
    			    .statusCode(200)
    			    .extract()
    			    .response()
    			    ;
    			   
        String body = response.asString();
        
        assertTrue(body.contains("Pay Lab - arquivo de estudo"));
        assertTrue(body.contains("Este arquivo existe para praticar download"));
        
    }

    @Test
    public void deveRetornar200AoBuscarMetadadosDoArquivo() {
    	given()
    	    .accept("application/json")
    	.when()
    	    .get("/arquivos/metadados")
    	.then()
    	    .statusCode(200)
    	    .body("data.nome", equalTo("pay-lab-estudo.txt"))
    	    .body("data.content_type", equalTo("text/plain"))
    	    .body("data.tamanho_bytes", equalTo(146))
    	    .body("data.extensao", equalTo(".txt"))
    	    .body("data.sha256", equalTo("940beaf1ea4ae37cc05b7d967715a0b889b79314f204e817d45382b9029748b0"))
    	    .body("data.download_url", equalTo("/arquivos/download"))

    	;
    }
    
    @Test
    public void deveRetornar200DentroUmSegundo() {
    	given()
    	    .accept("application/json")
    	    .queryParam("segundos", 1)
    	.when()
    	    .get("/comportamento/delay")
    	.then()
    	    .statusCode(200)
    	    .time(lessThan(1200L))
    	    .body("success", equalTo(true))
    	    .body("data.delay_solicitado_segundos", equalTo(1.0F))
    	;
    }

    @Test
    public void deveRetornar200DentroDoTempoMaximoEsperado() {
    	given()
    	    .accept("application/json")
    	    .queryParam("segundos", 1)
    	.when()
    	    .get("/comportamento/delay")
    	.then()
    	    .statusCode(200)
    	    .time(lessThan(2000L))
    	    .body("success", equalTo(true))
    	    .body("message", equalTo("Operação realizada com sucesso"))
    	    .body("data.delay_solicitado_segundos", equalTo(1.0F))
    	;
    }
  
    @Test
    public void deveRetornar200DentroDoDelay() {
    	given()
            .accept("application/json")
            .queryParam("segundos", 5)
    	.when()
    	    .get("/comportamento/delay")
    	.then()
    	    .statusCode(200)
    	    .time(lessThan(6000L))
    	    .body("success", equalTo(true))
    	    .body("message", equalTo("Operação realizada com sucesso"))
    	;
    }
}
