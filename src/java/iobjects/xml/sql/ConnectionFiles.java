package iobjects.xml.sql;

import java.io.*;
import java.sql.*;
import java.util.*;

import iobjects.sql.*;
import iobjects.util.*;
import iobjects.xml.sql.*;

/**
 * Representa uma lista arquivos de conexões.
 */
public class ConnectionFiles {

  private Vector vectorNames           = new Vector();
  private Vector vectorConnectionFiles = new Vector();

  /**
   * Construtor padrão.
   */
  public ConnectionFiles() {
  }

  /**
   * Adiciona o ConnectionFile especificado à lista.
   * @param connectionFile ConnectionFile para adicionar à lista.
   * @param connectionName Nome da conexão para futuras referencias.
   * @return Retorna true em caso de sucesso.
   */
  public boolean add(ConnectionFile connectionFile, String connectionName) {
    return vectorConnectionFiles.add(connectionFile) && vectorNames.add(connectionName);
  }

  /**
   * Remove todos os ConnectionFile´s da lista.
   */
  public void clear() {
    vectorConnectionFiles.clear();
    vectorNames.clear();
  }

  /**
   * Retorna true se existir um ConnectionFile com o nome especificado.
   * @param name connectionName do Connection que se deseja localizar.
   * @return Retorna true se existir um Connection com o nome especificado.
   */
  public boolean contains(String name) {
    return indexOf(name) >= 0;
  }

  public void finalize() {
    vectorNames.clear();
    vectorConnectionFiles.clear();
    vectorConnectionFiles = null;
    vectorNames = null;
  }

  /**
   * Retorna o Connection na posição indicada por index.
   * @param index Índice do Connection que se dejesa retornar.
   * @return Retorna o Connection na posição indicada por index.
   */
  public ConnectionFile get(int index) {
    return (ConnectionFile)vectorConnectionFiles.get(index);
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
   * Retorna o ConnectionFile com o nome especificado.
   * @param name Nome do ConnectionFile que se deseja localizar, desprezando
   *             maiúsculas e minúsculas.
   * @return Retorna o ConnectionFile com o nome especificado.
   */
  public ConnectionFile get(String name) {
    return get(name, false);
  }

  /**
   * Retorna o ConnectionFile com o nome especificado.
   * @param name Nome do ConnectionFile que se deseja localizar, desprezando
   *             maiúsculas e minúsculas.
   * @param reload True para que o arquivo seja recarregado, atualizando assim
   *               seus parâmetros, ou tente ser localizado caso ainda não
   *               tenha sido carregado.
   * @return Retorna o ConnectionFile com o nome especificado.
   */
  public ConnectionFile get(String name, boolean reload) {
    // índice do arquivo de conexão desejado
    int index = indexOf(name);
    // se não encontramos e podemos carregar...
    if ((index < 0) && reload) {
      // nosso resultado
      ConnectionFile result = null;
      try {
        // obtém o primeiro arquivo de conexão para definir
        // o diretório onde deve ser procurado o arquivo atual
        ConnectionFile first = get(0);
        File connectionFilePath = new File(first.getFileName());
        String path = FileTools.includeSeparatorChar(connectionFilePath.getParent());
        // tenta carregar o arquivo
        result = new ConnectionFile(path + name + ConnectionManager.CONFIG_FILE_EXTENSION);
        // adiciona na lista
        add(result, name);
      }
      catch (Exception e) {
      } // try-catch
      // retorna
      return result;
    }
    // se encontramos...
    else if (index >= 0) {
      // obtém o arquivo da lista
      ConnectionFile result = (ConnectionFile)vectorConnectionFiles.get(index);
      try {
        // se devemos recarregá-lo...
        if (reload) {
          // recarrega
          result = new ConnectionFile(result.getFileName());
          // atualiza na lista
          vectorConnectionFiles.set(index, result);
        } // if
      }
      catch (Exception e) {
      } // try-catch
      // retorna
      return result;
    }
    // se não encontramos...retorna nada
    else
      return null;
  }

  /**
   * Retorna o índice do ConnectionFile com o nome especificado.
   * @param name Nome do ConnectionFile que se deseja localizar,
   *                       desprezando letras maiúsculas e minúsculas.
   * @return Retorna o índice do ConnectionFile com o nome especificado.
   */
  public int indexOf(String name) {
    for (int i=0; i<size(); i++) {
      if (((String)vectorNames.elementAt(i)).compareToIgnoreCase(name) == 0)
        return i;
    } // for
    return -1;
  }

  /**
   * Remove o ConnectionFile na posição indicada por index.
   * @param index Posição do ConnectionFile para remover.
   */
  public void remove(int index) {
    vectorConnectionFiles.remove(index);
    vectorNames.remove(index);
  }

  /**
   * Retorna a quantidade de Connection´s existentes na lista.
   * @return Retorna a quantidade de Connection´s existentes na lista.
   */
  public int size() {
    return vectorConnectionFiles.size();
  }

}
