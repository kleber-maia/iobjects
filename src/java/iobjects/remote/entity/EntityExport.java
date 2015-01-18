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
package iobjects.remote.entity;

import java.sql.*;

import iobjects.*;
import iobjects.entity.*;
import iobjects.util.*;

/**
 * Utilitário capaz de gerar pacotes de dados a partir de Entity's selecionados
 * para sincronização de aplicações clientes.
 * <p>
 * Esta classe deve ser utilizada no servidor para serialização e compactação
 * dos dados exportados. A aplicação cliente por sua vez realiza o
 * download e a importação para a base de dados locais utilizando um EntityImport.
 * </p>
 * <p>
 * Esta classe é convenientemente descendente de BusinessObjects de modo a
 * poder ser localizada pelo servlet RemoteServer.
 * </p>
 */
public class EntityExport extends BusinessObject {

  static final String METHOD_EXPORT_DATA = "exportData";

  /**
   * Construtor padrão.
   */
  public EntityExport() {
  }

  /**
   * Retorna um EntityExportDataset contendo o pacote de dados proveniente
   * do Entity informados por 'importExportInfo'.
   * @param importExportInfo EntityImportExportInfo contendo as preferências de
   *                         exportação dos dados desejados.
   * @return EntityExportDataset Retorna um EntityExportDataset contendo
   *         o pacote de dados proveniente do Entity informado por
   *         'importExportInfo'.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de dados.
   */
  public EntityExportDataset exportData(EntityImportExportInfo importExportInfo) throws Exception {
    // obtém o Entity desejado
    Entity entity = getFacade().getEntity(importExportInfo.getExportEntityClassName());
    // dados desejados
    PreparedStatement statement = null;
    ResultSet         resultSet = null;
    try {
      // se a origem dos dados é o Entity...
      if (importExportInfo.getExportSelect() == EntityImportExportInfo.EXPORT_SELECT_FROM_ENTITY)
        statement = entity.prepareSelect(importExportInfo.getExportFilter());
      // se a origem é a tabela...
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
        // obtém seus dados
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
   * @param importExportInfoList EntityExportInfoList contendo as preferências de
   *                             exportação dos dados desejados.
   * @return EntityExportDatasetList Retorna um EntityExportDatasetList contendo
   *         os pacotes de dados provenientes dos Entity's informados por
   *         'importExportInfoList'.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de dados.
   */
  public EntityExportDatasetList exportData(EntityImportExportInfoList importExportInfoList) throws Exception {
    // nosso resultado
    EntityExportDatasetList result = new EntityExportDatasetList();
    // loop nas informações de exportação
    for (int i=0; i<importExportInfoList.size(); i++) {
      // informação de importação e exportação da vez
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
