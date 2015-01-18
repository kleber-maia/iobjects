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
 * lookup.
 * @since 2006
 */
public class ReportLookupText {

  private ReportLookupText() {
  }

  /**
   * Retorna o script HTML contendo a representação do ReportLookupText.
   * @param entityLookup EntityLookup referente ao lookup que se deseja representar.
   * @param entityInfo EntityInfo que mantém as informações de 'entityLookup'.
   * @return String Retorna o script HTML contendo a representação do ReportLookupText.
   * @throws Exception Em caso de exceção na tentativa de obtenção ou na
   *                   formatação do valor do campo.
   */
  static public String script(EntityLookup entityLookup,
                              EntityInfo   entityInfo) throws Exception {
    return script(entityLookup, entityInfo, "", "");
  }

  /**
   * Retorna o script HTML contendo a representação do ReportLookupText.
   * @param entityLookup EntityLookup referente ao lookup que se deseja representar.
   * @param entityInfo EntityInfo que mantém as informações de 'entityLookup'.
   * @param id String Identificação do ReportLookupText na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportLookupText.
   * @throws Exception Em caso de exceção na tentativa de obtenção ou na
   *                   formatação do valor do campo.
   */
  static public String script(EntityLookup entityLookup,
                              EntityInfo   entityInfo,
                              String       id,
                              String       style) throws Exception {
    // valor do campo formatado
    String fieldValue = entityInfo.lookupValueList().get(entityLookup).getDisplayFieldValuesToString();
    // retorna
    return script(fieldValue, id, style);
  }

  /**
   * Retorna o script HTML contendo a representação do ReportLookupText.
   * @param fieldValue Valor do ReportLookupText na página.
   * @param id String Identificação do ReportLookupText na página.
   * @param style String Estilo para modificação do elemento HTML apresentado.
   * @return String Retorna o script HTML contendo a representação do ReportLookupText.
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
