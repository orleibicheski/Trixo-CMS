/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.query;

import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.qualifier.SiteSelecionado;

/**
 * @author Orlei Bicheski
 * @version $Revision$
 */
@Named
@ViewScoped
public class CategoriaQuery extends QueryController<Categoria> {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = -738042345827082517L;

    /**
     * Referência do Site.
     */
    @Inject
    @SiteSelecionado
    private Site site;

    /**
     * @see br.com.trixo.cms.controller.query.QueryController#getJpaQL()
     */
    @Override
    protected String getJpaQL() {
        StringBuilder jpaQL = new StringBuilder();

        jpaQL.append("select new ");
        jpaQL.append("br.com.trixo.cms.entity.site.Categoria(");
        jpaQL.append("c.id, c.marcador, c.descricao, c.alteracao) ");
        jpaQL.append("from Categoria c ");
        jpaQL.append("where c.site.id = :id");

        return jpaQL.toString();
    }

    /**
     * @see br.com.trixo.cms.controller.query.QueryController#getParameters()
     */
    @Override
    protected List<QueryParameter> getParameters() {
        List<QueryParameter> parametros = super.getParameters();
        parametros.add(new QueryParameter("id", site.getId()));

        return parametros;
    }
}
