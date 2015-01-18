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

import java.util.*;
import javax.servlet.http.*;

import iobjects.ui.ajax.*;
import iobjects.util.*;

/**
 * Representa uma lista de Param's.
 * @since 3.1
 */
public class ParamList implements AjaxBean {

  class NameComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      Param param1 = (Param)o1;
      Param param2 = (Param)o2;
      // ordena pelo nome
      int result = param1.getName().compareTo(param2.getName());
      // retorna
      return result;
    }
  }

  static public final String CLASS_NAME = "iobjects.ParamList";

  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   */
  public ParamList() {
  }

  /**
   * Adiciona 'param' na lista.
   * @param param Param para ser adicionado.
   */
  public void add(Param param) {
    if (get(param.getName()) == null)
      vector.add(param);
  }

  /**
   * Remove todos os Param´s da lista.
   */
  public void clear() {
    vector.clear();
  }

  /**
   * Define os valores de todos os Params da lista como uma String vazia.
   */
  public void clearValues() {
    for (int i=0; i<vector.size(); i++) {
      Param param = get(i);
      param.setValue("");
      param.setObject(null);
    } // for
  }

  /**
   * Retorna true se existir um Param com o 'name' especificado.
   * @param name Nome do Param que se deseja localizar, desprezando letras
   *             maiúsculas e minúsculas.
   * @return Retorna true se existir um Param com o 'name' especificado.
   */
  public boolean contains(String name) {
    return indexOf(name) >= 0;
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    vector.clear();
    vector = null;
  }

  /**
   * Retorna o Param na posição indicada por 'index'.
   * @param index Índice do Param que se dejesa retornar.
   * @return Retorna o Param na posição indicada por 'index'.
   */
  public Param get(int index) {
    return (Param)vector.get(index);
  }

  /**
   * Retorna o Param com o 'name' especificado.
   * @param name Nome do Param que se deseja localizar, desprezando letras
   *             maiúsculas e minúsculas.
   * @return Retorna o Param com o 'name' especificado.
   */
  public Param get(String name) {
    int index = indexOf(name);
    if (index >=0)
      return (Param)vector.get(index);
    else
      return null;
  }

  /**
   * Retorna o índice do Param com o 'name' especificado.
   * @param name Nome do Param que se deseja localizar, desprezando letras
   *             maiúsculas e minúsculas.
   * @return Retorna o índice do Param com o 'name' especificado.
   */
  public int indexOf(String name) {
    for (int i=0; i < size(); i++)
      if (get(i).getName().compareToIgnoreCase(name) == 0)
        return i;
    return -1;
  }

  /**
   * Insere 'param' no início da lista.
   * @param param Param para ser inserido.
   */
  public void insert(Param param) {
    if (get(param.getName()) == null)
      vector.insertElementAt(param, 0);
  }

  /**
   * Carrega os Param's gravados com o método Store em 'fileName'.
   * @param fileName String Nome do arquivo de origem.
   * @throws Exception Em caso de exceção na tentativa de acesso ao arquivo.
   */
  public void load(String fileName) throws Exception {
    // apaga a lista atual
    clear();
    // lê o arquivo de propriedades
    String[] lines = FileTools.loadTextFile(fileName);
    // loop nas linhas
    for (int i=0; i<lines.length; i++) {
      // valores da linha atual
      String[] lineValues = lines[i].split("\t");
      // se não temos pelo menos 4 valores...continua
      if (lineValues.length < 4)
        continue;
      // parâmetros do Param
      String   name         = lineValues[0];
      String   caption      = lineValues[1];
      String   description  = lineValues[2];
      String   value        = lineValues[3].replaceAll("/r/n", "\r\n").replaceAll("/n/r", "\n\r");
      String[] valueAsArray = {""};
      if (lineValues.length > 4)
        valueAsArray = lineValues[4].split(";");
      // novo parâmetro
      Param param = new Param(name, caption, description, value);
      param.setArrayValue(valueAsArray);
      add(param);
    } // for
  }

  /**
   * Remove o Param na posição indicada por 'index'.
   * @param index Posição do Param para remover.
   */
  public void remove(int index) {
    vector.remove(index);
  }

  /**
   * Define os valores dos Params a partir de 'request'.
   * @param request HttpServletRequest Requisição atual.
   * @throws Exception Em caso de exceção na tentativa de remover a formatação
   *                   do valor informado.
   */
  public void setParamsValues(HttpServletRequest request) throws Exception {
    // define os valores dos parâmetros pelos valores na requisição
    for (int i=0; i<size(); i++)
      get(i).setValue(request);
  }

  /**
   * Retorna a quantidade de Param´s existentes na lista.
   * @return Retorna a quantidade de Param´s existentes na lista.
   */
  public int size() {
    return vector.size();
  }

  /**
   * Ordena os Params's por nome em ordem alfabética.
   */
  public void sort() {
    // copia a lista atual para um Array
    Param[] params = new Param[size()];
    this.vector.copyInto(params);
    // ordena
    Arrays.sort(params, new NameComparator());
    // refaz nossa lista
    this.vector.clear();
    for (int i=0; i<params.length; i++)
      this.vector.add(params[i]);
  }

  /**
   * Salva os Param's existentes na lista no arquivo informado. Os Param's que
   * contiverem a propriedade Object preenchida serão descartados.
   * @param fileName String Nome do arquivo de destino.
   * @throws Exception Em caso de exceção na tentativa de acesso ao arquivo.
   */
  public void store(String fileName) throws Exception {
    // linhas de texto que iremos salvar
    Vector vector = new Vector();
    // loop nos parâmetros
    for (int i=0; i<size(); i++) {
      // parâmetro da vez
      Param param = get(i);
      // se tem um objeto...continua
      if (param.getObject() != null)
        continue;
      // se não temos nome...continua
      if (param.getName().trim().equals(""))
        continue;
      // linha que o representa
      vector.add(param.getName() + "\t"
               + param.getCaption() + "\t"
               + param.getDescription() + "\t"
               + param.getValue().replaceAll("\r\n", "/r/n").replaceAll("\n\r", "/n/r") + "\t"
               + StringTools.arrayStringToString(param.getArrayValue(), ";"));
    } // for
    // salva no arquivo informado
    String[] lines = new String[vector.size()];
    vector.copyInto(lines);
    FileTools.saveTextFile(fileName, lines);
  }

  public String toXML() {
    try {
      // nosso resultado
      StringBuffer result = new StringBuffer();
      // inicia o XML
      result.append("<" + CLASS_NAME + ">");
      // loop nos Param´s
      for (int i=0; i<size(); i++) {
        // Param da vez
        Param param = get(i);
        // adiciona ao XML no formato:
        // <paramName value="" formatedValue="">value</paramName>
        String value         = param.getValue();
        String formatedValue = param.getFormatedValue();
        result.append("<" + param.getName() + " "
                    + "value=\"" + value + "\" "
                    + "formatedValue=\"" + formatedValue + "\">"
                    + value
                    + "</" + param.getName() + ">");
      } // for
      // termina o XML
      result.append("</" + CLASS_NAME + ">");
      // retorna
      return result.toString();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna o código em JavaScript que irá retornar true ou false indicando
   * se a resposta a requisição Ajax indicada foi gerada e é suportada pela
   * classe ParamList.
   * <p>Exemplo</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     // faz a requisição
   *     &lt;%=myAjax.request()%&gt;;
   *     // se a resposta está pronta...
   *     if (&lt;%=myAjax.isResponseReady()%&gt;) {
   *       // se a resposta é o que esperamos...
   *       if (&lt;%=ParamList.xmlCheck(myAjax)%&gt;) {
   *         // ...
   *       } // if
   *     } // if
   *   &lt;/script&gt;
   * </pre>
   * @param ajax Ajax Instância que representa a requisição Ajax realizada.
   * @return String
   */
  static public String xmlCheck(Ajax ajax) {
    return "ParamList_xmlCheck(" + ajax.id() + ")";
  }

  /**
   * Retorna o código JavaScript que irá retornar o Param indicado por 'index'
   * a partir da resposta a requisição Ajax indicada. O Param retornado possui
   * as seguintes propriedades:
   * <ul>
   *   <li>name - nome do Param, Ex.: param.name;</li>
   *   <li>value - valor do Param, Ex.: param.value;</li>
   *   <li>formatedValue - valor formatado do Param, Ex.: param.formatedValue;</li>
   * </ul>
   * <p>Exemplo</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     // ...
   *
   *     // loop nos Params recebidos
   *     for (i=0; i&lt;&lt;%=ParamList.xmlSize(myAjax)%&gt;; i++) {
   *       // Param da vez
   *       var param = &lt;%=ParamList.xmlGetByIndex(myAjax, "i")%&gt;;
   *       // mostra as propriedades
   *       alert(param.name + " - " + param.value + " - " + param.formatedValue);
   *     } // for
   *   &lt;/script&gt;
   * </pre>
   * @param ajax Ajax Instância que representa a requisição Ajax realizada.
   * @param index String Índice (constante numérica) ou nome da variável local
   *                     em JavaScript indicando o Param que se deseja obter.
   * @return String
   */
  static public String xmlGetByIndex(Ajax ajax, String index) {
    return "ParamList_xmlGet(" + ajax.id() + ", " + index + ", null)";
  }

  /**
   * Retorna o código JavaScript que irá retornar o Param indicado por 'name'
   * a partir da resposta a requisição Ajax indicada. O Param retornado possui
   * as seguintes propriedades:
   * <ul>
   *   <li>name - nome do Param, Ex.: param.name;</li>
   *   <li>value - valor do Param, Ex.: param.value;</li>
   *   <li>formatedValue - valor formatado do Param, Ex.: param.formatedValue;</li>
   * </ul>
   * <p>Exemplo 1</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     // ...
   *
   *     // nome do Param desejado
   *     var paramName = "myParam";
   *     // Param desejado
   *     var param = &lt;%=ParamList.xmlGetByName(myAjax, "paramName")%&gt;;
   *     // mostra as propriedades
   *     alert(param.name + " - " + param.value + " - " + param.formatedValue);
   *
   *     // ...
   *   &lt;/script&gt;
   * </pre>
   * <p>Exemplo 2</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     // ...
   *
   *     // Param desejado
   *     var param = &lt;%=ParamList.xmlGetByName(myAjax, "'myParam'")%&gt;;
   *     // mostra as propriedades
   *     alert(param.name + " - " + param.value + " - " + param.formatedValue);
   *
   *     // ...
   *   &lt;/script&gt;
   * </pre>
   * @param ajax Ajax Instância que representa a requisição Ajax realizada.
   * @param name String Nome (entre '') ou nome da variável local em JavaScript
   *                    indicando o Param que se deseja obter.
   * @return String
   */
  static public String xmlGetByName(Ajax ajax, String name) {
    return "ParamList_xmlGet(" + ajax.id() + ", null, " + name + ")";
  }

  /**
   * Retorna o código JavaScript que irá retornar a quantidade de Param´s
   * existentes na resposta a requisição Ajax indicada.
   * <p>Exemplo</p>
   * <pre>
   *   &lt;script type="text/javascript"&gt;
   *     // ...
   *
   *     // loop nos Params recebidos
   *     for (i=0; i&lt;&lt;%=ParamList.xmlSize(myAjax)%&gt;; i++) {
   *       // ...
   *     } // for
   *   &lt;/script&gt;
   * </pre>
   * @param ajax Ajax Instância que representa a requisição Ajax realizada.
   * @return String
   */
  static public String xmlSize(Ajax ajax) {
    return "ParamList_xmlSize(" + ajax.id() + ")";
  }

}
