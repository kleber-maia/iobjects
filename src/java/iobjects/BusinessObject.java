package iobjects;

import java.io.*;
import java.sql.*;

import iobjects.help.*;
import iobjects.util.*;

import iobjects.xml.sql.*;

/**
 * Representa um objeto que implementa regras de neg�cio.
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
   * Construtor padr�o.
   */
  public BusinessObject() {
  }

  /**
   * Retorna a lista de a��es do objeto.
   * @return ActionList Retorna a lista de a��es do objeto.
   */
  public ActionList actionList() {
    return actionList;
  }

  /**
   * Retorna a inst�ncia de Extension que carregou o objeto.
   * @return Extension Retorna a inst�ncia de Extension que carregou o objeto.
   */
  public Extension extension() {
    // se j� temos a inst�ncia de Extension...retorna-a
    if (extension != null)
      return extension;
    // pega o ger�ncia de extens�es
    ExtensionManager extensionManager = ExtensionManager.getInstance();
    // loop nas extens�es
    for (int i=0; i<extensionManager.extensions().length; i++) {
      // extens�o da vez
      Extension extension = extensionManager.extensions()[i];
      // se a extens�o carregou nossa classe...retorna-a
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
   * <b>De modo a trabalhar com o pool de conex�es de forma eficiente, � necess�rio
   * ter uma transa��o iniciada para usar este m�doto.</b>
   * @return Retorna o Connection utilizado para acesso ao banco de dados.
   * @throws Exception Em caso de exce��o na tentativa de localizar uma conex�o
   *                   com o nome informado ou de instanci�-la.
   */
  public Connection getConnection() throws Exception {
    return facade.getConnection(connectionName);
  }

  /**
   * Retorna o ConnectionFile contendo as configura��es da conex�o para acesso
   * ao banco de dados.
   * @return Retorna o ConnectionFile contendo as configura��es da conex�o para
   *         acesso ao banco de dados.
   */
  public ConnectionFile getConnectionFile() {
    return connectionFile;
  }

  /**
   * Retorna o nome da conex�o para acesso ao banco de dados.
   * @return Retorna o nome da conex�o para acesso ao banco de dados.
   */
  public String getConnectionName() {
    return connectionName;
  }

  /**
   * Retorna a inst�ncia da fachada que instanciou o objeto.
   * @return Facade Retorna a inst�ncia da fachada que instanciou o objeto.
   */
  public Facade getFacade() {
    return facade;
  }

  /**
   * Inicializa o objeto. Deve ser sobreposto pelas classes descendentes a fim
   * de executar opera��es inerentes � inicializa��o do objeto.
   * @throws Exception Em caso de exce��o na tentativa de inicializar o objeto.
   */
  public void initialize() throws Exception {
  }

  /**
   * Define o ConnectionFile contendo as configura��es da conex�o para acesso
   * ao banco de dados.
   * @param connectionFile ConnectionFile contendo as configura��es da conex�o
   *                       para acesso ao banco de dados.
   */
  public void setConnectionFile(ConnectionFile connectionFile) {
    this.connectionFile = connectionFile;
  }

  /**
   * Retorna o nome da conex�o para acesso ao banco de dados.
   * @param connectionName String Nome da conex�o.
   */
  public void setConnectionName(String connectionName) {
    this.connectionName = connectionName;
  }

  /**
   * Define a inst�ncia da fachada que instanciou o objeto.
   * @param facade Inst�ncia da fachada que instanciou o objeto.
   */
  public void setFacade(Facade facade) {
    this.facade = facade;
  }

  /**
   * Retorna a lista de par�metros de usu�rio do objeto.
   * @return ParamList Retorna a lista de par�metros de usu�rio do objeto.
   * @since 3.1
   */
  public ParamList userParamList() {
    return userParamList;
  }

}
