/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.custom;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.com.trixo.cms.controller.entity.TemplateEntity;
import br.com.trixo.cms.controller.query.TemplateQuery;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.entity.site.Template;
import br.com.trixo.cms.qualifier.SiteSelecionado;

/**
 * Classe que controla a montagem dos nós dos arquivos do site, estes serão
 * organizados em templates e suas páginas.
 * @author Orlei Bicheski
 */
@Named
public class MontarArvoreArquivos extends CustomController {

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
     * Referência do Site selecionado.
     */
    @Inject
    @SiteSelecionado
    private Site siteSelecionado;

    /**
     * Classe responsável por realizar a consulta de tempaltes do site.
     */
    @Inject
    private TemplateQuery templateQuery;

    /**
     * Classe responsável por realizar a consulta de tempaltes do site.
     */
    @Inject
    private TemplateEntity templateEntity;

    /**
     * Objeto que representa a árvore de templates e páginas.
     */
    private TreeNode arvore;

    /**
     * Executa a carga das pastas e páginas.
     */
    @PostConstruct
    public void load() {
        arvore = new DefaultTreeNode("root", null);

        // TreeNode rootDir = new DefaultTreeNode("/", arvore);

        templateQuery.query();

        List<Template> templates = templateQuery.getResult();

        for (Template template : templates) {
            TreeNode templateNo = new DefaultTreeNode(template, arvore);

            // templateEntity.clearInstance();
            // templateEntity.setId(template.getId());
            //
            // // arrumar essa merda aqui
            // template = templateEntity.getInstance();

            List<Pagina> paginas = template.getPaginas();

            for (Pagina pagina : paginas) {

                TreeNode paginaNo = new DefaultTreeNode(pagina, templateNo);

            }

        }
    }

    /**
     * @return the rootArvore
     */
    public TreeNode getArvore() {
        return arvore;
    }

    private TreeNode selectedNode;

    public void setNoSelecionado(TreeNode noSelecionado) {

        Object objeto = noSelecionado.getData();

        if (objeto instanceof Template) {

            Template template = (Template) objeto;

            templateEntity.setId(template.getId());
        } else if (objeto instanceof Pagina) {

            Pagina pagina = (Pagina) objeto;

            // paginaEntity.setId(pagina.getId())
        }

    }

    /**
     * Remove managed entity instance from the Persistence Context and the
     * underlying database. If the remove is successful, a log message is
     * printed, a {@link javax.faces.application.FacesMessage} is added and a
     * transaction success event raised.
     * @see Home#deletedMessage()
     * @see Home#raiseAfterTransactionSuccessEvent()
     * @return "removed" if the remove is successful
     */
    public String remove() {
        log.info("Metodo Remove executado.");

        return "";
    }

    /**
     * @return the selectedNode
     */
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
     * @param selectedNode
     *            the selectedNode to set
     */
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void displaySelectedSingle(ActionEvent event) {

        if (selectedNode != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", selectedNode.getData()
                    .toString());

            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}
