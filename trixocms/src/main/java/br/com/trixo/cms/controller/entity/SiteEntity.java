/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.entity;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.entity.site.Site;

/**
 * Classe TransactionController da entidade 'Site'.
 * @author Orlei Bicheski
 * @version $Revision$
 */
@Named
@ConversationScoped
public class SiteEntity extends EntityController<Site, Long> {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = 9030310975321783579L;

    /**
     * Referência do LogFac.
     */
    @Inject
    private Log log;

    /**
     * Referência ao controller do host
     */
    @Inject
    private HostEntity hostEntity;

    /**
     * Carrega todas as referências de Sites registrados na base de dados.
     * TODO: passar essa query para um siteusuarioquery
     */
    @Produces
    @Named("sites")
    public List<Site> listar() {
        log.info("Metodo List executado.");

        String jpaQL = "from Site";

        try {
            if (log.isDebugEnabled()) {
                if (getEntityManager() != null) {
                    log.debug("EntityManager inicializado com sucesso.");
                }
            }

            TypedQuery<Site> query = getEntityManager().createQuery(jpaQL, Site.class);

            List<Site> resultado = query.getResultList();
            if (log.isDebugEnabled()) {
                log.debug("Query executada com sucesso.");
                if (resultado != null && !resultado.isEmpty()) {
                    log.debug("Lista de Sites carregada com sucesso.");
                    log.debug("Foram encontrados ".concat(String.valueOf(resultado.size())).concat(" Sites."));
                }
            }
            return resultado;
        } catch (Exception e) {
            log.error("Problemas na execucao da Query: ".concat(jpaQL), e);
        }

        return new ArrayList<Site>();
    }

    /**
     * Prepara as informações básicas de uma categoria. Informa o Site
     * selecionado.
     */
    private void preparar() {

        // relaciona um ao outro
        instance.setHost(hostEntity.getInstance());
        hostEntity.getInstance().setSite(instance);

    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#persist()
     */
    @Override
    public String persist() {
        preparar();
        return super.persist();
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#update()
     */
    @Override
    public String update() {
        preparar();
        return super.update();
    }

    @Override
    protected Long converterIdentificador(String value) {
        return Long.parseLong(value);
    }
}
