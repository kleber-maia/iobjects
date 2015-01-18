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
package iobjects.xml.sql;

import java.io.*;
import java.sql.*;
import java.util.*;

import iobjects.sql.*;
import iobjects.util.*;
import iobjects.xml.sql.*;

/**
 * Representa uma lista arquivos de conex�es.
 */
public class ConnectionFiles {

  private Vector vectorNames           = new Vector();
  private Vector vectorConnectionFiles = new Vector();

  /**
   * Construtor padr�o.
   */
  public ConnectionFiles() {
  }

  /**
   * Adiciona o ConnectionFile especificado � lista.
   * @param connectionFile ConnectionFile para adicionar � lista.
   * @param connectionName Nome da conex�o para futuras referencias.
   * @return Retorna true em caso de sucesso.
   */
  public boolean add(ConnectionFile connectionFile, String connectionName) {
    return vectorConnectionFiles.add(connectionFile) && vectorNames.add(connectionName);
  }

  /**
   * Remove todos os ConnectionFile�s da lista.
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
   * Retorna o Connection na posi��o indicada por index.
   * @param index �ndice do Connection que se dejesa retornar.
   * @return Retorna o Connection na posi��o indicada por index.
   */
  public ConnectionFile get(int index) {
    return (ConnectionFile)vectorConnectionFiles.get(index);
  }

  /**
   * Retorna o nome da conex�o na posi��o indicada por index.
   * @param index �ndice do nome da conex�o que se dejesa retornar.
   * @return Retorna o nome da conex�o na posi��o indicada por index.
   */
  public String getName(int index) {
    return (String)vectorNames.get(index);
  }

  /**
   * Retorna o ConnectionFile com o nome especificado.
   * @param name Nome do ConnectionFile que se deseja localizar, desprezando
   *             mai�sculas e min�sculas.
   * @return Retorna o ConnectionFile com o nome especificado.
   */
  public ConnectionFile get(String name) {
    return get(name, false);
  }

  /**
   * Retorna o ConnectionFile com o nome especificado.
   * @param name Nome do ConnectionFile que se deseja localizar, desprezando
   *             mai�sculas e min�sculas.
   * @param reload True para que o arquivo seja recarregado, atualizando assim
   *               seus par�metros, ou tente ser localizado caso ainda n�o
   *               tenha sido carregado.
   * @return Retorna o ConnectionFile com o nome especificado.
   */
  public ConnectionFile get(String name, boolean reload) {
    // �ndice do arquivo de conex�o desejado
    int index = indexOf(name);
    // se n�o encontramos e podemos carregar...
    if ((index < 0) && reload) {
      // nosso resultado
      ConnectionFile result = null;
      try {
        // obt�m o primeiro arquivo de conex�o para definir
        // o diret�rio onde deve ser procurado o arquivo atual
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
      // obt�m o arquivo da lista
      ConnectionFile result = (ConnectionFile)vectorConnectionFiles.get(index);
      try {
        // se devemos recarreg�-lo...
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
    // se n�o encontramos...retorna nada
    else
      return null;
  }

  /**
   * Retorna o �ndice do ConnectionFile com o nome especificado.
   * @param name Nome do ConnectionFile que se deseja localizar,
   *                       desprezando letras mai�sculas e min�sculas.
   * @return Retorna o �ndice do ConnectionFile com o nome especificado.
   */
  public int indexOf(String name) {
    for (int i=0; i<size(); i++) {
      if (((String)vectorNames.elementAt(i)).compareToIgnoreCase(name) == 0)
        return i;
    } // for
    return -1;
  }

  /**
   * Remove o ConnectionFile na posi��o indicada por index.
   * @param index Posi��o do ConnectionFile para remover.
   */
  public void remove(int index) {
    vectorConnectionFiles.remove(index);
    vectorNames.remove(index);
  }

  /**
   * Retorna a quantidade de Connection�s existentes na lista.
   * @return Retorna a quantidade de Connection�s existentes na lista.
   */
  public int size() {
    return vectorConnectionFiles.size();
  }

}
