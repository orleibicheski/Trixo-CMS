/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.publicacao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import br.com.trixo.cms.entity.TrixoCMSDefaultEntity;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.entity.site.Site;

/**
 * @author rafaelabreu
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING, length = 15)
@DiscriminatorValue("ONLINE")
public class Publicacao extends TrixoCMSDefaultEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 8105324976374786160L;

    /**
     * Data de publicação das páginas e arquivos.
     */
    @Column(nullable = true)
    private Date publicacao;

    /**
     * Referência as páginas que devem ser publicadas.
     */
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Collection<Pagina> paginas = new ArrayList<Pagina>();

    /**
     * Site da publicação.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Site site;

    /**
     * Erros ocorridos na publicação
     */
    @OneToMany(mappedBy = "publicacao")
    private Collection<Erro> erros = new ArrayList<Erro>();

    /**
     * Situação
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private SituacaoPublicacao situacao;

    /**
     * @return the publicacao
     */
    public Date getPublicacao() {
        return publicacao;
    }

    /**
     * @param publicacao
     *            the publicacao to set
     */
    public void setPublicacao(Date publicacao) {
        this.publicacao = publicacao;
    }

    /**
     * @return the paginas
     */
    public Collection<Pagina> getPaginas() {
        return paginas;
    }

    /**
     * @param paginas
     *            the paginas to set
     */
    public void setPaginas(Collection<Pagina> paginas) {
        this.paginas = paginas;
    }

    /**
     * Adiciona uma nova página a ser publicada.
     * @param pagina
     *            Referência da página a ser publicada.
     */
    public void addPagina(Pagina pagina) {
        this.paginas.add(pagina);
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
     * @return the erros
     */
    public Collection<Erro> getErros() {
        return erros;
    }

    /**
     * @param erros
     *            the erros to set
     */
    public void setErros(Collection<Erro> erros) {
        this.erros = erros;
    }

    /**
     * @return the situacao
     */
    public SituacaoPublicacao getSituacao() {
        return situacao;
    }

    /**
     * @param situacao
     *            the situacao to set
     */
    public void setSituacao(SituacaoPublicacao situacao) {
        this.situacao = situacao;
    }

}
