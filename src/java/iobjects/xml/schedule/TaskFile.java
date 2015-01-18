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
package iobjects.xml.schedule;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * Responsável pela leitura dos arquivos XML de parâmetros de trabalho agendado.
 * <p>
 * Exemplo do formato do arquivo:
 * </p>
 * <p>
 * <pre>
 * &lt;task&gt;
 *   &lt;authentication&gt;
 *     &lt;login defaultconnectionname=""       username="Administrador" password="administrador" /&gt;
 *     &lt;login defaultconnectionname="sample" username="Administrador" password="administrador" /&gt;
 *   &lt;/authentication&gt;
 *   &lt;businessobjects&gt;
 *     &lt;scheduleable classname="package.ClassName1" /&gt;
 *     &lt;scheduleable classname="package.ClassName2" /&gt;
 *     &lt;scheduleable classname="package.ClassName3" /&gt;
 *   &lt;/businessobjects&gt;
 *   &lt;schedule reportstatus="false" reportaddress="falecom@softgroup.com.br"&gt;
 *     &lt;hourly active="false"&gt;
 *       &lt;hour value="10" /&gt;
 *       &lt;hour value="12" /&gt;
 *       &lt;hour value="14" /&gt;
 *     &lt;/hourly&gt;
 *     &lt;daily active="false" hour="12:00" /&gt;
 *     &lt;weekly active="false" hour="12:00" sunday="false" monday="false" tuesday="false" wednesday="false" thursday="false" friday="false" saturday="false" /&gt;
 *     &lt;monthly active="false" hour="12:00"&gt;
 *       &lt;day value="10" /&gt;
 *       &lt;day value="15" /&gt;
 *       &lt;day value="20" /&gt;
 *     &lt;/monthly&gt;
 *   &lt;/schedule&gt;
 * &lt;/task&gt;
 * </pre>
 * </p>
 */
public class TaskFile extends DefaultHandler {

  /**
   * Representa o elemento "authentication" no arquivo de conexão.
   */
  public class Authentication {
    private Vector vector = new Vector();
    public Authentication() {
    }
    public void add(Login login) {
      vector.add(login);
    }
    public Login item(int index) {
      return (Login)vector.elementAt(index);
    }
    public int size() {
      return vector.size();
    }
  }

  /**
   * Representa o elemento "businessobjects" no arquivo de conexão.
   */
  public class BusinessObjects {
    private Vector vector = new Vector();
    public BusinessObjects() {
    }
    public void add(String className) {
      vector.add(className);
    }
    public boolean contains(String className) {
      for (int i=0; i<vector.size(); i++)
        if (item(i).equals(className))
          return true;
      return false;
    }
    public String item(int index) {
      return (String)vector.elementAt(index);
    }
    public int size() {
      return vector.size();
    }
  }

  /**
   * Representa o elemento "daily" no arquivo de trabalho agendado.
   */
  public class Daily {
    private boolean active = false;
    private String  hour   = "";
    public Daily(boolean active, String hour) {
      this.active = active;
      this.hour  = hour;
    }
    public boolean getActive() {return active;}
    public String  getHour() {return hour;}
  }

  /**
   * Representa o elemento "login" no arquivo de trabalho agendado.
   */
  public class Login {
    private String defaultConnectionName = "";
    private String userName = "";
    private String password  = "";
    public Login(String defaultConnectionName, String userName, String password) {
      this.defaultConnectionName = defaultConnectionName;
      this.userName = userName;
      this.password  = password;
    }
    public String getDefaultConnectionName() {return defaultConnectionName;}
    public String getPassword() {return password;}
    public String getUserName() {return userName;}
  }

  /**
   * Representa o elemento "Hourly" no arquivo de trabalho agendado.
   */
  public class Hourly {
    private boolean active = false;
    private Vector  vector = new Vector();
    public Hourly(boolean active) {
      this.active = active;
    }
    public void addHour(int hour) {
      vector.add(hour + "");
    }
    public boolean containsHour(int hour) {
      for (int i=0; i<vector.size(); i++)
        if (getHour(i) == hour)
          return true;
      return false;
    }
    public boolean getActive() {
      return active;
    }
    public int getHour(int index) {
      return Integer.parseInt(vector.elementAt(index).toString());
    }
    public int size() {
      return vector.size();
    }
  }

  /**
   * Representa o elemento "Monthly" no arquivo de trabalho agendado.
   */
  public class Monthly {
    private boolean active = false;
    private String  hour   = "";
    private Vector  vector = new Vector();
    public Monthly(boolean active, String hour) {
      this.active = active;
      this.hour  = hour;
    }
    public void addDay(int day) {
      vector.add(day + "");
    }
    public boolean containsDay(int day) {
      for (int i=0; i<vector.size(); i++)
        if (getDay(i) == day)
          return true;
      return false;
    }
    public boolean getActive() {
      return active;
    }
    public int getDay(int index) {
      return Integer.parseInt(vector.elementAt(index).toString());
    }
    public String getHour() {
      return hour;
    }
    public int size() {
      return vector.size();
    }
  }

  /**
   * Representa o elemento "schedule" no arquivo de trabalho agendado.
   */
  public class Schedule {
    private Hourly  hourly        = null;
    private Daily   daily         = null;
    private Monthly monthly       = null;
    private boolean reportStatus  = false;
    private String  reportAddress = "";
    private Weekly  weekly        = null;
    public Schedule(boolean reportStatus,
                    String  reportAddress) {
      this.reportStatus = reportStatus;
      this.reportAddress = reportAddress;
    }
    public Hourly  getHourly()        {return hourly;}
    public Daily   getDaily()         {return daily;}
    public Monthly getMonthly()       {return monthly;}
    public String  getReportAddress() {return reportAddress;}
    public boolean getReportStatus()  {return reportStatus;}
    public Weekly  getWeekly()        {return weekly;}
    public void setHourly(Hourly hourly)   {this.hourly = hourly;}
    public void setDaily(Daily daily)      {this.daily = daily;}
    public void setMontly(Monthly monthly) {this.monthly = monthly;}
    public void setWeekly(Weekly weekly)   {this.weekly = weekly;}
  }

  /**
   * Representa o elemento "weekly" no arquivo de trabalho agendado.
   */
  public class Weekly {
    private boolean active    = false;
    private boolean friday    = false;
    private String  hour      = "";
    private boolean monday    = false;
    private boolean saturday  = false;
    private boolean sunday    = false;
    private boolean tuesday   = false;
    private boolean wednesday = false;
    private boolean thursday  = false;
    public Weekly(boolean active, String hour, boolean sunday, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday) {
      this.active    = active;
      this.hour      = hour;
      this.sunday    = sunday;
      this.monday    = monday;
      this.tuesday   = tuesday;
      this.wednesday = wednesday;
      this.thursday  = thursday;
      this.friday    = friday;
      this.saturday  = saturday;
    }
    public boolean getActive()    {return active;}
    public String  getHour()      {return hour;}
    public boolean getSunday()    {return sunday;}
    public boolean getMonday()    {return monday;}
    public boolean getTuesday()   {return tuesday;}
    public boolean getWednesday() {return wednesday;}
    public boolean getThursday()  {return thursday;}
    public boolean getFriday()    {return friday;}
    public boolean getSaturday()  {return saturday;}
  }

  private static final String ACTIVE                  = "active";
  private static final String AUTHENTICATION          = "authentication";
  private static final String BUSINESS_OBJECTS        = "businessobjects";
  private static final String CLASS_NAME              = "classname";
  private static final String DAILY                   = "daily";
  private static final String DAY                     = "day";
  private static final String DEFAULT_CONNECTION_NAME = "defaultconnectionname";
  private static final String FRIDAY                  = "friday";
  private static final String HOUR                    = "hour";
  private static final String HOURLY                  = "hourly";
  private static final String LOGIN                   = "login";
  private static final String MONDAY                  = "monday";
  private static final String MONTHLY                 = "monthly";
  private static final String PASSWORD                = "password";
  private static final String REPORT_ADDRESS          = "reportaddress";
  private static final String REPORT_STATUS           = "reportstatus";
  private static final String SATURDAY                = "saturday";
  private static final String SCHEDULE                = "schedule";
  private static final String SCHEDULEABLE            = "scheduleable";
  private static final String SUNDAY                  = "sunday";
  private static final String TUESDAY                 = "tuesday";
  private static final String THURSDAY                = "thursday";
  private static final String USERNAME                = "username";
  private static final String VALUE                   = "value";
  private static final String WEDNESDAY               = "wednesday";
  private static final String WEEKLY                  = "weekly";

  private Authentication  authentication  = null;
  private BusinessObjects businessObjects = null;
  private Schedule        schedule        = null;

  /**
   * Construtor padrão.
   * @param taskFileName Nome do arquivo de trabalho agendado.
   * @throws Exception Em caso de exceção na tentativa de abertura do arquivo
   *                   XML especificado ou de inicialização do parser.
   */
  public TaskFile(String taskFileName) throws Exception {
    // nossa fábrica de parsers
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    // não validaremos o documento
    parserFactory.setValidating(false);
    // sem suporte para XML namespaces
    parserFactory.setNamespaceAware(false);
    // cria o parser
    SAXParser saxParser = parserFactory.newSAXParser();
    // inicia a análise
    saxParser.parse(new File(taskFileName), this);
  }

  /**
   * Retorna um BusinessObjects contendo as informações do elemento
   * "businessobjects" do arquivo de configuração.
   * @return Retorna um BusinessObjects contendo as informações do elemento
   *         "businessobjects" do arquivo de configuração.
   */
  public BusinessObjects businessObjects() {
    return businessObjects;
  }

  /**
   * Retorna um Authentication contendo as informações do elemento
   * "authentication" do arquivo de configuração.
   * @return Retorna um Authentication contendo as informações do elemento
   *         "authentication" do arquivo de configuração.
   */
  public Authentication authentication() {
    return authentication;
  }

  /**
   * Retorna um Schedule contendo as informações do elemento
   * "schedule" do arquivo de configuração.
   * @return Retorna um Schedule contendo as informações do elemento
   *         "schedule" do arquivo de configuração.
   */
  public Schedule schedule() {
    return schedule;
  }

  public void startElement(java.lang.String uri,
                           java.lang.String localName,
                           java.lang.String qName,
                           Attributes       attributes) {
    // se encontramos o elemento "authentication"
    if (qName.equalsIgnoreCase(AUTHENTICATION))
      authentication = new Authentication();
    // se encontramos o elemento "login"
    else if (qName.equalsIgnoreCase(LOGIN))
      authentication.add(new Login(attributes.getValue(DEFAULT_CONNECTION_NAME),
                                   attributes.getValue(USERNAME),
                                   attributes.getValue(PASSWORD)));
    // se encontramos o elemento "businessobjects"
    else if (qName.equalsIgnoreCase(BUSINESS_OBJECTS))
      businessObjects = new BusinessObjects();
    // se encontramos o elemento "scheduleable"
    else if (qName.equalsIgnoreCase(SCHEDULEABLE))
      businessObjects.add(attributes.getValue(CLASS_NAME));
    // se encontramos o elemento "schedule"
    else if (qName.equalsIgnoreCase(SCHEDULE))
      schedule = new Schedule(Boolean.valueOf(attributes.getValue(REPORT_STATUS)).booleanValue(),
                              attributes.getValue(REPORT_ADDRESS));
    // se encontramos o elemento "hourly"
    else if (qName.equalsIgnoreCase(HOURLY))
      schedule.setHourly(new Hourly(Boolean.valueOf(attributes.getValue(ACTIVE)).booleanValue()));
    // se encontramos o elemento "daily"
    else if (qName.equalsIgnoreCase(DAILY))
      schedule.setDaily(new Daily(Boolean.valueOf(attributes.getValue(ACTIVE)).booleanValue(),
                                  attributes.getValue(HOUR)));
    // se encontramos o elemento "weekly"
    else if (qName.equalsIgnoreCase(WEEKLY))
      schedule.setWeekly(new Weekly(Boolean.valueOf(attributes.getValue(ACTIVE)).booleanValue(),
                                    attributes.getValue(HOUR),
                                    Boolean.valueOf(attributes.getValue(SUNDAY)).booleanValue(),
                                    Boolean.valueOf(attributes.getValue(MONDAY)).booleanValue(),
                                    Boolean.valueOf(attributes.getValue(TUESDAY)).booleanValue(),
                                    Boolean.valueOf(attributes.getValue(WEDNESDAY)).booleanValue(),
                                    Boolean.valueOf(attributes.getValue(THURSDAY)).booleanValue(),
                                    Boolean.valueOf(attributes.getValue(FRIDAY)).booleanValue(),
                                    Boolean.valueOf(attributes.getValue(SATURDAY)).booleanValue()));
    // se encontramos o elemento "monthly"
    else if (qName.equalsIgnoreCase(MONTHLY))
      schedule.setMontly(new Monthly(Boolean.valueOf(attributes.getValue(ACTIVE)).booleanValue(),
                                     attributes.getValue(HOUR)));
    // se encontramos o elemento "day"
    else if (qName.equalsIgnoreCase(DAY))
      schedule.getMonthly().addDay(Integer.parseInt(attributes.getValue(VALUE)));
    // se encontramos o elemento "hour"
    else if (qName.equalsIgnoreCase(HOUR))
      schedule.getHourly().addHour(Integer.parseInt(attributes.getValue(VALUE)));
  }

}
