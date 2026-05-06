package br.com.koyama.apiqalab.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class PagamentoPayload {

    @JsonProperty("fatura_id")
    private Integer faturaId;
    private BigDecimal valor;

    public PagamentoPayload() {
    }

    public PagamentoPayload(Integer faturaId, BigDecimal valor) {
        this.faturaId = faturaId;
        this.valor = valor;
    }

    public Integer getFaturaId() {
        return faturaId;
    }

    public void setFaturaId(Integer faturaId) {
        this.faturaId = faturaId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
