package iobjects.xml.ui;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.*;

/**
 * Responsável pela leitura dos arquivos XML de dicionário.
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
   * Construtor padrão.
   * @param dictionaryFileName Nome do arquivo de dicionário.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public DictionaryFile(String dictionaryFileName) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
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
   * Retorna um ParamList contendo as expressões encontradas no dicionário, onde
   * o nome de cada parâmetro é a palavra nativa e o valor do parâmetro é sua
   * tradução.
   * @return String Retorna um ParamList contendo as expressões encontradas no dicionário.
   */
  public ParamList getExpressions() {
    return paramList;
  }

  /**
   * Retorna o nome do dicionário.
   * @return String Retorna o nome do dicionário.
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
