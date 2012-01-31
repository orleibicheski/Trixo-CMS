/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.query;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.Query;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.controller.Controller;

/**
 * TransactionController abstrato responsável por realizar pesquisas elaboradas.
 * @author Orlei Bicheski
 * @version $Revision$
 */
public abstract class QueryController<E> extends Controller {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = -52681543703136117L;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Lista que contém o resutlado de uma consulta.
     */
    protected List<E> result = new ArrayList<E>();

    /**
     * Controle se já foi realizada a pesquisa ou não.
     */
    protected boolean pesquisado = false;

    /**
     * @return the result
     */
    public List<E> getResult() {
        return result;
    }

    /**
     * Método que somente pesquisa se ainda não foi pesquisado.
     */
    @PostConstruct
    public void inicializar() {

        if (!pesquisado)
            query();
    }

    /**
     * TODO Orlei, comentar esse método.
     */
    @SuppressWarnings("unchecked")
    public void query() {
        log.info("Metodo query executado.");

        String jpaQL = getJpaQL();

        if (jpaQL != null && !jpaQL.isEmpty()) {
            List<QueryParameter> parameters = getParameters();

            try {
                if (log.isDebugEnabled()) {
                    if (getEntityManager() != null) {
                        log.debug("EntityManager inicializado com sucesso.");
                    }
                }

                if (log.isTraceEnabled()) {
                    log.trace("JpaQL: ".concat(jpaQL));
                }

                Query query = getEntityManager().createQuery(jpaQL);

                if (!parameters.isEmpty()) {
                    for (QueryParameter param : parameters) {
                        if (log.isTraceEnabled()) {
                            log.trace("Parameter: ".concat(param.getName()).concat(" Value: ")
                                    .concat(param.getValue().toString()));
                        }
                        query.setParameter(param.getName(), param.getValue());
                    }
                }

                result = query.getResultList();

                pesquisado = true;

                if (log.isDebugEnabled()) {
                    log.debug("Query executada com sucesso.");
                    if (result != null && !result.isEmpty()) {
                        log.debug("Lista de Objetos carregada com sucesso.");
                        log.debug("Foram encontradas ".concat(String.valueOf(result.size())).concat(" entidades."));
                    }
                }
            } catch (Exception e) {
                log.error("Problemas na execucao da Query: ".concat(jpaQL), e);
            }
        } else {
            log.info("JpaQL nao foi implementado.");
        }
    }

    /**
     * Método que monta e retorna uma lista contendo os parâmetros a serem
     * utilizados na clausula Where da jpaQL suportada pela instância da
     * QueryController e deve sobrescrito na classe filha. Caso não seja
     * necessário o uso de parâmetros na jpaQL não é preciso sobrescrevê-lo. <br>
     * <b>IMPORTANTE</b>: Sobrescrever método somente quando for necessário
     * utilizar parâmetros na jpaQL.
     * @return Lista de parâmetros a serem utilizados na execução da Query.
     */
    protected List<QueryParameter> getParameters() {
        return new ArrayList<QueryParameter>();
    }

    /**
     * Retorna a Query JPA que será executada pelo controller. <br>
     * <b>IMPORTANTE</b>: Implementar na Classe Entity o construtor necessário
     * para atender o retorno específico da jpaQL, caso necessário. Exemplo: <br>
     * 
     * <pre>
     * // JPA Query Language
     * String jpaQL = "select new br.com.trixo.cms.entity.Entidade(e.id, e.param1) from Entidade e"
     * // Entidade persistente
     * public class Entidade {
     *   // É necessário criar um construtor vázio para que o JPA possa criar novas instância da
     *   // classe em questão
     *   public Entidade() {
     *   }
     *   // Construtor para atender o jpaQL
     *   public Entidade(Long id, String param1) {
     *   }
     * }
     * </pre>
     * @return jpaQL Query JPA
     */
    protected abstract String getJpaQL();

    /**
     * Classe utilizada para montar os parâmetros que serão utilizados na
     * clausula Where do jpaQL.
     * @author Orlei Bicheski
     */
    public class QueryParameter {
        /**
         * Nome do parâmetro.
         */
        private String name;

        /**
         * Valor referente ao parâmetro informado
         */
        private Object value;

        /**
         * Construtor padrão.
         * @param name
         * @param value
         */
        public QueryParameter(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the value
         */
        public Object getValue() {
            return value;
        }

    }
}
