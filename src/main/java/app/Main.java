package app;

import banco.Banco;

public class Main {
    public static void main(String[] args) {
        Banco banco = new Banco("Banco POO");
        System.out.println("Bem-vindo ao " + banco.getNome());
    }
}
