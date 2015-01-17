package iobjects.util.mail;

import java.io.*;
import java.net.*;

import javax.activation.*;

/**
 * Implementa a interface DataSource para obter conte�do a partir de uma URL
 * com a possibilidade de uso de uma sess�o de usu�rio existente no web server.
 * @since 2006
 */
public class URLDataSourceEx implements DataSource {

  private URL           url           = null;
  private String        sessionID     = "";

  /**
   * Construtor padr�o.
   * @param url URL contendo o caminho de origem para ser acessado.
   */
  public URLDataSourceEx(URL url) {
    // guarda a URL e a sess�o
    this.url = url;
  }

  /**
   * Construtor estendido.
   * @param url URL contendo o caminho de origem para ser acessado.
   * @param sessionID String Identifa��o da sess�o do usu�rio no servidor web.
   *                  Permite obter conte�do associado exclusivamente ao usu�rio
   *                  que mant�m a sess�o informada.
   */
  public URLDataSourceEx(URL    url,
                         String sessionID) {
    // guarda a URL e a sess�o
    this.url = url;
    this.sessionID = sessionID;
  }

  /**
   * Retorna o tipo do conte�do de origem no formato 'type/subtype'.
   * @return String Retorna o tipo do conte�do de origem no formato 'type/subtype'.
   */
  public String getContentType() {
    // conex�o que ser� utilizada
    URLConnection urlConnection = null;
    try {
      // tenta abrir uma conex�o
      urlConnection = openConnection();
    }
    catch (IOException e) {
    } // try-catch
    // retorno padr�o
    String result = "application/octet-stream";
    // se conseguimos uma conex�o...obt�m o tipo do dado de origem
    if (urlConnection != null)
      result = urlConnection.getContentType();
    // retorna
    return result;
  }

  /**
   * Retorna o InputStream para leitura do dado de origem.
   * @return InputStream Retorna o InputStream para leitura do dado de origem.
   * @throws IOException Em caso de exce��o na tentavida de se conectar com o
   *                     servidor.
   */
  public InputStream getInputStream() throws IOException {
    // tenta abrir uma conex�o
    URLConnection urlConnection = openConnection();
    // retorna
    return urlConnection.getInputStream();
  }

  /**
   * Retorna o nome do conte�do de origem.
   * @return String Retorna o nome do conte�do de origem.
   */
  public String getName() {
    // retorna
    return url.getFile();
  }

  /**
   * Retorna o OutputStream para escrita na requisi��o do dado de origem.
   * @return OutputStream Retorna o OutputStream para escrita na requisi��o do
   *                      dado de origem.
   * @throws IOException Em caso de exce��o na tentavida de se conectar com o
   *                     servidor.
   */
  public OutputStream getOutputStream() throws IOException {
    // tenta abrir uma conex�o
    URLConnection urlConnection = openConnection();
    // deixa escrever
    urlConnection.setDoOutput(true);
    // retorna
    return urlConnection.getOutputStream();
  }

  /**
   * Retorna uma URLConnection para acesso ao conte�do de origem apontado
   * pela URL passada no construtor.
   * @return URLConnection Retorna uma URLConnection para acesso ao conte�do de
   *         origem apontado pela URL passada no construtor.
   * @throws IOException Em caso de exce��o na tentavida de se conectar com o
   *                     servidor.
   */
  private URLConnection openConnection() throws IOException {
    // abre a conex�o
    URLConnection result = url.openConnection();
    // adiciona a informa��o da sess�o
    if ((sessionID != null) && (!sessionID.equals("")))
      result.addRequestProperty("Cookie", "JSESSIONID=" + sessionID + ";");
    // retorna
    return result;
  }

}
