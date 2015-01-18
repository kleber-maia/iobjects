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
package iobjects.remote.entity;

import java.util.*;

import iobjects.remote.*;
import java.io.IOException;

/**
 * Representa uma lista de EntityImportExportInfo's.
 */
public class EntityImportExportInfoList implements Serializable {

  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   */
  public EntityImportExportInfoList() {
  }

  /**
   * Adiciona 'entityExportInfo' na lista.
   * @param importExportInfo EntityExportInfo para ser adicionado.
   */
  public void add(EntityImportExportInfo importExportInfo) {
    vector.add(importExportInfo);
  }

  /**
   * Remove todos os EntityExportInfo´s da lista.
   */
  public void clear() {
    vector.clear();
  }

  public void deserialize(SerializeReader reader) throws IOException {
    // quantidade de itens
    int size = reader.readInt();
    // limpa tudo
    clear();
    // loop nos itens
    for (int i=0; i<size; i++) {
      // item da vez
      EntityImportExportInfo item = new EntityImportExportInfo();
      item.deserialize(reader);
      // adiciona
      add(item);
    } // for
  }

  /**
   * Libera recursos.
   */
  public void finalize() {
    clear();
    vector = null;
  }

  /**
   * Retorna o EntityExportInfo na posição indicada por 'index'.
   * @param index Índice do EntityExportInfo que se dejesa retornar.
   * @return Retorna o EntityExportInfo na posição indicada por 'index'.
   */
  public EntityImportExportInfo get(int index) {
    return (EntityImportExportInfo)vector.get(index);
  }

  /**
   * Remove o EntityExportInfo na posição indicada por 'index'.
   * @param index Posição do EntityExportInfo para remover.
   */
  public void remove(int index) {
    vector.remove(index);
  }

  public void serialize(SerializeWriter writer) throws IOException {
    // quantidade de itens
    writer.writeInt(size());
    // loop nos itens
    for (int i=0; i<size(); i++) {
      // item da vez
      get(i).serialize(writer);
    } // for
  }

  /**
   * Retorna a quantidade de EntityExportInfo´s existentes na lista.
   * @return Retorna a quantidade de EntityExportInfo´s existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
