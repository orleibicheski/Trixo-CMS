/**
 * 
 */
package br.com.trixo.cms.publicador.exception;

import br.com.trixo.cms.entity.publicacao.CausaErro;

/**
 * Exception lançada quando algum problema em uma publicação de site ocorre.
 * @author rafaelabreu
 */
public class PublicacaoException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 2683403090141219564L;
    /**
     * Tipo do erro ocorrido na publicação.
     */
    private CausaErro causa = CausaErro.INDETERMINADO;

    /**
     * Contrutor default.
     */
    public PublicacaoException() {

    }

    /**
     * @param tipoErro
     *            Tipo do erro ocorrido.
     */
    public PublicacaoException(CausaErro causa) {
        this.causa = causa;
    }

    /**
     * @return the causa
     */
    public CausaErro getCausa() {
        return causa;
    }

}
