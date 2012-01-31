/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.custom;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.primefaces.event.SelectEvent;

import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.qualifier.SiteSelecionado;

/**
 * @author Orlei Bicheski
 * @version $Revision$
 */
@Named
@SessionScoped
public class SelecionarSite extends CustomController {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = 251070209899228850L;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Referência do objeto Site.
     */
    private Site site;

    /**
     * Retorna a referência do objeto Site a partir do IDentificador único
     * selecionado.
     * 
     * @return {@link Site} Referência do objeto Site
     */
    @Produces
    @SiteSelecionado
    @Named("siteSelecionado")
    public Site getSite() {
        return site;
    }

    /**
     * Encaminha para o painel do site.
     * 
     * @return Foward para o painel do site.
     */
    public String selecionar() {
        log.info("Metodo Select executado.");

        if (log.isDebugEnabled()) {
            log.debug("Referencia do Objeto Site: "
                    .concat(getSite().toString()));
            if (getSite().getId() != null) {
                log.debug("ID a ser removido: ".concat(String.valueOf(getSite()
                        .getId())));
            }
        }

        return "painelSite?faces-redirect=true";
    }

    /**
     * Chama o selecionar.
     */
    public String selecionar(SelectEvent event) {
        return selecionar();
    }

    /**
     * @param site
     */
    public void setSite(Site site) {
        this.site = site;
    }
}
