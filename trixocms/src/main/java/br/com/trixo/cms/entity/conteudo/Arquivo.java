/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.conteudo;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
public abstract class Arquivo extends Conteudo {

    /**
     * serialID.
     */
    private static final long serialVersionUID = 3027338407932126479L;

    /**
     * Nome do arquivo.
     */
    @Column(length = 100, nullable = true)
    private String chave;

    /**
     * Array de bytes contendo o arquivo.
     */
    @Column(nullable = true)
    private byte[] valor;

    /**
     * Mime Type do Arquivo.
     */
    @Column(length = 50, nullable = true)
    private String mimeType;

    /**
     * @return the chave
     */
    public String getChave() {
	return chave;
    }

    /**
     * @param chave
     *            the chave to set
     */
    public void setChave(String chave) {
	this.chave = chave;
    }

    /**
     * @return the valor
     */
    public byte[] getValor() {
	return valor;
    }

    /**
     * @param valor
     *            the valor to set
     */
    public void setValor(byte[] valor) {
	this.valor = valor;
    }

    /**
     * @return the mimeType
     */
    public String getMimeType() {
	return mimeType;
    }

    /**
     * @param mimeType
     *            the mimeType to set
     */
    public void setMimeType(String mimeType) {
	this.mimeType = mimeType;
    }
}
