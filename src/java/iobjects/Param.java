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
package iobjects;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import iobjects.util.*;
import iobjects.ui.*;
import iobjects.xml.*;
import iobjects.entity.EntityField;

/**
 * Representa um parâmetro cuja finalidade é armazenar valores de propriedades,
 * objetos e opções a fim de promover persistência de estado.
 * @since 3.1
 */
public class Param {

  static public final int ALIGN_LEFT   = Align.ALIGN_LEFT;
  static public final int ALIGN_CENTER = Align.ALIGN_CENTER;
  static public final int ALIGN_RIGHT  = Align.ALIGN_RIGHT;

  static public final int FORMAT_NONE       = Format.FORMAT_NONE;
  static public final int FORMAT_DATE       = Format.FORMAT_DATE;
  static public final int FORMAT_DATE_TIME  = Format.FORMAT_DATE_TIME;
  static public final int FORMAT_TIME       = Format.FORMAT_TIME;
  static public final int FORMAT_INTEGER    = Format.FORMAT_INTEGER;
  static public final int FORMAT_DOUBLE     = Format.FORMAT_DOUBLE;
  static public final int FORMAT_UPPER_CASE = Format.FORMAT_UPPER_CASE;

  /**
   * Mantém as configurações obtidas a partir do arquivo de configurações de
   * Params.
   * @see iobjects.xml.ParamFile
   */
  static private ParamFile paramFile = null;

  private boolean  accordingToMaskChars         = false;
  private boolean  accordingToMinimumLength     = false;
  private boolean  accordingToPeriod            = false;
  private int      align                        = ALIGN_LEFT;
  private Param    beginDateParam               = null;
  private String   caption                      = "";
  private String   description                  = "";
  private int      format                       = FORMAT_NONE;
  private String[] lookupList                   = {};
  private String   mask                         = "";
  private String   name                         = "";
  private Object   object                       = null;
  private int      precision                    = 2;
  private String   scriptConstraint             = "";
  private String   scriptConstraintErrorMessage = "";
  private int      size                         = 0;
  private String   value                        = "";
  private String[] valueAsArray                 = new String[]{""};

  /**
   * Construtor padrão.
   * @param name String Nome do parâmetro.
   * @param value String Valor inicial do parâmetro.
   */
  public Param(String name,
               String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Construtor padrão.
   * @param name String Nome do parâmetro.
   * @param object Object Objeto associado inicialmente ao parâmetro.
   */
  public Param(String name,
               Object object) {
    this.name   = name;
    this.object = object;
  }

  /**
   * Construtor estendido.
   * @param name String Nome do Param.
   * @param caption String Título do Param.
   * @param description String Decrição do Param.
   * @param value String Valor inicial do Param.
   */
  public Param(String name,
               String caption,
               String description,
               String value) {
    this.caption     = caption;
    this.description = description;
    this.name        = name;
    this.value       = value;
  }

  /**
   * Construtor estendido.
   * @param caption String Título do Param.
   * @param description String Descrição do Param.
   * @param name String Nome do Param.
   * @param value String Valor do Param.
   * @param size int Tamanho do Param.
   * @param align int Alinhamento do Param.
   * @param format int Formato de edição do Param.
   */
  public Param(String name,
               String caption,
               String description,
               String value,
               int    size,
               int    align,
               int    format) {
    // nossos valores
    this.caption = caption;
    this.description = description;
    this.name = name;
    this.value = value;
    this.size = size;
    this.align = align;
    this.format = format;
  }

  /**
   * Construtor estendido.
   * @param caption String Título do Param.
   * @param description String Descrição do Param.
   * @param name String Nome do Param.
   * @param value String Valor do Param.
   * @param size int Tamanho do Param.
   * @param align int Alinhamento do Param.
   * @param format int Formato de edição do Param.
   * @param mask String Máscara de edição do Param, ex.: 000.000.000-00.
   * @param scriptConstraint String Script de validação do Param.
   * @param scriptConstraintErrorMessage String Mensagem de validação do Param.
   */
  public Param(String name,
               String caption,
               String description,
               String value,
               int    size,
               int    align,
               int    format,
               String mask,
               String scriptConstraint,
               String scriptConstraintErrorMessage) {
    // nossos valores
    this.caption = caption;
    this.description = description;
    this.name = name;
    this.value = value;
    this.size = size;
    this.align = align;
    this.format = format;
    this.mask = mask;
    this.scriptConstraint = scriptConstraint;
    this.scriptConstraintErrorMessage = scriptConstraintErrorMessage;
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    beginDateParam               = null;
    caption                      = null;
    description                  = null;
    lookupList                   = null;
    mask                         = null;
    name                         = null;
    object                       = null;
    scriptConstraint             = null;
    scriptConstraintErrorMessage = null;
    value                        = null;
  }

  public boolean getAccordingToMaskChars() {
    return accordingToMaskChars;
  }

  public boolean getAccordingToMinimumLength() {
    return accordingToMinimumLength;
  }

  public boolean getAccordingToPeriod() {
    return accordingToPeriod;
  }

  public int getAlign() {
    return align;
  }

  /**
   * Retorna a lista de valores do Param obtidas a partir da requisição ou do
   * método setArrayValue().
   * Caso o valor obtido da requisição seja um valor simples, será retornado um
   * String[] com apenas 1 elemento e cujo valor é o mesmo retornado pelo método
   * getValue().
   * @return String[] Retorna a lista de valores do Param obtidas a partir da
   *                  requisição ou do método setArrayValue(). Caso o valor
   *                  obtido da requisição seja um valor simples, será retornado
   *                  um String[] com apenas 1 elemento e cujo valor é o mesmo
   *                  retornado pelo método getValue().
   */
  public String[] getArrayValue() {
    return valueAsArray;
  }

  public Param getBeginDateParam() {
    return beginDateParam;
  }

  public String getCaption() {
    return caption;
  }

  public String getDescription() {
    return description;
  }

  /**
   * Retorna o valor do Param interpretado como uma data. O valor informado
   * deve estar no formato dd/mm/yyyy.
   * @since 2006
   * @throws Exception Em caso de exceção na tentativa de interpretar o valor
   *                   recebido.
   * @return Timestamp Retorna o valor do Param interpretado como uma data.
   *                   O valor informado deve estar no formato dd/mm/yyyy.
   */
  public Timestamp getDateValue() throws Exception {
    return DateTools.parseDate(value);
  }

  /**
   * Retorna o valor do Param interpretado como uma data e hora. O valor informado
   * deve estar no formato dd/mm/yyyy hh:mm.
   * @since 2006
   * @throws Exception Em caso de exceção na tentativa de interpretar o valor
   *                   recebido.
   * @return Timestamp Retorna o valor do Param interpretado como uma data.
   *                   O valor informado deve estar no formato dd/mm/yyyy hh:mm.
   */
  public Timestamp getDateTimeValue() throws Exception {
    return DateTools.parseDateTime(value);
  }

  /**
   * Retorna o valor do Param interpretado como um double.
   * @since 2006
   * @throws Exception Em caso de exceção na tentativa de interpretar o valor
   *                   recebido ou a quantidade de casas decimais exceder o
   *                   máximo permitido.
   * @return double Retorna o valor do Param interpretado como um double.
   */
  public double getDoubleValue() throws Exception {
    return NumberTools.parseDouble(value);
  }

  /**
   * Retorna o valor do Param interpretado como um double.
   * @since 2006
   * @param maxFractionDigits int Quantidade máxima de casas decimais aceitáveis.
   * @throws Exception Em caso de exceção na tentativa de interpretar o valor
   *                   recebido ou a quantidade de casas decimais exceder o
   *                   máximo permitido.
   * @return double Retorna o valor do Param interpretado como um double.
   */
  public double getDoubleValue(int maxFractionDigits) throws Exception {
    return NumberTools.parseDouble(value, maxFractionDigits);
  }

  public int getFormat() {
    return format;
  }

  /**
   * Retorna o valor do Param formatado de acordo com a definição de formato de
   * edição configurado.
   * @throws Exception Em caso de exceção na tentativa de formatar o valor
   *                   do Param.
   * @return String Retorna o valor do Param formatado de acordo com a definição
   *                de formato de edição configurado.
   */
  public String getFormatedValue() throws Exception {
    // se recebemos null...exceção
    if (value == null)
      throw new ExtendedException(getClass().getName(), "getFormatedValue", "null não é um valor de parâmetro válido.");
    // se temos uma lista lookup...
    if (lookupList.length > 0) {
      // o valor do campo deve corresponder ao índice da lista de pesquisa
      int lookupIndex = getIntValue();
      // retorna o valor correspondente na lista de pesquisa
      return lookupList[lookupIndex];
    }
    // se não temos uma lista lookup...
    else {
      // se temos uma máscara...
      if (!mask.equals(""))
        return StringTools.formatCustomMask(value, mask);
      // se temos um formato...
      switch (format) {
        case FORMAT_DATE:
          return DateTools.formatDate(getDateValue());
        case FORMAT_DATE_TIME:
          return DateTools.formatDateTime(getDateTimeValue());
        case FORMAT_TIME:
          return DateTools.formatTime(getDateTimeValue());
        case FORMAT_DOUBLE:
          return NumberTools.format(getDoubleValue(), getPrecision(), getPrecision());
        case FORMAT_INTEGER:
          return NumberTools.format(getIntValue());
        case FORMAT_UPPER_CASE:
          return getValue().toUpperCase();
        default:
          return getValue();
      } // switch
    } // if
  }

  /**
   * Retorna o valor do Param interpretado como um int.
   * @since 2006
   * @throws Exception Em caso de exceção na tentativa de interpretar o valor
   *                   recebido.
   * @return int Retorna o valor do Param interpretado como um int.
   */
  public int getIntValue() throws Exception {
    return NumberTools.parseInt(value);
  }

  /**
   * Retorna a lista de valores de pesquisa do Param.
   * @return String[] Retorna a lista de valores de pesquisa do Param.
   */
  public String[] getLookupList() {
    return lookupList;
  }

  public String getMask() {
    return mask;
  }

  public String getName() {
    return name;
  }

  public Object getObject() {
    return object;
  }

  /**
   * Retorna as configurações obtidas a partir do arquivo de configurações de
   * Params.
   * @return ParamFile Retorna as configurações obtidas a partir do arquivo de
   *         configurações de Params.
   * @see iobjects.xml.ParamFile
   */
  static public ParamFile getParamFile() {
    return paramFile;
  }

  /**
   * Retorna a precisão do Param. Utilizado apenas quando a propriedade "format"
   * é igual a FORMAT_DOUBLE.
   * @return int Retorna a precisão do Param. Utilizado apenas quando a
   *             propriedade "format" é igual a FORMAT_DOUBLE.
   */
  public int getPrecision() {
    return precision;
  }

  public String getScriptConstraint() {
    return scriptConstraint;
  }

  public String getScriptConstraintErrorMessage() {
    return scriptConstraintErrorMessage;
  }

  public int getSize() {
    return size;
  }

  public String getValue() {
    return value;
  }

  public void setAlign(int align) {
    this.align = align;
  }

  /**
   * Define a lista de valores do Param. Os valores definidos a partir deste
   * método devem ser lidos a partir do método getArrayValue().
   * @param valueAsArray String[] Lista de valores do Param.
   */
  public void setArrayValue(String[] valueAsArray) {
    this.valueAsArray = valueAsArray;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  /**
   * Define o uso das constraints especiais para o Param de acordo com o arquivo
   * de configurações de parâmetros.
   * @param accordingToMaskChars boolean True para que seja verificado se o valor
   *                             informado no Param está em conformidade com as
   *                             configurações de caracteres de máscara, não
   *                             permitindo-os.
   * @param accordingToMinimumLength boolean True para que seja verificado se o
   *                                 valor informado no Param está em conformidade
   *                                 com as configurações de tamanho mínimo.
   * @see iobjects.xml.ParamFile
   * @since 2006
   */
  public void setSpecialConstraint(boolean accordingToMaskChars,
                                   boolean accordingToMinimumLength) {
    this.accordingToMaskChars     = accordingToMaskChars;
    this.accordingToMinimumLength = accordingToMinimumLength;
  }

  /**
   * Define o uso das constraints especiais para o Param de acordo com o arquivo
   * de configurações de parâmetros.
   * @param accordingToPeriod boolean True para que seja verificado se o valor
   *                          informado no Param está em conformidade com as
   *                          configurações de período.
   * @param beginDateParam Param que formará a primeira data do período
   *                       juntamente com este Param.
   * @see iobjects.xml.ParamFile
   * @since 2006
   */
  public void setSpecialConstraint(boolean accordingToPeriod,
                                   Param   beginDateParam) {
    this.accordingToPeriod = accordingToPeriod;
    this.beginDateParam    = beginDateParam;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setFormat(int format) {
    this.format = format;
  }

  /**
   * Define o valor do Param formatado de acordo com a definição de formato de
   * edição configurado.
   * @param formatedValue String Valor formatado para ser definido ao Param.
   * @throws Exception Em caso de exceção na tentativa de remover a formatação
   *                   do valor informado.
   * @since 2006.
   */
  public void setFormatedValue(String formatedValue) throws Exception {
    // se temos uma lista lookup...
    if (lookupList.length > 0) {
      // o valor informado deve corresponder a um valor na lista de pesquisa...
      int lookupIndex = StringTools.arrayIndexOf(lookupList, formatedValue);
      // se encontramos um índice válido...define como valor do parâmetro
      if (lookupIndex >= 0)
        setValue(lookupIndex + "");
      // se não encontramos...define o valor da requisição
      else
        setValue(formatedValue);
    }
    // se temos uma máscara...
    else if (!mask.equals(""))
      setValue(StringTools.removeCustomMask(formatedValue, mask));
    // se temos um formato...
    else if (format != FORMAT_NONE) {
      switch (format) {
        case FORMAT_DATE:
          setValue(DateTools.formatDate(DateTools.parseDate(formatedValue))); break;
        case EntityField.FORMAT_DATE_TIME:
          setValue(DateTools.formatDateTime(DateTools.parseDateTime(formatedValue))); break;
        case EntityField.FORMAT_TIME:
          setValue(DateTools.formatTime(DateTools.parseTime(formatedValue))); break;
        case EntityField.FORMAT_INTEGER:
          setValue(NumberTools.format(NumberTools.parseInt(formatedValue))); break;
        case EntityField.FORMAT_DOUBLE:
          setValue(NumberTools.format(NumberTools.parseDouble(formatedValue, getPrecision()), getPrecision(), getPrecision())); break;
        case EntityField.FORMAT_UPPER_CASE:
          setValue(formatedValue.toUpperCase()); break;
        default:
          setValue(formatedValue);
      } // switch
    }
    // se não temos nada...
    else
      setValue(formatedValue);
  }

  /**
   * Define a lista de valores de pesquisa do Param.
   * @param lookupList String[] lista de valores de pesquisa do Param.
   */
  public void setLookupList(String[] lookupList) {
    this.lookupList = lookupList;
  }

  public void setMask(String mask) {
    this.mask = mask;
  }

  public void setObject(Object object) {
    this.object = object;
  }

  /**
   * Define as configurações obtidas a partir do arquivo de configurações de
   * Params.
   * @param paramFile ParamFile.
   * @see iobjects.xml.ParamFile
   */
  static public void setParamFile(ParamFile paramFile) {
    Param.paramFile = paramFile;
  }

  /**
   * Define a precisão do Param. Utilizado apenas quando a propriedade "format"
   * é igual a FORMAT_DOUBLE.
   * @param precision int Precisão do Param.
   */
  public void setPrecision(int precision) {
    this.precision = precision;
  }

  public void setScriptConstraint(String scriptConstraint) {
    this.scriptConstraint = scriptConstraint;
  }

  public void setScriptConstraintErrorMessage(String scriptConstraintErrorMessage) {
    this.scriptConstraintErrorMessage = scriptConstraintErrorMessage;
  }

  public void setSize(int size) {
    this.size = size;
  }

  /**
   * Define o novo valor para o Param e faz a verificação das cosntrainsts
   * especiais.
   * @param value String Valor para ser definido ao Param.
   */
  public void setValue(String value) {
    try {
      // se temos uma constraint especial de período...
      if (accordingToPeriod && (beginDateParam != null) && (paramFile != null)) {
        // qde de dias entre a data inicial e 'value'
        int days = DateTools.getSubtractedDays(beginDateParam.getDateValue(), DateTools.parseDate(value));
        // se a qde de dias ultrapassa as configurações de período máximo...
        if ((paramFile.specialConstraints().getMaximumPeriod() > 0) &&
            (paramFile.specialConstraints().getMaximumPeriod() < days)) {
          // apaga o valor atual do parâmetro
          this.value = "";
          // exceção
          throw new RuntimeException(new ExtendedException(getClass().getName(),
                                     "setValue",
                                     "O período de " + beginDateParam.getValue() + " até " + value + " ultrapassa a quantidade máxima de " + paramFile.specialConstraints().getMaximumPeriod() + " dias."));
        } // if
      } // for
      // define nosso valor
      this.value = value;
      // define nosso valor como Array
      this.valueAsArray = new String[]{value};
    }
    catch (Exception e) {
      // lança uma exceção de runtime
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Define o valor do Param a partir de 'request'.
   * @param request HttpServletRequest Requisição que contém um parâmetro
   *                de mesmo nome do Param cujo valor será obtido.
   * @throws Exception Em caso de exceção na tentativa de remover a formatação
   *                   do valor informado.
   */
  public void setValue(HttpServletRequest request) throws Exception {
    // valores proveniente da requisição
    String   requestValue        = request.getParameter(name);
    String[] requestValueAsArray = request.getParameterValues(name);
    // se temos um valor válido...define os valores
    if (requestValue != null)
      setFormatedValue(requestValue);
    if (requestValueAsArray != null)
      setArrayValue(requestValueAsArray);
  }

}
