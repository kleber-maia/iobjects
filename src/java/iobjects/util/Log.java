package iobjects.util;

import java.io.*;
import java.util.*;

import iobjects.util.*;

/**
 * Utilitário para geraçação de arquivos de log para a aplicação.
 * @since 2006
 */
public class Log {

  static private final String LOG_EXTENSION = ".log";

  private Date   date    = null;
  private String logPath = "";

  /**
   * Construtor padrão.
   * @param logPath String Caminho onde os arquivos de log são gerados.
   */
  public Log(String logPath) {
    // guarda a data e hora atuais
    date = new Date();
    // guarda nosso path
    this.logPath = FileTools.includeSeparatorChar(logPath);
  }

  /**
   * Limpa o diretório de logs apagando todos os arquivos mais antigos que
   * a quantidade de meses indicados. A data de referência é a data atual
   * do sistema subtraídos a quantidade de meses indicados.
   * @param monthsToMaintain int Quantidade de meses cujos logs devem ser
   *                             mantidos contando com o mês atual. Ex.:
   *                             2 indica que todos os arquivos de log do mês
   *                             atual e do mês anterior serão mantidos.
   */
  public void clean(int monthsToMaintain) {
    // data de referência
    Date referenceDate = DateTools.getActualDate();
         referenceDate = DateTools.getCalculatedMonths(referenceDate, monthsToMaintain * -1);
    // obtém os arquivos de log
    File[] files = FileTools.getFiles(logPath, new String[]{LOG_EXTENSION}, false);
    // apaga todos mais antigos que a data de referência
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
   * @param logId String Identificação do log.
   * @param message String Mensagem de log.
   * @return boolean Escreve 'message' no arquivo de log identificado por 'log'
   *                 e retorna true em caso de sucesso.
   */
  public boolean write(String logId,
                       String message) {
    try {
      // data e hora da criação do arquivo de log
      String[] dateParts = DateTools.splitDateTime(date);
      String dateTime = dateParts[2] + dateParts[1] + dateParts[0];
      // arquivo de log
      File logFile = new File(logPath + logId + "_" + dateTime + LOG_EXTENSION);
      // escreve
      write(logFile, message);
      // se chegou até aqui...retorna OK
      return false;
    }
    catch (Exception e) {
      // não podemos fazer nada
      return false;
    } // try-catch
  }

  /**
   * Escreve 'message' em 'logFile' garantindo que só existirá um acesso por
   * vez ao arquivo informado.
   * @param logFile File onde a mensagem será adicionada.
   * @param message String Mensagem de log.
   * @throws Exception Em caso de exceção na tentativa de acesso ao arquivo
   *                   informado.
   */
  static synchronized private void write(File   logFile,
                                         String message) throws Exception {
    // cria o diretório onde o arquivo será criado
    File directory = new File(logFile.getParent());
    directory.mkdirs();
    // se o arquivo não existe...cria
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
