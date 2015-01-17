package iobjects;

import java.io.*;
import java.sql.*;

import iobjects.help.*;
import iobjects.util.*;

import iobjects.xml.sql.*;

/**
 * Representa um objeto que implementa regras de negócio.
 */
public class BusinessObject {

  private ActionList     actionList     = new ActionList();
  private String         connectionName = "";
  private ConnectionFile connectionFile = null;
  private Extension      extension      = null;
  private Facade         facade         = null;
  private FAQList        faqList        = new FAQList();
  private ParamList      userParamList  = new ParamList();

  /**
   * Construtor padrão.
   */
  public BusinessObject() {
  }

  /**
   * Retorna a lista de ações do objeto.
   * @return ActionList Retorna a lista de ações do objeto.
   */
  public ActionList actionList() {
    return actionList;
  }

  /**
   * Retorna a instância de Extension que carregou o objeto.
   * @return Extension Retorna a instância de Extension que carregou o objeto.
   */
  public Extension extension() {
    // se já temos a instância de Extension...retorna-a
    if (extension != null)
      return extension;
    // pega o gerência de extensões
    ExtensionManager extensionManager = ExtensionManager.getInstance();
    // loop nas extensões
    for (int i=0; i<extensionManager.extensions().length; i++) {
      // extensão da vez
      Extension extension = extensionManager.extensions()[i];
      // se a extensão carregou nossa classe...retorna-a
      if (StringTools.arrayContains(extension.getClassNames(), getClass().getName())) {
        this.extension = extension;
        break;
      } // if
    } // for
    // retorna o que encontramos
    return extension;
  }

  /**
   * Retorna a FAQList do objeto.
   * @return FAQList Retorna a FAQList do objeto.
   */
  public FAQList faqList() {
    return faqList;
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    actionList     = null;
    connectionFile = null;
    extension      = null;
    facade         = null;
    faqList        = null;
    userParamList  = null;
  }

  /**
   * Retorna o Connection utilizado para acesso ao banco de dados.
   * <b>De modo a trabalhar com o pool de conexões de forma eficiente, é necessário
   * ter uma transação iniciada para usar este médoto.</b>
   * @return Retorna o Connection utilizado para acesso ao banco de dados.
   * @throws Exception Em caso de exceção na tentativa de localizar uma conexão
   *                   com o nome informado ou de instanciá-la.
   */
  public Connection getConnection() throws Exception {
    return facade.getConnection(connectionName);
  }

  /**
   * Retorna o ConnectionFile contendo as configurações da conexão para acesso
   * ao banco de dados.
   * @return Retorna o ConnectionFile contendo as configurações da conexão para
   *         acesso ao banco de dados.
   */
  public ConnectionFile getConnectionFile() {
    return connectionFile;
  }

  /**
   * Retorna o nome da conexão para acesso ao banco de dados.
   * @return Retorna o nome da conexão para acesso ao banco de dados.
   */
  public String getConnectionName() {
    return connectionName;
  }

  /**
   * Retorna a instância da fachada que instanciou o objeto.
   * @return Facade Retorna a instância da fachada que instanciou o objeto.
   */
  public Facade getFacade() {
    return facade;
  }

  /**
   * Inicializa o objeto. Deve ser sobreposto pelas classes descendentes a fim
   * de executar operações inerentes à inicialização do objeto.
   * @throws Exception Em caso de exceção na tentativa de inicializar o objeto.
   */
  public void initialize() throws Exception {
  }

  /**
   * Define o ConnectionFile contendo as configurações da conexão para acesso
   * ao banco de dados.
   * @param connectionFile ConnectionFile contendo as configurações da conexão
   *                       para acesso ao banco de dados.
   */
  public void setConnectionFile(ConnectionFile connectionFile) {
    this.connectionFile = connectionFile;
  }

  /**
   * Retorna o nome da conexão para acesso ao banco de dados.
   * @param connectionName String Nome da conexão.
   */
  public void setConnectionName(String connectionName) {
    this.connectionName = connectionName;
  }

  /**
   * Define a instância da fachada que instanciou o objeto.
   * @param facade Instância da fachada que instanciou o objeto.
   */
  public void setFacade(Facade facade) {
    this.facade = facade;
  }

  /**
   * Retorna a lista de parâmetros de usuário do objeto.
   * @return ParamList Retorna a lista de parâmetros de usuário do objeto.
   * @since 3.1
   */
  public ParamList userParamList() {
    return userParamList;
  }

}
