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
 * Tem a função de localizar as extensões da aplicação, gerenciá-las, extrair
 * seus arquivos em um diretório de cache e publicá-los.
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
   * Define a extensão dos arquivos de Extensão da aplicação.
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
    // carrega as extensões
    try {
      loadExtensions();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna true se a extensão de nome 'extensionName' foi carregada.
   * @param extensionName String Nome da extensão sem a extensão do nome do arquivo.
   * @return boolean Retorna true se a extensão de nome 'extensionName' foi
   *                 carregada.
   */
  public boolean contains(String extensionName) {
    // loop nas extensões
    for (int i=0; i<extensions.length; i++) {
      // se é o nome procurado...retorna
      if (extensions[i].getName().equalsIgnoreCase(extensionName))
        return true;
    } // for
    // se chegou aqui...não encotnramos nada
    return false;
  }

  /**
   * Retorna o nome da classe que imeplementa a interface DemoService.
   * Retorna "" caso nenhum objeto das extensões implemente esta interface.
   * @return String Retorna o nome da classe que imeplementa a interface
   *         DemoService. Retorna "" caso nenhum objeto das extensões
   *         implemente esta interface.
   */
  public String demoServiceClassName() {
    return demoServiceClassName;
  }
  
  /**
   * Retorna um Extension[] contendo as extensões carregadas.
   * @return Retorna um Extension[] contendo as extensões carregadas.
   */
  public Extension[] extensions() {
    return extensions;
  }

  /**
   * Retorna a URL onde os recursos das extensões podem ser localizados.
   * @return String Retorna a URL onde os recursos das extensões podem ser localizados.
   */
  public String extensionsUrl() {
    return extensionsUrl;
  }

  public void finalize() {
    extensions = null;
  }

  /**
   * Retorna o caminho local onde os arquivos contidos nas extensões serão
   * armazenados para futura utilização.
   * @return Retorna o caminho local onde os arquivos contidos nas extensões
   *         serão armazenados para futura utilização.
   */
  public String getCacheFilesPath() {
    return cacheFilesPath;
  }

  /**
   * Retorna o caminho local onde os arquivos de Extensões da aplicação
   * estão localizados.
   * @return Retorna o caminho local onde os arquivos de Extensões da aplicação
   *         estão localizados.
   */
  public String getExtensionFilesPath() {
    return extensionFilesPath;
  }

  /**
   * Retorna a instância de ExtensionManager que está sendo utilizada pela
   * aplicação.
   * @return Retorna a instância de ExtensionManager que está sendo utilizada
   *         pela aplicação.
   */
  static public ExtensionManager getInstance() {
    return instance;
  }

  /**
   * Retorna a instância de ExtensionManager para ser utilizada pela aplicação.
   * @param extensionFilesPath String Caminho local onde os arquivos de Extensões
   *        da aplicação estão localizados.
   * @param cacheFilesPath String Caminho local onde os arquivos contidos
   *        nas extensões serão armazenados para futura utilização.
   * @param classFilesPath String Caminho local onde as classes das extensões
   *        serão copiadas.
   * @param extensionsUrl Caminho relativo para ser usado como URL dos arquivos
   *                      da extensão.
   * @return Retorna a instância de ExtensionManager para ser utilizada pela aplicação.
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
   * Carrega as Extensões.
   * @throws Exception Em caso de exceção na tentativa de acesso aos caminhos
   *                   informados, extração dos arquivos e operações inerentes.
   */
  private void loadExtensions() throws Exception {
    // se já carregamos as extensões...dispara
    if (extensionsLoaded)
      return;
    // arquivos de extensão encontrados
    String[] extensionFileExtension = {EXTENSION_FILE_EXTENSION};
    String[] extensionFileNames     = FileTools.getFileNames(extensionFilesPath, extensionFileExtension, false);
    // vetor para as extensões
    Vector vectorExtensions = new Vector();
    // loop nos arquivos
    for (int i=0; i<extensionFileNames.length; i++) {
      // nome do arquivo da vez
      String extensionFileName = extensionFileNames[i];
      // cria o objeto que vai guardar as informações da extensão
      Extension extension = new Extension(this,
                                          extensionFilesPath + extensionFileName,
                                          cacheFilesPath + extensionFileName.substring(0, extensionFileName.indexOf('.')) + File.separatorChar,
                                          classFilesPath,
                                          extensionsUrl + "/" + extensionFileName.substring(0, extensionFileName.indexOf('.')));
      // adiciona na lista
      vectorExtensions.add(extension);
    } // for
    // loop nas extensões carregadas para inicializá-las
    Vector vectorSystemListeners = new Vector();
    for (int i=0; i<vectorExtensions.size(); i++) {
      // extensão da vez
      Extension extension = (Extension)vectorExtensions.elementAt(i);
      // inicializa
      extension.initialize();
      // a classe implementa o registro?
      if (!extension.registryClassName().equals("")) {
        // se já temos a classe do Registro...exceção
        if (!registryClassName.equals(""))
          throw new ExtendedException(getClass().getName(), "loadExtensions", "Mais de uma classe encontrada implementando o registro.");
        // se ainda não temos...guarda
        else
          registryClassName = extension.registryClassName();
      } // if
      // a classe implementa o serviço de demonstração?
      if (!extension.demoServiceClassName().equals("")) {
        // se já temos a classe do DemoService...exceção
        if (!demoServiceClassName.equals(""))
          throw new ExtendedException(getClass().getName(), "loadExtensions", "Mais de uma classe encontrada implementando o serviço de demonstração.");
        // se ainda não temos...guarda
        else
          demoServiceClassName = extension.demoServiceClassName();
      } // if
      // a classe implementa o serviço de segurança?
      if (!extension.securityServiceClassName().equals("")) {
        // se já temos a classe do SecurityService...exceção
        if (!securityServiceClassName.equals(""))
          throw new ExtendedException(getClass().getName(), "loadExtensions", "Mais de uma classe encontrada implementando o serviço de segurança.");
        // se ainda não temos...guarda
        else
          securityServiceClassName = extension.securityServiceClassName();
      } // if
    } // for
    // guarda as listas
    extensions = new Extension[vectorExtensions.size()];
    vectorExtensions.copyInto(extensions);
    // marca as extensões como carregadas
    extensionsLoaded = true;
  }

  /**
   * Retorna o nome da classe que imeplementa a interface Registry.
   * Retorna "" caso nenhum objeto das extensões implemente esta interface.
   * @return String Retorna o nome da classe que imeplementa a interface
   *         Registyr. Retorna "" caso nenhum objeto das extensões
   *         implemente esta interface.
   */
  public String registryClassName() {
    return registryClassName;
  }

  /**
   * Retorna o nome da classe que imeplementa a interface SecurityService.
   * Retorna "" caso nenhum objeto das extensões implemente esta interface.
   * @return String Retorna o nome da classe que imeplementa a interface
   *         SecurityService. Retorna "" caso nenhum objeto das extensões
   *         implemente esta interface.
   */
  public String securityServiceClassName() {
    return securityServiceClassName;
  }

}
