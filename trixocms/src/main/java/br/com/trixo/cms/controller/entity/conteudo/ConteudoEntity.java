/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.entity.conteudo;

import java.util.Date;
import java.util.List;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.controller.entity.CategoriaEntity;
import br.com.trixo.cms.controller.entity.EntityController;
import br.com.trixo.cms.controller.entity.PaginaEntity;
import br.com.trixo.cms.entity.conteudo.Conteudo;
import br.com.trixo.cms.entity.conteudo.ConteudoAssociado;
import br.com.trixo.cms.entity.conteudo.SituacaoConteudoAssociado;
import br.com.trixo.cms.entity.publicacao.PublicacaoAgendada;
import br.com.trixo.cms.entity.publicacao.SituacaoPublicacao;
import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.entity.site.SituacaoPagina;
import br.com.trixo.cms.qualifier.PaginaSelecionada;
import br.com.trixo.cms.qualifier.SiteSelecionado;
import br.com.trixo.cms.repository.EntityRepo;

/**
 * @author Orlei Bicheski
 */
@SuppressWarnings("serial")
public abstract class ConteudoEntity<E extends ConteudoAssociado<?>> extends
	EntityController<E, Long> {

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Referência do Site.
     */
    @Inject
    @SiteSelecionado
    protected Site site;

    /**
     * Referência do Site.
     */
    @Inject
    @PaginaSelecionada
    protected Pagina pagina;

    /**
     * Referência da classe Entity do objeto Categoria.
     */
    @Inject
    protected CategoriaEntity categoriaEntity;

    /**
     * Referência da classe Entity do objeto Página.
     */
    @Inject
    protected PaginaEntity paginaEntity;

    /**
     * Referência do repositório de ferramentas para tratamento de ações de
     * Conteúdo Associado.
     */
    @Inject
    private EntityRepo repositorio;

    /**
     * Retorna a referência da categoria selecionada pelo cliente para criar /
     * editar conteúdo.
     * 
     * @return {@link Categoria}
     */
    public Categoria getCategoria() {
	return categoriaEntity.getInstance();
    }

    /**
     * Set para utilizado pelo botão Criar Contéudo para receber o identificador
     * da categoria e setar no Entity da categoria.
     * 
     * @param categoriaId
     *            Identificador único da categoria
     */
    public void setCategoriaId(Long categoriaId) {
	if (categoriaId != null && categoriaId > 0) {
	    categoriaEntity.setId(categoriaId);
	}
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#newEntity()
     */
    @Override
    public String newEntity() {
	String forward = super.newEntity();
	getConversation().begin();

	return forward;
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#view()
     */
    @Override
    public String view() {
	String forward = super.view();
	categoriaEntity.setId(getInstance().getCategoria().getId());

	return forward;
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#persist()
     */
    @Override
    public String persist() {
	preparar();
	notificarPagina();

	String forward = super.persist();

	return forward;
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#update()
     */
    @Override
    public String update() {
	preparar();
	notificarConteudo();

	String forward = super.update();

	return forward;
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#remove()
     */
    @Override
    public String remove() {
	String forward = super.remove();

	notificarConteudo();

	return forward;
    }

    /**
     * Prepara as informações básicas de um conteúdo.
     * <p>
     * <b>IMPORTANTE</b>: Na edição dos conteúdos algumas atributos não são
     * modificados em virtude de serem objetos utilizados na definição do tipo
     * de conteúdo. Para os relacionamentos sejam acionados foi necessário
     * adicionar um IF que realiza a verifica que se trata de um objeto
     * gerenciado pelo EntityManager.
     * </p>
     */
    protected void preparar() {
	if (!isManaged()) {
	    Categoria categoria = getCategoria();
	    if (categoria == null || !categoria.isCadastrado()) {
		String mensagem = "A referência da Categoria para o ID '${id}' esta nula.";
		mensagem = mensagem.replace("${id}",
			String.valueOf(categoriaEntity.getId()));

		log.error(mensagem);
		throw new IllegalArgumentException(mensagem);
	    }
	    getInstance().setCategoria(categoria);

	    if (pagina == null) {
		pagina.setSituacao(SituacaoPagina.AGUARDANDO_PUBLICACAO);

		String mensagem = "A referência da Pagina esta nula.";
		log.error(mensagem);
		throw new IllegalArgumentException(mensagem);

	    }
	    paginaEntity.setId(pagina.getId());
	    getInstance().setPagina(paginaEntity.getInstance());
	}
    }

    /**
     * Método utilizado para forçar a chamada do método newEntity.
     * <b>Importante:</b> Essa solução foi adotada em virtude da necessidade
     * termos que selecionar uma categoria relacionada à página selecionada para
     * o cadastro de conteúdo. <br>
     * A operação <code>newEntity</code> só deve ser acionada quando for a
     * criação de um novo objeto do tipo conteúdo.<br>
     * TODO Rever solução.
     * 
     * @param event
     * @throws AbortProcessingException
     */
    public void init(ComponentSystemEvent event)
	    throws AbortProcessingException {
	if (!isManaged()) {
	    newEntity();
	}
    }

    /**
     * TODO Deixar esse método implementado em classes mais genéricas.
     * 
     * @return
     */
    public String voltar() {
	if (!getConversation().isTransient())
	    getConversation().end();

	return "voltar";
    }

    @Override
    protected Long converterIdentificador(String value) {
	return Long.parseLong(value);
    }

    /**
     * Provê a referência do objeto de Publicação do conteúdo.
     */
    protected void notificarConteudo() {
	if (getInstance().isAgendado()) {
	    resolverAgendamento(getInstance().getAgendadoDe());
	    resolverAgendamento(getInstance().getAgendadoAte());
	} else {
	    getInstance().setSituacao(
		    SituacaoConteudoAssociado.AGUARDANDO_ATUALIZACAO);
	    getInstance().getPagina().setSituacao(
		    SituacaoPagina.AGUARDANDO_PUBLICACAO);

	    List<ConteudoAssociado<? extends Conteudo>> conteudos = repositorio
		    .listarConteudosPublicar(getInstance());

	    for (ConteudoAssociado<? extends Conteudo> associado : conteudos) {
		if (!associado.getId().equals(getInstance().getId())) {
		    associado
			    .setSituacao(SituacaoConteudoAssociado.AGUARDANDO_ATUALIZACAO);
		    associado.getPagina().setSituacao(
			    SituacaoPagina.AGUARDANDO_PUBLICACAO);
		    getEntityManager().merge(associado);
		}
	    }
	}
    }

    /**
     * 
     * @param agendamento
     *            Data para a execução da publicação agendada
     */
    private void resolverAgendamento(Date agendamento) {
	PublicacaoAgendada agendada = repositorio.getPublicacaoAgendada(
		agendamento, getInstance().getPagina().getSite());

	if (agendada == null) {
	    agendada = new PublicacaoAgendada();
	    agendada.setAgendamento(getInstance().getAgendadoDe());
	    agendada.setSituacao(SituacaoPublicacao.SOLICITADA);
	    agendada.setSite(getInstance().getPagina().getSite());
	}
	agendada.addPagina(getInstance().getPagina());

	if (agendada.isCadastrado()) {
	    getEntityManager().merge(agendada);
	} else {
	    getEntityManager().persist(agendada);
	}
    }

    /**
     * 
     */
    protected void notificarPagina() {
	if (getInstance().isAgendado()) {
	    resolverAgendamento(getInstance().getAgendadoDe());
	    resolverAgendamento(getInstance().getAgendadoAte());
	}

	getInstance().getPagina().setSituacao(
		SituacaoPagina.AGUARDANDO_PUBLICACAO);
    }
}
