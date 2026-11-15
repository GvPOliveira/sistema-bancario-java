package conta;

import cliente.Cliente;
import simulacao.ProcessavelMensalmente;

import java.math.BigDecimal;

public abstract class Conta implements ProcessavelMensalmente {
    private final int numero;
    private final Cliente titular;
    private BigDecimal saldo = BigDecimal.ZERO;

    protected Conta(int numero, Cliente titular) {
        this.numero = numero;
        this.titular = titular;
    }

    /** Lanca excecao se o valor for invalido. */
    public void depositar(BigDecimal valor) {
        throw new UnsupportedOperationException("depositar ainda nao implementado");
    }

    /** Lanca excecao se o valor for invalido ou maior que o saldo disponivel. */
    public void sacar(BigDecimal valor) {
        throw new UnsupportedOperationException("sacar ainda nao implementado");
    }

    /** A ContaCorrente sobrescreve para somar o cheque especial. */
    public BigDecimal consultarSaldoDisponivel() {
        return saldo;
    }

    /** Deve usar sacar e depositar. */
    public void transferir(Conta destino, BigDecimal valor) {
        throw new UnsupportedOperationException("transferir ainda nao implementado");
    }

    /**
     * Soma o valor ao saldo sem passar pelas regras de saque. Use valor
     * negativo para debitar (ex.: tarifa). So as subclasses enxergam.
     */
    protected void ajustarSaldo(BigDecimal valor) {
        saldo = saldo.add(valor);
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
