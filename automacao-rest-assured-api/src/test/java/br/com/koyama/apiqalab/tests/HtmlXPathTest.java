package br.com.koyama.apiqalab.tests;

import br.com.koyama.apiqalab.base.BaseTest;

public class HtmlXPathTest extends BaseTest {

    // Topicos complementares: trabalhando com HTML e XPath com HTML

    // TODO: implementar teste GET /html/estudo retornando 200.
    // Validar Content-Type text/html.

    // TODO: extrair o corpo da resposta como String.
    // Exemplo de estudo: response.extract().asString().

    // TODO: validar que o HTML contem titulo, h1 e textos esperados.
    // Exemplos: Pay Lab HTML, Resumo de estudos, XPath com HTML.

    // TODO: implementar teste GET /html/clientes retornando 200.
    // Validar que a pagina contem uma tabela com id tabela-clientes.

    // TODO: estudar XPath para localizar elementos por id.
    // Exemplos de XPath para pesquisar:
    // //*[@id='titulo-principal']
    // //*[@id='tabela-clientes']

    // TODO: estudar XPath para localizar elementos por texto.
    // Exemplos de XPath para pesquisar:
    // //td[text()='Cliente Pay Lab']
    // //li[text()='XPath com HTML']

    // TODO: estudar XPath para localizar elementos por atributo.
    // Exemplos de XPath para pesquisar:
    // //tr[@data-cliente-id='1']
    // //li[@data-tipo='xpath']

    // TODO: estudar XPath para localizar elementos por posicao.
    // Exemplo de XPath para pesquisar:
    // //table[@id='tabela-clientes']/tbody/tr[1]/td[2]
}
