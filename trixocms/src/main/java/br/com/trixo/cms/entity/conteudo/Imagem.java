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
@DiscriminatorValue("IMAGEM")
public class Imagem extends Arquivo {

    /**
     * serialID.
     */
    private static final long serialVersionUID = -2264015124673794353L;

    /**
     * Referência para a coleção de conteúdos associados.
     */
    @OneToMany(mappedBy = "conteudo", cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    private Collection<ImagemAssociado> imagens = new ArrayList<ImagemAssociado>();

    /**
     * @return the imagens
     */
    public Collection<ImagemAssociado> getImagens() {
        return imagens;
    }

    /**
     * @param imagens the imagens to set
     */
    public void setImagens(Collection<ImagemAssociado> imagens) {
        this.imagens = imagens;
    }
}
