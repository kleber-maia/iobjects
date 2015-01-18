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
package iobjects.security;

import iobjects.*;
import iobjects.security.*;
import iobjects.util.*;

/**
 * Representa um usuário da aplicação e seus direitos.
 */
public class User {

  private ActionInfoList         actionInfoList         = new ActionInfoList();
  private boolean                cannotChangePassword   = false;
  private boolean                changePassword         = false;
  private String                 description            = "";
  private String                 email                  = "";
  private int                    id                     = 0;
  private String                 level                  = "";
  private String                 name                   = "";
  private MasterRelationInfoList masterRelationInfoList = new MasterRelationInfoList();
  private boolean                privileged             = false;
  private boolean                superUser              = false;

  /**
   * Construtor padrão.
   * @param id int Identificação do usuário.
   * @param name String Nome do usuário.
   * @param description String Descrição do usuário.
   * @param email String e-Mail do usuário.
   * @param level String Nível hierárquico do usuário.
   * @param privileged boolean True se o usuário for privilegiado.
   * @param superUser boolean True se o usuário é um super usuário.
   * @param changePassword boolean True se o usuário deve alterar a senha.
   * @param cannotChangePassword boolean True se o usuário não pode alterar a
   *                             senha.
   * @param actionInfoList ActionInfoList Lista de informações sobre as ações as
   *                       quais o usuário tem direito de acesso.
   * @param masterRelationInfoList MasterRelationInfoList Lista de informações
   *                               sobre os itens de relação mestre a que o
   *                               usuário tem direito de acesso.
   */
  public User(int                    id,
              String                 name,
              String                 description,
              String                 email,
              String                 level,
              boolean                privileged,
              boolean                superUser,
              boolean                changePassword,
              boolean                cannotChangePassword,
              ActionInfoList         actionInfoList,
              MasterRelationInfoList masterRelationInfoList) {
    // nossos valores
    this.id                     = id;
    this.name                   = name;
    this.description            = description;
    this.email                  = email;
    this.level                  = level;
    this.privileged             = privileged;
    this.superUser              = superUser;
    this.changePassword         = changePassword;
    this.cannotChangePassword   = cannotChangePassword;
    this.actionInfoList         = actionInfoList;
    this.masterRelationInfoList = masterRelationInfoList;
  }

  /**
   * Retorna a lista de informações sobre as ações as quais o usuário tem
   * direito de acesso.
   * @return ActionInfoList Retorna a lista de informações sobre as ações as quais
   *         o usuário tem direito de acesso.
   */
  public ActionInfoList getActionInfoList() {
    return actionInfoList;
  }

  /**
   * Retorna true se o usuário não pode alterar a senha.
   * @return boolean Retorna true se o usuário não pode alterar a senha.
   */
  public boolean getCannotChangePassword() {
    return cannotChangePassword;
  }

  /**
   * Retorna true se o usuário deve alterar a senha.
   * @return boolean Retorna true se o usuário deve alterar a senha.
   */
  public boolean getChangePassword() {
    return changePassword;
  }

  /**
   * Retorna a descrição do usuário.
   * @return String Retorna a descrição do usuário.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Retorna o e-Mail do usuário.
   * @return String Retorna o e-Mail do usuário.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Retorna a identificação do usuário.
   * @return int Retorna a identificação do usuário.
   */
  public int getId() {
    return id;
  }

  /**
   * Retorna a lista de informações sobre os itens de relação mestre a que o
   * usuário tem direito de acesso.
   * @return MasterRelationInfoList Retorna a lista de informações sobre os itens
   *         de relação mestre a que o usuário tem direito de acesso.
   */
  public MasterRelationInfoList getMasterRelationInfoList() {
    return masterRelationInfoList;
  }

  /**
   * Retorna o nível hierárquico do usuário.
   * @return String Retorna o nível hierárquico do usuário.
   */
  public String getLevel() {
    return level;
  }

  /**
   * Retorna o nome do usuário.
   * @return String Retorna o nome do usuário.
   */
  public String getName() {
    return name;
  }

  /**
   * Retorna true se o usuário for privilegiado.
   * @return boolean Retorna true se o usuário for privilegiado.
   */
  public boolean getPrivileged() {
    return privileged;
  }

  /**
   * Retorna true se o usuário for um Super Usuáriop.
   * @return boolean Retorna true se o usuário for um Super Usuário.
   */
  public boolean getSuperUser() {
    return superUser;
  }

  /**
   * Retorna true se o usuário for privilegiado na Relação Mestre informada.
   * @param masterRelation MasterRelation Representa o estado atual da relação
   *                       mestre configurada na aplicação.
   * @return boolean Retorna true se o usuário for privilegiado na Relação Mestre
   *                 informada.
   * @throws Exception Em caso de exceção na tentativa de obter valores da
   *                   relação mestre.
   */
  public boolean getPrivileged(MasterRelation masterRelation) throws Exception {
    // se o usuário é privilegiado...retorna
    if (getPrivileged())
      return true;
    // transforma os valores em um só
    String value = StringTools.arrayStringToString(masterRelation.getValues(), ";");
    // procura pela informação de Relação Mestre
    MasterRelationInfo masterRelationInfo = masterRelationInfoList.get(value);
    // retorna
    return (masterRelationInfo != null) && masterRelationInfo.getPrivileged();
  }

  /**
   * Retorna true se o usuário tem direito de acesso sobre a ação informada.
   * @param action Action Ação para verificar direito de acesso.
   * @return boolean Retorna true se o usuário tem direito de acesso sobre a
   *                 ação informada.
   */
  public boolean hasAccessRight(Action action) {
    return hasAccessRight(action, null);
  }

  /**
   * Retorna true se o usuário tem direito de acesso sobre a ação e o comando
   * informados.
   * @param action Action Ação para verificar direito de acesso.
   * @param command Command Comando da ação para verificar direito de acesso.
   * @return boolean Retorna true se o usuário tem direito de acesso sobre a
   *                 ação e o comando informados.
   */
  public boolean hasAccessRight(Action  action,
                                Command command) {
    // se o usuário é privilegiado...retorna OK
    if (privileged)
      return true;
    // se temos um comando...
    if (command != null) {
      // a ação a ser verificada é a que possui este comando
      action = command.getAction();
      // se não temos Action...retorna OK
      if (action == null)
        return true;
    } // if
    // se a ação é invisível...retorna OK
    if (!action.getVisible())
      return true;
    // procura pela informação sobre a ação
    ActionInfo actionInfo = actionInfoList.get(action.getName());
    // se não encontrou...retorna false
    if (actionInfo == null)
      return false;
    // se encontramos e não temos comando para verificar...retorna OK
    else if (command == null)
      return true;
    // retorna se tem direito de acesso sobre o comando no Action informado
    return StringTools.arrayContains(actionInfo.getCommandList(), command.getName());
  }

  /**
   * Define se o usuário deve alterar a senha.
   * @param changePassword boolean Define se o usuário deve alterar a senha.
   */
  public void setChangePassword(boolean changePassword) {
    this.changePassword = changePassword;
  }

}
