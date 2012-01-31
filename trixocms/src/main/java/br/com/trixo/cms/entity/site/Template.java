/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.site;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import br.com.trixo.cms.entity.TrixoCMSDefaultEntity;

/**
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "NOME_UNICO", columnNames = { "SITE_ID", "NOME" }) })
public class Template extends TrixoCMSDefaultEntity {

    /**
     * serialID.
     */
    private static final long serialVersionUID = -7540446405759185830L;

    @Column(length = 100, nullable = false)
    @NotNull
    private String nome;

    @Column(length = 50, nullable = false)
    @NotNull
    private String referencia;

    @Transient
    private String valor;

    /**
     * Encoding utilizado na geração de páginas.
     * TODO abreu: ver se existe uma Collectiona com os possíveis.
     */
    @Column(length = 10, nullable = false)
    @NotNull
    private String encoding = "UTF-8";

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Site site;

    @OneToMany(mappedBy = "template")
    private List<Pagina> paginas = new ArrayList<Pagina>();

    @ManyToMany(targetEntity = Categoria.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Categoria> categorias = new ArrayList<Categoria>();

    /**
     * Construtor padrão.
     */
    public Template() {
        super();
    }

    /**
     * Construtor utilizado somente para as consultas evitarem de retornar o
     * conteúdo do template.
     * @param id
     *            Identificador do templaet.
     * @param nome
     *            Nome do template.
     */
    public Template(Long id, String nome) {
        super();
        setId(id);
        this.nome = nome;
    }

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
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor
     *            the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
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
     * Retorna a Collectiona de páginas relacionadas ao Template. Caso esteja
     * Nula a
     * mesma é inicializa.
     * @return the paginas
     */
    public List<Pagina> getPaginas() {
        return paginas;
    }

    /**
     * @param paginas
     *            the paginas to set
     */
    public void setPaginas(List<Pagina> paginas) {
        this.paginas = paginas;
    }

    /**
     * Retorna a Collectiona de categorias relacionadas ao Template. Caso esteja
     * Nula
     * a mesma é inicializa.
     * @return the categorias
     */
    public List<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * @param categorias
     *            the categorias to set
     */
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding
     *            the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the referencia
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * @param referencia
     *            the referencia to set
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

}
