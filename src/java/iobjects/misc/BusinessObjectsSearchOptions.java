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
package iobjects.misc;

import iobjects.*;

/**
 * Classe utilitária para armazenamento das opções de pesquisa.
 */

public class BusinessObjectsSearchOptions {

  private String     keyWords         = "";
  private ActionList results          = new ActionList();
  private boolean    searchForEntity  = true;
  private boolean    searchForProcess = true;
  private boolean    searchForReport  = true;

  /**
   * Construtor padrão.
   */
  public BusinessObjectsSearchOptions() {
  }

  /**
   * Retora o ActionList utilizado para armazenamento do resultado da pesquisa.
   * @return ActionList Retora o ActionList utilizado para armazenamento do
   *         resultado da pesquisa.
   */
  public ActionList actionList() {
    return results;
  }

  /**
   * Retorna as palavras chave da pesquisa.
   * @return String Retorna as palavras chave da pesquisa.
   */
  public String getKeyWords() {
    return keyWords;
  }

  /**
   * Retorna true se a pesquisa incluir objetos Entity.
   * @return boolean Retorna true se a pesquisa incluir objetos Entity.
   */
  public boolean getSearchForEntity() {
    return searchForEntity;
  }

  /**
   * Retorna true se a pesquisa incluir objetos Process.
   * @return boolean Retorna true se a pesquisa incluir objetos Process.
   */
  public boolean getSearchForProcess() {
    return searchForProcess;
  }

  /**
   * Retorna true se a pesquisa incluir objetos Report.
   * @return boolean Retorna true se a pesquisa incluir objetos Report.
   */
  public boolean getSearchForReport() {
    return searchForReport;
  }

  /**
   * Define as palavras chave da pesquisa.
   * @param keyWords String Palavras chave da pesquisa.
   */
  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  /**
   * Define se a pesquisa deve incluir objetos Entity.
   * @param searchForEntity boolean True se a pesquisa deve incluir objetos Entity.
   */
  public void setSearchForEntity(boolean searchForEntity) {
    this.searchForEntity = searchForEntity;
  }

  /**
   * Define se a pesquisa deve incluir objetos Process.
   * @param searchForProcess boolean True se a pesquisa deve incluir objetos Process.
   */
  public void setSearchForProcess(boolean searchForProcess) {
    this.searchForProcess = searchForProcess;
  }

  /**
   * Define se a pesquisa deve incluir objetos Report.
   * @param searchForReport boolean True se a pesquisa deve incluir objetos Report.
   */
  public void setSearchForReport(boolean searchForReport) {
    this.searchForReport = searchForReport;
  }

}
