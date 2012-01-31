package br.com.trixo.cms.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.publicador.controller.Publicador;

/**
 * MessageDrivenBean responsável por consumir as mensagem na fila de
 * Publicações.
 * @author rafaelabreu
 */
@MessageDriven(name = "PublicacoesMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/publicacoes"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class JMSPublicacoesConsumer implements MessageListener {

    /**
     * Controller que executa os agendamentos e solicita as publicações.
     */
    @Inject
    private Publicador publicador;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    @Override
    public void onMessage(Message message) {

        ObjectMessage objectMessage = (ObjectMessage) message;

        Long idPublicacao = null;

        try {
            idPublicacao = (Long) objectMessage.getObject();
        } catch (JMSException e) {
            log.error("Obter o identificador da publicação na mensagem da fila de publicações.", e);

            throw new IllegalStateException();
        }

        log.debug("Publicação " + idPublicacao + " consumida da fila de publicacões.");

        publicador.publicar(idPublicacao);

        log.debug("FOI CERTINHO.");
    }

}
