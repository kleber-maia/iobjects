package iobjects;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import iobjects.card.*;
import iobjects.entity.*;
import iobjects.report.*;
import iobjects.security.*;
import iobjects.ui.*;
import iobjects.util.*;

/**
 * Representa uma Extens�o da aplica��o. Extens�es s�o componentes externos que
 * cont�m objetos de neg�cio e suas interfaces.
 */
public class Extension {

  static private final String LAST_MODIFIED_DATE = "lastModifiedDate";

  private String           cacheFilesPath           = "";
  private String           classFilesPath           = "";
  private String[]         classNames               = {};
  private int              cardCount                = 0;
  private String           demoServiceClassName     = "";
  private int              entityCount              = 0;
  private ExtensionManager extensionManager         = null;
  private String           fileName                 = "";
  private ImageList        imageList                = null;
  private String           name                     = "";
  private int              otherCount               = 0;
  private int              processCount             = 0;
  private String           registryClassName        = "";
  private int              reportCount              = 0;
  private String           securityServiceClassName = "";
  private String           url                      = "";

  /**
   * Construtor padr�o.
   * @param extensionManager ExtensionManager respons�vel pelo gerenciamento da
   *                         extens�o.
   * @param fileName String Nome do arquivo da extens�o.
   * @param cacheFilesPath Caminho local onde os arquivos da extens�o ser�o
   *        extra�dos.
   * @param classFilesPath Caminho local onde os arquivos de classes da extens�o
   *        ser�o copiados.
   * @param url Caminho relativo para ser usado como URL dos arquivos
   *            da extens�o.
   */
  public Extension(ExtensionManager extensionManager,
                   String           fileName,
                   String           cacheFilesPath,
                   String           classFilesPath,
                   String           url) {
    // nossos valores
    this.extensionManager = extensionManager;
    this.fileName = fileName;
    this.cacheFilesPath = cacheFilesPath;
    this.classFilesPath = classFilesPath;
    this.url = url;
    // nosso nome
    name = fileName.substring(fileName.lastIndexOf(File.separatorChar)+1);
    name = name.substring(0, name.indexOf('.'));
    try {
      // carrega a extens�o
      loadExtension();
      // cria nosso ImageList
      imageList = new ImageList(cacheFilesPath + Facade.IMAGES_PATH + File.separatorChar,
                                url + "/" + Facade.IMAGES_PATH);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna o nome da classe que imeplementa a interface DemoService.
   * Retorna "" caso nenhum objeto da extens�o implemente esta interface.
   * @return String Retorna o nome da classe que imeplementa a interface
   *         DemoService. Retorna "" caso nenhum objeto da extens�o
   *         implemente esta interface.
   */
  public String demoServiceClassName() {
    return demoServiceClassName;
  }
    
  /**
   * Libera recursos.
   */
  public void finalize() {
    cacheFilesPath           = null;
    classFilesPath           = null;
    classNames               = null;
    demoServiceClassName     = null;
    fileName                 = null;
    extensionManager         = null;
    imageList                = null;
    registryClassName        = null;
    securityServiceClassName = null;
    url                      = null;
  }

  /**
   * Retorna a quantidade de cart�es informativos existentes na extens�o.
   * @return int Retorna a quantidade de cart�es informativos existentes na extens�o.
   */
  public int getCardCount() {
    return cardCount;
  }

  /**
   * Retorna o caminho local onde os arquivos da extens�o ser�o extraidos.
   * @return String Retorna o caminho local onde os arquivos da extens�o ser�o extraidos.
   */
  public String getCacheFilesPath() {
    return cacheFilesPath;
  }

  /**
   * Retorna a lista dos nomes das classes descendentes de ProviderBean
   * encontradas na extens�o.
   * @return Retorna a lista dos nomes das classes descendentes de ProviderBean
   *         encontradas na extens�o.
   */
  public String[] getClassNames() {
    return classNames;
  }

  /**
   * Retorna o ExtensionManager respons�vel pelo gerenciamento do Extension.
   * @return Retorna o ExtensionManager respons�vel pelo gerenciamento do
   *         Extension.
   */
  public ExtensionManager getExtensionManager() {
    return extensionManager;
  }

  /**
   * Retorna o nome do arquivo da Extens�o.
   * @return Retorna o nome do arquivo da Extens�o.
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Retorna a quantidade de entidades existentes na extens�o.
   * @return int Retorna a quantidade de entidades existentes na extens�o.
   */
  public int getEntityCount() {
    return entityCount;
  }

  public String getName() {
    return name;
  }

  /**
   * Retorna a quantidade de outras classes existentes na extens�o.
   * @return int Retorna a quantidade de outras classes existentes na extens�o.
   */
  public int getOtherCount() {
    return otherCount;
  }

  /**
   * Retorna a quantidade de processos existentes na extens�o.
   * @return int Retorna a quantidade de processos existentes na extens�o.
   */
  public int getProcessCount() {
    return processCount;
  }

  /**
   * Retorna a quantidade de relat�rios existentes na extens�o.
   * @return int Retorna a quantidade de relat�rios existentes na extens�o.
   */
  public int getReportCount() {
    return reportCount;
  }

  /**
   * Retorna a inst�ncia de ImageList que mant�m as imagens da extens�o.
   * @return ImageList Retorna a inst�ncia de ImageList que mant�m as imagens
   *         da extens�o.
   */
  public ImageList imageList() {
    return imageList;
  }

  /**
   * Inicializa a extens�o.
   * @throws Exception Em caso de exce��o na tentativa de inicializar a extens�o
   *                   ou suas classes.
   */
  public void initialize() throws Exception {
    // verifica todas as classes e mant�m na lista apenas
    // as descendentes de BusinessObject
    Vector vectorClasses = new Vector();
    Vector vectorSystemListeners = new Vector();
    // classe dos objetos de neg�cio
    Class businessObjectClass = new BusinessObject().getClass();
    for (int i=0; i<classNames.length; i++) {
      // nome da classe da vez
      String className = classNames[i];
      // carrega a classe da vez
      Class objectClass = Class.forName(className);
      // se � descendente de BusinessObject...
      if (businessObjectClass.isAssignableFrom(objectClass)) {
        // adiciona na lista
        vectorClasses.add(className);
        // instancia
        BusinessObject businessObject = (BusinessObject)objectClass.newInstance();
        // incrementa os contadores de classes
        if (businessObject instanceof Card)
          cardCount++;
        else if (businessObject instanceof Entity)
          entityCount++;
        else if (businessObject instanceof iobjects.process.Process)
          processCount++;
        else if (businessObject instanceof Report)
          reportCount++;
        else
          otherCount++;
        // se implementa a interface Registry...
        if (businessObject instanceof Registry) {
          // se j� temos a classe do Registry...exce��o
          if (!registryClassName.equals(""))
            throw new ExtendedException(getClass().getName(), "loadExtension", "Mais de uma classe encontrada implementando o registro.");
          // se ainda n�o temos...guarda
          else
            registryClassName = className;
        } // if
        // se implementa a interface DemoService...
        if (businessObject instanceof DemoService) {
          // se j� temos a classe do DemoService...exce��o
          if (!demoServiceClassName.equals(""))
            throw new ExtendedException(getClass().getName(), "loadExtension", "Mais de uma classe encontrada implementando o servi�o de demonstra��o.");
          // se ainda n�o temos...guarda
          else
            demoServiceClassName = className;
        } // if
        // se implementa a interface SecurityService...
        if (businessObject instanceof SecurityService) {
          // se j� temos a classe do SecurityService...exce��o
          if (!securityServiceClassName.equals(""))
            throw new ExtendedException(getClass().getName(), "loadExtension", "Mais de uma classe encontrada implementando o servi�o de seguran�a.");
          // se ainda n�o temos...guarda
          else
            securityServiceClassName = className;
        } // if
      } // if
    } // for
    // guarda as listas finais
    classNames = new String[vectorClasses.size()];
    vectorClasses.copyInto(classNames);
  }

  /**
   * Carrega a extens�o.
   * @throws Exception Em caso de exce��o na tentativa de extrair os arquivos
   *                   da extens�o ou suas classes.
   */
  private void loadExtension() throws Exception {
    // acesso ao arquivo da extens�o
    File file = new File(fileName);
    // nome do arquivo de par�metros da extens�o
    String paramListFileName = cacheFilesPath + ".." + File.separatorChar + name + ".params";
    // carrega os par�metros da extens�o
    ParamList paramList = new ParamList();
    paramList.load(paramListFileName);
    // par�metro que cont�m a data de modifica��o da extens�o que se encontra extra�da
    Param paramLastModifiedDate = paramList.get(LAST_MODIFIED_DATE);
    // se ainda n�o temos o par�metro...
    if (paramLastModifiedDate == null) {
      // cria o par�metro com data zero, ou seja, como se nunca tivesse sido extra�da
      paramLastModifiedDate = new Param(LAST_MODIFIED_DATE, "0");
      // adiciona na lista
      paramList.add(paramLastModifiedDate);
    } // if
    // se a extens�o foi modificada ap�s a �ltima extra��o...devemos extrai-la
    boolean extract = file.lastModified() > Long.parseLong(paramLastModifiedDate.getValue());

    // se devemos extrair os arquivos da extens�o...
    if (extract) {
      // apaga os arquivos de cache atualmente existentes
      String[] cacheExtensions = {"*"};
      File[] cacheFiles = FileTools.getFiles(cacheFilesPath, cacheExtensions, true);
      for (int i=cacheFiles.length-1; i>=0; i--) {
        cacheFiles[i].delete();
      } // for
      // descomprime o arquivo
      new UnZip(fileName, cacheFilesPath);
      // salva a data de modifica��o da extens�o nos par�metros
      paramLastModifiedDate.setValue(file.lastModified() + "");
      paramList.store(paramListFileName);
    } // if

    // caminho raiz das classes da extens�o
    String classFilesRootPath = cacheFilesPath + "WEB-INF" + File.separatorChar + "classes" + File.separatorChar;
    // adiciona o caminho das classes da extens�o ao ClassPath
    SystemTools.addClassPath(this, classFilesRootPath);
    // pega a lista de classes
    String[] extensions = {".class"};
    classNames = FileTools.getFileNames(classFilesRootPath,
                                        extensions,
                                        true);

    // ajusta os nomes das classes deixando apenas na forma package.className
    for (int i=0; i<classNames.length; i++) {
      // nome da classe da vez
      String className = classNames[i];
      // retira o caminho e a extens�o
      className = className.substring(classFilesRootPath.length(),
                                      className.length() - 6);
      // troca as '/' por '.'
      className = className.replace(File.separatorChar, '.');
      // p�e o nome novo na lista
      classNames[i] = className;
    } // for
  }

  /**
   * Retorna o nome da classe que imeplementa a interface Registry.
   * Retorna "" caso nenhum objeto da extens�o implemente esta interface.
   * @return String Retorna o nome da classe que imeplementa a interface
   *         Registry. Retorna "" caso nenhum objeto da extens�o
   *         implemente esta interface.
   */
  public String registryClassName() {
    return registryClassName;
  }
  
  /**
   * Retorna o nome da classe que imeplementa a interface SecurityService.
   * Retorna "" caso nenhum objeto da extens�o implemente esta interface.
   * @return String Retorna o nome da classe que imeplementa a interface
   *         SecurityService. Retorna "" caso nenhum objeto da extens�o
   *         implemente esta interface.
   */
  public String securityServiceClassName() {
    return securityServiceClassName;
  }

  /**
   * Retorna o caminho relativo para ser usado como URL dos arquivos da extens�o.
   * @return String Retorna o caminho relativo para ser usado como URL dos
   *         arquivos da extens�o.
   */
  public String getUrl() {
    return url;
  }

}
