/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda. 
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.controller.entity.PaginaEntity;
import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.qualifier.PaginaSelecionada;

/**
 * Classe que controla a montagem dos itens de menu de acordo os categorias da
 * página.
 * 
 * @author Orlei Bicheski
 * 
 */
@Named
public class MontarCategoriaPagina extends CustomController {

    /**
     * serialID.
     */
    private static final long serialVersionUID = -3606939778683276424L;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Referência da página.
     */
    @Inject
    @PaginaSelecionada
    private Pagina pagina;

    /**
     * Referência para o EntityController do objeto Página.
     */
    @Inject
    private PaginaEntity paginaEntity;

    /**
     * Coleção contendo a referência de um objeto tipo <code>Map</code> que
     * conterá quatro chaves:
     * <ul>
     * <li>descricao</li>
     * <li>tipo</li>
     * <li>categoriaId</li>
     * </ul>
     */
    private List<Map<String, Object>> categorias = new ArrayList<Map<String, Object>>();

    /**
     * Retorna as categorias disponíveis para a página especificada pelo
     * usuário.
     * 
     * @return the categorias
     */
    public List<Map<String, Object>> getCategorias() {
	return categorias;
    }

    /**
     * Executa a carga dos itens de menu de tipos de conteúdo a partir das
     * categorias associadas ao template da página selecionada pelo usuário. O
     * objetivo desse método disponibilizar para a tela itens de menu que
     * corresponde aos tipos de conteúdo das categorias disponíveis na página.
     */
    @PostConstruct
    public void load() {
	paginaEntity.setId(pagina.getId());
	Pagina pagina = paginaEntity.getInstance();

	Collection<Categoria> categoriasTemplate = pagina.getTemplate().getCategorias();

	if (!categoriasTemplate.isEmpty()) {
	    for (Categoria categoria : categoriasTemplate) {
		Map<String, Object> dados = new HashMap<String, Object>();
		dados.put("tipo", categoria.getTipo().getDescricao());
		dados.put("tableId", categoria.getTipo().name() + categoria.getId());
		dados.put("descricao", categoria.getDescricao());
		dados.put("categoriaId", categoria.getId());

		categorias.add(dados);
	    }
	} else {
	    log.warn("Nao existem categorias registradas para a pagina "
		    .concat(pagina.getNome()));
	}
    }
}
