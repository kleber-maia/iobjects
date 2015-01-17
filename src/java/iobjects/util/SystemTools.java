package iobjects.util;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

import iobjects.*;

/**
 * Utilitário contendo ferramentas para o sistema.
 */
public class SystemTools {

  static public void main(String[] args) {
    try {
      Param obj = new Param(null, null);
      SystemTools.addClassPath(obj, "c:\\");
      System.out.println("---");
      System.out.println(StringTools.arrayStringToString(SystemTools.getClassPath(obj), ";"));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private SystemTools() {
  }

  /**
   * Adiciona 'classPath' ao ClassLoader responsável pela carga da classe
   * do objeto indicado por 'reference'.
   * @param reference Object Objeto de referência para obtenção do ClassLoader.
   * @param classPath String Caminho do diretório ou arquivo JAR que será adicionado
   *                         ao ClassPath.
   * @throws Exception Em caso de exceção na tentativa de adicionar o caminho
   *                   informado ao ClassPath.
   */
  static public void addClassPath(Object reference, String classPath) throws Exception {
    // obtém o nosso ClassLoader
    ClassLoader classLoader = reference.getClass().getClassLoader();
    // obtém a classe do nosso ClassLoader
    Class classLoaderClass = classLoader.getClass();
    // vai subindo na hierarquia de classes até achar URLClassLoader
    while ((classLoaderClass != null) && (classLoaderClass != URLClassLoader.class))
      classLoaderClass = classLoaderClass.getSuperclass();
    // se não achamos...exceção
    if (classLoaderClass == null)
      throw new ExtendedException("iobjects.util.SystemTools", "addClassPath", "O ClassLoader de referência não é descendente de URLClassLoader.");
    // localiza o método "addURL" do ClassLoader
    Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
    // torna-o acessível, pois por padrão ele é protegido (protected)
    method.setAccessible(true);
    // executa o método passando o caminho recebido
    method.invoke(classLoader, new Object[]{new File(classPath).toURL()});
  }

  /**
   * Retorna o ClassPath do ClassLoader responsável pela carga da classe do
   * objeto identificado por 'reference';
   * @param reference Object Objeto de referência para obtenção do ClassLoader.
   * @return String[] Retorna o ClassPath do ClassLoader responsável pela carga
   *                  da classe do objeto identificado por 'reference';
   * @throws Exception Em caso de exceção na tentativa de obter os caminhos
   *                   do ClassPath.
   */
  static public String[] getClassPath(Object reference) throws Exception {
    // obtém o nosso ClassLoader
    ClassLoader classLoader = reference.getClass().getClassLoader();
    // se o ClassLoader não é descendente de URLClassLoader...exceção
    if (!(classLoader instanceof URLClassLoader))
      throw new ExtendedException("iobjects.util.SystemTools", "getClassPath", "O ClassLoader de referência não é descendente de URLClassLoader.");
    // obtém as URL´s
    URL[] urls = ((URLClassLoader)classLoader).getURLs();
    // nosso resultado
    String[] result = new String[urls.length];
    // copia
    for (int i=0; i<urls.length; i++)
      result[i] = urls[i].toString();
    // retorna
    return result;
  }

}
