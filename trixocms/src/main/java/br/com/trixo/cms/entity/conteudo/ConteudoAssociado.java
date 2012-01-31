/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.conteudo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.trixo.cms.entity.TrixoCMSDefaultEntity;
import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Pagina;

/**
 * Enitidade que mantem o relacionamento entre o conteúdo, categoria e a página.
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ConteudoAssociado<E extends Conteudo> extends TrixoCMSDefaultEntity {

    /**
     * serialID.
     */
    private static final long serialVersionUID = -5868931556625055606L;

    /**
     * Referência da Página associada ao conteúdo.
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(nullable = false)
    @NotNull
    private Pagina pagina;

    /**
     * Referência da Categoria do Conteúdo associado.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Categoria categoria;

    /**
     * Situação do conteúdo associado.
     */
    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false)
    @NotNull
    private SituacaoConteudoAssociado situacao = SituacaoConteudoAssociado.CRIADO;

    /**
     * A partir desta data que o conteúdo torna-se legível para publicação.
     */
    @JoinColumn(nullable = true)
    private Date agendadoDe;

    /**
     * Até esta data o conteúdo está legível para publicações.
     */
    @JoinColumn(nullable = true)
    private Date agendadoAte;

    /**
     * Referência de {@link Map} que deverá ser utilizado para armazenar dos
     * dados retornados pelas consultas que buscam dados básicos dos objetos do
     * tipo Conteúdo Associado.
     */
    @Transient
    private Map<String, Object> map = new HashMap<String, Object>();

    /**
     * Construtor padrão.
     */
    public ConteudoAssociado() {
        super();
    }

    /**
     * Contrutor utilizado no retorno da consulta utilizada na montagem da
     * listagem do conteúdo.
     * @param id
     *            Identificador único do conteúdo associado
     *            {@link DownloadAssociado}
     * @param categoriaTipo
     *            Referência para o Enumerado que define o nome do tipo de
     *            categoria
     * @param categoriaDescricao
     *            Descrição da categoria associada ao conteúdo
     * @param conteudoId
     *            Identificador único do Conteúdo
     * @param conteudoDescricao
     *            Descrição do Conteúdo
     */
    public ConteudoAssociado(Long id, TipoConteudo categoriaTipo, String categoriaDescricao, Long conteudoId,
            String conteudoDescricao) {
        super();
        setId(id);
        map.put("categoriaTipo", categoriaTipo.name());
        map.put("categoriaTipoDescricao", categoriaTipo.getDescricao());
        map.put("categoriaDescricao", categoriaDescricao);
        map.put("conteudoId", conteudoId);
        map.put("conteudoDescricao", conteudoDescricao);
    }

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
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria
     *            the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the map
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * @param map
     *            the map to set
     */
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    /**
     * @return the situacao
     */
    public SituacaoConteudoAssociado getSituacao() {
        return situacao;
    }

    /**
     * @param situacao
     *            the situacao to set
     */
    public void setSituacao(SituacaoConteudoAssociado situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the agendadoDe
     */
    public Date getAgendadoDe() {
        return agendadoDe;
    }

    /**
     * @param agendadoDe the agendadoDe to set
     */
    public void setAgendadoDe(Date agendadoDe) {
        this.agendadoDe = agendadoDe;
    }

    /**
     * @return the agendadoAte
     */
    public Date getAgendadoAte() {
        return agendadoAte;
    }

    /**
     * @param agendadoAte the agendadoAte to set
     */
    public void setAgendadoAte(Date agendadoAte) {
        this.agendadoAte = agendadoAte;
    }

    /**
     * Retorna a referência do conteúdo associado a página.
     * @return E referência do Conteúdo associado a página
     */
    public abstract E getConteudo();

    /**
     * @return Condicional para verificar se o conteúdo é ou não legivel para
     *         publicação.
     */
    public boolean isLegivel() {

        boolean legivel = true;

        if (getAgendadoDe() != null)
            legivel &= getAgendadoDe().after(new Date());

        if (getAgendadoDe() != null)
            legivel &= getAgendadoDe().before(new Date());

        return legivel;
    }

    /**
     * Método que verifica se o conteúdo é passível de ser agendado para
     * publicação.
     * 
     * @return boolean TRUE: Passível de gerar uma publicação agendado e FALSE:
     *         caso não seja passível de gerar uma publicação agendada
     */
    public boolean isAgendado() {
	return getAgendadoDe() != null && !isLegivel();
    }
}
