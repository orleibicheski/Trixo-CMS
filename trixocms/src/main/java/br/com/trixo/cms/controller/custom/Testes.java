package br.com.trixo.cms.controller.custom;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.trixo.cms.controller.entity.CategoriaEntity;
import br.com.trixo.cms.controller.entity.PaginaEntity;
import br.com.trixo.cms.controller.query.PaginaQuery;
import br.com.trixo.cms.entity.conteudo.Conteudo;
import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.publicador.controller.GerarPaginas;
import br.com.trixo.cms.publicador.controller.Publicador;
import br.com.trixo.cms.publicador.exception.PublicacaoException;
import br.com.trixo.cms.repository.EntityRepo;

/**
 * Controller responsável por solicitar publicação das páginas selecionadas.
 * @author rafaelabreu
 */
@Named
@RequestScoped
public class Testes extends CustomController {

    /**
     * 
     */
    private static final long serialVersionUID = 1639721223210749909L;

    @Inject
    private PaginaQuery paginaQuery;

    @Inject
    private PaginaEntity paginaEntity;

    @Inject
    private CategoriaEntity categoriaEntity;

    @Inject
    private PublicarSite publicarSite;

    @Inject
    private Conversation conversation;

    @Inject
    private EntityRepo entityRepo;

    @Inject
    private GerarPaginas gerarPaginas;

    @Inject
    private Publicador publicador;

    /**
     * Atualiza as páginas selecionadas para situacao SOLICITADA_PUBLICACAO,
     * cria a publicação a ser executada futuramente, atualiza os conteudos
     * relacionados para SOLICITADO_PUBLICACAO e envia uma mensagem para a fila
     * de publicações.
     */
    public void testes() {

        publicadorAtalho();
    }

    private void publicadorAtalho() {
        publicador.publicar(65L);
    }

    private void gerarPagina() throws PublicacaoException {

        paginaEntity.setId(6L);

        Pagina pagina = paginaEntity.getInstance();

        System.out.println(gerarPaginas.gerarPagina(pagina).toString());

    }

    private void pesquisarRapio() {

        paginaEntity.setId(6L);

        categoriaEntity.setId(3L);

        Pagina pagina = paginaEntity.getInstance();

        Categoria categoria = categoriaEntity.getInstance();

        Collection<Conteudo> conteudos = entityRepo.listarAssociados(pagina, categoria);

        for (Conteudo conteudo : conteudos) {
            System.out.println(conteudo.getDescricao());
        }

    }

    private void publicarRapido() {
        conversation.begin();

        paginaQuery.query();

        List<Pagina> paginas = paginaQuery.getResult();

        for (Pagina pagina : paginas) {

            paginaEntity.setId(pagina.getId());

            paginaEntity.getInstance().setNome(pagina.getNome() + System.currentTimeMillis());

            paginaEntity.update();
        }

        Pagina[] array = new Pagina[paginas.size()];

        publicarSite.setPaginasSelecionadas(paginas.toArray(array));

        publicarSite.publicar();
    }
}
