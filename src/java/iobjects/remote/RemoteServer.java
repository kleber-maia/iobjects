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
package iobjects.remote;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Utilit�rio para comunica��o com o servidor remoto da aplica��o. Tem a
 * capacidade de executar m�todos remotos no servidor com transpar�ncia de
 * localiza��o e protocolo.
 * <p>
 * Esta classe deve ser utilizada como meio de comunica��o entre aplica��es
 * clientes que necessitem acessar objetos de neg�cio dispon�veis atrav�s
 * de uma aplica��o web.
 * </p>
 */
public class RemoteServer { 

  static public void main(String[] args) {
    try {
      RemoteServer.setServerURL("http://imanager.imanager.com.br");
      RemoteServer.setDefaultConnectionName("Suporte");
      RemoteServer.setUsername("@iManager");
      RemoteServer.setPassword("iman456526");
      Object[] result = (Object[])RemoteServer.callRemoteMethod("imanager.entity.Empresa", "selectByFilter", new Object[]{"%", "", 0});
      System.out.println(result.length);
    }
    catch (Exception e) {
      e.printStackTrace();
    } // try-catch
  }
  
  static private String   defaultConnectionName    = "";
  static private String[] masterRelationValues     = {};
  static private String[] masterRelationUserValues = {};
  static private String   password                 = "";
  static private String   serverURL                = "";
  static private String   username                 = "";
  static private String   workConfiguration        = "";

  static private CookieManager cookieManager = new CookieManager();
  
  private RemoteServer() {
  }

  /**
   * Executa o m�todo 'methodName' remoto, no servidor, na classe identificada
   * por 'className', passando os par�metros 'parameterValues' e retorna um
   * Object representando a resposta do m�todo.
   * @param className int ID da classe remota.
   * @param methodName String Nome do m�todo remoto.
   * @param parameterValues Object[] Valores dos par�metros do m�todo.
   * @return Object Executa o m�todo 'methodName' remoto, no servidor, na classe
   *                identificada por 'className', passando os par�metros
   *                'parameterValues' e retorna um Object representando a
   *                resposta do m�todo.
   * @throws IOException Em caso de exce��o na tentativa de se conectar ao
   *                     servidor, enviar ou receber dados.
   * @throws RemoteException Em caso de exce��o no servidor ao realizar a
   *                         opera��o desejada.
   */
  static public synchronized Object callRemoteMethod(String   className,
                                                     String   methodName,
                                                     Object[] parameterValues) throws IOException, RemoteException {
    // URL do servidor
    URL url = new URL(serverURL + "remoteserver");
    // conex�o
    HttpURLConnection connection = null;
    try {
      // estrutura do m�todo remoto
      RemoteMethod remoteMethod = new RemoteMethod(workConfiguration,
                                                   defaultConnectionName,
                                                   username,
                                                   password,
                                                   masterRelationValues,
                                                   masterRelationUserValues,
                                                   className,
                                                   methodName,
                                                   parameterValues);
      // serializa o m�todo remoto
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      Serializer.serialize(remoteMethod, outStream);
      // usa nosso gerenciador de cookies
      CookieHandler.setDefault(cookieManager);
      // cria a conex�o com o servidor
      connection = (HttpURLConnection)url.openConnection();
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setUseCaches(false);
      connection.setRequestMethod("POST");
      connection.setFixedLengthStreamingMode(outStream.size());
      // conecta
      connection.connect();
      // envia os dados
      connection.getOutputStream().write(outStream.toByteArray());
      // obt�m o c�digo da resposta
      int responseCode = connection.getResponseCode();
      // se n�o recebemos uma resposta OK...avisa
      if (responseCode != HttpURLConnection.HTTP_ACCEPTED)
        throw new Exception(responseCode + " - " + connection.getResponseMessage());
      // obt�m nossa resposta em forma de objeto
      Object result = Serializer.deserialize(connection.getInputStream());
      // se � uma exce��o...lan�a
      if (result instanceof RemoteException)
        throw new Exception(((RemoteException)result).getCauseClassName() + ": " + ((RemoteException)result).getCauseMessage());
      // retorna
      return result;
    }
    catch (RemoteException e) {
      throw e;
    }
    catch (Exception e) {
      throw new RemoteException(e);
    } // try-catch
    finally {
      // libera recursos
      if (connection != null)
        connection.disconnect();
      // tenta liberar mem�ria
      System.gc();
    } // try-finally
  }

  /**
   * Retorna o nome da conex�o padr�o.
   * @return String Retorna o nome da conex�o padr�o.
   */
  static public synchronized String getDefaultConnectionName() {
    return defaultConnectionName;
  }

  /**
   * Retorna os valores que combinam com 'returnUserFieldNames' na configura��o
   * da rela��o mestre.
   * @return String[] Retorna os valores que combinam com 'returnUserFieldNames'
   *                  na configura��o da rela��o mestre.
   */
  static public synchronized String[] getMasterRelationUserValues() {
    return masterRelationUserValues;
  }

  /**
   * Retorna os valores que combinam com 'returnFieldNames' na configura��o
   * da rela��o mestre.
   * @return String[] Retorna os valores que combinam com returnFieldNames' na
   *                  configura��o da rela��o mestre.
   */
  static public synchronized String[] getMasterRelationValues() {
    return masterRelationValues;
  }

  /**
   * Retorna a senha do usu�rio para autentica��o.
   * @return String Retorna a senha do usu�rio para autentica��o.
   */
  static public synchronized String getPassword() {
    return password;
  }

  /**
   * Retorna o endere�o remoto da aplica��o servidora.
   * @return String Retorna o endere�o remoto da aplica��o servidora.
   */
  static public synchronized String getServerURL() {
    return serverURL;
  }

  /**
   * Retorna o nome do usu�rio para autentica��o.
   * @return String Retorna o nome do usu�rio para autentica��o.
   */
  static public synchronized String getUsername() {
    return username;
  }

  /**
   * Retorna o nome da configura��o de trabalho da inst�ncia do servidor web.
   * @return String Retorna o nome da configura��o de trabalho da inst�ncia do
   *         servidor web.
   */
  static public synchronized String getWorkConfiguration() {
    return workConfiguration;
  }

  /**
   * Realiza uma opera��o de ping no servidor remoto e retorna true em caso
   * de sucesso.
   * @return boolean Realiza uma opera��o de ping no servidor remoto e retorna
   *                 true em caso de sucesso.
   * @throws IOException Em caso de exce��o na tentativa de se conectar ao
   *                     servidor, enviar ou receber dados.
   * @throws RemoteException Em caso de exce��o no servidor ao realizar a
   *                         opera��o desejada.
   */
  static public synchronized void logoff() throws IOException, RemoteException {
    // chama o m�todo remoto
    callRemoteMethod(iobjects.servlet.RemoteServer.CLASS_NAME,
                     iobjects.servlet.RemoteServer.METHOD_LOGOFF,
                     new Object[]{});
  }

  /**
   * Realiza uma opera��o de ping no servidor remoto e retorna true em caso
   * de sucesso.
   * @return boolean Realiza uma opera��o de ping no servidor remoto e retorna
   *                 true em caso de sucesso.
   * @throws IOException Em caso de exce��o na tentativa de se conectar ao
   *                     servidor, enviar ou receber dados.
   * @throws RemoteException Em caso de exce��o no servidor ao realizar a
   *                         opera��o desejada.
   */
  static public synchronized boolean ping() throws IOException, RemoteException {
    // gerador de n�meros aleat�rios
    Random random = new Random();
    // nossa chave do ping
    int key = random.nextInt();
    // chama o m�todo remoto
    Integer result = (Integer)callRemoteMethod(iobjects.servlet.RemoteServer.CLASS_NAME,
                                               iobjects.servlet.RemoteServer.METHOD_PING,
                                               new Object[]{new Integer(key)});
    // retorna
    return result.intValue() == key;
  }
  
  /**
   * Define o nome da conex�o padr�o
   * @param defaultConnectionName String Nome da conex�o padr�o
   */
  static public synchronized void setDefaultConnectionName(String defaultConnectionName) {
    RemoteServer.defaultConnectionName = defaultConnectionName;
  }

  /**
   * Define os valores que devem combinar com 'returnUserFieldNames' na
   * configura��o da rela��o mestre.
   * @param masterRelationUserValues String[] Valores que devem combinar com
   *                                'returnUserFieldNames' na configura��o da
   *                                 rela��o mestre.
   */
  static public synchronized void setMasterRelationUserValues(String[] masterRelationUserValues) {
    RemoteServer.masterRelationUserValues = masterRelationUserValues;
  }

  /**
   * Define os valores que devem combinar com 'returnFieldNames' na configura��o
   * da rela��o mestre.
   * @param masterRelationValues String[] Valores que devem combinar com
   *                             'returnFieldNames' na configura��o da rela��o
   *                             mestre.
   */
  static public synchronized void setMasterRelationValues(String[] masterRelationValues) {
    RemoteServer.masterRelationValues = masterRelationValues;
  }

  /**
   * Define a senha do usu�rio para autentica��o.
   * @param password String Senha do usu�rio para autentica��o.
   */
  static public synchronized void setPassword(String password) {
    RemoteServer.password = password;
  }

  /**
   * Define o endere�o remoto da aplica��o servidora.
   * @param serverURL String Endere�o remoto da aplica��o servidora.
   */
  static public synchronized void setServerURL(String serverURL) {
    RemoteServer.serverURL = serverURL + (serverURL.endsWith("/") ? "" : "/");
  }

  /**
   * Define o nome do usu�rio para autentica��o.
   * @param username String Nome do usu�rio para autentica��o.
   */
  static public synchronized void setUsername(String username) {
    RemoteServer.username = username;
  }

  /**
   * Define o nome da configura��o de trabalho da inst�ncia do servidor web.
   * @param workConfiguration String nome da configura��o de trabalho da
   *                          inst�ncia do servidor web.
   */
  static public synchronized void setWorkConfiguration(String workConfiguration) {
    RemoteServer.workConfiguration = workConfiguration;
  }

}
