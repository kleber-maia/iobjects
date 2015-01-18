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
 * Os dicionários são utilizados para localização da aplicação em termos de
 * cultura. A aplicação é utilizada por grupos de mesmo idioma, mas com culturas 
 * de nomenclatura diferentes, exemplo: o que na Empresa A é conhecido como 
 * 'Cliente', na Empresa B é conhecido como 'Paciente' e na Empresa C é conhecido 
 * como 'Aluno'.
 */
public class Dictionary {

  static public void main(String[] args) {
    Dictionary dic = new Dictionary(null);
    dic.paramList.add(new Param("Cliente", "Paciente"));
    //dic.paramList.add(new Param("Vendedor", "Fonoaudiólogo"));
    String value = "Informe a avaliação do(a) [Cliente] em relação a(o) [Vendedor].";
    System.out.println(dic.translate(value));
  }

  /**
   * Representa a Pattern utilizada para localizar as ocorrências de expressões
   * pelo método translate() e que serão substituídas pelas suas respectivas
   * traduções.
   */
  static public final Pattern PATTERN = Pattern.compile("\\[[^\\[\\]]+\\]", Pattern.MULTILINE);
  
  private ParamList paramList = null;
  
  /**
   * Construtor padrão.
   */
  public Dictionary() {
  }
  
  /**
   * Construtor padrão.
   * @param dictionaryFile Dictionary File contendo os termos nativos e suas 
   *                       traduções.
   */
  public Dictionary(DictionaryFile dictionaryFile) {
    // guarda nosso arquivo de dicionário
    setDictionaryFile(dictionaryFile);
  }
  
  /**
   * Retorna 'value' com cada uma suas de suas expressões substituídas pelas
   * suas respectivas traduções. Caso não sejam encontradas traduções no dicionário,
   * as expressões originais serão mantidas.
   * <p><b>Utilize a marcação [] em cada expressão que deseja traduzir. Exemplo:
   * Informe a avaliação do(a) [Cliente] em relação a(o) [Vendedor].</b></p>
   * @param value Frase contendo a(s) expressão(ões) que se deseja traduzir.
   * @return 
   */
  public String translate(String value) {
    // nosso resultado
    StringBuffer result = new StringBuffer(value.length() * 2);
    // encontra <PALAVRA> no valor informado
    Matcher matcher = PATTERN.matcher(value);
    // enquanto houverem expressões...
    while (matcher.find()) {
      // expressão encontrada
      String expression = matcher.group();
      expression = expression.substring(1, expression.length()-1);
      // procura na nossa lista
      Param param = paramList.get(expression);
      // substitui pela sua tradução
      matcher.appendReplacement(result, param != null ? param.getValue() : expression);
    } // while
    // complementa nosso resultado
    matcher.appendTail(result);
    // retorna
    return result.toString();
  }

  /**
   * Define o arquivo de dicionário para ser utilizado nas traduções.
   * @param dictionaryFile Dictionary File contendo os termos nativos e suas 
   *                       traduções.
   */
  public void setDictionaryFile(DictionaryFile dictionaryFile) {
    // nossos valores
    if (dictionaryFile != null)
      paramList = dictionaryFile.getExpressions();
    else
      paramList = new ParamList();
  }
  
}
