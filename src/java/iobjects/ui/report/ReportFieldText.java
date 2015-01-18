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
package iobjects.ui.report;

import java.sql.*;

import iobjects.entity.*;

/**
 * Representa um controle de exibição de texto em um relatório associado a um
 * campo.
 * @since 2006
 */
public class ReportFieldText {

  private ReportFieldText() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportFieldText.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @param entityInfo EntityInfo que mantém as informações de 'entityField'.
   * @return String Retorna o script HTML contendo a representação do ReportFieldText.
   * @throws Exception Em caso de exceção na tentativa de obtenção ou na
   *                   formatação do valor do campo.
   */
  static public String script(EntityField entityField,
                              EntityInfo  entityInfo) throws Exception {
    return script(entityField, entityInfo, "", "");
  }

  /**
   * Retorna o script HTML contendo a representação do ReportFieldText.
   * @param entityField EntityField referente ao campo que se deseja representar.
   *                    O nome da coluna no ResultSet deverá estar na forma
   *                    'tableName.fieldName', onde 'tableName' é a propriedade
   *                    de mesmo nome em 'entity' e 'fieldName' é a propriedade
   *                    de mesmo nome em 'entityField'.
   * @param entity Entity que mantém 'entityField' e cuja propriedade 'tableName'
   *               será utilizada para formar o nome da coluna no ResultSet.
   * @param resultSet ResultSet contendo os dados para serem exibidos.
   * @return String Retorna o script HTML contendo a representação do ReportFieldText.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de dados
   *                   ou na formatação do valor do campo.
   */
  static public String script(EntityField entityField,
                              Entity      entity,
                              ResultSet   resultSet) throws Exception {
    return script(entityField, entity, resultSet, "", "");
  }

  /**
   * Retorna o script HTML contendo a representação do ReportFieldText.
   * @param entityField EntityField referente ao campo que se deseja representar.
   * @param entityInfo EntityInfo que mantém as informações de 'entityField'.
   * @param id String Identificação do ReportFieldText na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportFieldText.
   * @throws Exception Em caso de exceção na tentativa de obtenção ou na
   *                   formatação do valor do campo.
   */
  static public String script(EntityField entityField,
                              EntityInfo  entityInfo,
                              String      id,
                              String      style) throws Exception {
    // valor do campo formatado
    String fieldValue = Entity.formatFieldValue(entityField, entityInfo.getPropertyValue(entityField.getFieldAlias()));
    // retorna
    return script(fieldValue, id, style);
  }

  /**
   * Retorna o script HTML contendo a representação do ReportFieldText.
   * @param entityField EntityField referente ao campo que se deseja representar.
   *                    O nome da coluna no ResultSet deverá estar na forma
   *                    'tableName.fieldName', onde 'tableName' é a propriedade
   *                    de mesmo nome em 'entity' e 'fieldName' é a propriedade
   *                    de mesmo nome em 'entityField'.
   * @param entity Entity que mantém 'entityField' e cuja propriedade 'tableName'
   *               será utilizada para formar o nome da coluna no ResultSet.
   * @param resultSet ResultSet contendo os dados para serem exibidos.
   * @param id String Identificação do ReportFieldText na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportFieldText.
   * @throws Exception Em caso de exceção na tentativa de acesso ao banco de dados
   *                   ou na formatação do valor do campo.
   */
  static public String script(EntityField entityField,
                              Entity      entity,
                              ResultSet   resultSet,
                              String      id,
                              String      style) throws Exception {
    // nome do campo
    String fieldName = entityField.getFieldName(entity != null ? entity.getTableName() : "");
    // valor do campo formatado
    String fieldValue = Entity.formatFieldValue(entityField, resultSet.getObject(fieldName));
    // retorna
    return script(fieldValue, id, style);
  }

  /**
   * Retorna o script HTML contendo a representação do ReportFieldText.
   * @param fieldValue Valor do ReportFieldText na página.
   * @param id String Identificação do ReportFieldText na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportFieldText.
   */
  static private String script(String fieldValue,
                               String id,
                               String style) {
    // retorna
    return "<span id=\"" + id + "\" "
         +       "class=\"FieldText\" "
         +       "style=\"" + style + "\">"
         + fieldValue
         + "</span> \r";
  }

}
