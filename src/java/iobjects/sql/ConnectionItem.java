package iobjects.sql;

import java.sql.*;

/**
 * Representa uma conexão com o banco de dados e mantém informações para que
 * esta conexão possa ser reutilizada no pool de conexões.
 */
public class ConnectionItem {

  private String         name       = "";
  private Connection     connection = null;
  private java.util.Date lastUse    = null;

  /**
   * Construtor padrão.
   * @param name String Nome da conexão.
   * @param connection Connection com o banco.
   */
  public ConnectionItem(String     name,
                        Connection connection) {
    // nossos dados
    this.name       = name;
    this.connection = connection;
    this.lastUse    = new java.util.Date();
  }

  public Connection connection() {
    return connection;
  }

  public java.util.Date getLastUse() {
    return lastUse;
  }

  public String name() {
    return name;
  }

  public void setLastUse(java.util.Date lastUse) {
    this.lastUse = lastUse;
  }

}
