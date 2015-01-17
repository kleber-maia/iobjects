package safenet;

import java.io.*;
import java.math.*;
import java.util.*;

import iobjects.*;

/**
 * Utilitário para acesso ao kit de segurança Compact500 da Safenet. Esta classe
 * realiza chamadas para a biblioteca jc500.dll (somente Windows) e elimina a
 * necessidade de programação de baixo nível.
 * @since 2006
 */
public class Compact500 {

  /**
   * Representa uma mensagem de status.
   */
  static class Status {
    int    code    = 0;
    String message = "";
    public Status(int code, String message) {
      this.code = code;
      this.message = message;
    }
  }

  /**
   * Representa uma lista de mensagens de status.
   */
  static class StatusList {
    Vector vector = new Vector();
    public void add(int code, String message) {
      vector.add(new Status(code, message));
    }
    public String getMessage(int code) {
      for (int i=0; i<vector.size(); i++) {
        Status result = (Status)vector.get(i);
        if (result.code == code)
          return result.message;
      } // for
      return "DESCONHECIDO";
    }
  }

  static private final StatusList STATUS_LIST = new StatusList();

  static {
    STATUS_LIST.add(0, "OK");
    STATUS_LIST.add(-1, "GPF");
    STATUS_LIST.add(-2, "REENTER");
    STATUS_LIST.add(-3, "PARAM_ERROR");
    STATUS_LIST.add(-4, "SEND_DATA_FAIL");
    STATUS_LIST.add(-5, "TIMEOUT");
    STATUS_LIST.add(-6, "PARAM_MODIF_COMM");
    STATUS_LIST.add(-7, "HARDKEY_NOT_FOUND");
    STATUS_LIST.add(-8, "PARAM_LENGTH_ERROR");
    STATUS_LIST.add(-9, "MEMORY_FAIL");
    STATUS_LIST.add(-10, "VIRTUAL_DRIVER_NOT_FOUND");
    STATUS_LIST.add(-11, "VIRTUAL_DRIVER_NOT_LOADED");
    STATUS_LIST.add(-12, "VIRTUAL_DRIVER_EXEC_ERROR");
    STATUS_LIST.add(-13, "DLL_NET_ERROR");
    STATUS_LIST.add(-14, "LOAD_ERROR");
    STATUS_LIST.add(-15, "GETPROC_ERROR");
    STATUS_LIST.add(-16, "PSERV_CREATE_COMM_ERROR");
    STATUS_LIST.add(-17, "PSERV_COMM_ERROR");
    STATUS_LIST.add(-18, "PSERV_PARAM_COMM_ERROR");
    STATUS_LIST.add(-19, "PARAM_MODIF_NET");
    STATUS_LIST.add(-20, "ALL_NET_LICENSES_IN_USE");
    STATUS_LIST.add(-21, "PSERV_COMM_NOT_STARTED");
    STATUS_LIST.add(-22, "PSERV_INI_NOT_FOUND");
    STATUS_LIST.add(-50, "COUNTER_ZERO");
    STATUS_LIST.add(-51, "SYSTEM_DATE_ERROR");
    STATUS_LIST.add(-52, "SYSTEM_HOUR_ERROR");
    STATUS_LIST.add(-54, "AUTORIZATION_CODE_ERROR");
    STATUS_LIST.add(-55, "INVALID_DATE");
    STATUS_LIST.add(-400, "LINK_ERROR");
    STATUS_LIST.add(-100, "LIBRARY_FAIL");
    STATUS_LIST.add(-200, "FUNCTION_NOT_FOUND");
    STATUS_LIST.add(-300, "INVALID_DATA");
    STATUS_LIST.add(-301, "INVALID_INTERNAL_CODE");
    STATUS_LIST.add(-302, "INVALID_ACCESS");
    STATUS_LIST.add(-303, "INVALID_RETURN");
    STATUS_LIST.add(-304, "INVALID_STRING");
  }

  static private final int API_RESULT_OK             = 0;
  static private final int DLL_STATUS_OK             = 0;
  static private final int COMMAND_DECREMENT_COUNTER = 9;
  static private final int COMMAND_DECRYPT           = 21;
  static private final int COMMAND_ENCRYPT           = 20;
  static private final int COMMAND_INITIALIZE        = 3;
  static private final int COMMAND_READ_COUNTER      = 8;
  static private final int COMMAND_READ_ALL_MEMORY   = 11;
  static private final int COMMAND_READ_MEMORY       = 1;
  static private final int COMMAND_WRITE_ALL_MEMORY  = 12;
  static private final int COMMAND_WRITE_MEMORY      = 2;
  static private final int PLUG_TYPE_PARALEL         = 3;
  static private final int PLUG_TYPE_USB             = 85;

  static private final String ENCODE_US_ASCII = "US-ASCII";
  static private final String LABRARY_NAME    = "JC500";

  private int dllStatus = -400;

  static public void main(String args[]) {
    safenet.Compact500 compact500 = new safenet.Compact500();
    try {
      System.out.println("Inicializando...");
      System.out.print("Código interno: ");
      System.out.println(compact500.initialize("51U48WF04"));
      // *
      System.out.print("Decrementando Contador...");
      compact500.decrementCounter();
      System.out.println(compact500.readCounter());
      // *
      byte index = 0;
      int  buf = compact500.readCounter();
      System.out.println("Grava memória " + index + ": " + buf);
      compact500.writeIntMemory(index, buf);
      // *
      System.out.print("Lê memória " + index + ": ");
      System.out.println(compact500.readIntMemory(index));
      // *
      StringBuffer buf2 = new StringBuffer();
      buf2.append("iManager Web 1.0");
      while (buf2.length() < 480)
        buf2.append(' ');
      System.out.println("Grava memória total: " + buf2.toString());
      compact500.writeMemory(buf2.toString());
      // *
      System.out.print("Lê memória total: ");
      System.out.println(compact500.readMemory());
      // *
      String value = "iManager Web 1.0";
      System.out.print("Criptografando '" + value + "': ");
      value = compact500.encrypt(value);
      System.out.println(value);
      System.out.print("Descriptografando '" + value + "': ");
      value = compact500.decrypt(value);
      System.out.println(value);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Construtor padrão.
   */
  public Compact500() {
    // carrega a biblioteca do Compact500
    System.loadLibrary(LABRARY_NAME);
    // se chegou aqui...carregamos a biblioteca com sucesso
    dllStatus = DLL_STATUS_OK;
  }

  /**
   * Função da biblioteca para execução de comandos.
   * @param cmd byte[] com os dados do comando para ser executado.
   * @return int Retorna um dos códigos de status definido.
   */
  private native int C500(byte[] cmd);

  /**
   * Verifica se a biblioteca foi carrega com sucesso.
   * @param methodName String Nome do método que deseja realizar a verificação.
   * @throws Exception Para o caso de a biblioteca não ter sido carregada com
   *                   sucesso.
   */
  private void checkLibrary(String methodName) throws Exception {
    // se a biblioteca não foi carregada...exceção
    if (dllStatus != DLL_STATUS_OK)
      throw new ExtendedException(getClass().getName(), methodName, "A biblioteca não foi inicializa com o status " + STATUS_LIST.getMessage(dllStatus) + ".");
  }

  /**
   * Copia um número de bytes de um array para outro a partir de uma determinada
   * posição.
   * @param dest byte[] Array de destino.
   * @param beginDest int Início no array de destino.
   * @param size int Tamanho de bytes para serem copiados.
   * @param beginSource int Início no array de origem.
   * @param source byte[] Array de origem.
   */
  private void copyBytes(byte[] dest, int beginDest, int size, int beginSource, byte[] source) {
    for (int i=beginDest, j=beginSource; ((i<(beginDest + size)) && (j<(beginSource + size)) && (j < source.length)); i++, j++) {
      dest[i] = source[j];
    } // for
  }

  /**
   * Decrementa o contador interno do plug.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public void decrementCounter() throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("decrementCounter");
    // se a contagem já é zero...dispara
    if (readCounter() == 0)
      return;
    // comando desejado
    byte[] command = new byte[80];
    command[0] = COMMAND_DECREMENT_COUNTER;
    // chama a API C500
    int apiResult = C500(command);
    // se não foi bem sucedido...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "decrementCounter", "A chamada falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");
  }

  /**
   * Retorna 'value' descriptografado com a semenete gravada no plug.
   * @param value String Valor que se deseja criptografar.
   * @return String Retorna 'value' descriptografado com a semenete gravada no plug.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public String decrypt(String value) throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("decrypt");
    // se o tamanho é inválido...exceção
    if ((value.length() < 12) || (value.length() > 250))
      throw new ExtendedException(getClass().getName(), "decrypt", "O valor deve ter entre 12 e 250 caracteres.");
    // comando desejado
    byte[] command = new byte[value.length() + 2];
    command[0] = COMMAND_DECRYPT;
    command[1] = (byte)value.length();
    copyBytes(command, 2, value.length(), 0, value.getBytes());
    // chama a API C500
    int apiResult = C500(command);
    // se não foi bem sucedido...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "decrypt", "A chamada falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");
    // compõe o resultado
    byte[] buffer = new byte[value.length()];
    copyBytes(buffer, 0, value.length(), 2, command);
    String result = new String(buffer);
    // retorna
    return result;
  }

  /**
   * Retorna 'value' criptografado com a semenete gravada no plug.
   * @param value String Valor que se deseja criptografar.
   * @return String Retorna 'value' criptografado com a semenete gravada no plug.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public String encrypt(String value) throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("encrypt");
    // se o tamanho é inválido...exceção
    if ((value.length() < 12) || (value.length() > 250))
      throw new ExtendedException(getClass().getName(), "encrypt", "O valor deve ter entre 12 e 250 caracteres.");
    // comando desejado
    byte[] command = new byte[value.length() + 2];
    command[0] = COMMAND_ENCRYPT;
    command[1] = (byte)value.length();
    copyBytes(command, 2, value.length(), 0, value.getBytes());
    // chama a API C500
    int apiResult = C500(command);
    // se não foi bem sucedido...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "encrypt", "A chamada falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");
    // compõe o resultado
    byte[] buffer = new byte[value.length()];
    copyBytes(buffer, 0, value.length(), 2, command);
    String result = new String(buffer);
    // retorna
    return result;
  }

  /**
   * Libera recursos da biblioteca e retorna true em caso de sucesso.
   * @return boolean Libera recursos da biblioteca e retorna true em caso de
   * sucesso.
   */
  private native boolean freeDll();

  /**
   * Inicializa o plug e retorna o código interno.
   * @param password String Senha para acesso ao plug.
   * @return String Inicializa o plug e retorna o código interno.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public String initialize(String password) throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("initialize");

    // inicializa a dll do C500
    loadDll();
    // buffer
    byte[] buffer = null;
    try {
      // tenta transformar os dados recebidos em um byte[]
      buffer = password.getBytes(ENCODE_US_ASCII);
    }
    catch (UnsupportedEncodingException e) {
      throw new ExtendedException(getClass().getName(), "initialize", "A senha informada contém caracteres inválidos para o encode " + ENCODE_US_ASCII + ".");
    } // try-catch

    // preenche o comando com a senha
    byte[] command = new byte[80];
    copyBytes(command, 1, buffer.length, 0, buffer);
    // informa o comando desejado
    command[0] = COMMAND_INITIALIZE;
    // terminar a string da senha com nulo
    command[10] = 0;
    // chama a API C500
    int apiResult = C500(command);
    // se a chamada da API foi bem sucedida...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "initialize", "A inicialização falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");

    // se o tipo do plug é desconhecido...exceção
    if ((command[0] != PLUG_TYPE_PARALEL) && (command[0] != PLUG_TYPE_USB))
      throw new ExtendedException(getClass().getName(), "initialize", "Acesso inválido.");

    // retorna o código interno
    byte[] internalCode = new byte[8];
    copyBytes(internalCode, 0, 8, 1, command);
    try {
      // tenta converter o código interno para String
      return new String(internalCode, ENCODE_US_ASCII);
    }
    catch (UnsupportedEncodingException e) {
      throw new ExtendedException(getClass().getName(), "initialize", "O código interno contém caracteres inválidos para o encode " + ENCODE_US_ASCII + ".");
    } // try-catch
  }

  /**
   * Inicializa a biblioteca e retorna true em caso de sucesso.
   * @return boolean Inicializa a biblioteca e retorna true em caso de sucesso.
   */
  private native boolean loadDll();

  /**
   * Retorna o contador interno do plug.
   * @return int Retorna o contador interno do plug.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public int readCounter() throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("readCounter");
    // comando desejado
    byte[] command = new byte[10];
    command[0] = COMMAND_READ_COUNTER;
    // chama a API C500
    int apiResult = C500(command);
    // se não foi bem sucedido...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "readCounter", "A chamada falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");
    // compõe o resultado
    BigInteger result = new BigInteger(new byte[]{command[1], command[2]});
    // retorna
    return result.intValue();
  }

  /**
   * Retorna toda a memória do plug.
   * @return int Retorna toda a memória do plug.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public String readMemory() throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("readMemory");
    // comando desejado
    byte[] command = new byte[481];
    command[0] = COMMAND_READ_ALL_MEMORY;
    // chama a API C500
    int apiResult = C500(command);
    // se não foi bem sucedido...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "readMemory", "A chamada falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");
    // compõe o resultado
    byte[] buffer = new byte[480];
    copyBytes(buffer, 0, 479, 1, command);
    String result = new String(buffer).replace('\0', ' ');
    // retorna
    return result;
  }

  /**
   * Retorna o valor contido na posição de memória 'index'.
   * @param index byte Índice da memória que se deseja ler entre 0 e 239.
   * @return int Retorna o valor contido na posição de memória 'index'.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public int readIntMemory(byte index) throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("readMemory");
    // se é uma posição inválida de memória...exceção
    if (index > 239)
      throw new ExtendedException(getClass().getName(), "readMemory", "A posição de memória é inválida: " + index + ".");
    // comando desejado
    byte[] command = new byte[10];
    command[0] = COMMAND_READ_MEMORY;
    command[3] = index;
    // chama a API C500
    int apiResult = C500(command);
    // se não foi bem sucedido...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "readMemory", "A chamada falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");
    // compõe o resultado
    BigInteger result = new BigInteger(new byte[]{command[1], command[2]});
    // retorna
    return result.intValue();
  }

  /**
   * Retorna o valor contido na posição de memória 'index'.
   * @param index byte Índice da memória que se deseja ler entre 0 e 239.
   * @return int Retorna o valor contido na posição de memória 'index'.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public String readMemory(byte index) throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("readMemory");
    // se é uma posição inválida de memória...exceção
    if (index > 239)
      throw new ExtendedException(getClass().getName(), "readMemory", "A posição de memória é inválida: " + index + ".");
    // comando desejado
    byte[] command = new byte[10];
    command[0] = COMMAND_READ_MEMORY;
    command[3] = index;
    // chama a API C500
    int apiResult = C500(command);
    // se não foi bem sucedido...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "readMemory", "A chamada falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");
    // compõe o resultado
    String result = new String(new byte[]{command[1], command[2]});
    // retorna
    return result;
  }

  /**
   * Grava 'value' na memória.
   * @param value String Valor que se deseja gravar. Deve ter 480 bytes.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public void writeMemory(String value) throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("writeMemory");
    // se o tamanho é inválido...exceção
    if (value.length() != 480)
      throw new ExtendedException(getClass().getName(), "writeMemory", "O tamanho do valor é inválido: " + value.length() + ".");
    // comando desejado
    byte[] command = new byte[481];
    command[0] = COMMAND_WRITE_ALL_MEMORY;
    copyBytes(command, 1, 480, 0, value.getBytes());
    // chama a API C500
    int apiResult = C500(command);
    // se não foi bem sucedido...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "readMemory", "A chamada falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");
  }

  /**
   * Grava 'value' na posição de memória 'index'.
   * @param index byte Índice da memória que se deseja gravar entre 0 e 239.
   * @param value int Valor que se deseja gravar.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public void writeIntMemory(byte index, int value) throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("writeMemory");
    // comando desejado
    byte[] command = new byte[10];
    BigInteger bigValue = new BigInteger(value + "");
    byte[] bytes = bigValue.toByteArray();
    command[0] = COMMAND_WRITE_MEMORY;
    command[1] = bytes[0];
    command[2] = bytes[1];
    command[3] = index;
    // chama a API C500
    int apiResult = C500(command);
    // se não foi bem sucedido...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "readMemory", "A chamada falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");
  }

  /**
   * Grava 'value' na posição de memória 'index'.
   * @param index byte Índice da memória que se deseja gravar entre 0 e 239.
   * @param value String Valor que se deseja gravar. Deve ter 2 bytes.
   * @throws Exception Em caso de exceção na tentativa de acesso ao plug
   *                   ou na chamada das funções da API.
   */
  public void writeMemory(byte index, String value) throws Exception {
    // verifica o estado da biblioteca
    checkLibrary("writeMemory");
    // se o tamanho é inválido...exceção
    if (value.length() != 2)
      throw new ExtendedException(getClass().getName(), "writeMemory", "O tamanho do valor é inválido: " + value.length() + ".");
    // comando desejado
    byte[] command = new byte[10];
    byte[] bytes   = value.getBytes();
    command[0] = COMMAND_WRITE_MEMORY;
    command[1] = bytes[0];
    command[2] = bytes[1];
    command[3] = index;
    // chama a API C500
    int apiResult = C500(command);
    // se não foi bem sucedido...
    if (apiResult != API_RESULT_OK)
      throw new ExtendedException(getClass().getName(), "readMemory", "A chamada falhou com o código " + STATUS_LIST.getMessage(apiResult) + ".");
  }

}
