/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informática Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.site;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.trixo.cms.entity.TrixoCMSDefaultEntity;

/**
 * @author Rafael Abreu
 * @version $revision$
 */
@Entity
public class Host extends TrixoCMSDefaultEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 634091893115941392L;

    /**
     * Host de acesso
     */
    @Column(length = 100, nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String host = "/";

    /**
     * Porta de acesso
     */
    @Column(length = 5, nullable = false)
    @NotNull
    @Min(value = 1)
    @Max(value = 99999)
    private Integer porta;

    /**
     * Usuário de acesso
     */
    @Column(length = 100, nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String usuario;

    /**
     * Password de acesso
     */
    @Column(length = 100, nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String senha;

    /**
     * Diretório onde serão armazenados os arquivos
     */
    @Column(length = 100, nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String diretorio = "";

    /**
     * Site do hosting
     */
    @OneToOne
    @NotNull
    @JoinColumn(nullable = false)
    private Site site;

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the diretorio
     */
    public String getDiretorio() {
        return diretorio;
    }

    /**
     * @param diretorio
     *            the diretorio to set
     */
    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
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
     * @return the porta
     */
    public Integer getPorta() {
        return porta;
    }

    /**
     * @param porta
     *            the porta to set
     */
    public void setPorta(Integer porta) {
        this.porta = porta;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario
     *            the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha
     *            the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

}