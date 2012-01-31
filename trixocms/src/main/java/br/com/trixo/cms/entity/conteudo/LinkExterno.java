/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.conteudo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
@Table
@DiscriminatorValue("LINK_EXTERNO")
public class LinkExterno extends Link {

    /**
     * serial ID.
     */
    private static final long serialVersionUID = 5187436007660193021L;

    /**
     * URL de acesso externo ao Site.
     */
    @Column(length = 100, nullable = false)
    @NotNull
    private String url;

    /**
     * Referência para a coleção de conteúdos associados.
     */
    @OneToMany(mappedBy = "conteudo", cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    private Collection<LinkExternoAssociado> linksExternos = new ArrayList<LinkExternoAssociado>();

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the linksExternos
     */
    public Collection<LinkExternoAssociado> getLinksExternos() {
        return linksExternos;
    }

    /**
     * @param linksExternos the linksExternos to set
     */
    public void setLinksExternos(Collection<LinkExternoAssociado> linksExternos) {
        this.linksExternos = linksExternos;
    }
}
