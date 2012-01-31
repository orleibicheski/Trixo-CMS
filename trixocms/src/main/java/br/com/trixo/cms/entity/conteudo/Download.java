/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.conteudo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
public class Download extends Arquivo {

    /**
     * serialID.
     */
    private static final long serialVersionUID = 2500451866094488068L;

    /**
     * Referência para a coleção de conteúdos associados.
     */
    @OneToMany(mappedBy = "conteudo", cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    private Collection<DownloadAssociado> downloads = new ArrayList<DownloadAssociado>();

    /**
     * @return the downloads
     */
    public Collection<DownloadAssociado> getDownloads() {
        return downloads;
    }

    /**
     * @param downloads the downloads to set
     */
    public void setDownloads(Collection<DownloadAssociado> downloads) {
        this.downloads = downloads;
    }

}
