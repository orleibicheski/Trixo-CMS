package br.com.trixo.cms.entity.conteudo;

/**
 * Enumerado para armazenar as situações de conteúdo associado.
 * <ul>
 * <li>CRIACAO</li>
 * <li>AGUARDANDO_ATUALIZACAO</li>
 * <li>SOLICITADO_PUBLICACAO</li>
 * <li>PUBLICADO</li>
 * <li>SOLICITADO_DESPUBLCIACAO</li>
 * <li>DESPUBLICADO</li>
 * </ul>
 * @author Orlei Bicheski.
 *
 */
public enum SituacaoConteudoAssociado {

    CRIADO, AGUARDANDO_ATUALIZACAO, SOLICITADO_PUBLICACAO, PUBLICADO, SOLICITADO_DESPUBLCIACAO, DESPUBLICADO;

}
