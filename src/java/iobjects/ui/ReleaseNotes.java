package iobjects.ui;

import java.io.*;
import java.util.*;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.ui.*;

/**
 * Representa as notas de vers�o e mudan�as da aplica��o.
 * @since 2006 R1
 */
public class ReleaseNotes implements Comparator {

  static private ReleaseNotes instance = null;

  private String                     releaseNotesFilesPath  = "";
  private ReleaseNotesFile.Release[] releaseList            = {};

  private ReleaseNotes(String releaseNotesFilesPath) {
    // nossos valores
    this.releaseNotesFilesPath = releaseNotesFilesPath;
    // carrega as notas de vers�o
    try {
      loadReleaseNotes();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  public int compare(Object o1, Object o2) {
    // avisos
    ReleaseNotesFile.Release release1 = (ReleaseNotesFile.Release)o1;
    ReleaseNotesFile.Release release2 = (ReleaseNotesFile.Release)o2;
    // p�e na frente as notas de vers�o mais recentes
    return release2.getDate().compareTo(release1.getDate());
  }

  public boolean equals(Object obj) {
    return false;
  }

  public void finalize() {
    releaseList = null;
  }

  /**
   * Retorna a inst�ncia de ReleaseNotes para ser utilizada pela aplica��o.
   * @param releaseNotesFilesPath String Caminho local onde se encontra o arquivo
   *                              de notas de vers�o.
   * @return ReleaseNotes Retorna a inst�ncia de ReleaseNotes para ser utilizada
   *                      pela aplica��o.
   */
  static public synchronized ReleaseNotes getInstace(String releaseNotesFilesPath) {
    // se n�o temos nossa inst�ncia...cria
    if (instance == null)
      instance = new ReleaseNotes(releaseNotesFilesPath);
    // retorna
    return instance;
  }

  /**
   * Retorna o caminho local onde o arquivo de notas de vers�o est� localizado.
   * @return Retorna o caminho local onde o arquivo de notas de vers�o est� localizado.
   */
  public String getReleaseNotesFilesPath() {
    return releaseNotesFilesPath;
  }

  /**
   * Carrega as notas de vers�o.
   * @throws Exception Em caso de exce��o na tentativa de acesso aos caminhos
   *                   informados, extra��o dos arquivos e opera��es inerentes.
   */
  private void loadReleaseNotes() throws Exception {
    // arquivo de notas de vers�o
    ReleaseNotesFile releaseNotesFile = new ReleaseNotesFile(releaseNotesFilesPath);
    // lista tempor�ria de avisos
    Vector vector = new Vector();
    // se temos notas de vers�o...
    if (releaseNotesFile.size() > 0) {
      // adiciona as notas de vers�o na lista tempor�ria
      for (int i = 0; i < releaseNotesFile.size(); i++)
        vector.add(releaseNotesFile.get(i));
      // guarda nossa lista final de avisos
      releaseList = new ReleaseNotesFile.Release[vector.size()];
      vector.copyInto(releaseList);
      // ordena
      Arrays.sort(releaseList, this);
    }
    // se n�o temos nenhuma nota de vers�o...cria uma padr�o
    else {
      releaseList = new ReleaseNotesFile.Release[]{new ReleaseNotesFile.Release("n/a", DateTools.getCalculatedDays(-1), DateTools.getCalculatedDays(-1))};
    } // if
  }

  /**
   * Retorna a nota de vers�o mais recente.
   * @return Retorna a nota de vers�o mais recente.
   */
  public ReleaseNotesFile.Release mostRecentRelease() {
    return releaseList[0];
  }

  /**
   * Retorna a lista de notas de vers�o da aplica��o, ordenadas da mais recente
   * para a mais antiga.
   * @return Retorna a lista de notas de vers�o da aplica��o, ordenadas da mais
   *         recente para a mais antiga.
   */
  public ReleaseNotesFile.Release[] releaseList() {
    return releaseList;
  }

}
