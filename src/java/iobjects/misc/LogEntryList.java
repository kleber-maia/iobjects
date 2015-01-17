package iobjects.misc;

import java.io.*;
import java.util.*;

import iobjects.util.*;

/**
 * Representa uma lista de entradas de log com capacidade de extrair
 * estatísticas.
 */
public class LogEntryList implements Comparator {

  /**
   * Define a ordenação da lista de entradas por defaultConnectinName e userName.
   */
  static public final int ORDER_BY_USER_NAME = 0;
  /**
   * Define a ordenação da lista de entradas por dateTime.
   */
  static public final int ORDER_BY_DATE_TIME = 1;
  /**
   * Define a ordenação da lista de entradas por originalDateTime.
   */
  static public final int ORDER_BY_ORIGINAL_DATE_TIME = 2;

  private Date   date         = new Date();
  private String logLocalPath = "";
  private int    orderBy      = -1;
  private Vector vector       = new Vector();

  /**
   * Construtor padrão.
   * @param logLocalPath String Caminho local dos arquivos de log.
   * @param date Date Data dos logs para carregar.
   */
  public LogEntryList(String logLocalPath,
                      Date   date) {
    // nossos dados
    this.logLocalPath = FileTools.includeSeparatorChar(logLocalPath);
    this.date = date;
    // vasculha os arquivos de log
    scanLogFiles();
  }

  /**
   * Compara os dois objetos recebidos, que devem ser instâncias de LogEntry,
   * de acordo com a ordem definida pelo método getLogEntryList().
   * @param o1 Object
   * @param o2 Object
   * @return int Compara os dois objetos recebidos, que devem ser instâncias de
   *         LogEntry, de acordo com a ordem definida pelo método
   *         getLogEntryList().
   */
  public int compare(Object o1, Object o2) {
    // entradas para comparar
    LogEntry logEntry1 = (LogEntry)o1;
    LogEntry logEntry2 = (LogEntry)o2;
    // se devemos ordenar por usuário
    if (orderBy == ORDER_BY_USER_NAME) {
      // se os nomes das conexões não são iguais...retorna sua comparação
      if (!logEntry1.getDefaultConnectionName().equals(logEntry2.getDefaultConnectionName()))
        return logEntry1.getDefaultConnectionName().compareTo(logEntry2.getDefaultConnectionName());
      // se são iguais...retorna a comparação dos nomes dos usuários
      else
        return logEntry1.getUserName().compareTo(logEntry2.getUserName());
    }
    // se devemos ordenar por data e hora
    else if (orderBy == ORDER_BY_DATE_TIME) {
      // se as datas não são iguais...retorna sua comparação
      if (!logEntry1.getDateTime().equals(logEntry2.getDateTime()))
        return logEntry1.getDateTime().compareTo(logEntry2.getDateTime());
      // se são iguais...retorna a comparação dos eventos
      else
        return logEntry1.getEvent() - logEntry2.getEvent();
    }
    // se devemos ordenar por data e hora originais
    else if (orderBy == ORDER_BY_ORIGINAL_DATE_TIME) {
      // se as datas não são iguais...retorna sua comparação
      if (!logEntry1.getOriginalDateTime().equals(logEntry2.getOriginalDateTime()))
        return logEntry1.getOriginalDateTime().compareTo(logEntry2.getOriginalDateTime());
      // se são iguais...retorna a comparação dos eventos
      else
        return logEntry1.getEvent() - logEntry2.getEvent();
    }
    // se é outra forma de ordenação...retorna que são iguais
    else {
      return 0;
    } // if
  }

  /**
   * Retorna um LogEntry[] contendo a lista de entrada de logs encontrados.
   * @param orderBy int Opção de ordenação da lista retornada.
   * @return LogEntry[] Retorna um LogEntry[] contendo a lista de entrada de
   *         logs encontrados.
   */
  public LogEntry[] getLogEntryList(int orderBy) {
    // nosso resultado
    LogEntry[] result = new LogEntry[vector.size()];
    // copia do vetor para o resultado
    vector.copyInto(result);
    // ordena
    this.orderBy = orderBy;
    Arrays.sort(result, this);
    // retorna
    return result;
  }

  /**
   * Vasculha os arquivos de log e carrega suas entradas. Os dados de cada
   * entrada são processados de forma a privilegiar a exibição do gráfico
   * de atividade por hora. Os eventos de logoff são atrasados em uma hora
   * para que o gráfico possa exibir aproximadamente a quantidade de usuário
   * que estavam conectados no sistema a cada hora.
   */
  private void scanLogFiles() {
    // data dos logs no formato aaaammdd
    String[]     dateParts    = DateTools.splitDate(date);
    final String logFilesDate = dateParts[2] + dateParts[1] + dateParts[0];

    // filtro para retornar apenas os arquivos do dia desejado
    FilenameFilter filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.contains(logFilesDate);
      }
    };

    // obtém os arquivos de log da data definida
    File[] logFiles = FileTools.getFiles(logLocalPath, new String[]{".log"}, filter, false, FileTools.ORDER_BY_NAME);
    // loop nos arquivos
    for (int i=0; i<logFiles.length; i++) {
      // nome do arquivo da vez
      String logFileName = logFiles[i].getName();
      // se não é um arquivo de usuário...continua
      if (!logFileName.startsWith("user_"))
        continue;
      // carrega o arquivo de log
      String[] logFileLines          = {};
      String   defaultConnectionName = "";
      String   userName              = "";
      try {
        // carrega o arquivo
        logFileLines = FileTools.loadTextFile(logLocalPath + logFileName);
        // dados do nome do arquivo
        String temp = logFileName;
        temp = temp.substring(temp.indexOf('_')+1);
        // nome da conexão default
        defaultConnectionName = temp.substring(0, temp.indexOf('_'));
        temp = temp.substring(temp.indexOf('_')+1);
        // se o próximo dado é a data...não temos default connection
        if (temp.startsWith(logFilesDate)) {
          userName = defaultConnectionName;
          defaultConnectionName = "";
        }
        // se não...pega o usuário
        else {
          userName = temp.substring(0, temp.indexOf('_'));
        } // if
      }
      catch (Exception e) {
      } // try-catch
      // loop nas linhas de log
      for (int w=0; w<logFileLines.length; w++) {
        // linha da vez
        String logFileLine = logFileLines[w];
        // verifica o tipo de evento conhecido ou continua
        int event = -1;
        if (logFileLine.indexOf("Logon") > 0)
          event = LogEntry.EVENT_LOGON;
        else if (logFileLine.indexOf("Logoff") > 0)
          event = LogEntry.EVENT_LOGOFF;
        else if (logFileLine.indexOf("Exceção") > 0)
          event = LogEntry.EVENT_EXCEPTION;
        else
          continue;
        // data e hora originais
        String strOriginalDateTime = logFileLine.substring(0, logFileLine.indexOf("."));
        Date originalDateTime = DateTools.parseDateTime(strOriginalDateTime.substring(0, strOriginalDateTime.lastIndexOf(":")));
        // obtém a data e hora, desprezando minutos, segundos e milisegundos
        String strDateTime = logFileLine.substring(0, logFileLine.indexOf(":")) + ":00";
        Date dateTime = new Date(DateTools.parseDateTime(strDateTime).getTime());
        // se o evento é um Logoff...põe a próxima hora
        if (event == LogEntry.EVENT_LOGOFF) {
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(dateTime);
          calendar.add(Calendar.HOUR_OF_DAY, 1);
          dateTime = calendar.getTime();
        } // if
        // obtém a mensagem
        String message = logFileLine.substring(logFileLine.indexOf("->") + 2).trim();
        // adiciona na nossa lista
        vector.add(new LogEntry(defaultConnectionName,
                                userName,
                                dateTime,
                                originalDateTime,
                                event,
                                message));
      } // for w
    } // for i
  }

}
