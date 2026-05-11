# Pay Lab

Pay Lab é uma API de estudo da Coala inspirada em um contexto bancário, com domínios de login, clientes, cartões, faturas e pagamentos. O projeto foi criado para servir como laboratório central para testes manuais no Bruno, automação de API com Java + REST Assured, automação de API com Cypress usando `cy.request()` e futura integração em CI/CD com GitHub Actions.

## Tecnologias utilizadas

- Python 3.11+
- FastAPI
- Uvicorn
- Pydantic
- Persistência em arquivo JSON
- CORS habilitado
- Swagger em `/docs`

## Como instalar dependências

No terminal, entre na pasta do projeto:

```bash
cd api-lab-QAKoyama
```

Crie e ative um ambiente virtual, se desejar:

```bash
python -m venv .venv
```

Windows PowerShell:

```bash
.\.venv\Scripts\Activate.ps1
```

Instale as dependências:

```bash
pip install -r requirements.txt
```

## Como rodar a API

```bash
uvicorn app.main:app --reload
```

- URL base: `http://localhost:8000`
- Swagger: `http://localhost:8000/docs`
- Health check: `http://localhost:8000/health`

## Padrão de respostas

Sucesso:

```json
{
  "success": true,
  "message": "Operação realizada com sucesso",
  "data": {}
}
```

Erro:

```json
{
  "success": false,
  "message": "Mensagem clara do erro",
  "error": "TIPO_DO_ERRO"
}
```

Observação: `DELETE /clientes/{id}` e `DELETE /faturas/{id}` retornam `204 No Content`, seguindo a regra REST para exclusão bem-sucedida.

## Endpoints

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/health` | Verifica se a API está online |
| GET | `/headers` | Endpoint de estudo para headers de requisição e resposta |
| POST | `/login` | Realiza login com usuario e senha fixos |
| GET | `/auth/publica` | Endpoint publico para estudo de autenticacao |
| GET | `/auth/api-key` | Endpoint protegido por API Key |
| GET | `/auth/basic` | Endpoint protegido por Basic Auth |
| GET | `/auth/bearer` | Endpoint protegido por Bearer Token |
| GET | `/contratos/json/health` | Endpoint para estudo de contrato JSON do health |
| GET | `/contratos/json/cliente` | Endpoint para estudo de contrato JSON de cliente |
| GET | `/contratos/xml/cliente` | Endpoint para estudo de contrato XML de cliente |
| GET | `/contratos/diferenca` | Explica validacao funcional e contratual |
| POST | `/serializacao/json/map` | Recebe um Map JSON para estudo de serializacao |
| POST | `/serializacao/json/cliente` | Recebe um objeto cliente em JSON para estudo de serializacao |
| GET | `/serializacao/json/cliente` | Retorna JSON para estudo de desserializacao |
| POST | `/serializacao/xml/cliente` | Recebe XML para estudo de serializacao |
| GET | `/serializacao/xml/cliente` | Retorna XML para estudo de desserializacao |
| POST | `/arquivos/upload` | Upload de arquivo para estudo multipart |
| GET | `/arquivos/download` | Download de arquivo de estudo |
| GET | `/arquivos/metadados` | Metadados do arquivo de download |
| GET | `/comportamento/delay` | Delay artificial para estudar tempo maximo e timeout |
| GET | `/html/estudo` | Pagina HTML para estudo |
| GET | `/html/clientes` | Tabela HTML para estudo de XPath |
| GET | `/clientes` | Lista clientes e permite filtros por query params |
| GET | `/clientes/{id}` | Busca cliente por ID |
| POST | `/clientes` | Cria cliente |
| PUT | `/clientes/{id}` | Atualiza cliente |
| DELETE | `/clientes/{id}` | Remove cliente |
| GET | `/cartoes` | Lista cartões e permite filtros por query params |
| GET | `/cartoes/{id}` | Busca cartão por ID |
| POST | `/cartoes` | Cria cartão |
| PUT | `/cartoes/{id}/bloquear` | Bloqueia cartão |
| PUT | `/cartoes/{id}/desbloquear` | Desbloqueia cartão |
| PUT | `/cartoes/{id}/cancelar` | Cancela cartão |
| GET | `/faturas` | Lista faturas e permite filtros por query params |
| GET | `/faturas/{id}` | Busca fatura por ID |
| POST | `/faturas` | Cria fatura |
| PUT | `/faturas/{id}` | Atualiza fatura |
| DELETE | `/faturas/{id}` | Remove fatura |
| GET | `/clientes/{id}/faturas` | Lista faturas de um cliente |
| POST | `/pagamentos` | Realiza pagamento de fatura |
| POST | `/reset` | Restaura a massa inicial de dados |

## Exemplos de payload JSON

### POST /login

```json
{
  "usuario": "admin",
  "senha": "123456"
}
```

Login válido retorna um token fake:

```json
{
  "success": true,
  "message": "Operação realizada com sucesso",
  "data": {
    "token": "fake-token-qa-lab-123456",
    "token_type": "Bearer",
    "expires_in": 3600
  }
}
```

### POST /clientes

```json
{
  "nome": "Cliente Pay Lab",
  "email": "matheus@email.com",
  "cpf": "12345678900"
}
```

### PUT /clientes/{id}

```json
{
  "nome": "Cliente Pay Lab Atualizado",
  "email": "matheus.novo@email.com",
  "cpf": "12345678900",
  "ativo": true
}
```

### POST /cartoes

```json
{
  "cliente_id": 1,
  "numero_masked": "**** **** **** 1234",
  "limite": 5000
}
```

### POST /faturas

```json
{
  "cliente_id": 1,
  "cartao_id": 1,
  "valor": 950.75,
  "status": "ABERTA",
  "vencimento": "2026-07-10"
}
```

### PUT /faturas/{id}

```json
{
  "valor": 999.99,
  "status": "FECHADA",
  "vencimento": "2026-07-20"
}
```

### POST /pagamentos

```json
{
  "fatura_id": 1,
  "valor": 1200.50
}
```

## Parâmetros e headers para estudo

Estes cenários foram adicionados para estudar `pathParam`, `queryParam`, `header`, `contentType` e `accept` no REST Assured.

### Path param

```text
GET /clientes/1
GET /cartoes/1
GET /faturas/1
```

### Query params

```text
GET /clientes?ativo=true
GET /clientes?nome=Cliente
GET /clientes?nome=NomeInexistente
GET /cartoes?status=ATIVO
GET /cartoes?cliente_id=1
GET /cartoes?cliente_id=999
GET /faturas?status=ABERTA
GET /faturas?cliente_id=1&cartao_id=1
GET /faturas?cliente_id=999
```

Quando um filtro por query param não encontra registros, a API retorna `404` com `success=false` e mensagem clara para automação de cenário negativo.

### Headers de requisição e resposta

```text
GET /headers
```

Headers aceitos para estudo:

```text
x-canal: bruno
x-api-version: 1
x-request-id: bruno-req-001
Accept: application/json
```

A resposta inclui headers como:

```text
X-Pay-Lab
X-API-Version
X-Canal
X-Request-ID
```

## Serializacao e desserializacao para estudo

Estes endpoints foram adicionados para estudar o topico 1.5 no REST Assured.

### Serializando Map para JSON

```text
POST /serializacao/json/map
```

Payload exemplo:

```json
{
  "origem": "bruno",
  "ativo": true,
  "quantidade": 3
}
```

### Serializando objeto para JSON

```text
POST /serializacao/json/cliente
```

Payload exemplo:

```json
{
  "nome": "Cliente Serializacao",
  "email": "serializacao@email.com",
  "cpf": "12345678900"
}
```

### Desserializando resposta JSON para objeto

```text
GET /serializacao/json/cliente
```

Use este retorno para praticar `response.as(...)`, POJOs de resposta ou leitura com JSONPath.

### Serializacao para XML

```text
POST /serializacao/xml/cliente
```

Payload exemplo:

```xml
<cliente>
  <nome>Cliente XML</nome>
  <email>xml@email.com</email>
  <cpf>12345678900</cpf>
</cliente>
```

### Desserializacao de XML

```text
GET /serializacao/xml/cliente
```

Use este retorno para praticar leitura com `XmlPath`, `response.asString()` ou desserializacao XML.

## Autenticacoes para estudo

Estes endpoints foram adicionados para estudar o topico 1.6 no REST Assured.

### Acesso publico

```text
GET /auth/publica
```

Nao exige autenticacao.

### API Key

```text
GET /auth/api-key
```

Header valido:

```text
x-api-key: qa-lab-api-key-123
```

Sem API Key ou com API Key invalida, a API retorna `401`.

### Basic Auth

```text
GET /auth/basic
```

Credenciais validas:

```text
usuario: admin
senha: 123456
```

Credenciais ausentes ou invalidas retornam `401`.

### Bearer Token

```text
GET /auth/bearer
```

Header valido:

```text
Authorization: Bearer fake-token-qa-lab-123456
```

Token ausente ou invalido retorna `401`.

## Contratos e schemas para estudo

Estes endpoints foram adicionados para estudar validacao de contrato.

### JSON Schema

```text
GET /contratos/json/health
GET /contratos/json/cliente
```

Use estes retornos para validar arquivos `.schema.json` no REST Assured.

Arquivos preparados no projeto Java:

```text
automacao-rest-assured-api/src/test/resources/schemas/health.schema.json
automacao-rest-assured-api/src/test/resources/schemas/cliente.schema.json
```

### XML Schema

```text
GET /contratos/xml/cliente
```

Use este retorno para validar um arquivo `.xsd`.

Arquivo preparado no projeto Java:

```text
automacao-rest-assured-api/src/test/resources/schemas/cliente.xsd
```

### Validacao funcional x validacao contratual

```text
GET /contratos/diferenca
```

Validacao funcional verifica comportamento, regra de negocio e valores.
Validacao contratual verifica estrutura, campos obrigatorios e tipos.

## Arquivos e comportamento avancado

Estes endpoints foram adicionados para estudar upload, download, metadados de arquivo e tempo maximo dos testes.

### Upload de arquivo

```text
POST /arquivos/upload
```

Envie um arquivo multipart/form-data no campo:

```text
arquivo
```

A resposta retorna metadados do arquivo enviado:

```text
nome
content_type
tamanho_bytes
extensao
salvo
```

### Download de arquivo

```text
GET /arquivos/download
```

Retorna o arquivo de estudo `pay-lab-estudo.txt`.

Use para validar:

```text
Content-Type
Content-Disposition
Content-Length
bytes do arquivo
```

### Metadados do arquivo

```text
GET /arquivos/metadados
```

Retorna nome, tipo, tamanho, extensao, hash `sha256` e URL de download.

### Tempo maximo / timeout

```text
GET /comportamento/delay?segundos=1
```

Use para estudar tempo de resposta e validacoes como `time(lessThan(...))` no REST Assured.

## HTML e XPath para estudo

Estes endpoints foram adicionados para estudar respostas HTML e XPath com HTML.

### Pagina HTML de estudo

```text
GET /html/estudo
```

Retorna uma pagina HTML simples com:

```text
title
h1
section
ul
li
link
atributos id, class e data-*
```

Use para estudar validacoes de `Content-Type`, texto, tags e atributos.

### Tabela HTML de clientes

```text
GET /html/clientes
```

Retorna uma tabela HTML previsivel para praticar XPath.

Exemplos de XPath para estudo:

```text
//*[@id='titulo-principal']
//*[@id='tabela-clientes']
//td[text()='Cliente Pay Lab']
//tr[@data-cliente-id='1']
//table[@id='tabela-clientes']/tbody/tr[1]/td[2]
```

## Dados iniciais previsiveis

O arquivo `app/database/db.json` já vem preenchido com dados iniciais:

- Clientes com IDs `1`, `2` e `3`
- Cartões com IDs `1`, `2` e `3`
- Faturas com IDs `1`, `2` e `3`
- Um pagamento inicial com ID `1`

Estados importantes para testes:

- Cartão `1`: `ATIVO`
- Cartão `2`: `BLOQUEADO`
- Cartão `3`: `CANCELADO`
- Fatura `1`: `ABERTA`, valor `1200.50`
- Fatura `2`: `FECHADA`, valor `800.00`
- Fatura `3`: `PAGA`, valor `300.00`

## Regras de negocio

- `GET /health` retorna `200`.
- `GET /headers` retorna `200` quando `x-api-version` é `1`.
- `GET /headers` retorna `400` quando `x-api-version` é inválido.
- `GET /auth/publica` retorna `200` sem autenticacao.
- `GET /auth/api-key` retorna `200` com `x-api-key` valida.
- `GET /auth/api-key` retorna `401` sem API Key ou com API Key invalida.
- `GET /auth/basic` retorna `200` com Basic Auth valido.
- `GET /auth/basic` retorna `401` com Basic Auth invalido.
- `GET /auth/bearer` retorna `200` com Bearer Token valido.
- `GET /auth/bearer` retorna `401` sem token ou com token invalido.
- `GET /contratos/json/health` retorna JSON para validacao de contrato.
- `GET /contratos/json/cliente` retorna JSON para validacao de contrato.
- `GET /contratos/xml/cliente` retorna XML para validacao de contrato.
- `GET /contratos/diferenca` retorna explicacao sobre validacao funcional e contratual.
- `POST /serializacao/json/map` retorna `200` com o Map recebido.
- `POST /serializacao/json/cliente` retorna `200` com o objeto cliente recebido.
- `GET /serializacao/json/cliente` retorna `200` com JSON para desserializacao.
- `POST /serializacao/xml/cliente` retorna `200` com XML quando o corpo XML e valido.
- `POST /serializacao/xml/cliente` retorna `400` quando o XML e invalido.
- `GET /serializacao/xml/cliente` retorna `200` com XML para desserializacao.
- `POST /arquivos/upload` retorna `200` com metadados do arquivo enviado.
- `GET /arquivos/download` retorna `200` com arquivo para download.
- `GET /arquivos/metadados` retorna `200` com metadados do arquivo de estudo.
- `GET /comportamento/delay` retorna `200` apos o tempo solicitado.
- `GET /html/estudo` retorna `200` com HTML para estudo.
- `GET /html/clientes` retorna `200` com tabela HTML para XPath.
- Filtros por query param sem resultado retornam `404`.
- Login válido retorna `200` com token fake.
- Login inválido retorna `401`.
- Buscar recurso existente retorna `200`.
- Criar cliente válido retorna `201`.
- Atualizar cliente válido retorna `200`.
- Deletar cliente válido retorna `204`.
- Cliente inexistente retorna `404`.
- Campos obrigatórios ausentes retornam `400`.
- E-mail inválido retorna `400`.
- CPF inválido retorna `400`.
- Cartão inexistente retorna `404`.
- Cartão ativo pode ser bloqueado.
- Cartão bloqueado pode ser desbloqueado.
- Cartão cancelado não pode ser bloqueado nem desbloqueado.
- Fatura inexistente retorna `404`.
- Criar fatura válida retorna `201`.
- Atualizar fatura válida retorna `200`.
- Deletar fatura válida retorna `204`.
- Fatura paga não pode ser deletada e retorna `409`.
- Fatura já paga não pode ser paga novamente e retorna `409`.
- Pagamento com valor diferente do valor da fatura retorna `400`.
- Pagamento valido muda o status da fatura para `PAGA`.
- Todas as mensagens de erro retornam no padrao da API.

## Como usar o endpoint /reset

O endpoint `POST /reset` copia a massa semente de `app/database/initial_db.json` para `app/database/db.json`.

Use antes de testes automatizados para garantir uma base previsível:

```bash
curl -X POST http://localhost:8000/reset
```

Fluxo recomendado:

1. Subir a API com `uvicorn app.main:app --reload`.
2. Chamar `POST /reset`.
3. Executar a suíte de testes no REST Assured ou Cypress.

## Como testar manualmente no Bruno

1. Crie uma collection no Bruno chamada `Pay Lab`.
2. Configure uma variável de ambiente `baseUrl` com `http://localhost:8000`.
3. Crie requests para cada endpoint usando `{{baseUrl}}`.
4. Comece com `POST /reset` para restaurar a massa.
5. Teste casos felizes e negativos:
   - Login válido e inválido
   - Cliente existente e inexistente
   - Cliente com e-mail inválido
   - Cliente com CPF inválido
   - Bloqueio, desbloqueio e cancelamento de cartoes
   - Query params na pasta `Parametros e Headers`
   - Headers de requisição e resposta na pasta `Parametros e Headers`
   - Serializacao e desserializacao na pasta `Serializacao e Desserializacao`
   - Pagamento valido, valor divergente e fatura ja paga

## Uso futuro com REST Assured

Esta API foi pensada para facilitar automação com Java + REST Assured:

- Validação de status code
- Validação de headers, como `Content-Type: application/json`
- Validação de body com JSONPath
- Uso de `pathParam`
- Uso de `queryParam`
- Uso de `header`
- Uso de `contentType` e `accept`
- Uso de API Key, Basic Auth e Bearer Token
- Serializacao de Map para JSON
- Serializacao de objeto Java para JSON
- Desserializacao de resposta JSON para objeto
- Serializacao para XML
- Desserializacao de resposta XML
- Validação de contrato JSON
- Validação de contrato XML
- Uso de schemas em `src/test/resources/schemas`
- Upload multipart/form-data
- Download de arquivos
- Validacao de metadados de arquivo
- Validacao de tempo de resposta e timeout
- Leitura de HTML
- XPath com HTML
- Massa previsível com `POST /reset`

Exemplo de estrategia:

1. Chamar `POST /reset` em um `@BeforeEach`.
2. Testar `GET /clientes/1` esperando `200`.
3. Testar `GET /clientes/999` esperando `404`.
4. Testar `POST /pagamentos` com valor correto e validar fatura como `PAGA`.

## Uso futuro com Cypress

A API tambem funciona bem com Cypress usando `cy.request()`:

```javascript
cy.request("POST", "http://localhost:8000/reset")

cy.request("GET", "http://localhost:8000/clientes/1").then((response) => {
  expect(response.status).to.eq(200)
  expect(response.body.success).to.eq(true)
  expect(response.body.data.id).to.eq(1)
})
```

Para cenarios negativos, use `failOnStatusCode: false`:

```javascript
cy.request({
  method: "POST",
  url: "http://localhost:8000/login",
  failOnStatusCode: false,
  body: {
    usuario: "admin",
    senha: "errada"
  }
}).then((response) => {
  expect(response.status).to.eq(401)
  expect(response.body.success).to.eq(false)
  expect(response.body.error).to.eq("UNAUTHORIZED")
})
```

## Sugestão futura de CI/CD com GitHub Actions

Um fluxo futuro pode:

1. Instalar Python 3.11.
2. Instalar dependências com `pip install -r requirements.txt`.
3. Subir a API em background com `uvicorn app.main:app --host 0.0.0.0 --port 8000`.
4. Chamar `POST /reset`.
5. Rodar testes REST Assured com Maven ou Gradle.
6. Rodar testes Cypress com `npx cypress run`.
7. Publicar relatórios de teste como artefatos.

Exemplo conceitual:

```yaml
name: Pay Lab Tests

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  api-tests:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with:
          python-version: "3.11"
      - run: pip install -r requirements.txt
      - run: uvicorn app.main:app --host 0.0.0.0 --port 8000 &
      - run: curl -X POST http://localhost:8000/reset
      - run: echo "Rodar testes REST Assured e Cypress aqui"
```
