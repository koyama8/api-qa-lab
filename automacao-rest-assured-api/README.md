# Automacao REST Assured API

Projeto Maven em Java 21 criado para estudos de automacao de API com REST Assured, JUnit 5 e Hamcrest.

Este projeto faz parte do repositorio `api-lab-QAKoyama`, que possui uma API Python/FastAPI na raiz. A API precisa estar rodando localmente antes da execucao dos testes:

```text
http://localhost:8000
```

## Objetivo

Preparar a estrutura inicial para automatizar futuramente os endpoints da API com REST Assured.

Neste momento, o projeto contem apenas:

- Configuracao Maven
- Classe base de testes
- Utilitario simples para reset da massa
- Classes de teste com TODOs
- POJOs basicos para payloads
- Pastas preparadas para JSON Schema e payloads externos

## Tecnologias

- Java 21
- Maven
- JUnit 5
- REST Assured
- Hamcrest
- Jackson Databind
- Maven Surefire Plugin

## Como importar no Eclipse

1. Abra o Eclipse.
2. Clique em `File`.
3. Clique em `Import`.
4. Selecione `Existing Maven Projects`.
5. Em `Root Directory`, selecione a pasta:

```text
api-lab-QAKoyama/automacao-rest-assured-api
```

6. Confirme se o `pom.xml` foi encontrado.
7. Clique em `Finish`.

## Como executar pelo Eclipse

Antes de rodar os testes, suba a API Python/FastAPI no VS Code:

```powershell
python -m uvicorn app.main:app --reload
```

Depois, no Eclipse:

1. Clique com o botao direito no projeto `automacao-rest-assured-api`.
2. Selecione `Run As`.
3. Clique em `Maven test`.

## Como executar pelo terminal

Entre na pasta do projeto de automacao:

```powershell
cd automacao-rest-assured-api
```

Execute:

```powershell
mvn test
```

## Estrutura de pastas

```text
automacao-rest-assured-api/
├── pom.xml
├── README.md
├── src/
│   └── test/
│       ├── java/
│       │   └── br/
│       │       └── com/
│       │           └── koyama/
│       │               └── apiqalab/
│       │                   ├── base/
│       │                   │   └── BaseTest.java
│       │                   ├── tests/
│       │                   │   ├── HealthTest.java
│       │                   │   ├── AuthTest.java
│       │                   │   ├── ClientesTest.java
│       │                   │   ├── CartoesTest.java
│       │                   │   ├── FaturasTest.java
│       │                   │   └── PagamentosTest.java
│       │                   ├── utils/
│       │                   │   └── ResetUtils.java
│       │                   └── payloads/
│       │                       ├── LoginPayload.java
│       │                       ├── ClientePayload.java
│       │                       └── PagamentoPayload.java
│       └── resources/
│           ├── schemas/
│           └── payloads/
```

## Proximos passos de automacao

- Automatizar `GET /health`
- Automatizar `POST /reset`
- Automatizar login valido e invalido
- Automatizar endpoints de clientes
- Automatizar cartoes
- Automatizar faturas
- Automatizar pagamentos
- Adicionar validacao de schema futuramente
- Integrar ao GitHub Actions futuramente

## Observacoes

- Este projeto ainda nao possui testes completos.
- As classes em `tests/` foram criadas apenas com TODOs para evolucao manual.
- A massa de dados da API deve ser restaurada com `POST /reset` antes dos cenarios automatizados.
- A API Python/FastAPI deve estar rodando em `http://localhost:8000` para que os testes funcionem.
