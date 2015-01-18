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

import java.text.*;
import java.util.*;
import java.text.DecimalFormat;

/**
 * Utilit�rio para formata��o de valores monet�rios por extenso.
 * @since 2006
 */
public class Money {

  static public void main(String[] args) {
    try {
      System.out.println(Money.format(2345.67));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  static private double  fValue;
  static private String  fLiteral;
  static private Vector  arrayCentenas;
  static private Vector  arrayICentenas;
  static private boolean jaExisteDE;
  static private boolean jaExisteE;
  static private boolean enableMilions;

  /**
   * Define as constantes ...
   */
  static private final String CENTAVOS_PLURAL   = "centavos";
  static private final String CENTAVOS_SINGULAR = "centavo";
  static private final String MOEDA_PLURAL      = "reais";
  static private final String MOEDA_SINGULAR    = "real";
  static private final String MILESIMOS         = "mil�simos de";
  static private final String[] UNIDADE = {"um", "dois", "tr�s", "quatro", "cinco",
                                           "seis", "sete", "oito", "nove", "dez",
                                           "onze", "doze", "treze", "quatorze",
                                           "quinze", "dezeseis", "dezesete",
                                           "dezoito", "dezenove"
                                          };
  static private final String[] DEZENA = {"dez", "vinte", "trinta", "quarenta",
                                          "cinq�enta", "sessenta", "setenta",
                                          "oitenta", "noventa"
                                         };
  static private final String[] CENTENA = {"cem", "duzentos", "trezentos",
                                           "quatrocentos", "quinhentos", "seiscentos",
                                           "setecentos", "oitocentos", "novecentos"
                                          };
  static private final String[] GRANDEZA = {"milh�o", "bilh�o", "trilh�o",
                                            "milh�es", "bilh�es", "trilh�es"
                                           };

  static private void understadingCentavos(){
    String str = "";
    DecimalFormat formatoDecimal = new DecimalFormat("0.###");
    // pega o valor inteiro
    StringBuffer stValue = new StringBuffer(formatoDecimal.format(fValue));
    // recupera os formatos de numero do windows
    DecimalFormatSymbols decimalFormat = new DecimalFormatSymbols();
    // se n�o tem centavos dispara
    int posDecimal = stValue.indexOf(decimalFormat.getDecimalSeparator() + "");
    if (posDecimal == -1)
      return;
    // pega apenas as tr�s primeiras casas decimais
    stValue = new StringBuffer(stValue.substring((posDecimal + 1), stValue.length()));
    // se n�o tem nada...dispara
    if (stValue.equals("") || (Integer.parseInt(stValue.toString()) == 0))
      return;
    // se tem menos de tr�s casas ou n�o ativou mil�simos...
    else if ( (stValue.length() < 3) || ! (enableMilions)) {
      // se s� tem uma casa...p�e um zero ap�s
      if (stValue.length() == 1)
        stValue.append("0");
        // se a dezena for at� 19
      if (Integer.parseInt(stValue.toString()) <= 19)
        str += UNIDADE[Integer.parseInt(stValue.toString())-1];
        // se for a partir de 20
      else {
        // p�e a dezena
        str += DEZENA[Integer.parseInt(stValue.charAt(0)+"")-1];
        // se tem unidade...p�e
        if (Integer.parseInt(stValue.charAt(1)+"") > 0)
          str += " e " + UNIDADE[Integer.parseInt(stValue.charAt(1)+"")-1];
      } // if
      // se tem outros valores...adiciona o 'e'
      if (fValue >= 1)
        fLiteral += " e ";
        // p�e no extenso
      fLiteral += str + " ";
      // acrescenta o nome dos centavos
      if (Integer.parseInt(stValue.toString()) == 1)
        fLiteral += CENTAVOS_SINGULAR;
      else // no singular...
        fLiteral += CENTAVOS_PLURAL; // ...ou no plural
    // se tem 3 casas...vamos tratar como mil�simo de real...
    } else {
      // se a centena � apenas 1
      if (Integer.parseInt(stValue.toString()) == 1)
        str = "um";
        // se a centena tem dezena ou unidade...
      else if (Integer.parseInt(stValue.substring(1,3)) >= 1) {
        // se tem valor na centena...
        if (Integer.parseInt(stValue.charAt(1)+"") > 0){
          // ...se for uma centena de Cem
          if (stValue.charAt(0) == '1')
            str = "cento e ";
            // ...se for outra centena
          else
            str = CENTENA[Integer.parseInt(stValue.charAt(1)+"")-1] + " e ";
        } // if
            // se a dezena for at� 19
        if (Integer.parseInt(stValue.substring(1,3)) <= 19)
          str += UNIDADE[Integer.parseInt(stValue.substring(1,3))-1];
          // se for a partir de 20
        else {
          // p�e a dezena
          str += DEZENA[Integer.parseInt(stValue.charAt(1)+"")-1];
          // se tem unidade...p�e
          if (Integer.parseInt(stValue.charAt(2)+"") > 0)
            str += " e " + UNIDADE[Integer.parseInt(stValue.charAt(2)+"")-1];
        } // if
      } // if
      // se � uma centena pura...
      else if (Integer.parseInt(stValue.charAt(0)+"") > 0)
        str = CENTENA[Integer.parseInt(stValue.charAt(0)+"")-1];
        // se tem outros valores...adiciona a 'e'
      if (fValue >= 1)
        fLiteral += " e ";
        // p�e no extenso
      fLiteral += str + " " + MILESIMOS + " " + MOEDA_SINGULAR;
    } // if
  }

  static private boolean addDe(int deIndex){
    // se j� existe um De... dispara
    // se a centena for abaixo de Milh�o...dispara
    // se a centena n�o tem valor...dispara
    if ((jaExisteDE) || (deIndex < 2) || (Integer.parseInt((String)arrayCentenas.get(deIndex))==0))
      return false;
    // vasculha as arstCentenas inferiores
    for (int w = deIndex -1; w >=0; w--){
      if (Integer.parseInt( (String) arrayCentenas.get(w)) > 0)
        return false;
    } // for
    // se chegou at� aqui � por que merece De
    jaExisteDE = true;
    return  jaExisteDE;
  }

  static private boolean addE(int eIndex){
    // se j� existe E...dispara
    // se a centena for abaixo de Mil...dispara
    // se a centena n�o tem valor...dispara
    if ((jaExisteE) || (eIndex < 1) || (Integer.parseInt((String)arrayCentenas.get(eIndex))==0))
      return false;
    // vasculha arstCentenas inferiores
    int q = 0;
    boolean ok = false;
    for (int w =eIndex -1; w >=0; w--){
      if (Integer.parseInt((String)arrayCentenas.get(w)) > 0){
        // qde de E's
        q++;
        // se merece mais de um E...dispara
        if (q > 1)
          return false;
        // s� merece E se for menor que 100 ou for uma centena pura
        ok = ((Integer.parseInt((String)arrayCentenas.get(w)) < 100) || ((Integer.parseInt((String)arrayCentenas.get(w))%100)==0));
      } // if
    } // for
    // se chegou at� aqui e est� Ok...merece o E
    jaExisteE  = ok;
    return  ok;
  }

  static private boolean addVirgula(int commaIndex){
    // se a centena for abaixo de Mil...dispara
    // se a centena n�o tem valor...dispara
    if ((commaIndex < 1) || (Integer.parseInt((String)arrayCentenas.get(commaIndex))==0))
      return false;
    int q =0;
    for (int w = commaIndex-1; w >=0; w--){
      if (Integer.parseInt((String)arrayCentenas.get(w)) > 0)
        q++;
    } // for
    // se q for maior que 1...merece v�rgula
    return ((q >= 1) && !(addE(commaIndex)));
  }

  static private void groupCentenas(){
    // se n�o tem nenhuma centena...dispara
    if (arrayCentenas.size() == 0)
      return;
    // agrupando as arstCentenas
    for (int i = arrayCentenas.size() -1; i>=0; i--){
      // p�e o extenso da centena
      fLiteral = fLiteral.trim() + " " + ((String)arrayICentenas.get(arrayCentenas.size()-1-i)).trim();
      // p�e o DE
      if (addDe(i))
        fLiteral += " de ";
      // p�e o E
      else if (addE(i))
        fLiteral += " e ";
      else if (addVirgula(i))
        fLiteral += ", ";
    } // for
    // p�e o nome da moeda no plural
    fLiteral = (fLiteral.trim()) + " " + MOEDA_PLURAL;
  }

  static private void separateCentenas(){
    // pega o valor como String
    StringBuffer stbValue = new StringBuffer(NumberTools.format(fValue));
    // recupera os formatos de numero da maquina
    DecimalFormatSymbols decimalFormat = new DecimalFormatSymbols();
    int pos = stbValue.indexOf(decimalFormat.getDecimalSeparator()+"");
    if (pos != -1)
      stbValue.delete(pos,stbValue.length());
    while (stbValue.indexOf(decimalFormat.getGroupingSeparator()+"") > 0){
      arrayCentenas.addElement(stbValue.substring(stbValue.length()-3));
      stbValue.delete(stbValue.length()-4, stbValue.length());
    } // while
    // �ltima centena
    arrayCentenas.addElement(stbValue.toString());
    // organiza as arstCentenas com zeros na frente
    for (int i =0; i < arrayCentenas.size() ; i++){
      if (Integer.parseInt((String)arrayCentenas.get(i)) < 10){
         arrayCentenas.setElementAt("00"+(String)arrayCentenas.get(i),i);
      } else if  (Integer.parseInt((String)arrayCentenas.get(i)) < 100){
         arrayCentenas.setElementAt("0"+(String)arrayCentenas.get(i),i);;
      } // if
    } // for
  }

  static private void understadingCentenas()throws Exception {
    // gerando o extenso de cada centena individualmente
    for (int i = arrayCentenas.size()-1; i >= 0; i--) {
      // pega o valor da centena
      String stValue = (String) arrayCentenas.get(i);
      // apaga o extenso
      String str = "";
      // se a centena � apenas 1
      if (Integer.parseInt(stValue) == 1) {
        str = "um";
      } // se a centena tem dezena ou unidade...
      else if (Integer.parseInt(stValue.substring(1,3)) >= 1) {
        // se tem valor na centena...
        if (Integer.parseInt(""+stValue.charAt(0)) > 0) {
          // ...se for uma centena de Cem
          if (stValue.charAt(0) == '1')
            str = "cento e ";
            // ...se for outra centena
          else
            str = CENTENA[ (Integer.parseInt(""+stValue.charAt(0))) -1] + " e ";
        } // if
        if (Integer.parseInt(stValue.substring(1,3)) <= 19) {
          str += UNIDADE[(Integer.parseInt(stValue.substring(1,3))) -1];
        } else {
          // p�e a dezena
          str += DEZENA[ (Integer.parseInt(""+stValue.charAt(1))) -1];
          if ( (Integer.parseInt(""+stValue.charAt(2))) > 0)
            str += " e " + UNIDADE[ (Integer.parseInt(""+stValue.charAt(2)))-1];
        } // if
      } // if
      // se � uma centena pura...
      else if ((Integer.parseInt(""+stValue.charAt(0))) > 0)
        str = CENTENA[ (Integer.parseInt(""+stValue.charAt(0))) -1];
        // grandeza da centena...
        // ...se � a primeira...p�e s� o nome da moeda
      if (i == 0)
        str = str;
      else if (!str.equals("")) {
        if (i == 1)
          str += " mil";
        else {
          if (Integer.parseInt(stValue.toString()) == 1)
            // grandeza no singular...
            str += " " + GRANDEZA[i -2];
          else
            // ...ou no plural
            str += " " + GRANDEZA[i + 1];
        } // else
      } // if
      // adiciona nas centenas interpretadas
      arrayICentenas.addElement(str);
    } // for
  }

  /**
   * Retorna 'value' formatado por extenso.
   * @param value double Valor monet�rio para qual se deseja obter a
   *              representa��o por extenso.
   * @throws Exception Em caso de exce��o na tentativa de formata��o do valor.
   * @return String Retorna 'value' formatado por extenso.
   */
  static public String format(double value) throws Exception {
    // alimenta a variavel global
    fValue = value;
    // se � um valor negativo...ajusta
    if (fValue < 0)
      fValue = fValue * -1;

    // se o valor � zero...pronto
    if (fValue == 0){
      fLiteral = "zero " + MOEDA_PLURAL;
    }
    // se � 1 real...pronto
    else if (fValue == 1){
      fLiteral = "um " + MOEDA_SINGULAR;
    } // if
    // se � um outro valor maior que 2...vai para as rotinas gerais
    else if (fValue > 1){
      // centena
      arrayCentenas = new Vector();
      arrayICentenas = new Vector();
      jaExisteDE = false;
      jaExisteE = false;
      fLiteral = "";
      enableMilions = true;
      // separa o n�mero em arstCentenas
      separateCentenas();
      // escreve o extenso de cada centena
      understadingCentenas();
      // agrupa os valores num s� n�mero
      groupCentenas();
    }
    // interpreta os centavos
    understadingCentavos();
    // retorna o extenso
    return fLiteral;
  }

}
