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
