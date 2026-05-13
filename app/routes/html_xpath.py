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
        <p id="descricao-estudo" class="descricao">
          Use esta pagina para praticar leitura de HTML em testes de API.
        </p>
        <ul id="topicos">
          <li id="topico-html" data-tipo="html">Trabalhando com HTML</li>
          <li id="topico-xpath" data-tipo="xpath">XPath com HTML</li>
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
      <h1 id="titulo-clientes">Clientes para XPath</h1>
      <table id="tabela-clientes">
        <thead id="cabecalho-tabela-clientes">
          <tr>
            <th id="coluna-id">ID</th>
            <th id="coluna-nome">Nome</th>
            <th id="coluna-email">E-mail</th>
            <th id="coluna-status">Status</th>
          </tr>
        </thead>
        <tbody id="corpo-tabela-clientes">
          <tr id="cliente-1" data-cliente-id="1" data-status="ATIVO">
            <td id="cliente-1-id" class="id" data-campo="id">1</td>
            <td id="cliente-1-nome" class="nome" data-campo="nome">Cliente Pay Lab</td>
            <td id="cliente-1-email" class="email" data-campo="email">cliente@email.com</td>
            <td id="cliente-1-status" class="status" data-campo="status">ATIVO</td>
          </tr>
          <tr id="cliente-2" data-cliente-id="2" data-status="ATIVO">
            <td id="cliente-2-id" class="id" data-campo="id">2</td>
            <td id="cliente-2-nome" class="nome" data-campo="nome">Ana Silva</td>
            <td id="cliente-2-email" class="email" data-campo="email">ana.silva@email.com</td>
            <td id="cliente-2-status" class="status" data-campo="status">ATIVO</td>
          </tr>
          <tr id="cliente-3" data-cliente-id="3" data-status="INATIVO">
            <td id="cliente-3-id" class="id" data-campo="id">3</td>
            <td id="cliente-3-nome" class="nome" data-campo="nome">Carlos Souza</td>
            <td id="cliente-3-email" class="email" data-campo="email">carlos.souza@email.com</td>
            <td id="cliente-3-status" class="status" data-campo="status">INATIVO</td>
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
