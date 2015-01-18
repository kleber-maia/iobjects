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

import iobjects.remote.*;

/**
 * Representa as informações e preferências de importação e exportação de dados
 * de um Entity.
 */
public class EntityImportExportInfo implements Serializable {

  /**
   * Tipo de exportação que utiliza o método 'prepareSelect()' do Entity. Desta
   * forma são exportados os dados referentes aos EntityField's e EntityLookup's
   * configurados.
   */
  static public final byte EXPORT_SELECT_FROM_ENTITY = 0;
  /**
   * Tipo de exportação que gera os dados diretamente a partir da tabela física
   * do banco de dados aos qual o Entity está configurado.
   */
  static public final byte EXPORT_SELECT_FROM_TABLE  = 1;

  private String exportEntityClassName = "";
  private String exportFilter          = "";
  private byte   exportSelect          = 0;
  private String importEntityClassName = "";

  /**
   * Construtor estendido. Não deve ser chamado pela aplicação.
   */
  public EntityImportExportInfo() {
  }

  /**
   * Construtor padrão.
   * @param importEntityClassName String Nome da classe do Entity de destino
   *                              dos dados param serem importados.
   * @param exportEntityClassName String Nome da classe do Entity de origem
   *                              dos dados para serem exportados.
   * @param exportFilter String Expressão SQL que limita os registros para serem
   *                            exportados.
   * @param exportSelect byte Tipo de seleção dos dados para serem exportados.
   */
  public EntityImportExportInfo(String importEntityClassName,
                                String exportEntityClassName,
                                String exportFilter,
                                byte   exportSelect) {
    this.importEntityClassName = importEntityClassName;
    this.exportEntityClassName = exportEntityClassName;
    this.exportFilter = exportFilter;
    this.exportSelect = exportSelect;
  }

  public void deserialize(SerializeReader reader) throws IOException {
    // lê nossas propriedades
    exportEntityClassName = reader.readString();
    exportFilter          = reader.readString();
    exportSelect          = reader.readByte();
    importEntityClassName = reader.readString();
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    importEntityClassName  = null;
    exportFilter           = null;
    exportEntityClassName  = null;
  }

  /**
   * Retorna o nome da classe do Entity de origem dos dados para serem exportados.
   * @return String Retorna o nome da classe do Entity de origem dos dados para
   *                serem exportados.
   */
  public String getExportEntityClassName() {
    return exportEntityClassName;
  }

  /**
   * Retorna a expressão SQL que limita os registros para serem exportados.
   * @return String Retorna a expressão SQL que limita os registros para serem
   *                exportados.
   */
  public String getExportFilter() {
    return exportFilter;
  }

  /**
   * Retorna o tipo de seleção dos dados para serem exportados.
   * @return byte Retorna o tipo de seleção dos dados para serem exportados.
   */
  public byte getExportSelect() {
    return exportSelect;
  }

  /**
   * Retorna o nome da classe do Entity de destino dos dados para serem importados.
   * @return String Retorna o nome da classe do Entity de destino dos dados para
   *                serem importados.
   */
  public String getImportEntityClassName() {
    return importEntityClassName;
  }

  public void serialize(SerializeWriter writer) throws IOException {
    // escreve nossas propriedades
    writer.writeString(exportEntityClassName);
    writer.writeString(exportFilter);
    writer.writeByte(exportSelect);
    writer.writeString(importEntityClassName);
  }

  /**
   * Define a expressão SQL que limita os registros para serem exportados.
   * @param exportFilter String Expressão SQL que limita os registros para
   *                            serem exportados.
   */
  public void setExportFilter(String exportFilter) {
    this.exportFilter = exportFilter;
  }

  /**
   * Define o tipo de seleção dos dados para serem exportados.
   * @param exportSelect byte Tipo de seleção dos dados para serem exportados.
   */
  public void setExportSelect(byte exportSelect) {
    this.exportSelect = exportSelect;
  }

}
