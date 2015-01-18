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
package iobjects.util;

import java.io.*;
import java.sql.*;

/**
 * Classe utilit�ria com rotinas para inclus�o, consulta, altera��o, exclus�o,
 * entre outros, em base de dados padr�o SQL.
 */
public class SqlTools {

  static public void main(String[] args) {
    try {
      Class.forName("org.postgresql.Driver");
      Connection connection = DriverManager.getConnection("jdbc:postgresql://server.imanager.com.br/iman_auditon", "iman_auditon", "iman456526");
      ResultSet resultSet = executeQuery(connection, "select ch_uf || to_char(in_contato_id, '00000'), va_nome from relacionamento_contato order by ch_uf, in_contato_id");
      saveResultSetToFile(resultSet, "/Volumes/Dados/Auditon.txt");
    }
    catch (Exception e) {
      e.printStackTrace();
    } // try-catch
  }
  
  /**
   * Executa a express�o 'sql' informada e retorna true em caso de sucesso.
   * @param connection Connection para ser utilizado na opera��o.
   * @param sql Express�o SQL para ser executada.
   * @return Retorna true em caso de sucesso.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public boolean execute(Connection connection,
                                String     sql) throws Exception {
    // cria o Statement
    Statement statement = connection.createStatement();
    try {
      // executa o SQL
      return statement.execute(sql);
    }
    finally {
      // fecha o Statement
      if (statement != null)
        statement.close();
    } // try-finally
  }

  /**
   * Retorna um ResultSet representando o resultado da execu��o da express�o
   * SQL informada. <b>O m�todo que faz a chamada a este deve ser respons�vel
   * pelo fechamento do ResultSet e do seu Statement.</b>
   * @param connection Connection para ser utilizado na opera��o.
   * @param sql Express�o SQL que se deseja executar.
   * @return Retorna um ResultSet representando o resultado da execu��o da
   *         express�o SQL informada. <b>O m�todo que faz a chamada a executeQuery
   *         � respons�vel pelo fechamento do ResultSet e do seu Statement.</b>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public ResultSet executeQuery(Connection connection,
                                       String     sql) throws Exception {
    // cria o Statement
    Statement statement = connection.createStatement();
    // retorna o ResultSet
    return statement.executeQuery(sql);
  }

  /**
   * Retorna o pr�ximo valor da seq��ncia hier�rquica na entidade formada por
   * 'sequenceFieldName' no formato:
   * 001.001.001. Onde: cada grupo de 3 d�gitos representa a seq��ncia em um
   * n�vel na hierarquia, similar a um plano de contas cont�bil.
   * @param connection Connection para ser utilizado na opera��o.
   * @param tableName Nome da tabela.
   * @param sequenceFieldName Campo formador da seq��ncia hier�rquica.
   * @param where Express�o que limita a faixa da seq��ncia que ser� gerada.
   * @param parentSequence Valor da seq��ncia do registro pai. Esta � a refer�ncia
   *                       a partir da qual poder� ser determinado o pr�ximo valor
   *                       da seq��ncia do registro filho.
   * @return Retorna o pr�ximo valor da seq��ncia hier�rquica na entidade formada
   *         por 'sequenceFieldName',  no formato: 001.001.001. Onde: cada grupo
   *         de 3 d�gitos representa  a seq��ncia em um n�vel na hierarquia,
   *         similar a um plano de contas cont�bil.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *         dados.
   */
  static public String getNextParentSequence(Connection connection,
                                             String     tableName,
                                             String     sequenceFieldName,
                                             String     where,
                                             String     parentSequence) throws Exception {
    // consulta o maior c�digo com a seq��ncia pai
    String[] fields = {"MAX(" + sequenceFieldName + ")"};
    String[] order = {};
    PreparedStatement preparedStatement = prepareSelect(connection,
                                                        tableName,
                                                        fields,
                                                        order,
                                                        where + (!where.equals("") ? " AND " : "") + sequenceFieldName + " LIKE ?");
    ResultSet resultSet = null;
    try {
      preparedStatement.setString(1, parentSequence + "%");
      preparedStatement.execute();
      resultSet = preparedStatement.getResultSet();
      resultSet.next();
      // resultado da consulta
      String result = resultSet.getString(1);
      // se ainda n�o temos nada...seq��ncia inicial do primeiro n�vel
      if (result == null)
        result = "000";
      // divide a seq��ncia pai em n�veis
      String[] parentLevels = (!parentSequence.equals("") ? parentSequence.split("-") : new String[0]);
      // divide o maior valor da seq��ncia em n�veis
      String[] maxLevels = result.split("-");
      // se temos a qde de n�veis necess�rios...
      if (maxLevels.length >= parentLevels.length+1) {
        // incrementa a seq��ncia um n�vel acima do pai
        String nextSequence = maxLevels[parentLevels.length];
        nextSequence = NumberTools.completeZero(Integer.parseInt(nextSequence)+1, 3);
        // utilizaremos os n�veis iniciais da seq��ncia pai
        result = "";
        for (int i=0; i<parentLevels.length; i++) {
          if (result.length() > 0)
            result += "-";
          result += parentLevels[i];
        } // if
        // o �ltimo n�vel � o que acabamos de incrementar
        if (result.length() > 0)
          result += "-";
        result += nextSequence;
      }
      // se temos que criar um n�vel a mais
      else {
        // nosso resultado
        result = parentSequence + "-001";
      } // if
      // retorna
      return result;
    }
    finally {
      if (resultSet != null)
        resultSet.close();
      if (preparedStatement != null)
        preparedStatement.close();
    } // try-finally
  }

  /**
   * Retorna o pr�ximo valor da seq��ncia formada por 'sequenceFieldName'
   * em 'tableName'.
   * @param connection Connection para ser utilizado na opera��o.
   * @param tableName Nome da tabela.
   * @param sequenceFieldName Nome do campo formador da seq��ncia.
   * @param digitVerifier True para adicionar o d�gito verificador ao valor
   *                      retornado pela seq��ncia.
   * @return Retorna o pr�ximo valor da seq��ncia formada por 'sequenceFieldName'
   *         em 'tableName'.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   * dados.
   */
  static public int getNextSequence(Connection connection,
                                    String     tableName,
                                    String     sequenceFieldName,
                                    boolean    digitVerifier) throws Exception {
    // retorna
    return getNextSequence(connection,
                           tableName,
                           sequenceFieldName,
                           "",
                           digitVerifier);
  }

  /**
   * Retorna o pr�ximo valor da seq��ncia formada por 'sequenceFieldName'
   * em 'tableName'.
   * @param connection Connection para ser utilizado na opera��o.
   * @param tableName Nome da tabela.
   * @param sequenceFieldName Nome do campo formador da seq��ncia.
   * @param where Express�o que limita a faixa da seq��ncia que ser� gerada.
   * @param digitVerifier True para adicionar o d�gito verificador ao valor
   *                      retornado pela seq��ncia.
   * @return Retorna o pr�ximo valor da seq��ncia formada por 'sequenceFieldName'
   *         em 'tableName'.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   * dados.
   */
  static public int getNextSequence(Connection connection,
                                    String     tableName,
                                    String     sequenceFieldName,
                                    String     where,
                                    boolean    digitVerifier) throws Exception {
    // faz a consulta
    String[] fields = {"MAX(" + sequenceFieldName + ")"};
    PreparedStatement preparedStatement = prepareSelect(connection,
                                                        tableName,
                                                        fields,
                                                        new String[0],
                                                        where);
    ResultSet resultSet = null;
    try {
      preparedStatement.execute();
      resultSet = preparedStatement.getResultSet();
      resultSet.next();
      // resultado da consulta
      int result = resultSet.getInt(1);
      if (result < 0)
        result = 0;
      // se vamos usar d�gito verificador...
      if (digitVerifier) {
        // se temos mais de 1 digito...elimina o �ltimo que � o d�gito verificador
        String strResult = Integer.toString(result);
        if (strResult.length() > 1)
          result = Integer.parseInt(strResult.substring(0, strResult.length()-1));
        // incrementa em uma unidade
        result++;
        // adiciona o d�gito verificador calculado
        result = DigitVerifier.calculateSimpleDigitVerifier(result);
      } // if
      // se n�o vamos usar d�gito verificador...incrementa em uma unidade
      else
        result++;
      // retorna
      return result;
    }
    finally {
      if (resultSet != null)
        resultSet.close();
      if (preparedStatement != null)
        preparedStatement.close();
    } // try-finally
  }

  /**
   * Retorna um PreparedStatement para execu��o da express�o 'sql' informada.
   * <b>O m�todo que faz a chamada a prepare � respons�vel pelo fechamento do
   * Statement.</b>
   * @param connection Connection para ser utilizado na opera��o.
   * @param sql Express�o SQL para ser preparada.
   * @return Retorna um PreparedStatement para execu��o da express�o 'sql'
   *         informada. <b>O m�todo que faz a chamada a prepare � respons�vel pelo
   *         fechamento do Statement.</b>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public PreparedStatement prepare(Connection connection,
                                          String     sql) throws Exception {
    // executa o SQL
    return connection.prepareStatement(sql);
  }

  /**
   * Retorna um PreparedStatement para exclus�o de registros em 'tableName' que
   * atendam a condi��o informada por 'where'. <b>O m�todo que faz a chamada a
   * este deve ser respons�vel pelo fechamento do Statement.</b>
   * @param connection Connection para ser utilizado na opera��o.
   * @param tableName Nome da tabela alvo da exclus�o.
   * @param where Express�o que limita os registros que ser�o exclu�dos.
   * @return Retorna um PreparedStatement para exclus�o de registros em
   *         'tableName' que atendam a condi��o informada por 'where'.
   *         <b>O m�todo que faz a chamada a este deve ser respons�vel
   *         pelo fechamento do Statement.</b>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public PreparedStatement prepareDelete(Connection connection,
                                                String     tableName,
                                                String     where) throws Exception {
    // express�o SQL
    StringBuffer sql = new StringBuffer("DELETE FROM " + tableName);
    // temos where?
    if ((where != null) && (!where.equals("")))
      sql.append(" WHERE (" + where + ")");
    // executa
    return prepare(connection, sql.toString());
  }

  /**
   * Retorna um PreparedStatement para inser��o de registros na tabela
   * informada por 'tableName' preenchendo os campos informados por 'fields'.
   * <b>O m�todo que faz a chamada a prepareInsert � respons�vel pelo fechamento do
   * Statement.</b>
   * @param connection Connection para ser utilizado na opera��o.
   * @param tableName Nome da tabela alvo da inclus�o.
   * @param fields Nomes dos campos cujos valores ser�o inclu�dos.
   * @return Retorna um PreparedStatement para inser��o de registros na tabela
   * informada por 'tableName' preenchendo os campos informados por 'fields'.
   * <b>O m�todo que faz a chamada a prepareInsert � respons�vel pelo fechamento do
   * Statement.</b>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public PreparedStatement prepareInsert(Connection connection,
                                                String     tableName,
                                                String[]   fields) throws Exception {
    return prepareInsert(connection,
                         tableName,
                         fields,
                         null);
  }

  /**
   * Retorna um PreparedStatement para inser��o de registros na tabela
   * informada por 'tableName' preenchendo os campos informados por 'fields'.
   * <b>O m�todo que faz a chamada a prepareInsert � respons�vel pelo fechamento do
   * Statement.</b>
   * @param connection Connection para ser utilizado na opera��o.
   * @param tableName Nome da tabela alvo da inclus�o.
   * @param fields Nomes dos campos cujos valores ser�o inclu�dos.
   * @param values Valores para serem atribu�dos diretamente aos campos. Utilizado
   *               por exemplo em casos de campos constantes ou Sequences do
   *               Oracle. Deve estar na mesma ordem que 'fields' por�m n�o
   *               � necess�rio incluir um valor para todos os campos.
   * @return Retorna um PreparedStatement para inser��o de registros na tabela
   * informada por 'tableName' preenchendo os campos informados por 'fields'.
   * <b>O m�todo que faz a chamada a prepareInsert � respons�vel pelo fechamento do
   * Statement.</b>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public PreparedStatement prepareInsert(Connection connection,
                                                String     tableName,
                                                String[]   fields,
                                                String[]   values) throws Exception {
    // express�o SQL
    StringBuffer sql = new StringBuffer("INSERT INTO " + tableName + " ");
    // nomes dos campos
    sql.append("(");
    for (int i=0; i<fields.length; i++) {
      // devemos colocar ',' ?
      if (i > 0)
        sql.append(",");
      // campo da vez
      sql.append(fields[i]);
    } // for
    sql.append(")");
    // valores
    sql.append(" VALUES (");
    for (int i=0; i<fields.length; i++) {
      // devemos colocar ',' ?
      if (i > 0)
        sql.append(",");
      // se temos um valor para o campo...usa-o
      if ((values != null) && (i < values.length))
        sql.append(values[i]);
      // se n�o...ser� um par�metro
      else
        sql.append("?");
    } // for
    sql.append(")");
    // executa
    return prepare(connection, sql.toString());
  }

  /**
   * Retorna um PreparedStatement para uma consulta na tabela
   * informada por 'tableName', com os campos informados por 'fields', na ordem
   * informada por 'orderFields' que atendam a condi��o informada por 'where'.
   * <b>O m�todo que faz a chamada a select � respons�vel pelo fechamento do
   * ResultSet e do seu Statement.</b>
   * @param connection Connection para ser utilizado na opera��o.
   * @param tableName Nome da tabela alvo da pesquisa.
   * @param fields Nomes dos campos que ser�o retornados.
   * @param orderFields Nomes dos que se deseja aplicar aos registros retornados.
   * @param where Condi��o que limita os registros que ser�o exibidos.
   * @return Retorna um PreparedStatement para uma consulta na tabela
   *         informada por 'tableName', com os campos informados por 'fields', na
   *         ordem informada por 'orderFields' que atendam a condi��o informada
   *         por 'where'. <b>O m�todo que faz a chamada a select � respons�vel pelo
   *         fechamento do ResultSet e do seu Statement.</b>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public PreparedStatement prepareSelect(Connection connection,
                                                String     tableName,
                                                String[]   fields,
                                                String[]   orderFields,
                                                String     where) throws Exception {
    return prepareSelect(connection, tableName, fields, orderFields, where, 0, 0);
  }
  
  /**
   * Retorna um PreparedStatement para uma consulta na tabela
   * informada por 'tableName', com os campos informados por 'fields', na ordem
   * informada por 'orderFields' que atendam a condi��o informada por 'where'.
   * <b>O m�todo que faz a chamada a select � respons�vel pelo fechamento do
   * ResultSet e do seu Statement.</b>
   * @param connection Connection para ser utilizado na opera��o.
   * @param tableName Nome da tabela alvo da pesquisa.
   * @param fields Nomes dos campos que ser�o retornados.
   * @param orderFields Nomes dos que se deseja aplicar aos registros retornados.
   * @param where Condi��o que limita os registros que ser�o exibidos.
   * @param limit Limite de registros que se deseja retornar. <b>Esta propriedade 
   *               � espec�fica para PostgreSQL e MySQL. Para SQLServer e Oracle
   *               deve ser usada a fun��o RowNumber na cl�usula SELECT.</b>
   * @param offset Posi��o a partir da qual ser�o contados os registros que se
   *               deseja retornar. <b>Esta propriedade � espec�fica para PostgreSQL
   *               e MySQL. Para SQLServer e Oracle deve ser usada a fun��o
   *               RowNumber na cl�usula SELECT.</b>
   * @return Retorna um PreparedStatement para uma consulta na tabela
   *         informada por 'tableName', com os campos informados por 'fields', na
   *         ordem informada por 'orderFields' que atendam a condi��o informada
   *         por 'where'. <b>O m�todo que faz a chamada a select � respons�vel pelo
   *         fechamento do ResultSet e do seu Statement.</b>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public PreparedStatement prepareSelect(Connection connection,
                                                String     tableName,
                                                String[]   fields,
                                                String[]   orderFields,
                                                String     where,
                                                int        limit,
                                                int        offset) throws Exception {
    // express�o SQL
    StringBuffer sql = new StringBuffer("SELECT ");
    // loop nos campos
    for (int i=0; i<fields.length; i++) {
      if (i>0)
        sql.append(",");
      sql.append(fields[i]);
    } // for
    // nome tabela
    sql.append(" FROM " + tableName);
    // temos where?
    if ((where != null) && (!where.equals("")))
      sql.append(" WHERE (" + where + ")");
    // loop nos campos de ordena��o
    if (orderFields.length > 0) {
      sql.append(" ORDER BY ");
      for (int i=0; i<orderFields.length; i++) {
        if (i>0)
          sql.append(",");
        sql.append(orderFields[i]);
      } // for
    } // if
    // pagina��o
    if (limit > 0) {
      sql.append(" LIMIT " + limit + " OFFSET " + offset);
    } // if
    // executa o SQL
    return prepare(connection, sql.toString());
  }

  /**
   * Retorna um PreparedStatement para atualiza��o de registros na tabela
   * informada por 'tableName', preenchendo os campos informados por 'fields' e
   * que atendam a condi��o estabelecida por 'where'.
   * <b>O m�todo que faz a chamada a prepareInsert � respons�vel pelo fechamento do
   * Statement.</b>
   * @param connection Connection para ser utilizado na opera��o.
   * @param tableName Nome da tabela alvo da altera��o.
   * @param fields Nomes dos campos cujos valores ser�o alterados.
   * @param where Express�o que limita os registros que ser�o atualizados.
   * @return Retorna um PreparedStatement para atualiza��o de registros na tabela
   * informada por 'tableName', preenchendo os campos informados por 'fields' e
   * que atendam a condi��o estabelecida por 'where'. <b>O m�todo que faz a
   * chamada a prepareInsert � respons�vel pelo fechamento do Statement.</b>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public PreparedStatement prepareUpdate(Connection connection,
                                                String     tableName,
                                                String[]   fields,
                                                String     where) throws Exception {
    return prepareUpdate(connection,
                         tableName,
                         fields,
                         null,
                         where);
  }

  /**
   * Retorna um PreparedStatement para atualiza��o de registros na tabela
   * informada por 'tableName', preenchendo os campos informados por 'fields' e
   * que atendam a condi��o estabelecida por 'where'.
   * <b>O m�todo que faz a chamada a prepareInsert � respons�vel pelo fechamento do
   * Statement.</b>
   * @param connection Connection para ser utilizado na opera��o.
   * @param tableName Nome da tabela alvo da altera��o.
   * @param fields Nomes dos campos cujos valores ser�o alterados.
   * @param values Valores para serem atribu�dos diretamente aos campos. Utilizado
   *               por exemplo em casos de campos constantes ou Sequences do
   *               Oracle. Deve estar na mesma ordem que 'fields' por�m n�o
   *               � necess�rio incluir um valor para todos os campos.
   * @param where Express�o que limita os registros que ser�o atualizados.
   * @return Retorna um PreparedStatement para atualiza��o de registros na tabela
   * informada por 'tableName', preenchendo os campos informados por 'fields' e
   * que atendam a condi��o estabelecida por 'where'.
   * <b>O m�todo que faz a chamada a prepareInsert � respons�vel pelo fechamento do
   * Statement.</b>
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  static public PreparedStatement prepareUpdate(Connection connection,
                                                String     tableName,
                                                String[]   fields,
                                                String[]   values,
                                                String     where) throws Exception {
    // express�o SQL
    StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET ");
    // loop nos campos
    for (int i=0; i<fields.length; i++) {
      // devemos colocar ',' ?
      if (i > 0)
        sql.append(",");
      // campo da vez
      sql.append(fields[i] + "=");
      // se temos um valor para o campo...usa-o
      if ((values != null) && (i < values.length))
        sql.append(values[i]);
      // se n�o...ser� um par�metro
      else
        sql.append("?");
    } // for
    // temos where?
    if ((where != null) && (!where.equals("")))
      sql.append(" WHERE (" + where + ")");
    // executa
    return prepare(connection, sql.toString());
  }

  /**
   * Salva o 'resultSet' em 'fileName' utilizando TAB como separador de valores
   * e RETURN + BREAK como separador de registros.
   * @param resultSet ResultSet para ser percorrido.
   * @param filename Nome do arquivo de destino.
   * @throws Exception Em caso de exce��o na tentativa de navegar pelo ResultSet
   *                   ou criar o arquivo de destino.
   */
  static public void saveResultSetToFile(ResultSet resultSet,
                                         String    fileName) throws Exception {
    saveResultSetToFile(resultSet, fileName, "\t", "\r\n");
  }

  /**
   * Salva o 'resultSet' em 'fileName' utilizando os separadores de valores
   * e de registros.
   * @param resultSet ResultSet para ser percorrido.
   * @param filename Nome do arquivo de destino.
   * @param valueSeparator Separador de valores de um mesmo registro.
   * @param recordSeparator Separador de registros.
   * @throws Exception Em caso de exce��o na tentativa de navegar pelo ResultSet
   *                   ou criar o arquivo de destino.
   */
  static public void saveResultSetToFile(ResultSet resultSet,
                                         String    filename,
                                         String    valueSeparator,
                                         String    recordSeparator) throws Exception {
    // salva em arquivo
    FileOutputStream out = new FileOutputStream(filename, false);
    // loop nos registros
    while (resultSet.next()) {
      // loop nos nossos campos
      for (int i=1; i<=resultSet.getMetaData().getColumnCount(); i++) {
        // adiciona o separador
        if (i > 1)
          out.write(valueSeparator.getBytes());
        // adiciona o valor
        out.write(resultSet.getString(i).getBytes());
      } // for
      // separador de registro
      out.write(recordSeparator.getBytes());
    } // while
    // fecha o arquivo
    out.close();
  }
  
}
