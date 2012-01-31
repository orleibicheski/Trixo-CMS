/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.conteudo;

/**
 * Enumerado utilizado na classe Categoria para identificar o tipo de conteúdo
 * que será atendido por determinada Categoria.
 * @author Orlei Bicheski
 * @version $Revision$
 */
public enum TipoConteudo {
    IMAGEN("Imagem", Imagem.class, ImagemAssociado.class), DOWNLOAD("Download", Download.class, DownloadAssociado.class), TEXTO_CURTO(
            "Texto Curto", TextoCurto.class, TextoCurtoAssociado.class), TEXTO_LONGO("Texto Longo", TextoLongo.class,
            TextoLongoAssociado.class), LINK_INTERNO("Link Interno", LinkInterno.class, LinkInternoAssociado.class), LINK_EXTERNO(
            "Link Externo", LinkExterno.class, LinkExternoAssociado.class);

    /**
     * Descrição do tipo do Conteúdo
     */
    private String descricao;

    private Class<? extends Conteudo> conteudoClass;

    private Class<? extends ConteudoAssociado<? extends Conteudo>> conteudoAssociadoClass;

    private TipoConteudo(String descricao, Class<? extends Conteudo> conteudoClass,
            Class<? extends ConteudoAssociado<? extends Conteudo>> conteudoAssociadoClass) {
        this.descricao = descricao;
        this.conteudoClass = conteudoClass;
        this.conteudoAssociadoClass = conteudoAssociadoClass;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @return the conteudoClass
     */
    public Class<? extends Conteudo> getConteudoClass() {
        return conteudoClass;
    }

    /**
     * @return the conteudoAssociadoClass
     */
    public Class<? extends ConteudoAssociado<? extends Conteudo>> getConteudoAssociadoClass() {
        return conteudoAssociadoClass;
    }

}
