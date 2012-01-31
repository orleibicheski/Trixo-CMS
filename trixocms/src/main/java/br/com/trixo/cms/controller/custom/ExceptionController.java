/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.custom;

import org.jboss.seam.exception.control.CaughtException;
import org.jboss.seam.exception.control.Handles;
import org.jboss.seam.exception.control.HandlesExceptions;

import br.com.trixo.cms.controller.Controller;

/**
 * Controller que captura as exceptions para que as mesmas não sejam
 * apresentadas as telas.
 * @author rafaelabreu
 */
@HandlesExceptions
public class ExceptionController extends Controller {

    /**
     * 
     */
    private static final long serialVersionUID = 5588439638130881115L;

    public void catchExceptions(@Handles CaughtException<Throwable> caughtException) {

        fatal("Falha do sistema", "Contate nosso suporte.");

    }
}
