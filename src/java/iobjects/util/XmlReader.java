package iobjects.util;

import java.io.*;
import java.util.*;
import java.net.*;

import javax.xml.parsers.*;

import iobjects.*;

import org.w3c.dom.*;

/**
 *
 * @author klebermaia
 */
public class XmlReader {

  static public void main(String[] args) {
    try {
      XmlReader reader = new XmlReader("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                                      + "<Saida EmpresaId=\"1\" SaidaId=\"123\" DataHoraFaturamento=\"01/01/2013 13:00\">"
                                      +   "<FormasPagamento Quantidade=\"2\">"
                                      +      "<FormaPagamento Id=\"1\" Nome=\"Dinheiro\" Valor=\"123,90\" Parcelas=\"0\" Banco=\"\" Cheque=\"\" Data=\"\"/>"
                                      +      "<FormaPagamento Id=\"2\" Nome=\"Cartao de Credito\" Valor=\"24,00\" Parcelas=\"3\" Banco=\"\" Cheque=\"\" Data=\"\"/>"
                                      +   "</FormasPagamento>"
                                      +   "<NotaFiscal Modelo=\"\" Serie=\"\" Numero=\"\" Chave=\"\"/>"
                                      + "</Saida>");

      XmlTag root = reader.getItem("Saida"); //reader.getRoot();
      String empresaId = root.getAttribute("EmpresaId");
      String saidaId = root.getAttribute("SaidaId");
      String dataHoraFaturamento = root.getAttribute("DataHoraFaturamento");
      System.out.println(empresaId);
      System.out.println(saidaId);
      System.out.println(dataHoraFaturamento);
      
      XmlTag formasPagamento = reader.getItem("Saida/FormasPagamento");
      String quantidade = formasPagamento.getAttribute("Quantidade");
      System.out.println(quantidade);
      
      XmlTag[] formaPagamentoList = reader.getChildren("Saida/FormasPagamento");
      for (int i=0; i<formaPagamentoList.length; i++) {
        System.out.println(formaPagamentoList[i].getAttribute("Id") + "; " + formaPagamentoList[i].getAttribute("Nome") + "; " + formaPagamentoList[i].getAttribute("Valor") + "; " + formaPagamentoList[i].getAttribute("Parcelas") + "; " + formaPagamentoList[i].getAttribute("Banco") + "; " + formaPagamentoList[i].getAttribute("Cheque") + "; " + formaPagamentoList[i].getAttribute("Data"));
      } // for

      XmlTag formaPagamento1 = reader.getItem("Saida/FormasPagamento/FormaPagamento");
      System.out.println(formaPagamento1.getAttribute("Id") + "; " + formaPagamento1.getAttribute("Nome") + "; " + formaPagamento1.getAttribute("Valor") + "; " + formaPagamento1.getAttribute("Parcelas") + "; " + formaPagamento1.getAttribute("Banco") + "; " + formaPagamento1.getAttribute("Cheque") + "; " + formaPagamento1.getAttribute("Data"));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Representa um elemento XML e seus atributos.
   */
  public class XmlTag {
    private String    name      = "";
    private Node      node      = null;
    private ParamList paramList = new ParamList();
    // *
    public XmlTag(Node self) {
      this.node = self;
      // *
      this.name = self.getNodeName();
      for (int i=0; i<self.getAttributes().getLength(); i++) {
        Node attribute = self.getAttributes().item(i);
        paramList.add(new Param(attribute.getNodeName(), attribute.getNodeValue()));
      } // for
    }
    /**
     * Retorna o nome da Tag XML.
     * @return 
     */
    public String getName() {
      return name;
    }
    /**
     * Retorna o valor do atributo da Tag XML representado por 'name'.
     * @param name Nome do atributo cujo valor se deseja obter.
     * @return 
     */
    public String getAttribute(String name) {
      Param param = paramList.get(name);
      if (param != null)
        return param.getValue();
      else
        return "";
    }
    /**
     * Retorna a Tag XML pai.
     * @return 
     */
    public XmlTag getParent() {
      if (node.getParentNode() != null)
        return new XmlTag(node.getParentNode());
      else
        return null;
    }
  }
  
  private Document document = null;

  /**
   * Construtor default.
   * @param xml String contendo o XML que será lido.
   * @throws Exception Em caso de exceção na tentativa de instanciar o parser
   *                   ou interpretar o XML.
   */
  public XmlReader(String xml) throws Exception {
    // nosso factory
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    // nosso builder
    DocumentBuilder builder = factory.newDocumentBuilder();
    // carrega o XML
    ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes());
    document = builder.parse(inputStream);
  }

  /**
   * Construtor estendido.
   * @param xmlFile File representando o arquivo XML que será lido.
   * @throws Exception Em caso de exceção na tentativa de instanciar o parser
   *                   ou interpretar o XML.
   */
  public XmlReader(File xmlFile) throws Exception {
    // nosso factory
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    // nosso builder
    DocumentBuilder builder = factory.newDocumentBuilder();
    // carrega o XML
    FileInputStream inputStream = new FileInputStream(xmlFile);
    document = builder.parse(inputStream);
  }

  /**
   * Construtor estendido.
   * @param xmlURL URL representando o arquivo XML que será lido.
   * @throws Exception Em caso de exceção na tentativa de instanciar o parser
   *                   ou interpretar o XML.
   */
  public XmlReader(URL xmlURL) throws Exception {
    // nosso factory
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    // nosso builder
    DocumentBuilder builder = factory.newDocumentBuilder();
    // carrega o XML
    document = builder.parse(xmlURL.openConnection().getInputStream());
  }

  /**
   * Retorna um XmlTag[] contendo as tags filhas do caminho informado.
   * @param path Caminho da tag que se deseja obter, ex: Raiz/Item/Subitem
   * @return
   * @throws Exception 
   */
  public XmlTag[] getChildren(String path) throws Exception {
    // obtém o próprio item
    XmlTag item = getItem(path);
    // nossa lista
    Vector<XmlTag> vector = new Vector<XmlTag>();
    for (int i=0; i<item.node.getChildNodes().getLength(); i++) {
      vector.add(new XmlTag(item.node.getChildNodes().item(i)));
    } // for
    // retorna
    XmlTag[] result = new XmlTag[vector.size()];
    vector.copyInto(result);
    return result;
  }
  
  /**
   * Retorna um XmlTag representando o caminho informado.
   * @param path Caminho da tag que se deseja obter, ex: Raiz/Item/Subitem
   * @return
   * @throws Exception 
   */
  public XmlTag getItem(String path) throws Exception {
    // separa as partes do caminho
    String[] pathParts = path.split("/");
    // obtém a primeira parte a partir da raiz do documento
    Element root = document.getDocumentElement();
    // se a raiz do XML não combina com a raiz do path...exceção
    if (!root.getTagName().equals(pathParts[0]))
      throw new ExtendedException(getClass().getName(), "getNode", "A raiz do XML '" + root.getTagName() + "' não combina com '" + pathParts[0] + "'.");
    // se o item desejado é o root...retorna
    if (root.getTagName().equals(path))
      return new XmlTag(root);
    // filhos do nó atual
    NodeList childNodes = root.getChildNodes();
    // nosso resultado
    Node result = null;
    // loop nas outras partes
    for (int i=1; i<pathParts.length; i++) {
      // loop nos filhos
      for (int w=0; w<childNodes.getLength(); w++) {
        // nó atual
        Node temp = childNodes.item(w);
        // se é o que procuramos...dispara
        if (temp.getNodeName().equals(pathParts[i])) {
          result = temp;
          childNodes = result.getChildNodes();
          break;
        } // if
      } // for w
    } // for i
    // retorna
    return new XmlTag(result);
  }

  /**
   * Retorna a Tag que representa a raiz do XML.
   * @return
   * @throws Exception 
   */
  public XmlTag getRoot() throws Exception {
    return new XmlTag(document.getDocumentElement().cloneNode(false));
  }
  
}
