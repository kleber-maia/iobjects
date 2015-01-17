package iobjects.ui;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.ui.*;

/**
 * Tem a função de localizar e gerenciar os arquivos de propaganda.
 * Apenas o arquivo de propaganda informado será carregado.
 */
public class Advertising {

  private String                  advertisingName      = "";
  private String                  advertisingFilesPath = "";
  private String                  tempFilesPath        = "";
  private String                  tempFilesUrl         = "";
  private AdvertisingFile.Media[] mediaList            = {};

  /**
   * Define o caminho onde serão copiados os arquivos de media.
   */
  static public final String ADVERTISING_PATH = "advertising";
  /**
   * Define a extensão dos arquivos de propaganda.
   */
  static public final String ADVERTISING_FILE_EXTENSION = ".advertising";

  /**
   * Construtor padrão.
   * @param advertisingFilesPath String Caminho local onde os arquivos de
   *                             propaganda estão localizados.
   * @param advertisingName String Nome da propaganda para utilizar.
   * @param tempFilesPath Caminho local temporário da aplicação. Neste caminho
   *                      serão copiados os arquivos de media.
   * @param tempFilesUrl URL dos arquivos temporários da aplicação.
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
    // carrega as extensões
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
   * Retorna o caminho local onde os arquivos de propaganda estão localizados.
   * @return Retorna o caminho local onde os arquivos de propaganda
   *         estão localizados.
   */
  public String getAdvertisingFilesPath() {
    return advertisingFilesPath;
  }

  /**
   * Retorna a próxima media que deverá ser exibida de acordo com as exibições
   * anteriores da propaganda.
   * <b>O índice da última media retornada sempre será lida e gravada em
   * cookies.</b>
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return Media Retorna a próxima media que deverá ser exibida de acordo com
   *               as exibições anteriores da propaganda.
   * @throws Exception Em caso de exceção na tentativa de leitura/escrita dos
   *                   cookies.
   */
  public AdvertisingFile.Media getNextMedia(HttpServletRequest  request,
                                            HttpServletResponse response) throws Exception {
    // se não temos medias...retorna vazio
    if (mediaList.length == 0)
      return null;
    // última propaganda exibida
    int lastAdvertising = NumberTools.parseInt(HttpTools.getCookieValue(request, advertisingName, "-1"));
    // mostraremos a próxima
    lastAdvertising++;
    // se não temos mais medias...retorna para a primeira
    if (mediaList.length - 1 < lastAdvertising)
      lastAdvertising = 0;
    // salva esse índice
    HttpTools.setCookieValue(response, advertisingName, lastAdvertising + "", false);
    // retorna a media para exibir
    return mediaList[lastAdvertising];
  }

  /**
   * Carrega o arquivos de propaganda.
   * @throws Exception Em caso de exceção na tentativa de acesso aos caminhos
   *                   informados, extração dos arquivos e operações inerentes.
   */
  private void loadAdvertisingFile() throws Exception {
    // arquivo informado
    File file = new File(advertisingFilesPath + advertisingName + ADVERTISING_FILE_EXTENSION);
    // se não existe...dispara
    if (!file.exists())
      return;
    // arquivo de propaganda desejado
    AdvertisingFile advertisingFile = new AdvertisingFile(file.getAbsolutePath());
    // lista temporária de media
    Vector vector = new Vector();
    // loop nas medias
    for (int i=0; i<advertisingFile.size(); i++) {
      // media da vez
      AdvertisingFile.Media media = advertisingFile.get(i);
      // se não temos nada...continua
      if (media.getFilename().equals("") ||
          media.getType().equals("") ||
          media.getInterval() == 0)
        continue;
      // arquivo local da media
      File sourceMediaFile = new File(advertisingFilesPath + media.getFilename());
      // arquivo de destino da media
      File targetMediaFile = new File(tempFilesPath + ADVERTISING_PATH + File.separator + media.getFilename());
      // se ainda não temos o arquivo no diretório temporário ou ele foi modificado...
      if (!targetMediaFile.exists() || (new Date(sourceMediaFile.lastModified())).after(new Date(targetMediaFile.lastModified()))) {
        // copia para o diretório temporário da aplicação
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
