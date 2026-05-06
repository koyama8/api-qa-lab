package br.com.koyama.apiqalab.payloads;

public class ClientePayload {

    private String nome;
    private String email;
    private String cpf;
    private Boolean ativo;

    public ClientePayload() {
    }

    public ClientePayload(String nome, String email, String cpf) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }

    public ClientePayload(String nome, String email, String cpf, Boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
