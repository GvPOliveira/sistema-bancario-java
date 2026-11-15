package conta;

import java.math.BigDecimal;

public interface ProcessavelMensalmente {
    /*
     * Aplica os efeitos da passagem de um mes e devolve o resultado
     * financeiro para o banco.
     *
     * se retornar valor positivo significa que o banco ganhou dinheiro no mes (tarifa, juros);
     * valor negativo significa que o banco pagou dinheiro (rendimento da poupanca);
     * zero se nada mudou
     */
    BigDecimal processarMes();
}
