/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.primefaces.model.DualListModel;

import br.com.trixo.cms.controller.query.CategoriaQuery;
import br.com.trixo.cms.entity.site.Categoria;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.entity.site.Template;
import br.com.trixo.cms.qualifier.SiteSelecionado;
import br.com.trixo.cms.storage.ProvisionadorArquivos;
import br.com.trixo.cms.storage.ProvisionadorException;

/**
 * @author Orlei Bicheski
 * @version $Revision$
 */
@Named
@ConversationScoped
public class TemplateEntity extends EntityController<Template, Long> {

    /**
     * SerialID.
     */
    private static final long serialVersionUID = 4865641946222410473L;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Identificador único do objeto.
     */
    private Long templateId;

    /**
     * Referência do Site.
     */
    @Inject
    @SiteSelecionado
    private Site site;

    /**
     * Referência da classe Entity do objeto Site.
     */
    @Inject
    private SiteEntity siteEntity;

    /**
     * Referência da classe Entity do objeto Categoria.
     */
    @Inject
    private CategoriaEntity categoriaEntity;

    /**
     * Referência para o controller que tem os dados do arquivo uplodeado.
     * TODO abreu: remover depois da correção para inputtextarea
     */
    // @Inject
    // private UploadArquivos uploadArquivos;

    /**
     * Referência das categorias
     */
    private Categoria[] categoriasSelecionadas;

    /**
     * Controller para obter as categorias cadastradas.
     */
    @Inject
    private CategoriaQuery categoriaQuery;

    /**
     * Provisiona o conteúdo do template
     */
    @Inject
    private ProvisionadorArquivos provisionadorArquivos;

    /**
     * Controller do picklist de seleção de categorias.
     */
    private DualListModel<Categoria> picklistCategorias;

    /**
     * Arquivo para download.
     * TODO abreu: remover depois da correção para inputtextarea
     */
    // private StreamedContent download;

    /**
     * @return the templateId
     */
    public Long getId() {
        return templateId;
    }

    /**
     * @param id
     *            the templateId to set
     */
    public void setId(Long id) {
        this.templateId = id;
    }

    /**
     * @return the categoriasSelecionadas
     */
    public Categoria[] getCategoriasSelecionadas() {
        return categoriasSelecionadas;
    }

    /**
     * @param categoriasSelecionadas
     *            the categoriasSelecionadas to set
     */
    public void setCategoriasSelecionadas(Categoria[] categoriasSelecionadas) {
        this.categoriasSelecionadas = categoriasSelecionadas;
    }

    /**
     * Prepara as informações básicas de uma categoria. Informa o Site
     * selecionado.
     */
    private void preparar() {
        siteEntity.setId(this.site.getId());
        Site site = siteEntity.getInstance();
        if (site == null) {
            String mensagem = "A referência do Site esta nula.";
            log.error(mensagem);
            throw new IllegalArgumentException(mensagem);
        }
        getInstance().setSite(site);

        getInstance().getCategorias().clear();

        for (Categoria categoria : picklistCategorias.getTarget()) {
            categoriaEntity.setId(categoria.getId());
            getInstance().getCategorias().add(categoriaEntity.getInstance());
        }

        // if (uploadArquivos.getArquivo() != null)
        // getInstance().setValor(uploadArquivos.getArquivo().getContents());
        //
        // uploadArquivos.zerar();
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#persist()
     */
    @Override
    public String persist() {
        preparar();

        provisionarConteudo();

        String retorno = null;

        // tentativa de não ficar mantendo arquivos em cadastros que deram
        // problema. Isso não garante nada.
        try {
            retorno = super.persist();
        } catch (Exception e) {
            desprovisionarConteudo();
        }

        return retorno;
    }

    /**
     * Provisiona o conteúdo.
     */
    private void provisionarConteudo() {

        try {
            String externalId = provisionadorArquivos.salvar(instance.getValor().getBytes());

            instance.setReferencia(externalId);
        } catch (ProvisionadorException e) {

            throw new IllegalArgumentException("Ocorre um erro ao salvar o conteúdo do template.");
        }
    }

    /**
     * Desprovisiona o conteúdo.
     */
    private void desprovisionarConteudo() {

        try {
            provisionadorArquivos.remover(instance.getReferencia());

            instance.setReferencia(null);
        } catch (ProvisionadorException e) {

            throw new IllegalArgumentException("Ocorre um erro ao remover o conteúdo do template.");
        }

    }

    private void buscarConteudoProvisionado() {

        try {
            byte[] conteudoBytes = provisionadorArquivos.buscar(instance.getReferencia());

            getInstance().setValor(new String(conteudoBytes));
        } catch (ProvisionadorException e) {

            throw new IllegalArgumentException("Ocorre um erro ao buscar o conteúdo do template.");
        }
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#update()
     */
    @Override
    public String update() {
        preparar();

        desprovisionarConteudo();
        provisionarConteudo();

        return super.update();
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#newEntity()
     */
    @Override
    public String newEntity() {
        // uploadArquivos.zerar();

        categoriaQuery.query();

        List<Categoria> categorias = categoriaQuery.getResult();

        setPicklistCategorias(new DualListModel<Categoria>(categorias, new ArrayList<Categoria>()));

        String forward = super.newEntity();
        getConversation().begin();

        return forward;
    }

    /**
     * @see br.com.trixo.cms.controller.entity.EntityController#view()
     */
    @Override
    public String view() {

        // uploadArquivos.zerar();

        super.view();

        buscarConteudoProvisionado();

        categoriaQuery.query();

        Collection<Categoria> todasCategorias = categoriaQuery.getResult();

        Collection<Categoria> categoriasTemplate = getInstance().getCategorias();

        for (Categoria categoria : categoriasTemplate) {
            log.debug("Removendo categoria: " + categoria.getId() + " da lista disponível.");

            todasCategorias.remove(categoria);
        }

        setPicklistCategorias(new DualListModel<Categoria>((List<Categoria>) todasCategorias,
                (List<Categoria>) categoriasTemplate));

        // categoriasSelecionadas = (Categoria[]) categorias.toArray(new
        // Categoria[categorias.size()]);

        // InputStream in = new ByteArrayInputStream(getInstance().getValor());

        // download = new DefaultStreamedContent(in, "application/octet-stream",
        // getInstance().getNome());

        return "formularioTemplate?faces-redirect=true";
    }

    /**
     * @return the picklistCategorias
     */
    public DualListModel<Categoria> getPicklistCategorias() {
        return picklistCategorias;
    }

    /**
     * @param picklistCategorias
     *            the picklistCategorias to set
     */
    public void setPicklistCategorias(DualListModel<Categoria> picklistCategorias) {
        this.picklistCategorias = picklistCategorias;
    }

    // public StreamedContent getDownload() {
    // return download;
    // }

    @Override
    protected Long converterIdentificador(String value) {
        return Long.parseLong(value);
    }
}
