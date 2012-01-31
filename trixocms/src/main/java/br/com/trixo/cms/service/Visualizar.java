package br.com.trixo.cms.service;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * TODO Mover essa classe deste pacote.
 */
@Path("/visualizar")
public class Visualizar {

    @GET
    @Path("/pagina/{id}")
    public String getPaginaHtml(@PathParam("id") Long id) {

        // para cada página pesquisar as categorias relacionadas

        // para cada categoria pesquisar os conteúdos relacionados a página

        // gerar o html de cada uma das páginas

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < 1000; i++) {
            sb.append("Teste \n");
        }

        return "Teste 123" + id + sb.toString();
    }

    @GET
    @Path("/arquivo/{id}")
    @Produces("application/x-octet-stream")
    public void getConteudoArquivo(@PathParam("id") Long id, @Context HttpServletResponse response) {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < 1000; i++) {
            sb.append("Teste \n");
        }

        // eturn "Teste 123" + id + sb.toString();
    }

}
