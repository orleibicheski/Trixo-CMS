/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.jboss.seam.transaction.Transactional;

/**
 * @author Orlei Bicheski
 * @version $Revision$
 */
@SuppressWarnings("serial")
@Transactional
public abstract class Controller implements Serializable {

    /**
     * EntityManager injetado por padrão.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * @return Entity manager gerenciado por padr??o.
     */
    protected EntityManager getEntityManager() {

        // Valida se o EntityManager eh valido
        if (this.entityManager == null) {
            String managerError = "Update: EntityManager nao foi carregado.";
            log.error(managerError);
            throw new PersistenceException(managerError);
        }

        return this.entityManager;
    }

    /**
     * Adiciona mensagem do tipo INFO no contexto de mensagens do Faces.
     * @param titulo
     *            Título da mensagem a ser apresentação
     * @param mensagem
     *            Valor da mensagem que será apresentada
     */
    protected void info(String titulo, String mensagem) {
        criarMensagem(FacesMessage.SEVERITY_INFO, titulo, mensagem);
    }

    /**
     * Adiciona mensagem do tipo WARN no contexto de mensagens do Faces.
     * @param titulo
     *            Título da mensagem a ser apresentação
     * @param mensagem
     *            Valor da mensagem que será apresentada
     */
    protected void warn(String titulo, String mensagem) {
        criarMensagem(FacesMessage.SEVERITY_WARN, titulo, mensagem);
    }

    /**
     * Adiciona mensagem do tipo ERROR no contexto de mensagens do Faces.
     * @param titulo
     *            Título da mensagem a ser apresentação
     * @param mensagem
     *            Valor da mensagem que será apresentada
     */
    protected void error(String titulo, String mensagem) {
        criarMensagem(FacesMessage.SEVERITY_ERROR, titulo, mensagem);
    }

    /**
     * Adiciona mensagem do tipo FATAL no contexto de mensagens do Faces.
     * @param titulo
     *            Título da mensagem a ser apresentação
     * @param mensagem
     *            Valor da mensagem que será apresentada
     */
    protected void fatal(String titulo, String mensagem) {
        criarMensagem(FacesMessage.SEVERITY_FATAL, titulo, mensagem);
    }

    /**
     * Cria uma mensagem que será apresentada na tela para o usuário.
     * @param severidade
     *            Severidade da mensagem {Exemplo: INFO, WARN, outros}.
     * @param titulo
     *            Título da mensagem a ser apresentação
     * @param mensagem
     *            Valor da mensagem que será apresentada
     */
    private void criarMensagem(FacesMessage.Severity severidade, String titulo, String mensagem) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(severidade, titulo, mensagem));
    }

}
