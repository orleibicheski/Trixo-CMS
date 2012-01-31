/**
 * Copyright (c) 200 3-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import br.com.trixo.cms.entity.listener.TrixoCMSEntityListener;

/**
 * Entidade abstrata responsável pelo padrão de entidades do sistema TrixoCMS.
 * @author rafael abreu
 */
@SuppressWarnings("serial")
@MappedSuperclass
@EntityListeners(TrixoCMSEntityListener.class)
public abstract class TrixoCMSEntity<I extends Serializable> implements Serializable {

    /**
     * Data do cadastro.
     */
    @Column(nullable = false)
    private Date cadastro;

    /**
     * Data da lateração.
     */
    @Column(nullable = true)
    private Date alteracao;

    /**
     * Versão da instância da entidade.
     */
    @Version
    private Integer versao;

    /**
     * Contrutor Padrão.
     */
    public TrixoCMSEntity() {
        super();
    }

    /**
     * @return the id
     */
    public abstract I getId();

    /**
     * @param id
     *            the id to set
     */
    public abstract void setId(I id);

    /**
     * Valida se o valor do identificador é diferente de NULL e maior que Zero.
     * @return boolean
     */
    public boolean isCadastrado() {
        return getId() != null;
    }

    /**
     * @return the cadastro
     */
    public Date getCadastro() {
        return cadastro;
    }

    /**
     * @param cadastro
     *            the cadastro to set
     */
    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }

    /**
     * @return the alteracao
     */
    public Date getAlteracao() {
        return alteracao;
    }

    /**
     * @param alteracao
     *            the alteracao to set
     */
    public void setAlteracao(Date alteracao) {
        this.alteracao = alteracao;
    }

    /**
     * @return the versao
     */
    public Integer getVersao() {
        return versao;
    }
}
