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
 * Representa o atualizador da aplicação. Tem a capacidade de acessar o
 * repositório de atualizações configurado, fazer o download dos pacotes
 * necessários e instalá-los automaticamente.
 * <p>
 * <b>Se os pacotes de atualização instalados contiverem arquivos do iObjects,
 * como classes, bibliotecas ou JSP's, o servidor de aplicação deve ser
 * reiniciado logo após a atualização. Se os pacotes de atualização instalados
 * contiverem apenas arquivos de configuração ou de extensões, as atualizações
 * somente terão efeito após a próxima reinicialização do servidor de
 * aplicação.</b>
 * </p>
 * <p>
 * Como implementa a interface Scheduleable, pode ter sua execução agendada
 * através do Schedule da aplicação. Neste caso serão verificadas e instaladas
 * as atualizações disponíveis. Uma boa estratégia é agendar a execução do
 * atualizador para alguns minutos antes do reinício do servidor de aplicação.
 * </p>
 * <p>
 * É possível ainda disponibilizar uma atualização e fazer com que ela seja
 * instalada somente em uma determinada data. Para isso basta informar a data
 * da atualização como uma data futura, então seu pacote não será instalado até
 * que a data do sistema seja maior ou igual a data definida.
 * </p>
 * <p>
 * O conteúdo dos pacotes de atualização serão descompactados diretamente na
 * raiz do diretório da aplicação e na raiz do diretório de trabalho, de acordo
 * com o tipo de pacote informado no arquivo de repositório.
 * </p>
 * @since 2006 R1
 */
public class Updater extends BusinessObject implements Scheduleable {

  static public interface EventListener {
    /**
     * Evento chamado ao acessar o arquivo local de configurações.
     * @param configurationFile String Caminho completo do arquivo de configurações.
     */
    public void onAccessLocalConfiguration(String configurationFile);
    /**
     * Evento chamado ao acessar o arquivo de configurações do repositório.
     * @param repositoryFile String Caminho completop do arquivo de configurações do respositório.
     */
    public void onAccessRepositoryConfiguration(String repositoryFile);
    /**
     * Evento chamado após a instalação de uma atualização.
     * @param update Update Atualização instalada.
     */
    public void onAfterInstallUpdate(RepositoryFile.Update update);
    /**
     * Evento chamado após a instalação de um dos pacotes da atualização.
     * @param update Update Atualização instalada.
     * @param packageType int Pacote instalado.
     */
    public void onAfterInstallUpdatePackage(RepositoryFile.Update update, int packageType);
    /**
     * Evento chamado antes da instalação de uma atualização.
     * @param update Update Atualização que será instalada.
     */
    public void onBeforeInstallUpdate(RepositoryFile.Update update);
    /**
     * Evento chamado antes da instalação de uma dos pacotes da atualização.
     * @param update Update Atualização que será instalada.
     * @param packageType int Pacote que será instalado.
     */
    public void onBeforeInstallUpdatePackage(RepositoryFile.Update update, int packageType);
    /**
     * Evento chamado após a obtenção da lista de atualizações que devem ser instaladas.
     * @param updateList Update[] Lista de atualizações necessárias.
     */
    public void onGetUpdateList(RepositoryFile.Update[] updateList);
  }

  static public final String CLASS_NAME = "iobjects.Updater";

  static private final int PACKAGE_TYPE_APPLICATION = 0;
  static private final int PACKAGE_TYPE_WORK        = 1;

  private Vector eventListenerList = new Vector();

  /**
   * Construtor padrão.
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
   * @param update Update Atualização que se deseja verificar.
   * @param installedUpdateList Update[] Lista de atualizações instaladas.
   * @return boolean Retorna true se 'update' se encontra em 'installedUpdateList'.
   */
  private boolean alreadyInstalled(RepositoryFile.Update   update,
                                   RepositoryFile.Update[] installedUpdateList) {
    // loop nas atualizações instaladas
    for (int w=0; w<installedUpdateList.length; w++) {
      // se o nome dos pacotes coincide...retorna ok
      if (installedUpdateList[w].getApplicationPackage().equals(update.getApplicationPackage()) &&
          installedUpdateList[w].getWorkPackage().equals(update.getWorkPackage()))
        return true;
    } // for w
    return false;
  }

  /**
   * Retorna a lista de atualizações que devem ser instaladas, comparando as
   * atualizações disponíveis e as já realizadas. Primeiro será verificado se
   * existe uma atualização do tipo 'completa'. Se houver, todas as atualizações
   * anteriores serão desprezadas. Após isso todas as atualizações do tipo
   * 'incremental', mais recentes e que estiverem disponíveis serão instaladas.
   * @param updateList Update[] Lista de atualizações disponíveis.
   * @param installedUpdateList Update[] Lista de atualizações já realizadas.
   * @return Update[] Retorna a lista de atualizações que devem ser instaladas,
   *                  comparando as atualizações disponíveis e as já realizadas.
   */
  public RepositoryFile.Update[] getUpdateListForInstall(RepositoryFile.Update[] updateList,
                                                         RepositoryFile.Update[] installedUpdateList) {
    // só podemos instalar atualizações cuja data é menor ou igual
    // a data atual do sistema
    Date now = new Date();

    // procura pela última atualização completa disponível
    int lastCompleteUpdateIndex = -1;
    for (int i=updateList.length-1; i>=0; i--) {
      // se é uma atualização completa...pára
      if ((updateList[i].getType() == RepositoryFile.TYPE_COMPLETE) &&
          (updateList[i].getDate().before(now))) {
        lastCompleteUpdateIndex = i;
        break;
      } // if
    } // for

    // nossa lista temporária
    Vector vector = new Vector();
    // loop nas atualizações disponíveis a partir da última atualização
    // completa encontrada ou a partir do início
    for (int i=(lastCompleteUpdateIndex > 0 ? lastCompleteUpdateIndex : 0); i<updateList.length; i++) {
      // atualização da vez
      RepositoryFile.Update update = updateList[i];
      // se hoje ainda não é a data de instalação...continua
      if (!update.getDate().before(now))
        continue;
      // se já foi instalada...continnua
      if (alreadyInstalled(update, installedUpdateList))
        continue;
      // se chegou até aqui...temos que instalá-la
      vector.add(update);
    } // for

    // retorna
    RepositoryFile.Update[] result = new RepositoryFile.Update[vector.size()];
    vector.copyInto(result);
    return result;
  }

  /**
   * Realiza a verificação das atualizações disponíveis no repositórios e ainda
   * não instaladas na aplicação e as instala. Primeiro será verificado se
   * existe uma atualização do tipo 'completa'. Se houver, todas as atualizações
   * anteriores serão desprezadas. Após isso todas as atualizações do tipo
   * 'incremental', mais recentes e que estiverem disponíveis serão instaladas.
   * @throws Exception Em caso de exceção na tentativa de acesso ao repositório
   *                   de atualizações, de copiar as atualizações ou instalá-las.
   */
  public void installUpdates() throws Exception {
    // nosso log
    String logId = "updater";
    // log
    getFacade().log().write(logId, "Instalando atualizações...");
    // nosso arquivo de configuração
    getFacade().log().write(logId, "Acessando arquivo de configuração: " + getFacade().configurationLocalPath() + UpdaterFile.UPDATER_FILE_NAME);
    onAccessLocalConfiguration(getFacade().configurationLocalPath() + UpdaterFile.UPDATER_FILE_NAME);
    UpdaterFile updaterFile = new UpdaterFile(getFacade().configurationLocalPath());
    // nosso arquivo de repositório, indicando as atualizações disponíveis
    getFacade().log().write(logId, "Acessando arquivo de repositório: " + updaterFile.repositoryURL() + RepositoryFile.REPOSITORY_FILE_NAME);
    onAccessRepositoryConfiguration(updaterFile.repositoryURL() + RepositoryFile.REPOSITORY_FILE_NAME);
    RepositoryFile repositoryFile = new RepositoryFile(updaterFile.repositoryURL());
    // obtém somente a lista das atualizações que devemos instalar
    getFacade().log().write(logId, "Obtendo lista de atualizações...");
    RepositoryFile.Update[] updateListForInstall =
        getUpdateListForInstall(repositoryFile.updateList(),
                                updaterFile.installedUpdateList());
    getFacade().log().write(logId, "Obtendo lista de atualizações: " + updateListForInstall.length + " disponíveis");
    onGetUpdateList(updateListForInstall);
    // data de hoje
    Date now = new Date();
    // instala as atualizações
    for (int i=0; i<updateListForInstall.length; i++) {
      // atualização da vez
      RepositoryFile.Update update = updateListForInstall[i];
      getFacade().log().write(logId, "Instalando atualização: tipo " +  RepositoryFile.TYPES[update.getType()] + " de " + DateTools.formatDate(update.getDate()));
      onBeforeInstallUpdate(update);
      // instala o pacote da aplicação
      if (!update.getApplicationPackage().equals("")) {
        getFacade().log().write(logId, "Instalando pacote: " + update.getApplicationPackage());
        onBeforeInstallUpdatePackage(update, PACKAGE_TYPE_APPLICATION);
        installUpdate(updaterFile.repositoryURL() + update.getApplicationPackage(), PACKAGE_TYPE_APPLICATION);
        getFacade().log().write(logId, "Instalando pacote: OK");
        onAfterInstallUpdatePackage(update, PACKAGE_TYPE_APPLICATION);
      } // if
      // instala o pacote do diretório de trabalho
      if (!update.getWorkPackage().equals("")) {
        getFacade().log().write(logId, "Instalando pacote: " + update.getWorkPackage());
        onBeforeInstallUpdatePackage(update, PACKAGE_TYPE_WORK);
        installUpdate(updaterFile.repositoryURL() + update.getWorkPackage(), PACKAGE_TYPE_WORK);
        getFacade().log().write(logId, "Instalando pacote: OK");
        onAfterInstallUpdatePackage(update, PACKAGE_TYPE_WORK);
      } // if
      getFacade().log().write(logId, "Instalando atualização: OK");
      onAfterInstallUpdate(update);
      // adiciona na lista de atualizações instaladas
      updaterFile.insertInstalledUpdate(update.getType(),
                                        update.getApplicationPackage(),
                                        update.getWorkPackage(),
                                        now);
    } // for
    // log
    getFacade().log().write(logId, "Instalando atualizações: OK");
  }

  /**
   * Instala o pacote de atualização indicado por 'packageURL'.
   * @param packageURL String URL de origem do pacote de atualização.
   * @param packageType int Tipo do pacote indicado em 'packageURL'. Isto dirá
   *                        onde os arquivos do pacote serão extraídos.
   * @throws Exception Em caso de exceção na tentativa de acesso a URL informada
   *                   ou na tentativa de extrair os arquivos.
   */
  private void installUpdate(String packageURL,
                             int    packageType) throws Exception {
    // constrói a URL da package
    URL url = new URL(packageURL);
    // arquivo de destino
    String targetFile = getFacade().updateLocalPath() + url.getFile();
    // faz o download do arquivo de atualização para o diretório temporário
    FileTools.copyFile(packageURL, targetFile);
    // diretório de instalação
    String installPath = "";
    if (packageType == PACKAGE_TYPE_APPLICATION)
      installPath = getFacade().applicationLocalPath();
    else
      installPath = getFacade().applicationLocalWorkPath();
    // extrai o conteúdo
    new UnZip(targetFile, installPath);
  }

  /**
   * Chama o evento nos EventListeners.
   * @param configurationFile String Caminho completo do arquivo de configurações.
   */
  private void onAccessLocalConfiguration(String configurationFile) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onAccessLocalConfiguration(configurationFile);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param repositoryFile String Caminho completop do arquivo de configurações do respositório.
   */
  private void onAccessRepositoryConfiguration(String repositoryFile) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onAccessRepositoryConfiguration(repositoryFile);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param update Update Atualização instalada.
   */
  private void onAfterInstallUpdate(RepositoryFile.Update update) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onAfterInstallUpdate(update);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param update Update Atualização instalada.
   * @param packageType int Pacote instalado.
   */
  private void onAfterInstallUpdatePackage(RepositoryFile.Update update, int packageType) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onAfterInstallUpdatePackage(update, packageType);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param update Update Atualização que será instalada.
   */
  private void onBeforeInstallUpdate(RepositoryFile.Update update) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onBeforeInstallUpdate(update);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param update Update Atualização que será instalada.
   * @param packageType int Pacote que será instalado.
   */
  private void onBeforeInstallUpdatePackage(RepositoryFile.Update update, int packageType) {
    for (int i=0; i<eventListenerList.size(); i++) {
      ((EventListener)eventListenerList.get(i)).onBeforeInstallUpdatePackage(update, packageType);
    } // for
  }

  /**
   * Chama o evento nos EventListeners.
   * @param updateList Update[] Lista de atualizações necessárias.
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
   * Executa a rotina de verificação das atualizações disponíveis e as instala.
   * @return RunStatus
   * @throws Exception Em caso de exceção na tentativa de acesso ao repositório
   *                   de atualizações, de copiar as atualizações ou instalá-las.
   */
  public RunStatus runScheduledTask() throws Exception {
    try {
      installUpdates();
      return new RunStatus(true, "Atualizações verificadas!");
    }
    catch (Exception e) {
      return new RunStatus(false, e.getMessage());
    } // try-catch
  }

}
