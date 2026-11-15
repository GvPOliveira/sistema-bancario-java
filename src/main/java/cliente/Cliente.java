package cliente;

import java.math.BigDecimal;

public abstract class Cliente {
    private final String nome;
    private final String documento;
    private BigDecimal rendaMensal;
    private int scoreCredito;

    protected Cliente(String nome, String documento, BigDecimal rendaMensal, int scoreCredito) {
        this.nome = nome;
        this.documento = documento;
        this.rendaMensal = rendaMensal;
        this.scoreCredito = scoreCredito;
    }

    /**
     * Valida o documento do cliente (CPF ou CNPJ, conforme a subclasse).
     * Lanca excecao se o documento for invalido.
     */
    public abstract void validarDocumento();

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public BigDecimal getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(BigDecimal rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    public int getScoreCredito() {
        return scoreCredito;
    }

    public void setScoreCredito(int scoreCredito) {
        this.scoreCredito = scoreCredito;
    }
}
