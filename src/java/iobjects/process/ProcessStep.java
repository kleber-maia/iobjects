package iobjects.process;

import iobjects.*;

/**
 * Representa uma etapa a ser executada por um Process.
 */
public class ProcessStep {

  private String    caption     = "";
  private String    description = "";
  private String    name        = "";
  private ParamList paramList = new ParamList();

  /**
   * Construtor padr�o.
   * @param caption String T�tulo do ProcessStep.
   * @param description String Descri��o do ProcessStep.
   * @param name String Nome do ProcessStep.
   */
  public ProcessStep(String name,
                     String caption,
                     String description) {
    this.name = name;
    this.caption = caption;
    this.description = description;
  }

  /**
   * Adiciona 'param' a ParamList oferencendo uma forma c�moda de criar
   * um novo par�metro ao tempo em que o adiciona a lista de par�metros associados.
   * @param param Param Par�metro para ser adicionado a ParamList.
   * @return Param Adiciona 'param' a ParamList oferencendo uma forma c�moda de
   *               criar um novo par�metro ao tempo em que o adiciona a lista de
   *               par�metros associados.
   */
  public Param addParam(Param param) {
    // adiciona o par�metro a lista
    paramList.add(param);
    // retorna-o
    return param;
  }

  /**
   * Retorna o t�tulo do ProcessStep.
   * @return String Retorna o t�tulo do ProcessStep.
   */
  public String getCaption() {
    return caption;
  }

  /**
   * Retorna a descri��o do ProcessStep.
   * @return String Retorna a descri��o do ProcessStep.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Retorna o nome do ProcessStep.
   * @return String Retorna o nome do ProcessStep.
   */
  public String getName() {
    return name;
  }

  /**
   * Retorna a lista de Params associados ao ProcessStep.
   * @return ParamList Retorna a lista de Params associados ao ProcessStep.
   */
  public ParamList paramList() {
    return paramList;
  }

  /**
   * Define o t�tulo do ProcessStep.
   * @param caption String T�tulo do ProcessStep.
   */
  public void setCaption(String caption) {
    this.caption = caption;
  }

  /**
   * Define a descri��o do ProcessStep.
   * @param description String Descri��o do ProcessStep.
   */
  public void setDescription(String description) {
    this.description = description;
  }

}
