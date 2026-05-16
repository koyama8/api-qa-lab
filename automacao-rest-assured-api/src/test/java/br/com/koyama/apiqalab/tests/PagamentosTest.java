package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;
import br.com.koyama.apiqalab.payloads.PagamentoPayload;
import br.com.koyama.apiqalab.utils.ResetUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PagamentosTest extends BaseTest {

    @BeforeEach
    void resetarDados() {
        ResetUtils.resetarMassaDeDados();
    }

    @Test
    void deveRetornar200AoRealizarPagamentoValido() {
        given()
            .body(pagamentoValido())
        .when()
            .post("/pagamentos")
        .then()
            .statusCode(200)
            .body("success", equalTo(true))
            .body("message", equalTo("Operação realizada com sucesso"))
            .body("data.fatura_id", equalTo(1))
            .body("data.valor", equalTo(1200.5F))
            .body("data.status", equalTo("APROVADO"))
        ;
    }

    @Test
    void deveAlterarStatusDaFaturaParaPagaAposPagamentoValido() {
        given()
            .body(pagamentoValido())
        .when()
            .post("/pagamentos")
        .then()
            .statusCode(200)
        ;

        given()
        .when()
            .get("/faturas/1")
        .then()
            .statusCode(200)
            .body("data.status", equalTo("PAGA"))
        ;
    }

    @Test
    void deveRetornar400AoPagarComValorDivergente() {
        given()
            .body(pagamentoComValorDivergente())
        .when()
            .post("/pagamentos")
        .then()
            .statusCode(400)
            .body("success", equalTo(false))
            .body("message", equalTo("Valor do pagamento deve ser igual ao valor da fatura."))
            .body("error", equalTo("VALIDATION_ERROR"))
        ;
    }

    @Test
    void deveRetornar409AoPagarFaturaJaPaga() {
        given()
            .body(pagamentoFaturaJaPaga())
        .when()
            .post("/pagamentos")
        .then()
            .statusCode(409)
            .body("success", equalTo(false))
            .body("message", equalTo("Fatura já foi paga e não pode ser paga novamente."))
            .body("error", equalTo("BUSINESS_RULE_VIOLATION"))
        ;
    }

    private PagamentoPayload pagamentoValido() {
        PagamentoPayload body = new PagamentoPayload();
        body.setFaturaId(1);
        body.setValor(new BigDecimal("1200.50"));
        return body;
    }

    private PagamentoPayload pagamentoComValorDivergente() {
        PagamentoPayload body = new PagamentoPayload();
        body.setFaturaId(1);
        body.setValor(new BigDecimal("10.00"));
        return body;
    }

    private PagamentoPayload pagamentoFaturaJaPaga() {
        PagamentoPayload body = new PagamentoPayload();
        body.setFaturaId(3);
        body.setValor(new BigDecimal("300.00"));
        return body;
    }
}
