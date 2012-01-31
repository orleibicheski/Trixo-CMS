/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.publicacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import br.com.trixo.cms.entity.TrixoCMSDefaultEntity;

/**
 * Entidade responsável por armazenar informações dos erros ocorridos para uma
 * determinada publicação.
 * @author rafaelabreu
 */
@Entity
public class Erro extends TrixoCMSDefaultEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 3149798304701434030L;

    /**
     * Tipo do erro, utilizado pelo usuário leigo.
     */
    @Column(length = 50, nullable = false)
    @NotNull
    private CausaErro causa;

    /**
     * Detalhes do erro, normalmente com informações mais técnicas.
     */
    @NotNull
    private String detalhes;

    /**
     * Publicação que ocorreu o erro.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Publicacao publicacao;

    /**
     * @param detalhes
     *            the detalhes to set
     */
    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    /**
     * @return the detalhes
     */
    public String getDetalhes() {
        return detalhes;
    }

    /**
     * @return the publicacao
     */
    public Publicacao getPublicacao() {
        return publicacao;
    }

    /**
     * @param publicacao
     *            the publicacao to set
     */
    public void setPublicacao(Publicacao publicacao) {
        this.publicacao = publicacao;
    }

    /**
     * @return the causa
     */
    public CausaErro getCausa() {
        return causa;
    }

    /**
     * @param causa
     *            the causa to set
     */
    public void setCausa(CausaErro causa) {
        this.causa = causa;
    }

}
