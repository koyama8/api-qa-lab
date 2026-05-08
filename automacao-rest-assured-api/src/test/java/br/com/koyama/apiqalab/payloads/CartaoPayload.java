package br.com.koyama.apiqalab.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class CartaoPayload {

    @JsonProperty("cliente_id")
    private Integer clienteId;

    @JsonProperty("numero_masked")
    private String numeroMasked;

    private BigDecimal limite;

    public CartaoPayload() {
    }

    public CartaoPayload(Integer clienteId, String numeroMasked, BigDecimal limite) {
        this.clienteId = clienteId;
        this.numeroMasked = numeroMasked;
        this.limite = limite;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getNumeroMasked() {
        return numeroMasked;
    }

    public void setNumeroMasked(String numeroMasked) {
        this.numeroMasked = numeroMasked;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }
}
