# Automacao REST Assured API

Projeto Maven em Java 21 criado para estudos de automacao de API com REST Assured, JUnit 5 e Hamcrest.

Este projeto faz parte do repositorio `api-lab-QAKoyama`, que possui uma API Python/FastAPI na raiz. A API precisa estar rodando localmente antes da execucao dos testes:

```text
http://localhost:8000
```

## Objetivo

Preparar a estrutura inicial para automatizar futuramente os endpoints da API com REST Assured.

Neste momento, o projeto contem:

- Configuracao Maven
- Classe base de testes
- Utilitario simples para reset da massa
- Classes de teste para estudo gradual dos endpoints
- POJOs basicos para payloads
- Pastas preparadas para JSON Schema e payloads externos

## Tecnologias

- Java 21
- Maven
- JUnit 5
- REST Assured
- REST Assured JSON Schema Validator
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

## Estudos do topico 1.4

A API tambem possui recursos para praticar parametros e formatos de comunicacao:

- `pathParam`: `/clientes/{cliente_id}`, `/cartoes/{cartao_id}`, `/faturas/{fatura_id}`
- `queryParam`: `/clientes?ativo=true`, `/cartoes?status=ATIVO`, `/faturas?cliente_id=1`
- headers de requisicao: `x-canal`, `x-api-version`, `x-request-id`
- headers de resposta: `X-Pay-Lab`, `X-API-Version`, `X-Canal`, `X-Request-ID`
- `contentType` e `accept` configurados na `BaseTest`

## Estudos do topico 1.6

A classe `AutenticacoesTest.java` foi criada apenas com TODOs para estudo futuro de autenticacao em API.

Endpoints preparados na API:

- `GET /auth/publica`
- `GET /auth/api-key`
- `GET /auth/basic`
- `GET /auth/bearer`

Conceitos que serao praticados depois:

- API publica
- API Key com header `x-api-key`
- Basic Auth
- Bearer Token
- Autenticacao aplicada aos testes REST Assured

## Estudos de validacao de contrato

A classe `ContratosSchemasTest.java` foi criada apenas com TODOs para estudo futuro de validacao de contrato.

Endpoints preparados na API:

- `GET /contratos/json/health`
- `GET /contratos/json/cliente`
- `GET /contratos/xml/cliente`
- `GET /contratos/diferenca`

Arquivos preparados em `src/test/resources/schemas`:

- `health.schema.json`
- `cliente.schema.json`
- `cliente.xsd`

Conceitos que serao praticados depois:

- Validacao de schema JSON
- Validacao de schema XML
- Uso de arquivos de schema em resources
- Diferenca entre validacao funcional e contratual

## Proximos passos de automacao

- Automatizar `GET /health`
- Automatizar `POST /reset`
- Automatizar login valido e invalido
- Automatizar endpoints de clientes
- Automatizar cartoes
- Automatizar faturas
- Evoluir estudos de parametros, headers, `contentType` e `accept`
- Evoluir estudos de autenticacao com API Key, Basic Auth e Bearer Token
- Evoluir estudos de validacao de contrato com JSON Schema e XML Schema
- Adicionar validacao de schema futuramente
- Integrar ao GitHub Actions futuramente

## Observacoes

- Este projeto deve evoluir aos poucos, conforme os topicos de estudo.
- A massa de dados da API deve ser restaurada com `POST /reset` antes dos cenarios automatizados.
- A API Python/FastAPI deve estar rodando em `http://localhost:8000` para que os testes funcionem.
