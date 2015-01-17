package iobjects.ui.ajax;

/**
 * Interface que deve ser implementada pelas classes que t�m a capacidade de
 * expor seus atributos atrav�s do modelo Ajax. Esse modelo prev� que
 * suas informa��es sejam representadas no formato XML e, a partir
 * dessa representa��o, sejam recuperadas com o uso de JavaScript.
 * <b>As classes que implementam esta interface devem disponibilizar m�todos
 * capazes de recuperar seus atributos, itens e propriedades atrav�s de
 * JavaScript.</b>
 */
public interface AjaxBean {

  /**
   * Retorna os atributos do objeto no formato XML.
   * @return String Retorna os atributos do objeto no formato XML.
   */
  public String toXML();

}
