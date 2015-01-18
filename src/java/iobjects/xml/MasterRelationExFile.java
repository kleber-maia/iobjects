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
package iobjects.xml;

import java.io.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.*;
import iobjects.util.*;

/**
 * Responsável pela leitura do arquivo XML de configurações da relação mestre
 * indicado por MASTER_RELATION_EX_FILE_NAME.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;masterrelationex&gt;
 *   &lt;information active="true"
 *                classname="package.ClassName"
 *                sidebaractionname="action1"
 *                securityserviceactioname="action2" /&gt;
 * &lt;/masterrelationex&gt;
 * </pre>
 * </p>
 */
public class MasterRelationExFile extends DefaultHandler {

  public class Information {
    private boolean  active                    = false;
    private String   className                 = "";
    private String   sideBarActionName         = "";
    private String   securityServiceActionName = "";
    // *
    public Information(boolean  active,
                       String   className,
                       String   sideBarActionName,
                       String   securityServiceActionName) throws Exception {
      // verifica as quantidades de campos dos vetores
      if (active) {
        if (className.equals(""))
          throw new ExtendedException(getClass().getName(), "", "ClassName não informado.");
        if (sideBarActionName.equals(""))
          throw new ExtendedException(getClass().getName(), "", "SideBarActionName não informado.");
        if (securityServiceActionName.equals(""))
          throw new ExtendedException(getClass().getName(), "", "SecurityServiceActionName não informado.");
      } // if
      this.active                    = active;
      this.className                 = className;
      this.sideBarActionName         = sideBarActionName;
      this.securityServiceActionName = securityServiceActionName;
    }
    // *
    public boolean  getActive() {return active;};
    public String   getClassName() {return className;};
    public String   getSideBarActionName() {return sideBarActionName;};
    public String   getecurityServiceActionName() {return securityServiceActionName;};
  }

  /**
   * Constante que indica o nome do arquivo de configuração da relação mestre.
   */
  private static final String MASTER_RELATION_EX_FILE_NAME = "masterrelationex.xml";
  // *
  private static final String INFORMATION = "information";
  private static final String ACTIVE = "active";
  private static final String CLASS_NAME = "classname";
  private static final String SIDE_BAR_ACTION_NAME = "sidebaractionname";
  private static final String SECURITY_SERVICE_ACTION_NAME = "securityserviceactionname";
  // *
  private Information information = null;

  /**
   * Construtor padrão.
   * @param masterRelationExFilePath Caminho local onde se encontra o arquivo de
   *                        configurações da relação mestre. Veja MASTER_RELATION_EX_FILE_NAME.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public MasterRelationExFile(String masterRelationExFilePath) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(masterRelationExFilePath + MASTER_RELATION_EX_FILE_NAME), this);
  }

  public Information information() {
    return information;
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equalsIgnoreCase(INFORMATION)) {
      try {
        information = new Information(Boolean.valueOf(attributes.getValue(ACTIVE)).booleanValue(),
                                      attributes.getValue(CLASS_NAME),
                                      attributes.getValue(SIDE_BAR_ACTION_NAME),
                                      attributes.getValue(SECURITY_SERVICE_ACTION_NAME));
      }
      catch (Exception e) {
        throw new RuntimeException(new ExtendedException(getClass().getName(), "startElement", e));
      } // try-catch
    } // if
  }

  public int[] stringToInt(String[] array) throws Exception {
    int[] result = new int[array.length];
    for (int i=0; i<array.length; i++)
      result[i] = NumberTools.parseInt(array[i]);
    return result;
  }

}
