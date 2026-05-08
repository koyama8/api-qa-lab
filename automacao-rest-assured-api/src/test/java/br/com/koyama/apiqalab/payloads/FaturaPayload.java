package br.com.koyama.apiqalab.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaturaPayload {

    @JsonProperty("cliente_id")
    private Integer clienteId;

    @JsonProperty("cartao_id")
    private Integer cartaoId;

    private BigDecimal valor;
    private String status;
    private String vencimento;

    public FaturaPayload() {
    }

    public FaturaPayload(Integer clienteId, Integer cartaoId, BigDecimal valor, String vencimento) {
        this.clienteId = clienteId;
        this.cartaoId = cartaoId;
        this.valor = valor;
        this.vencimento = vencimento;
    }

    public FaturaPayload(
            Integer clienteId,
            Integer cartaoId,
            BigDecimal valor,
            String status,
            String vencimento
    ) {
        this.clienteId = clienteId;
        this.cartaoId = cartaoId;
        this.valor = valor;
        this.status = status;
        this.vencimento = vencimento;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getCartaoId() {
        return cartaoId;
    }

    public void setCartaoId(Integer cartaoId) {
        this.cartaoId = cartaoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }
}
