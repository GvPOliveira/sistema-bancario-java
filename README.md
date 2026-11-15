# sistema-bancario-java

Sistema bancário no console, em Java, com foco em POO: clientes, contas, empréstimos, agências e uma simulação das finanças do banco mês a mês.

Trabalho em grupo da disciplina de Programação Orientada a Objetos.

## Como rodar

Você precisa de **JDK 17 ou mais novo** e do **Maven**. Confira com `java -version` e `mvn -version`.

Pelo terminal, na raiz do projeto:

```
mvn compile
java -cp target/classes app.Main
```

Pela IDE: abra a pasta do projeto (o IntelliJ e o VS Code reconhecem o `pom.xml` sozinhos) e rode a classe `app.Main`.

O projeto não tem nenhuma dependência. O Maven está aqui só para que a estrutura de pastas seja a mesma no IntelliJ e no VS Code, sem ninguém versionar configuração da própria IDE.

## Estrutura

Todo o código fica em `src/main/java`, dividido em pacotes:

| Pacote | O que tem | Situação |
|---|---|---|
| `banco` | `Banco`, `Agencia` | esqueleto pronto |
| `cliente` | `Cliente` (abstrata), `PessoaFisica`, `PessoaJuridica` | só `Cliente` existe |
| `conta` | `ProcessavelMensalmente`, `Conta` (abstrata), `ContaCorrente`, `ContaPoupanca` | só a interface e `Conta` existem |
| `emprestimo` | empréstimos | a criar |
| `simulacao` | simulador que gera clientes e avança os meses | a criar |
| `app` | `Main` e os menus do console | só o `Main` existe |

Os pacotes `emprestimo` e `simulacao` ainda não aparecem no repositório porque o Git não versiona pasta vazia. Eles nascem quando a primeira classe for criada.

## A base e o que cada um implementa

A base tem só estrutura e contratos, sem regra de negócio. Cada integrante implementa a sua parte em cima dela.

### `Cliente` (abstrata)

- Atributos: `nome`, `documento` (CPF ou CNPJ), `rendaMensal` e `scoreCredito`.
- `nome` e `documento` não mudam depois que o cliente é criado: entram pelo construtor e não têm setter.
- `validarDocumento()` é abstrato: `PessoaFisica` valida CPF e `PessoaJuridica` valida CNPJ.

### `Conta` (abstrata)

- Atributos: `numero` (gerado pela `Agencia`), `titular` e `saldo`.
- `depositar`, `sacar` e `transferir` são implementados **uma vez só, dentro da própria `Conta`**, e valem para todos os tipos de conta. Não sobrescreva esses métodos nas subclasses.
- `sacar` confere o valor contra `consultarSaldoDisponivel()`. A `ContaCorrente` sobrescreve esse método para somar o cheque especial, e assim o saque dela já considera o limite sem reescrever o `sacar`.
- O `saldo` não tem setter. Se uma subclasse precisar mexer nele fora das regras de saque (cobrar tarifa, creditar rendimento), usa o método protegido `ajustarSaldo(valor)`, com valor negativo para debitar.
- `processarMes()` fica para cada subclasse.

Hoje `depositar`, `sacar` e `transferir` lançam `UnsupportedOperationException`. Isso é de propósito: quer dizer "ainda não implementado". Quem for implementar apaga essa linha e escreve a regra no lugar.

### `ProcessavelMensalmente`

É o contrato de tudo que muda com a passagem do mês: a poupança rende, a corrente cobra tarifa, o empréstimo cobra juros. O Simulador depende só dessa interface, sem conhecer os tipos concretos.

`processarMes()` aplica os efeitos do mês no objeto e devolve o resultado **do ponto de vista do banco**:

| Situação | Retorno |
|---|---|
| A corrente cobrou R$ 15 de tarifa | `15` |
| O empréstimo cobrou R$ 50 de juros | `50` |
| A poupança rendeu R$ 10 para o cliente | `-10` |
| Nada mudou | `0` |

Positivo é dinheiro que o banco ganhou, negativo é dinheiro que o banco pagou. O Simulador soma os retornos e tem o resultado do mês.

## Regras do projeto

Estas decisões valem para todo o código. Cada uma tem um motivo.

- **Dinheiro é sempre `BigDecimal`, nunca `double`.** O `double` erra centavos em contas simples (`0.1 + 0.2` não dá `0.3`).
- **Classe de modelo não imprime no console.** Só o pacote `app` usa `System.out` e `Scanner`. Assim as classes de modelo funcionam igual no menu, no Simulador e em qualquer teste.
- **Falha é exceção, nunca `println`.** Valor inválido ou saldo insuficiente lança exceção, e o console decide o que mostrar ao usuário. Com `println`, o programa seguiria em frente como se a operação tivesse dado certo.
- **A `Conta` conhece o titular, mas o `Cliente` não guarda lista de contas.** Quem guarda as contas é a `Agencia`. Com a relação num sentido só, não existem duas listas para manter sincronizadas.
- **Classe abstrata só quando há tipos com comportamento diferente.** `Conta` e `Cliente` são abstratas porque têm variações (corrente e poupança, física e jurídica). `Banco` e `Agencia` são classes comuns: várias agências são vários objetos da mesma classe.

## Convenções de nomes

- Classes, métodos e variáveis em **português, sem acento**, sem misturar idiomas. As exceções são os prefixos e métodos padrão do Java: `get`, `set`, `toString`, `equals`.
- Métodos começam com verbo no infinitivo: `depositar`, `sacar`, `consultarSaldoDisponivel`.
- Classes são substantivos no singular: `Conta`, `Agencia`.
- Comentários podem ter acento.

## Como contribuir

A `main` é protegida: ninguém envia código direto para ela. Só a base inicial entrou sem Pull Request.

Cada integrante trabalha numa branch com o próprio nome, em minúsculas e sem acento (por exemplo, `gabriel`).

1. Na primeira vez, crie a sua branch a partir da `main`: `git checkout main`, `git pull` e `git checkout -b seu-nome`.
2. Faça commits pequenos, um por assunto.
3. Antes de enviar, rode `mvn compile` e confirme que deu `BUILD SUCCESS`.
4. Envie a branch (`git push -u origin seu-nome`) e abra um Pull Request no GitHub.
5. Outra pessoa do grupo revisa e aprova. Com uma aprovação, o PR pode entrar na `main`.
6. Depois que qualquer PR entrar na `main`, traga as novidades para a sua branch antes de continuar: `git pull origin main`.

Não espere terminar a sua parte inteira para abrir o PR. Abra um a cada pedaço que funciona (uma classe pronta, um método implementado): PRs pequenos são revisados mais rápido e geram menos conflito.

### Mensagens de commit

Em português, no formato [Conventional Commits](https://www.conventionalcommits.org/pt-br/), com o verbo no presente:

```
feat: adiciona saque na conta
fix: corrige calculo do rendimento da poupanca
docs: atualiza instrucoes de execucao
```

Os tipos mais usados são `feat` (funcionalidade nova), `fix` (correção), `docs` (documentação), `refactor` (reorganização sem mudar o comportamento) e `build` (Maven e configuração).

### O que não vai para o repositório

O `.gitignore` já cobre `target/`, `.idea/`, `*.iml` e `.vscode/`. São arquivos gerados pelo Maven ou configurações pessoais da IDE.
