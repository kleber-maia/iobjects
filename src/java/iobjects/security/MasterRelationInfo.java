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
