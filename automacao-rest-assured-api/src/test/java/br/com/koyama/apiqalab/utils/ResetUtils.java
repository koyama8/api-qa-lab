package br.com.koyama.apiqalab.utils;

import static io.restassured.RestAssured.given;

public class ResetUtils {

    private ResetUtils() {
    }

    public static void resetarMassaDeDados() {
        given()
        .when()
                .post("/reset")
        .then()
                .statusCode(200);
    }
}
