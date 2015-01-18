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

import java.io.IOException;
import java.util.*;

import iobjects.remote.*;

/**
 * Representa uma lista de EntityExportDataset's.
 */
public class EntityExportDatasetList implements Serializable {

  private Vector vector = new Vector();

  /**
   * Construtor padrão.
   */
  public EntityExportDatasetList() {
  }

  /**
   * Adiciona 'entityExportDataset' na lista.
   * @param entityExportDataset EntityExportDataset para ser adicionado.
   */
  public void add(EntityExportDataset entityExportDataset) {
    vector.add(entityExportDataset);
  }

  /**
   * Remove todos os EntityExportDataset´s da lista.
   */
  public void clear() {
    vector.clear();
  }

  public void deserialize(SerializeReader reader) throws IOException {
    // quantidade de datasets
    int size = reader.readInt();
    // loop
    for (int i=0; i<size; i++) {
      // dataset da vez
      EntityExportDataset dataset = new EntityExportDataset();
      dataset.deserialize(reader);
      // adiciona na lista
      add(dataset);
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
   * Retorna o EntityExportDataset na posição indicada por 'index'.
   * @param index Índice do EntityExportDataset que se dejesa retornar.
   * @return Retorna o EntityExportDataset na posição indicada por 'index'.
   */
  public EntityExportDataset get(int index) {
    return (EntityExportDataset)vector.get(index);
  }

  /**
   * Remove o EntityExportDataset na posição indicada por 'index'.
   * @param index Posição do EntityExportDataset para remover.
   */
  public void remove(int index) {
    vector.remove(index);
  }

  public void serialize(SerializeWriter writer) throws IOException {
    // qde de datasets
    writer.writeInt(size());
    // loop
    for (int i=0; i<size(); i++)
      get(i).serialize(writer);
  }

  /**
   * Retorna a quantidade de EntityExportDataset´s existentes na lista.
   * @return Retorna a quantidade de EntityExportDataset´s existentes na lista.
   */
  public int size() {
    return vector.size();
  }

}
