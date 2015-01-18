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
package iobjects.util;

import java.util.*;
import java.util.regex.*;
import java.sql.Timestamp;

import iobjects.*;

/**
 * Classe utilitária para manipulação de data.
 */
public class DateTools {

  static public final Timestamp ZERO_DATE                = Timestamp.valueOf("1970-01-01 00:00:00.000000000");
  static public final Pattern   DATE_PATTERN             = Pattern.compile("[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]");
  static public final Pattern   DATE_TIME_PATTERN        = Pattern.compile("[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9] [0-9][0-9]:[0-9][0-9]");
  static public final Pattern   DATE_NATIVE_PATTERN      = Pattern.compile("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]");
  static public final Pattern   DATE_TIME_NATIVE_PATTERN = Pattern.compile("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9] [0-9][0-9]:[0-9][0-9]");
  static public final Pattern   MONTH_YEAR_PATTERN       = Pattern.compile("[0-9][0-9]/[0-9][0-9][0-9][0-9]");

  static public final String[] MONTH_ARRAY       = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
  static public final String[] SHORT_MONTH_ARRAY = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
  static public final String[] DAY_ARRAY         = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
  static public final String[] SHORT_DAY_ARRAY   = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};

  static public void main(String[] args) {
    System.out.println(
            DateTools.getSubtractedMonths(
                  DateTools.parseDate("09/05/2011"), 
                  DateTools.parseDate("25/05/2011")));
  }
  
  /**
   * Retorna 'date' no formato dd/mm/yyyy.
   * @param date Date Data para ser formatada.
   * @return Retorna 'date' no formato dd/mm/yyyy.
   */
  static public String formatDate(Date date) {
    if (date == null)
      return "";
    else if (date.getTime() == ZERO_DATE.getTime())
      return "";
    String[] parts = splitDate(date);
    return parts[0]  + "/" + parts[1] + "/" + parts[2];
  }

  /**
   * Retorna 'date' no formato Dia, dd.
   * @param date Date Data para ser formatada.
   * @param shortName True para que o nome do dia seja abreviado.
   * @param includeDay True para que o número do dia seja incluído
   * @return Retorna 'date' no formato dd/mm/yyyy.
   */
  static public String formatDay(Date date, boolean shortName, boolean includeDay) {
    if (date == null)
      return "";
    else if (date.getTime() == ZERO_DATE.getTime())
      return "";
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    String day = (shortName ? SHORT_DAY_ARRAY[calendar.get(Calendar.DAY_OF_WEEK)-1] : DAY_ARRAY[calendar.get(Calendar.DAY_OF_WEEK)-1]);
    if (includeDay)
      return day + ", " + NumberTools.completeZero(calendar.get(Calendar.DAY_OF_MONTH), 2);
    else
      return day;
  }

  /**
   * Retorna 'timestamp' no formato dd/mm/yyyy.
   * @param timestamp Timestamp Data para ser formatada.
   * @return Retorna 'timestamp' no formato dd/mm/yyyy.
   */
  static public String formatDate(Timestamp timestamp) {
    return formatDate(new Date(timestamp.getTime()));
  }

  /**
   * Retorna 'date' no formato yyyy-mm-dd.
   * @param date Date Data para ser formatada.
   * @return Retorna 'date' no formato yyyy-mm-dd.
   * @since 2006
   */
  static public String formatDateNative(Date date) {
    if (date == null)
      return "";
    String[] parts = splitDate(date);
    return parts[2]  + "-" + parts[1] + "-" + parts[0];
  }

  /**
   * Retorna 'date' informando quantos dias faltam ou se passaram em um formato
   * amigável.
   * @param date Data para ser formatada.
   * @return Retorna 'date' informando quantos dias faltam ou se passaram em um
   *         formato amigável.
   */
  static public String formatDateRelative(Date date) {
    int dif = (int)((getActualDate().getTime() - getOnlyDate(date).getTime()) / (1000 * 60 * 60 * 24));
    if (dif == -1)
      return "Amanhã";
    else if (dif == 0)
      return "Hoje";
    else if (dif == 1)
      return "Ontem";
    else if ((dif >= 2) && (dif <= 15))
      return dif + " dias atrás";
    else 
      return formatDate(date);
  }

  /**
   * Retorna 'timestamp' informando quantos dias se passaram até hoje ou no formato
   * dd/mm/yyyy se já se passaram mais de 15 dias.
   * @param timestamp Data para ser formatada.
   * @return Retorna 'timestamp' informando quantos dias se passaram até hoje ou no 
   *         formato dd/mm/yyyy se já se passaram mais de 15 dias.
   */
  static public String formatDateRelative(Timestamp timestamp) {
    return formatDateRelative(new Date(timestamp.getTime()));
  }

  /**
   * Retorna 'date' no formato dd/mm/yyyy hh:mm.
   * @param date Date Data para ser formatada.
   * @return Retorna 'date' no formato dd/mm/yyyy hh:mm.
   * @since 2006
   */
  static public String formatDateTime(Date date) {
    if (date == null)
      return "";
    else if (date.getTime() == ZERO_DATE.getTime())
      return "";
    String[] parts = splitDateTime(date);
    return parts[0]  + "/" + parts[1] + "/" + parts[2]
           + " " + parts[3] + ":" + parts[4];
  }

  /**
   * Retorna 'timestamp' no formato dd/mm/yyyy hh:mm.
   * @param timestamp Timestamp Data para ser formatada.
   * @return Retorna 'timestamp' no formato dd/mm/yyyy hh:mm.
   */
  static public String formatDateTime(Timestamp timestamp) {
    return formatDateTime(new Date(timestamp.getTime()));
  }

  /**
   * Retorna 'date' no formato yyyy-mm-dd hh:mm.
   * @param date Date Data para ser formatada.
   * @return Retorna 'date' no formato yyyy-mm-dd hh:mm.
   * @since 2006
   */
  static public String formatDateTimeNative(Date date) {
    if (date == null)
      return "";
    String[] parts = splitDateTime(date);
    return parts[2]  + "-" + parts[1] + "-" + parts[0]
           + " " + parts[3] + ":" + parts[4];
  }

  /**
   * Retorna 'date' no formato dd/mm/yyyy hh:mm:ss.ms.
   * @param date Date Data para ser formatada.
   * @return Retorna 'date' no formato dd/mm/yyyy hh:mm:ss.ms.
   * @since 2006
   */
  static public String formatDateTimePrecision(Date date) {
    // define a data e hora recebidos em um calendário
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    // retorna
    return       NumberTools.completeZero(calendar.get(Calendar.DAY_OF_MONTH), 2)
         + "/" + NumberTools.completeZero(calendar.get(Calendar.MONTH)+1, 2)
         + "/" + NumberTools.completeZero(calendar.get(Calendar.YEAR), 2)
         + " " + NumberTools.completeZero(calendar.get(Calendar.HOUR_OF_DAY), 2)
         + ":" + NumberTools.completeZero(calendar.get(Calendar.MINUTE), 2)
         + ":" + NumberTools.completeZero(calendar.get(Calendar.SECOND), 2)
         + "." + calendar.get(Calendar.MILLISECOND);
  }

  /**
   * Retorna 'timestamp' no formato dd/mm/yyyy hh:mm:ss.ms.
   * @param timestamp Timestamp Data para ser formatada.
   * @return Retorna 'timestamp' no formato dd/mm/yyyy hh:mm:ss.ms.
   * @since 2006
   */
  static public String formatDateTimePrecision(Timestamp timestamp) {
    return formatDateTimePrecision(new Date(timestamp.getTime()));
  }

  /**
   * Retorna 'date' no formato Mês.
   * @param date Date Data para ser formatada.
   * @param shortName True para que o nome do dia seja abreviado.
   * @param includeDay True para que o número do dia seja incluído
   * @return Retorna 'date' no formato dd/mm/yyyy.
   */
  static public String formatMonth(Date date, boolean shortName) {
    if (date == null)
      return "";
    else if (date.getTime() == ZERO_DATE.getTime())
      return "";
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    String month = (shortName ? SHORT_MONTH_ARRAY[calendar.get(Calendar.MONTH)] : MONTH_ARRAY[calendar.get(Calendar.MONTH)]);
    return month;
  }

  /**
   * Retorna 'date' no formato mm/yyyy.
   * @param date Date Data para formatar.
   * @return Retorna 'date' no formato mm/yyyy.
   * @since 2006
   */
  static public String formatMonthYear(Date date) {
    if (date == null)
      return "";
    else if (date.getTime() == ZERO_DATE.getTime())
      return "";
    String[] parts = splitDate(date);
    return parts[1] + "/" + parts[2];
  }

  /**
   * Retorna 'timestamp' no formato mm/yyyy.
   * @param timestamp Timestamp Data para formatar.
   * @return Retorna 'timestamp' no formato mm/yyyy.
   */
  static public String formatMonthYear(Timestamp timestamp) {
    return formatMonthYear(new Date(timestamp.getTime()));
  }

  /**
   * Retorna 'date' no formato hh:mm.
   * @param date Date Data para formatar.
   * @return Retorna 'date' no formato hh:mm.
   * @since 2006
   */
  static public String formatTime(Date date) {
    if (date == null)
      return "";
    else if (date.getTime() == ZERO_DATE.getTime())
      return "";
    String[] parts = splitDateTime(date);
    return parts[3]  + ":" + parts[4];
  }

  /**
   * Retorna 'timestamp' no formato hh:mm.
   * @param timestamp Timestamp Data para formatar.
   * @return Retorna 'timestamp' no formato hh:mm.
   */
  static public String formatTime(Timestamp timestamp) {
    return formatTime(new Date(timestamp.getTime()));
  }

  /**
   * Retorna o valor correspondente a 'field' da data atual do sistema.
   * @param field Valor que se deseja retorna da data atual do sistema. As
   *              constantes reconhecidas são as de java.util.Calendar.
   * @return Retorna o valor correspondente a 'field' da data atual do sistema.
   * @see java.util.Calendar
   * @since 2006
   */
  static public int getActual(int field) {
    java.util.Date now = new java.util.Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(now);
    return calendar.get(field);
  }

  /**
   * Retorna a data atual, com hora zero.
   * @return Date Retorna a data atual, com hora zero.
   * @since 2006
   */
  static public Date getActualDate() {
    java.util.Date now = new java.util.Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(now);
    calendar.set(Calendar.AM_PM, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  /**
   * Retorna a data e hora atuais.
   * @return Date Retorna a data e hora atuais.
   * @since 2006 R1
   */
  static public Date getActualDateTime() {
    return new java.util.Date();
  }

  /**
   * Retorna o dia do mês da data atual do sistema.
   * @return Retorna o dia do mês da data atual do sistema.
   * @since 2006
   */
  static public int getActualDayOfMonth() {
    return getActual(Calendar.DAY_OF_MONTH);
  }

  /**
   * Retorna o dia da semana da data atual do sistema.
   * @return Retorna o dia da semana da data atual do sistema.
   * @since 2006
   */
  static public int getActualDayOfWeek() {
    return getActual(Calendar.DAY_OF_WEEK);
  }

  /**
   * Retorna o dia do ano da data atual do sistema.
   * @return Retorna o dia atual do ano da data atual do sistema.
   * @since 2006
   */
  static public int getActualDayOfYear() {
    return getActual(Calendar.DAY_OF_YEAR);
  }

  /**
   * Retorna o mês da data atual do sistema.
   * @return Retorna o mês da data atual do sistema.
   * @since 2006
   */
  static public int getActualMonth() {
    return getActual(Calendar.MONTH)+1;
  }

  /**
   * Retorna o ano da data atual do sistema.
   * @return Retorna o ano da data atual do sistema.
   * @since 2006
   */
  static public int getActualYear() {
    return getActual(Calendar.YEAR);
  }

  /**
   * Retorna a data calculada a partir da adição ou subtração de 'amount' ao
   * 'field' da data atual do sistema.
   * @param field Valor que se deseja calcular a partir da data atual do sistema. As
   *              constantes reconhecidas são as de java.util.Calendar.
   * @param amount Quantidade para ser somada ou subtraída do valor informado.
   * @return Date Retorna a data calculada a partir da adição ou subtração de
   *              'amount' ao 'field' da data atual do sistema.
   * @since 2006
   */
  static public Date getCalculatedDate(int field, int amount) {
    return getCalculatedDate(getActualDate(), field, amount);
  }

  /**
   * Retorna a data calculada a partir da adição ou subtração de 'amount' ao
   * 'field' da data referenciada por 'date'.
   * @param date Data de referência.
   * @param field Valor que se deseja calcular a partir da data atual do sistema. As
   *              constantes reconhecidas são as de java.util.Calendar.
   * @param amount Quantidade para ser somada ou subtraída do valor informado.
   * @return Date Retorna a data calculada a partir da adição ou subtração de
   *              'amount' ao 'field' da data referenciada por 'date'.
   * @since 2006
   */
  static public Date getCalculatedDate(Date date, int field, int amount) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(field, amount);
    return calendar.getTime();
  }

  /**
   * Retorna a data calculada a partir da adição ou subtração de 'days' à
   * data atual do sistema.
   * @param days int Quantidade de dias para ser somada ou subtraída
   *             à data atual do sistema.
   * @return Date Retorna a data calculada a partir da adição ou subtração de
   *              'days' à data atual do sistema.
   * @since 2006
   */
  static public Date getCalculatedDays(int days) {
    return getCalculatedDate(Calendar.DATE, days);
  }

  /**
   * Retorna a data calculada a partir da adição ou subtração de 'days' à
   * data referenciada por 'date'.
   * @param date Data de referência.
   * @param days int Quantidade de dias para ser somada ou subtraída
   *             à data atual do sistema.
   * @return Date Retorna a data calculada a partir da adição ou subtração de
   *              'days' à data referenciada por 'date'.
   * @since 2006
   */
  static public Date getCalculatedDays(Date date, int days) {
    return getCalculatedDate(date, Calendar.DATE, days);
  }

  /**
   * Retorna a data calculada a partir da adição ou subtração de 'months' à
   * data atual do sistema.
   * @param months int Quantidade de meses para ser somada ou subtraída
   *               à data atual do sistema.
   * @return Date Retorna a data calculada a partir da adição ou subtração de
   *              'months' à data atual do sistema.
   * @since 2006
   */
  static public Date getCalculatedMonths(int months) {
    return getCalculatedDate(Calendar.MONTH, months);
  }

  /**
   * Retorna a data calculada a partir da adição ou subtração de 'months' à
   * data referenciada por 'date'.
   * @param date Data de referência.
   * @param months int Quantidade de meses para ser somada ou subtraída
   *               à data atual do sistema.
   * @return Date Retorna a data calculada a partir da adição ou subtração de
   *              'months' à data referenciada por 'date'.
   * @since 2006
   */
  static public Date getCalculatedMonths(Date date, int months) {
    return getCalculatedDate(date, Calendar.MONTH, months);
  }

  /**
   * Retorna a data calculada a partir da adição ou subtração de 'weeks' à
   * data atual do sistema.
   * @param weeks int Quantidade de semanas para ser somada ou subtraída
   *               à data atual do sistema.
   * @return Date Retorna a data calculada a partir da adição ou subtração de
   *              'weeks' à data atual do sistema.
   * @since 2006
   */
  static public Date getCalculatedWeeks(int weeks) {
    return getCalculatedDate(Calendar.WEEK_OF_YEAR, weeks);
  }

  /**
   * Retorna a data calculada a partir da adição ou subtração de 'weeks' à
   * data referenciada por 'date'.
   * @param date Data de referência.
   * @param weeks int Quantidade de semanas para ser somada ou subtraída
   *               à data atual do sistema.
   * @return Date Retorna a data calculada a partir da adição ou subtração de
   *              'weeks' à data referenciada por 'date'.
   * @since 2006
   */
  static public Date getCalculatedWeeks(Date date, int weeks) {
    return getCalculatedDate(date, Calendar.WEEK_OF_YEAR, weeks);
  }

  /**
   * Retorna o próximo dia útil calculado a partir de 'date'. Se a data informada
   * já for um dia útil, retorna ela mesma.
   * @param date Data de referência.
   * @return Retorna o próximo dia útil calculado a partir de 'date'. Se a data
   *         informada já for um dia útil, retorna ela mesma.
   */
  static public Date getNextBusinessDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    while ((day < Calendar.MONDAY) || (day > Calendar.FRIDAY)) {
      calendar.add(Calendar.DATE, 1);
      day = calendar.get(Calendar.DAY_OF_WEEK);
    } // while
    return calendar.getTime();
  }

  /**
   * Retorna 'date' contendo apenas com a porção dd/mm/yyyy, ou seja, zera
   * a porção hh:mm:ss.mmmmmmm.
   * @param date Data de referência.
   * @return Retorna 'date' contendo apenas com a porção dd/mm/yyyy, ou seja, 
   *         zera a porção hh:mm:ss.mmmmmmm.
   */
  static public Date getOnlyDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.AM_PM, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }
  
  /**
   * Retorna o valor da operação 'endDate - beginDate' em dias. Se as datas
   * informadas contiverem horas, essas serão desprezadas.
   * @param beginDate Date Data inicial.
   * @param endDate Date Data final.
   * @return int Retorna o valor da operação 'endDate - beginDate' em dias. Se
   * as datas informadas contiverem horas essas serão desprezadas.
   * @since 2006
   */
  static public int getSubtractedDays(Date beginDate,
                                      Date endDate) {
    return Math.abs((int)((getOnlyDate(endDate).getTime() - getOnlyDate(beginDate).getTime()) / (1000 * 60 * 60 * 24)));
  }

  /**
   * Retorna o valor da operação 'endDate - beginDate' em horas. Se as datas
   * informadas não contiverem horas serão consideradas as zero horas de cada
   * data.
   * @param beginDate Date Data inicial.
   * @param endDate Date Data final.
   * @return int Retorna o valor da operação 'endDate - beginDate' em horas. Se
   *         as datas informadas não contiverem horas serão consideradas as zero
   *         horas de cada data.
   * @since 2006
   */
  static public int getSubtractedHours(Date beginDate,
                                       Date endDate) {
    return Math.abs((int)((endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60)));
  }

  /**
   * Retorna o valor da operação 'endDate - beginDate' em meses. Se as datas
   * informadas contiverem horas, essas serão desprezadas.
   * @param beginDate Date Data inicial.
   * @param endDate Date Data final.
   * @return int Retorna o valor da operação 'endDate - beginDate' em meses. Se
   * as datas informadas contiverem horas essas serão desprezadas.
   * @since 2006
   */
  static public int getSubtractedMonths(Date beginDate,
                                        Date endDate) {
    // precisamos apenas do mês e ano
    String[] beginParts = splitDate(beginDate);
    String[] endParts   = splitDate(endDate);
    Date begin = parseDate("01/" + beginParts[1] + "/" + beginParts[2]);
    Date end   = parseDate("01/" + endParts[1] + "/" + endParts[2]);
    // se é o mesmo mês...retorna
    if (begin.equals(end))
      return 0;
    // nosso calendário
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(begin);
    // qual a data menor?
    int fator = begin.before(end) ? 1 : -1;
    // conta os meses
    int result = 0;
    while (!calendar.getTime().equals(end)) {
      calendar.add(Calendar.MONTH, fator);
      result++;
    } // white
    return result;
  }

  /**
   * Retorna o valor recebido no formato dd/mm/yyyy e retorna como Timestamp.
   * @param value String Valor formatado para ser interpretado.
   * @return Retorna o valor recebido no formato dd/mm/yyyy e retorna como Timestamp.
   */
  static public Timestamp parseDate(String value) {
    if ((value == null) || (value.trim().equals("")))
      return ZERO_DATE;
    if (!DATE_PATTERN.matcher(value).matches())
      throw new RuntimeException("A data deve estar no formato: dd/mm/aaaa.");
    String day    = value.substring(0, 2);
    String month  = value.substring(3, 5);
    String year   = value.substring(6, 10);
    return Timestamp.valueOf(year + "-" + month + "-" + day + " 00:00:00.000000000");
  }

  /**
   * Retorna o valor recebido no formato yyyy-mm-dd e retorna como Timestamp.
   * @param value String Valor formatado para ser interpretado.
   * @return Retorna o valor recebido no formato yyyy-mm-dd e retorna como Timestamp.
   */
  static public Timestamp parseDateNative(String value) {
    if ((value == null) || (value.trim().equals("")))
      return ZERO_DATE;
    if (!DATE_NATIVE_PATTERN.matcher(value).matches())
      throw new RuntimeException("A data deve estar no formato: aaaa-mm-dd.");
    String day   = value.substring(8, 10);
    String month = value.substring(5, 7);
    String year  = value.substring(0, 4);
    return Timestamp.valueOf(year + "-" + month + "-" + day + " 00:00:00.000000000");
  }

  /**
   * Retorna o valor recebido no formato dd/mm/yyyy hh:mm e retorna como Timestamp.
   * @param value String Valor formatado para ser interpretado.
   * @return Retorna o valor recebido no formato dd/mm/yyyy hh:mm e retorna como Timestamp.
   */
  static public Timestamp parseDateTime(String value) {
    if ((value == null) || (value.trim().equals("")))
      return ZERO_DATE;
    if (!DATE_TIME_PATTERN.matcher(value).matches())
      throw new RuntimeException("A data/hora deve estar no formato: dd/mm/aaaa hh:mm.");
    String day    = value.substring(0, 2);
    String month  = value.substring(3, 5);
    String year   = value.substring(6, 10);
    String hour   = value.substring(11, 13);
    String minute = value.substring(14, 16);
    if (hour.equals("24")) {
      hour   = "23";
      minute = "59";
    } // if
    return Timestamp.valueOf(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00.000000000");
  }

  /**
   * Retorna o valor recebido no formato yyyy-mm-dd 00:00 e retorna como Timestamp.
   * @param value String Valor formatado para ser interpretado.
   * @return Retorna o valor recebido no formato yyyy-mm-dd 00:00 e retorna como Timestamp.
   */
  static public Timestamp parseDateTimeNative(String value) {
    if ((value == null) || (value.trim().equals("")))
      return ZERO_DATE;
    if (!DATE_TIME_NATIVE_PATTERN.matcher(value).matches())
      throw new RuntimeException("A data/hora deve estar no formato: aaaa-mm-dd hh:mm.");
    String day    = value.substring(8, 10);
    String month  = value.substring(5, 7);
    String year   = value.substring(0, 4);
    String hour   = value.substring(11, 13);
    String minute = value.substring(14, 16);
    return Timestamp.valueOf(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00.000000000");
  }

  /**
   * Retorna o valor recebido no formato mm/yyyy e retorna como Timestamp.
   * @param value String Valor formatado para ser interpretado.
   * @return Retorna o valor recebido no formato mm/yyyy e retorna como Timestamp.
   */
  static public Timestamp parseMonthYear(String value) {
    if ((value == null) || (value.trim().equals("")))
      return ZERO_DATE;
    if (!MONTH_YEAR_PATTERN.matcher(value).matches())
      throw new RuntimeException("O mês/ano deve estar no formato: mm/aaaa.");
    String month = value.substring(0, 2);
    String year  = value.substring(3, 7);
    return Timestamp.valueOf(year + "-" + month + "-01 00:00:00.000000000");
  }

  /**
   * Retorna o valor recebido no formato hh:mm e retorna como Timestamp.
   * @param value String Valor formatado para ser interpretado.
   * @return Retorna o valor recebido no formato hh:mm e retorna como Timestamp.
   */
  static public Timestamp parseTime(String value) {
    if ((value == null) || (value.trim().equals("")))
      return ZERO_DATE;
    int dots = value.indexOf(':');
    if (dots < 0)
      return ZERO_DATE;
    String hours   = value.substring(0, dots);
    if (hours.length() < 2)
      hours = "0" + hours;
    String minutes = value.substring(dots+1, value.length());
    if (minutes.length() < 2)
      minutes = "0" + minutes;
    // *
    String result  = ZERO_DATE.toString(); // 1970-01-01 00:00:00.0
    result = result.substring(0, result.indexOf(' '));
    return Timestamp.valueOf(result + " " + hours + ":" + minutes + ":00.000000000");
  }

  /**
   * Retorna 'date' em um String[] contendo: dia, mês, ano.
   * @param date Date Data que se deseja separar.
   * @return Retorna 'date' em um String[] contendo: dia, mês, ano.
   */
  static public String[] splitDate(Date date) {
    if (date == null)
      return new String[3];
    else if (date.getTime() == ZERO_DATE.getTime())
      return new String[3];
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day   = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH) + 1;
    int year  = calendar.get(Calendar.YEAR);
    String[] result = new String[3];
    result[0] = NumberTools.completeZero(day, 2);
    result[1] = NumberTools.completeZero(month, 2);
    result[2] = NumberTools.completeZero(year, 4);
    return result;
  }

  /**
   * Retorna 'timestamp' em um String[] contendo: dia, mês, ano.
   * @param timestamp Timestamp Data que se deseja separar.
   * @return Retorna 'timestamp' em um String[] contendo: dia, mês, ano.
   */
  static public String[] splitDate(Timestamp timestamp) {
    return splitDate(new Date(timestamp.getTime()));
  }

  /**
   * Retorna 'date' em um String[] contendo: dia, mês, ano, hora, minuto.
   * @param date Date Data que se deseja separar.
   * @return Retorna 'date' em um String[] contendo: dia, mês, ano, hora, minuto.
   */
  static public String[] splitDateTime(Date date) {
    if (date == null)
      return new String[5];
    else if (date.getTime() == ZERO_DATE.getTime())
      return new String[5];
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day    = calendar.get(Calendar.DAY_OF_MONTH);
    int month  = calendar.get(Calendar.MONTH) + 1;
    int year   = calendar.get(Calendar.YEAR);
    int hour   = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    String[] result = new String[5];
    result[0] = NumberTools.completeZero(day, 2);
    result[1] = NumberTools.completeZero(month, 2);
    result[2] = NumberTools.completeZero(year, 4);
    result[3] = NumberTools.completeZero(hour, 2);
    result[4] = NumberTools.completeZero(minute, 2);
    return result;
  }

  /**
   * Retorna 'timestamp' em um String[] contendo: dia, mês, ano, hora, minuto.
   * @param timestamp Timestamp Data que se deseja separar.
   * @return Retorna 'timestamp' em um String[] contendo: dia, mês, ano, hora, minuto.
   */
  static public String[] splitDateTime(Timestamp timestamp) {
    return splitDateTime(new Date(timestamp.getTime()));
  }

  /**
   * @param value String
   * @return String
   * @deprecated Veja parseDateTime().
   */
  static public Timestamp stringDateTimeToTimestamp(String value) {
    return parseDateTime(value);
  }

  /**
   * @param value String
   * @return String
   * @deprecated Veja parseDate().
   */
  static public Timestamp stringDateToTimestamp(String value) {
    return parseDate(value);
  }

  /**
   * @param value String
   * @return String
   * @deprecated Veja parseMonthYear().
   */
  static public Timestamp stringMonthYearToTimestamp(String value) {
    return parseMonthYear(value);
  }

  /**
   * @param date Date
   * @return String
   * @deprecated Veja formatTime().
   */
  static public String timeToString(Date date) {
    return formatTime(date);
  }

}
