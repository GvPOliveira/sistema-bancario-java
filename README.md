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
| `conta` | `Conta` (abstrata), `ContaCorrente`, `ContaPoupanca` | só `Conta` existe |
| `emprestimo` | empréstimos | a criar |
| `simulacao` | `ProcessavelMensalmente`, simulador que gera clientes e avança os meses | só a interface existe |
| `app` | `Main` e os menus do console | só o `Main` existe |

O pacote `emprestimo` ainda não aparece no repositório porque o Git não versiona pasta vazia. Ele nasce quando a primeira classe for criada.

A interface `ProcessavelMensalmente` fica em `simulacao` porque o contrato pertence a quem o usa, o Simulador. Contas e empréstimos é que se adaptam a ele.

## A base e o que cada um implementa

A base tem só estrutura e contratos, sem regra de negócio. Cada integrante implementa a sua parte em cima dela.

### `Cliente` (abstrata)

- Atributos: `nome`, `documento` (CPF ou CNPJ), `rendaMensal` e `scoreCredito`.
- `nome` e `documento` não mudam depois que o cliente é criado: entram pelo construtor e não têm setter.
- `validarDocumento()` é abstrato: `PessoaFisica` valida CPF e `PessoaJuridica` valida CNPJ.
- Cada subclasse chama `validarDocumento()` no final do próprio construtor, para que nunca exista um cliente com documento inválido. A chamada não fica no construtor de `Cliente` porque, ali, o método sobrescrito rodaria antes de a subclasse terminar de ser inicializada.

### `Banco` e `Agencia`

- O `Banco` guarda as agências, e a `Agencia` guarda as contas.
- `getAgencias()` e `getContas()` devolvem listas **somente leitura**: dá para percorrer, mas um `add` ou `clear` lança exceção. Para incluir algo, use o método da própria classe (`adicionarAgencia` no `Banco` e, em breve, `abrirConta` na `Agencia`, que também gera o número da conta).

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

A `main` é protegida: ninguém envia código direto para ela. Tudo entra por Pull Request, que o Gabriel Vinicius abre e mescla.

Cada integrante trabalha numa branch com o nome e o sobrenome juntos, sem acento e sem espaço (por exemplo, `GabrielVinicius`).

1. Na primeira vez, crie a sua branch a partir da `main`: `git checkout main`, `git pull` e `git checkout -b NomeSobrenome`.
2. Faça commits pequenos, um por assunto.
3. Antes de enviar, traga as novidades da `main` (`git pull origin main`), rode `mvn compile` e confirme que deu `BUILD SUCCESS`.
4. Envie a branch (`git push -u origin NomeSobrenome`) e avise no grupo.
5. O Gabriel Vinicius abre o Pull Request da sua branch para a `main`, confere o que mudou e faz o merge.

Não espere terminar a sua parte inteira para avisar. Suba a cada pedaço que funciona (uma classe pronta, um método implementado): entregas pequenas são conferidas mais rápido e geram menos conflito.

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
