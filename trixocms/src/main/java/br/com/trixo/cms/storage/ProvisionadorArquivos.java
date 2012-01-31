package br.com.trixo.cms.storage;

/**
 * Interface de provisionamento de arquivos. Utilizado para salvar qualquer tipo
 * de arquivo do sistema.
 * @author rafaelabreu
 */
public interface ProvisionadorArquivos {

    /**
     * Provisiona o conteúdo de um arquivo no local padrão.
     * @return Chave do arquivo, gerada pelo provisionador.
     * @param conteudo
     *            Array de bytes com o conteúdo do arquivo.
     */
    String salvar(byte[] conteudo) throws ProvisionadorException;

    /**
     * Remove o arquivo informado.
     * @param key
     *            Chave do arquivo a ser removido.
     */
    void remover(String key) throws ProvisionadorException;

    /**
     * Obtem o conteudo do arquivo informado.
     * @param key
     *            Chave do arquivo a ser removido.
     * @return Conteúdo em bytes
     */
    byte[] buscar(String key) throws ProvisionadorException;

}
