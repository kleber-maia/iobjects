package iobjects.security;

/**
 * Representa a informação de seguranção de um item de relação mestre.
 */
public class MasterRelationInfo {

  private String  value      = "";
  private boolean privileged = false;

  /**
   * Construtor padrão.
   * @param value String Valor da relação mestre correspondente.
   * @param privileged boolean True se o usuário for privilegiado nesta relação mestre.
   */
  public MasterRelationInfo(String  value,
                            boolean privileged) {
    this.value = value;
    this.privileged = privileged;
  }

  /**
   * Retorna true se o usuário for privilegiado para a relação mestre indicada.
   * @return boolean Retorna true se o usuário for privilegiado para a relação
   *         mestre indicada.
   */
  public boolean getPrivileged() {
    return privileged;
  }

  /**
   * Retorna o valor da relação mestre correspondente.
   * @return String Retorna o valor da relação mestre correspondente.
   */
  public String getValue() {
    return value;
  }

  /**
   * Define se o usuário for privilegiado para a relação mestre indicada.
   * @param privileged boolean True para que o usuário seja privilegiado para a relação
   *                   mestre indicada.
   */
  public void setPrivileged(boolean privileged) {
    this.privileged = privileged;
  }

}
