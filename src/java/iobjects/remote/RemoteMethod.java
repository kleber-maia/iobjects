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

import iobjects.util.*;

/**
 * Representa a estrutura de chamada de um método remoto no servidor.
 */
public class RemoteMethod implements Serializable {

  private String   className                = "";
  private String   defaultConnectionName    = "";
  private String[] masterRelationValues     = {};
  private String[] masterRelationUserValues = {};
  private String   methodName               = "";
  private Object[] parameterValues          = {};
  private String   password                 = "";
  private String   username                 = "";
  private String   workConfiguration        = "";

  /**
   * Construtor padrão.
   */
  public RemoteMethod() {
  }

  /**
   * Construtor estendido.
   * @param workConfiguration Nome da configuração de trabalho da instância do servidor web.
   * @param defaultConnectionName Nome da conexão padrão.
   * @param username Nome do usuário para autenticação.
   * @param password Senha do usuário para autenticação.
   * @param masterRelationValues Valores que devem combinar com 'returnFieldNames'
   *                             na configuração da relação mestre.
   * @param masterRelationUserValues Valores que devem combinar com 'returnUserFieldNames'
   *                                 na configuração da relação mestre.
   * @param className String Nome da classe remota.
   * @param methodName String Nome do método da classe remota.
   * @param parameterValues Object[] Valores dos parâmetros do método remoto.
   */
  public RemoteMethod(String   workConfiguration,
                      String   defaultConnectionName,
                      String   username,
                      String   password,
                      String[] masterRelationValues,
                      String[] masterRelationUserValues,
                      String   className,
                      String   methodName,
                      Object[] parameterValues) {
    this.workConfiguration = workConfiguration;
    this.defaultConnectionName = defaultConnectionName;
    this.username = username;
    this.password = password;
    this.masterRelationValues = masterRelationValues;
    this.masterRelationUserValues = masterRelationUserValues;
    this.className = className;
    this.methodName = methodName;
    this.parameterValues = parameterValues;
  }

  public void deserialize(SerializeReader reader) throws IOException {
    workConfiguration = reader.readString();
    defaultConnectionName = reader.readString();
    username = reader.readString();
    password = reader.readString();
    // *
    String temp1 = reader.readString();
    if (temp1.length() > 0)
      masterRelationValues = temp1.split(";");
    String temp2 = reader.readString();
    if (temp2.length() > 0)
      masterRelationUserValues = temp2.split(";");
    // *
    className = reader.readString();
    methodName = reader.readString();
    int parameterCount = reader.readInt();
    parameterValues = new Object[parameterCount];
    for (int i=0; i<parameterCount; i++) {
      parameterValues[i] = reader.readObject();
    } // for
  }

  /**
   * Retorna o nome da classe remota.
   * @return String Retorna o nome da classe remota.
   */
  public String getClassName() {
    return className;
  }

  /**
   * Retorna o nome da conexão padrão.
   * @return String Retorna o nome da conexão padrão.
   */
  public String getDefaultConnectionName() {
    return defaultConnectionName;
  }

  /**
   * Retorna os valores que combinam com 'returnUserFieldNames' na configuração
   * da relação mestre.
   * @return String[] Retorna os valores que combinam com 'returnUserFieldNames'
   *                  na configuração da relação mestre.
   */
  public String[] getMasterRelationUserValues() {
    return masterRelationUserValues;
  }

  /**
   * Retorna os valores que combinam com 'returnFieldNames' na configuração
   * da relação mestre.
   * @return String[] Retorna os valores que combinam com returnFieldNames' na
   *                  configuração da relação mestre.
   */
  public String[] getMasterRelationValues() {
    return masterRelationValues;
  }

  /**
   * Retorna o nome do método da classe remota.
   * @return String Retorna o nome do método da classe remota.
   */
  public String getMethodName() {
    return methodName;
  }

  /**
   * Retorna os valores dos parâmetros do método remoto
   * @return Object[] Retorna os valores dos parâmetros do método remoto
   */
  public Object[] getParameterValues() {
    return parameterValues;
  }

  /**
   * Retorna a senha do usuário para autenticação.
   * @return String Retorna a senha do usuário para autenticação.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Retorna o nome do usuário para autenticação.
   * @return String Retorna o nome do usuário para autenticação.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Retorna o nome da configuração de trabalho da instância do servidor web.
   * @return String Retorna o nome da configuração de trabalho da instância do
   *         servidor web.
   */
  public String getWorkConfiguration() {
    return workConfiguration;
  }

  public void serialize(SerializeWriter writer) throws IOException {
    writer.writeString(workConfiguration);
    writer.writeString(defaultConnectionName);
    writer.writeString(username);
    writer.writeString(password);
    writer.writeString(StringTools.arrayStringToString(masterRelationValues, ";"));
    writer.writeString(StringTools.arrayStringToString(masterRelationUserValues, ";"));
    writer.writeString(className);
    writer.writeString(methodName);
    writer.writeInt(parameterValues.length);
    for (int i=0; i<parameterValues.length; i++) {
      writer.writeObject(parameterValues[i]);
    } // for
  }

}
