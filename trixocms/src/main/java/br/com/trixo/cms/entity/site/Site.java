/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.site;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.trixo.cms.entity.TrixoCMSDefaultEntity;
import br.com.trixo.cms.entity.conteudo.Conteudo;

/**
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
@NamedQuery(name = "sites", query = "select s from Site s")
@Table(uniqueConstraints = { @UniqueConstraint(name = "NOME_UNICO", columnNames = { "NOME" }) })
public class Site extends TrixoCMSDefaultEntity {

    /**
     * serialID.
     */
    private static final long serialVersionUID = -7036606070625730372L;

    @Column(length = 100, nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String nome;

    /**
     * Host com os dados de armazenamento do site.
     */
    @OneToOne(mappedBy = "site", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @NotNull
    private Host host = new Host();

    @OneToMany(mappedBy = "site")
    private Collection<Pagina> paginas = new ArrayList<Pagina>();

    @OneToMany(mappedBy = "site")
    private Collection<Template> templates = new ArrayList<Template>();

    @OneToMany(mappedBy = "site")
    private Collection<Categoria> categorias = new ArrayList<Categoria>();

    @OneToMany(mappedBy = "site")
    private Collection<Conteudo> conteudos = new ArrayList<Conteudo>();

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * @return the templates
     */
    public Collection<Template> getTemplates() {
        return templates;
    }

    /**
     * @param templates
     *            the templates to set
     */
    public void setTemplates(Collection<Template> templates) {
        this.templates = templates;
    }

    /**
     * @return the categorias
     */
    public Collection<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * @param categorias
     *            the categorias to set
     */
    public void setCategorias(Collection<Categoria> categorias) {
        this.categorias = categorias;
    }

    /**
     * @return the host
     */
    public Host getHost() {
        return host;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(Host host) {
        this.host = host;
    }

    /**
     * @return the conteudos
     */
    public Collection<Conteudo> getConteudos() {
        return conteudos;
    }

    /**
     * @param conteudos
     *            the conteudos to set
     */
    public void setConteudos(Collection<Conteudo> conteudos) {
        this.conteudos = conteudos;
    }

}