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
package iobjects.xml.ui;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.*;

/**
 * Respons�vel pela leitura dos arquivos XML de dicion�rio.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;dictionary&gt;
 *   &lt;expression native="Cliente" translation="Customer" /&gt;
 * &lt;/dictionary&gt;
 * </pre>
 * </p>
 */
public class DictionaryFile extends DefaultHandler {

  private static final String EXPRESSION  = "expression";
  private static final String NATIVE      = "native";
  private static final String TRANSLATION = "translation";

  private ParamList paramList = new ParamList();
  private String    name      = "";

  /**
   * Construtor padr�o.
   * @param dictionaryFileName Nome do arquivo de dicion�rio.
   * @throws Exception Em caso de exce��o na tentativa de abertura do arquivo
   *                   XML especificado ou de inicializa��o do parser.
   */
  public DictionaryFile(String dictionaryFileName) throws Exception {
    // nossa f�brica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // n�o validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a an�lise
    saxParser.parse(new File(dictionaryFileName), this);
    // guarda nosso nome
    File file = new File(dictionaryFileName);
    name = file.getName().substring(0, file.getName().indexOf("."));
  }

  public void finalize() {
    paramList.clear();
    paramList = null;
  }

  /**
   * Retorna um ParamList contendo as express�es encontradas no dicion�rio, onde
   * o nome de cada par�metro � a palavra nativa e o valor do par�metro � sua
   * tradu��o.
   * @return String Retorna um ParamList contendo as express�es encontradas no dicion�rio.
   */
  public ParamList getExpressions() {
    return paramList;
  }

  /**
   * Retorna o nome do dicion�rio.
   * @return String Retorna o nome do dicion�rio.
   */
  public String getName() {
    return name;
  }

  public void startElement(java.lang.String uri,
                           java.lang.String localName,
                           java.lang.String qName,
                           Attributes attributes) {
    // se encontramos o elemento "expression"
    if (qName.equalsIgnoreCase(EXPRESSION))
      paramList.add(new Param(attributes.getValue(NATIVE), attributes.getValue(TRANSLATION)));
  }

}
