/**
 * get * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.entity;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import javax.enterprise.context.Conversation;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.controller.Controller;
import br.com.trixo.cms.entity.TrixoCMSEntity;

/**
 * Controller abstrato responsável por regras básicas de persistencia de
 * entidades.
 * @author rafaelabreu
 * @version $Revision$
 */
@SuppressWarnings("serial")
public abstract class EntityController<E extends TrixoCMSEntity<I>, I extends Serializable> extends Controller
        implements Converter {

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Conversation atual.
     */
    @Inject
    private Conversation conversation;

    /**
     * Instância da entidade controlada.
     */
    protected E instance;

    /**
     * Class do atributo E.
     */
    private Class<E> entityClass;

    /**
     * Identificador utilizado para consultas de entidades.
     */
    private I id;

    /**
     * @param selecionada
     *            the selecionada to set
     */
    public E getSelecionada() {
        return this.instance;
    }

    /**
     * @param selecionada
     *            the selecionada to set
     */
    public void setSelecionada(E selecionada) {
        if (selecionada != null) {
            setId(selecionada.getId());
        }
    }

    /**
     * Busca a entidade a partir do identificador informado no entityController.
     */
    public void loadInstance() {
        EntityManager manager = getEntityManager();

        if (manager == null) {
            log.error("loadInstance: EntityManager nao foi carregado.");

            throw new PersistenceException("loadInstance: EntityManager nao foi carregado.");
        }

        try {
            instance = manager.find(getEntityClass(), getId());
        } catch (Exception e) {
            String message = "Ocorreu uma falha na execucao do metodo loadInstance.";
            log.error(message, e);
            throw new PersistenceException(message, e);
        }

        if (instance == null) {
            StringBuilder str = new StringBuilder();
            str.append("Class: ");
            str.append(getEntityClass().getName());
            str.append(" ID: ");
            str.append(getId());

            log.error("Objeto não foi carregado ".concat(str.toString()));

            throw new EntityNotFoundException(str.toString());
        } else {
            if (log.isInfoEnabled()) {
                String mensagem = "Metodo loadInstance executado. Classe: ${class}.";
                log.info(mensagem.replace("${class}", getEntityClass().getName()));
            }
        }
    }

    /**
     * Persist unmanaged entity instance to the underlying database. If the
     * persist is successful, a log message is printed, a
     * {@link javax.faces.application.FacesMessage } is added and a transaction
     * success event raised.
     * @see Home#createdMessage()
     * @see Home#raiseAfterTransactionSuccessEvent()
     * @return "persisted" if the persist is successful
     */
    public String persist() {
        log.info("Metodo Persist executado.");

        EntityManager manager = getEntityManager();

        E instance = getInstance();

        if (log.isDebugEnabled()) {
            log.debug("Referencia do Objeto a ser persistido: ".concat(instance.toString()));
            if (instance.isCadastrado()) {
                log.debug("Objeto novo.");
            } else {
                log.debug("ID: ".concat(String.valueOf(instance.getId())));
            }
        }

        try {
            if (!getConversation().isTransient())
                getConversation().end();

            manager.persist(instance);
            manager.flush();

            log.debug("Objeto persistido.");

            setId(instance.getId());

            return getForwardPersisted();
        } catch (Exception e) {
            String message = "Ocorreu uma falha na execucao do metodo Persist ou Flush.";
            log.error(message, e);
            throw new PersistenceException(message, e);
        }
    }

    /**
     * Método que retorna o Forward padrão de direcionamento para a tela de
     * listagem da Entity após ter sido persistida.
     * @return Direcionador para a navegação de persistência do objeto
     */
    protected String getForwardPersisted() {
        return "persisted";
    }

    // TODO comentar isso aqui
    /**
     * 
     */
    public String update() {

        try {
            E instance = getInstance();

            getEntityManager().merge(instance);

            getConversation().end();

            log.debug("Objeto atualizado.");

            return getForwardUpdated();
        } catch (Exception e) {
            String message = "Ocorreu uma falha na execucao do metodo updateNovo.";
            log.error(message, e);
            throw new PersistenceException(message, e);
        }
    }

    /**
     * Método que retorna o Forward padrão de direcionamento para a tela de
     * listagem da Entity após ter sido atualizado.
     * @return Direcionador para a navegação de atualização do objeto
     */
    protected String getForwardUpdated() {
        return "updated";
    }

    /**
     * Remove managed entity instance from the Persistence Context and the
     * underlying database. If the remove is successful, a log message is
     * printed, a {@link javax.faces.application.FacesMessage} is added and a
     * transaction success event raised.
     * @see Home#deletedMessage()
     * @see Home#raiseAfterTransactionSuccessEvent()
     * @return "removed" if the remove is successful
     */
    public String remove() {
        log.info("Metodo Remove executado.");

        EntityManager manager = getEntityManager();

        if (log.isDebugEnabled()) {
            log.debug("Referencia do Objeto a ser removido: ".concat(instance.toString()));
            if (instance.getId() != null) {
                log.debug("ID a ser removido: ".concat(String.valueOf(instance.getId())));
            }
        }

        try {
            manager.remove(getInstance());
            manager.flush();

            log.debug("Objeto removido.");

            return getForwardRemoved();
        } catch (Exception e) {
            String message = "Ocorreu uma falha na execucao do metodo Remove.";
            log.error(message, e);
            throw new PersistenceException(message, e);
        }
    }

    /**
     * Método que retorna o Forward padrão de direcionamento para a remoção do
     * Entity.
     * @return Direcionador para a navegação de remoção do objeto
     */
    protected String getForwardRemoved() {
        return "removed";
    }

    /**
     * Create a new instance of the entity. <br />
     * Utility method called by {@link #initInstance()} to create a new instance
     * of the entity.
     */
    protected void newInstance() {
        if (getEntityClass() != null) {
            instance = criarInstancia();
        } else {
            log.error("Nao foi identificada a classe do objeto para a criacao da instancia.");
        }
    }

    /**
     * @return Instância novinha em folha
     */
    private E criarInstancia() {

        E nova = null;

        try {
            nova = getEntityClass().newInstance();

            if (log.isInfoEnabled()) {
                String mensagem = "Metodo newInstance executado. Classe: ${class}.";
                log.info(mensagem.replace("${class}", getEntityClass().getName()));
            }
        } catch (Exception e) {
            log.error("newInstance: Instancia do objeto nao foi criada.", e);
            throw new RuntimeException(e);
        }

        return nova;
    }

    /**
     * Load the instance if the id is defined otherwise create a new instance <br />
     * Utility method called by {@link #getInstance()} to load the instance from
     * the Persistence Context if the id is defined. Otherwise a new instance is
     * created.
     * @see #find()
     * @see #newInstance()
     */
    protected void initInstance() {
        if (isNew()) {
            newInstance();
        } else {
            loadInstance();
        }
    }

    /**
     * Returns true if the id of the object managed is known.
     */
    public boolean isNew() {
        return getId() == null;
    }

    /**
     * Returns true if the entity instance is managed
     */
    public boolean isManaged() {
        if (!isNew()) {
            if (getInstance() != null) {
                if (getId() != null) {
                    return getEntityManager().contains(getEntityManager().getReference(getEntityClass(), getId()));
                }
                return getEntityManager().contains(
                        getEntityManager().getReference(getEntityClass(), getInstance().getId()));
            }
        }
        return false;
    }

    /**
     * Get the managed entity, using the id from {@link #getId()} to load it
     * from the Persistence Context or creating a new instance if the id is not
     * defined.
     * @see #getId()
     */
    public E getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }

    /**
     * Get the class of the entity being managed. <br />
     * If not explicitly specified, the generic type of implementation is used.
     */
    @SuppressWarnings("unchecked")
    public Class<E> getEntityClass() {
        if (entityClass == null) {

            @SuppressWarnings("rawtypes")
            Class entityControllerClass = getClass();

            do {
                // lógica para navegar até a classe que extends EntityController
                entityControllerClass = entityControllerClass.getSuperclass();
            } while (!EntityController.class.isAssignableFrom(entityControllerClass));

            Type type = entityControllerClass.getGenericSuperclass();

            if (log.isDebugEnabled()) {
                log.debug("Type Class: ".concat(type.toString()));
            }

            if (type instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) type;

                if (!(paramType.getActualTypeArguments()[0] instanceof TypeVariable)) {
                    entityClass = (Class<E>) paramType.getActualTypeArguments()[0];
                }

                if (entityClass == null)
                    throw new IllegalArgumentException("Could not guess entity class by reflection");
            }

        }

        if (log.isDebugEnabled()) {
            log.debug("Type Class: ".concat(entityClass.toString()));
        }

        return entityClass;
    }

    /**
     * Zera as referências para entidade do controller.
     */
    public void clearInstance() {
        this.instance = null;
        this.entityClass = null;
        this.id = null;
    }

    /**
     * Método responsável por receber o valor do identificador único do objeto
     * Entity, iniciar uma conversação para a visualização e alteração.
     * @return Direcionador para a navegação de Viewer
     */
    public String view() {
        if (getConversation().isTransient())
            getConversation().begin();

        return getForwardView();
    }

    /**
     * Método que retorna o Forward padrão de direcionamento para a tela de
     * cadastro da visualização Entity.
     * @return Direcionador para a navegação de visualização do objeto
     */
    protected String getForwardView() {
        return "view";
    }

    /**
     * Método que direciona para a criação de um novo objeto Entity, limpa os
     * principais atributos de referência da Entity e finaliza a conversation
     * existente (caso aja uma).
     * @return Direcionador para a navegação de novo objeto
     */
    public String newEntity() {

        clearInstance();

        if (!getConversation().isTransient())
            getConversation().end();

        return getForwardNew();
    }

    /**
     * Método que retorna o Forward padrão de direcionamento para a tela de
     * cadastro de novo Entity.
     * @return Direcionador para a navegação de novo objeto
     */
    protected String getForwardNew() {
        return "new";
    }

    /**
     * @return Identificador encontrado no EntityController da Entidade.
     */
    public I getId() {
        return this.id;
    }

    /**
     * @param id
     *            Identificador a ser setado.
     */
    public void setId(I id) {
        clearInstance();

        this.id = id;
    }

    /**
     * @return Conversation atual.
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     * Método que deve ser implementado retornando o Identificador em sua forma
     * basica.
     * @param value
     *            Identificador no formato String
     * @return Identificador convertido para o formato desejado
     */
    protected abstract I converterIdentificador(String value);

    /**
     * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext
     *      , javax.faces.component.UIComponent, java.lang.String)
     */
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {

        E entity = null;

        if (value != null && !"".equals(value)) {
            if ("novo".equals(value)) {
                entity = criarInstancia();
            } else {
                setId(converterIdentificador(value));

                entity = getInstance();
            }
        }

        return entity;
    }

    /**
     * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext
     *      , javax.faces.component.UIComponent, java.lang.Object)
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {

        String id = "novo";

        if (value != null) {
            @SuppressWarnings("unchecked")
            E entity = (E) value;

            if (entity.isCadastrado())
                id = String.valueOf(entity.getId());
        } else {
            id = "";
        }

        return id;
    }
}
