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
   * Construtor padrão.
   * @param caption String Título do ProcessStep.
   * @param description String Descrição do ProcessStep.
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
   * Adiciona 'param' a ParamList oferencendo uma forma cômoda de criar
   * um novo parâmetro ao tempo em que o adiciona a lista de parâmetros associados.
   * @param param Param Parâmetro para ser adicionado a ParamList.
   * @return Param Adiciona 'param' a ParamList oferencendo uma forma cômoda de
   *               criar um novo parâmetro ao tempo em que o adiciona a lista de
   *               parâmetros associados.
   */
  public Param addParam(Param param) {
    // adiciona o parâmetro a lista
    paramList.add(param);
    // retorna-o
    return param;
  }

  /**
   * Retorna o título do ProcessStep.
   * @return String Retorna o título do ProcessStep.
   */
  public String getCaption() {
    return caption;
  }

  /**
   * Retorna a descrição do ProcessStep.
   * @return String Retorna a descrição do ProcessStep.
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
   * Define o título do ProcessStep.
   * @param caption String Título do ProcessStep.
   */
  public void setCaption(String caption) {
    this.caption = caption;
  }

  /**
   * Define a descrição do ProcessStep.
   * @param description String Descrição do ProcessStep.
   */
  public void setDescription(String description) {
    this.description = description;
  }

}
