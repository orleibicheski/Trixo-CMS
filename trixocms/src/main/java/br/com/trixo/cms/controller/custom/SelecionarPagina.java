/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.custom;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;

import br.com.trixo.cms.controller.entity.PaginaEntity;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.qualifier.PaginaSelecionada;

/**
 * @author Orlei Bicheski
 * @version $Revision$
 */
@Named
@SessionScoped
public class SelecionarPagina extends CustomController {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = 251070209899228850L;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Referência do objeto Pagina.
     */
    private Pagina pagina;
    
    /**
     * Referência para o EntityController do objeto Página.
     */
    @Inject
    private PaginaEntity paginaEntity;

    /**
     * Retorna a referência do objeto Pagina a partir do Identificador único
     * selecionado.
     * @return {@link Pagina} Referência do objeto Pagina
     */
    @Produces
    @PaginaSelecionada
    @Named("paginaSelecionada")
    public Pagina getPagina() {
        return pagina;
    }

    /**
     * Encaminha para o painel da página.
     * @return Foward para o painel da página.
     */
    public String selecionar(Long id) {
        log.info("Metodo Select executado.");

        paginaEntity.setId(id);
        Pagina pagina = paginaEntity.getInstance();
        setPagina(pagina);
        
        if (log.isDebugEnabled()) {
            log.debug("Referencia do Objeto Pagina: ".concat(pagina.toString()));
            if (getPagina().getId() != null) {
                log.debug("ID a ser removido: ".concat(String.valueOf(pagina.getId())));
            }
        }

        return "painelPagina?faces-redirect=true";
    }

    /**
     * @param pagina
     */
    public void setPagina(Pagina pagina) {
        this.pagina = pagina;
    }
}
