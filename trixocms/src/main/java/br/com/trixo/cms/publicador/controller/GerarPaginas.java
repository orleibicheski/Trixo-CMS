package br.com.trixo.cms.publicador.controller;

import java.io.StringWriter;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;

import br.com.trixo.cms.controller.Controller;
import br.com.trixo.cms.entity.conteudo.Conteudo;
import br.com.trixo.cms.entity.publicacao.CausaErro;
import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.entity.site.Template;
import br.com.trixo.cms.jms.qualifier.Publicacoes;
import br.com.trixo.cms.publicador.exception.PublicacaoException;
import br.com.trixo.cms.repository.EntityRepo;

public class GerarPaginas extends Controller {

    /**
     * 
     */
    private static final long serialVersionUID = -540928052143576070L;

    /**
     * VelocityEngine modificado.
     */
    @Inject
    @Publicacoes
    private VelocityEngine ve;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Repositório de conteúdos
     */
    @Inject
    private EntityRepo entityRepo;

    /**
     * Obtém o template.<br>
     * Obtém o Site.<br>
     * Carrega as categorias.<br>
     * Realiza o merge de templates e conteúdos.<br>
     * @param pagina
     *            Página a ser gerada.
     * @return HTML da página gerado.
     */
    public StringWriter gerarPagina(Pagina pagina) throws PublicacaoException {

        Template template = pagina.getTemplate();
        Site site = template.getSite();

        // carrega para o entitymanager e o site para uso do SiteResourceLoader
        ve.setApplicationAttribute("entityManager", getEntityManager());
        ve.setApplicationAttribute("site", site);

        VelocityContext context = new VelocityContext();

        carregaCategorias(pagina, template.getCategorias(), context);
        // terminar de carregar aqui o que é necessário.

        StringWriter paginaHtml = new StringWriter();

        try {
            ve.mergeTemplate(template.getNome(), template.getEncoding(), context, paginaHtml);
        } catch (ParseErrorException e) {

            log.error("Ocorreu um erro na geração da página: " + pagina.getId(), e);

            throw new PublicacaoException(CausaErro.GERAR_PAGINA);
        } catch (Exception e) {
            log.error("Ocorreu um erro desconhecido no gerar página: " + pagina.getId(), e);

            throw new PublicacaoException();
        }

        return paginaHtml;
    }

    /**
     * @param pagina
     *            Página que será carregada
     * @param categorias
     *            Categorias a serem carregadas
     * @param context
     *            destino das coleções de conteúdos
     */
    private void carregaCategorias(Pagina pagina, Collection<Categoria> categorias, VelocityContext context) {

        for (Categoria categoria : categorias) {

            Collection<Conteudo> conteudos = entityRepo.listarAssociados(pagina, categoria);

            context.put("C_" + categoria.getMarcador(), conteudos);
        }
    }

}
