package br.com.trixo.cms.publicador.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.controller.Controller;
import br.com.trixo.cms.controller.entity.PublicacaoEntity;
import br.com.trixo.cms.entity.conteudo.Conteudo;
import br.com.trixo.cms.entity.publicacao.Erro;
import br.com.trixo.cms.entity.publicacao.Publicacao;
import br.com.trixo.cms.entity.publicacao.PublicacaoAgendada;
import br.com.trixo.cms.entity.publicacao.SituacaoPublicacao;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.publicador.exception.PublicacaoException;

/**
 * Bean responsável por realizar todo o processo de publicação de uma
 * solicitação de atualização de páginas e conteúdos.
 * @author rafaelabreu
 */
public class Publicador extends Controller {

    /**
     * 
     */
    private static final long serialVersionUID = -4451649176714147007L;

    @Inject
    private ArmazenarHostSite armazenar;

    @Inject
    private GerarPaginas gerarPaginas;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Controller para obter e manipular a publicação.
     */
    @Inject
    private PublicacaoEntity publicacaoEntity;

    /**
     * @param idPublicacao
     *            Identificador da publicação que será realizada.
     */
    public void publicar(Long idPublicacao) {

        publicacaoEntity.setId(idPublicacao);

        Publicacao publicacao = null;

        try {
            publicacao = publicacaoEntity.getInstance();
        } catch (EntityNotFoundException e) {

            log.error("Publicacao não encontrada!" + publicacaoEntity.getId(), e);

            return;
            // OK
        }

        if (publicacao == null)
            return;

        publicacao.setPublicacao(new Date());

        Site site = publicacao.getSite();

        armazenar.carregar(site, publicacao);

        try {
            armazenar.conectar();

            armazenar.verificaDiretorios();

            Collection<Pagina> paginas = publicacao.getPaginas();

            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);

            armazenar.logArquivoPublicacao("Iniciando publicação: " + publicacao.getId() + " em "
                    + dateFormat.format(publicacao.getPublicacao()));

            for (Pagina pagina : paginas) {

                try {
                    StringWriter paginaHtml = null;

                    try {
                        armazenar.logArquivoPublicacao("Gerando pagina: " + pagina.getNomeArquivo());

                        paginaHtml = gerarPaginas.gerarPagina(pagina);

                        armazenar.logArquivoPublicacao("Enviando pagina: " + pagina.getNomeArquivo()
                                + " para pasta temporaria: " + armazenar.diretorioPaginasTemporarias());

                        armazenar.enviarPagina(pagina, paginaHtml);
                    } finally {
                        try {
                            paginaHtml.close();
                        } catch (IOException e) {
                            log.error("Ocorreu um erro ao fechar o StringWriter da página.", e);
                        }
                    }
                } catch (PublicacaoException e) {
                    adicionaErro(publicacao, e);
                }
            }

            // verifica se existem erros
            if (publicacao.getErros().isEmpty()) {

                for (Pagina pagina : paginas) {

                    try {
                        armazenar.logArquivoPublicacao("Publicando pagina: " + pagina.getNomeArquivo()
                                + " para pasta: " + armazenar.diretorioHost());

                        armazenar.publicarPagina(pagina);
                    } catch (PublicacaoException e) {
                        adicionaErro(publicacao, e);
                    }
                }
            }

            // armazenar.logArquivoPublicacao("Removendo pasta temporária: " +
            // armazenar.diretorioPaginasTemporarias());
            //
            // armazenar.limparPastaTemporarias();

            // verifica se existem erros novamente
            if (publicacao.getErros().isEmpty()) {
                // testar o required new aqui
                // caso exista deve ser salvo a publicacao como sucesso e
                // atualizado os conteúdos e páginas como publicados
                publicacao.setSituacao(SituacaoPublicacao.CONCLUIDA);

            } else {
                // testar o required new aqui
                // caso exista deve ser salvo a publicação com erros e

                publicacao.setSituacao(SituacaoPublicacao.ERRO);

            }

            getEntityManager().persist(publicacao);

            // TODO remover o arquivo anterior e substitui-lo por um mais
            // completo

        } catch (PublicacaoException e) {
            adicionaErro(publicacao, e);
        }

        armazenar.desconectar();
    }

    /**
     * @param publicacao
     *            Publicação que receberá os erros
     * @param e
     *            Exception com os dados do erro
     */
    private void adicionaErro(Publicacao publicacao, PublicacaoException e) {
        Erro erro = new Erro();

        erro.setPublicacao(publicacao);
        erro.setCausa(e.getCausa());

        publicacao.getErros().add(erro);
    }

    /**
     * @param idPublicacao
     *            Identificador da publicação agendada que será processada.
     */
    public void executarAgendamento(Long idPublicacao) {

        PublicacaoAgendada publicacao = null;

        // atualizaSituacao(publicacao.getTextosCurto());
        // atualizaSituacao(publicacao.getDownloads());

        publicacao.setSituacao(SituacaoPublicacao.SOLICITADA);

        getEntityManager().persist(publicacao);

        // TODO adicionar a fila de publicações uma publicação
    }

    private void atualizaSituacao(Collection<? extends Conteudo> conteudos) {
        for (Conteudo conteudo : conteudos) {

            // if (conteudo.isLegivel())
            // conteudo.setSituacao(SituacaoConteudoAssociado.SOLICITADO_PUBLICACAO);
            // else
            // conteudo.setSituacao(SituacaoConteudoAssociado.SOLICITADO_DESPUBLCIACAO);

            // ENCONTRAR AS PAGINAS DO CONTEUDO
        }

    }
}
