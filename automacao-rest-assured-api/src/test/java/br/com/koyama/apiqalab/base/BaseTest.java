package br.com.koyama.apiqalab.base;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    protected static final String BASE_URL = "http://localhost:8000";

    @BeforeEach
    protected void configurarRestAssured() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.requestSpecification = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }
}
