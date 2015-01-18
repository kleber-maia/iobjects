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

import iobjects.remote.*;

/**
 * Utilitário capaz de receber pacotes de dados a partir de Entity's existentes
 * no servidor para sincronização de aplicações clientes.
 * <p>
 * Esta classe deve ser utilizada na aplicação cliente para deserialização e
 * descompactação dos dados importados. O servidor por sua vez realiza o
 * a exportação dos dados utilizando um EntityExport.
 * </p>
 */
public class EntityImport {

  static public void main(String[] args) {
    try {
      // configura o RemoteServer
      RemoteServer.setDefaultConnectionName("geracoes");
      RemoteServer.setUsername("Administrador");
      RemoteServer.setPassword("administrador");
      RemoteServer.setServerURL("http://localhost:8080/iobjects");
      RemoteServer.setWorkConfiguration("imanager");
      // configura o Import
      EntityImport entityImport = new EntityImport();
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "securityservice.entity.Usuario", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "securityservice.entity.Papel", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "securityservice.entity.PapelUsuario", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "securityservice.entity.PapelAcao", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "securityservice.entity.PapelRelacaoMestre", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "imanager.entity.Empresa", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "imanager.entity.ConfiguracaoFiscal", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "imanager.entity.Favorecido", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "imanager.entity.Produto", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "imanager.entity.ProdutoEmpresa", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "imanager.entity.Unidade", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "imanager.entity.FormaPagamento", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));
      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "imanager.entity.TabelaPreco", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));

      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "imanager.entity.ProdutoEmpresaEstatisticaDiaria", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));

      entityImport.importExportInfoList().add(new EntityImportExportInfo("", "imanager.entity.TabelaPrecoProduto", "", EntityImportExportInfo.EXPORT_SELECT_FROM_TABLE));

      System.out.println("Importando dados...");
      EntityExportDatasetList datasetList = entityImport.importData();

      for (int k=0; k<datasetList.size(); k++) {
        EntityExportDataset dataset = datasetList.get(k);
        // *
        System.out.println("");
        System.out.println("Estrutura de " + dataset.getImportExportInfo().getExportEntityClassName() + "...");
        for (int i=0; i<dataset.metaData().getFieldCount(); i++)
          System.out.println("Campo " + (i+1) + ": " + dataset.metaData().getFieldName(i) + " (type: " + dataset.metaData().getFieldType(i) + ", size: " + dataset.metaData().getFieldSize(i) + ")");
        // *
        System.out.println("");
        System.out.println("Dados de " + dataset.getImportExportInfo().getExportEntityClassName() + "...");
        for (int i=0; i<dataset.size(); i++) {
          Object[] record = dataset.get(i);
          System.out.print("Registro " + (i+1) + ": ");
          for (int w=0; w<record.length; w++) {
            System.out.print(record[w] + ";");
          } // for w
          System.out.println("");
        } // for i
      } // for k
    }
    catch (Exception e) {
      e.printStackTrace();
    } // try-catch
  }






  private EntityImportExportInfoList importExportInfoList = new EntityImportExportInfoList();

  /**
   * Construtor padrão.
   */
  public EntityImport() {
  }

  /**
   * Retorna as informações de importação e exportação utilizadas pelo método
   * 'importData()'.
   * @return EntityExportInfoList Retorna as informações de importação e exportação
   *         utilizadas pelo método 'importData()'.
   */
  public EntityImportExportInfoList importExportInfoList() {
    return importExportInfoList;
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    importExportInfoList.clear();
    importExportInfoList = null;
  }

  /**
   * Realiza a importação dos dados a partir das informações e preferências
   * contidas na propriedade 'importExportInfoList' e retorna um
   * EntityExportDatasetList contendo os dados recebidos.
   * @return Realiza a importação dos dados a partir das informações e
   *         preferências contidas na propriedade 'importExportInfoList' e
   *         retorna um EntityExportDatasetList contendo os dados recebidos.
   * @throws Exception Em caso de exceção no servidor durante a exportação dos
   *                   dados ou de exceção durante a importação dos mesmos.
   */
  public EntityExportDatasetList importData() throws Exception {
    return (EntityExportDatasetList)RemoteServer.callRemoteMethod(EntityExport.class.getName(),
                                                                  EntityExport.METHOD_EXPORT_DATA,
                                                                  new Object[]{importExportInfoList});
  }

  /**
   * Realiza a importação dos dados a partir das informações e preferências
   * contidas em 'importExportInfo' e retorna um EntityExportDataset contendo
   * os dados recebidos.
   * @param importExportInfo EntityImportExportInfo
   * @return EntityExportDataset Realiza a importação dos dados a partir das
   *                             informações e preferências contidas em
   *                             'importExportInfo' e retorna um
   *                             EntityExportDataset contendo os dados
   *                             recebidos.
   * @throws Exception Em caso de exceção no servidor durante a exportação dos
   *                   dados ou de exceção durante a importação dos mesmos.
   */
  public EntityExportDataset importData(EntityImportExportInfo importExportInfo) throws Exception {
    return (EntityExportDataset)RemoteServer.callRemoteMethod(EntityExport.class.getName(),
                                                              EntityExport.METHOD_EXPORT_DATA,
                                                              new Object[]{importExportInfo});
  }

}
