/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.jboss.seam.transaction.Transactional;

/**
 * Classe Repositório base para os demais repositórios
 * @author Rafael Abreu
 * @version $Revision$
 */
@SuppressWarnings("serial")
@Transactional
public abstract class RepoEntityManager implements Serializable {

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

}
