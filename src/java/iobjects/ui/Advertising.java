package iobjects.ui;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.ui.*;

/**
 * Tem a fun��o de localizar e gerenciar os arquivos de propaganda.
 * Apenas o arquivo de propaganda informado ser� carregado.
 */
public class Advertising {

  private String                  advertisingName      = "";
  private String                  advertisingFilesPath = "";
  private String                  tempFilesPath        = "";
  private String                  tempFilesUrl         = "";
  private AdvertisingFile.Media[] mediaList            = {};

  /**
   * Define o caminho onde ser�o copiados os arquivos de media.
   */
  static public final String ADVERTISING_PATH = "advertising";
  /**
   * Define a extens�o dos arquivos de propaganda.
   */
  static public final String ADVERTISING_FILE_EXTENSION = ".advertising";

  /**
   * Construtor padr�o.
   * @param advertisingFilesPath String Caminho local onde os arquivos de
   *                             propaganda est�o localizados.
   * @param advertisingName String Nome da propaganda para utilizar.
   * @param tempFilesPath Caminho local tempor�rio da aplica��o. Neste caminho
   *                      ser�o copiados os arquivos de media.
   * @param tempFilesUrl URL dos arquivos tempor�rios da aplica��o.
   */
  public Advertising(String advertisingFilesPath,
                     String advertisingName,
                     String tempFilesPath,
                     String tempFilesUrl) {
    // nossos valores
    this.advertisingFilesPath = advertisingFilesPath;
    this.advertisingName = advertisingName;
    this.tempFilesPath = tempFilesPath;
    this.tempFilesUrl = tempFilesUrl;
    // carrega as extens�es
    try {
      loadAdvertisingFile();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  public void finalize() {
    mediaList = null;
  }

  /**
   * Retorna o nome da propaganda utilizada.
   * @return Retorna o nome da propaganda utilizada.
   */
  public String getAdvertisingName() {
    return advertisingName;
  }

  /**
   * Retorna o caminho local onde os arquivos de propaganda est�o localizados.
   * @return Retorna o caminho local onde os arquivos de propaganda
   *         est�o localizados.
   */
  public String getAdvertisingFilesPath() {
    return advertisingFilesPath;
  }

  /**
   * Retorna a pr�xima media que dever� ser exibida de acordo com as exibi��es
   * anteriores da propaganda.
   * <b>O �ndice da �ltima media retornada sempre ser� lida e gravada em
   * cookies.</b>
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return Media Retorna a pr�xima media que dever� ser exibida de acordo com
   *               as exibi��es anteriores da propaganda.
   * @throws Exception Em caso de exce��o na tentativa de leitura/escrita dos
   *                   cookies.
   */
  public AdvertisingFile.Media getNextMedia(HttpServletRequest  request,
                                            HttpServletResponse response) throws Exception {
    // se n�o temos medias...retorna vazio
    if (mediaList.length == 0)
      return null;
    // �ltima propaganda exibida
    int lastAdvertising = NumberTools.parseInt(HttpTools.getCookieValue(request, advertisingName, "-1"));
    // mostraremos a pr�xima
    lastAdvertising++;
    // se n�o temos mais medias...retorna para a primeira
    if (mediaList.length - 1 < lastAdvertising)
      lastAdvertising = 0;
    // salva esse �ndice
    HttpTools.setCookieValue(response, advertisingName, lastAdvertising + "", false);
    // retorna a media para exibir
    return mediaList[lastAdvertising];
  }

  /**
   * Carrega o arquivos de propaganda.
   * @throws Exception Em caso de exce��o na tentativa de acesso aos caminhos
   *                   informados, extra��o dos arquivos e opera��es inerentes.
   */
  private void loadAdvertisingFile() throws Exception {
    // arquivo informado
    File file = new File(advertisingFilesPath + advertisingName + ADVERTISING_FILE_EXTENSION);
    // se n�o existe...dispara
    if (!file.exists())
      return;
    // arquivo de propaganda desejado
    AdvertisingFile advertisingFile = new AdvertisingFile(file.getAbsolutePath());
    // lista tempor�ria de media
    Vector vector = new Vector();
    // loop nas medias
    for (int i=0; i<advertisingFile.size(); i++) {
      // media da vez
      AdvertisingFile.Media media = advertisingFile.get(i);
      // se n�o temos nada...continua
      if (media.getFilename().equals("") ||
          media.getType().equals("") ||
          media.getInterval() == 0)
        continue;
      // arquivo local da media
      File sourceMediaFile = new File(advertisingFilesPath + media.getFilename());
      // arquivo de destino da media
      File targetMediaFile = new File(tempFilesPath + ADVERTISING_PATH + File.separator + media.getFilename());
      // se ainda n�o temos o arquivo no diret�rio tempor�rio ou ele foi modificado...
      if (!targetMediaFile.exists() || (new Date(sourceMediaFile.lastModified())).after(new Date(targetMediaFile.lastModified()))) {
        // copia para o diret�rio tempor�rio da aplica��o
        FileTools.copyFile(sourceMediaFile, targetMediaFile);
      } // if
      // guarda sua URL
      media.setUrl(tempFilesUrl + "/" + ADVERTISING_PATH + "/" + media.getFilename().replace('\\', '/'));
      // adiciona a media na nossa lista
      vector.add(media);
    } // for
    // guarda nossa lista final de medias
    mediaList = new AdvertisingFile.Media[vector.size()];
    vector.copyInto(mediaList);
  }

  /**
   * Retorna a lista de medias a partir do arquivo de propaganda.
   * @return Media[] Retorna a lista de medias a partir do arquivo de propaganda.
   */
  public AdvertisingFile.Media[] mediaList() {
    return mediaList;
  }

}
