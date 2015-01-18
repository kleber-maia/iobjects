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

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.ui.*;
import iobjects.xml.*;

/**
 * Representa o estado atual da rela��o mestre configurada na aplica��o.
 */
public class MasterRelation {

  static private final String PARAM_MASTER_RELATION      = "masterRelation";
  static private final String PARAM_MASTER_RELATION_USER = "masterRelationUser";

  private MasterRelationFile.Information information = null;
  private ParamList                      values      = new ParamList();

  /**
   * Construtor padr�o.
   * @param information MasterRelationFile.Information contendo as informa��es
   *                    de configura��o da rela��o mestre.
   */
  public MasterRelation(MasterRelationFile.Information information) {
    // guarda as informa��es da rela��o mestre
    this.information = information;
  }

  /**
   * Apaga o estado atual da rela��o mestre.
   */
  public void clear() {
    values.clear();
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    information = null;
    values      = null;
  }

  /**
   * Retorna o valor referenciado por 'index'.
   * @param index int �ndice do valor que se deseja retornar.
   * @throws Exception Em caso de exce��o na tentativa de localizar o valor.
   * @return String Retorna o valor referenciado por 'index'.
   */
  private String get(int index) throws Exception {
    // se n�o temos nenhum valor configurado...exce��o
    if (values.size() < information.getReturnFieldNames().length)
      throw new ExtendedException(getClass().getName(), "get", "O(a) " + information.getCaption() + " n�o foi propriamente selecionado(a).");
    // localiza o valor
    Param param = values.get(index);
    // se n�o encontrou..exce��o
    if (param == null)
      throw new ExtendedException(getClass().getName(), "get", index + " n�o configurado na rela��o mestre.");
    // se n�o tem valor associado...exce��o
    if (param.getValue().trim().equals(""))
      throw new ExtendedException(getClass().getName(), "get", information.getCaption() + " n�o foi propriamente selecionado(a).");
    // retorna
    return param.getValue();
  }

  /**
   * Retorna o valor referenciado por 'name'.
   * @param name String Nome do valor que se deseja retornar.
   * @throws Exception Em caso de exce��o na tentativa de localizar o valor.
   * @return String Retorna o valor referenciado por 'name'.
   */
  private String get(String name) throws Exception {
    // se n�o temos nenhum valor configurado...exce��o
    if (values.size() < information.getReturnFieldNames().length)
      throw new ExtendedException(getClass().getName(), "get",  "O(a) " + information.getCaption() + " n�o foi propriamente selecionado(a).");
    // localiza o valor
    Param param = values.get(name);
    // se n�o encontrou..exce��o
    if (param == null)
      throw new ExtendedException(getClass().getName(), "get", name + " n�o configurado na rela��o mestre.");
    // se n�o tem valor associado...exce��o
    if (param.getValue().trim().equals(""))
      throw new ExtendedException(getClass().getName(), "get", information.getCaption() + " n�o foi propriamente selecionado(a).");
    // retorna
    return param.getValue();
  }

  /**
   * Retorna o valor informado por 'index' como um boolean.
   * @param index int �ndice do valor que se deseja retornar. Sendo '0' (zero)
   *              o �ndice do primeiro valor.
   * @throws Exception Em caso de exce��o na tentativa de localizar o valor.
   * @return boolean Retorna o valor informado por 'index' como um boolean.
   * @since 3.3
   */
  public boolean getBoolean(int index) throws Exception {
    return Boolean.valueOf(get(index)).booleanValue();
  }

  /**
   * Retorna o valor informado por 'name' como um boolean.
   * @param name String Nome do valor que se deseja retornar.
   * @throws Exception Em caso de exce��o na tentativa de localizar o valor.
   * @return boolean Retorna o valor informado por 'name' como um boolean.
   * @since 3.3
   */
  public boolean getBoolean(String name) throws Exception {
    return Boolean.valueOf(get(name)).booleanValue();
  }

  /**
   * Retorna o valor informado por 'index' como um double.
   * @param index int �ndice do valor que se deseja retornar. Sendo '0' (zero)
   *              o �ndice do primeiro valor.
   * @throws Exception Em caso de exce��o na tentativa de localizar o valor.
   * @return boolean Retorna o valor informado por 'index' como um double.
   * @since 3.3
   */
  public double getDouble(int index) throws Exception {
    return NumberTools.parseDouble(get(index));
  }

  /**
   * Retorna o valor informado por 'name' como um double.
   * @param name String Nome do valor que se deseja retornar.
   * @throws Exception Em caso de exce��o na tentativa de localizar o valor.
   * @return boolean Retorna o valor informado por 'name' como um double.
   * @since 3.3
   */
  public double getDouble(String name) throws Exception {
    return NumberTools.parseDouble(get(name));
  }

  /**
   * Retorna o valor informado por 'index' como um int.
   * @param index int �ndice do valor que se deseja retornar. Sendo '0' (zero)
   * o �ndice do primeiro valor.
   * @throws Exception Em caso de exce��o na tentativa de localizar o valor.
   * @return boolean Retorna o valor informado por 'index' como um int.
   * @since 3.3
   */
  public int getInt(int index) throws Exception {
    return NumberTools.parseInt(get(index));
  }

  /**
   * Retorna o valor informado por 'name' como um int.
   * @param name String Nome do valor que se deseja retornar.
   * @throws Exception Em caso de exce��o na tentativa de localizar o valor.
   * @return boolean Retorna o valor informado por 'name' como um int.
   * @since 3.3
   */
  public int getInt(String name) throws Exception {
    return NumberTools.parseInt(get(name));
  }

  /**
   * Retorna o valor informado por 'index' como um String.
   * @param index int �ndice do valor que se deseja retornar. Sendo '0' (zero)
   * o �ndice do primeiro valor.
   * @throws Exception Em caso de exce��o na tentativa de localizar o valor.
   * @return boolean Retorna o valor informado por 'index' como um String.
   * @since 3.3
   */
  public String getString(int index) throws Exception {
    return get(index);
  }

  /**
   * Retorna o valor informado por 'name' como um String.
   * @param name String Nome do valor que se deseja retornar.
   * @throws Exception Em caso de exce��o na tentativa de localizar o valor.
   * @return boolean Retorna o valor informado por 'name' como um String.
   * @since 3.3
   */
  public String getString(String name) throws Exception {
    return get(name);
  }

  /**
   * Retorna um String[] contendo os valores de 'returnFieldNames' definidos
   * na rela��o mestre.
   * @throws Exception Em caso de exce��o na tentativa de localiar um dos valores.
   * @return String[] Retorna um String[] contendo os valores de 'returnFieldNames'
   *         definidos na rela��o mestre.
   */
  public String[] getValues() throws Exception {
    String[] result = new String[information.getReturnFieldNames().length];
    for (int i=0; i<information.getReturnFieldNames().length; i++)
      result[i] = get(information.getReturnFieldNames()[i]);
    return result;
  }

  /**
   * Retorna um String[] contendo os valores de 'returnUserFieldNames' definidos
   * na rela��o mestre.
   * @throws Exception Em caso de exce��o na tentativa de localiar um dos valores.
   * @return String[] Retorna um String[] contendo os valores de 'returnUserFieldNames'
   *         definidos na rela��o mestre.
   */
  public String[] getUserValues() throws Exception {
    String[] result = new String[information.getReturnUserFieldNames().length];
    for (int i=0; i<information.getReturnUserFieldNames().length; i++)
      result[i] = get(information.getReturnUserFieldNames()[i]);
    return result;
  }

  /**
   * Retorna true caso algum os valores da rela��o mestre n�o tenham sido
   * definidos.
   * @return boolean Retorna true caso os valores da rela��o mestre n�o
   *         tenham sido definidos.
   */
  public boolean isEmpty() {
    return values.size() == 0;
  }

  /**
   * Obt�m os valores da MasterRelation atrav�s dos par�metros informados.
   * @param paramList
   * @throws java.lang.Exception
   */
  public void load(ParamList paramList) throws Exception {
    // obt�m o par�metro
    Param paramMasterRelation = paramList.get(PARAM_MASTER_RELATION);
    // se temos...obt�m o valor
    if (paramMasterRelation != null)
      setValues(paramMasterRelation.getArrayValue());
    // obt�m o par�metro
    Param paramMasterRelationUser = paramList.get(PARAM_MASTER_RELATION_USER);
    // se temos...obt�m o valor
    if (paramMasterRelationUser != null)
      setUserValues(paramMasterRelationUser.getArrayValue());
  }

  /**
   * Salva os valores da MasterRelation na lista de par�metros informada.
   * @param paramList
   * @throws java.lang.Exception
   */
  public void store(ParamList paramList) throws Exception {
    // obt�m o par�metro
    Param paramMasterRelation = paramList.get(PARAM_MASTER_RELATION);
    // se n�o temos...cria
    if (paramMasterRelation == null) {
      paramMasterRelation = new Param(PARAM_MASTER_RELATION, "");
      paramList.add(paramMasterRelation);
    } // if
    // define o valor
    paramMasterRelation.setArrayValue(getValues());

    // obt�m o par�metro
    Param paramMasterRelationUser = paramList.get(PARAM_MASTER_RELATION_USER);
    // se n�o temos...cria
    if (paramMasterRelationUser == null) {
      paramMasterRelationUser = new Param(PARAM_MASTER_RELATION_USER, "");
      paramList.add(paramMasterRelationUser);
    } // if
    // define o valor
    paramMasterRelationUser.setArrayValue(getUserValues());
  }
  
  /**
   * Define o valor referenciado por 'name'.
   * @param name String Nome do valor que se dejesa definir.
   * @param value String Nome valor para ser definido.
   * @throws Exception Em caso de exce��o na tentativa de definir o novo valor.
   */
  private void set(String name, String value) throws Exception {
    // se recebemos um valor inv�lido...exce��o
    if ((value == null) || (value.equals("")))
      throw new ExtendedException(getClass().getName(), "set", "'" + value + "' � um valor inv�lido para " + name + " na rela��o mestre.");
    // procura o valor na lista
    Param thisValue = values.get(name);
    // se ainda n�o foi definido...
    if (thisValue == null)
      // ...cria um novo
      values.add(new Param(name, value.trim()));
    // se j� foi definido...altera seu valor
    else
      thisValue.setValue(value);
  }

  /**
   * Define os valores informados em 'values' que devem combinar com
   * 'returnFieldNames' na configura��o da rela��o mestre.
   * @param values String[] Valores para serem definidos.
   * @throws Exception Em caso de exce��o na defini��o dos valores.
   */
  public void setValues(String[] values) throws Exception {
    // se a quantidade de valores recebidos n�o combida...exce��o
    if (information.getReturnFieldNames().length != values.length)
      throw new ExtendedException(getClass().getName(), "setValues", "A quantidade de valores n�o combina com os campos configurados na rela��o mestre.");
    // loop no vetor de campos de retorno
    for (int i=0; i<information.getReturnFieldNames().length; i++)
      set(information.getReturnFieldNames()[i], values[i]);
  }

  /**
   * Define os valores informados em 'values' que devem combinar com
   * 'returnUserFieldNames' na configura��o da rela��o mestre.
   * @param values String[] Valores para serem definidos.
   * @throws Exception Em caso de exce��o na defini��o dos valores.
   */
  public void setUserValues(String[] values) throws Exception {
    // se a quantidade de valores recebidos n�o combida...exce��o
    if (information.getReturnUserFieldNames().length != values.length)
      throw new ExtendedException(getClass().getName(), "setUserValues", "A quantidade de valores n�o combina com os campos configurados na rela��o mestre.");
    // loop no vetor de campos de retorno
    for (int i=0; i<information.getReturnUserFieldNames().length; i++)
      set(information.getReturnUserFieldNames()[i], values[i]);
  }

}
