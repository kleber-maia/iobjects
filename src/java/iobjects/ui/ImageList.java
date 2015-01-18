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
package iobjects.ui;

import iobjects.*;

/**
 * Representa a lista de imagens disponíveis para uso pela aplicação.
 * @since 3.2
 */
public class ImageList {

  private String imageFilesLocalPath = "";
  private String imageFilesUrl       = "";

  /**
   * Imagem de comando 'voltar'.
   */
  static public final String COMMAND_BACK   = "images/commands/back.png";
  /**
   * Imagem de comando 'copiar'.
   */
  static public final String COMMAND_COPY   = "images/commands/copy.png";
  /**
   * Imagem de comando 'excluir'.
   */
  static public final String COMMAND_DELETE = "images/commands/delete.png";
  /**
   * Imagem de comando 'executar'.
   */
  static public final String COMMAND_EXECUTE = "images/commands/execute.png";
  /**
   * Imagem de comando 'ajuda'.
   */
  static public final String COMMAND_HELP = "images/commands/help.png";
  /**
   * Imagem de comando 'inserir'.
   */
  static public final String COMMAND_INSERT = "images/commands/insert.png";
  /**
   * Imagem de comando 'próximo'.
   */
  static public final String COMMAND_NEXT   = "images/commands/next.png";
  /**
   * Imagem de comando 'anterior'.
   */
  static public final String COMMAND_PREVIOUS = "images/commands/previous.png";
  /**
   * Imagem de comando 'colar'.
   */
  static public final String COMMAND_PASTE   = "images/commands/paste.png";
  /**
   * Imagem de comando 'imprimir'.
   */
  static public final String COMMAND_PRINT  = "images/commands/print.png";
  /**
   * Imagem de comando 'salvar'.
   */
  static public final String COMMAND_SAVE   = "images/commands/save.png";
  /**
   * Imagem de comando 'pesquisar'.
   */
  static public final String COMMAND_SEARCH = "images/commands/search.png";

  /**
   * Imagem 'balloon'.
   */
  static public final String IMAGE_BALLOON = "images/balloon16x16.png";

  /**
   * Imagem 'beta'.
   */
  static public final String IMAGE_BETA = "images/beta16x16.png";

  /**
   * Imagem 'calendar'.
   */
  static public final String IMAGE_CALENDAR = "images/calendar16x16.png";

  /**
   * Imagem 'centalPoint'.
   */
  static public final String IMAGE_CENTRAL_POINT = "images/centralpoint16x16.png";

  /**
   * Imagem 'displayMode'.
   */
  static public final String IMAGE_DISPLAY_MODE = "images/displaymode16x16.png";

  /**
   * Imagem 'download'.
   */
  static public final String IMAGE_DOWNLOAD = "images/download16x16.png";

  /**
   * Imagem 'entity'.
   */
  static public final String IMAGE_ENTITY = "images/entity16x16.png";

  /**
   * Imagem 'error'.
   */
  static public final String IMAGE_ERROR = "images/error16x16.png";

  /**
   * Imagem 'explorer'.
   */
  static public final String IMAGE_EXPLORER = "images/explorer16x16.png";

  /**
   * Imagem 'favorites'.
   */
  static public final String IMAGE_FAVORITES = "images/favorites16x16.png";

  /**
   * Imagem 'flowChart'.
   */
  static public final String IMAGE_FLOW_CHART = "images/flowchart16x16.png";

  /**
   * Imagem 'folder'.
   */
  static public final String IMAGE_FOLDER = "images/folder16x16.png";

  /**
   * Imagem 'graph'.
   */
  static public final String IMAGE_GRAPH = "images/graph16x16.png";

  /**
   * Imagem 'help'.
   */
  static public final String IMAGE_HELP = "images/help16x16.png";

  /**
   * Imagem 'history'.
   */
  static public final String IMAGE_HISTORY = "images/history16x16.png";

  /**
   * Imagem 'information'.
   */
  static public final String IMAGE_INFORMATION = "images/information16x16.png";

  /**
   * Imagem 'logoff'.
   */
  static public final String IMAGE_LOGOFF = "images/logoff16x16.png";

  /**
   * Imagem 'mail'.
   */
  static public final String IMAGE_MAIL = "images/mail16x16.png";

  /**
   * Imagem 'map'.
   */
  static public final String IMAGE_MAP = "images/map16x16.png";

  /**
   * Imagem 'masterRelation'.
   */
  static public final String IMAGE_MASTER_RELATION = "images/masterrelation16x16.png";

  /**
   * Imagem 'myDesktop'.
   */
  static public final String IMAGE_MY_DESKTOP = "images/mydesktop16x16.png";

  /**
   * Imagem 'no'.
   */
  static public final String IMAGE_NO = "images/no16x16.png";

  /**
   * Imagem 'note'.
   */
  static public final String IMAGE_NOTE = "images/note16x16.png";

  /**
   * Imagem 'presentation'.
   */
  static public final String IMAGE_PRESENTATION = "images/presentation16x16.png";

  /**
   * Imagem 'printer'.
   */
  static public final String IMAGE_PRINTER = "images/printer16x16.png";

  /**
   * Imagem 'process'.
   */
  static public final String IMAGE_PROCESS = "images/process16x16.png";

  /**
   * Imagem 'release'.
   */
  static public final String IMAGE_RELEASE = "images/release16x16.png";

  /**
   * Imagem 'report'.
   */
  static public final String IMAGE_REPORT = "images/report16x16.png";

  /**
   * Imagem 'securityService'.
   */
  static public final String IMAGE_SECURITY_SERVICE = "images/securityservice16x16.png";

  /**
   * Imagem 'style'.
   */
  static public final String IMAGE_STYLE = "images/style16x16.png";

  /**
   * Imagem 'systemInformation'.
   */
  static public final String IMAGE_SYSTEM_INFORMATION = "images/systeminformation16x16.png";

  /**
   * Imagem 'upload'.
   */
  static public final String IMAGE_UPLOAD = "images/upload16x16.png";

  /**
   * Imagem 'user'.
   */
  static public final String IMAGE_USER = "images/user16x16.png";

  /**
   * Imagem 'warning'.
   */
  static public final String IMAGE_WARNING = "images/warning16x16.png";

  /**
   * Imagem 'yes'.
   */
  static public final String IMAGE_YES = "images/yes16x16.png";

  /**
   * Construtor padrão.
   * @param imageFilesLocalPath String Caminho local onde os arquivos de imagem
   *                            estão localizados.
   * @param imageFilesUrl String Caminho relativo para ser usado como URL dos
   *                      arquivos de imagem.
   */
  public ImageList(String imageFilesLocalPath,
                   String imageFilesUrl) {
    this.imageFilesLocalPath = imageFilesLocalPath;
    this.imageFilesUrl = imageFilesUrl;
  }

  /**
   * Retorna a URL para a imagem especificada.
   * @param fileName String Nome do arquivo de imagem cuja URL se deseja obter.
   * @return String Retorna a URL para a imagem especificada.
   */
  public String getImageUrl(String fileName) {
    return imageFilesUrl + "/" + fileName;
  }

  static public String getImageUrl(Action action, boolean small) {
    String result = "";
    switch (action.getCategory()) {
      case Action.CATEGORY_CARD   : result = "images/card";  break;
      case Action.CATEGORY_ENTITY : result = "images/entity";  break;
      case Action.CATEGORY_PROCESS: result = "images/process"; break;
      case Action.CATEGORY_REPORT : result = "images/report";  break;
    } // switch
    return result + (action.getJustReleased() ? "release" : "") + (small ? "16x16" : "32x32") + ".png";
  }
  
}
