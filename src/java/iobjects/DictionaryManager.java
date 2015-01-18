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

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.ui.*;

/**
 * Tem a fun��o de localizar e gerenciar os arquivos de dicion�rios.
 */
public class DictionaryManager {

  private String           dictionaryFilesPath = "";
  private DictionaryFile[] dictionaryFiles     = {};

  static private DictionaryManager instance = null;

  /**
   * Define a extens�o dos arquivos de dicion�rio.
   */
  public final String DICTIONARY_FILE_EXTENSION = ".dictionary";

  private DictionaryManager(String dictionaryFilesPath) {
    // nossos valores
    this.dictionaryFilesPath = dictionaryFilesPath;
    // carrega as extens�es
    try {
      loadDictionaryFiles();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna o caminho local onde os arquivos de dicion�rios est�o localizados.
   * @return Retorna o caminho local onde os arquivos de dicion�rios
   *         est�o localizados.
   */
  public String getDictionaryFilesPath() {
    return dictionaryFilesPath;
  }

  /**
   * Retorna a inst�ncia de Dictionary que est� sendo utilizada pela
   * aplica��o.
   * @return Retorna a inst�ncia de Dictionary que est� sendo utilizada
   *         pela aplica��o.
   */
  static public DictionaryManager getInstance() {
    return instance;
  }

  /**
   * Retorna a inst�ncia de Dictionary para ser utilizada pela aplica��o.
   * @param dictionaryFilesPath Caminho local onde os arquivos de dicion�rios 
   *                            est�o localizados.
   * @return Retorna a inst�ncia de ExtensionManager para ser utilizada pela aplica��o.
   */
  static public DictionaryManager getInstance(String dictionaryFilesPath) {
    if (instance == null)
      instance = new DictionaryManager(dictionaryFilesPath);
    return instance;
  }

  /**
   * Retorna o DictionaryFile correspondente ao 'name' informado.
   * @param name String Nome do dicion�rio que se deseja retornar.
   * @return DictionaryFile Retorna o DictionaryFile correspondente ao 'name' informado.
   */
  public DictionaryFile getDictionaryFile(String name) {
    for (int i=0; i<dictionaryFiles.length; i++) {
      DictionaryFile result = dictionaryFiles[i];
      if (result.getName().equalsIgnoreCase(name))
        return result;
    } // for
    return null;
  }

  /**
   * Carrega os arquivos de dicion�rio.
   * @throws Exception Em caso de exce��o na tentativa de acesso aos caminhos
   *                   informados, extra��o dos arquivos e opera��es inerentes.
   */
  private void loadDictionaryFiles() throws Exception {
    // arquivos de configura��o de estilo encontrados
    String[] dictionaryFileExtension = {DICTIONARY_FILE_EXTENSION};
    String[] dictionaryFileNames     = FileTools.getFileNames(dictionaryFilesPath, dictionaryFileExtension, false);
    // vetor para as extens�es
    Vector vector = new Vector();
    // loop nos arquivos
    for (int i=0; i<dictionaryFileNames.length; i++) {
      // nome do arquivo da vez
      String dictionaryFileName = dictionaryFilesPath + dictionaryFileNames[i];
      // carrega o arquivo de configura��o
      DictionaryFile dictionaryFile = new DictionaryFile(dictionaryFileName);
      // adiciona na lista
      vector.add(dictionaryFile);
    } // for
    // guarda a lista de configura��es de estilo
    dictionaryFiles = new DictionaryFile[vector.size()];
    vector.copyInto(dictionaryFiles);
  }

  /**
   * Retorna um DictionaryFile[] contendo a lista de arquivos de dicion�rios.
   * @return Retorna um DictionaryFile[] contendo a lista de arquivos de
   *         dicion�rios.
   */
  public DictionaryFile[] dictionaryFiles() {
    return dictionaryFiles;
  }

}
