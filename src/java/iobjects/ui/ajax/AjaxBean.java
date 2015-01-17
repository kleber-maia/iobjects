package iobjects.ui.ajax;

/**
 * Interface que deve ser implementada pelas classes que têm a capacidade de
 * expor seus atributos através do modelo Ajax. Esse modelo prevê que
 * suas informações sejam representadas no formato XML e, a partir
 * dessa representação, sejam recuperadas com o uso de JavaScript.
 * <b>As classes que implementam esta interface devem disponibilizar métodos
 * capazes de recuperar seus atributos, itens e propriedades através de
 * JavaScript.</b>
 */
public interface AjaxBean {

  /**
   * Retorna os atributos do objeto no formato XML.
   * @return String Retorna os atributos do objeto no formato XML.
   */
  public String toXML();

}
