package iobjects.ui;

import java.io.*;
import java.util.*;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.ui.*;

/**
 * Tem a função de localizar e gerenciar os arquivos de configuração de estilo.
 */
public class StyleManager implements Comparator {

  private StyleFile   defaultStyle   = null;
  private String      styleFilesPath = "";
  private StyleFile[] styles         = {};
  private String      stylesUrl      = "";

  static private StyleManager instance = null;

  /**
   * Define a extensão dos arquivos de configuração de estilo.
   */
  public final String STYLE_FILE_EXTENSION = ".xml";
  /**
   * Nome do arquivo de configuração padrão.
   */
  private final String DEFAULT_STYLE = "default";

  private StyleManager(String styleFilesPath,
                       String stylesUrl) {
    // nossos valores
    this.styleFilesPath = styleFilesPath;
    this.stylesUrl = stylesUrl;
    // carrega as extensões
    try {
      loadStyles();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna o StyleFile padrão.
   * @return Retorna o StyleFile padrão.
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
   * Retorna o caminho local onde os arquivos de configuração de estilo
   * estão localizados.
   * @return Retorna o caminho local onde os arquivos de configuração de estilo
   *         estão localizados.
   */
  public String getStyleFilesPath() {
    return styleFilesPath;
  }

  /**
   * Retorna a instância de StyleManager que está sendo utilizada pela
   * aplicação.
   * @return Retorna a instância de StyleManager que está sendo utilizada
   *         pela aplicação.
   */
  static public StyleManager getInstance() {
    return instance;
  }

  /**
   * Retorna a instância de StyleManager para ser utilizada pela aplicação.
   * @param styleFilesPath Caminho local onde os arquivos de configuração de
   *                       estilo estão localizados.
   * @param stylesUrl URL onde os arquivos de estilo podem ser localizados.
   * @return Retorna a instância de ExtensionManager para ser utilizada pela aplicação.
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
   * Carrega as configurações de estilo.
   * @throws Exception Em caso de exceção na tentativa de acesso aos caminhos
   *                   informados, extração dos arquivos e operações inerentes.
   */
  private void loadStyles() throws Exception {
    // arquivos de configuração de estilo encontrados
    String[] styleFileExtension = {STYLE_FILE_EXTENSION};
    String[] styleFileNames     = FileTools.getFileNames(styleFilesPath, styleFileExtension, false);
    // vetor para as extensões
    Vector vector = new Vector();
    // loop nos arquivos
    for (int i=0; i<styleFileNames.length; i++) {
      // nome do arquivo da vez
      String styleFileName = styleFilesPath + styleFileNames[i];
      // carrega o arquivo de configuração
      StyleFile styleFile = new StyleFile(styleFileName, stylesUrl);
      // se é o estilo default...guarda sua referência
      if (styleFileNames[i].startsWith(DEFAULT_STYLE))
        defaultStyle = styleFile;
      // adiciona na lista
      vector.add(styleFile);
    } // for
    // se não achamos o estilo padrão...exceção
    if (defaultStyle == null)
      throw new ExtendedException(getClass().getName(), "loadStyles", "O estilo padrão não foi encontrado: '" + DEFAULT_STYLE + "'.");
    // guarda a lista de configurações de estilo
    styles = new StyleFile[vector.size()];
    vector.copyInto(styles);
    // ordena
    Arrays.sort(styles, this);
  }

  /**
   * Retorna um StyleFile[] contendo as configurações de estilo carregadas.
   * @return Retorna um StyleFile[] contendo as configurações de estilo carregadas.
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
