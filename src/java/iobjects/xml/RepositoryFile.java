package iobjects.xml;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import iobjects.*;
import iobjects.util.*;

/**
 * Responsável pela leitura do arquivo XML de configurações do repositório
 * de atualizações, indicado por REPOSITORY_FILE_NAME.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;repository&gt;
 *   &lt;update type="complete"    applicationpackage="iobjects_20060201.update" workpackage="iobjects_work_20060201.update" date="01/02/2006" /&gt;
 *   &lt;update type="incremental" applicationpackage=""                         workpackage="iobjects_work_20060202.update" date="02/02/2006" /&gt;
 * &lt;/repository&gt;
 * </pre>
 * </p>
 */
public class RepositoryFile extends DefaultHandler {

  static public class Update {
    private int    type               = 0;
    private String applicationPackage = "";
    private Date   date               = null;
    private String workPackage        = "";
    public Update(int    type,
                  String applicationPackage,
                  String workPackage,
                  Date   date) {
      this.type = type;
      this.applicationPackage = applicationPackage;
      this.workPackage = workPackage;
      this.date = date;
    }
    public int    getType()               { return type; }
    public String getApplicationPackage() { return applicationPackage; }
    public Date   getDate()               { return date; }
    public String getWorkPackage()        { return workPackage; }
  }

  /**
   * Constante que indica o nome do arquivo de repositório de atualizações.
   */
  static public final String REPOSITORY_FILE_NAME = "repository.xml";
  // *
  static public final String DATE                = "date";
  static public final String APPLICATION_PACKAGE = "applicationpackage";
  static public final String TYPE                = "type";
  static public final String UPDATE              = "update";
  static public final String WORK_PACKAGE        = "workpackage";
  // *
  static public  final String[] TYPES = {"complete", "incremental"};
  static public  final int TYPE_COMPLETE    = 0;
  static public  final int TYPE_INCREMENTAL = 1;
  // *
  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   * @param repositoryURL URL onde se encontra o arquivo de atualizações
   *                      da aplicação. Veja REPOSITORY_FILE_NAME.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public RepositoryFile(String repositoryURL) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    URL repositoryFileURL = new URL(repositoryURL + REPOSITORY_FILE_NAME);
    saxParser.parse(repositoryFileURL.openStream(), this);
  }

  public void finalize() {
    vector.clear();
    vector = null;
  }

  public Update[] updateList() {
    Update[] result = new Update[vector.size()];
    vector.copyInto(result);
    return result;
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    // achamos um nó de update?
    if (qName.equalsIgnoreCase(UPDATE)) {
      vector.add(new Update(StringTools.arrayIndexOf(TYPES, attributes.getValue(TYPE)),
                            attributes.getValue(APPLICATION_PACKAGE),
                            attributes.getValue(WORK_PACKAGE),
                            DateTools.parseDate(attributes.getValue(DATE))));
    } // if
  }

}
