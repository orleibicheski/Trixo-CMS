package br.com.trixo.cms.publicador.velocity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.entity.site.Template;

public class SiteResourceLoader extends ResourceLoader {

    @Override
    public InputStream getResourceStream(String nomeTemplate) throws ResourceNotFoundException {

        // TODO remover e comentar a classe
        System.out.println("getResourceStream");

        EntityManager entityManager = (EntityManager) rsvc.getApplicationAttribute("entityManager");
        Site site = (Site) rsvc.getApplicationAttribute("site");

        if (entityManager == null || site == null)
            throw new ResourceNotFoundException("EntityManager ou Site nulos ao pesquisar template:" + nomeTemplate);

        if (log.isDebugEnabled())
            log.debug("Buscando template: " + nomeTemplate + " site:" + site.getNome());

        String jpaQL = "from Template t where t.site.id = :idSite and t.nome = :nomeTemplate";

        try {
            TypedQuery<Template> query = entityManager.createQuery(jpaQL, Template.class);

            query.setParameter("idSite", site.getId());
            query.setParameter("nomeTemplate", nomeTemplate);

            Template template = query.getSingleResult();

            if (template != null) {
                if (log.isDebugEnabled())
                    log.debug("Encontrado template: " + nomeTemplate + " site:" + site.getNome());

                return new ByteArrayInputStream(template.getValor().getBytes());
            } else {
                if (log.isDebugEnabled())
                    log.debug("Não encontrado template: " + nomeTemplate + " site:" + site.getNome());
            }

        } catch (Exception e) {
            log.error("Problemas na execucao da Query: ".concat(jpaQL), e);
        }

        return null;
    }

    @Override
    public void init(ExtendedProperties properties) {

    }

    @Override
    public long getLastModified(Resource arg0) {

        // TODO rever como fica isso exatamente.

        System.out.println("getLastModified");

        return 0;
    }

    @Override
    public boolean isSourceModified(Resource arg0) {

        // TODO rever como fica isso exatamente.

        System.out.println("isSourceModified");

        return false;
    }

}
