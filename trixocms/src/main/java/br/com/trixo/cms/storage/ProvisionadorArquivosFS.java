package br.com.trixo.cms.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.apache.commons.logging.Log;

/**
 * Implementação do provisionador para Arquivos em disco local.
 * TODO trocar essa implementação por uma mais confiavel, como cassandra ou
 * solução especifica de storage distribuido.
 * @author rafaelabreu
 */
@Default
@RequestScoped
public class ProvisionadorArquivosFS implements ProvisionadorArquivos {

    /**
     * Caminho para os arquivos.
     * TODO abreu: colocar isso em properties.
     */
    private static final String diretorioPadrao = "/Users/rafaelabreu/trixocms_home/arquivos";

    /**
     * Diretório padrão com os arquivos.
     */
    private File diretorio;

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Construtor default.
     */
    public ProvisionadorArquivosFS() {
        diretorio = new File(diretorioPadrao);

        if (!diretorio.exists()) {
            throw new IllegalStateException("O diretório " + diretorioPadrao + " não existe.");
        } else if (!diretorio.isDirectory()) {
            throw new IllegalStateException("O caminho " + diretorioPadrao + " não é um diretório.");
        } else if (!diretorio.canRead()) {
            throw new IllegalStateException("O diretório " + diretorioPadrao + " não tem permissão de leitura.");
        } else if (!diretorio.canWrite()) {
            throw new IllegalStateException("O diretório " + diretorioPadrao + " não tem permisão de escrita.");
        }
    }

    @Override
    public String salvar(byte[] conteudo) throws ProvisionadorException {

        UUID key = UUID.randomUUID();

        try {
            FileOutputStream fileOut = null;

            try {

                File novoArquivo = new File(diretorioPadrao, key.toString());

                fileOut = new FileOutputStream(novoArquivo);

                fileOut.write(conteudo);

            } finally {
                fileOut.close();
            }
        } catch (IOException e) {
            log.error("Problemas ao provisionar novo arquivo.", e);
        }

        return key.toString();
    }

    @Override
    public void remover(String key) throws ProvisionadorException {

        File arquivo = new File(diretorioPadrao, key.toString());

        arquivo.delete();
    }

    @Override
    public byte[] buscar(String key) throws ProvisionadorException {

        byte[] conteudo = null;

        try {
            FileInputStream fileIn = null;

            try {

                File arquivo = new File(diretorioPadrao, key.toString());

                conteudo = new byte[(int) arquivo.length()];

                fileIn = new FileInputStream(arquivo);

                fileIn.read(conteudo);
            } finally {
                fileIn.close();
            }
        } catch (IOException e) {
            log.error("Problemas ao provisionar novo arquivo.", e);
        }

        return conteudo;
    }
}
