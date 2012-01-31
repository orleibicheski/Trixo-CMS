/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.trixo.cms.entity.conteudo.Conteudo;
import br.com.trixo.cms.entity.conteudo.ConteudoAssociado;
import br.com.trixo.cms.entity.conteudo.SituacaoConteudoAssociado;
import br.com.trixo.cms.entity.conteudo.TextoCurtoAssociado;
import br.com.trixo.cms.entity.conteudo.TextoLongoAssociado;
import br.com.trixo.cms.entity.publicacao.PublicacaoAgendada;
import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.entity.site.SituacaoPagina;

/**
 * Repositório reposnsável pelas consultas de conteúdos utilizadas pelo sistema.
 * @author rafaelabreu
 */
public class EntityRepo extends RepoEntityManager {

    /**
     * serialID.
     */
    private static final long serialVersionUID = 7743445206132132161L;

    /**
     * Coleção de situações pendentes estaticas.
     */
    public static final List<SituacaoConteudoAssociado> SITUACOES_CONTEUDO_ASSOCIADO_PENDENTES_PUBLICACAO = new ArrayList<SituacaoConteudoAssociado>();

    /**
     * Coleção de situções para publicação estáticas
     */
    public static final List<SituacaoConteudoAssociado> SITUACAO_CONTEUDO_ASSOCIADO_PARA_PUBLICACAO = new ArrayList<SituacaoConteudoAssociado>();

    /**
     * Coleção de situções para publicação estáticas
     */
    public static final List<SituacaoPagina> SITUACAO_PAGINA_PENDENTES_PUBLICACAO = new ArrayList<SituacaoPagina>();

    /**
     * Carrega as coleções a serem utilizadas.
     */
    static {
        SITUACOES_CONTEUDO_ASSOCIADO_PENDENTES_PUBLICACAO.add(SituacaoConteudoAssociado.CRIADO);
        SITUACOES_CONTEUDO_ASSOCIADO_PENDENTES_PUBLICACAO.add(SituacaoConteudoAssociado.AGUARDANDO_ATUALIZACAO);

        SITUACAO_CONTEUDO_ASSOCIADO_PARA_PUBLICACAO.add(SituacaoConteudoAssociado.PUBLICADO);
        SITUACAO_CONTEUDO_ASSOCIADO_PARA_PUBLICACAO.add(SituacaoConteudoAssociado.SOLICITADO_PUBLICACAO);
        SITUACAO_CONTEUDO_ASSOCIADO_PARA_PUBLICACAO.add(SituacaoConteudoAssociado.SOLICITADO_DESPUBLCIACAO);

        SITUACAO_PAGINA_PENDENTES_PUBLICACAO.add(SituacaoPagina.CRIADA);
        SITUACAO_PAGINA_PENDENTES_PUBLICACAO.add(SituacaoPagina.AGUARDANDO_PUBLICACAO);
    }

    /**
     * Lista os conteúdos associados relacionados ao site, as páginas e é
     * legivel para publicação.
     * @param <R>
     * @param site
     *            Site relacionado
     * @param paginas
     *            Coleção de páginas relacionadas
     * @param de
     *            legivel a partir de
     * @param ate
     *            legivel até
     * @param tipoConteudoAssociado
     *            Class a ser utilizado na consulta
     * @return Coleção de conteúdos associados de todos os tipos que entram nas
     *         condições da pesquisa.
     */
    @SuppressWarnings("unchecked")
    private <R extends ConteudoAssociado<Conteudo>> List<R> listarPendentesAtualizacao(Site site, List<Pagina> paginas,
            Date de, Date ate, Class<? extends ConteudoAssociado<? extends Conteudo>> tipoConteudoAssociado) {

        List<R> retorno = new ArrayList<R>();

        StringBuffer jpaQL = new StringBuffer();

        jpaQL.append("select a ");
        jpaQL.append(" from ");
        jpaQL.append(tipoConteudoAssociado.getSimpleName());
        jpaQL.append(" a ");
        jpaQL.append(" left join a.conteudo c ");
        jpaQL.append(" where 1 = 1 ");
        jpaQL.append(" and c.site = :site ");
        jpaQL.append(" and a.situacao in (:situacoes) ");
        jpaQL.append(" and ((a.agendadoDe is null or a.agendadoDe <= :de) ");
        jpaQL.append(" and (a.agendadoAte is null or a.agendadoAte >= :ate)) ");
        jpaQL.append(" and a.pagina in (:paginas) ");

        Query query = getEntityManager().createQuery(jpaQL.toString());

        query.setParameter("site", site);
        query.setParameter("situacoes", SITUACOES_CONTEUDO_ASSOCIADO_PENDENTES_PUBLICACAO);
        query.setParameter("paginas", paginas);
        query.setParameter("de", de);
        query.setParameter("ate", ate);

        retorno = query.getResultList();

        return (List<R>) retorno;
    }

    /**
     * Lista os conteúdos associados relacionados ao site, as páginas e é
     * legivel para publicação.
     * @param site
     *            Site relacionado
     * @param paginas
     *            Coleção de páginas relacionadas
     * @param de
     *            legivel a partir de
     * @param ate
     *            legivel até
     * @return Coleção de conteúdos associados de todos os tipos que entram nas
     *         condições da pesquisa.
     */
    public List<ConteudoAssociado<Conteudo>> listarPendentesAtualizacao(Site site, List<Pagina> paginas, Date de,
            Date ate) {

        List<ConteudoAssociado<Conteudo>> retorno = new ArrayList<ConteudoAssociado<Conteudo>>();

        // TODO abreu: arrumar isso daqui
        retorno.addAll((List<ConteudoAssociado<Conteudo>>) listarPendentesAtualizacao(site, paginas, de, ate,
                TextoCurtoAssociado.class));
        retorno.addAll((List<ConteudoAssociado<Conteudo>>) listarPendentesAtualizacao(site, paginas, de, ate,
                TextoLongoAssociado.class));

        // TODO abreu: adicionar aqui as demais consultas por tipo de conteudo
        // associado.

        return retorno;
    }

    /**
     * Lista os conteúdos associados relacionados ao site, as páginas e é
     * legivel para publicação agora.
     * @param site
     *            Site relacionado
     * @param paginas
     *            Coleção de páginas relacionadas
     * @return Coleção de conteúdos associados de todos os tipos que entram nas
     *         condições da pesquisa.
     */
    public List<ConteudoAssociado<Conteudo>> listarPendentesAtualizacaoAgora(Site site, List<Pagina> paginas) {

        Date agora = new Date();

        return listarPendentesAtualizacao(site, paginas, agora, agora);
    }

    /**
     * Lista os conteúdos relacionados ao site, página, categoria e é legivel
     * para publicação.
     * @param site
     *            Site relacionado
     * @param pagina
     *            Página relacionada
     * @param categoria
     *            Categoria relacionada
     * @return Coleção de conteúdos de todos os tipos que entram nas condições
     *         da pesquisa.
     */
    public List<Conteudo> listarAssociados(Pagina pagina, Categoria categoria) {

        Site site = pagina.getSite();

        Date de = new Date();
        Date ate = new Date();

        List<Conteudo> retorno = new ArrayList<Conteudo>();

        StringBuffer jpaQL = new StringBuffer();

        jpaQL.append(" select distinct c ");
        jpaQL.append(" from ").append(categoria.getTipo().getConteudoAssociadoClass().getSimpleName()).append(" ca ");
        jpaQL.append(" join ca.conteudo c ");
        jpaQL.append(" where 1=1 ");
        jpaQL.append(" and c.site = :site ");
        jpaQL.append(" and ca.situacao in (:situacoes) ");
        jpaQL.append(" and ((ca.agendadoDe is null or ca.agendadoDe <= :de) ");
        jpaQL.append(" and (ca.agendadoAte is null or ca.agendadoAte >= :ate)) ");
        jpaQL.append(" and ca.pagina = :pagina ");
        jpaQL.append(" and ca.categoria = :categoria ");

        TypedQuery<Conteudo> query = getEntityManager().createQuery(jpaQL.toString(), Conteudo.class);

        query.setParameter("site", site);
        query.setParameter("situacoes", SITUACAO_CONTEUDO_ASSOCIADO_PARA_PUBLICACAO);
        query.setParameter("de", de);
        query.setParameter("ate", ate);
        query.setParameter("pagina", pagina);
        query.setParameter("categoria", categoria);

        return query.getResultList();
    }

    /**
     * @param associado
     *            Referência do conteúdo associado que está sendo criado,
     *            modificado ou removido
     * @return List<ConteudoAssociado<? extends Conteudo>>
     */
    @SuppressWarnings("unchecked")
    public List<ConteudoAssociado<? extends Conteudo>> listarConteudosPublicar(
            ConteudoAssociado<? extends Conteudo> associado) {
        Conteudo conteudo = associado.getConteudo();

        StringBuilder jpaQL = new StringBuilder();
        jpaQL.append("from ");
        jpaQL.append(associado.getClass().getSimpleName());
        jpaQL.append(" ca ");
        jpaQL.append("where ca.conteudo.id = :conteudoId and ");
        jpaQL.append("(ca.agendadoDe is null or ca.agendadoDe <= :agora) ");

        Query query = getEntityManager().createQuery(jpaQL.toString());

        query.setParameter("conteudoId", conteudo.getId());
        query.setParameter("agora", new Date());

        return (List<ConteudoAssociado<? extends Conteudo>>) query.getResultList();
    }

    /**
     * Obtém um agendamento já existente para a data informada e o site.
     * @param agendamento
     *            Data utilizado para encontrar a publicação agendada
     * @param site
     *            Site da publicação agendada
     * @return Caso sejam retornadas mais de um agendamento, é retornado o
     *         primeiro.
     */
    public PublicacaoAgendada getPublicacaoAgendada(Date agendamento, Site site) {

        StringBuilder jpaQL = new StringBuilder();
        jpaQL.append("from PublicacaoAgendada pa ");
        jpaQL.append("where ");
        jpaQL.append("pa.agendamento = :agendamento and ");
        jpaQL.append("pa.site = :site ");

        TypedQuery<PublicacaoAgendada> query = getEntityManager().createQuery(jpaQL.toString(),
                PublicacaoAgendada.class);

        query.setParameter("agendamento", agendamento);
        query.setParameter("site", site);

        List<PublicacaoAgendada> pas = query.getResultList();

        // trazer somente o primeiro caso exista mais que uma!
        return !pas.isEmpty() ? pas.get(0) : null;
    }

}
