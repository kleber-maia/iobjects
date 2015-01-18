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
