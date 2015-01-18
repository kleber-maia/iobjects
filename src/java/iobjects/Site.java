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

import iobjects.ui.*;
import iobjects.util.*;

/**
 * Representa uma Extens�o da aplica��o. Extens�es s�o componentes externos que
 * cont�m objetos de neg�cio e suas interfaces.
 */
public class Site {

  static private final String LAST_MODIFIED_DATE = "lastModifiedDate";

  private String      cacheFilesPath = "";
  private String      classFilesPath = "";
  private SiteManager siteManager    = null;
  private String      fileName       = "";
  private ImageList   imageList      = null;
  private String      name           = "";
  private String      url            = "";

  /**
   * Construtor padr�o.
   * @param siteManager SiteManager respons�vel pelo gerenciamento do
   *                                site.
   * @param fileName String Nome do arquivo do site.
   * @param cacheFilesPath Caminho local onde os arquivos do site ser�o
   *        extra�dos.
   * @param classFilesPath Caminho local onde os arquivos de classes do site
   *        ser�o copiados.
   * @param url Caminho relativo para ser usado como URL dos arquivos
   *            do site.
   */
  public Site(SiteManager siteManager,
              String      fileName,
              String      cacheFilesPath,
              String      classFilesPath,
              String      url) {
    // nossos valores
    this.siteManager = siteManager;
    this.fileName = fileName;
    this.cacheFilesPath = cacheFilesPath;
    this.classFilesPath = classFilesPath;
    this.url = url;
    // nosso nome
    name = fileName.substring(fileName.lastIndexOf(File.separatorChar)+1);
    name = name.substring(0, name.indexOf('.'));
    try {
      // carrega o site
      loadSite();
      // cria nosso ImageList
      imageList = new ImageList(cacheFilesPath + Facade.IMAGES_PATH + File.separatorChar,
                                url + "/" + Facade.IMAGES_PATH);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    cacheFilesPath = null;
    classFilesPath = null;
    fileName       = null;
    siteManager    = null;
    imageList      = null;
    url            = null;
  }

  /**
   * Retorna o caminho local onde os arquivos do site ser�o extraidos.
   * @return String Retorna o caminho local onde os arquivos do site ser�o extraidos.
   */
  public String getCacheFilesPath() {
    return cacheFilesPath;
  }

  /**
   * Retorna o SiteManager respons�vel pelo gerenciamento do Site.
   * @return Retorna o SiteManager respons�vel pelo gerenciamento do
   *         Site.
   */
  public SiteManager getSiteManager() {
    return siteManager;
  }

  /**
   * Retorna o nome do arquivo da Extens�o.
   * @return Retorna o nome do arquivo da Extens�o.
   */
  public String getFileName() {
    return fileName;
  }

  public String getName() {
    return name;
  }

  /**
   * Retorna o caminho relativo para ser usado como URL dos arquivos do site.
   * @return String Retorna o caminho relativo para ser usado como URL dos
   *         arquivos do site.
   */
  public String getUrl() {
    return url;
  }

  /**
   * Retorna a inst�ncia de ImageList que mant�m as imagens do site.
   * @return ImageList Retorna a inst�ncia de ImageList que mant�m as imagens
   *         do site.
   */
  public ImageList imageList() {
    return imageList;
  }

  /**
   * Inicializa o site.
   * @throws Exception Em caso de exce��o na tentativa de inicializar o site
   *                   ou suas classes.
   */
  public void initialize() throws Exception {
  }

  /**
   * Carrega o site.
   * @throws Exception Em caso de exce��o na tentativa de extrair os arquivos
   *                   do site ou suas classes.
   */
  private void loadSite() throws Exception {
    // acesso ao arquivo do site
    File file = new File(fileName);
    // nome do arquivo de par�metros do site
    String paramListFileName = cacheFilesPath + ".." + File.separatorChar + name + ".params";
    // carrega os par�metros do site
    ParamList paramList = new ParamList();
    paramList.load(paramListFileName);
    // par�metro que cont�m a data de modifica��o do site que se encontra extra�da
    Param paramLastModifiedDate = paramList.get(LAST_MODIFIED_DATE);
    // se ainda n�o temos o par�metro...
    if (paramLastModifiedDate == null) {
      // cria o par�metro com data zero, ou seja, como se nunca tivesse sido extra�da
      paramLastModifiedDate = new Param(LAST_MODIFIED_DATE, "0");
      // adiciona na lista
      paramList.add(paramLastModifiedDate);
    } // if
    // se o site foi modificado ap�s a �ltima extra��o...devemos extrai-lo
    boolean extract = file.lastModified() > Long.parseLong(paramLastModifiedDate.getValue());

    // se devemos extrair os arquivos do site...
    if (extract) {
      // apaga os arquivos de cache atualmente existentes
      String[] cacheSites = {"*"};
      File[] cacheFiles = FileTools.getFiles(cacheFilesPath, cacheSites, true);
      for (int i=cacheFiles.length-1; i>=0; i--) {
        cacheFiles[i].delete();
      } // for
      // descomprime o arquivo
      UnZip unZip = new UnZip(fileName, cacheFilesPath);
      // salva a data de modifica��o do site nos par�metros
      paramLastModifiedDate.setValue(file.lastModified() + "");
      paramList.store(paramListFileName);
    } // if

    // caminho raiz das classes do site
    String classFilesRootPath = cacheFilesPath + "WEB-INF" + File.separatorChar + "classes" + File.separatorChar;
    // adiciona o caminho das classes da extens�o ao ClassPath
    SystemTools.addClassPath(this, classFilesRootPath);
  }

}
