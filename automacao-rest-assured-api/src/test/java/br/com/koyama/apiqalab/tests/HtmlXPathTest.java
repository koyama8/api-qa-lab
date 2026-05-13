package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.ConsString;

import com.sun.source.tree.AssertTree;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class HtmlXPathTest extends BaseTest {

    // Topicos complementares: trabalhando com HTML e XPath com HTML

	
    @Test
	public void deveRetornar200EExtrairHtmlComoString() {
		Response response =
				given()
				    .accept("text/html")		
				.when()
				    .get("/html/estudo")
				.then() 
				    .statusCode(200)
				    .contentType(containsString("text/html"))
				    .extract()
				    .response()
				;
		
		String hmtl = response.asString();
		
		assertTrue(hmtl.contains("Pay Lab HTML"));
		assertTrue(hmtl.contains("Resumo de estudos"));
	}
    
    @Test
    public void deveValidarConteudosEsperadosDoHtml() {
    	Response response =
    			given()
    			    .accept("text/html")
    			.when()
    			    .get("/html/estudo")
    			.then() 
    			    .statusCode(200)
    		        .contentType(containsString("text/html"))	
    		        .extract()
    		        .response()
    			;
    	
    	String hmtl = response.asString();
        
    	assertTrue(hmtl.contains("Pay Lab HTML"));
    	assertTrue(hmtl.contains("Resumo de estudos"));
    	assertTrue(hmtl.contains("XPath com HTML"));

    }

    @Test
    public void deveValidarTabelaDoHtml() {
    	Response response =
    			given()
    			    .accept("text/html")
    		    .when()	
    		        .get("/html/estudo")
    		    .then()
    		        .statusCode(200)
    		        .contentType(containsString("text/html"))
    		        .extract()
    		        .response()
    			;
    	
    	String html = response.asString();
    	
    	assertTrue(html.contains("Trabalhando com HTML"));
    	assertTrue(html.contains("XPath com HTML"));
    }

    @Test
    public void deveEstudarXpathPorIdNoHtml() {
    	Response response =
    			given()
    			    .accept("text/html")
    			.when()
    			    .get("/html/clientes")
    			.then()
    			    .statusCode(200)
    			    .contentType(containsString("text/html"))
    			    .extract()
    			    .response()
    			;
    	
    	String html = response.asString();
    			
    	assertTrue(html.contains("id=\"titulo-clientes\""));
    	assertTrue(html.contains("id=\"tabela-clientes\""));
    }

    @Test
    public void deveEstudarXpathPorTextoNoHtmlClientes() {
    	Response response = 
    			given()
    			    .accept("text/html")
    			.when()
    			    .get("/html/clientes")
    			.then()
    			    .statusCode(200)
    			    .contentType(containsString("text/html"))
    			    .extract()
    			    .response()
    			;
    
    	String html = response.asString();
    	
    	assertTrue(html.contains("Clientes para XPath"));
    	assertTrue(html.contains("Cliente Pay Lab"));
    	assertTrue(html.contains("Ana Silva"));
    }

    @Test
    public void deveEstudarXpathPorAtributoNoHtmlClientes() {
    	Response response =
    			given()
    			    .accept("text/html")
    			.when()
    			    .get("/html/clientes")
    			.then()
    			    .statusCode(200)
    			    .contentType(containsString("text/html"))
    	            .extract()
    	            .response()
    			;
    	
    	String html = response.asString();
    	
    	assertTrue(html.contains("data-cliente-id=\"1\""));
    	assertTrue(html.contains("data-status=\"ATIVO\""));

    }

    @Test
    public void deveValidarConteudosDaPrimeiraLinhaParaEstudoDeXpathPorPosicao() {
    	Response response = 
    			given()
    			    .accept("text/html")    		
    			.when()
    			    .get("/html/clientes")
    			.then()
    			    .statusCode(200)
    			    .contentType(containsString("text/html"))
    			    .extract()
    			    .response()
    			    ;
    	String html = response.asString();
    	
    	assertTrue(html.contains("id=\"corpo-tabela-clientes\""));
    	assertTrue(html.contains("id=\"cliente-1-id\""));
    	assertTrue(html.contains("Cliente Pay Lab"));
    	assertTrue(html.contains("cliente@email.com"));
    	assertTrue(html.contains("ATIVO"));    	
    }
}
