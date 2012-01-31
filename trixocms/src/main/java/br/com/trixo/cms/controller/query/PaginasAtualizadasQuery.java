/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.query;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.qualifier.SiteSelecionado;
import br.com.trixo.cms.repository.EntityRepo;

/**
 * @author Rafael Abreu
 * @version $Revision$
 */
@Named
public class PaginasAtualizadasQuery extends QueryController<Pagina> {

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

        jpaQL.append("from Pagina p ");
        jpaQL.append("where 1=1 ");
        jpaQL.append("and p.site.id = :id ");
        jpaQL.append("and p.situacao in (:situacoes) ");

        return jpaQL.toString();
    }

    /**
     * @see br.com.trixo.cms.controller.query.QueryController#getParameters()
     */
    @Override
    protected List<QueryParameter> getParameters() {

        List<QueryParameter> parametros = super.getParameters();
        parametros.add(new QueryParameter("id", site.getId()));
        parametros.add(new QueryParameter("situacoes", EntityRepo.SITUACAO_PAGINA_PENDENTES_PUBLICACAO));

        return parametros;
    }
}
