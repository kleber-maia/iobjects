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
 * Representa um usu�rio da aplica��o e seus direitos.
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
   * Construtor padr�o.
   * @param id int Identifica��o do usu�rio.
   * @param name String Nome do usu�rio.
   * @param description String Descri��o do usu�rio.
   * @param email String e-Mail do usu�rio.
   * @param level String N�vel hier�rquico do usu�rio.
   * @param privileged boolean True se o usu�rio for privilegiado.
   * @param superUser boolean True se o usu�rio � um super usu�rio.
   * @param changePassword boolean True se o usu�rio deve alterar a senha.
   * @param cannotChangePassword boolean True se o usu�rio n�o pode alterar a
   *                             senha.
   * @param actionInfoList ActionInfoList Lista de informa��es sobre as a��es as
   *                       quais o usu�rio tem direito de acesso.
   * @param masterRelationInfoList MasterRelationInfoList Lista de informa��es
   *                               sobre os itens de rela��o mestre a que o
   *                               usu�rio tem direito de acesso.
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
   * Retorna a lista de informa��es sobre as a��es as quais o usu�rio tem
   * direito de acesso.
   * @return ActionInfoList Retorna a lista de informa��es sobre as a��es as quais
   *         o usu�rio tem direito de acesso.
   */
  public ActionInfoList getActionInfoList() {
    return actionInfoList;
  }

  /**
   * Retorna true se o usu�rio n�o pode alterar a senha.
   * @return boolean Retorna true se o usu�rio n�o pode alterar a senha.
   */
  public boolean getCannotChangePassword() {
    return cannotChangePassword;
  }

  /**
   * Retorna true se o usu�rio deve alterar a senha.
   * @return boolean Retorna true se o usu�rio deve alterar a senha.
   */
  public boolean getChangePassword() {
    return changePassword;
  }

  /**
   * Retorna a descri��o do usu�rio.
   * @return String Retorna a descri��o do usu�rio.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Retorna o e-Mail do usu�rio.
   * @return String Retorna o e-Mail do usu�rio.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Retorna a identifica��o do usu�rio.
   * @return int Retorna a identifica��o do usu�rio.
   */
  public int getId() {
    return id;
  }

  /**
   * Retorna a lista de informa��es sobre os itens de rela��o mestre a que o
   * usu�rio tem direito de acesso.
   * @return MasterRelationInfoList Retorna a lista de informa��es sobre os itens
   *         de rela��o mestre a que o usu�rio tem direito de acesso.
   */
  public MasterRelationInfoList getMasterRelationInfoList() {
    return masterRelationInfoList;
  }

  /**
   * Retorna o n�vel hier�rquico do usu�rio.
   * @return String Retorna o n�vel hier�rquico do usu�rio.
   */
  public String getLevel() {
    return level;
  }

  /**
   * Retorna o nome do usu�rio.
   * @return String Retorna o nome do usu�rio.
   */
  public String getName() {
    return name;
  }

  /**
   * Retorna true se o usu�rio for privilegiado.
   * @return boolean Retorna true se o usu�rio for privilegiado.
   */
  public boolean getPrivileged() {
    return privileged;
  }

  /**
   * Retorna true se o usu�rio for um Super Usu�riop.
   * @return boolean Retorna true se o usu�rio for um Super Usu�rio.
   */
  public boolean getSuperUser() {
    return superUser;
  }

  /**
   * Retorna true se o usu�rio for privilegiado na Rela��o Mestre informada.
   * @param masterRelation MasterRelation Representa o estado atual da rela��o
   *                       mestre configurada na aplica��o.
   * @return boolean Retorna true se o usu�rio for privilegiado na Rela��o Mestre
   *                 informada.
   * @throws Exception Em caso de exce��o na tentativa de obter valores da
   *                   rela��o mestre.
   */
  public boolean getPrivileged(MasterRelation masterRelation) throws Exception {
    // se o usu�rio � privilegiado...retorna
    if (getPrivileged())
      return true;
    // transforma os valores em um s�
    String value = StringTools.arrayStringToString(masterRelation.getValues(), ";");
    // procura pela informa��o de Rela��o Mestre
    MasterRelationInfo masterRelationInfo = masterRelationInfoList.get(value);
    // retorna
    return (masterRelationInfo != null) && masterRelationInfo.getPrivileged();
  }

  /**
   * Retorna true se o usu�rio tem direito de acesso sobre a a��o informada.
   * @param action Action A��o para verificar direito de acesso.
   * @return boolean Retorna true se o usu�rio tem direito de acesso sobre a
   *                 a��o informada.
   */
  public boolean hasAccessRight(Action action) {
    return hasAccessRight(action, null);
  }

  /**
   * Retorna true se o usu�rio tem direito de acesso sobre a a��o e o comando
   * informados.
   * @param action Action A��o para verificar direito de acesso.
   * @param command Command Comando da a��o para verificar direito de acesso.
   * @return boolean Retorna true se o usu�rio tem direito de acesso sobre a
   *                 a��o e o comando informados.
   */
  public boolean hasAccessRight(Action  action,
                                Command command) {
    // se o usu�rio � privilegiado...retorna OK
    if (privileged)
      return true;
    // se temos um comando...
    if (command != null) {
      // a a��o a ser verificada � a que possui este comando
      action = command.getAction();
      // se n�o temos Action...retorna OK
      if (action == null)
        return true;
    } // if
    // se a a��o � invis�vel...retorna OK
    if (!action.getVisible())
      return true;
    // procura pela informa��o sobre a a��o
    ActionInfo actionInfo = actionInfoList.get(action.getName());
    // se n�o encontrou...retorna false
    if (actionInfo == null)
      return false;
    // se encontramos e n�o temos comando para verificar...retorna OK
    else if (command == null)
      return true;
    // retorna se tem direito de acesso sobre o comando no Action informado
    return StringTools.arrayContains(actionInfo.getCommandList(), command.getName());
  }

  /**
   * Define se o usu�rio deve alterar a senha.
   * @param changePassword boolean Define se o usu�rio deve alterar a senha.
   */
  public void setChangePassword(boolean changePassword) {
    this.changePassword = changePassword;
  }

}
