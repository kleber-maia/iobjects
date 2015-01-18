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
package iobjects.help;

import java.util.*;

/**
 * Representa uma lista de FAQ's.
 * @since 2006
 */
public class FAQList {

  class ModuleComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      FAQList faqList1 = (FAQList)o1;
      FAQList faqList2 = (FAQList)o2;
      return faqList1.moduleName.compareTo(faqList2.moduleName);
    }
  }

  class QuestionComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      FAQ faq1 = (FAQ)o1;
      FAQ faq2 = (FAQ)o2;
      return faq1.getQuestion().compareTo(faq2.getQuestion());
    }
  }

  private Vector  faqs       = new Vector();
  private TreeSet modules    = new TreeSet(new ModuleComparator());
  private String  moduleName = "";

  /**
   * Construtor padr�o.
   */
  public FAQList() {
  }

  /**
   * Construtor estendido.
   * @param moduleName String Nome do m�dulo que a FAQList representa.
   */
  private FAQList(String moduleName) {
    // se n�o informou o nome...exce��o
    if (moduleName.trim().equals(""))
      throw new RuntimeException(getClass().getName() + "(): nome do m�dulo n�o informado.");
    // nossos valores
    this.moduleName = moduleName;
  }

  /**
   * Adiciona o FAQ a lista.
   * @param faq FAQ para ser adicionada a lista.
   */
  public void add(FAQ faq) {
    // se j� temos o FAQ na lista...dispara
    if (get(faq.getName()) != null)
      return;
    // adiciona na lista
    faqs.add(faq);
    // se representamos um m�dulo...dispara
    if (!moduleName.equals(""))
      return;
    // procura o m�dulo a que a FAQ pertence
    FAQList module = getModuleFAQList(faq.getModule());
    // se ainda n�o temos o m�dulo...adiciona-o
    if (module == null) {
      // cria a lista de FAQ's que representa o m�dulo
      module = new FAQList(faq.getModule());
      // adiciona a FAQ
      module.add(faq);
      // adiciona o m�dulo a lista
      modules.add(module);
    }
    // se j� temos o m�dulo...adiciona a FAQ
    else {
      module.add(faq);
    } // if
  }

  /**
   * Limpa a lista de FAQ's.
   */
  public void clear() {
    faqs.clear();
    modules.clear();
  }

  public void finalize() {
    clear();
    faqs = null;
    modules = null;
  }

  /**
   * Retorna a FAQ referente ao �ndice informado.
   * @param index int �ndice da FAQ que se deseja retornar.
   * @return FAQ Retorna a FAQ referente ao �ndice informado.
   */
  public FAQ get(int index) {
    return (FAQ)faqs.elementAt(index);
  }

  /**
   * Retorna a FAQ referente ao nome informado.
   * @param name String Nome da FAQ que se deseja retornar.
   * @return FAQ Retorna a FAQ referente ao nome informado.
   */
  public FAQ get(String name) {
    FAQ result = null;
    for (int i=0; i<faqs.size(); i++) {
      result = get(i);
      if (result.getName().equalsIgnoreCase(name))
        return result;
    } // for
    return null;
  }

  /**
   * Retorna a lista de FAQ's contida no m�dulo representado por 'moduleName' ou
   * null caso n�o exista nenhum FAQ.
   * @param moduleName String Nome do m�dulo cujas FAQ's se deseja retornar.
   * @return FAQList Retorna a lista de FAQ's contida no m�dulo representado
   *         por 'moduleName' ou null caso n�o exista nenhum FAQ.
   */
  public FAQList getModuleFAQList(String moduleName) {
    // loop nos m�dulos
    Iterator iterator = modules.iterator();
    for (int i=0; iterator.hasNext(); i++) {
      // m�dulo da vez
      FAQList result = (FAQList)iterator.next();
      // se � o m�dulo desejado...retorna-o
      if (result.moduleName.equalsIgnoreCase(moduleName))
        return result;
    } // for
    // se chegou at� aqui n�o achamos nada
    return null;
  }

  /**
   * Retorna a lista de m�dulos em ordem alfab�tica.
   * @return String[] Retorna a lista de m�dulos em ordem alfab�tica.
   */
  public String[] getModuleNames() {
    // nosso resultado
    String[] result = new String[modules.size()];
    // loop nos m�dulos
    Iterator iterator = modules.iterator();
    for (int i=0; iterator.hasNext(); i++)
      result[i] = ((FAQList)iterator.next()).moduleName;
    // retorna
    return result;
  }

  /**
   * Retorna o tamanho da lista.
   * @return int Retorna o tamanho da lista.
   */
  public int size() {
    return faqs.size();
  }

  /**
   * Ordena os FAQ's por ordem alfab�tica das suas perguntas.
   */
  public void sort() {
    // copia a lista atual para um Array
    FAQ[] faqArray = new FAQ[size()];
    faqs.copyInto(faqArray);
    // ordena
    Arrays.sort(faqArray, new QuestionComparator());
    // refaz nossa lista
    clear();
    for (int i=0; i<faqArray.length; i++)
      add(faqArray[i]);
  }

}
