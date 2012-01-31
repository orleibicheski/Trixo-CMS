/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.entity;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.entity.site.SituacaoPagina;
import br.com.trixo.cms.entity.site.Template;
import br.com.trixo.cms.qualifier.SiteSelecionado;

/**
 * @author Orlei Bicheski
 */
@Named
@ConversationScoped
public class PaginaEntity extends EntityController<Pagina, Long> {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = 2580828524053377571L;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Referência do Template selecionada na tela de manutenção de Página.
     */
    private Long templateSelecionadoId;

    /**
     * Referência do Site.
     */
    @Inject
    @SiteSelecionado
    private Site site;

    /**
     * Referência da classe Entity do objeto Site.
     */
    @Inject
    private SiteEntity siteEntity;

    /**
     * Referência do Entity TransactionController do objeto Template.
     */
    @Inject
    private TemplateEntity templateEntity;

    /**
     * @return the templateSelecionadoId
     */
    public Long getTemplateSelecionadoId() {
        return templateSelecionadoId;
    }

    /**
     * @param templateSelecionadoId
     *            the templateSelecionadoId to set
     */
    public void setTemplateSelecionadoId(Long templateSelecionadoId) {
        this.templateSelecionadoId = templateSelecionadoId;
    }

    /**
     * Carrega todas as referências de Sites registrados na base de dados.
     */
    @Produces
    @Named("paginas")
    public List<Pagina> listar() {
        log.info("Metodo Listar executado.");

        String jpaQL = "select p from Pagina p where p.site.id = :id";

        try {
            if (log.isDebugEnabled()) {
                if (getEntityManager() != null) {
                    log.debug("EntityManager inicializado com sucesso.");
                }
            }

            TypedQuery<Pagina> query = getEntityManager().createQuery(jpaQL, Pagina.class);
            query.setParameter("id", site.getId());

            List<Pagina> resultado = query.getResultList();
            if (log.isDebugEnabled()) {
                log.debug("Query executada com sucesso.");
                if (resultado != null && !resultado.isEmpty()) {
                    log.debug("Lista de Paginas carregada com sucesso.");
                    log.debug("Foram encontradas ".concat(String.valueOf(resultado.size())).concat(" Paginas."));
                }
            }
            return resultado;
        } catch (Exception e) {
            log.error("Problemas na execucao da Query: ".concat(jpaQL), e);
        }
        return new ArrayList<Pagina>();
    }

    /**
     * Prepara as informações básicas de uma página. Informa o Template e o Site
     * selecionados.
     */
    private void prepararAlteracao() {

        if (templateSelecionadoId != null) {
            templateEntity.setId(templateSelecionadoId);
            Template template = templateEntity.getInstance();
            if (template == null) {
                String mensagem = "A referência do Template esta nula.";
                log.error(mensagem);
                throw new IllegalArgumentException(mensagem);
            }
            getInstance().setTemplate(template);
        }
    }

    /**
     * Prepara as informações básicas de uma página. Informa o Template e o Site
     * selecionados.
     */
    private void prepararNovo() {
        siteEntity.setId(this.site.getId());
        Site site = siteEntity.getInstance();
        if (site == null) {
            String mensagem = "A referência do Site esta nula.";
            log.error(mensagem);
            throw new IllegalArgumentException(mensagem);
        }
        getInstance().setSite(site);

        templateEntity.setId(templateSelecionadoId);
        Template template = templateEntity.getInstance();
        if (template == null) {
            String mensagem = "A referência do Template esta nula.";
            log.error(mensagem);
            throw new IllegalArgumentException(mensagem);
        }
        getInstance().setTemplate(template);
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#view()
     */
    @Override
    public String view() {
        String forward = super.view();
        setTemplateSelecionadoId(getInstance().getTemplate().getId());
        return forward;
    }

    /**
     * TODO Comentário
     * @return Forward
     */
    public String viewConteudo() {
        super.view();
        return "painel";
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#persist()
     */
    @Override
    public String persist() {
        prepararNovo();

        // seta situação para criada.
        getInstance().setSituacao(SituacaoPagina.CRIADA);

        return super.persist();
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#update()
     */
    @Override
    public String update() {
        prepararAlteracao();

        // seta situação para criada.
        getInstance().setSituacao(SituacaoPagina.AGUARDANDO_PUBLICACAO);

        return super.update();
    }

    @Override
    protected Long converterIdentificador(String value) {
        return Long.parseLong(value);
    }
}
