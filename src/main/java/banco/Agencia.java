package banco;

import conta.Conta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Agencia {
    private final int numero;
    private final List<Conta> contas = new ArrayList<>();

    public Agencia(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public List<Conta> getContas() {
        return Collections.unmodifiableList(contas);
    }
}
