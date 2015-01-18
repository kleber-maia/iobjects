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
 * Utilitário para comunicação com o servidor remoto da aplicação. Tem a
 * capacidade de executar métodos remotos no servidor com transparência de
 * localização e protocolo.
 * <p>
 * Esta classe deve ser utilizada como meio de comunicação entre aplicações
 * clientes que necessitem acessar objetos de negócio disponíveis através
 * de uma aplicação web.
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
   * Executa o método 'methodName' remoto, no servidor, na classe identificada
   * por 'className', passando os parâmetros 'parameterValues' e retorna um
   * Object representando a resposta do método.
   * @param className int ID da classe remota.
   * @param methodName String Nome do método remoto.
   * @param parameterValues Object[] Valores dos parâmetros do método.
   * @return Object Executa o método 'methodName' remoto, no servidor, na classe
   *                identificada por 'className', passando os parâmetros
   *                'parameterValues' e retorna um Object representando a
   *                resposta do método.
   * @throws IOException Em caso de exceção na tentativa de se conectar ao
   *                     servidor, enviar ou receber dados.
   * @throws RemoteException Em caso de exceção no servidor ao realizar a
   *                         operação desejada.
   */
  static public synchronized Object callRemoteMethod(String   className,
                                                     String   methodName,
                                                     Object[] parameterValues) throws IOException, RemoteException {
    // URL do servidor
    URL url = new URL(serverURL + "remoteserver");
    // conexão
    HttpURLConnection connection = null;
    try {
      // estrutura do método remoto
      RemoteMethod remoteMethod = new RemoteMethod(workConfiguration,
                                                   defaultConnectionName,
                                                   username,
                                                   password,
                                                   masterRelationValues,
                                                   masterRelationUserValues,
                                                   className,
                                                   methodName,
                                                   parameterValues);
      // serializa o método remoto
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      Serializer.serialize(remoteMethod, outStream);
      // usa nosso gerenciador de cookies
      CookieHandler.setDefault(cookieManager);
      // cria a conexão com o servidor
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
      // obtém o código da resposta
      int responseCode = connection.getResponseCode();
      // se não recebemos uma resposta OK...avisa
      if (responseCode != HttpURLConnection.HTTP_ACCEPTED)
        throw new Exception(responseCode + " - " + connection.getResponseMessage());
      // obtém nossa resposta em forma de objeto
      Object result = Serializer.deserialize(connection.getInputStream());
      // se é uma exceção...lança
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
      // tenta liberar memória
      System.gc();
    } // try-finally
  }

  /**
   * Retorna o nome da conexão padrão.
   * @return String Retorna o nome da conexão padrão.
   */
  static public synchronized String getDefaultConnectionName() {
    return defaultConnectionName;
  }

  /**
   * Retorna os valores que combinam com 'returnUserFieldNames' na configuração
   * da relação mestre.
   * @return String[] Retorna os valores que combinam com 'returnUserFieldNames'
   *                  na configuração da relação mestre.
   */
  static public synchronized String[] getMasterRelationUserValues() {
    return masterRelationUserValues;
  }

  /**
   * Retorna os valores que combinam com 'returnFieldNames' na configuração
   * da relação mestre.
   * @return String[] Retorna os valores que combinam com returnFieldNames' na
   *                  configuração da relação mestre.
   */
  static public synchronized String[] getMasterRelationValues() {
    return masterRelationValues;
  }

  /**
   * Retorna a senha do usuário para autenticação.
   * @return String Retorna a senha do usuário para autenticação.
   */
  static public synchronized String getPassword() {
    return password;
  }

  /**
   * Retorna o endereço remoto da aplicação servidora.
   * @return String Retorna o endereço remoto da aplicação servidora.
   */
  static public synchronized String getServerURL() {
    return serverURL;
  }

  /**
   * Retorna o nome do usuário para autenticação.
   * @return String Retorna o nome do usuário para autenticação.
   */
  static public synchronized String getUsername() {
    return username;
  }

  /**
   * Retorna o nome da configuração de trabalho da instância do servidor web.
   * @return String Retorna o nome da configuração de trabalho da instância do
   *         servidor web.
   */
  static public synchronized String getWorkConfiguration() {
    return workConfiguration;
  }

  /**
   * Realiza uma operação de ping no servidor remoto e retorna true em caso
   * de sucesso.
   * @return boolean Realiza uma operação de ping no servidor remoto e retorna
   *                 true em caso de sucesso.
   * @throws IOException Em caso de exceção na tentativa de se conectar ao
   *                     servidor, enviar ou receber dados.
   * @throws RemoteException Em caso de exceção no servidor ao realizar a
   *                         operação desejada.
   */
  static public synchronized void logoff() throws IOException, RemoteException {
    // chama o método remoto
    callRemoteMethod(iobjects.servlet.RemoteServer.CLASS_NAME,
                     iobjects.servlet.RemoteServer.METHOD_LOGOFF,
                     new Object[]{});
  }

  /**
   * Realiza uma operação de ping no servidor remoto e retorna true em caso
   * de sucesso.
   * @return boolean Realiza uma operação de ping no servidor remoto e retorna
   *                 true em caso de sucesso.
   * @throws IOException Em caso de exceção na tentativa de se conectar ao
   *                     servidor, enviar ou receber dados.
   * @throws RemoteException Em caso de exceção no servidor ao realizar a
   *                         operação desejada.
   */
  static public synchronized boolean ping() throws IOException, RemoteException {
    // gerador de números aleatórios
    Random random = new Random();
    // nossa chave do ping
    int key = random.nextInt();
    // chama o método remoto
    Integer result = (Integer)callRemoteMethod(iobjects.servlet.RemoteServer.CLASS_NAME,
                                               iobjects.servlet.RemoteServer.METHOD_PING,
                                               new Object[]{new Integer(key)});
    // retorna
    return result.intValue() == key;
  }
  
  /**
   * Define o nome da conexão padrão
   * @param defaultConnectionName String Nome da conexão padrão
   */
  static public synchronized void setDefaultConnectionName(String defaultConnectionName) {
    RemoteServer.defaultConnectionName = defaultConnectionName;
  }

  /**
   * Define os valores que devem combinar com 'returnUserFieldNames' na
   * configuração da relação mestre.
   * @param masterRelationUserValues String[] Valores que devem combinar com
   *                                'returnUserFieldNames' na configuração da
   *                                 relação mestre.
   */
  static public synchronized void setMasterRelationUserValues(String[] masterRelationUserValues) {
    RemoteServer.masterRelationUserValues = masterRelationUserValues;
  }

  /**
   * Define os valores que devem combinar com 'returnFieldNames' na configuração
   * da relação mestre.
   * @param masterRelationValues String[] Valores que devem combinar com
   *                             'returnFieldNames' na configuração da relação
   *                             mestre.
   */
  static public synchronized void setMasterRelationValues(String[] masterRelationValues) {
    RemoteServer.masterRelationValues = masterRelationValues;
  }

  /**
   * Define a senha do usuário para autenticação.
   * @param password String Senha do usuário para autenticação.
   */
  static public synchronized void setPassword(String password) {
    RemoteServer.password = password;
  }

  /**
   * Define o endereço remoto da aplicação servidora.
   * @param serverURL String Endereço remoto da aplicação servidora.
   */
  static public synchronized void setServerURL(String serverURL) {
    RemoteServer.serverURL = serverURL + (serverURL.endsWith("/") ? "" : "/");
  }

  /**
   * Define o nome do usuário para autenticação.
   * @param username String Nome do usuário para autenticação.
   */
  static public synchronized void setUsername(String username) {
    RemoteServer.username = username;
  }

  /**
   * Define o nome da configuração de trabalho da instância do servidor web.
   * @param workConfiguration String nome da configuração de trabalho da
   *                          instância do servidor web.
   */
  static public synchronized void setWorkConfiguration(String workConfiguration) {
    RemoteServer.workConfiguration = workConfiguration;
  }

}
