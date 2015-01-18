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
package iobjects.entity;

import iobjects.util.*;

/**
 * Representa um valor de um EntityLookup obtido a partir de uma consulta ao
 * banco de dados.
 * @since 3.1
 */
public class EntityLookupValue {

  private EntityLookup entityLookup       = null;
  private String[]     displayFieldValues = {};

  /**
   * Construtor padrão.
   * @param entityLookup EntityLookup que representa o valor.
   * @param displayFieldValues String[] Valores que representam o EntityLookup.
   */
  public EntityLookupValue(EntityLookup entityLookup,
                           String[]     displayFieldValues) {
    this.entityLookup = entityLookup;
    this.displayFieldValues = displayFieldValues;
  }

  /**
   * Retorna o EntityLookup que representa o valor.
   * @return EntityLookup Retorna o EntityLookup que representa o valor.
   */
  public EntityLookup getLookup() {
    return entityLookup;
  }

  /**
   * Retorna os valores que representam o EntityLookup.
   * @return String[] Retorna os valores que representam o EntityLookup.
   */
  public String[] getDisplayFieldValues() {
    return displayFieldValues;
  }

  /**
   * Retorna os valores que representam o EntityLookup separados por ' - '.
   * @return String Retorna os valores que representam o EntityLookup separados
   * por ' - '.
   */
  public String getDisplayFieldValuesToString() {
    // nosso resultado
    String result = StringTools.arrayStringToString(displayFieldValues, " - ");
    // exclui os separadores que não possuírem valor entre si
    result = result.replaceAll(" -  - ", "");
    // retorna
    return result;
  }

}
