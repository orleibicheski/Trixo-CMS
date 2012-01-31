/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda. 
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.query.conteudo;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import br.com.trixo.cms.controller.query.QueryController;
import br.com.trixo.cms.entity.conteudo.ConteudoAssociado;
import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.qualifier.PaginaSelecionada;

/**
 * Query responsável por realizar a busca dos conteúdos associados a página
 * seleciona pelo usuário. A consulta fará a consulta no objeto
 * {@link ConteudoAssociado} específico de um determinado conteúdo.<br>
 * Com isso é possível recuperar as referências registradas na base de dados
 * para o tipo de conteúdo desejado. <br>
 * O retorno da Query será armazenado em objeto do tipo {@link Map}. Abaixo
 * segue a composição do retorno (Key: Value):<br>
 * <ul>
 * <li>0: ID Conteúdo Associado</li>
 * <li>1: Tipo de conteúdo associado ao objeto {@link Categoria} do Conteúdo
 * Associado</li>
 * <li>2: Descrição da Categoria</li>
 * <li>3: ID do Conteúdo</li>
 * <li>4: Descrição do Conteúdo</li>
 * </ul>
 * 
 * @author Orlei Bicheski
 * 
 */
@SuppressWarnings("serial")
public abstract class ConteudoPaginaQuery<E> extends QueryController<E> {

    /**
     * Referência da Página.
     */
    @Inject
    @PaginaSelecionada
    private Pagina pagina;

    /**
     * Referência da página.
     */
    private Long categoriaId;

    /**
     * @see br.com.trixo.cms.controller.query.QueryController#getParameters()
     */
    @Override
    protected List<QueryParameter> getParameters() {
	List<QueryParameter> parametros = super.getParameters();
	parametros.add(new QueryParameter("paginaId", pagina.getId()));
	parametros.add(new QueryParameter("categoriaId", categoriaId));

	return parametros;
    }

    /**
     * Prepara a Query base de execução da pesquisa de conteúdos associados.
     * Para que a pesquisa seja realizada adequadamente é necessário subtituir o
     * valor 'tabOjeto' (que representa a classe de conteúdo associado desejado)
     * pelo nome da classe de conteúdo.
     * 
     * @param objeto Nome da classe de conteúdo associado
     * @return String JPAQL de pesquisa dos conteúdos associados.
     */
    protected String gerarJpaQL(String objeto) {
	StringBuilder jpaQL = new StringBuilder();

	jpaQL.append("select new ");
	jpaQL.append(objeto);
	jpaQL.append(" (ca.id, ca.categoria.tipo, ca.categoria.descricao,");
	jpaQL.append(" ca.conteudo.id, ca.conteudo.descricao)");
	jpaQL.append(" from ");
	jpaQL.append(objeto);
	jpaQL.append(" ca");
	jpaQL.append(" where ca.pagina.id = :paginaId");
	jpaQL.append(" and ca.categoria.id = :categoriaId");

	return jpaQL.toString();
    }

    /**
     * Executa a Query de determinado conteúdo associado à página e categoria
     * selecionados pelo usuário.
     * 
     * @param categoriaId
     *            Identificador Único da Categoria
     * @return List<E>
     */
    public List<E> result(Long categoriaId) {
	this.categoriaId = categoriaId;
	super.query();

	return super.getResult();
    }

    /**
     * Foi nencessário sobrescrever este método, pois foi necessário criar um
     * método específico para execução da Query, visto que precisamos informar o
     * Identificador Único da categoria correspondente a selecionada pelo
     * usuário.
     * 
     * @see br.com.trixo.cms.controller.query.QueryController#inicializar()
     */
    @Override
    public void inicializar() {
    }
}
