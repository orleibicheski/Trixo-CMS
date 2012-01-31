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
@MessageDriven(name = "AgendamentosMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/agendamentos") })
public class JMSAgendamentosConsumer implements MessageListener {

    /**
     * Controller que realiza as publicações solicitadas.
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

        log.debug(String.format("Publicação %s consumida da fila de publicacões.", idPublicacao));

        // publicador.executarAgendamento(idPublicacao);
    }

}
