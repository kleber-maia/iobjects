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

import java.net.*;
import java.util.*;

import iobjects.schedule.*;
import iobjects.schedule.*;
import iobjects.util.*;
import iobjects.xml.*;

/**
 * Representa o atualizador da aplica��o. Tem a capacidade de acessar o
 * reposit�rio de atualiza��es configurado, fazer o download dos pacotes
 * necess�rios e instal�-los automaticamente.
 * <p>
 * <b>Se os pacotes de atualiza��o instalados contiverem arquivos do iObjects,
 * como classes, bibliotecas ou JSP's, o servidor de aplica��o deve ser
 * reiniciado logo ap�s a atualiza��o. Se os pacotes de atualiza��o instalados
 * contiverem apenas arquivos de configura��o ou de extens�es, as atualiza��es
 * somente ter�o efeito ap�s a pr�xima reinicializa��o do servidor de
 * aplica��o.</b>
 * </p>
 * <p>
 * Como implementa a interface Scheduleable, pode ter sua execu��o agendada
 * atrav�s do Schedule da aplica��o. Neste caso ser�o verificadas e instaladas
 * as atualiza��es dispon�veis. Uma boa estrat�gia � agendar a execu��o do
 * atualizador para alguns minutos antes do rein�cio do servidor de aplica��o.
 * </p>
 * <p>
 * � poss�vel ainda disponibilizar uma atualiza��o e fazer com que ela seja
 * instalada somente em uma determinada data. Para isso basta informar a data
 * da atualiza��o como uma data futura, ent�o seu pacote n�o ser� instalado at�
 * que a data do sistema seja maior ou igual a data definida.
 * </p>
 * <p>
 * O conte�do dos pacotes de atualiza��o ser�o descompactados diretamente na
 * raiz do diret�rio da aplica��o e na raiz do diret�rio de trabalho, de acordo
 * com o tipo de pacote informado no arquivo de reposit�rio.
 * </p>
 * @since 2006 R1
 */
public class Updater extends BusinessObject implements Scheduleable {

  static public interface EventListener {
    /**
     * Evento chamado ao acessar o arquivo local de configura��es.
     * @param configurationFile String Caminho completo do arquivo de configura��es.
     */
    public void onAccessLocalConfiguration(String configurationFile);
    /**
     * Evento chamado ao acessar o arquivo de configura��es do reposit�rio.
     * @param repositoryFile String Caminho completop do arquivo de configura��es do resposit�rio.
     */
    public void onAccessRepositoryConfiguration(String repositoryFile);
    /**
     * Evento chamado ap�s a instala��o de uma atualiza��o.
     * @param update Update Atualiza��o instalada.
     */
    public void onAfterInstallUpdate(RepositoryFile.Update update);
    /**
     * Evento chamado ap�s a instala��o de um dos pacotes da atualiza��o.
     * @param update Update Atualiza��o instalada.
     * @param packageType int Pacote instalado.
     */
    public void onAfterInstallUpdatePackage(RepositoryFile.Update update, int packageType);
    /**
     * Evento chamado antes da instala��o de uma atualiza��o.
     * @param update Update Atualiza��o que ser� instalada.
     */
    public void onBeforeInstallUpdate(RepositoryFile.Update update);
    /**
     * Evento chamado antes da instala��o de uma dos pacotes da atualiza��o.
     * @param update Update Atualiza��o que ser� instalada.
     * @param packageType int Pacote que ser� instalado.
     */
    public void onBeforeInstallUpdatePackage(RepositoryFile.Update update, int packageType);
    /**
     * Evento chamado ap�s a obten��o da lista de atualiza��es que devem ser instaladas.
     * @param updateList Update[] Lista de atualiza��es necess�rias.
     */
    public void onGetUpdateList(RepositoryFile.Update[] updateList);
  }

  static public final String CLASS_NAME = "iobjects.Updater";

  static private final int PACKAGE_TYPE_APPLICATION = 0;
  static private final int PACKAGE_TYPE_WORK        = 1;

  private Vector eventListenerList = new Vector();

  /**
   * Construtor padr�o.
   */
  public Updater() {
  }

  /**
   * Adiciona o manipulador de eventos.
   * @param eventListener EventListener
   */
  public void addEventListener(EventListener eventListener) {
    // adiciona o manipulador de eventos
    eventListenerList.add(eventListener);
  }

  /**
   * Retorna true se 'update' se encontra em 'installedUpdateList'.
   * @param update Update Atualiza��o que se deseja verificar.
   * @param installedUpdateList Update[] Lista de atualiza��es instaladas.
   * @return boolean Retorna true se 'update' se encontra em 'installedUpdateList'.
   */
  private boolean alreadyInstalled(RepositoryFile.Update   update,
                                   RepositoryFile.Update[] installedUpdateList) {
    // loop nas atualiza��es instaladas
    for (int w=0; w<installedUpdateList.length; w++) {
      // se o nome dos pacotes coincide...retorna ok
      if (installedUpdateList[w].getApplicationPackage().equals(update.getApplicationPackage()) &&
          installedUpdateList[w].getWorkPackage().equals(update.getWorkPackage()))
        return true;
    } // for w
    return false;
  }

  /**
   * Retorna a lista de atualiza��es que devem ser instaladas, comparando as
   * atualiza��es dispon�veis e as j� realizadas. Primeiro ser� verificado se
   * existe uma atualiza��o do tipo 'completa'. Se houver, todas as atualiza��es
   * anteriores ser�o desprezadas. Ap�s isso todas as atualiza��es do tipo
   * 'incremental', mais recentes e que estiverem dispon�veis ser�o instaladas.
   * @param updateList Update[] Lista de atualiza��es dispon�veis.
   * @param installedUpdateList Update[] Lista de atualiza��es j� realizadas.
   * @return Update[] Retorna a lista de atualiza��es que devem ser instaladas,
   *                  comparando as atualiza��es dispon�veis e as j� realizadas.
   */
  public RepositoryFile.Update[] getUpdateListForInstall(RepositoryFile.Update[] updateList,
                                                         RepositoryFile.Update[] installedUpdateList) {
    // s� podemos instalar atualiza��es cuja data � menor ou igual
    // a data atual do sistema
    Date now = new Date();

    // procura pela �ltima atualiza��o completa dispon�vel
    int lastCompleteUpdateIndex = -1;
    for (int i=updateList.length-1; i>=0; i--) {
      // se � uma atualiza��o completa...p�ra
      if ((updateList[i].getType() == RepositoryFile.TYPE_COMPLETE) &&
          (updateList[i].getDate().before(now))) {
        lastCompleteUpdateIndex = i;
        break;
      } // if
    } // for

    // nossa lista tempor�ria
    Vector vector = new Vector();
    // loop nas atualiza��es dispon�veis a partir da �ltima atualiza��o
    // completa encontrada ou a partir do in�cio
    for (int i=(lastCompleteUpdateIndex > 0 ? lastCompleteUpdateIndex : 0); i<updateList.length; i++) {
      // atualiza��o da vez
      RepositoryFile.Update update = updateList[i];
      // se hoje ainda n�o � a data de instala��o...continua
      if (!update.getDate().before(now))
        continue;
      // se j� foi instalada...continnua
      if (alreadyInstalled(update, installedUpdateList))
        continue;
      // se chegou at� aqui...temos que instal�-la
      vector.add(update);
    } // for

    // retorna
    RepositoryFile.Update[] result = new RepositoryFile.Update[vector.size()];
    vector.copyInto(result);
    return result;
  }

  /**
   * Realiza a verifica��o das atualiza��es dispon�veis no reposit�rios e ainda
   * n�o instaladas na aplica��o e as instala. Primeiro ser� verificado se
   * existe uma atualiza��o do tipo 'completa'. Se houver, todas as atualiza��es
   * anteriores ser�o desprezadas. Ap�s isso todas as atualiza��es do tipo
   * 'incremental', mais recentes e que estiverem dispon�veis ser�o instaladas.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao reposit�rio
   *                   de atualiza��es, de copiar as atualiza��es ou instal�-las.
   */
  public void installUpdates() throws Exception {
    // nosso log
    String logId = "updater";
    // log
    getFacade().log().write(logId, "Instalando atualiza��es...");
    // nosso arquivo de configura��o
    getFacade().log().write(logId, "Acessando arquivo de configura��o: " + getFacade().configurationLocalPath() + UpdaterFile.UPDATER_FILE_NAME);
    onAccessLocalConfiguration(getFacade().configurationLocalPath() + UpdaterFile.UPDATER_FILE_NAME);
    UpdaterFile updaterFile = new UpdaterFile(getFacade().configurationLocalPath());
    // nosso arquivo de reposit�rio, indicando as atualiza��es dispon�veis
    getFacade().log().write(logId, "Acessando arquivo de reposit�rio: " + updaterFile.repositoryURL() + RepositoryFile.REPOSITORY_FILE_NAME);
    onAccessRepositoryConfiguration(updaterFile.repositoryURL() + RepositoryFile.REPOSITORY_FILE_NAME);
    RepositoryFile repositoryFile = new RepositoryFile(updaterFile.repositoryURL());
    // obt�m somente a lista das atualiza��es que devemos instalar
    getFacade().log().write(logId, "Obtendo lista de atualiza��es...");
    RepositoryFile.Update[] updateListForInstall =
        getUpdateListForInstall(repositoryFile.updateList(),
                                updaterFile.installedUpdateList());
    getFacade().log().write(logId, "Obtendo lista de atualiza��es: " + updateListForInstall.length + " dispon�veis");
    onGetUpdateList(updateListForInstall);
    // data de hoje
    Date now = new Date();
    // instala as atualiza��es
    for (int i=0; i<updateListForInstall.length; i++) {
      // atualiza��o da vez
      RepositoryFile.Update update = updateListForInstall[i];
      getFacade().log().write(logId, "Instalando atualiza��o: tipo " +  RepositoryFile.TYPES[update.getType()] + " de " + DateTools.formatDate(update.getDate()));
      onBeforeInstallUpdate(update);
      // instala o pacote da aplica��o
      if (!update.getApplicationPackage().equals("")) {
        getFacade().log().write(logId, "Instalando pacote: " + update.getApplicationPackage());
        onBeforeInstallUpdatePackage(update, PACKAGE_TYPE_APPLICATION);
        installUpdate(updaterFile.repositoryURL() + update.getApplicationPackage(), PACKAGE_TYPE_APPLICATION);
        getFacade().log().write(logId, "Instalando pacote: OK");
        onAfterInstallUpdatePackage(update, PACKAGE_TYPE_APPLICATION);
      } // if
      // instala o pacote do diret�rio de trabalho
      if (!update.getWorkPackage().equals("")) {
        getFacade().log().write(logId, "Instalando pacote: " + update.getWorkPackage());
        onBeforeInstallUpdatePackage(update, PACKAGE_TYPE_WORK);
        installUpdate(updaterFile.repositoryURL() + update.getWorkPackage(), PACKAGE_TYPE_WORK);
        getFacade().log().write(logId, "Instalando pacote: OK");
        onAfterInstallUpdatePackage(update, PACKAGE_TYPE_WORK);
      } // if
      getFacade().log().write(logId, "Instalando atualiza��o: OK");
      onAfterInstallUpdate(update);
      // adiciona na lista de atualiza��es instaladas
      updaterFile.insertInstalledUpdate(update.getType(),
                                        update.getApplicationPackage(),
                                        update.getWorkPackage(),
                                        now);
    } // for
    // log
    getFacade().log().write(logId, "Instalando atualiza��es: OK");
  }

  /**
   * Instala o pacote de atualiza��o indicado por 'packageURL'.
   * @param packageURL String URL de origem do pacote de atualiza��o.
   * @param packageType int Tipo do pacote indicado em 'packageURL'. Isto dir�
   *                        onde os arquivos do pacote ser�o extra�dos.
   * @throws Exception Em caso de exce��o na tentativa de acesso a URL informada
   *                   ou na tentativa de extrair os arquivos.
   */
  private void installUpdate(String packageURL,
                             int    packageType) throws Exception {
    // constr�i a URL da package
    URL url = new URL(packageURL);
    // arquivo de destino
    String targetFile = getFacade().updateLocalPath() + url.getFile();
    // faz o download do arquivo de atualiza��o para o diret�rio tempor�rio
    FileTools.copyFile(packageURL, targetFile);
    // diret�rio de instala��o
    String installPath = "";
    if (packageType == PACKAGE_TYPE_APPLICATION)
      installPath = getFacade().applicationLocalPath();
    else
      installPath = getFacade().applicationLocalWorkPath();
    // extrai o conte�do
    new UnZip(targetFile, installPath);
  }

  /**
   * Chama o evento nos EventListeners.
   * @param configurationFile String Caminho completo do arquivo de configura��es.
   */
  private void onAccessLocalConfiguration(String configurationFile) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onAccessLocalConfiguration(configurationFile);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param repositoryFile String Caminho completop do arquivo de configura��es do resposit�rio.
   */
  private void onAccessRepositoryConfiguration(String repositoryFile) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onAccessRepositoryConfiguration(repositoryFile);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param update Update Atualiza��o instalada.
   */
  private void onAfterInstallUpdate(RepositoryFile.Update update) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onAfterInstallUpdate(update);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param update Update Atualiza��o instalada.
   * @param packageType int Pacote instalado.
   */
  private void onAfterInstallUpdatePackage(RepositoryFile.Update update, int packageType) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onAfterInstallUpdatePackage(update, packageType);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param update Update Atualiza��o que ser� instalada.
   */
  private void onBeforeInstallUpdate(RepositoryFile.Update update) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onBeforeInstallUpdate(update);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param update Update Atualiza��o que ser� instalada.
   * @param packageType int Pacote que ser� instalado.
   */
  private void onBeforeInstallUpdatePackage(RepositoryFile.Update update, int packageType) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onBeforeInstallUpdatePackage(update, packageType);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param updateList Update[] Lista de atualiza��es necess�rias.
   */
  private void onGetUpdateList(RepositoryFile.Update[] updateList) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onGetUpdateList(updateList);
    } // for
  }

  /**
   * Remove o manipulador de eventos.
   * @param eventListener EventListener
   */
  public void removeEventListener(EventListener eventListener) {
    // adiciona o manipulador de eventos
    eventListenerList.remove(eventListener);
  }

  /**
   * Executa a rotina de verifica��o das atualiza��es dispon�veis e as instala.
   * @return RunStatus
   * @throws Exception Em caso de exce��o na tentativa de acesso ao reposit�rio
   *                   de atualiza��es, de copiar as atualiza��es ou instal�-las.
   */
  public RunStatus runScheduledTask() throws Exception {
    try {
      installUpdates();
      return new RunStatus(true, "Atualiza��es verificadas!");
    }
    catch (Exception e) {
      return new RunStatus(false, e.getMessage());
    } // try-catch
  }

}
