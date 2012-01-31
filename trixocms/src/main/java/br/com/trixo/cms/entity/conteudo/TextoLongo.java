/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.conteudo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
@Table
@DiscriminatorValue("TEXTO_LONGO")
public class TextoLongo extends Conteudo {

    /**
     * serialID.
     */
    private static final long serialVersionUID = 7240086993346334354L;

    @Lob
    @Column(nullable = true)
    @NotNull
    private String valor;

    /**
     * Referência para a coleção de conteúdos associados.
     */
    @OneToMany(mappedBy = "conteudo", cascade = { CascadeType.MERGE })
    private Collection<TextoLongoAssociado> textosLongos = new ArrayList<TextoLongoAssociado>();

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
     * @return the textosLongos
     */
    public Collection<TextoLongoAssociado> getTextosLongos() {
	return textosLongos;
    }

    /**
     * @param textosLongos
     *            the textosLongos to set
     */
    public void setTextosLongos(Collection<TextoLongoAssociado> textosLongos) {
	this.textosLongos = textosLongos;
    }
}
