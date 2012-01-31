/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.conteudo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author Orlei Bicheski
 */
@Entity
public class DownloadAssociado extends ConteudoAssociado<Download> {

    /**
     * serialID
     */
    private static final long serialVersionUID = 6031204342753623491L;

    /**
     * Referência do objeto para manter a referência do tipo de conteúdo
     * Download.
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(nullable = false)
    @NotNull
    private Download conteudo = new Download();

    /**
     * {@inheritDoc}
     */
    public DownloadAssociado() {
	super();
    }

    /**
     * {@inheritDoc}
     */
    public DownloadAssociado(Long id, TipoConteudo categoriaTipo,
	    String categoriaDescricao, Long conteudoId, String conteudoDescricao) {
	super(id, categoriaTipo, categoriaDescricao, conteudoId,
		conteudoDescricao);
    }

    /**
     * @return the conteudo
     */
    public Download getConteudo() {
	return conteudo;
    }

    /**
     * @param conteudo
     *            the conteudo to set
     */
    public void setConteudo(Download conteudo) {
	this.conteudo = conteudo;
    }

}
