package iobjects.remote.entity;

import java.sql.*;

import iobjects.*;
import iobjects.entity.*;
import iobjects.util.*;

/**
 * Utilit�rio capaz de gerar pacotes de dados a partir de Entity's selecionados
 * para sincroniza��o de aplica��es clientes.
 * <p>
 * Esta classe deve ser utilizada no servidor para serializa��o e compacta��o
 * dos dados exportados. A aplica��o cliente por sua vez realiza o
 * download e a importa��o para a base de dados locais utilizando um EntityImport.
 * </p>
 * <p>
 * Esta classe � convenientemente descendente de BusinessObjects de modo a
 * poder ser localizada pelo servlet RemoteServer.
 * </p>
 */
public class EntityExport extends BusinessObject {

  static final String METHOD_EXPORT_DATA = "exportData";

  /**
   * Construtor padr�o.
   */
  public EntityExport() {
  }

  /**
   * Retorna um EntityExportDataset contendo o pacote de dados proveniente
   * do Entity informados por 'importExportInfo'.
   * @param importExportInfo EntityImportExportInfo contendo as prefer�ncias de
   *                         exporta��o dos dados desejados.
   * @return EntityExportDataset Retorna um EntityExportDataset contendo
   *         o pacote de dados proveniente do Entity informado por
   *         'importExportInfo'.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de dados.
   */
  public EntityExportDataset exportData(EntityImportExportInfo importExportInfo) throws Exception {
    // obt�m o Entity desejado
    Entity entity = getFacade().getEntity(importExportInfo.getExportEntityClassName());
    // dados desejados
    PreparedStatement statement = null;
    ResultSet         resultSet = null;
    try {
      // se a origem dos dados � o Entity...
      if (importExportInfo.getExportSelect() == EntityImportExportInfo.EXPORT_SELECT_FROM_ENTITY)
        statement = entity.prepareSelect(importExportInfo.getExportFilter());
      // se a origem � a tabela...
      else
        statement = SqlTools.prepareSelect(entity.getConnection(),
                                           entity.getTableName(),
                                           new String[]{"*"},
                                           new String[0],
                                           importExportInfo.getExportFilter());
      // executa
      statement.execute();
      // pacote de dados de resultado
      EntityExportDataset result = new EntityExportDataset(importExportInfo);

      // dados obtidos
      resultSet = statement.getResultSet();
      // loop nos registros
      while (resultSet.next()) {
        // registro da vez
        Object[] record = new Object[resultSet.getMetaData().getColumnCount()];
        // obt�m seus dados
        for (int w=0; w<record.length; w++)
          record[w] = resultSet.getObject(w+1);
        // adiciona no pacote de dados
        result.add(record);
      } // while

      // preenche a estrutura dos dados
      for (int i=1; i<=resultSet.getMetaData().getColumnCount(); i++) {
        result.metaData().add(resultSet.getMetaData().getColumnName(i),
                              resultSet.getMetaData().getColumnType(i),
                              resultSet.getMetaData().getColumnDisplaySize(i));
      } // for

      // retorna
      return result;
    }
    finally {
      if (resultSet != null)
        resultSet.close();
      if (statement != null)
        statement.close();
    } // try-finally
  }

  /**
   * Retorna um EntityExportDatasetList contendo os pacotes de dados provenientes
   * dos Entity's informados por 'importExportInfoList'.
   * @param importExportInfoList EntityExportInfoList contendo as prefer�ncias de
   *                             exporta��o dos dados desejados.
   * @return EntityExportDatasetList Retorna um EntityExportDatasetList contendo
   *         os pacotes de dados provenientes dos Entity's informados por
   *         'importExportInfoList'.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao banco de dados.
   */
  public EntityExportDatasetList exportData(EntityImportExportInfoList importExportInfoList) throws Exception {
    // nosso resultado
    EntityExportDatasetList result = new EntityExportDatasetList();
    // loop nas informa��es de exporta��o
    for (int i=0; i<importExportInfoList.size(); i++) {
      // informa��o de importa��o e exporta��o da vez
      EntityImportExportInfo importExportInfo = importExportInfoList.get(i);
      // adiciona o pacote de dados no nosso resultado
      result.add(exportData(importExportInfo));
    } // for
    // retorna
    return result;
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
  }

}
