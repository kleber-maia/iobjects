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

import java.io.IOException;
import java.util.*;

import iobjects.*;
import iobjects.remote.*;

/**
 * Representa um pacote de dados exportado a partir de um Entity.
 */
public class EntityExportDataset implements Serializable {

  /**
   * Mantém informações sobre a estrutura do pacote de dados.
   */
  public class MetaData implements Serializable {
    private Vector fieldNames = new Vector();
    private Vector fieldTypes = new Vector();
    private Vector fieldSizes = new Vector();
    // *
    public MetaData() {}
    // *
    public void   add(String fieldName, int fieldType, int fieldSize) { fieldNames.add(fieldName); fieldTypes.add(new Integer(fieldType)); fieldSizes.add(new Integer(fieldSize)); }
    public int    getFieldCount() { return fieldNames.size(); }
    public String getFieldName(int index) { return (String)fieldNames.elementAt(index); }
    public int    getFieldSize(int index) { return ((Integer)fieldSizes.elementAt(index)).intValue(); }
    public int    getFieldType(int index) { return ((Integer)fieldTypes.elementAt(index)).intValue(); }
    // *
    public void deserialize(SerializeReader reader) throws IOException {
      int fieldCount = reader.readInt();
      for (int i=0; i<fieldCount; i++)
        add(reader.readString(), reader.readInt(), reader.readInt());
    }
    // *
    public void serialize(SerializeWriter writer) throws IOException {
      writer.writeInt(getFieldCount());
      for (int i=0; i<getFieldCount(); i++) {
        writer.writeString(getFieldName(i));
        writer.writeInt(getFieldType(i));
        writer.writeInt(getFieldSize(i));
      } // for
    }
  }

  private EntityImportExportInfo importExportInfo = null;
  private MetaData               metaData         = new MetaData();
  private Vector                 recordList       = new Vector();

  /**
   * Construtor estendido. Não deve ser chamado pela aplicação.
   */
  public EntityExportDataset() {
  }

  /**
   * Construtor padrão.
   * @param importExportInfo Informações de importação e exportação utilizados
   *                         para geração do pacote de dados.
   */
  public EntityExportDataset(EntityImportExportInfo importExportInfo) {
    this.importExportInfo = importExportInfo;
  }

  /**
   * Adiciona 'record' aos registros.
   * @param record Object[] Registro que se deseja adicionar.
   */
  public void add(Object[] record) {
    recordList.add(record);
  }

  /**
   * Apaga todos os registros.
   */
  public void clear() {
    recordList.clear();
  }

  public void deserialize(SerializeReader reader) throws IOException {
    // limpa nossos registros
    clear();
    // informações de importação e exportação
    importExportInfo = new EntityImportExportInfo();
    importExportInfo.deserialize(reader);
    // informações da estrutura dos dados
    metaData = new MetaData();
    metaData.deserialize(reader);
    // quantidade de registros
    int recordListSize = reader.readInt();
    // deserializa os registros
    for (int i=0; i<recordListSize; i++) {
      // quantidade de dados
      int recordSize = reader.readInt();
      // registro da vez
      Object[] record = new Object[recordSize];
      // lê os dados
      for (int w=0; w<recordSize; w++)
        record[w] = reader.readObject();
      // adiciona
      add(record);
    } // for
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    clear();
    recordList       = null;
    importExportInfo = null;
    metaData         = null;
  }

  /**
   * Retorna o registro referenciado pelo índice informado.
   * @param index int Índice do registro que se deseja obter.
   * @return Object[] Retorna o registro referenciado pelo índice informado.
   */
  public Object[] get(int index) {
    return (Object[])recordList.elementAt(index);
  }

  /**
   * Retorna as informações de importação e exportação utilizados para geração
   * do pacote de dados.
   * @return Retorna as informações de importação e exportação utilizados para
   *         geração do pacote de dados.
   */
  public EntityImportExportInfo getImportExportInfo() {
    return importExportInfo;
  }

  /**
   * Retorna as informações sobre a estrutura do pacote de dados.
   * @return MetaData Retorna as informações sobre a estrutura do pacote de dados.
   */
  public MetaData metaData() {
    return metaData;
  }

  public void serialize(SerializeWriter writer) throws IOException {
    // informações de importação e exportação
    importExportInfo.serialize(writer);
    // informações da estrutura dos dados
    metaData.serialize(writer);
    // quantidade de registros
    writer.writeInt(size());
    // loop nos registros
    for (int i=0; i<size(); i++) {
      // registro da vez
      Object[] record = get(i);
      // quantidade de dados
      writer.writeInt(record.length);
      // grava os dados
      for (int w=0; w<record.length; w++)
        writer.writeObject(record[w]);
    } // for
  }

  /**
   * Retorna a quantidade de registros.
   * @return int Retorna a quantidade de registros.
   */
  public int size() {
    return recordList.size();
  }

}
