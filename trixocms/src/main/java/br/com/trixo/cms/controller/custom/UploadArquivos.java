/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.custom;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * @author Rafael Abreu
 * @version $Revision$
 */
@Named
@SessionScoped
public class UploadArquivos extends CustomController {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = 5638294847030536677L;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Arquivos uploadeados.
     */
    private List<UploadedFile> arquivos = new ArrayList<UploadedFile>();

    /**
     * Listener chamado quando é realizado um upload.
     * @param event
     *            Evendo relacionado ao upload do arquivo.
     */
    public void fileUploadListener(FileUploadEvent event) {

        getArquivos().add(event.getFile());

        log.debug("Realizando o upload do arquivo: " + event.getFile().getFileName());
    }

    /**
     * Zera o controler.
     */
    public void zerar() {

        arquivos = new ArrayList<UploadedFile>();
    }

    /**
     * @return the arquivos
     */
    public List<UploadedFile> getArquivos() {
        return arquivos;
    }

    /**
     * @param index
     *            Indice desejado
     * @return Arquivo do indice desejado.
     */
    public UploadedFile getArquivo(int index) {

        if (index != 0 && arquivos != null && arquivos.get(index) != null)
            return arquivos.get(index);

        return null;
    }

    /**
     * @return Primeiro arquivo.
     */
    public UploadedFile getArquivo() {

        return getArquivo(0);
    }
}
