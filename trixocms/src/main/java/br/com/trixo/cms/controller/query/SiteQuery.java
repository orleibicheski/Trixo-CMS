/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.query;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import br.com.trixo.cms.entity.site.Site;

/**
 * @author Orlei Bicheski
 * @version $Revision$
 */
@Named
@ViewScoped
public class SiteQuery extends QueryController<Site> {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = 6614262929024759249L;

    /**
     * @see br.com.trixo.cms.controller.query.QueryController#getJpaQL()
     */
    @Override
    protected String getJpaQL() {
        return null;
    }

}
