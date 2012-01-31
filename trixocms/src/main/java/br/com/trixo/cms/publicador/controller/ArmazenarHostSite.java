package br.com.trixo.cms.publicador.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import br.com.trixo.cms.entity.publicacao.CausaErro;
import br.com.trixo.cms.entity.publicacao.Publicacao;
import br.com.trixo.cms.entity.site.Pagina;
import br.com.trixo.cms.entity.site.Site;
import br.com.trixo.cms.publicador.exception.PublicacaoException;

public class ArmazenarHostSite {

    private FTPClient ftp = new FTPClient();

    /**
     * Referência para Logger.
     */
    @Inject
    private Log log;

    /**
     * Site da publicação.
     */
    private Site site;

    /**
     * Site da publicação.
     */
    private Publicacao publicacao;

    public void carregar(Site site, Publicacao publicacao) {
        this.site = site;
        this.publicacao = publicacao;
    }

    /**
     * Conecta-se ao hosting do site.
     * @return Condicional se a conexão ftp ao hosting do site foi estabelacida.
     */
    public void conectar() throws PublicacaoException {

        try {
            // ftp.connect("trixo.com.br", 21);
            ftp.connect(site.getHost().getHost(), site.getHost().getPorta());

            // valida resposta do conect realizado
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {

                desconectar();

                log.error("Conexão negada no FTP " + hostPorta());

                throw new PublicacaoException(CausaErro.CONECTAR_HOST);
            }
        } catch (IOException e) {

            log.error("Ocorreu um erro ao conectar-se ao FTP " + hostPorta(), e);

            desconectar();

            throw new PublicacaoException(CausaErro.CONECTAR_HOST);
        }

        if (log.isDebugEnabled())
            log.debug("Conectado no FTP " + hostPorta());

        try {
            ftp.login(site.getHost().getUsuario(), site.getHost().getSenha());

            // valida resposta do login realizado
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {

                desconectar();

                log.error("Login não realizado no FTP " + hostPorta() + " com usuário " + "rcvconcursos");

                throw new PublicacaoException(CausaErro.LOGAR_HOST);
            }
        } catch (IOException e) {

            log.error("Ocorreu um erro ao logar-se ao FTP " + hostPorta() + " com usuário " + "rcvconcursos", e);

            desconectar();

            throw new PublicacaoException(CausaErro.LOGAR_HOST);
        }

        if (log.isDebugEnabled())
            log.debug("Logado no FTP " + hostPorta() + " com usuário " + "rcvconcursos");
    }

    /**
     * @return Diretório onde ficam as publicações do sistema.
     */
    private String diretorioPublicacoes() {
        return diretorioHost() + ".publicacoes/";
    }

    /**
     * @return Diretório da publicacao.
     */
    private String diretorioPublicacao() {
        return diretorioPublicacoes() + publicacao.getId() + "/";
    }

    /**
     * @return Diretório onde ficam as páginas temporárias do sistema.
     */
    public String diretorioPaginasTemporarias() {
        return diretorioPublicacao() + "paginas/";
    }

    /**
     * @return Diretório onde ficam as páginas publicadas do sistema.
     */
    public String diretorioHost() {
        return "/" + site.getHost().getDiretorio() + "/";
    }

    /**
     * Cria um arquivo em uma pasta contendo o log do que foi publicado.
     */
    public void verificaDiretorios() throws PublicacaoException {

        try {
            resolveDiretorios(diretorioPaginasTemporarias());

        } catch (IOException e) {
            log.error("Ocorreu um erro verificar diretórios do FTP " + hostPorta() + "com usuário " + "rcvconcursos", e);

            throw new PublicacaoException(CausaErro.CRIAR_PASTAS_PADROES);
        }

    }

    /**
     * Resolve os diretórios informados, percorrendo cada um deles e solicitando
     * a criação de cada um.
     * @param caminho
     *            Caminho dos diretórios a serem resolvidos
     * @throws IOException
     *             Exceção ao listar ou criar o diretório.
     */
    public void resolveDiretorios(String caminho) throws IOException {

        String[] diretorios = caminho.split("/");

        for (String dir : diretorios) {

            if (!"".equals(dir)) {

                if (!ftp.makeDirectory(dir)) {
                    log.warn("Ocorreu algum problema ao criar a pasta: " + dir + " no FTP" + hostPorta()
                            + ", resposta: " + ftp.getReplyString());
                }

                if (!ftp.changeWorkingDirectory(dir)) {
                    log.warn("Ocorreu algum problema entrar na pasta: " + dir + " no FTP" + hostPorta()
                            + ", resposta: " + ftp.getReplyString());
                }

            }
        }

        if (!ftp.changeWorkingDirectory("/")) {
            log.warn("Ocorreu algum problema entrar na pasta raiz no FTP" + hostPorta() + ", resposta: "
                    + ftp.getReplyString());
        }
    }

    /**
     * @param pagina
     *            Página a ser enviada
     * @param paginaHtml
     *            Conteúdo da página gerada em HTML
     * @throws PublicacaoException
     *             Exceção com a possível causa.
     */
    public void enviarPagina(Pagina pagina, StringWriter paginaHtml) throws PublicacaoException {

        String paginaTemporaria = diretorioPaginasTemporarias() + pagina.getNomeArquivo();

        try {

            ftp.enterLocalPassiveMode();

            ftp.setFileType(FTPClient.ASCII_FILE_TYPE);

            InputStream inPaginaTemporaria = null;

            try {

                inPaginaTemporaria = new ByteArrayInputStream(paginaHtml.getBuffer().toString().getBytes());

                boolean sucesso = ftp.storeFile(paginaTemporaria, inPaginaTemporaria);

                if (!sucesso) {
                    log.error("Ocorreu um erro ao enviar a página: " + paginaTemporaria + " ao FTP" + hostPorta()
                            + ", resposta: " + ftp.getReplyString());

                    throw new PublicacaoException(CausaErro.ENVIAR_PAGINA);
                }

            } finally {
                if (inPaginaTemporaria != null) {
                    inPaginaTemporaria.close();
                }
            }
        } catch (IOException e) {

            log.error("Ocorreu um erro ao enviar a página: " + paginaTemporaria + " ao FTP" + hostPorta(), e);

            throw new PublicacaoException(CausaErro.ENVIAR_PAGINA);
        }
    }

    public void logArquivoPublicacao(String mensagem) throws PublicacaoException {
        String nomeArquivoPublicacao = diretorioPublicacao() + "LOG";

        try {
            ftp.enterLocalPassiveMode();

            ftp.setFileType(FTPClient.ASCII_FILE_TYPE);

            InputStream inArquivoPublicacao = null;

            try {

                inArquivoPublicacao = new ByteArrayInputStream((mensagem + "\n").getBytes());

                if (!ftp.appendFile(nomeArquivoPublicacao, inArquivoPublicacao)) {
                    log.error("Não foi possível logar no arquivo de publicação: " + nomeArquivoPublicacao + " no FTP"
                            + hostPorta() + ", resposta: " + ftp.getReplyString());

                    throw new PublicacaoException(CausaErro.ESCREVER_LOG);
                }

            } finally {
                if (inArquivoPublicacao != null) {
                    inArquivoPublicacao.close();
                }
            }
        } catch (IOException e) {

            log.error("Ocorreu um erro ao enviar o arquivo de publicação: " + nomeArquivoPublicacao + " ao FTP"
                    + hostPorta(), e);

            throw new PublicacaoException(CausaErro.ESCREVER_LOG);
        }
    }

    /**
     * Move o arquivo da página para o local correto onde ela deve-se ficar.
     * @param pagina
     *            Página a ser publicada
     * @throws PublicacaoException
     *             Exceção com a causa do problema.
     */
    public void publicarPagina(Pagina pagina) throws PublicacaoException {

        String paginaTemporaria = diretorioPaginasTemporarias() + pagina.getNomeArquivo();
        String paginaPublicada = diretorioHost() + pagina.getNomeArquivo();

        try {
            ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
            ftp.rename(paginaTemporaria, paginaPublicada);
        } catch (IOException e) {

            log.error("Ocorreu um erro ao mover a página: " + paginaTemporaria + " para " + paginaPublicada + " no FTP"
                    + hostPorta(), e);

            throw new PublicacaoException(CausaErro.MOVER_PAGINA);
        }
    }

    // TODO comentar
    public void limparPastaTemporarias() throws PublicacaoException {

        try {
            if (!ftp.removeDirectory(diretorioPaginasTemporarias())) {
                log.error("Não foi possível excluir a pasta de páginas temporárias: " + diretorioPaginasTemporarias()
                        + " ao FTP" + hostPorta() + ", resposta: " + ftp.getReplyString());
            }
        } catch (IOException e) {

            log.error("Ocorreu um erro ao excluir a pasta de páginas temporárias: " + diretorioPaginasTemporarias()
                    + " no FTP" + hostPorta(), e);

            throw new PublicacaoException(CausaErro.EXCLUIR_PASTAS_TEMPORARIAS);
        }
    }

    /**
     * Desconecta do host do site.
     */
    public void desconectar() {

        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException f) {

                log.error("Ocorreu um erro ao desconectar-se do FTP " + hostPorta(), f);
            }
        }
    }

    /**
     * @return Host e porta formatados ;)
     */
    private String hostPorta() {
        return site.getHost().getHost() + ":" + site.getHost().getPorta();
    }
}
