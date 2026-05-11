from fastapi import APIRouter
from fastapi.responses import HTMLResponse

router = APIRouter(prefix="/html", tags=["HTML e XPath"])


@router.get(
    "/estudo",
    response_class=HTMLResponse,
    summary="Pagina HTML para estudo",
    description=(
        "Endpoint didatico para estudar resposta HTML no REST Assured. "
        "Use para validar status code, Content-Type, titulo, textos e atributos."
    ),
)
def pagina_html_estudo():
    html = """<!doctype html>
<html lang="pt-BR">
  <head>
    <meta charset="utf-8" />
    <title>Pay Lab HTML</title>
  </head>
  <body data-page="html-estudo">
    <header id="cabecalho">
      <h1 id="titulo-principal">Pay Lab HTML</h1>
      <p class="subtitulo">Pagina didatica para estudos de HTML.</p>
    </header>
    <main id="conteudo">
      <section id="resumo" data-status="ATIVO">
        <h2>Resumo de estudos</h2>
        <p class="descricao">
          Use esta pagina para praticar leitura de HTML em testes de API.
        </p>
        <ul id="topicos">
          <li data-tipo="html">Trabalhando com HTML</li>
          <li data-tipo="xpath">XPath com HTML</li>
        </ul>
        <a id="link-docs" href="/docs">Docs da API</a>
      </section>
    </main>
  </body>
</html>
"""
    return HTMLResponse(content=html)


@router.get(
    "/clientes",
    response_class=HTMLResponse,
    summary="Tabela HTML de clientes",
    description=(
        "Endpoint didatico com tabela HTML previsivel para estudar XPath. "
        "Use seletores por id, atributo, texto e posicao."
    ),
)
def pagina_html_clientes():
    html = """<!doctype html>
<html lang="pt-BR">
  <head>
    <meta charset="utf-8" />
    <title>Clientes HTML</title>
  </head>
  <body data-page="html-clientes">
    <main id="conteudo-clientes">
      <h1>Clientes para XPath</h1>
      <table id="tabela-clientes">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>E-mail</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr data-cliente-id="1">
            <td class="id">1</td>
            <td class="nome">Cliente Pay Lab</td>
            <td class="email">cliente@email.com</td>
            <td class="status">ATIVO</td>
          </tr>
          <tr data-cliente-id="2">
            <td class="id">2</td>
            <td class="nome">Ana Silva</td>
            <td class="email">ana.silva@email.com</td>
            <td class="status">ATIVO</td>
          </tr>
          <tr data-cliente-id="3">
            <td class="id">3</td>
            <td class="nome">Carlos Souza</td>
            <td class="email">carlos.souza@email.com</td>
            <td class="status">INATIVO</td>
          </tr>
        </tbody>
      </table>
      <section id="resumo-clientes">
        <span id="total-clientes">3</span>
        <span id="clientes-ativos">2</span>
      </section>
    </main>
  </body>
</html>
"""
    return HTMLResponse(content=html)
