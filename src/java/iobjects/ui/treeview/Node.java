package iobjects.ui.treeview;

/**
 * Representa um n� da �rvore do TreeView.
 */
public class Node {

  public boolean  checked        = false;
  public NodeList childNodes     = new NodeList();
  public String   extraImageUrl  = "";
  public String   imageUrl       = "";
  public String   imageOpenedUrl = "";
  public String   href           = "";
  public String   target         = "";
  public String   text           = "";
  public String   toolTip        = "";
  public String   value          = "";

  /**
   * Construtor padr�o.
   * @param text String Texto.
   */
  public Node(String text) {
    this(text, "");
  }

  /**
   * Construtor estendido.
   * @param text String Texto.
   * @param href String Refer�ncia.
   */
  public Node(String text,
              String href) {
    this(text, href, "");
  }

  /**
   * Construtor estendido.
   * @param text String Texto.
   * @param href String Refer�ncia.
   * @param toolTip String Informa��es adicionais.
   */
  public Node(String text,
              String href,
              String toolTip) {
    // nossos valores
    this.text = text;
    this.href = href;
    this.toolTip = toolTip;
  }

  /**
   * Construtor estendido.
   * @param text String Texto.
   * @param href String Refer�ncia.
   * @param toolTip String Informa��es adicionais.
   * @param checked boolean True se o n� ter� seu checkbox marcado.
   */
  public Node(String  text,
              String  href,
              String  toolTip,
              boolean checked) {
    // nossos valores
    this.text = text;
    this.href = href;
    this.toolTip = toolTip;
    this.checked = checked;
  }

  /**
   * Adiciona um n� filho.
   * @param node Node N� para ser adicionado.
   */
  public void add(Node node) {
    // adiciona o n�s
    childNodes.add(node);
  }

  /**
   * Adiciona um n� filho.
   * @param text String Texto do n� filho.
   * @return Retorna o n� adicionado.
   */
  public Node add(String text) {
    Node result = new Node(text);
    add(result);
    return result;
  }

  /**
   * Adiciona um n� filho.
   * @param text String Texto do n� para ser adicionado.
   * @param href String Refer�ncia.
   * @return Retorna o n� adicionado.
   */
  public Node add(String text,
                  String href) {
    Node result = new Node(text, href);
    add(result);
    return result;
  }

  /**
   * Adiciona um n� filho.
   * @param text String Texto do n� para ser adicionado.
   * @param href String Refer�ncia.
   * @param toolTip String Informa��es adicionais.
   * @return Retorna o n� adicionado.
   */
  public Node add(String text,
                  String href,
                  String toolTip) {
    Node result = new Node(text, href, toolTip);
    add(result);
    return result;
  }

  /**
   * Define as imagens do n�s.
   * @param imageUrl String Imagem do n�.
   * @param imageOpenedUrl String Imagem do n� no estado "aberto".
   */
  public void setImages(String imageUrl,
                        String imageOpenedUrl) {
    this.imageUrl = imageUrl;
    this.imageOpenedUrl = imageOpenedUrl;
  }

  /**
   * Define as imagens do n�s.
   * @param imageUrl String Imagem do n�.
   * @param imageOpenedUrl String Imagem do n� no estado "aberto".
   * @param extraImageUrl String Imagem extra do n�.
   */
  public void setImages(String imageUrl,
                        String imageOpenedUrl,
                        String extraImageUrl) {
    this.imageUrl = imageUrl;
    this.imageOpenedUrl = imageOpenedUrl;
    this.extraImageUrl = extraImageUrl;
  }

}
