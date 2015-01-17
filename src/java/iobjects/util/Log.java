package iobjects.util;

import java.io.*;
import java.util.*;

import iobjects.util.*;

/**
 * Utilit�rio para gera�a��o de arquivos de log para a aplica��o.
 * @since 2006
 */
public class Log {

  static private final String LOG_EXTENSION = ".log";

  private Date   date    = null;
  private String logPath = "";

  /**
   * Construtor padr�o.
   * @param logPath String Caminho onde os arquivos de log s�o gerados.
   */
  public Log(String logPath) {
    // guarda a data e hora atuais
    date = new Date();
    // guarda nosso path
    this.logPath = FileTools.includeSeparatorChar(logPath);
  }

  /**
   * Limpa o diret�rio de logs apagando todos os arquivos mais antigos que
   * a quantidade de meses indicados. A data de refer�ncia � a data atual
   * do sistema subtra�dos a quantidade de meses indicados.
   * @param monthsToMaintain int Quantidade de meses cujos logs devem ser
   *                             mantidos contando com o m�s atual. Ex.:
   *                             2 indica que todos os arquivos de log do m�s
   *                             atual e do m�s anterior ser�o mantidos.
   */
  public void clean(int monthsToMaintain) {
    // data de refer�ncia
    Date referenceDate = DateTools.getActualDate();
         referenceDate = DateTools.getCalculatedMonths(referenceDate, monthsToMaintain * -1);
    // obt�m os arquivos de log
    File[] files = FileTools.getFiles(logPath, new String[]{LOG_EXTENSION}, false);
    // apaga todos mais antigos que a data de refer�ncia
    for (int i=0; i<files.length; i++) {
      File file = files[i];
      Date fileDate = new Date(file.lastModified());
      if (fileDate.before(referenceDate))
        file.delete();
    } // for
  }

  /**
   * Escreve 'message' no arquivo de log identificado por 'log' e retorna
   * true em caso de sucesso.
   * @param logId String Identifica��o do log.
   * @param message String Mensagem de log.
   * @return boolean Escreve 'message' no arquivo de log identificado por 'log'
   *                 e retorna true em caso de sucesso.
   */
  public boolean write(String logId,
                       String message) {
    try {
      // data e hora da cria��o do arquivo de log
      String[] dateParts = DateTools.splitDateTime(date);
      String dateTime = dateParts[2] + dateParts[1] + dateParts[0];
      // arquivo de log
      File logFile = new File(logPath + logId + "_" + dateTime + LOG_EXTENSION);
      // escreve
      write(logFile, message);
      // se chegou at� aqui...retorna OK
      return false;
    }
    catch (Exception e) {
      // n�o podemos fazer nada
      return false;
    } // try-catch
  }

  /**
   * Escreve 'message' em 'logFile' garantindo que s� existir� um acesso por
   * vez ao arquivo informado.
   * @param logFile File onde a mensagem ser� adicionada.
   * @param message String Mensagem de log.
   * @throws Exception Em caso de exce��o na tentativa de acesso ao arquivo
   *                   informado.
   */
  static synchronized private void write(File   logFile,
                                         String message) throws Exception {
    // cria o diret�rio onde o arquivo ser� criado
    File directory = new File(logFile.getParent());
    directory.mkdirs();
    // se o arquivo n�o existe...cria
    if (!logFile.exists())
      logFile.createNewFile();
    // abre o arquivo para escrita
    FileWriter fileWriter = new FileWriter(logFile, true);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    // escreve a data e hora atuais
    bufferedWriter.write(DateTools.formatDateTimePrecision(new Date()) + " -> ");
    // escreve a mensagem
    bufferedWriter.write(message);
    // adiciona quebra de linha
    bufferedWriter.newLine();
    // fecha o arquivo
    bufferedWriter.close();
    fileWriter.close();
  }

}
