package br.com.trixo.cms.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.entity.publicacao.PublicacaoAgendada;
import br.com.trixo.cms.jms.qualifier.Agendamentos;
import br.com.trixo.cms.publicador.exception.PublicacaoException;

/**
 * Responsável por criar uma mensagem na fila de publicações.
 * @author rafaelabreu
 */
public class JMSAgendamentosProducer {

    /**
     * Session para acesso a fila de publicacões.
     */
    @Inject
    private Session session;

    /**
     * MessageProducer para acesso a fila de publicacões.
     */
    @Inject
    @Agendamentos
    private MessageProducer producer;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * @param publicacao
     *            Publicação que será enviada para fila de agendamentos.
     * @throws PublicacaoException
     *             Exceção lançada quando não foi possivel enviar a mensagem
     *             para a fila de agendamentos.
     */
    public void enviarPublicacao(PublicacaoAgendada publicacao) throws PublicacaoException {

        if (log.isDebugEnabled())
            log.debug("Agendando a publicação " + publicacao);

        try {
            ObjectMessage message = session.createObjectMessage(publicacao.getId());

            producer.send(message);
        } catch (JMSException e) {

            log.error("Ocorreu um erro ao criar a mensagem para fila de agendamentos.", e);

            throw new PublicacaoException();
        }

        if (log.isDebugEnabled())
            log.debug("Mensagem adicionada com sucesso a fila de agendamentos " + publicacao);

    }
}
