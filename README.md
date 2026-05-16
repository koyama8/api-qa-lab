# Pay Lab API

API REST criada para estudo e portfolio de qualidade de software, automacao de testes de API e validacao de regras de negocio em um dominio financeiro simples.

O projeto simula operacoes de login, clientes, cartoes, faturas e pagamentos. Ele tambem inclui endpoints didaticos para estudar headers, query params, path params, autenticacao, serializacao, desserializacao, contratos, arquivos, tempo de resposta, HTML e XPath.

## Destaques do Projeto

- API REST com FastAPI, Pydantic e persistencia em JSON.
- Documentacao interativa via Swagger em `/docs`.
- Massa de dados previsivel com `POST /reset`.
- Cenarios positivos e negativos para testes manuais e automatizados.
- Collection manual no Bruno para exploracao dos endpoints.
- Projeto Java separado para automacao com REST Assured, JUnit 5, Maven e Hamcrest.
- Preparado para evolucao futura com Cypress, CI/CD e GitHub Actions.

## Stack Tecnica

| Area | Tecnologias |
| --- | --- |
| API | Python, FastAPI, Uvicorn, Pydantic |
| Persistencia | Arquivos JSON |
| Testes manuais | Bruno |
| Automacao de API | Java 21, Maven, REST Assured, JUnit 5, Hamcrest |
| Contratos | JSON Schema, XML Schema |
| Qualidade | SonarLint, organizacao por camadas, massa resetavel |

## Estrutura do Repositorio

```text
api-lab-QAKoyama/
|-- app/
|   |-- database/
|   |   |-- db.json
|   |   |-- initial_db.json
|   |-- routes/
|   |   |-- auth.py
|   |   |-- clientes.py
|   |   |-- cartoes.py
|   |   |-- faturas.py
|   |   |-- pagamentos.py
|   |   |-- contratos.py
|   |   |-- serializacao.py
|   |   |-- arquivos.py
|   |   |-- html_xpath.py
|   |-- schemas/
|   |-- services/
|   |-- static/
|   |-- utils/
|   |-- main.py
|-- automacao-rest-assured-api/
|   |-- src/test/java/br/com/koyama/apiqalab/
|   |   |-- base/
|   |   |-- payloads/
|   |   |-- responses/
|   |   |-- tests/
|   |   |-- utils/
|   |-- src/test/resources/
|   |   |-- payloads/
|   |   |-- schemas/
|   |-- pom.xml
|-- requirements.txt
|-- README.md
```

## Como Rodar a API

Entre na raiz do projeto:

```bash
cd api-lab-QAKoyama
```

Crie e ative o ambiente virtual:

```bash
python -m venv .venv
```

Windows PowerShell:

```powershell
.\.venv\Scripts\Activate.ps1
```

Instale as dependencias:

```bash
pip install -r requirements.txt
```

Suba a API:

```bash
uvicorn app.main:app --reload
```

Acesse:

```text
API:     http://localhost:8000
Swagger: http://localhost:8000/docs
Health:  http://localhost:8000/health
```

## Padrao de Resposta

Sucesso:

```json
{
  "success": true,
  "message": "Operacao realizada com sucesso",
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

Operacoes de exclusao bem-sucedidas podem retornar `204 No Content`.

## Principais Endpoints

| Dominio | Endpoints |
| --- | --- |
| Sistema | `GET /health`, `POST /reset`, `GET /headers` |
| Auth | `POST /login`, `GET /auth/publica`, `GET /auth/api-key`, `GET /auth/basic`, `GET /auth/bearer` |
| Clientes | `GET /clientes`, `GET /clientes/{id}`, `POST /clientes`, `PUT /clientes/{id}`, `DELETE /clientes/{id}` |
| Cartoes | `GET /cartoes`, `GET /cartoes/{id}`, `POST /cartoes`, `PUT /cartoes/{id}/bloquear`, `PUT /cartoes/{id}/desbloquear`, `PUT /cartoes/{id}/cancelar` |
| Faturas | `GET /faturas`, `GET /faturas/{id}`, `POST /faturas`, `PUT /faturas/{id}`, `DELETE /faturas/{id}`, `GET /clientes/{id}/faturas` |
| Pagamentos | `POST /pagamentos` |
| Contratos | `GET /contratos/json/health`, `GET /contratos/json/cliente`, `GET /contratos/xml/cliente`, `GET /contratos/diferenca` |
| Estudos REST Assured | `/serializacao/*`, `/arquivos/*`, `/comportamento/delay`, `/html/*` |

## Dados Iniciais

A massa inicial fica em `app/database/initial_db.json` e pode ser restaurada a qualquer momento:

```bash
curl -X POST http://localhost:8000/reset
```

Dados base:

- Clientes: IDs `1`, `2` e `3`.
- Cartoes: IDs `1`, `2` e `3`.
- Faturas: IDs `1`, `2` e `3`.
- Pagamento inicial: ID `1`.

Estados relevantes:

- Cartao `1`: `ATIVO`.
- Cartao `2`: `BLOQUEADO`.
- Cartao `3`: `CANCELADO`.
- Fatura `1`: `ABERTA`, valor `1200.50`.
- Fatura `2`: `FECHADA`, valor `800.00`.
- Fatura `3`: `PAGA`, valor `300.00`.

## Regras de Negocio Cobertas

- Login valido retorna token fake.
- Login invalido retorna `401`.
- API Key, Basic Auth e Bearer Token possuem cenarios validos e invalidos.
- Cliente existente retorna `200`; cliente inexistente retorna `404`.
- Criacao de cliente valida retorna `201`.
- E-mail e CPF invalidos retornam `400`.
- Cartao cancelado nao pode ser bloqueado ou desbloqueado.
- Filtros sem resultado retornam `404`.
- Fatura paga nao pode ser paga novamente.
- Pagamento com valor divergente retorna `400`.
- Pagamento valido altera a fatura para `PAGA`.
- Upload, download e metadados de arquivo possuem endpoints dedicados.
- Contratos JSON e XML possuem arquivos de schema no projeto de automacao.

## Automacao REST Assured

O modulo `automacao-rest-assured-api` contem a suite Java de testes automatizados de API.

Coberturas principais:

- Base de testes com `baseURI`, `Content-Type`, `Accept` e logs.
- Health check.
- Autenticacao e autorizacao.
- CRUD de clientes, cartoes e faturas.
- Pagamentos e regras financeiras.
- Query params, path params e headers.
- Serializacao e desserializacao JSON/XML.
- Validacao de contrato JSON/XML.
- Upload, download, metadados e tempo de resposta.
- HTML e XPath.

Leia a documentacao especifica em:

```text
automacao-rest-assured-api/README.md
```

## Testes Manuais no Bruno

O projeto foi organizado para estudo manual no Bruno com pastas por dominio:

- Sistema.
- Auth.
- Autenticacoes.
- Clientes.
- Cartoes.
- Faturas.
- Pagamentos.
- Parametros e Headers.
- Serializacao e Desserializacao.
- Contratos e Schemas.
- Arquivos e Comportamento.
- HTML e XPath.

Fluxo recomendado:

1. Subir a API localmente.
2. Executar `POST /reset`.
3. Validar endpoints no Bruno.
4. Reproduzir os cenarios na suite REST Assured.

## Roadmap

- Estabilizar execucao completa da suite automatizada.
- Revisar nomes e organizacao dos testes para padrao profissional.
- Evoluir relatorios e evidencias de execucao.
- Adicionar pipeline CI/CD com GitHub Actions.
- Criar modulo futuro para estudos com Cypress e `cy.request()`.

## Competencias Demonstradas

API Testing, REST Assured, Java, JUnit 5, Maven, FastAPI, Python, Pydantic, JSON Schema, XML Schema, Bruno, HTTP, REST, CRUD, autenticacao, contratos, massa de dados, testes negativos, testes positivos, validacao de regras de negocio e automacao de qualidade.
