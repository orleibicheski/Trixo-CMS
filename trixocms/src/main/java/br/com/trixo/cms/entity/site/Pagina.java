/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.site;

import java.text.Normalizer;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import br.com.trixo.cms.entity.TrixoCMSDefaultEntity;

/**
 * TODO: comentar
 * @author Orlei Bicheski
 * @version $revision$
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "NOME_UNICO", columnNames = { "SITE_ID", "NOME" }) })
public class Pagina extends TrixoCMSDefaultEntity {

    /**
     * serialID.
     */
    private static final long serialVersionUID = 7163572080905835547L;

    @Column(length = 255, nullable = false)
    @NotNull
    private String nome;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Site site;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Template template;

    /**
     * Situação da página
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoPagina situacao;

    /**
     * A partir desta data que a página torna-se legível para publicação.
     */
    private Date legivelDe;

    /**
     * Até esta data a página está legível para publicações.
     */
    private Date legivelAte;

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the site
     */
    public Site getSite() {
        return site;
    }

    /**
     * @param site
     *            the site to set
     */
    public void setSite(Site site) {
        this.site = site;
    }

    /**
     * @return the template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * @param template
     *            the template to set
     */
    public void setTemplate(Template template) {
        this.template = template;
    }

    /**
     * @return the situacao
     */
    public SituacaoPagina getSituacao() {
        return situacao;
    }

    /**
     * @param situacao
     *            the situacao to set
     */
    public void setSituacao(SituacaoPagina situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the legivelDe
     */
    public Date getLegivelDe() {
        return legivelDe;
    }

    /**
     * @param legivelDe
     *            the legivelDe to set
     */
    public void setLegivelDe(Date legivelDe) {
        this.legivelDe = legivelDe;
    }

    /**
     * @return the legivelAte
     */
    public Date getLegivelAte() {
        return legivelAte;
    }

    /**
     * @param legivelAte
     *            the legivelAte to set
     */
    public void setLegivelAte(Date legivelAte) {
        this.legivelAte = legivelAte;
    }

    /**
     * @return Nome do arquivo.
     */
    public String getNomeArquivo() {

        return getId() + "_" + normalizador(getNome()) + "." + getExtensao();
    }

    /**
     * @return Extensão do arquivo. Por enquanto será somente HTML.
     */
    public String getExtensao() {

        return "html";
    }

    /**
     * Normaliza removendo acentuação, barras e espaços.
     * @param string
     *            String a ser normalizada
     * @return String normalizada.
     */
    public String normalizador(String string) {

        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        string = string.trim();
        string = string.replaceAll("[^\\p{ASCII}]", "");
        string = string.replaceAll("[ \t]+", "_");
        string = string.replaceAll("[-./]+", "_");

        return string;
    }

}
