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

import java.io.*;
import java.util.*;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.ui.*;

/**
 * Tem a função de localizar e gerenciar os arquivos de quadro de avisos.
 * Apenas o quadro de avisos padrão e o quadro de avisos de mesmo nome da
 * conexão padrão do usuário serão carregados.
 * @since 2006 R1
 */
public class NoticeBoard implements Comparator {

  private String defaultConnectionName = "";
  private String noticeBoardFilesPath  = "";
  private Vector vector                = new Vector();

  /**
   * Define a extensão dos arquivos de quadro de avisos.
   */
  public final String NOTICEBOARD_FILE_EXTENSION = ".notice";
  /**
   * Define o nome do arquivo do quadro de avisos padrão.
   */
  public final String DEFAULT_NOTICEBOARD_FILE_NAME = "noticeboard";

  /**
   * Construtor padrão.
   * @param noticeBoardFilesPath String Caminho local onde os arquivos de
   *                             quadro de avisos estão localizados.
   * @param defaultConnectionName String Nome da conexão padrão.
   */
  public NoticeBoard(String noticeBoardFilesPath,
                     String defaultConnectionName) {
    // nossos valores
    this.noticeBoardFilesPath = noticeBoardFilesPath;
    this.defaultConnectionName = defaultConnectionName;
    // carrega as extensões
    try {
      loadNoticeBoardFiles();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Adiciona o aviso 'notice' ao quadro de avisos do usuário que efetuou logon.
   * @param notice Aviso para ser adicionado.
   */
  public void add(boolean warning, Date date, Date emphasize, String title) {
    // adiciona
    vector.add(new NoticeBoardFile.Notice(warning, date, emphasize, title));
    // reordena a lista
    sort();
  }

  public int compare(Object o1, Object o2) {
    // avisos
    NoticeBoardFile.Notice notice1 = (NoticeBoardFile.Notice)o1;
    NoticeBoardFile.Notice notice2 = (NoticeBoardFile.Notice)o2;
    // põe na frente os avisos mais recentes
    return notice2.getDate().compareTo(notice1.getDate());
  }

  public boolean equals(Object obj) {
    return false;
  }

  public void finalize() {
    vector.clear();
  }

  /**
   * Retorna a lista de avisos obtidos a partir dos arquivos de quadro de avisos
   * localizados.
   * @return Notice[] Retorna a lista de avisos obtidos a partir dos arquivos de
   *         quadro de avisos localizados.
   */
  public NoticeBoardFile.Notice get(int index) {
    // retorna
    return (NoticeBoardFile.Notice)vector.elementAt(index);
  }

  /**
   * Retorna o nome da conexão padrão.
   * @return Retorna o nome da conexão padrão.
   */
  public String getDefaultConnectionName() {
    return defaultConnectionName;
  }

  /**
   * Retorna o caminho local onde os arquivos de quadro de avisos estão localizados.
   * @return Retorna o caminho local onde os arquivos de quadro de avisos
   *         estão localizados.
   */
  public String getNoticeBoardFilesPath() {
    return noticeBoardFilesPath;
  }

  /**
   * Carrega os arquivos de quadro de avisos.
   * @throws Exception Em caso de exceção na tentativa de acesso aos caminhos
   *                   informados, extração dos arquivos e operações inerentes.
   */
  private void loadNoticeBoardFiles() throws Exception {
    // arquivo de quadro de avisos da conexão default
    File defaultConnectionFile = new File(noticeBoardFilesPath + defaultConnectionName + NOTICEBOARD_FILE_EXTENSION);
    // arquivo de quadro de avisos padrão
    File defaultFile = new File(noticeBoardFilesPath + DEFAULT_NOTICEBOARD_FILE_NAME + NOTICEBOARD_FILE_EXTENSION);
    // carrega os avisos da conexão padrão
    if (defaultConnectionFile.exists()) {
      // carrega o quadro de avisos
      NoticeBoardFile noticeBoardFile = new NoticeBoardFile(defaultConnectionFile.getAbsolutePath());
      // adiciona os avisos na lista temporária
      for (int i=0; i<noticeBoardFile.size(); i++)
        vector.add(noticeBoardFile.get(i));
    } // if
    // carrega os avisos para todos
    if (defaultFile.exists()) {
      // carrega o quadro de avisos
      NoticeBoardFile noticeBoardFile = new NoticeBoardFile(defaultFile.getAbsolutePath());
      // adiciona os avisos na lista temporária
      for (int i=0; i<noticeBoardFile.size(); i++)
        vector.add(noticeBoardFile.get(i));
    } // if
    // ordena
    sort();
  }

  /**
   * Retorna a quantidade de avisos existentes.
   * @return Retorna a quantidade de avisos existentes.
   */
  public int size() {
    return vector.size();
  }

  /**
   * Ordena os avisos existentes.
   */
  public void sort() {
    // guarda nossa lista final de avisos
    NoticeBoardFile.Notice[] sortList = new NoticeBoardFile.Notice[vector.size()];
    vector.copyInto(sortList);
    // ordena
    Arrays.sort(sortList, this);
    // copia de volta
    vector.clear();
    for (int i=0; i<sortList.length; i++)
      vector.add(sortList[i]);
  }

}
