/*
The MIT License (MIT)

Copyright (c) 2008 Kleber Maia de Andrade

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/   
package iobjects.util.mail;

import java.io.*;
import java.net.*;

import javax.activation.*;

/**
 * Implementa a interface DataSource para obter conteúdo a partir de uma URL
 * com a possibilidade de uso de uma sessão de usuário existente no web server.
 * @since 2006
 */
public class URLDataSourceEx implements DataSource {

  private URL           url           = null;
  private String        sessionID     = "";

  /**
   * Construtor padrão.
   * @param url URL contendo o caminho de origem para ser acessado.
   */
  public URLDataSourceEx(URL url) {
    // guarda a URL e a sessão
    this.url = url;
  }

  /**
   * Construtor estendido.
   * @param url URL contendo o caminho de origem para ser acessado.
   * @param sessionID String Identifação da sessão do usuário no servidor web.
   *                  Permite obter conteúdo associado exclusivamente ao usuário
   *                  que mantém a sessão informada.
   */
  public URLDataSourceEx(URL    url,
                         String sessionID) {
    // guarda a URL e a sessão
    this.url = url;
    this.sessionID = sessionID;
  }

  /**
   * Retorna o tipo do conteúdo de origem no formato 'type/subtype'.
   * @return String Retorna o tipo do conteúdo de origem no formato 'type/subtype'.
   */
  public String getContentType() {
    // conexão que será utilizada
    URLConnection urlConnection = null;
    try {
      // tenta abrir uma conexão
      urlConnection = openConnection();
    }
    catch (IOException e) {
    } // try-catch
    // retorno padrão
    String result = "application/octet-stream";
    // se conseguimos uma conexão...obtém o tipo do dado de origem
    if (urlConnection != null)
      result = urlConnection.getContentType();
    // retorna
    return result;
  }

  /**
   * Retorna o InputStream para leitura do dado de origem.
   * @return InputStream Retorna o InputStream para leitura do dado de origem.
   * @throws IOException Em caso de exceção na tentavida de se conectar com o
   *                     servidor.
   */
  public InputStream getInputStream() throws IOException {
    // tenta abrir uma conexão
    URLConnection urlConnection = openConnection();
    // retorna
    return urlConnection.getInputStream();
  }

  /**
   * Retorna o nome do conteúdo de origem.
   * @return String Retorna o nome do conteúdo de origem.
   */
  public String getName() {
    // retorna
    return url.getFile();
  }

  /**
   * Retorna o OutputStream para escrita na requisição do dado de origem.
   * @return OutputStream Retorna o OutputStream para escrita na requisição do
   *                      dado de origem.
   * @throws IOException Em caso de exceção na tentavida de se conectar com o
   *                     servidor.
   */
  public OutputStream getOutputStream() throws IOException {
    // tenta abrir uma conexão
    URLConnection urlConnection = openConnection();
    // deixa escrever
    urlConnection.setDoOutput(true);
    // retorna
    return urlConnection.getOutputStream();
  }

  /**
   * Retorna uma URLConnection para acesso ao conteúdo de origem apontado
   * pela URL passada no construtor.
   * @return URLConnection Retorna uma URLConnection para acesso ao conteúdo de
   *         origem apontado pela URL passada no construtor.
   * @throws IOException Em caso de exceção na tentavida de se conectar com o
   *                     servidor.
   */
  private URLConnection openConnection() throws IOException {
    // abre a conexão
    URLConnection result = url.openConnection();
    // adiciona a informação da sessão
    if ((sessionID != null) && (!sessionID.equals("")))
      result.addRequestProperty("Cookie", "JSESSIONID=" + sessionID + ";");
    // retorna
    return result;
  }

}
