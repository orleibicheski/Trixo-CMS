/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.conteudo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.trixo.cms.entity.TrixoCMSDefaultEntity;
import br.com.trixo.cms.entity.site.Site;

/**
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Conteudo extends TrixoCMSDefaultEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -8828401056346691262L;

    @Column(length = 255, nullable = false)
    @NotNull
    private String descricao;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Site site;

    /**
     * Utilizado na visualização de páginas.
     */
    @Transient
    private transient boolean visualizacao = false;

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao
     *            the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the site
     */
    public Site getSite() {
        return site;
    }

    /**
     * @param site
     *            the site to set
     */
    public void setSite(Site site) {
        this.site = site;
    }

    /**
     * @return the visualizacao
     */
    public boolean isVisualizacao() {
        return visualizacao;
    }

    /**
     * @param visualizacao
     *            the visualizacao to set
     */
    public void setVisualizacao(boolean visualizacao) {
        this.visualizacao = visualizacao;
    }

}
