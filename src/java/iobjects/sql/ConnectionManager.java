package iobjects.sql;

import java.sql.*;
import java.util.*;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.sql.*;

/**
 * Classe respons�vel pelo gerenciamento global das conex�es com o banco de
 * dados. Realiza novas conex�es com o banco e mant�m um pool de reuso.
 */
public class ConnectionManager implements Comparator {

  /**
   * Agendamento para limpeza autom�tica das conex�es antigas sem uso.
   */
  class ShrinkTask extends TimerTask {
    public void run() {
      try {
        if (connectionManager == null)
          return;
        connectionManager.shrinkSafeItems();
        connectionManager.shrinkUnsafeItems();
      }
      catch (Exception e) {
        e.printStackTrace();
      } // try-catch
    }
  }

  /**
   * Define a extens�o dos arquivos de configura��o de conex�o com o banco de dados.
   */
  static public final String CONFIG_FILE_EXTENSION = ".dbc";

  /**
   * Define o tempo m�ximo em minutos que uma conex�o pode ficar sem uso at� que seja
   * fechada e descartada pelo m�todo 'shrink()'.
   */
  static public final int CONNECTION_TIME_OUT = 3;
  /**
   * Define o intervalo em minutos em que o Srhink � executado.
   */
  static public final int SHRINK_SCHEDULE = 1;

  static private ConnectionManager connectionManager = null;

  private ConnectionFiles connectionFiles     = new ConnectionFiles();
  private String          connectionFilesPath = "";
  private TreeSet         safeItems           = new TreeSet(this);
  private TreeSet         unsafeItems         = new TreeSet(this);
  private Timer           timer               = new Timer();

  /**
   * construtor padr�o
   * @param connectionFilesPath String Caminho onde se encontram os arquivos
   *        de conex�o com banco de dados.
   */
  private ConnectionManager(String connectionFilesPath) {
    // nossos valores
    this.connectionFilesPath = connectionFilesPath;
    try {
      // carrega as conex�es
      loadConnectionFiles();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
    // nosso timer para o shrink
    timer.schedule(new ShrinkTask(), 1000 * 60 * SHRINK_SCHEDULE, 1000 * 60 * SHRINK_SCHEDULE);
  }

  /**
   * Pega um ConnectionItem emprestado para uso, casa haja um. Se houver uma
   * conex�o existente no pool, ela ser� retornada, se n�o houver, chame o m�todo
   * 'createSafeConnection()'.
   * <p><b>� obrigat�rio devolver a conex�o emprestada atrav�s do m�todo
   * 'returnSafeConnection()', sob pena de causar uma falha catastr�fica na
   * aplica��o.</b></p>
   * @param connectionName String Representa o nome da conex�o, ou seja, nome
   *                       do arquivo de conex�o que cont�m as informa��es
   *                       do driver, banco de dados e usu�rio.
   * @throws Exception Em caso de exce��o na tentativa de inicar a conex�o.
   * @return Pega um ConnectionItem emprestado para uso.
   */
  public ConnectionItem borrowSafeConnection(String connectionName) throws Exception {
    // localiza um item no pool
    return borrowSafeItem(connectionName);
  }

  /**
   * Retorna a lista arquivos de conex�es.
   * @return Retorna a lista arquivos de conex�es.
   */
  public ConnectionFiles connectionFiles() {
    return connectionFiles;
  }

  /**
   * Cria uma nova conex�o utilizando os par�metros do arquivo de configura��es
   * informado por 'connectionName'.
   * @param connectionName String Representa o nome da conex�o, ou seja, nome
   *                       do arquivo de conex�o que cont�m as informa��es
   *                       do driver, banco de dados e usu�rio.
   * @return ConnectionItem
   * @throws Exception
   */
  private ConnectionItem createConnection(String connectionName) throws Exception {
    // localiza o arquivo de conex�o
    ConnectionFile connectionFile = connectionFiles.get(connectionName, true);
    // se n�o achou o arquivo de conex�o...exce��o
    if (connectionFile == null)
      throw new ExtendedException(getClass().getName(), "createConnection", "Configura��o de conex�o n�o encontrada: '" + connectionName + "'.");
    // carrega o Driver
    Class.forName(connectionFile.driver().getName());
    // nova conex�o
    Connection connection = DriverManager.getConnection(connectionFile.driver().getURL(),
                                                        connectionFile.user().getName(),
                                                        connectionFile.user().getPassword());
    // retorna
    return new ConnectionItem(connectionName, connection);
  }

  /**
   * Cria uma nova conex�o com o banco de dados a partir das configura��es
   * existentes no arquivo de conex�es informado por 'connectionName'.
   * Antes de chamar este m�doto, deve ser chamado o m�todo 'borrowSafeConnection()'.
   * <p><b>� obrigat�rio devolver a conex�o emprestada atrav�s do m�todo
   * 'returnSafeConnection()', sob pena de causar uma falha catastr�fica na
   * aplica��o.</b></p>
   * @param connectionName String Representa o nome da conex�o, ou seja, nome
   *                       do arquivo de conex�o que cont�m as informa��es
   *                       do driver, banco de dados e usu�rio.
   * @return Connection
   * @throws Exception
   */
  public ConnectionItem createSafeConnection(String connectionName) throws Exception {
    // retorna
    return createConnection(connectionName);
  }

  public void finalize() {
    connectionFiles.clear();
    safeItems.clear();
    unsafeItems.clear();
  }

  /**
   * Retorna um ConnectionItem para uso compartilhado. <b>Estes itens n�o podem
   * ser utilizados em transa��es.</b>
   * @param connectionName String Representa o nome da conex�o, ou seja, nome
   *                       do arquivo de conex�o que cont�m as informa��es
   *                       do driver, banco de dados e usu�rio.
   * @return Retorna um ConnectionItem para uso compartilhado. <b>Estes itens
   *         n�o podem ser utilizados em transa��es.</b>
   * @throws Exception
   */
  public ConnectionItem getUnsafeConnection(String connectionName) throws Exception {
    // localiza um item no pool
    ConnectionItem result = getUnsafeItem(connectionName);
    // se j� temos...
    if (result != null) {
      // atualiza �ltimo uso
      result.getLastUse().setTime(System.currentTimeMillis());
      // retorna
      return result;
    }
    // se ainda n�o temos...
    else {
      // cria
      result = createConnection(connectionName);
      // adiciona na lista
      unsafeItems.add(result);
      // retorna
      return result;
    } // if
  }

  /**
   * Procura nos arquivos de conex�o por um mapeamento para a classe informada
   * por 'className' e retorna seu nome. Caso n�o encontre retorna 'defaultConnectionName'.
   * @param className String Nome da classe cujo mapeamento se deseja localizar.
   * @param defaultConnectionName String Nome da conex�o que ser� retornado
   *        caso nenhum mapeamento seja encontrado.
   * @return String Procura nos arquivos de conex�o por um mapeamento para a classe
   *         informada por 'className' e retorna seu nome. Caso n�o encontre retorna
   *         'defaultConnectionName'.
   */
  public String getConnectionNameForClassMapping(String className, String defaultConnectionName) {
    // loop nos arquivos de conex�o
    for (int i=0; i<connectionFiles.size(); i++) {
      // arquivo da vez
      ConnectionFile connectionFile = connectionFiles.get(i);
      // se encontramos um mapeamento para esta classe...
      if (connectionFile.mapping().contains(className))
        return connectionFiles.getName(i);
    } // for
    // se n�o achamos nada...retorna a default
    return defaultConnectionName;
  }

  /**
   * Retorna a inst�ncia de ConnectionManager para a aplica��o.
   * @param connectionFilesPath String Caminho onde se encontram os arquivos
   *        de conex�o com banco de dados.
   * @return Retorna a inst�ncia de ConnectionManager para a aplica��o.
   */
  static public ConnectionManager getInstance(String connectionFilesPath) {
    if (connectionManager == null)
      connectionManager = new ConnectionManager(connectionFilesPath);
    return connectionManager;
  }

  /**
   * Carrega os arquivos de conex�es de banco de dados.
   * @throws Exception Em caso de exce��o na tentativa de acesso aos arquivos
   *                   de conex�o com o banco de dados.
   */
  private void loadConnectionFiles() throws Exception {
    // pega a lista de arquivos de configura��o de conex�es
    String[] fileExtensions = {CONFIG_FILE_EXTENSION};
    String[] fileNames = FileTools.getFileNames(connectionFilesPath, fileExtensions, false);
    // limpa as conex�es atuais
    connectionFiles.clear();
    // loop nos arquivos para carreg�-los
    for (int i=0; i<fileNames.length; i++) {
      // arquivo da vez
      String fileName = fileNames[i];
      // carrega o arquivo
      ConnectionFile connectionFile = new ConnectionFile(connectionFilesPath + fileName);
      // adiciona a conex�o
      connectionFiles.add(connectionFile, fileName.substring(0, fileName.indexOf('.')));
    } // for
  }

  /**
   * Devolve o ConnectionItem anteriormente emprestado para uso atrav�s do m�todo
   * 'borrowSafeConnection()' ou 'createSafeConnection()'. A conex�o ser�
   * devolvida para o pool e ficar� a disposi��o para reuso.
   * @param connectionItem ConnectionItem para ser devolvido.
   * @throws Exception Em caso de exce��o na tentativa de fechar as conex�es.
   */
  public void returnSafeConnection(ConnectionItem connectionItem) throws Exception {
    // devolve a conex�o segura
    returnSafeItem(connectionItem);
  }

  /*
   * Mant�m os itens ordenados por nome (ascendente) e por data de uso (descendente).
   * Desta forma os itens de nomes iguais ficam agrupados e f�ceis de serem utilizados
   * e as conex�es mais recentemente utilizadas s�o priorizadas para que as
   * demais conex�es possam ficar antigas e sejam descartadas.
   */
  public int compare(Object o1, Object o2) {
    // itens para comparar
    ConnectionItem connectionItem1 = (ConnectionItem)o1;
    ConnectionItem connectionItem2 = (ConnectionItem)o2;
    // compara��o dos nomes
    int result = connectionItem1.name().compareTo(connectionItem2.name());
    // se n�o temos nomes iguais...retorna
    if (result != 0)
      return result;
    // se temos os nome iguais...compara pela data de uso, mas ordena descendente
    else
      return connectionItem1.getLastUse().compareTo(connectionItem2.getLastUse()) * -1;
  }

  public boolean equals(Object obj) {
    return false;
  }

  /*
   * M�todos para trabalhar de forma segura com 'safeItems'.
   */

  private ConnectionItem borrowSafeItem(String name) throws Exception {
    return interactSafeItems(name, null);
  }

  private void returnSafeItem(ConnectionItem item) throws Exception {
    interactSafeItems(null, item);
  }

  private void shrinkSafeItems() throws Exception {
    interactSafeItems(null, null);
  }

  /*
   * Utilize os m�todos 'borrowSafeItem()', 'returnSafeItem()' ou 'shrinkSafeItems()'.
   */
  synchronized private ConnectionItem interactSafeItems(String name, ConnectionItem item) throws Exception {
    // se temos um nome...vasculha por um item existente, remove da lista e retorna
    if (name != null) {
      // nosso resultado
      ConnectionItem result = null;
      // loop nos itens
      Iterator iterator = safeItems.iterator();
      while (iterator.hasNext()) {
        // item da vez
        result = (ConnectionItem)iterator.next();
        // se a conex�o est� fechada (provavelmente devido a um erro)...
        if (result.connection().isClosed()) {
          // remove
          iterator.remove();
          // pega a pr�xima
          continue;
        } // if
        // se achamos...
        if (result.name().equalsIgnoreCase(name)) {
          // remove da lista
          safeItems.remove(result);
          // retorna
          return result;
        } // if
      } // while
      // se chegou at� aqui...n�o achamos
      return null;
    }
    // se recebemos um item...devolve para a lista
    else if (item != null) {
      // se a conex�o est� fechada (provavelmente devido a um erro)...dispara
      if (item.connection().isClosed())
        return null;
      // define a �ltima hora de uso
      item.getLastUse().setTime(System.currentTimeMillis());
      // adiciona na lista
      safeItems.add(item);
      // n�o � esperado nada de retorno
      return null;
    }
    // se n�o recebemos nada...vascula por itens antigos
    else {
      // agora
      java.util.Date now = new java.util.Date();
      // loop nas conex�es seguras
      Iterator safeIterator = safeItems.iterator();
      while (safeIterator.hasNext()) {
        // item da vez
        item = (ConnectionItem)safeIterator.next();
        // se j� est� inativa h� mais tempo que permitido...descarta
        if ((now.getTime() - item.getLastUse().getTime()) / 1000 / 60 > CONNECTION_TIME_OUT) {
          try { item.connection().close(); } catch (Exception e) { }
          safeIterator.remove();
          //safeItems.remove(item);
        } // if
      } // while
      return null;
    }
  }

  /*
   * M�todos para trabalhar de forma segura com 'safeItems'.
   */

  private ConnectionItem getUnsafeItem(String name) throws Exception {
    return interactUnsafeItems(name);
  }

  private void shrinkUnsafeItems() throws Exception {
    interactUnsafeItems(null);
  }

  /*
   * Utilize os m�todos 'getUnsafeItem()' ou 'shrinkUnsafeItems()'.
   */
  synchronized private ConnectionItem interactUnsafeItems(String name) throws Exception {
    // se temos um nome...vascula por um item existente e retorna
    if (name != null) {
      // loop nos itens
      Iterator iterator = unsafeItems.iterator();
      while (iterator.hasNext()) {
        // item da vez
        ConnectionItem result = (ConnectionItem)iterator.next();
        // se a conex�o est� fechada (provavelmente devido a um erro)...
        if (result.connection().isClosed()) {
          // remove
          iterator.remove();
          // pega a pr�xima
          continue;
        } // if
        // se achamos...retorna
        if (result.name().equalsIgnoreCase(name))
          return result;
      } // while
      // se chegou at� aqui...n�o achamos
      return null;
    }
    // se n�o recebemos nada...vascula por itens antigos
    else {
      // agora
      java.util.Date now = new java.util.Date();
      // loop nas conex�es seguras
      Iterator unsafeIterator = unsafeItems.iterator();
      while (unsafeIterator.hasNext()) {
        // item da vez
        ConnectionItem item = (ConnectionItem)unsafeIterator.next();
        // se j� est� inativa h� mais tempo que permitido...descarta
        if ((now.getTime() - item.getLastUse().getTime()) / 1000 / 60 > CONNECTION_TIME_OUT) {
          try { item.connection().close(); } catch (Exception e) { }
          unsafeItems.remove(item);
        } // if
      } // while
      return null;
    }
  }

}
