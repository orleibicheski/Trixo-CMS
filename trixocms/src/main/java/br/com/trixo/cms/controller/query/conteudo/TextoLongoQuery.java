/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.query.conteudo;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.trixo.cms.controller.query.QueryController;
import br.com.trixo.cms.entity.conteudo.ConteudoAssociado;
import br.com.trixo.cms.entity.conteudo.TextoLongo;
import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.qualifier.PaginaSelecionada;

/**
 * Query responsável por realizar a busca dos conteúdos associados a página
 * seleciona pelo usuário. A consulta fará a consulta no objeto
 * {@link ConteudoAssociado} específico do conteúdo {@link TextoLongo}.<br>
 * Com isso é possível recuperar as referências registradas na base de dados
 * para o determinado tipo de conteúdo. <br>
 * O retorno da Query será armazenado em objeto do tipo {@link Map}. Abaixo
 * segue a composição do retorno (Key: Value):<br>
 * <ul>
 * <li>0: ID Conteúdo Associado Texto Longo</li>
 * <li>1: Tipo de conteúdo associado ao objeto {@link Categoria} do Conteúdo
 * Associado</li>
 * <li>2: Descrição da Categoria</li>
 * <li>3: ID do Conteúdo Texto Longo</li>
 * <li>4: Descrição do Conteúdo Texto Longo</li> TODO [Orlei] Revisar!!!
 * </ul>
 * @author Orlei Bicheski
 */
@Named
// @ViewScoped
public class TextoLongoQuery extends QueryController<TextoLongo> {

    /**
     * serialID.
     */
    private static final long serialVersionUID = -1987006702122431011L;

    /**
     * Referência do Site.
     */
    @Inject
    @PaginaSelecionada
    private Pagina pagina;

    /**
     * 
     */
    private Categoria categoria;

    /**
     * @see br.com.trixo.cms.controller.query.QueryController#getJpaQL()
     */
    @Override
    protected String getJpaQL() {
        StringBuilder jpaQL = new StringBuilder();

        jpaQL.append("select tla.conteudo from TextoLongoAssociado tla ");

        return jpaQL.toString();
    }

    /**
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria
     *            the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
