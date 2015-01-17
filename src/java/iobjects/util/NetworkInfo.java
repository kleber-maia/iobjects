package iobjects.util;

import java.net.*;
import java.io.*;
import java.util.*;

import iobjects.*;

/**
 * Ferramenta de informações de rede.
 */
public final class NetworkInfo {

  /**
   * Retorna o nome do computador.
   * @throws Exception Em caso de exceção na tentativa de obter informações de
   *         rede.
   * @return String Retorna o nome do computador.
   */
  static public String getHostName() throws Exception {
    return InetAddress.getLocalHost().getHostName();
  }

  /**
   * Retorna o endereço IP.
   * @throws Exception Em caso de exceção na tentativa de obter informações de
   *         rede.
   * @return String Retorna o endereço IP.
   */
  static public String getIPAddress() throws Exception {
    return InetAddress.getLocalHost().getHostAddress();
  }

  /**
   * Retorna o enderço Mac.
   * @throws Exception Em caso de exceção na tentativa de obter informações de
   *         rede.
   * @return String Retorna o endereço Mac.
   */
  static public String getMacAddress() throws Exception {
    // nome do sistema operacional
    String os = System.getProperty("os.name");
    // se é Windows...
    if (os.indexOf("Windows") >= 0)
      return parseWindowsMacAddress(runCommand("ipconfig /all"));
    // se é Linux...
    else if (os.indexOf("Linux") >=0 )
      return parseLinuxMacAddress(runCommand("ifconfig"));
    // outro...exceção
    else
      throw new ExtendedException("iobjects.util.NetworkInfo", "getMacAddress", "Sistema operacional desconhecido: " + os + ".");
  }

  /**
   * Retorna true se 'value' é um Mac Address recebido é valido.
   * @param value String Mac Address para ser verificado.
   * @return boolean Retorna true se 'value' é um Mac Address recebido é valido.
   */
  private final static boolean isMacAddress(String value) {
    return value.length() == 17;
  }

  /**
   * Localiza o Mac Address em 'commandResponse'.
   * @param commandResponse String Resultado da execução do comando que retorna
   *        informações de rede.
   * @throws Exception Em caso de o Mac Address não ser encontrado.
   * @return String Localiza o Mac Address em 'commandResponse'.
   */
  private final static String parseLinuxMacAddress(String commandResponse) throws Exception {
    // nosso IP
    String localHost = getIPAddress();
    // linhas do resultado do comando IPConfig
    StringTokenizer tokenizer = new StringTokenizer(commandResponse, "\n");
    // loop nas linhas do resultado
    String lastMacAddress = null;
    while (tokenizer.hasMoreTokens()) {
      // linha da vez
      String line = tokenizer.nextToken().trim();
      // a linha contém o IP da máquina?
      boolean containsLocalHost = line.indexOf(localHost) >= 0;
      // se contém o IP e já encontramos um MacAddress...retorna
      if (containsLocalHost && lastMacAddress != null)
        return lastMacAddress;
      // verifica se a linha possui o Mac Address
      int macAddressPosition = line.indexOf("HWaddr");
      // se não tem...continua
      if (macAddressPosition <= 0)
        continue;
      // este pode ser o Mac Address
      String macAddressCandidate = line.substring(macAddressPosition + 6).trim();
      // se é um MacAddress...guarda-o
      if (isMacAddress(macAddressCandidate)) {
        lastMacAddress = macAddressCandidate;
        continue;
      } // if
    } // while
    // se chegou até aqui...não achamos o Mac Address
    throw new ExtendedException("iobjects.util.NetworkInfo", "linuxParseMacAddress", "Não foi possível encontrar o Mac Address.");
  }

  /**
   * Localiza o Mac Address em 'commandResponse'.
   * @param commandResponse String Resultado da execução do comando que retorna
   *        informações de rede.
   * @throws Exception Em caso de o Mac Address não ser encontrado.
   * @return String Localiza o Mac Address em 'commandResponse'.
   */
  private final static String parseWindowsMacAddress(String commandResponse) throws Exception {

    /**
     * As linhas de código comentadas a seguir são parte do código original
     * do método. A linha de código que contém a expressão regular foi adicionada
     * em substituição do método "isMacAddress" que é falho. Essas alterações
     * foram realizadas na tentativa de obter o Mac Address mesmo se o computador
     * não estiver com a rede operante.
     */

    // nosso IP
    String localHost = getIPAddress();
    // linhas de resultado do comando IPConfig
    StringTokenizer tokenizer = new StringTokenizer(commandResponse, "\n");
    // loop nas linhas
//    String lastMacAddress = null;
    while (tokenizer.hasMoreTokens()) {
      // linha da vez
      String line = tokenizer.nextToken().trim();
      // se a linha contém o IP e já temos um Mac Address...retorna
//      if (line.endsWith(localHost) && lastMacAddress != null)
//        return lastMacAddress;
      // posição possível do Mac Address
      int macAddressPosition = line.indexOf(":");
      // se não possui...continua
      if (macAddressPosition <= 0)
        continue;
      // este pode ser o Mac Address
      String macAddressCandidate = line.substring(macAddressPosition + 1).trim();
      // se achamos o Mac Address...dispara
      if (macAddressCandidate.matches("\\w\\w-\\w\\w-\\w\\w-\\w\\w-\\w\\w-\\w\\w"))
        return macAddressCandidate;
//      if (isMacAddress(macAddressCandidate)) {
//        lastMacAddress = macAddressCandidate;
//        continue;
//      } // if
    } // while
    // se chegou até aqui...não achamos o Mac Address
    throw new ExtendedException("iobjects.util.NetworkInfo", "windowsParseMacAddress", "Não foi possível encontrar o Mac Address.");
  }

  /**
   * Executa o 'command' e retorna o seu resultado.
   * @param command String Comando para ser executado.
   * @throws Exception Em caso de exceção na tentativa de executar o comando.
   * @return String Executa o 'comand' e retorna o seu resultado.
   */
  private final static String runCommand(String command) throws Exception {
    // executa o comando
    Process process = Runtime.getRuntime().exec(command);
    // resultado do comando
    BufferedInputStream inputStream = new BufferedInputStream(process.getInputStream());
    // resultado do comando numa String
    StringBuffer buffer= new StringBuffer();
    while (true) {
      int c = inputStream.read();
      if (c == -1)
        break;
      buffer.append((char)c);
    } // while
    // retorna
    String result = buffer.toString();
    inputStream.close();
    return result;
  }

}
