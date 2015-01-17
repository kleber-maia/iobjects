package iobjects.report;

import java.sql.*;

import iobjects.*;
import iobjects.util.*;

/**
 * Representa a classe base de todas as classes que representam relat�rios
 * na aplica��o.
 */
public class Report extends BusinessObject {

  /**
   * Construtor padr�o.
   */
  public Report() {
  }

  /**
   * Executa a express�o 'sql' informada e retorna true em caso de sucesso.
   * @param sql Express�o SQL para ser executada.
   * @return Retorna true em caso de sucesso.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  protected boolean execute(String sql) throws Exception {
    return SqlTools.execute(getConnection(), sql);
  }

  /**
   * Retorna um ResultSet representando o resultado da execu��o da express�o
   * SQL informada. O m�todo que faz a chamada a este deve ser respons�vel
   * pelo fechamento do ResultSet e do seu Statement.
   * @param sql Express�o SQL que se deseja executar.
   * @return Retorna um ResultSet representando o resultado da execu��o da
   *         express�o SQL informada. O m�todo que faz a chamada a executeQuery
   *         � respons�vel pelo fechamento do ResultSet e do seu Statement.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  protected ResultSet executeQuery(String sql) throws Exception {
    return SqlTools.executeQuery(getConnection(), sql);
  }

  /**
   * Retorna um PreparedStatement para execu��o da express�o 'sql' informada.
   * O m�todo que faz a chamada a prepare � respons�vel pelo fechamento do
   * Statement.
   * @param sql Express�o SQL para ser preparada.
   * @return Retorna um PreparedStatement para execu��o da express�o 'sql'
   *         informada. O m�todo que faz a chamada a prepare � respons�vel pelo
   *         fechamento do Statement.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
   *                   dados.
   */
  protected PreparedStatement prepare(String sql) throws Exception {
    return SqlTools.prepare(getConnection(), sql);
  }

  /**
   * Retorna um PreparedStatement para uma consulta na tabela
   * informada por 'tableName', com os campos informados por 'fields', na ordem
   * informada por 'orderFields' que atendam a condi��o informada por 'where'.
   * O m�todo que faz a chamada a select � respons�vel pelo fechamento do
   * ResultSet e do seu Statement.
   * @param tableName Nome da tabela alvo da pesquisa.
   * @param fields Nomes dos campos que ser�o retornados.
   * @param orderFields Nomes dos que se deseja aplicar aos registros retornados.
   * @param where Condi��o que limita os registros que ser�o exibidos.
   * @return Retorna um PreparedStatement para uma consulta na tabela
   *         informada por 'tableName', com os campos informados por 'fields', na
   *         ordem informada por 'orderFields' que atendam a condi��o informada
   *         por 'where'. O m�todo que faz a chamada a select � respons�vel pelo
   *         fechamento do ResultSet e do seu Statement.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de
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
