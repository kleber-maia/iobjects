package iobjects.ui;

import java.io.*;
import java.util.*;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.ui.*;

/**
 * Tem a fun��o de localizar e gerenciar os arquivos de configura��o de estilo.
 */
public class StyleManager implements Comparator {

  private StyleFile   defaultStyle   = null;
  private String      styleFilesPath = "";
  private StyleFile[] styles         = {};
  private String      stylesUrl      = "";

  static private StyleManager instance = null;

  /**
   * Define a extens�o dos arquivos de configura��o de estilo.
   */
  public final String STYLE_FILE_EXTENSION = ".xml";
  /**
   * Nome do arquivo de configura��o padr�o.
   */
  private final String DEFAULT_STYLE = "default";

  private StyleManager(String styleFilesPath,
                       String stylesUrl) {
    // nossos valores
    this.styleFilesPath = styleFilesPath;
    this.stylesUrl = stylesUrl;
    // carrega as extens�es
    try {
      loadStyles();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna o StyleFile padr�o.
   * @return Retorna o StyleFile padr�o.
   */
  public StyleFile defaultStyle() {
    return defaultStyle;
  }

  public int compare(Object o1, Object o2) {
    StyleFile style1 = (StyleFile)o1;
    StyleFile style2 = (StyleFile)o2;
    return style1.css().getName().compareTo(style2.css().getName());
  }

  public boolean equals(Object obj) {
    return false;
  }

  /**
   * Retorna o caminho local onde os arquivos de configura��o de estilo
   * est�o localizados.
   * @return Retorna o caminho local onde os arquivos de configura��o de estilo
   *         est�o localizados.
   */
  public String getStyleFilesPath() {
    return styleFilesPath;
  }

  /**
   * Retorna a inst�ncia de StyleManager que est� sendo utilizada pela
   * aplica��o.
   * @return Retorna a inst�ncia de StyleManager que est� sendo utilizada
   *         pela aplica��o.
   */
  static public StyleManager getInstance() {
    return instance;
  }

  /**
   * Retorna a inst�ncia de StyleManager para ser utilizada pela aplica��o.
   * @param styleFilesPath Caminho local onde os arquivos de configura��o de
   *                       estilo est�o localizados.
   * @param stylesUrl URL onde os arquivos de estilo podem ser localizados.
   * @return Retorna a inst�ncia de ExtensionManager para ser utilizada pela aplica��o.
   */
  static public StyleManager getInstance(String styleFilesPath,
                                                      String stylesUrl) {
    if (instance == null)
      instance = new StyleManager(styleFilesPath, stylesUrl);
    return instance;
  }

  /**
   * Retorna o StyleFile correspondente ao 'name' informado.
   * @param name String Nome do estilo que se deseja retornar.
   * @return StyleFile Retorna o StyleFile correspondente ao 'name' informado.
   */
  public StyleFile getStyle(String name) {
    for (int i=0; i<styles.length; i++) {
      StyleFile result = styles[i];
      if (result.css().getName().equalsIgnoreCase(name))
        return result;
    } // for
    return null;
  }

  /**
   * Carrega as configura��es de estilo.
   * @throws Exception Em caso de exce��o na tentativa de acesso aos caminhos
   *                   informados, extra��o dos arquivos e opera��es inerentes.
   */
  private void loadStyles() throws Exception {
    // arquivos de configura��o de estilo encontrados
    String[] styleFileExtension = {STYLE_FILE_EXTENSION};
    String[] styleFileNames     = FileTools.getFileNames(styleFilesPath, styleFileExtension, false);
    // vetor para as extens�es
    Vector vector = new Vector();
    // loop nos arquivos
    for (int i=0; i<styleFileNames.length; i++) {
      // nome do arquivo da vez
      String styleFileName = styleFilesPath + styleFileNames[i];
      // carrega o arquivo de configura��o
      StyleFile styleFile = new StyleFile(styleFileName, stylesUrl);
      // se � o estilo default...guarda sua refer�ncia
      if (styleFileNames[i].startsWith(DEFAULT_STYLE))
        defaultStyle = styleFile;
      // adiciona na lista
      vector.add(styleFile);
    } // for
    // se n�o achamos o estilo padr�o...exce��o
    if (defaultStyle == null)
      throw new ExtendedException(getClass().getName(), "loadStyles", "O estilo padr�o n�o foi encontrado: '" + DEFAULT_STYLE + "'.");
    // guarda a lista de configura��es de estilo
    styles = new StyleFile[vector.size()];
    vector.copyInto(styles);
    // ordena
    Arrays.sort(styles, this);
  }

  /**
   * Retorna um StyleFile[] contendo as configura��es de estilo carregadas.
   * @return Retorna um StyleFile[] contendo as configura��es de estilo carregadas.
   */
  public StyleFile[] styles() {
    return styles;
  }

  /**
   * Retorna a URL onde os arquivos de estilo podem ser localizados.
   * @return String Retorna a URL onde os arquivos de estilo podem ser localizados.
   */
  public String stylesUrl() {
    return stylesUrl;
  }

}
