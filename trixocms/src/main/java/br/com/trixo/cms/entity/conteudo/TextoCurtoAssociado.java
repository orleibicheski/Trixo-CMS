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
 * 
 */
@Entity
public class TextoCurtoAssociado extends ConteudoAssociado<TextoCurto> {

    /**
     * serialID.
     */
    private static final long serialVersionUID = 6031204342753623491L;

    /**
     * Referência do objeto para manter a referência do tipo de conteúdo Texto
     * Curto.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(nullable = false)
    @NotNull
    private TextoCurto conteudo = new TextoCurto();

    /**
     * {@inheritDoc}
     */
    public TextoCurtoAssociado() {
	super();
    }

    /**
     * {@inheritDoc}
     */
    public TextoCurtoAssociado(Long id, TipoConteudo categoriaTipo,
	    String categoriaDescricao, Long conteudoId, String conteudoDescricao) {
	super(id, categoriaTipo, categoriaDescricao, conteudoId,
		conteudoDescricao);
    }

    /**
     * @return the conteudo
     */
    public TextoCurto getConteudo() {
        return conteudo;
    }

    /**
     * @param conteudo
     *            the Conteudo to set
     */
    public void setConteudo(TextoCurto conteudo) {
        this.conteudo = conteudo;
    }

}
