/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.entity;

import javax.inject.Named;

import br.com.trixo.cms.entity.publicacao.Publicacao;

/**
 * @author Rafael Abreu
 */
@Named
public class PublicacaoEntity extends EntityController<Publicacao, Long> {

    /**
     * 
     */
    private static final long serialVersionUID = -3630850279017089450L;

    @Override
    protected Long converterIdentificador(String value) {
        return Long.parseLong(value);
    }
}
