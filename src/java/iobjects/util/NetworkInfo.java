package iobjects.util;

import java.net.*;
import java.io.*;
import java.util.*;

import iobjects.*;

/**
 * Ferramenta de informa��es de rede.
 */
public final class NetworkInfo {

  /**
   * Retorna o nome do computador.
   * @throws Exception Em caso de exce��o na tentativa de obter informa��es de
   *         rede.
   * @return String Retorna o nome do computador.
   */
  static public String getHostName() throws Exception {
    return InetAddress.getLocalHost().getHostName();
  }

  /**
   * Retorna o endere�o IP.
   * @throws Exception Em caso de exce��o na tentativa de obter informa��es de
   *         rede.
   * @return String Retorna o endere�o IP.
   */
  static public String getIPAddress() throws Exception {
    return InetAddress.getLocalHost().getHostAddress();
  }

  /**
   * Retorna o ender�o Mac.
   * @throws Exception Em caso de exce��o na tentativa de obter informa��es de
   *         rede.
   * @return String Retorna o endere�o Mac.
   */
  static public String getMacAddress() throws Exception {
    // nome do sistema operacional
    String os = System.getProperty("os.name");
    // se � Windows...
    if (os.indexOf("Windows") >= 0)
      return parseWindowsMacAddress(runCommand("ipconfig /all"));
    // se � Linux...
    else if (os.indexOf("Linux") >=0 )
      return parseLinuxMacAddress(runCommand("ifconfig"));
    // outro...exce��o
    else
      throw new ExtendedException("iobjects.util.NetworkInfo", "getMacAddress", "Sistema operacional desconhecido: " + os + ".");
  }

  /**
   * Retorna true se 'value' � um Mac Address recebido � valido.
   * @param value String Mac Address para ser verificado.
   * @return boolean Retorna true se 'value' � um Mac Address recebido � valido.
   */
  private final static boolean isMacAddress(String value) {
    return value.length() == 17;
  }

  /**
   * Localiza o Mac Address em 'commandResponse'.
   * @param commandResponse String Resultado da execu��o do comando que retorna
   *        informa��es de rede.
   * @throws Exception Em caso de o Mac Address n�o ser encontrado.
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
      // a linha cont�m o IP da m�quina?
      boolean containsLocalHost = line.indexOf(localHost) >= 0;
      // se cont�m o IP e j� encontramos um MacAddress...retorna
      if (containsLocalHost && lastMacAddress != null)
        return lastMacAddress;
      // verifica se a linha possui o Mac Address
      int macAddressPosition = line.indexOf("HWaddr");
      // se n�o tem...continua
      if (macAddressPosition <= 0)
        continue;
      // este pode ser o Mac Address
      String macAddressCandidate = line.substring(macAddressPosition + 6).trim();
      // se � um MacAddress...guarda-o
      if (isMacAddress(macAddressCandidate)) {
        lastMacAddress = macAddressCandidate;
        continue;
      } // if
    } // while
    // se chegou at� aqui...n�o achamos o Mac Address
    throw new ExtendedException("iobjects.util.NetworkInfo", "linuxParseMacAddress", "N�o foi poss�vel encontrar o Mac Address.");
  }

  /**
   * Localiza o Mac Address em 'commandResponse'.
   * @param commandResponse String Resultado da execu��o do comando que retorna
   *        informa��es de rede.
   * @throws Exception Em caso de o Mac Address n�o ser encontrado.
   * @return String Localiza o Mac Address em 'commandResponse'.
   */
  private final static String parseWindowsMacAddress(String commandResponse) throws Exception {

    /**
     * As linhas de c�digo comentadas a seguir s�o parte do c�digo original
     * do m�todo. A linha de c�digo que cont�m a express�o regular foi adicionada
     * em substitui��o do m�todo "isMacAddress" que � falho. Essas altera��es
     * foram realizadas na tentativa de obter o Mac Address mesmo se o computador
     * n�o estiver com a rede operante.
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
      // se a linha cont�m o IP e j� temos um Mac Address...retorna
//      if (line.endsWith(localHost) && lastMacAddress != null)
//        return lastMacAddress;
      // posi��o poss�vel do Mac Address
      int macAddressPosition = line.indexOf(":");
      // se n�o possui...continua
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
    // se chegou at� aqui...n�o achamos o Mac Address
    throw new ExtendedException("iobjects.util.NetworkInfo", "windowsParseMacAddress", "N�o foi poss�vel encontrar o Mac Address.");
  }

  /**
   * Executa o 'command' e retorna o seu resultado.
   * @param command String Comando para ser executado.
   * @throws Exception Em caso de exce��o na tentativa de executar o comando.
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
