package br.com.trixo.cms.jms.repository;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.trixo.cms.jms.qualifier.Agendamentos;
import br.com.trixo.cms.jms.qualifier.Publicacoes;

/**
 * Responsável por TODO abreu: comentar toda a classe.
 * @author rafaelabreu
 */
@ApplicationScoped
public class JMSRepo {

    private InitialContext initialContext;

    private ConnectionFactory connectionFactory;
    private Connection connection;

    @Produces
    private Session session;

    @Produces
    @Publicacoes
    private Queue queuePublicacoes;

    @Produces
    @Agendamentos
    private Queue queueAgendamentos;

    @SuppressWarnings("unused")
    @Produces
    @Publicacoes
    private MessageProducer producerPublicacoes;

    @SuppressWarnings("unused")
    @Produces
    @Agendamentos
    private MessageProducer producerAgendamentos;

    public JMSRepo() {

        try {
            initialContext = new InitialContext();

            queuePublicacoes = (Queue) initialContext.lookup("/queue/publicacoes");
            queueAgendamentos = (Queue) initialContext.lookup("/queue/agendamentos");

            connectionFactory = (ConnectionFactory) initialContext.lookup("/JmsXA");

            connection = connectionFactory.createConnection();

            connection.start();

            session = connection.createSession(true, Session.SESSION_TRANSACTED);

            producerPublicacoes = session.createProducer(queuePublicacoes);
            producerAgendamentos = session.createProducer(queueAgendamentos);

        } catch (NamingException e) {
            // TODO: handle exception
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @PreDestroy
    public void finalizar() {
        try {
            connection.close();
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
