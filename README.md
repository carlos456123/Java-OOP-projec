# WFRestaurant

Sistema de console para gerenciamento de pedidos e vendas de um restaurante, desenvolvido como projeto da disciplina de **Programação Orientada a Objetos (POO 3)** — 3º período de Sistemas de Informação.

O foco do projeto é a aplicação prática de três padrões de projeto (Design Patterns) do catálogo GoF em um cenário real de negócio: montagem de pratos com adicionais, cadastro de vendas e geração de relatórios.

## Padrões de projeto aplicados

| Padrão | Onde está | Papel no sistema |
|---|---|---|
| **Singleton** | `wfr.Singleton.ClassSingleton` | Garante uma única instância compartilhada da `FactoryPrato` e do `ArquivoVendas` durante a execução. |
| **Factory Method (Simple Factory)** | `wfr.Factory.FactoryPrato` | Cria um `Prato` a partir do código do item do cardápio, escondendo a lógica de instanciação do restante do sistema. |
| **Decorator** | `wfr.Base.Decorator.*` | Permite "empilhar" adicionais (queijo, salada, molho madeira) sobre um prato base, recalculando descrição e preço dinamicamente, sem precisar de uma subclasse pra cada combinação. |

## Estrutura do projeto

```
wfr/
├── Main.java                  → ponto de entrada, menu do console
├── Base/
│   ├── Prato.java              → interface comum a prato base e decorators
│   ├── PratoBase.java          → implementação concreta do prato
│   ├── Venda.java              → entidade de venda + serialização/desserialização CSV
│   ├── ItemCardapio.java       → enum com os itens disponíveis (código, nome, preço)
│   └── Decorator/
│       ├── PratoDecorator.java         → decorator abstrato
│       ├── AdicionalQueijo.java
│       ├── AdicionalSalada.java
│       └── AdicionalMolhoMadeira.java
├── Factory/
│   └── FactoryPrato.java       → Simple Factory: cria Prato a partir de um código
├── Singleton/
│   └── ClassSingleton.java     → instância única de FactoryPrato + ArquivoVendas
├── Gestor/
│   └── GestorDeVendas.java     → regras de negócio: cadastrar, listar, buscar, resumir vendas
├── Leitor/
│   └── LeitorDeTexto.java      → leitura e validação de entrada do usuário no console
└── SalvaVenda/
    └── ArquivoVendas.java      → persistência das vendas em arquivo texto (vendas.txt)
```

## Cardápio disponível

| Código | Prato | Preço base |
|---|---|---|
| 1 | Lasanha | R$ 25,00 |
| 2 | Frango à parmegiana | R$ 28,00 |
| 3 | Filé à parmegiana | R$ 29,00 |
| 4 | Camarão ao molho madeira | R$ 32,00 |

Adicionais disponíveis (Decorator): **Queijo**, **Salada**, **Molho Madeira** — podem ser combinados livremente sobre qualquer prato base.

## Funcionalidades

- Cadastrar uma nova venda (cliente + prato + adicionais + quantidade)
- Listar todas as vendas registradas, com total geral
- Buscar vendas por nome de cliente
- Gerar resumo de vendas agrupado por prato
- Desfazer a última venda registrada
- Persistência simples em arquivo texto (`vendas.txt`), no formato CSV (separador `;`)

## Como executar

Pré-requisitos: JDK 21+ e Maven instalados.

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="wfr.Main"
```

> **Nota:** o `pom.xml` está configurado para `maven.compiler.source/target = 25`. Se o seu ambiente tiver apenas JDK 21, ajuste essas propriedades para `21` antes de compilar.

## Formato de persistência (vendas.txt)

Cada linha representa uma venda, no formato:

```
data_hora;cliente;codigo_prato;descricao_final;preco_unitario;quantidade;total
```

Exemplo:

```
2025-11-19 11:14:45;carlos;1;Lasanha;25,00;3;75,00
2025-11-19 18:46:34;denini;1;Lasanha + Adicional Molho Madeira;29,00;2;58,00
```

## Roadmap / próximos passos

- [ ] Testes unitários (JUnit) para Singleton, Factory e Decorator
- [ ] Diagrama UML de classes
- [ ] Ajuste de versão do Java no `pom.xml`

## Autor

Carlos Wesley — Estudante da Univesidade Pernanbuco - Sistemas de Informação
