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
   * Construtor padrão.
   */
  public FAQList() {
  }

  /**
   * Construtor estendido.
   * @param moduleName String Nome do módulo que a FAQList representa.
   */
  private FAQList(String moduleName) {
    // se não informou o nome...exceção
    if (moduleName.trim().equals(""))
      throw new RuntimeException(getClass().getName() + "(): nome do módulo não informado.");
    // nossos valores
    this.moduleName = moduleName;
  }

  /**
   * Adiciona o FAQ a lista.
   * @param faq FAQ para ser adicionada a lista.
   */
  public void add(FAQ faq) {
    // se já temos o FAQ na lista...dispara
    if (get(faq.getName()) != null)
      return;
    // adiciona na lista
    faqs.add(faq);
    // se representamos um módulo...dispara
    if (!moduleName.equals(""))
      return;
    // procura o módulo a que a FAQ pertence
    FAQList module = getModuleFAQList(faq.getModule());
    // se ainda não temos o módulo...adiciona-o
    if (module == null) {
      // cria a lista de FAQ's que representa o módulo
      module = new FAQList(faq.getModule());
      // adiciona a FAQ
      module.add(faq);
      // adiciona o módulo a lista
      modules.add(module);
    }
    // se já temos o módulo...adiciona a FAQ
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
   * Retorna a FAQ referente ao índice informado.
   * @param index int Índice da FAQ que se deseja retornar.
   * @return FAQ Retorna a FAQ referente ao índice informado.
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
   * Retorna a lista de FAQ's contida no módulo representado por 'moduleName' ou
   * null caso não exista nenhum FAQ.
   * @param moduleName String Nome do módulo cujas FAQ's se deseja retornar.
   * @return FAQList Retorna a lista de FAQ's contida no módulo representado
   *         por 'moduleName' ou null caso não exista nenhum FAQ.
   */
  public FAQList getModuleFAQList(String moduleName) {
    // loop nos módulos
    Iterator iterator = modules.iterator();
    for (int i=0; iterator.hasNext(); i++) {
      // módulo da vez
      FAQList result = (FAQList)iterator.next();
      // se é o módulo desejado...retorna-o
      if (result.moduleName.equalsIgnoreCase(moduleName))
        return result;
    } // for
    // se chegou até aqui não achamos nada
    return null;
  }

  /**
   * Retorna a lista de módulos em ordem alfabética.
   * @return String[] Retorna a lista de módulos em ordem alfabética.
   */
  public String[] getModuleNames() {
    // nosso resultado
    String[] result = new String[modules.size()];
    // loop nos módulos
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
   * Ordena os FAQ's por ordem alfabética das suas perguntas.
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
