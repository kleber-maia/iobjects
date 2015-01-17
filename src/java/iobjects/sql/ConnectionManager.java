package iobjects.sql;

import java.sql.*;
import java.util.*;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.sql.*;

/**
 * Classe responsável pelo gerenciamento global das conexões com o banco de
 * dados. Realiza novas conexões com o banco e mantém um pool de reuso.
 */
public class ConnectionManager implements Comparator {

  /**
   * Agendamento para limpeza automática das conexões antigas sem uso.
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
   * Define a extensão dos arquivos de configuração de conexão com o banco de dados.
   */
  static public final String CONFIG_FILE_EXTENSION = ".dbc";

  /**
   * Define o tempo máximo em minutos que uma conexão pode ficar sem uso até que seja
   * fechada e descartada pelo método 'shrink()'.
   */
  static public final int CONNECTION_TIME_OUT = 3;
  /**
   * Define o intervalo em minutos em que o Srhink é executado.
   */
  static public final int SHRINK_SCHEDULE = 1;

  static private ConnectionManager connectionManager = null;

  private ConnectionFiles connectionFiles     = new ConnectionFiles();
  private String          connectionFilesPath = "";
  private TreeSet         safeItems           = new TreeSet(this);
  private TreeSet         unsafeItems         = new TreeSet(this);
  private Timer           timer               = new Timer();

  /**
   * construtor padrão
   * @param connectionFilesPath String Caminho onde se encontram os arquivos
   *        de conexão com banco de dados.
   */
  private ConnectionManager(String connectionFilesPath) {
    // nossos valores
    this.connectionFilesPath = connectionFilesPath;
    try {
      // carrega as conexões
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
   * conexão existente no pool, ela será retornada, se não houver, chame o método
   * 'createSafeConnection()'.
   * <p><b>É obrigatório devolver a conexão emprestada através do método
   * 'returnSafeConnection()', sob pena de causar uma falha catastrófica na
   * aplicação.</b></p>
   * @param connectionName String Representa o nome da conexão, ou seja, nome
   *                       do arquivo de conexão que contém as informações
   *                       do driver, banco de dados e usuário.
   * @throws Exception Em caso de exceção na tentativa de inicar a conexão.
   * @return Pega um ConnectionItem emprestado para uso.
   */
  public ConnectionItem borrowSafeConnection(String connectionName) throws Exception {
    // localiza um item no pool
    return borrowSafeItem(connectionName);
  }

  /**
   * Retorna a lista arquivos de conexões.
   * @return Retorna a lista arquivos de conexões.
   */
  public ConnectionFiles connectionFiles() {
    return connectionFiles;
  }

  /**
   * Cria uma nova conexão utilizando os parâmetros do arquivo de configurações
   * informado por 'connectionName'.
   * @param connectionName String Representa o nome da conexão, ou seja, nome
   *                       do arquivo de conexão que contém as informações
   *                       do driver, banco de dados e usuário.
   * @return ConnectionItem
   * @throws Exception
   */
  private ConnectionItem createConnection(String connectionName) throws Exception {
    // localiza o arquivo de conexão
    ConnectionFile connectionFile = connectionFiles.get(connectionName, true);
    // se não achou o arquivo de conexão...exceção
    if (connectionFile == null)
      throw new ExtendedException(getClass().getName(), "createConnection", "Configuração de conexão não encontrada: '" + connectionName + "'.");
    // carrega o Driver
    Class.forName(connectionFile.driver().getName());
    // nova conexão
    Connection connection = DriverManager.getConnection(connectionFile.driver().getURL(),
                                                        connectionFile.user().getName(),
                                                        connectionFile.user().getPassword());
    // retorna
    return new ConnectionItem(connectionName, connection);
  }

  /**
   * Cria uma nova conexão com o banco de dados a partir das configurações
   * existentes no arquivo de conexões informado por 'connectionName'.
   * Antes de chamar este médoto, deve ser chamado o método 'borrowSafeConnection()'.
   * <p><b>É obrigatório devolver a conexão emprestada através do método
   * 'returnSafeConnection()', sob pena de causar uma falha catastrófica na
   * aplicação.</b></p>
   * @param connectionName String Representa o nome da conexão, ou seja, nome
   *                       do arquivo de conexão que contém as informações
   *                       do driver, banco de dados e usuário.
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
   * Retorna um ConnectionItem para uso compartilhado. <b>Estes itens não podem
   * ser utilizados em transações.</b>
   * @param connectionName String Representa o nome da conexão, ou seja, nome
   *                       do arquivo de conexão que contém as informações
   *                       do driver, banco de dados e usuário.
   * @return Retorna um ConnectionItem para uso compartilhado. <b>Estes itens
   *         não podem ser utilizados em transações.</b>
   * @throws Exception
   */
  public ConnectionItem getUnsafeConnection(String connectionName) throws Exception {
    // localiza um item no pool
    ConnectionItem result = getUnsafeItem(connectionName);
    // se já temos...
    if (result != null) {
      // atualiza último uso
      result.getLastUse().setTime(System.currentTimeMillis());
      // retorna
      return result;
    }
    // se ainda não temos...
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
   * Procura nos arquivos de conexão por um mapeamento para a classe informada
   * por 'className' e retorna seu nome. Caso não encontre retorna 'defaultConnectionName'.
   * @param className String Nome da classe cujo mapeamento se deseja localizar.
   * @param defaultConnectionName String Nome da conexão que será retornado
   *        caso nenhum mapeamento seja encontrado.
   * @return String Procura nos arquivos de conexão por um mapeamento para a classe
   *         informada por 'className' e retorna seu nome. Caso não encontre retorna
   *         'defaultConnectionName'.
   */
  public String getConnectionNameForClassMapping(String className, String defaultConnectionName) {
    // loop nos arquivos de conexão
    for (int i=0; i<connectionFiles.size(); i++) {
      // arquivo da vez
      ConnectionFile connectionFile = connectionFiles.get(i);
      // se encontramos um mapeamento para esta classe...
      if (connectionFile.mapping().contains(className))
        return connectionFiles.getName(i);
    } // for
    // se não achamos nada...retorna a default
    return defaultConnectionName;
  }

  /**
   * Retorna a instância de ConnectionManager para a aplicação.
   * @param connectionFilesPath String Caminho onde se encontram os arquivos
   *        de conexão com banco de dados.
   * @return Retorna a instância de ConnectionManager para a aplicação.
   */
  static public ConnectionManager getInstance(String connectionFilesPath) {
    if (connectionManager == null)
      connectionManager = new ConnectionManager(connectionFilesPath);
    return connectionManager;
  }

  /**
   * Carrega os arquivos de conexões de banco de dados.
   * @throws Exception Em caso de exceção na tentativa de acesso aos arquivos
   *                   de conexão com o banco de dados.
   */
  private void loadConnectionFiles() throws Exception {
    // pega a lista de arquivos de configuração de conexões
    String[] fileExtensions = {CONFIG_FILE_EXTENSION};
    String[] fileNames = FileTools.getFileNames(connectionFilesPath, fileExtensions, false);
    // limpa as conexões atuais
    connectionFiles.clear();
    // loop nos arquivos para carregá-los
    for (int i=0; i<fileNames.length; i++) {
      // arquivo da vez
      String fileName = fileNames[i];
      // carrega o arquivo
      ConnectionFile connectionFile = new ConnectionFile(connectionFilesPath + fileName);
      // adiciona a conexão
      connectionFiles.add(connectionFile, fileName.substring(0, fileName.indexOf('.')));
    } // for
  }

  /**
   * Devolve o ConnectionItem anteriormente emprestado para uso através do método
   * 'borrowSafeConnection()' ou 'createSafeConnection()'. A conexão será
   * devolvida para o pool e ficará a disposição para reuso.
   * @param connectionItem ConnectionItem para ser devolvido.
   * @throws Exception Em caso de exceção na tentativa de fechar as conexões.
   */
  public void returnSafeConnection(ConnectionItem connectionItem) throws Exception {
    // devolve a conexão segura
    returnSafeItem(connectionItem);
  }

  /*
   * Mantém os itens ordenados por nome (ascendente) e por data de uso (descendente).
   * Desta forma os itens de nomes iguais ficam agrupados e fáceis de serem utilizados
   * e as conexões mais recentemente utilizadas são priorizadas para que as
   * demais conexões possam ficar antigas e sejam descartadas.
   */
  public int compare(Object o1, Object o2) {
    // itens para comparar
    ConnectionItem connectionItem1 = (ConnectionItem)o1;
    ConnectionItem connectionItem2 = (ConnectionItem)o2;
    // comparação dos nomes
    int result = connectionItem1.name().compareTo(connectionItem2.name());
    // se não temos nomes iguais...retorna
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
   * Métodos para trabalhar de forma segura com 'safeItems'.
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
   * Utilize os métodos 'borrowSafeItem()', 'returnSafeItem()' ou 'shrinkSafeItems()'.
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
        // se a conexão está fechada (provavelmente devido a um erro)...
        if (result.connection().isClosed()) {
          // remove
          iterator.remove();
          // pega a próxima
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
      // se chegou até aqui...não achamos
      return null;
    }
    // se recebemos um item...devolve para a lista
    else if (item != null) {
      // se a conexão está fechada (provavelmente devido a um erro)...dispara
      if (item.connection().isClosed())
        return null;
      // define a última hora de uso
      item.getLastUse().setTime(System.currentTimeMillis());
      // adiciona na lista
      safeItems.add(item);
      // não é esperado nada de retorno
      return null;
    }
    // se não recebemos nada...vascula por itens antigos
    else {
      // agora
      java.util.Date now = new java.util.Date();
      // loop nas conexões seguras
      Iterator safeIterator = safeItems.iterator();
      while (safeIterator.hasNext()) {
        // item da vez
        item = (ConnectionItem)safeIterator.next();
        // se já está inativa há mais tempo que permitido...descarta
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
   * Métodos para trabalhar de forma segura com 'safeItems'.
   */

  private ConnectionItem getUnsafeItem(String name) throws Exception {
    return interactUnsafeItems(name);
  }

  private void shrinkUnsafeItems() throws Exception {
    interactUnsafeItems(null);
  }

  /*
   * Utilize os métodos 'getUnsafeItem()' ou 'shrinkUnsafeItems()'.
   */
  synchronized private ConnectionItem interactUnsafeItems(String name) throws Exception {
    // se temos um nome...vascula por um item existente e retorna
    if (name != null) {
      // loop nos itens
      Iterator iterator = unsafeItems.iterator();
      while (iterator.hasNext()) {
        // item da vez
        ConnectionItem result = (ConnectionItem)iterator.next();
        // se a conexão está fechada (provavelmente devido a um erro)...
        if (result.connection().isClosed()) {
          // remove
          iterator.remove();
          // pega a próxima
          continue;
        } // if
        // se achamos...retorna
        if (result.name().equalsIgnoreCase(name))
          return result;
      } // while
      // se chegou até aqui...não achamos
      return null;
    }
    // se não recebemos nada...vascula por itens antigos
    else {
      // agora
      java.util.Date now = new java.util.Date();
      // loop nas conexões seguras
      Iterator unsafeIterator = unsafeItems.iterator();
      while (unsafeIterator.hasNext()) {
        // item da vez
        ConnectionItem item = (ConnectionItem)unsafeIterator.next();
        // se já está inativa há mais tempo que permitido...descarta
        if ((now.getTime() - item.getLastUse().getTime()) / 1000 / 60 > CONNECTION_TIME_OUT) {
          try { item.connection().close(); } catch (Exception e) { }
          unsafeItems.remove(item);
        } // if
      } // while
      return null;
    }
  }

}
