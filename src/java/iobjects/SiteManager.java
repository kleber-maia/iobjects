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
 * Tem a fun��o de localizar os sites do portal da aplica��o, gerenci�-los,
 * extrair seus arquivos em um diret�rio de cache e public�-los.
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
   * Define a extens�o dos arquivos de Site da aplica��o.
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
      // nosso arquivo de configura��es
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
    // loop nas extens�es
    for (int i=0; i<sites.length; i++) {
      // se � o nome procurado...retorna
      if (sites[i].getName().equalsIgnoreCase(siteName))
        return sites[i];
    } // for
    // se chegou aqui...n�o encotnramos nada
    return null;
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
   * Retorna a inst�ncia de SiteManager que est� sendo utilizada pela
   * aplica��o.
   * @return Retorna a inst�ncia de SiteManager que est� sendo utilizada
   *         pela aplica��o.
   */
  static public SiteManager getInstance() {
    return instance;
  }

  /**
   * Retorna a inst�ncia de SiteManager que est� sendo usada pela aplica��o.
   * @param portalFilePath Caminho do arquivo de configura��o de portal.
   * @param siteFilesPath String Caminho local onde os arquivos de Extens�es
   *        da aplica��o est�o localizados.
   * @param cacheFilesPath String Caminho local onde os arquivos contidos
   *        nas extens�es ser�o armazenados para futura utiliza��o.
   * @param classFilesPath String Caminho local onde as classes das extens�es
   *        ser�o copiadas.
   * @param sitesUrl Caminho relativo para ser usado como URL dos arquivos
   *                      da extens�o.
   * @return SiteManager Retorna a inst�ncia de SiteManager que est� sendo
   *                       usada pela aplica��o.
   * @throws Exception Em caso de exce��o na tentativa de leitura do arquivo
   *                   de configura��o de portal.
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
   * Retorna o caminho local onde os arquivos de Extens�es da aplica��o
   * est�o localizados.
   * @return Retorna o caminho local onde os arquivos de Extens�es da aplica��o
   *         est�o localizados.
   */
  public String getSiteFilesPath() {
    return siteFilesPath;
  }

  /**
   * Carrega as Extens�es.
   * @throws Exception Em caso de exce��o na tentativa de acesso aos caminhos
   *                   informados, extra��o dos arquivos e opera��es inerentes.
   */
  private void loadSites() throws Exception {
    // se j� carregamos as extens�es...dispara
    if (sitesLoaded)
      return;
    // arquivos de extens�o encontrados
    String[] siteFileSite  = {SITE_FILE_EXTENSION};
    String[] siteFileNames = FileTools.getFileNames(siteFilesPath, siteFileSite, false);
    // vetor para as extens�es
    Vector vectorSites = new Vector();
    // loop nos arquivos
    for (int i=0; i<siteFileNames.length; i++) {
      // nome do arquivo da vez
      String siteFileName = siteFileNames[i];
      // cria o objeto que vai guarda as informa��es da extens�o
      Site site = new Site(this,
                                          siteFilesPath + siteFileName,
                                          cacheFilesPath + siteFileName.substring(0, siteFileName.indexOf('.')) + File.separatorChar,
                                          classFilesPath,
                                          sitesUrl + "/" + siteFileName.substring(0, siteFileName.indexOf('.')));
      // adiciona na lista
      vectorSites.add(site);
    } // for
    // loop nas extens�es carregadas para inicializ�-las
    for (int i=0; i<vectorSites.size(); i++) {
      // extens�o da vez
      Site site = (Site)vectorSites.elementAt(i);
      // inicializa
      site.initialize();
    } // for
    // guarda a lista de extens�es
    sites = new Site[vectorSites.size()];
    vectorSites.copyInto(sites);
    // marca as extens�es como carregadas
    sitesLoaded = true;
  }

  /**
   * Retorna a inst�ncia de SiteFile contendo as configura��es de portal.
   * @return SiteFile Retorna a inst�ncia de SiteFile contendo as
   *         configura��es de portal.
   */
  public PortalFile portalFile() {
    return portalFile;
  }

  /**
   * Retorna um Site[] contendo as extens�es carregadas.
   * @return Retorna um Site[] contendo as extens�es carregadas.
   */
  public Site[] sites() {
    return sites;
  }

  /**
   * Retorna a URL onde os recursos das extens�es podem ser localizados.
   * @return String Retorna a URL onde os recursos das extens�es podem ser localizados.
   */
  public String sitesUrl() {
    return sitesUrl;
  }

}
