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
- Validação de contrato JSON
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
