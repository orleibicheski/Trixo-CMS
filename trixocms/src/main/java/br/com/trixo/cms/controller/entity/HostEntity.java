/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.entity;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import br.com.trixo.cms.entity.site.Host;

/**
 * Classe TransactionController da entidade 'Host'.
 * @author Rafael Abreu
 * @version $Revision$
 */
@Named
@ConversationScoped
public class HostEntity extends EntityController<Host, Long> {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = 9030310975321783579L;

    @Override
    protected Long converterIdentificador(String value) {
        return Long.parseLong(value);
    }
}
