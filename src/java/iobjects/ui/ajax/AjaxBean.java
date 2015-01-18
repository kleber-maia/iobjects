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
