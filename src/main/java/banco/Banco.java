package banco;

import java.util.ArrayList;
import java.util.List;

public class Banco {
    private final String nome;
    private final List<Agencia> agencias = new ArrayList<>();

    public Banco(String nome) {
        this.nome = nome;
    }

    public void adicionarAgencia(Agencia agencia) {
        agencias.add(agencia);
    }

    public String getNome() {
        return nome;
    }

    public List<Agencia> getAgencias() {
        return agencias;
    }
}
