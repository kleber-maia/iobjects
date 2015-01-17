package iobjects.security;

/**
 * Representa a informa��o de seguran��o de um item de rela��o mestre.
 */
public class MasterRelationInfo {

  private String  value      = "";
  private boolean privileged = false;

  /**
   * Construtor padr�o.
   * @param value String Valor da rela��o mestre correspondente.
   * @param privileged boolean True se o usu�rio for privilegiado nesta rela��o mestre.
   */
  public MasterRelationInfo(String  value,
                            boolean privileged) {
    this.value = value;
    this.privileged = privileged;
  }

  /**
   * Retorna true se o usu�rio for privilegiado para a rela��o mestre indicada.
   * @return boolean Retorna true se o usu�rio for privilegiado para a rela��o
   *         mestre indicada.
   */
  public boolean getPrivileged() {
    return privileged;
  }

  /**
   * Retorna o valor da rela��o mestre correspondente.
   * @return String Retorna o valor da rela��o mestre correspondente.
   */
  public String getValue() {
    return value;
  }

  /**
   * Define se o usu�rio for privilegiado para a rela��o mestre indicada.
   * @param privileged boolean True para que o usu�rio seja privilegiado para a rela��o
   *                   mestre indicada.
   */
  public void setPrivileged(boolean privileged) {
    this.privileged = privileged;
  }

}
