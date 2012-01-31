/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.conteudo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
@Table
@DiscriminatorValue("TEXTO_CURTO")
public class TextoCurto extends Conteudo {

    /**
     * serialID.
     */
    private static final long serialVersionUID = -5190485576395613444L;

    /**
     * Referência para a coleção de conteúdos associados.
     */
    @OneToMany(mappedBy = "conteudo", cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    private Collection<TextoCurtoAssociado> textosCurtos = new ArrayList<TextoCurtoAssociado>();

    /**
     * @return the textosCurtos
     */
    public Collection<TextoCurtoAssociado> getTextosCurtos() {
        return textosCurtos;
    }

    /**
     * @param textosCurtos
     *            the textosCurtos to set
     */
    public void setTextosCurtos(Collection<TextoCurtoAssociado> textosCurtos) {
        this.textosCurtos = textosCurtos;
    }
}
