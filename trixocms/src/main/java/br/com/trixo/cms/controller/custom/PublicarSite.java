package br.com.trixo.cms.controller.custom;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.entity.conteudo.Conteudo;
import br.com.trixo.cms.entity.conteudo.ConteudoAssociado;
import br.com.trixo.cms.entity.conteudo.SituacaoConteudoAssociado;
import br.com.trixo.cms.entity.publicacao.Publicacao;
import br.com.trixo.cms.entity.publicacao.SituacaoPublicacao;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.entity.site.SituacaoPagina;
import br.com.trixo.cms.jms.JMSPublicacoesProducer;
import br.com.trixo.cms.qualifier.SiteSelecionado;
import br.com.trixo.cms.repository.EntityRepo;

/**
 * Controller responsável por solicitar publicação das páginas selecionadas.
 * @author rafaelabreu
 */
@Named
@RequestScoped
public class PublicarSite extends CustomController {

    /**
     * 
     */
    private static final long serialVersionUID = 2453381043549569220L;

    /**
     * Site seleciondado
     */
    @Inject
    @SiteSelecionado
    private Site site;

    /**
     * Array de páginas selecionadas
     */
    private Pagina[] paginasSelecionadas;

    /**
     * Gerenciador de mensagens
     */
    @Inject
    private JMSPublicacoesProducer publicacoesProducer;

    /**
     * Controler de consulta de conteúdos
     */
    @Inject
    private EntityRepo entityRepo;

    /**
     * Titulo para ser utilizado na publicação.
     */
    private static final String TITULO_MENSAGEM = "Publicação";

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Atualiza as páginas selecionadas para situacao SOLICITADA_PUBLICACAO,
     * cria a publicação a ser executada futuramente, atualiza os conteudos
     * relacionados para SOLICITADO_PUBLICACAO e envia uma mensagem para a fila
     * de publicações.
     */
    public void publicar() {

        if (paginasSelecionadas == null || paginasSelecionadas.length == 0) {
            warn(TITULO_MENSAGEM, "Selecione ao menos uma página para publicar.");

            return;
        }

        try {

            List<Pagina> paginas = new ArrayList<Pagina>();

            // atualiza as páginas selecionadas
            for (Pagina pagina : paginasSelecionadas) {

                pagina.setSituacao(SituacaoPagina.SOLICITADA_PUBLICACAO);

                paginas.add(pagina);
            }

            Publicacao publicacao = new Publicacao();

            publicacao.setSite(site);
            publicacao.setPaginas(paginas);
            publicacao.setSituacao(SituacaoPublicacao.SOLICITADA);

            getEntityManager().persist(publicacao);

            List<ConteudoAssociado<Conteudo>> conteudosAssociados = entityRepo.listarPendentesAtualizacaoAgora(site,
                    paginas);

            // Marca os conteudos associados para solicitados
            for (ConteudoAssociado<Conteudo> conteudoAssociado : conteudosAssociados) {
                conteudoAssociado.setSituacao(SituacaoConteudoAssociado.SOLICITADO_PUBLICACAO);

                getEntityManager().persist(conteudoAssociado);
            }

            publicacoesProducer.enviarPublicacao(publicacao);

            info(TITULO_MENSAGEM, "Solicitação realizada com sucesso. Aguarde alguns instantes");

        } catch (Exception e) {

            log.error("Ocorreu um erro na publicação das páginas.", e);

            error(TITULO_MENSAGEM, "Ocorreu um erro na publicação das páginas.");
        }
    }

    /**
     * @param paginasSelecionadas
     *            the paginasSelecionadas to set
     */
    public void setPaginasSelecionadas(Pagina[] paginasSelecionadas) {
        this.paginasSelecionadas = paginasSelecionadas;
    }

    /**
     * @return the paginasSelecionadas
     */
    public Pagina[] getPaginasSelecionadas() {
        return paginasSelecionadas;
    }
}
