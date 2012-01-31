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
import br.com.trixo.cms.entity.conteudo.Imagem;
import br.com.trixo.cms.entity.conteudo.ImagemAssociado;

/**
 * @author Orlei Bicheski
 */
@Named
@ConversationScoped
public class ImagemEntity extends ConteudoEntity<ImagemAssociado> {

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
     * @see br.com.trixo.cms.controller.entity.EntityController#getForwardNew()
     */
    @Override
    protected String getForwardNew() {
	return "newImagem";
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#getForwardRemoved()
     */
    @Override
    protected String getForwardRemoved() {
	return "removedImagem";
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#getForwardView()
     */
    @Override
    protected String getForwardView() {
	return "viewImagem";
    }

    /**
     * Referência para o controller que tem os dados do arquivo uplodeado.
     */
    @Inject
    private UploadArquivos uploadArquivos;

    /**
     * Sobrecarrega o método preparar do Controller {@link ConteudoEntity} e
     * realiza a preparação para o objeto {@link Imagem}.
     * 
     * @see br.com.trixo.cms.controller.entity.ConteudoEntity#preparar()
     */
    @Override
    protected void preparar() {
        super.preparar();

        Imagem imagem = getInstance().getConteudo();

        if (!isManaged()) {
            if (site == null) {
        	String mensagem = "A referência do Site esta nula.";
        	
        	log.error(mensagem);
        	throw new IllegalArgumentException(mensagem);
            }
            imagem.setSite(site);
	}

        imagem.setValor(uploadArquivos.getArquivo().getContents());
        imagem.setMimeType(getMimeType(uploadArquivos.getArquivo()
                .getFileName()));

        uploadArquivos.zerar();

        if (!isManaged()) {
            imagem.getImagens().add(getInstance());
	}
    }

    /**
     * Método que verifica o tipo a extensão do arquivo de imagem. Aqui são
     * tratados somente arquivos de imagens JPG, JEP, JEPG, PNG e GIF.
     * 
     * @param arquivo
     *            Nome do arquivo
     * @return MimeType referente ao tipo de arquivo de Imagem
     */
    private String getMimeType(String arquivo) {
        if (arquivo != null && !arquivo.isEmpty()) {
            // Arquivos de imagem padrão PNG
            if (arquivo.toLowerCase().endsWith(".png")) {
                return "image/png";
            }
            // Arquivos de imagem padrão JPG
            else if (arquivo.toLowerCase().endsWith(".jpg")
                    || arquivo.toLowerCase().endsWith(".jep")
                    || arquivo.toLowerCase().endsWith(".jepg")) {
                return "image/jpeg";
            }
        }
        // Arquivos de imagem padrão GIF
        return "image/gif";
    }

}
