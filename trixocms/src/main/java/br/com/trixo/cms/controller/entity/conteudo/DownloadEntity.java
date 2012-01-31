/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.entity.conteudo;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.controller.custom.UploadArquivos;
import br.com.trixo.cms.entity.conteudo.Download;
import br.com.trixo.cms.entity.conteudo.DownloadAssociado;

/**
 * @author Orlei Bicheski
 */
@Named
@ConversationScoped
public class DownloadEntity extends ConteudoEntity<DownloadAssociado> {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = 2580828524053377571L;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Referência para o controller que tem os dados do arquivo uplodeado.
     */
    @Inject
    private UploadArquivos uploadArquivos;

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#getForwardNew()
     */
    @Override
    protected String getForwardNew() {
	return "newDownload";
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#getForwardRemoved()
     */
    @Override
    protected String getForwardRemoved() {
	return "removedDownload";
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#getForwardView()
     */
    @Override
    protected String getForwardView() {
	return "viewDownload";
    }

    /**
     * Sobrecarrega o método preparar do Controller {@link ConteudoEntity} e
     * realiza a preparação para o objeto {@link Download}.
     * 
     * @see br.com.trixo.cms.controller.entity.ConteudoEntity#preparar()
     */
    @Override
    protected void preparar() {
        super.preparar();

        Download download = getInstance().getConteudo();

        if (!isManaged()) {
            if (site == null) {
        	String mensagem = "A referência do Site esta nula.";
        	
        	log.error(mensagem);
        	throw new IllegalArgumentException(mensagem);
            }
            download.setSite(site);
	}

        download.setValor(uploadArquivos.getArquivo().getContents());
        // Mime-Type padrão para download
        download.setMimeType("application/octet-stream");
        
        uploadArquivos.zerar();

        if (!isManaged()) {
            download.getDownloads().add(getInstance());
	}
    }
}