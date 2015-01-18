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
import iobjects.xml.ui.*;

/**
 * Tem a função de localizar os sites do portal da aplicação, gerenciá-los,
 * extrair seus arquivos em um diretório de cache e publicá-los.
 */
public class SiteManager {

  static private SiteManager instance    = null;
  static private boolean     sitesLoaded = false;

  private String      cacheFilesPath = "";
  private String      classFilesPath = "";
  private Site[]      sites          = {};
  private String      siteFilesPath  = "";
  private String      sitesUrl       = "";
  private PortalFile  portalFile     = null;

  /**
   * Define a extensão dos arquivos de Site da aplicação.
   */
  static public final String SITE_FILE_EXTENSION = ".iobjects";

  private SiteManager(String portalFilePath,
                      String siteFilesPath,
                      String cacheFilesPath,
                      String classFilesPath,
                      String sitesUrl) {
    // nossos valores
    this.siteFilesPath  = siteFilesPath;
    this.cacheFilesPath = cacheFilesPath;
    this.classFilesPath = classFilesPath;
    this.sitesUrl       = sitesUrl;

    try {
      // nosso arquivo de configurações
      portalFile = new PortalFile(portalFilePath);
      // carrega os sites
      loadSites();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  public void finalize() {
    cacheFilesPath           = null;
    classFilesPath           = null;
    sites               = null;
    siteFilesPath       = null;
    sitesUrl            = null;
  }

  /**
   * Retorna o Site representado por 'siteName'.
   * @param siteName String Nome do Site que se deseja retornar.
   * @return Site Retorna o Site representado por 'siteName'.
   */
  public Site get(String siteName) {
    // loop nas extensões
    for (int i=0; i<sites.length; i++) {
      // se é o nome procurado...retorna
      if (sites[i].getName().equalsIgnoreCase(siteName))
        return sites[i];
    } // for
    // se chegou aqui...não encotnramos nada
    return null;
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
   * Retorna a instância de SiteManager que está sendo utilizada pela
   * aplicação.
   * @return Retorna a instância de SiteManager que está sendo utilizada
   *         pela aplicação.
   */
  static public SiteManager getInstance() {
    return instance;
  }

  /**
   * Retorna a instância de SiteManager que está sendo usada pela aplicação.
   * @param portalFilePath Caminho do arquivo de configuração de portal.
   * @param siteFilesPath String Caminho local onde os arquivos de Extensões
   *        da aplicação estão localizados.
   * @param cacheFilesPath String Caminho local onde os arquivos contidos
   *        nas extensões serão armazenados para futura utilização.
   * @param classFilesPath String Caminho local onde as classes das extensões
   *        serão copiadas.
   * @param sitesUrl Caminho relativo para ser usado como URL dos arquivos
   *                      da extensão.
   * @return SiteManager Retorna a instância de SiteManager que está sendo
   *                       usada pela aplicação.
   * @throws Exception Em caso de exceção na tentativa de leitura do arquivo
   *                   de configuração de portal.
   */
  static public SiteManager getInstance(String portalFilePath,
                                        String siteFilesPath,
                                        String cacheFilesPath,
                                        String classFilesPath,
                                        String sitesUrl) throws Exception {
    if (instance == null)
      instance = new SiteManager(portalFilePath,
                                 siteFilesPath,
                                 cacheFilesPath,
                                 classFilesPath,
                                 sitesUrl);
    return instance;
  }

  /**
   * Retorna o caminho local onde os arquivos de Extensões da aplicação
   * estão localizados.
   * @return Retorna o caminho local onde os arquivos de Extensões da aplicação
   *         estão localizados.
   */
  public String getSiteFilesPath() {
    return siteFilesPath;
  }

  /**
   * Carrega as Extensões.
   * @throws Exception Em caso de exceção na tentativa de acesso aos caminhos
   *                   informados, extração dos arquivos e operações inerentes.
   */
  private void loadSites() throws Exception {
    // se já carregamos as extensões...dispara
    if (sitesLoaded)
      return;
    // arquivos de extensão encontrados
    String[] siteFileSite  = {SITE_FILE_EXTENSION};
    String[] siteFileNames = FileTools.getFileNames(siteFilesPath, siteFileSite, false);
    // vetor para as extensões
    Vector vectorSites = new Vector();
    // loop nos arquivos
    for (int i=0; i<siteFileNames.length; i++) {
      // nome do arquivo da vez
      String siteFileName = siteFileNames[i];
      // cria o objeto que vai guarda as informações da extensão
      Site site = new Site(this,
                                          siteFilesPath + siteFileName,
                                          cacheFilesPath + siteFileName.substring(0, siteFileName.indexOf('.')) + File.separatorChar,
                                          classFilesPath,
                                          sitesUrl + "/" + siteFileName.substring(0, siteFileName.indexOf('.')));
      // adiciona na lista
      vectorSites.add(site);
    } // for
    // loop nas extensões carregadas para inicializá-las
    for (int i=0; i<vectorSites.size(); i++) {
      // extensão da vez
      Site site = (Site)vectorSites.elementAt(i);
      // inicializa
      site.initialize();
    } // for
    // guarda a lista de extensões
    sites = new Site[vectorSites.size()];
    vectorSites.copyInto(sites);
    // marca as extensões como carregadas
    sitesLoaded = true;
  }

  /**
   * Retorna a instância de SiteFile contendo as configurações de portal.
   * @return SiteFile Retorna a instância de SiteFile contendo as
   *         configurações de portal.
   */
  public PortalFile portalFile() {
    return portalFile;
  }

  /**
   * Retorna um Site[] contendo as extensões carregadas.
   * @return Retorna um Site[] contendo as extensões carregadas.
   */
  public Site[] sites() {
    return sites;
  }

  /**
   * Retorna a URL onde os recursos das extensões podem ser localizados.
   * @return String Retorna a URL onde os recursos das extensões podem ser localizados.
   */
  public String sitesUrl() {
    return sitesUrl;
  }

}
