/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import br.com.trixo.cms.entity.TrixoCMSDefaultEntity;
import br.com.trixo.cms.entity.conteudo.TipoConteudo;

/**
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "MARCADOR_UNICO", columnNames = { "SITE_ID", "MARCADOR" }) })
public class Categoria extends TrixoCMSDefaultEntity {

    /**
     * serialID.
     */
    private static final long serialVersionUID = 3043950891749940191L;

    @Column(length = 100, nullable = false)
    @NotNull
    private String marcador;

    @Column(length = 255, nullable = false)
    @NotNull
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private TipoConteudo tipo;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Site site;

    @ManyToMany(mappedBy = "categorias", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Collection<Template> templates = new ArrayList<Template>();

    /**
     * Construtor padrão.
     */
    public Categoria() {
        super();
    }

    /**
     * Método utilizado na construção dos Objetos do tipo Categoria retornados
     * pelo JPAQL.
     * @param id
     *            Identificador único da Página
     * @param marcador
     *            Marcador do template
     * @param descricao
     *            Descrição da categoria
     * @param alteracao
     *            Data que informa a última alteração realiza na referência do
     *            objeto que está sendo restaurado
     */
    public Categoria(Long id, String marcador, String descricao, Date alteracao) {
        super();
        setId(id);
        setAlteracao(alteracao);
        this.marcador = marcador;
        this.descricao = descricao;
    }

    /**
     * @return the marcador
     */
    public String getMarcador() {
        return marcador;
    }

    /**
     * @param marcador
     *            the marcador to set
     */
    public void setMarcador(String marcador) {
        this.marcador = marcador;
    }

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
     * @return the tipo
     */
    public TipoConteudo getTipo() {
        return tipo;
    }

    /**
     * @param tipo
     *            the tipo to set
     */
    public void setTipo(TipoConteudo tipo) {
        this.tipo = tipo;
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
     * @return Variável a ser utilizada no template.
     */
    public String getVariavelTemplate() {

        if (marcador != null && !"".equals(marcador))
            return "${C_" + marcador + "}";
        else
            return "";
    }
}
