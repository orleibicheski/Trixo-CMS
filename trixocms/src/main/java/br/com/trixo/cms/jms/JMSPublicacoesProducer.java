package br.com.trixo.cms.jms;

import java.io.Serializable;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.entity.publicacao.Publicacao;
import br.com.trixo.cms.jms.qualifier.Publicacoes;
import br.com.trixo.cms.publicador.exception.PublicacaoException;

/**
 * Responsável por criar uma mensagem na fila de publicações.
 * @author rafaelabreu
 */
public class JMSPublicacoesProducer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7572689123980490081L;

    /**
     * Session para acesso a fila de publicacões.
     */
    @Inject
    private Session session;

    /**
     * MessageProducer para acesso a fila de publicacões.
     */
    @Inject
    @Publicacoes
    private MessageProducer producer;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * @param publicacao
     *            Publicação que será enviada para fila de publicação.
     * @throws PublicacaoException
     *             Exceção lançada quando não foi possivel enviar a mensagem
     *             para a fila de publicações.
     */
    public void enviarPublicacao(Publicacao publicacao) throws PublicacaoException {

        if (log.isDebugEnabled())
            log.debug("Enviando para publicação " + publicacao);

        try {
            ObjectMessage message = session.createObjectMessage(publicacao.getId());

            producer.send(message);
        } catch (JMSException e) {

            log.error("Ocorreu um erro ao criar a mensagem para fila de publicações.", e);

            throw new PublicacaoException();
        }

        if (log.isDebugEnabled())
            log.debug("Mensagem adicionada com sucesso a fila de publicações " + publicacao);

    }
}
