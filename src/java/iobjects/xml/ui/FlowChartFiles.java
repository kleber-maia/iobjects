package iobjects.xml.ui;

import java.sql.*;
import java.util.*;

import iobjects.xml.sql.*;

/**
 * Representa uma lista arquivos de fluxogramas.
 */
public class FlowChartFiles {

  private Vector vectorNames          = new Vector();
  private Vector vectorFlowChartFiles = new Vector();

  /**
   * Construtor padrão.
   */
  public FlowChartFiles() {
  }

  /**
   * Adiciona o FlowChartFile especificado à lista.
   * @param flowChartFile FlowChartFile para adicionar à lista.
   * @param flowChartName Nome da conexão para futuras referências.
   * @return Retorna true em caso de sucesso.
   */
  public boolean add(FlowChartFile flowChartFile, String flowChartName) {
    return vectorFlowChartFiles.add(flowChartFile) && vectorNames.add(flowChartName);
  }

  /**
   * Remove todos os FlowChartFile´s da lista.
   */
  public void clear() {
    vectorFlowChartFiles.clear();
    vectorNames.clear();
  }

  /**
   * Retorna true se existir um FlowChartFile com o nome especificado.
   * @param name flowChartName do FlowChart que se deseja localizar.
   * @return Retorna true se existir um FlowChart com o nome especificado.
   */
  public boolean contains(String name) {
    return indexOf(name) >= 0;
  }

  public void finalize() {
    vectorNames.clear();
    vectorFlowChartFiles.clear();
    vectorFlowChartFiles = null;
    vectorNames = null;
  }

  /**
   * Retorna o FlowChart na posição indicada por index.
   * @param index Índice do FlowChart que se dejesa retornar.
   * @return Retorna o FlowChart na posição indicada por index.
   */
  public FlowChartFile get(int index) {
    return (FlowChartFile)vectorFlowChartFiles.get(index);
  }

  /**
   * Retorna o nome da conexão na posição indicada por index.
   * @param index Índice do nome da conexão que se dejesa retornar.
   * @return Retorna o nome da conexão na posição indicada por index.
   */
  public String getName(int index) {
    return (String)vectorNames.get(index);
  }

  /**
   * Retorna o FlowChartFile com o nome especificado.
   * @param name Nome do FlowChartFile que se deseja localizar, desprezando
   *             maiúsculas e minúsculas.
   * @return Retorna o FlowChartFile com o nome especificado.
   */
  public FlowChartFile get(String name) {
    int index = indexOf(name);
    if (index >= 0)
      return (FlowChartFile)vectorFlowChartFiles.get(index);
    else
      return null;
  }

  /**
   * Retorna o índice do FlowChartFile com o nome especificado.
   * @param name Nome do FlowChartFile que se deseja localizar,
   *                       desprezando letras maiúsculas e minúsculas.
   * @return Retorna o índice do FlowChartFile com o nome especificado.
   */
  public int indexOf(String name) {
    for (int i=0; i<size(); i++) {
      if (((String)vectorNames.elementAt(i)).compareToIgnoreCase(name) == 0)
        return i;
    } // for
    return -1;
  }

  /**
   * Remove o FlowChartFile na posição indicada por index.
   * @param index Posição do FlowChartFile para remover.
   */
  public void remove(int index) {
    vectorFlowChartFiles.remove(index);
    vectorNames.remove(index);
  }

  /**
   * Retorna a quantidade de FlowChart´s existentes na lista.
   * @return Retorna a quantidade de FlowChart´s existentes na lista.
   */
  public int size() {
    return vectorFlowChartFiles.size();
  }

}
