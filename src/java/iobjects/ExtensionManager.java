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

import java.io.*;
import java.util.*;

import iobjects.util.*;

/**
 * Tem a fun��o de localizar as extens�es da aplica��o, gerenci�-las, extrair
 * seus arquivos em um diret�rio de cache e public�-los.
 */
public class ExtensionManager {

  static private ExtensionManager instance         = null;
  static private boolean          extensionsLoaded = false;

  private String      cacheFilesPath           = "";
  private String      classFilesPath           = "";
  private String      demoServiceClassName     = "";
  private Extension[] extensions               = {};
  private String      extensionFilesPath       = "";
  private String      extensionsUrl            = "";
  private String      registryClassName        = "";
  private String      securityServiceClassName = "";

  /**
   * Define a extens�o dos arquivos de Extens�o da aplica��o.
   */
  static public final String EXTENSION_FILE_EXTENSION = ".iobjects";

  private ExtensionManager(String extensionFilesPath,
                           String cacheFilesPath,
                           String classFilesPath,
                           String extensionsUrl) {
    // nossos valores
    this.extensionFilesPath = extensionFilesPath;
    this.cacheFilesPath = cacheFilesPath;
    this.classFilesPath = classFilesPath;
    this.extensionsUrl = extensionsUrl;
    // carrega as extens�es
    try {
      loadExtensions();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna true se a extens�o de nome 'extensionName' foi carregada.
   * @param extensionName String Nome da extens�o sem a extens�o do nome do arquivo.
   * @return boolean Retorna true se a extens�o de nome 'extensionName' foi
   *                 carregada.
   */
  public boolean contains(String extensionName) {
    // loop nas extens�es
    for (int i=0; i<extensions.length; i++) {
      // se � o nome procurado...retorna
      if (extensions[i].getName().equalsIgnoreCase(extensionName))
        return true;
    } // for
    // se chegou aqui...n�o encotnramos nada
    return false;
  }

  /**
   * Retorna o nome da classe que imeplementa a interface DemoService.
   * Retorna "" caso nenhum objeto das extens�es implemente esta interface.
   * @return String Retorna o nome da classe que imeplementa a interface
   *         DemoService. Retorna "" caso nenhum objeto das extens�es
   *         implemente esta interface.
   */
  public String demoServiceClassName() {
    return demoServiceClassName;
  }
  
  /**
   * Retorna um Extension[] contendo as extens�es carregadas.
   * @return Retorna um Extension[] contendo as extens�es carregadas.
   */
  public Extension[] extensions() {
    return extensions;
  }

  /**
   * Retorna a URL onde os recursos das extens�es podem ser localizados.
   * @return String Retorna a URL onde os recursos das extens�es podem ser localizados.
   */
  public String extensionsUrl() {
    return extensionsUrl;
  }

  public void finalize() {
    extensions = null;
  }

  /**
   * Retorna o caminho local onde os arquivos contidos nas extens�es ser�o
   * armazenados para futura utiliza��o.
   * @return Retorna o caminho local onde os arquivos contidos nas extens�es
   *         ser�o armazenados para futura utiliza��o.
   */
  public String getCacheFilesPath() {
    return cacheFilesPath;
  }

  /**
   * Retorna o caminho local onde os arquivos de Extens�es da aplica��o
   * est�o localizados.
   * @return Retorna o caminho local onde os arquivos de Extens�es da aplica��o
   *         est�o localizados.
   */
  public String getExtensionFilesPath() {
    return extensionFilesPath;
  }

  /**
   * Retorna a inst�ncia de ExtensionManager que est� sendo utilizada pela
   * aplica��o.
   * @return Retorna a inst�ncia de ExtensionManager que est� sendo utilizada
   *         pela aplica��o.
   */
  static public ExtensionManager getInstance() {
    return instance;
  }

  /**
   * Retorna a inst�ncia de ExtensionManager para ser utilizada pela aplica��o.
   * @param extensionFilesPath String Caminho local onde os arquivos de Extens�es
   *        da aplica��o est�o localizados.
   * @param cacheFilesPath String Caminho local onde os arquivos contidos
   *        nas extens�es ser�o armazenados para futura utiliza��o.
   * @param classFilesPath String Caminho local onde as classes das extens�es
   *        ser�o copiadas.
   * @param extensionsUrl Caminho relativo para ser usado como URL dos arquivos
   *                      da extens�o.
   * @return Retorna a inst�ncia de ExtensionManager para ser utilizada pela aplica��o.
   */
  static public ExtensionManager getInstance(String extensionFilesPath,
                                             String cacheFilesPath,
                                             String classFilesPath,
                                             String extensionsUrl) {
    if (instance == null)
      instance = new ExtensionManager(extensionFilesPath,
                                      cacheFilesPath,
                                      classFilesPath,
                                      extensionsUrl);
    return instance;
  }

  /**
   * Carrega as Extens�es.
   * @throws Exception Em caso de exce��o na tentativa de acesso aos caminhos
   *                   informados, extra��o dos arquivos e opera��es inerentes.
   */
  private void loadExtensions() throws Exception {
    // se j� carregamos as extens�es...dispara
    if (extensionsLoaded)
      return;
    // arquivos de extens�o encontrados
    String[] extensionFileExtension = {EXTENSION_FILE_EXTENSION};
    String[] extensionFileNames     = FileTools.getFileNames(extensionFilesPath, extensionFileExtension, false);
    // vetor para as extens�es
    Vector vectorExtensions = new Vector();
    // loop nos arquivos
    for (int i=0; i<extensionFileNames.length; i++) {
      // nome do arquivo da vez
      String extensionFileName = extensionFileNames[i];
      // cria o objeto que vai guardar as informa��es da extens�o
      Extension extension = new Extension(this,
                                          extensionFilesPath + extensionFileName,
                                          cacheFilesPath + extensionFileName.substring(0, extensionFileName.indexOf('.')) + File.separatorChar,
                                          classFilesPath,
                                          extensionsUrl + "/" + extensionFileName.substring(0, extensionFileName.indexOf('.')));
      // adiciona na lista
      vectorExtensions.add(extension);
    } // for
    // loop nas extens�es carregadas para inicializ�-las
    Vector vectorSystemListeners = new Vector();
    for (int i=0; i<vectorExtensions.size(); i++) {
      // extens�o da vez
      Extension extension = (Extension)vectorExtensions.elementAt(i);
      // inicializa
      extension.initialize();
      // a classe implementa o registro?
      if (!extension.registryClassName().equals("")) {
        // se j� temos a classe do Registro...exce��o
        if (!registryClassName.equals(""))
          throw new ExtendedException(getClass().getName(), "loadExtensions", "Mais de uma classe encontrada implementando o registro.");
        // se ainda n�o temos...guarda
        else
          registryClassName = extension.registryClassName();
      } // if
      // a classe implementa o servi�o de demonstra��o?
      if (!extension.demoServiceClassName().equals("")) {
        // se j� temos a classe do DemoService...exce��o
        if (!demoServiceClassName.equals(""))
          throw new ExtendedException(getClass().getName(), "loadExtensions", "Mais de uma classe encontrada implementando o servi�o de demonstra��o.");
        // se ainda n�o temos...guarda
        else
          demoServiceClassName = extension.demoServiceClassName();
      } // if
      // a classe implementa o servi�o de seguran�a?
      if (!extension.securityServiceClassName().equals("")) {
        // se j� temos a classe do SecurityService...exce��o
        if (!securityServiceClassName.equals(""))
          throw new ExtendedException(getClass().getName(), "loadExtensions", "Mais de uma classe encontrada implementando o servi�o de seguran�a.");
        // se ainda n�o temos...guarda
        else
          securityServiceClassName = extension.securityServiceClassName();
      } // if
    } // for
    // guarda as listas
    extensions = new Extension[vectorExtensions.size()];
    vectorExtensions.copyInto(extensions);
    // marca as extens�es como carregadas
    extensionsLoaded = true;
  }

  /**
   * Retorna o nome da classe que imeplementa a interface Registry.
   * Retorna "" caso nenhum objeto das extens�es implemente esta interface.
   * @return String Retorna o nome da classe que imeplementa a interface
   *         Registyr. Retorna "" caso nenhum objeto das extens�es
   *         implemente esta interface.
   */
  public String registryClassName() {
    return registryClassName;
  }

  /**
   * Retorna o nome da classe que imeplementa a interface SecurityService.
   * Retorna "" caso nenhum objeto das extens�es implemente esta interface.
   * @return String Retorna o nome da classe que imeplementa a interface
   *         SecurityService. Retorna "" caso nenhum objeto das extens�es
   *         implemente esta interface.
   */
  public String securityServiceClassName() {
    return securityServiceClassName;
  }

}
