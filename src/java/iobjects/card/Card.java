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
package iobjects.card;

import java.sql.*;

import iobjects.*;
import iobjects.util.*;
import java.util.Calendar;

/**
 * Representa a classe base de todas as classes que representam cartões de
 * informação na aplicação.
 */
public class Card extends BusinessObject {

  /**
   * Construtor padrão.
   */
  public Card() {
  }

  /**
   * Executa a expressão 'sql' informada e retorna true em caso de sucesso.
   * @param sql Expressão SQL para ser executada.
   * @return Retorna true em caso de sucesso.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de
   *                   dados.
   */
  protected boolean execute(String sql) throws Exception {
    return SqlTools.execute(getConnection(), sql);
  }

  /**
   * Retorna um ResultSet representando o resultado da execução da expressão
   * SQL informada. O método que faz a chamada a este deve ser responsável
   * pelo fechamento do ResultSet e do seu Statement.
   * @param sql Expressão SQL que se deseja executar.
   * @return Retorna um ResultSet representando o resultado da execução da
   *         expressão SQL informada. O método que faz a chamada a executeQuery
   *         é responsável pelo fechamento do ResultSet e do seu Statement.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de
   *                   dados.
   */
  protected ResultSet executeQuery(String sql) throws Exception {
    return SqlTools.executeQuery(getConnection(), sql);
  }

  /**
   * Retorna um PreparedStatement para execução da expressão 'sql' informada.
   * O método que faz a chamada a prepare é responsável pelo fechamento do
   * Statement.
   * @param sql Expressão SQL para ser preparada.
   * @return Retorna um PreparedStatement para execução da expressão 'sql'
   *         informada. O método que faz a chamada a prepare é responsável pelo
   *         fechamento do Statement.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de
   *                   dados.
   */
  protected PreparedStatement prepare(String sql) throws Exception {
    return SqlTools.prepare(getConnection(), sql);
  }

  /**
   * Retorna um PreparedStatement para uma consulta na tabela
   * informada por 'tableName', com os campos informados por 'fields', na ordem
   * informada por 'orderFields' que atendam a condição informada por 'where'.
   * O método que faz a chamada a select é responsável pelo fechamento do
   * ResultSet e do seu Statement.
   * @param tableName Nome da tabela alvo da pesquisa.
   * @param fields Nomes dos campos que serão retornados.
   * @param orderFields Nomes dos que se deseja aplicar aos registros retornados.
   * @param where Condição que limita os registros que serão exibidos.
   * @return Retorna um PreparedStatement para uma consulta na tabela
   *         informada por 'tableName', com os campos informados por 'fields', na
   *         ordem informada por 'orderFields' que atendam a condição informada
   *         por 'where'. O método que faz a chamada a select é responsável pelo
   *         fechamento do ResultSet e do seu Statement.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de
   *                   dados.
   */
  protected PreparedStatement prepareSelect(String   tableName,
                                            String[] fields,
                                            String[] orderFields,
                                            String   where) throws Exception {
    return SqlTools.prepareSelect(getConnection(),
                                  tableName,
                                  fields,
                                  orderFields,
                                  where);
  }
    
}