/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.entity;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.entity.conteudo.TipoConteudo;
import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.qualifier.SiteSelecionado;

/**
 * @author Orlei Bicheski
 */
@Named
@ConversationScoped
@FacesConverter("categoriaConverter")
public class CategoriaEntity extends EntityController<Categoria, Long> {

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
     * Referência do Site.
     */
    @Inject
    @SiteSelecionado
    private Site site;

    /**
     * Tipo de Conteúdo da Categoria.
     */
    private Integer tipo;

    /**
     * Referência da classe Entity do objeto Site.
     */
    @Inject
    private SiteEntity siteEntity;

    /**
     * @return the tipo
     */
    public Integer getTipo() {
        return tipo;
    }

    /**
     * @param tipo
     *            the tipo to set
     */
    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#view(java.io.Serializable)
     */
    @Override
    public String view() {
        String forward = super.view();
        setTipo(getInstance().getTipo().ordinal());
        return forward;
    }

    /**
     * Prepara as informações básicas de uma categoria. Informa o Site
     * selecionado.
     */
    private void preparar() {
        siteEntity.setId(this.site.getId());
        Site site = siteEntity.getInstance();
        if (site == null) {
            String mensagem = "A referência do Site esta nula.";
            log.error(mensagem);
            throw new IllegalArgumentException(mensagem);
        }
        getInstance().setSite(site);

        for (TipoConteudo tipoConteudo : TipoConteudo.values()) {
            if (tipoConteudo.ordinal() == tipo) {
                getInstance().setTipo(tipoConteudo);
                break;
            }
        }
        if (getInstance().getTipo() == null) {
            String mensagem = "A referência do Tipo de Conteudo esta nula.";
            log.error(mensagem);
            throw new IllegalArgumentException(mensagem);
        }
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#persist()
     */
    @Override
    public String persist() {
        preparar();
        return super.persist();
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#update()
     */
    @Override
    public String update() {
        preparar();
        return super.update();
    }

    /**
     * Retorna uma lista contendo todos os Enumerados referentes ao
     * TipoConteudo. A lista de Enumerados está organizada em objetos do tipo
     * {@link SelectItem}.
     * @return Lista de itens selecionáveis de TipoConteúdo
     */
    @Produces
    @Named("tiposConteudo")
    public List<SelectItem> getTiposConteudo() {
        List<SelectItem> retorno = new ArrayList<SelectItem>();

        for (TipoConteudo tipo : TipoConteudo.values()) {
            retorno.add(new SelectItem(tipo.ordinal(), tipo.getDescricao()));
        }
        return retorno;
    }

    @Override
    protected Long converterIdentificador(String value) {
        return Long.parseLong(value);
    }
}
