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

import java.util.*;
import java.net.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Representa uma mensagem recebida ou que será enviada no formato HTML.
 * Possui a capacidade de inserir automaticamente as referências externas do
 * conteúdo HTML na mensagem.
 * @since 2006
 */
public class HTMLMessage extends Message {

  private String         fileName                  = "";
  private MimeBodyPart   htmlContent               = null;
  private MimeBodyPart[] htmlReferencedContentList = null;
  private String         sessionID                 = "";
  private URL            url                       = null;

  /**
   * Construtor estendido.
   * @param from String Endereço do remetente.
   * @param to String Endereço do destinatário.
   * @param subject String Assunto.
   * @param fileName String Nome do arquivo HTML.
   */
  public HTMLMessage(String from,
                     String to,
                     String subject,
                     String fileName) {
    super(from, to, subject, "");
    // nossos dados
    this.fileName = fileName;
    try {
      // guarda o conteúdo HTML recebido
      htmlContent = loadBodyPart(fileName);
      // carrega todo conteúdo que é feito referência
      loadHTMLReferencedContentList();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Construtor estendido.
   * @param from String Endereço do remetente.
   * @param to String Endereço do destinatário.
   * @param subject String Assunto.
   * @param url URL do arquivo HTML.
   * @param sessionID String Identifação da sessão do usuário no servidor web.
   *                  Permite obter conteúdo associado exclusivamente ao usuário
   *                  que mantém a sessão informada.
   */
  public HTMLMessage(String from,
                     String to,
                     String subject,
                     URL    url,
                     String sessionID) {
    super(from, to, subject, "");
    // nossos dados
    this.url = url;
    this.sessionID = sessionID;
    try {
      // guarda o conteúdo HTML recebido
      htmlContent = loadBodyPart(url, sessionID);
      // carrega todo conteúdo que é feito referência
      loadHTMLReferencedContentList();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Construtor estendido.
   * @param from String[] Endereços dos remetentes.
   * @param replyTo String[] Endereços de resposta.
   * @param to String[] Endereços dos destinatários.
   * @param cc String[] Endereços das cópias carbono.
   * @param bcc String[] Endereços das cópias carbono ocultas.
   * @param subject String Assunto.
   * @param fileName String Nome do arquivo HTML.
   */
  public HTMLMessage(String[] from,
                     String[] replyTo,
                     String[] to,
                     String[] cc,
                     String[] bcc,
                     String   subject,
                     String   fileName) {
    super(from, replyTo, to, cc, bcc, subject, "");
    // nossos dados
    this.fileName = fileName;
    try {
      // guarda o conteúdo HTML recebido
      htmlContent = loadBodyPart(fileName);
      // carrega todo conteúdo que é feito referência
      loadHTMLReferencedContentList();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Construtor estendido.
   * @param from String[] Endereços dos remetentes.
   * @param replyTo String[] Endereços de resposta.
   * @param to String[] Endereços dos destinatários.
   * @param cc String[] Endereços das cópias carbono.
   * @param bcc String[] Endereços das cópias carbono ocultas.
   * @param subject String Assunto.
   * @param url URL do arquivo HTML.
   * @param sessionID String Identifação da sessão do usuário no servidor web.
   *                  Permite obter conteúdo associado exclusivamente ao usuário
   *                  que mantém a sessão informada.
   */
  public HTMLMessage(String[] from,
                     String[] replyTo,
                     String[] to,
                     String[] cc,
                     String[] bcc,
                     String   subject,
                     URL      url,
                     String   sessionID) {
    super(from, replyTo, to, cc, bcc, subject, "");
    // nossos dados
    this.url       = url;
    this.sessionID = sessionID;
    try {
      // guarda o conteúdo HTML recebido
      htmlContent = loadBodyPart(url, sessionID);
      // carrega todo conteúdo que é feito referência
      loadHTMLReferencedContentList();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna o conteúdo da mensagem HTML.
   * @return MimeBodyPart Retorna o conteúdo da mensagem HTML.
   */
  public MimeBodyPart getHTMLContent() {
   return htmlContent;
  }

  /**
   * Retorna o conteúdo HTML da mensagem como String.
   * @return String Retorna o conteúdo HTML da mensagem como String.
   * @throws Exception Em caso de exceção na tentativa de acesso ao conteúdo de
   *                   origem.
   */
  public String getHTMLContentAsString() throws Exception {
    return htmlContent.getContent().toString();
  }

  /**
   * Retorna o conteúdo referenciado pelo HTML que foi automaticamente embutido
   * na mensagem.
   * @return MimeBodyPart[] Retorna o conteúdo referenciado pelo HTML que foi
   *                        automaticamente embutido na mensagem.
   */
  public MimeBodyPart[] getHTMLReferencedContentList() {
    return htmlReferencedContentList;
  }

  /**
   * Retorna um 'MimeBodyPart' com conteúdo para ser adicionado à mensagem.
   * @param fileName String Arquivo de origem.
   * @return MimeBodyPart Retorna um 'MimeBodyPart' com conteúdo para ser
   *                      adicionado à mensagem.
   * @throws Exception Em caso de exceção na tentativa de acesso ao arquivo
   *                   informado.
   */
  private MimeBodyPart loadBodyPart(String fileName) throws Exception {
   // cria o BodyPart
   MimeBodyPart bodyPart = new MimeBodyPart();
   bodyPart.setDataHandler(new DataHandler(new FileDataSource(fileName)));
   // retorna
   return bodyPart;
  }

  /**
   * Retorna um 'MimeBodyPart' com conteúdo para ser adicionado à mensagem.
   * @param url URL do conteúdo de origem.
   * @param sessionID String Identifação da sessão do usuário no servidor web.
   *                  Permite obter conteúdo associado exclusivamente ao usuário
   *                  que mantém a sessão informada.
   * @return MimeBodyPart Retorna um 'MimeBodyPart' com conteúdo para ser
   *                      adicionado à mensagem.
   * @throws Exception Em caso de exceção na tentativa de acesso à URL informada.
   */
  private MimeBodyPart loadBodyPart(URL    url,
                                    String sessionID) throws Exception {
    // cria o BodyPart
    MimeBodyPart bodyPart = new MimeBodyPart();
    bodyPart.setDataHandler(new DataHandler(new URLDataSourceEx(url, sessionID)));
    // retorna
    return bodyPart;
  }

  /**
   * Carrega todo o conteúdo referenciado pelo HTML da mensagem, que
   * pode ser localizado, e o torna parte integrante da mensagem.
   * @throws Exception Em caso de exceção na tentativa de localizar e obter
   *                   o conteúdo que é feito referência.
   */
  private void loadHTMLReferencedContentList() throws Exception {
    // constrói o caminho relativo do conteúdo referenciado no HTML
    String relativePath = fileName.replace('\\', '/');
    if (!relativePath.equals(""))
      relativePath = relativePath.substring(0, relativePath.lastIndexOf('/')+1);
    // *
    String tempURL = (url != null ? url.toString() : "");
    if (!tempURL.equals(""))
      tempURL = tempURL.substring(0, tempURL.lastIndexOf('/')+1);
    URL relativeURL = (!tempURL.equals("") ? new URL(tempURL) : null);

    // obtém o conteúdo HTML
    StringBuffer html = new StringBuffer(getHTMLContentAsString());

    // lista de referências obtidas
    Vector referenceNameList = new Vector();
    Vector referenceBodyPartList = new Vector();
    // localiza todas as referências externas
    for (int pos=0; pos<html.length()-3; pos++) {
      // se encontramos uma referência HREF...
      if (html.substring(pos, pos+4).toLowerCase().equals("href")) {
        // localiza a próxima abertura de " ou '
        int startQuote = html.indexOf("\"", pos);
        int startQuote2 = html.indexOf("'", pos);
        // vamos usar o que acharmos primeiro
        boolean usingQuote2 = (startQuote2 > 0) && (startQuote2 < startQuote);
        // vamos usar o que acharmos primeiro
        if (usingQuote2)
          startQuote = startQuote2;
        // localiza o próximo fechamento de " ou '
        int endQuote = (!usingQuote2 ? html.indexOf("\"", startQuote+1) : html.indexOf("'", startQuote+1));
        // obtém o nome da referência externa
        String referenceName = html.substring(startQuote+1, endQuote);
        // se é uma referência absoluta...continua
        if (referenceName.indexOf("://") > 0)
          continue;
        try {
          // tenta carregar
          if (relativeURL != null)
            loadBodyPart(new URL(relativeURL.toString() + referenceName), sessionID);
          else
            loadBodyPart(relativePath + referenceName);
          // adiciona o caminho relativo ao início da referência
          html.insert(startQuote+1, (relativeURL != null ? relativeURL.toString() : relativePath));
        }
        catch (Exception e) {
          // se não carregamos a referência...não torna uma referência interna
          continue;
        } // try-catch
      }
      // se encontramos uma referência SRC...
      else if (html.substring(pos, pos+3).toLowerCase().equals("src")) {
        // localiza a próxima abertura de " ou '
        int startQuote = html.indexOf("\"", pos);
        int startQuote2 = html.indexOf("'", pos);
        // vamos usar o que acharmos primeiro
        boolean usingQuote2 = (startQuote2 > 0) && (startQuote2 < startQuote);
        // vamos usar o que acharmos primeiro
        if (usingQuote2)
          startQuote = startQuote2;
        // localiza o próximo fechamento de " ou '
        int endQuote = (!usingQuote2 ? html.indexOf("\"", startQuote+1) : html.indexOf("'", startQuote+1));
        // se não temos início e abertura válidos...continua
        if ((startQuote <= 0) || (endQuote <= 0) || (startQuote >= endQuote))
          continue;
        // obtém o nome da referência externa
        String referenceName = html.substring(startQuote+1, endQuote);
        // se é uma referência absoluta...continua
        if (referenceName.indexOf("://") > 0)
          continue;
        // verifica se já temos essa referência
        MimeBodyPart referenceBodyPart = null;
        int          referenceIndex    = referenceNameList.indexOf(referenceName);
        // se ainda não temos...tenta carregar
        if (referenceIndex < 0) {
          try {
            // tenta carregar
            if (relativeURL != null)
              referenceBodyPart = loadBodyPart(new URL(relativeURL.toString() + referenceName), sessionID);
            else
              referenceBodyPart = loadBodyPart(relativePath + referenceName);
            // adiciona o nome a referência nas listas
            referenceNameList.add(referenceName);
            referenceBodyPartList.add(referenceBodyPart);
            // obtém seu índice
            referenceIndex = referenceNameList.size()-1;
            // define o ID e que será exibido no corpo da mensagem e não como anexo
            referenceBodyPart.setContentID("<part." + referenceIndex + "@iobjects>");
            referenceBodyPart.setDisposition(Part.INLINE);
          }
          catch (Exception e) {
            // se não carregamos a referência...não torna uma referência interna
            continue;
          } // try-catch
        } // if
        // troca a referência externa para uma interna
        html.replace(startQuote+1, endQuote, "cid:part." + referenceIndex + "@iobjects");
      } // if
    } // while

    // guarda as referências externas
    htmlReferencedContentList = new MimeBodyPart[referenceBodyPartList.size()];
    referenceBodyPartList.copyInto(htmlReferencedContentList);
    // guarda o novo HTML
    if (referenceNameList.size() > 0) {
      htmlContent.setContent(html.toString(), htmlContent.getDataHandler().getContentType());
    } // if
  }

}
