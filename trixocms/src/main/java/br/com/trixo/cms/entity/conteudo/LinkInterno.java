/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.conteudo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.trixo.cms.entity.site.Pagina;

/**
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
@Table
@DiscriminatorValue("LINK_INTERNO")
public class LinkInterno extends Link {

    /**
     * serial ID.
     */
    private static final long serialVersionUID = 2334821445402971355L;

    @ManyToOne
    @JoinColumn(nullable = true)
    @NotNull
    private Pagina pagina;

    /**
     * Referência para a coleção de conteúdos associados.
     */
    @OneToMany(mappedBy = "conteudo", cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    private Collection<LinkInternoAssociado> linksInternos = new ArrayList<LinkInternoAssociado>();

    /**
     * @return the pagina
     */
    public Pagina getPagina() {
        return pagina;
    }

    /**
     * @param pagina
     *            the pagina to set
     */
    public void setPagina(Pagina pagina) {
        this.pagina = pagina;
    }

    /**
     * @return the linksInternos
     */
    public Collection<LinkInternoAssociado> getLinksInternos() {
        return linksInternos;
    }

    /**
     * @param linksInternos the linksInternos to set
     */
    public void setLinksInternos(Collection<LinkInternoAssociado> linksInternos) {
        this.linksInternos = linksInternos;
    }
}
