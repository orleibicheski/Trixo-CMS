/**
 * Copyright (c) 200 3-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.trixo.cms.entity.TrixoCMSEntity;

/**
 * Listener responsável por realizar as tarefas de atualização automática das
 * entidades.
 * @author rafaelabreu
 */
public class TrixoCMSEntityListener {

    /**
     * Referência para Logger. Este não foi injetado devido a injeção não
     * funcionar em interceptors.
     */
    private Log log = LogFactory.getLog(TrixoCMSEntityListener.class);

    /**
     * Método chamado antes de uma criação
     * @param entity
     *            Entidade a ser criação.
     */
    @PrePersist
    public void prePersist(TrixoCMSEntity<?> entity) {
        log.debug("Interceptado antes de criar a entidade: " + entity.toString());
        entity.setCadastro(new Date());
    }

    /**
     * Método chamado antes de uma atualização
     * @param entity
     *            Entidade a ser atualizada.
     */
    @PreUpdate
    public void preUpdate(TrixoCMSEntity<?> entity) {
        log.debug("Interceptado antes de atualizar a entidade: " + entity.toString());
        entity.setAlteracao(new Date());
    }

}
