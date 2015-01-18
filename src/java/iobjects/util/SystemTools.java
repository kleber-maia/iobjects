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

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

import iobjects.*;

/**
 * Utilit�rio contendo ferramentas para o sistema.
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
   * Adiciona 'classPath' ao ClassLoader respons�vel pela carga da classe
   * do objeto indicado por 'reference'.
   * @param reference Object Objeto de refer�ncia para obten��o do ClassLoader.
   * @param classPath String Caminho do diret�rio ou arquivo JAR que ser� adicionado
   *                         ao ClassPath.
   * @throws Exception Em caso de exce��o na tentativa de adicionar o caminho
   *                   informado ao ClassPath.
   */
  static public void addClassPath(Object reference, String classPath) throws Exception {
    // obt�m o nosso ClassLoader
    ClassLoader classLoader = reference.getClass().getClassLoader();
    // obt�m a classe do nosso ClassLoader
    Class classLoaderClass = classLoader.getClass();
    // vai subindo na hierarquia de classes at� achar URLClassLoader
    while ((classLoaderClass != null) && (classLoaderClass != URLClassLoader.class))
      classLoaderClass = classLoaderClass.getSuperclass();
    // se n�o achamos...exce��o
    if (classLoaderClass == null)
      throw new ExtendedException("iobjects.util.SystemTools", "addClassPath", "O ClassLoader de refer�ncia n�o � descendente de URLClassLoader.");
    // localiza o m�todo "addURL" do ClassLoader
    Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
    // torna-o acess�vel, pois por padr�o ele � protegido (protected)
    method.setAccessible(true);
    // executa o m�todo passando o caminho recebido
    method.invoke(classLoader, new Object[]{new File(classPath).toURL()});
  }

  /**
   * Retorna o ClassPath do ClassLoader respons�vel pela carga da classe do
   * objeto identificado por 'reference';
   * @param reference Object Objeto de refer�ncia para obten��o do ClassLoader.
   * @return String[] Retorna o ClassPath do ClassLoader respons�vel pela carga
   *                  da classe do objeto identificado por 'reference';
   * @throws Exception Em caso de exce��o na tentativa de obter os caminhos
   *                   do ClassPath.
   */
  static public String[] getClassPath(Object reference) throws Exception {
    // obt�m o nosso ClassLoader
    ClassLoader classLoader = reference.getClass().getClassLoader();
    // se o ClassLoader n�o � descendente de URLClassLoader...exce��o
    if (!(classLoader instanceof URLClassLoader))
      throw new ExtendedException("iobjects.util.SystemTools", "getClassPath", "O ClassLoader de refer�ncia n�o � descendente de URLClassLoader.");
    // obt�m as URL�s
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
