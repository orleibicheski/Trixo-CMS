/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda. 
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.query.conteudo;

import java.util.Map;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import br.com.trixo.cms.entity.conteudo.Download;
import br.com.trixo.cms.entity.conteudo.DownloadAssociado;

/**
 * Query responsável por buscar conteúdos associados do tipo {@link Download}.
 * 
 * @see br.com.trixo.cms.controller.query.conteudo.ConteudoPaginaQuery
 * @author Orlei Bicheski
 * 
 */
@Named
@ViewScoped
public class DownloadPaginaQuery extends ConteudoPaginaQuery<Map<String, ?>> {

    /**
     * serialID.
     */
    private static final long serialVersionUID = -1987006702122431011L;

    /**
     * @see br.com.trixo.cms.controller.query.QueryController#getJpaQL()
     */
    @Override
    protected String getJpaQL() {
	return gerarJpaQL(DownloadAssociado.class.getSimpleName()).toString();
    }
}
