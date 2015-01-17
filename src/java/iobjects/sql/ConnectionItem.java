package iobjects.sql;

import java.sql.*;

/**
 * Representa uma conex�o com o banco de dados e mant�m informa��es para que
 * esta conex�o possa ser reutilizada no pool de conex�es.
 */
public class ConnectionItem {

  private String         name       = "";
  private Connection     connection = null;
  private java.util.Date lastUse    = null;

  /**
   * Construtor padr�o.
   * @param name String Nome da conex�o.
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
