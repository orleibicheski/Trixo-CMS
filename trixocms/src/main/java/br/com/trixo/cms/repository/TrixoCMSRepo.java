/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.repository;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.commons.logging.Log;
import org.apache.velocity.app.VelocityEngine;

import br.com.trixo.cms.jms.qualifier.Publicacoes;

/**
 * @author Orlei Bicheski
 * @version $Revision$
 */
public class TrixoCMSRepo {

    /**
     * Referência para Logger.
     */
    // Não utilizar @Inject devido a um problema de injeção circular
    private Log log = org.apache.commons.logging.LogFactory.getLog(TrixoCMSRepo.class.getName());

    /**
     * Retorna uma referência de Logger para a classe específica que está
     * injetando essa factory.
     * @param ip
     *            Ponto de injeção
     * @return {@link Logger} Referência do Logger.
     */
    @Produces
    public Log createLog(InjectionPoint injectionPoint) {
        return org.apache.commons.logging.LogFactory.getLog(injectionPoint.getMember().getDeclaringClass().getName());
    }

    /**
     * Retorna uma referência do Velocity para a classe específica que está
     * injetando essa factory.
     * @param ip
     *            Ponto de injeção
     * @return {@link Logger} Referência do Velocity.
     */
    @Produces
    @Publicacoes
    public VelocityEngine createVelocity(InjectionPoint injectionPoint) {

        Properties properties = new Properties();

        try {
            properties.load(this.getClass().getResourceAsStream("/velocity.properties"));
        } catch (IOException e) {
            log.error("Ocorreu um erro ao carregar o arquivo velocity.properties.", e);

            return null;
        }

        VelocityEngine ve = new VelocityEngine();

        ve.init(properties);

        return ve;
    }

}
