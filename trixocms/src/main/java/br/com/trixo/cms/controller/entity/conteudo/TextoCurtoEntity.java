/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.entity.conteudo;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.entity.conteudo.TextoCurto;
import br.com.trixo.cms.entity.conteudo.TextoCurtoAssociado;

/**
 * @author Orlei Bicheski
 */
@Named
@ConversationScoped
public class TextoCurtoEntity extends ConteudoEntity<TextoCurtoAssociado> {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = 2580828524053377571L;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#getForwardNew()
     */
    @Override
    protected String getForwardNew() {
        return "newTextoCurto";
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#getForwardRemoved()
     */
    @Override
    protected String getForwardRemoved() {
        return "removedTextoCurto";
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#getForwardView()
     */
    @Override
    protected String getForwardView() {
        return "viewTextoCurto";
    }

    /**
     * Sobrecarrega o método preparar do Controller {@link ConteudoEntity} e
     * realiza a preparação para o objeto {@link TextoCurto}.
     * @see br.com.trixo.cms.controller.entity.ConteudoEntity#preparar()
     */
    @Override
    protected void preparar() {
        super.preparar();

        if (!isManaged()) {
            TextoCurto textoCurto = getInstance().getConteudo();

            if (site == null) {
                String mensagem = "A referência do Site esta nula.";

                log.error(mensagem);
                throw new IllegalArgumentException(mensagem);
            }
            textoCurto.setSite(site);

            textoCurto.getTextosCurtos().add(getInstance());
        }
    }

}
