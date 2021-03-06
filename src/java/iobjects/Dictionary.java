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

import java.util.regex.*
        ;
import iobjects.xml.ui.*;

/**
 * Os dicion�rios s�o utilizados para localiza��o da aplica��o em termos de
 * cultura. A aplica��o � utilizada por grupos de mesmo idioma, mas com culturas 
 * de nomenclatura diferentes, exemplo: o que na Empresa A � conhecido como 
 * 'Cliente', na Empresa B � conhecido como 'Paciente' e na Empresa C � conhecido 
 * como 'Aluno'.
 */
public class Dictionary {

  static public void main(String[] args) {
    Dictionary dic = new Dictionary(null);
    dic.paramList.add(new Param("Cliente", "Paciente"));
    //dic.paramList.add(new Param("Vendedor", "Fonoaudi�logo"));
    String value = "Informe a avalia��o do(a) [Cliente] em rela��o a(o) [Vendedor].";
    System.out.println(dic.translate(value));
  }

  /**
   * Representa a Pattern utilizada para localizar as ocorr�ncias de express�es
   * pelo m�todo translate() e que ser�o substitu�das pelas suas respectivas
   * tradu��es.
   */
  static public final Pattern PATTERN = Pattern.compile("\\[[^\\[\\]]+\\]", Pattern.MULTILINE);
  
  private ParamList paramList = null;
  
  /**
   * Construtor padr�o.
   */
  public Dictionary() {
  }
  
  /**
   * Construtor padr�o.
   * @param dictionaryFile Dictionary File contendo os termos nativos e suas 
   *                       tradu��es.
   */
  public Dictionary(DictionaryFile dictionaryFile) {
    // guarda nosso arquivo de dicion�rio
    setDictionaryFile(dictionaryFile);
  }
  
  /**
   * Retorna 'value' com cada uma suas de suas express�es substitu�das pelas
   * suas respectivas tradu��es. Caso n�o sejam encontradas tradu��es no dicion�rio,
   * as express�es originais ser�o mantidas.
   * <p><b>Utilize a marca��o [] em cada express�o que deseja traduzir. Exemplo:
   * Informe a avalia��o do(a) [Cliente] em rela��o a(o) [Vendedor].</b></p>
   * @param value Frase contendo a(s) express�o(�es) que se deseja traduzir.
   * @return 
   */
  public String translate(String value) {
    // nosso resultado
    StringBuffer result = new StringBuffer(value.length() * 2);
    // encontra <PALAVRA> no valor informado
    Matcher matcher = PATTERN.matcher(value);
    // enquanto houverem express�es...
    while (matcher.find()) {
      // express�o encontrada
      String expression = matcher.group();
      expression = expression.substring(1, expression.length()-1);
      // procura na nossa lista
      Param param = paramList.get(expression);
      // substitui pela sua tradu��o
      matcher.appendReplacement(result, param != null ? param.getValue() : expression);
    } // while
    // complementa nosso resultado
    matcher.appendTail(result);
    // retorna
    return result.toString();
  }

  /**
   * Define o arquivo de dicion�rio para ser utilizado nas tradu��es.
   * @param dictionaryFile Dictionary File contendo os termos nativos e suas 
   *                       tradu��es.
   */
  public void setDictionaryFile(DictionaryFile dictionaryFile) {
    // nossos valores
    if (dictionaryFile != null)
      paramList = dictionaryFile.getExpressions();
    else
      paramList = new ParamList();
  }
  
}
