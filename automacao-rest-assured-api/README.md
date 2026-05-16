# REST Assured API Automation

Suite de automacao de testes de API em Java para o projeto Pay Lab API. O objetivo e validar endpoints REST, regras de negocio, contratos, autenticacao, serializacao, arquivos e tempo de resposta usando REST Assured.

Este modulo pertence ao repositorio `api-lab-QAKoyama` e depende da API FastAPI rodando localmente em:

```text
http://localhost:8000
```

## Stack Tecnica

| Tecnologia | Uso |
| --- | --- |
| Java 21 | Linguagem da suite de testes |
| Maven | Build e execucao |
| JUnit 5 | Runner de testes |
| REST Assured | Requests, responses e validacoes HTTP |
| Hamcrest | Matchers para asserts |
| Jackson Databind | Serializacao e desserializacao |
| JSON Schema Validator | Validacao de contrato JSON |
| XML Schema | Validacao de contrato XML |
| SonarLint | Apoio de qualidade e limpeza de codigo |

## Estrutura

```text
automacao-rest-assured-api/
|-- pom.xml
|-- README.md
|-- src/
|   |-- test/
|       |-- java/br/com/koyama/apiqalab/
|       |   |-- base/
|       |   |   |-- BaseTest.java
|       |   |-- payloads/
|       |   |   |-- LoginPayload.java
|       |   |   |-- ClientePayload.java
|       |   |   |-- CartaoPayload.java
|       |   |   |-- FaturaPayload.java
|       |   |   |-- PagamentoPayload.java
|       |   |-- responses/
|       |   |   |-- ClienteResponse.java
|       |   |-- tests/
|       |   |   |-- HealthTest.java
|       |   |   |-- HeadersTest.java
|       |   |   |-- AuthTest.java
|       |   |   |-- AutenticacoesTest.java
|       |   |   |-- ClientesTest.java
|       |   |   |-- CartoesTest.java
|       |   |   |-- FaturasTest.java
|       |   |   |-- PagamentosTest.java
|       |   |   |-- SerializacaoDesserializacaoTest.java
|       |   |   |-- ContratosSchemasTest.java
|       |   |   |-- ArquivosComportamentoTest.java
|       |   |   |-- HtmlXPathTest.java
|       |   |-- utils/
|       |       |-- ResetUtils.java
|       |-- resources/
|           |-- payloads/
|           |   |-- upload-estudo.txt
|           |-- schemas/
|               |-- health.schema.json
|               |-- cliente.schema.json
|               |-- cliente.xsd
```

## Classes Principais

| Classe | Responsabilidade |
| --- | --- |
| `BaseTest` | Configura `baseURI`, `Content-Type`, `Accept` e logs em falha. |
| `ResetUtils` | Executa `POST /reset` para restaurar a massa inicial. |
| `HealthTest` | Valida disponibilidade da API. |
| `HeadersTest` | Valida headers de request e response. |
| `AuthTest` | Testa login valido, login invalido e token fake. |
| `AutenticacoesTest` | Testa API publica, API Key, Basic Auth e Bearer Token. |
| `ClientesTest` | Exercita CRUD, path params, query params e validacoes de cliente. |
| `CartoesTest` | Testa consulta, criacao e transicoes de status de cartoes. |
| `FaturasTest` | Testa consulta, criacao, atualizacao, exclusao e filtros de faturas. |
| `PagamentosTest` | Valida pagamento, valor divergente, fatura paga e efeito colateral. |
| `SerializacaoDesserializacaoTest` | Pratica JSON, XML, Map, POJO, `XmlPath` e desserializacao. |
| `ContratosSchemasTest` | Valida contratos JSON Schema e XML Schema. |
| `ArquivosComportamentoTest` | Testa upload, download, metadados e tempo de resposta. |
| `HtmlXPathTest` | Valida respostas HTML e conteudos preparados para XPath. |

## Como Executar

Suba a API na raiz do projeto principal:

```powershell
cd ..
uvicorn app.main:app --reload
```

Em outro terminal, volte para o modulo de automacao:

```powershell
cd automacao-rest-assured-api
```

Valide a compilacao:

```powershell
mvn test-compile
```

Execute a suite:

```powershell
mvn test
```

Para restaurar a massa manualmente antes de rodar:

```powershell
Invoke-WebRequest -Uri "http://localhost:8000/reset" -Method POST -UseBasicParsing
```

## Padrao dos Testes

Os testes seguem o fluxo do REST Assured:

```java
given()
    .body(payload)
.when()
    .post("/endpoint")
.then()
    .statusCode(200)
    .body("success", equalTo(true));
```

Boas praticas aplicadas:

- Separacao entre payloads, responses, testes e utilitarios.
- Uso de `BaseTest` para configuracoes comuns.
- Uso de `ResetUtils` para previsibilidade da massa.
- Validacao de status code, headers, body e tempo de resposta.
- Cobertura de cenarios positivos e negativos.
- Uso de arquivos em `src/test/resources`.

## Cobertura Funcional

| Area | Exemplos de validacao |
| --- | --- |
| Health | `GET /health`, status `200`, `success=true`, `data.status=UP`. |
| Headers | `x-api-version`, `x-canal`, headers de resposta. |
| Auth | Login valido/invalido, API Key, Basic Auth, Bearer Token. |
| Clientes | Listagem, busca por ID, criacao, atualizacao, exclusao, erro 400 e 404. |
| Cartoes | Criacao, busca, bloqueio, desbloqueio, cancelamento e regra 409. |
| Faturas | CRUD, filtros por status, cliente e cartao. |
| Pagamentos | Pagamento valido, valor divergente, fatura ja paga e status `PAGA`. |
| Contratos | JSON Schema, XML Schema e diferenca entre contrato e comportamento. |
| Arquivos | Multipart upload, download, metadados, headers e response time. |
| HTML | Validacao de `text/html`, texto, atributos e conteudos para XPath. |

## Recursos de Estudo

Este modulo consolida os seguintes topicos de automacao de API:

- HTTP request e HTTP response.
- `given()`, `when()`, `then()`.
- `GET`, `POST`, `PUT`, `DELETE`.
- `statusCode()`, `body()`, `contentType()`.
- `pathParam`, `queryParam` e headers.
- Serializacao e desserializacao JSON/XML.
- Payload classes e response classes.
- Validacao funcional e validacao contratual.
- Upload e download de arquivos.
- Tempo maximo de resposta.
- HTML e XPath.
- Reset de massa e independencia entre testes.

## Status do Modulo

- `mvn test-compile`: usado para validar build e compilacao.
- `mvn test`: depende da API local em execucao e da massa de dados previsivel.
- A suite esta em evolucao para eliminar dependencia de ordem entre testes e estabilizar cenarios de CRUD completos.

## Proximas Melhorias

- Padronizar nomes de metodos com foco em comportamento.
- Isolar cenarios que alteram massa usando `ResetUtils`.
- Corrigir testes dependentes de ordem de execucao.
- Fortalecer asserts de body e headers nos cenarios principais.
- Adicionar pipeline CI/CD com execucao automatica.
- Gerar relatorios de testes para evidencias tecnicas.

## Competencias Demonstradas

REST Assured, API Testing, Java 21, JUnit 5, Maven, Hamcrest, Jackson, JSON Schema, XML Schema, HTTP, CRUD, Authentication, API Key, Basic Auth, Bearer Token, Test Automation, Contract Testing, Data Reset, Positive Tests, Negative Tests e Quality Engineering.
