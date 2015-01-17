package iobjects.util;

/**
 * Utilitário para validação de dígitos verificadores de inscrições estaduais.
 * @since 2006
 * @see DigitVerifier
 */
class DigitVerifierIe {

  /**
   * Define o sufixo do nome dos métodos de verificação de inscrição estadual.
   */
  static public final String VERIFIER_METHOD_SUFIX_NAME = "Verifier";

  /**
   * Verifica se 'IE' é valido para o Estado do Acre.
   * @param ie String Insrição Estadual passada pelo cliente.
   * @return boolean Verifica se 'IE' é valido para o Estado do Acre.
   */
  static public boolean ACVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if ((ie.length() != 9) && (ie.length() != 13)) {
      return false;
    } // if
    // se a inscricao for de 9 (8+1) digitos
    if (ie.length() == 9) {
      int a = Integer.parseInt(ie.substring(7,8));
      int b = Integer.parseInt(ie.substring(6,7));
      int c = Integer.parseInt(ie.substring(5,6));
      int d = Integer.parseInt(ie.substring(4,5));
      int e = Integer.parseInt(ie.substring(3,4));
      int f = Integer.parseInt(ie.substring(2,3));
      int g = Integer.parseInt(ie.substring(1,2));
      int h = Integer.parseInt(ie.substring(0,1));
      int i = Integer.parseInt(ie.substring(8,9));
      int soma = (9*h) + (8*g) + (7*f) + (6*e) + (5*d) + (4*c) + (3*b) + (2*a);
      int resto = (soma % 11);
      if (resto < 2) {
        resto = 0;
      } else {
        resto = (11-resto);
      } // if
      return (resto == i);
    } else {
      // se a inscricao for de 13 (11+2) digitos
      if (ie.length() == 13) {
        int a = Integer.parseInt(ie.substring(10,11));
        int b = Integer.parseInt(ie.substring(9,10));
        int c = Integer.parseInt(ie.substring(8,9));
        int d = Integer.parseInt(ie.substring(7,8));
        int e = Integer.parseInt(ie.substring(6,7));
        int f = Integer.parseInt(ie.substring(5,6));
        int g = Integer.parseInt(ie.substring(4,5));
        int h = Integer.parseInt(ie.substring(3,4));
        int i = Integer.parseInt(ie.substring(2,3));
        int j = Integer.parseInt(ie.substring(1,2));
        int k = Integer.parseInt(ie.substring(0,1));
        int l = Integer.parseInt(ie.substring(12,13));
        int m = Integer.parseInt(ie.substring(13,14));
        int soma = (4*k) + (3*j) + (2*i) + (9*h) + (8*g) + (7*f) + (6*e) + (5*d) + (4*c) + (3*b) + (2*a);
        int resto = (soma % 11);
        resto = (11-resto);
        // se a diferenca em o modulo e o resto for 10 ou 11, o digito é 0.
        if ((resto == 10) || (resto == 11)) {
          resto = 0;
        } // if
        // se o primeiro digito for falso
        if (resto != l) {
          return false;
        } // if
        // calculando o segundo digito
        soma = (5*k) + (4*j) + (3*i) + (2*h) + (9*g) + (8*f) + (7*e) + (6*d) + (5*c) + (4*b) + (3*a) + (2*l);
        resto = (soma % 11);
        resto = (11-resto);
        // se a diferenca em o modulo e o resto for 10 ou 11, o digito é 0.
        if ((resto == 10) || (resto == 11))
         resto = 0;
        return (resto == m);
      } // if
    } // if
    return false;
  }

  /**
   * Verifica se 'IE' é valido para o Estado do Alagoas.
   * @param ie String Insrição Estadual passada pelo cliente.
   * @return boolean Verifica se 'IE' é valido para o Estado do Alagoas.
   */
  static public boolean ALVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0,2));
    // Se os dois primeiros dígitos são diferentes de 24...
    if (d1 != 24) {
      return false;
    } // if
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*8;
    int i = Integer.parseInt(ie.substring(0,1))*9;
    int soma = (b + c + d + e + f + g + h + i) * 10;
    // pega o resto da divisao
    int dig = (soma % 11);
    // se o valor encontrado for 10, o digito é 0
    if (dig == 10) {
      dig = 0;
    } // if
    // se o digito for valido
    return (dig == a);
  }

  /**
   * Verifica se 'IE' é valido para o Estado do Amapá.
   * @param ie String Insrição Estadual passada pelo cliente.
   * @return boolean Verifica se 'IE' é valido para o Estado do Amapá.
   */
  static public boolean APVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    int controle1 = 0;
    int controle2 = 0;
    int d1 = Integer.parseInt(ie.substring(0,2));
    // Se os dois primeiros dígitos são diferentes de 03...
    if (d1 != 03) {
     return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*8;
    int i = Integer.parseInt(ie.substring(0,1))*9;
    // Armazena a inscrição sem o dígito
    int faixa = Integer.parseInt(ie.substring(0,8));
    // Se a inscrição estiver na faixa...
    if ((faixa >= 3000001) && (faixa <= 3017000)) {
      controle1 = 5;
      controle2 = 0;
    }
    // Se a inscrição estiver na faixa...
    else if ((faixa >= 3017001) && (faixa <= 3019022)) {
      controle1 = 9;
      controle2 = 1;
    }
    // Se a inscrição estiver na faixa...
    else if (faixa >= 3019023) {
      controle1 = 0;
      controle2 = 0;
    } // if
    // Soma os valores e multiplica por 10
    int total = controle1 + b + c + d + e + f + g + h + i;
    // Subtrai de 11 para encontrar o digito verificador
    int dig = 11 - (total % 11);
    // Se o valor encontrado for igual 10...
    if (dig == 10) {
      dig = 0;
    } // if
    else if (dig == 11) {
      dig = controle2;
    } // if
    // Se o valor encontrado for igual ao dígito...
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Amazonas.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Amazonas.
  */
  static public boolean AMVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*8;
    int i = Integer.parseInt(ie.substring(0,1))*9;
    // Soma os valores
    int total = (b + c + d + e + f + g + h + i);
    // inicia a variavel
    int dig = 0;
    // Se o total for menor que 11...
    if (total < 11) {
      dig = (11 - total);
    } else {
      dig = (total % 11);
      // Se o resto for igual a 1...
      if (dig <= 1){
        dig = 0;
      } else {
        dig = (11 - dig);
      } // if
    } // if
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Bahia.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Bahia.
  */
  static public boolean BAVerifier(String ie){
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 8) {
      return false;
    } // if
    String digitoValidoModulo10 = "0123458";
    String digitoValidoModulo11 = "679";
    int modulo = 0;
    // Calcula a Inscricao pelo Modulo 10
    if (digitoValidoModulo10.indexOf(ie.substring(0,1)) != -1) {
      modulo = 10;
    // Calcula a Inscricao pelo Modulo 11
    } else if (digitoValidoModulo11.indexOf(ie.substring(0,1)) != -1) {
      modulo = 11;
    } // if
    // Cálculo do 2º Digito
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(0,1))*7;
    int b = Integer.parseInt(ie.substring(1,2))*6;
    int c = Integer.parseInt(ie.substring(2,3))*5;
    int d = Integer.parseInt(ie.substring(3,4))*4;
    int e = Integer.parseInt(ie.substring(4,5))*3;
    int f = Integer.parseInt(ie.substring(5,6))*2;
    int soma  = a + b + c + d + e + f;
    // O segundo digito é...
    int dig2 = 0;
    if ((soma % modulo) != 0) {
      dig2 = modulo -(soma % modulo);
    } // if
    // Cálculo do 1º Digito
    a = Integer.parseInt(ie.substring(0,1))*8;
    b = Integer.parseInt(ie.substring(1,2))*7;
    c = Integer.parseInt(ie.substring(2,3))*6;
    d = Integer.parseInt(ie.substring(3,4))*5;
    e = Integer.parseInt(ie.substring(4,5))*4;
    f = Integer.parseInt(ie.substring(5,6))*3;
    int g = (dig2 * 2)    ;
    soma  = a + b + c + d + e + f + g;
    // o primeiro digito é...
    int dig1 = 0;
    if ((soma % modulo) != 0) {
      dig1 = modulo - (soma % modulo);;
    } // if
    // verifica se a Inscrição é válida
    return ((dig2 == Integer.parseInt(ie.substring(7,8))) && (dig1 == Integer.parseInt(ie.substring(6,7))));
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Ceara.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Ceara.
  */
  static public boolean CEVerifier(String ie){
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0,2));
    // Se os dois primeiros dígitos são diferentes de 6...
    if (d1 != 06) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*8;
    int i = Integer.parseInt(ie.substring(0,1))*9;
    // Soma os valores
    int total = b + c + d + e + f + g + h + i;
    // Calculamos o dígito
    int dig = 11 - (total % 11);
    // Se o valor encontrado for igual 10...
    if ((dig == 10) || (dig == 11))
        dig = 0;
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Distrito Federal.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Distrito Federal.
  */
  static public boolean DFVerifier(String ie){
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 13) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0,2));
    // Se os dois primeiros dígitos são diferentes de 73...
    if (d1 != 07) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(0,1));
    int b = Integer.parseInt(ie.substring(1,2));
    int c = Integer.parseInt(ie.substring(2,3));
    int d = Integer.parseInt(ie.substring(3,4));
    int e = Integer.parseInt(ie.substring(4,5));
    int f = Integer.parseInt(ie.substring(5,6));
    int g = Integer.parseInt(ie.substring(6,7));
    int h = Integer.parseInt(ie.substring(7,8));
    int i = Integer.parseInt(ie.substring(8,9));
    int j = Integer.parseInt(ie.substring(9,10));
    int l = Integer.parseInt(ie.substring(10,11));
    int m = Integer.parseInt(ie.substring(11,12));
    int n = Integer.parseInt(ie.substring(12,13));
    // calculo primeiro digito
    int soma1 = (2*l) + (3*j) + (4*i) + (5*h) + (6*g) + (7*f) + (8*e) + (9*d)
                + (2*c) + (3*b) + (4*a);
    // acha o resto da divisao da soma acima pelo Modulo 11
    int resto1 = (soma1 % 11);
    // resultado do primeiro digito verificador
    int dig1 = 11 - resto1;
    // se o resultado acima for 10 ou 11 o primeiro digito sera 0
    if ((dig1 == 10) || (dig1 == 11))
      dig1 = 0;
    // calculo do segundo digito
    int soma2 = (2*m) + (3*l) + (4*j) + (5*i) + (6*h) + (7*g) + (8*f) + (9*e)
                + (2*g) + (3*c) + (4*b) + (5*a);
    int resto2 = (soma2 % 11);
    int dig2 = 11 - resto2;
    if ((dig2 == 10) || (dig2 == 11))
      dig2 = 0;
    return ((dig1 == m) && (dig2 == n));
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Goias.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Goias.
  */
  static public boolean GOVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    }// if
    int d1 = Integer.parseInt(ie.substring(0,2));
    // Se os dois primeiros dígitos são diferentes de 10, 11 e 15...
    if ((d1 != 10)&(d1 != 11)&(d1 != 15)) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*8;
    int i = Integer.parseInt(ie.substring(0,1))*9;
    // Soma os valores
    int total = b + c + d + e + f + g + h + i;
    // Calcula o resto
    int dig = (total % 11);
    int aux = Integer.parseInt(ie.substring(0,8));
    // Se a inscrição for igual a 11094402...
    if (aux == 11094402) {
      if ((a == 0)|(a == 1))
        dig = a;
    } else {
      // Se o resto for igual a 0...
      if (dig == 0) {
        dig = 0;
      } else if (dig == 1) {
        // Se a inscrição estiver na faixa...
        if ((aux >= 10103105) && (aux <= 10119997))
          dig = 1;
        else
          dig = 0;
      }
      // Se for diferente de um e zero(maior que um)...
      else dig = 11 - dig;
    }  // if
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Maranhã.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Maranhão.
  */
  static public boolean MAVerifier(String ie){
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0,2));
    // Se os dois primeiros dígitos são diferente de 12
    if (d1 != 12) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(0,1));
    int b = Integer.parseInt(ie.substring(1,2));
    int c = Integer.parseInt(ie.substring(2,3));
    int d = Integer.parseInt(ie.substring(3,4));
    int e = Integer.parseInt(ie.substring(4,5));
    int f = Integer.parseInt(ie.substring(5,6));
    int g = Integer.parseInt(ie.substring(6,7));
    int h = Integer.parseInt(ie.substring(7,8));
    int i = Integer.parseInt(ie.substring(8,9));
    // calcula a soma com seus respectivos pesos..
    int soma = (9*a) + (8*b) + (7*c) + (6*d) + (5*e) + (4*f) + (3*g) + (2*h);
    // acha o resto da divisao da soma pelo Modulo 11
    int resto = (soma % 11);
    int dig = 0;
    if (!((resto == 0) || (resto == 1))) {
      dig = 11 - resto;
    } // if
    return (dig == i);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Minas Gerais.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Minas Gerais.
  */
  static public boolean MGVerifier(String ie){
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 13) {
      return false;
    } // if
    ie = ie.substring(0,3) + "0" + ie.substring(3);
    // Recebe os valores
    int a = Integer.parseInt(ie.substring(12,13));
    // Atribui valor a flag
    int flag = 2;
    int totalAlgarismo = 0;
    // Efetua o cálculo do dígito verificador
    for (int i=11;i>=0;i--) {
      int b = Integer.parseInt(ie.substring(i,i+1)) * flag;
      // Se o resultado do cálculo tiver mais de um algarismo
      if (Integer.toString(b).length() > 1) {
        for (int j=0;j<Integer.toString(b).length();j++){
          // Soma o valor de cada algarismo
          totalAlgarismo = totalAlgarismo + Integer.parseInt(Integer.toString(b).substring(j,j+1));
        } // for
      } else {
        totalAlgarismo = totalAlgarismo + b;
      } // if
      // Muda o peso
      flag = flag == 2 ? 1:2;
    } // for
    String stAux1="";
    for (int i=Integer.toString(totalAlgarismo).length();i>=0;i--){
      if (Integer.toString(totalAlgarismo).length() > 2) {
        stAux1 = Integer.toString(totalAlgarismo).substring(1,2);
      } else {
        stAux1 = Integer.toString(totalAlgarismo).substring(0,1);
      } // if
    } // for
    // Calcula a dezena imediatamente superior
    int inAux = (Integer.parseInt(stAux1) + 1) * 10;
    // Calcula o digíto
    int dig1 = (inAux - totalAlgarismo);
    // Se for igual a 10 ou 11
    if ((dig1 == 10) || (dig1 == 11))
      dig1 = 0;
    // Concatena o dígito 1 com a inscricao
    ie = (ie.substring(0,3) + ie.substring(4,12) + Integer.toString(dig1) + ie.substring(13,14));
    // Calcula o produto dos valores recebidos para o segundo dígito
    int a1 = Integer.parseInt(ie.substring(12,13));
    flag = 2;
    int totalProduto = 0;
    // Efetua o cálculo do dígito verificador
    for (int i=11;i>=0;i--){
      // Calcula
      int b1 = Integer.parseInt(ie.substring(i,i+1))*flag;
      // soma
      totalProduto = totalProduto + b1;
      // Muda o peso
      if (flag <= 10)
        flag += 1;
      else
        flag = 2;
    } // for
    // Calculo do dígito
    int dig2 = (totalProduto % 11);
    // Se for igual a 1 ou 0
    if ((dig2 == 0) || (dig2 == 1))
      dig2 = 0;
    else
      dig2 = 11 - dig2;
   // Se o valor encontrado for igual ao dígito..
   return ((dig1 == a) && (dig2 == a1));
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Mato Grosso do Sul.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Mato Grosso do Sul.
  */
  static public boolean MSVerifier(String ie){
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int digito = Integer.parseInt(ie.substring(8,9));
    int d = Integer.parseInt(ie.substring(7,8))*2;
    int e = Integer.parseInt(ie.substring(6,7))*3;
    int f = Integer.parseInt(ie.substring(5,6))*4;
    int g = Integer.parseInt(ie.substring(4,5))*5;
    int h = Integer.parseInt(ie.substring(3,4))*6;
    int i = Integer.parseInt(ie.substring(2,3))*7;
    int j = Integer.parseInt(ie.substring(1,2))*8;
    int k = Integer.parseInt(ie.substring(0,1))*9;
    // Soma os valores
    int total = (d + e + f + g + h + i + j + k);
    // resto
    int inResto = (total % 11);
    int dig = 0;
    //
    if (inResto > 0) {
      int inT = (11 - inResto);
      dig = inT > 9 ? 0 : inT;
    } // if
    // Se o valor encontrado for igual ao dígito..
    return (dig == digito);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Mato Grosso.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Mato Grosso.
  */
  static public boolean MTVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() > 11) {
      return false;
    } else {
      // coloca zeros a esquerda
      while (ie.length() < 11){
       ie = "0" + ie;
      }
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(10,11));
    int b = Integer.parseInt(ie.substring(9,10))*2;
    int c = Integer.parseInt(ie.substring(8,9))*3;
    int d = Integer.parseInt(ie.substring(7,8))*4;
    int e = Integer.parseInt(ie.substring(6,7))*5;
    int f = Integer.parseInt(ie.substring(5,6))*6;
    int g = Integer.parseInt(ie.substring(4,5))*7;
    int h = Integer.parseInt(ie.substring(3,4))*8;
    int i = Integer.parseInt(ie.substring(2,3))*9;
    int j = Integer.parseInt(ie.substring(1,2))*2;
    int k = Integer.parseInt(ie.substring(0,1))*3;
    // Soma os valores
    int total = (b + c + d + e + f + g + h + i + j + k);
    // Calcula o dígito verificador
    int rest = (total % 11);
    int dig  = (11 - rest);
    // Se o dígito for igual a 0 ou 1...
    if ((rest == 0) || (rest == 1))
      dig = 0;
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Pará.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Pará.
  */
  static public boolean PAVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0,2));
    // Se os dois primeiros dígitos são diferentes de 15
    if (d1 != 15) {
      return false;
    } // if
    int a = Integer.parseInt(ie.substring(0,1));
    int b = Integer.parseInt(ie.substring(1,2));
    int c = Integer.parseInt(ie.substring(2,3));
    int d = Integer.parseInt(ie.substring(3,4));
    int e = Integer.parseInt(ie.substring(4,5));
    int f = Integer.parseInt(ie.substring(5,6));
    int g = Integer.parseInt(ie.substring(6,7));
    int h = Integer.parseInt(ie.substring(7,8));
    int i = Integer.parseInt(ie.substring(8,9));
    // calcula a soma com seus respectivos pesos..
    int soma = (9*a) + (8*b) + (7*c) + (6*d) + (5*e) + (4*f) + (3*g) + (2*h);
    // acha o resto da divisao da soma acima pelo Modulo 11/
    int resto = (soma % 11);
    int dig = 0;
    if (!((resto == 0) || (resto == 1))) {
      dig = (11 - resto);
    }
    return (dig == i);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Paraíba.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Paraíba.
  */
  static public boolean PBVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0,2));
    // Se os dois primeiros dígitos são diferentes de 16
    if (d1 != 16) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(7,8));
    int b = Integer.parseInt(ie.substring(6,7));
    int c = Integer.parseInt(ie.substring(5,6));
    int d = Integer.parseInt(ie.substring(4,5));
    int e = Integer.parseInt(ie.substring(3,4));
    int f = Integer.parseInt(ie.substring(2,3));
    int g = Integer.parseInt(ie.substring(1,2));
    int h = Integer.parseInt(ie.substring(0,1));
    int i = Integer.parseInt(ie.substring(8,9));
    int soma  = (9*h) + (8*g) + (7*f) + (6*e) + (5*d) + (4*c) + (3*b) + (2*a);
    int resto = 11 - (soma % 11);
    if ((resto == 10) || (resto == 11))
      resto = 0;
    return (resto == i);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Pernambuco.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Pernambuco.
  */
  static public boolean PEVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 14) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0,2));
    // Se os dois primeiros dígitos são diferentes de 18
    if (d1 != 18) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(13,14));
    int b = Integer.parseInt(ie.substring(12,13))*2;
    int c = Integer.parseInt(ie.substring(11,12))*3;
    int d = Integer.parseInt(ie.substring(10,11))*4;
    int e = Integer.parseInt(ie.substring(9,10))*5;
    int f = Integer.parseInt(ie.substring(8,9))*6;
    int g = Integer.parseInt(ie.substring(7,8))*7;
    int h = Integer.parseInt(ie.substring(6,7))*8;
    int i = Integer.parseInt(ie.substring(5,6))*9;
    int j = Integer.parseInt(ie.substring(4,5))*1;
    int k = Integer.parseInt(ie.substring(3,4))*2;
    int l = Integer.parseInt(ie.substring(2,3))*3;
    int m = Integer.parseInt(ie.substring(1,2))*4;
    int n = Integer.parseInt(ie.substring(0,1))*5;
    // Soma os valores
    int total = b + c + d + e + f + g + h + i + j + k + l + m + n;
    // Subtrai de 11 para encontrar o digito verificador
    int dig = 11 - (total % 11);
    // Se o valor encontrado for maior que 9...
    if (dig > 9)
      dig = dig - 10;
    //Se o valor encontrado for diferente do dígito...
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Piaui.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Piaui.
  */
  static public boolean PIVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*8;
    int i = Integer.parseInt(ie.substring(0,1))*9;
    //Soma os valores
    int total = b + c + d + e + f + g + h + i;
    // Calculamos o dígito
    int dig = 11 - (total % 11);
    // Se o valor encontrado for igual 10...
    if ((dig == 10) || (dig == 11))
      dig = 0;
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Piaui.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Piaui.
  */
  static public boolean PRVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 10) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*2;
    int i = Integer.parseInt(ie.substring(0,1))*3;
    // Soma os valores
    int total = b + c + d + e + f + g + h + i;
    // Armazenamos o resto da divisão
    int dig1 = 11 - (total % 11);
    // Se for igual a 10 ou 11...
    if ((dig1 == 10) || (dig1 == 11))
       dig1 = 0;
    // Calcula o produto dos valores recebidos para o segundo dígito
    int a1 = Integer.parseInt(ie.substring(9,10));
    int b1 = Integer.parseInt(ie.substring(8,9))*2;
    int c1 = Integer.parseInt(ie.substring(7,8))*3;
    int d1 = Integer.parseInt(ie.substring(6,7))*4;
    int e1 = Integer.parseInt(ie.substring(5,6))*5;
    int f1 = Integer.parseInt(ie.substring(4,5))*6;
    int g1 = Integer.parseInt(ie.substring(3,4))*7;
    int h1 = Integer.parseInt(ie.substring(2,3))*2;
    int i1 = Integer.parseInt(ie.substring(1,2))*3;
    int j1 = Integer.parseInt(ie.substring(0,1))*4;
    // Soma os valores
    total = b1 + c1 + d1 + e1 + f1 + g1 + h1 + i1 + j1;
    // Armazenamos o resto da divisão
    int dig2 = 11 - (total % 11);
    // Se for igual a 10 ou 11...
    if ((dig2 == 10) || (dig2 == 11))
       dig2 = 0;
    // Se o valor encontrado for igual ao dígito..
    return ((dig1 == a) && (dig2 == a1));
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Rio de Janeiro.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Rio de Janeiro.
  */
  static public boolean RJVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 8) {
      return false;
    } // if
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(7,8));
    int b = Integer.parseInt(ie.substring(6,7))*2;
    int c = Integer.parseInt(ie.substring(5,6))*3;
    int d = Integer.parseInt(ie.substring(4,5))*4;
    int e = Integer.parseInt(ie.substring(3,4))*5;
    int f = Integer.parseInt(ie.substring(2,3))*6;
    int g = Integer.parseInt(ie.substring(1,2))*7;
    int h = Integer.parseInt(ie.substring(0,1))*2;
    // Soma os valores
    int total = (b + c + d + e + f + g + h);
    // Calculamos o dígito
    int dig = (total % 11);
    // Se o resto for igual a 1...
    if (dig <= 1)
      dig = 0;
    else
      dig = (11 - dig);
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Rio Grande do Norte.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Rio Grande do Norte.
  */
  static public boolean RNVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0,2));
    // Se os dois primeiros dígitos são diferentes de 20
    if (d1 != 20) {
      return false;
    } // if
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*8;
    int i = Integer.parseInt(ie.substring(0,1))*9;
    // Soma os valores e multiplica por 10
    int total = (b + c + d + e + f + g + h + i) * 10;
    // Subtrai de 11 para encontrar o digito verificador
    int dig = (total % 11);
    // Se o valor encontrado for igual 10...
    if (dig == 10)
     dig = 0;
    // Se o valor encontrado for diferente do dígito...
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Rondonia.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Rondonia.
  */
  static public boolean ROVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if ((ie.length() != 9) && (ie.length() != 14)){
      return false;
    } // if

    // se a inscricao tiver o tamanho = 9 "anterior ao dia 01/08/2000"
    if ((ie.length()) == 9){
      ie = ie.substring(3);
      for (int i=0; i<8; i++)
        // acrecenta a quantidade de zeros necessarios
        ie = "0"+ie;
    }

    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(13, 14));
    int b = Integer.parseInt(ie.substring(12, 13))*2;
    int c = Integer.parseInt(ie.substring(11, 12))*3;
    int d = Integer.parseInt(ie.substring(10, 11))*4;
    int e = Integer.parseInt(ie.substring(9, 10))*5;
    int f = Integer.parseInt(ie.substring(8, 9))*6;
    int g = Integer.parseInt(ie.substring(7, 8))*7;
    int h = Integer.parseInt(ie.substring(6, 7))*8;
    int i = Integer.parseInt(ie.substring(5, 6))*9;
    int j = Integer.parseInt(ie.substring(4, 5))*2;
    int k = Integer.parseInt(ie.substring(3, 4))*3;
    int l = Integer.parseInt(ie.substring(2, 3))*4;
    int m = Integer.parseInt(ie.substring(1, 2))*5;
    int n = Integer.parseInt(ie.substring(0, 1))*6;
    // Soma os valores
    int total = (b + c + d + e + f + g + h + i + j + k + l + m + n);
    // Calculamos o dígito
    int dig = 11 - (total % 11);
    // Se o valor encontrado for igual 10...
    if ((dig == 10) || (dig == 11))
     dig = dig - 10;
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Roraima.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Roraima.
  */
  static public boolean RRVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*8;
    int c = Integer.parseInt(ie.substring(6,7))*7;
    int d = Integer.parseInt(ie.substring(5,6))*6;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*4;
    int g = Integer.parseInt(ie.substring(2,3))*3;
    int h = Integer.parseInt(ie.substring(1,2))*2;
    int i = Integer.parseInt(ie.substring(0,1))*1;
    // Soma os valores
    int total = (b + c + d + e + f + g + h + i);
    // Calculamos o dígito
    int dig = (total % 9);
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Rio Grande do Sul.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Rio Grande do Sul.
  */
  static public boolean RSVerifier(String ie){
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 10) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0, 3));
    // Se os dois primeiros dígitos são diferente de 12
    if ( (d1 <= 001) || (d1 >= 467)) {
      return false;
    } // if
    int a = Integer.parseInt(ie.substring(9,10));
    int b = Integer.parseInt(ie.substring(8,9))*2;
    int c = Integer.parseInt(ie.substring(7,8))*3;
    int d = Integer.parseInt(ie.substring(6,7))*4;
    int e = Integer.parseInt(ie.substring(5,6))*5;
    int f = Integer.parseInt(ie.substring(4,5))*6;
    int g = Integer.parseInt(ie.substring(3,4))*7;
    int h = Integer.parseInt(ie.substring(2,3))*8;
    int i = Integer.parseInt(ie.substring(1,2))*9;
    int j = Integer.parseInt(ie.substring(0,1))*2;
    // Soma os valores
    int total = (b + c + d + e + f + g + h + i + j);
    // Calcula o dígito verificador
    int dig = 11 - (total % 11);
    // Se o dígito for igual a 0 ou 1...
    if ((dig == 10) || (dig == 11))
      dig = 0;
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Santa Catarina.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Rio Santa Catarina.
  */
  static public boolean SCVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0, 2));
    // Se os dois primeiros dígitos são diferentes de 25
    if (d1 != 25) {
      return false;
    } // if
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*8;
    int i = Integer.parseInt(ie.substring(0,1))*9;
    // Soma os valores
    int total = b + c + d + e + f + g + h + i;
    // Calcula o resto
    int resto = (total % 11);
    int dig = 0;
    // Se o resto for igual a 0 ou 1...
    if (!((resto == 0) || (resto == 1)))
      // Calcula o dígito
      dig = 11 - resto;
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Sergipe.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Sergipe.
  */
  static public boolean SEVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if (ie.length() != 9) {
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0, 2));
    // Se os dois primeiros dígitos são diferentes de 27
    if (d1 != 27) {
      return false;
    } // if
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*8;
    int i = Integer.parseInt(ie.substring(0,1))*9;
    int total = b + c + d + e + f + g + h + i;
    // Calculamos o dígito
    int dig = 11 - (total % 11);
    // Se o valor encontrado for igual 10...
    if ((dig == 10) || (dig == 11))
      dig = 0;
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Sao Paulo.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Sao Paulo.
  */
  static public boolean SPVerifier(String ie) {
    if (!(ie.toUpperCase().substring(0,1).equals("P"))){
      // Se o número de digitos for diferente da insc.
      if (ie.length() != 12) {
        return false;
      } // if
      // Calcula o produto dos valores recebidos para o primeiro dígito
      int a = Integer.parseInt(ie.substring(8, 9));
      int b = Integer.parseInt(ie.substring(7, 8))*10;
      int c = Integer.parseInt(ie.substring(6, 7))*8;
      int d = Integer.parseInt(ie.substring(5, 6))*7;
      int e = Integer.parseInt(ie.substring(4, 5))*6;
      int f = Integer.parseInt(ie.substring(3, 4))*5;
      int g = Integer.parseInt(ie.substring(2, 3))*4;
      int h = Integer.parseInt(ie.substring(1, 2))*3;
      int i = Integer.parseInt(ie.substring(0, 1))*1;
      // Soma os valores
      int total = (b + c + d + e + f + g + h + i);
      // Armazenamos o resto da divisão
      int dig1 = (total % 11);
      int aux = Integer.toString(dig1).length();
      // Pegamos o dígito, que é o algarismo mais a direita do resto
      dig1 = Integer.parseInt(Integer.toString(dig1).substring(aux-1,aux));
      // Calcula o produto dos valores recebidos para o segundo dígito
      int a1 = Integer.parseInt(ie.substring(11, 12));
      int b1 = Integer.parseInt(ie.substring(10, 11))*2;
      int c1 = Integer.parseInt(ie.substring(9, 10))*3;
      int d1 = Integer.parseInt(ie.substring(8, 9))*4;
      int e1 = Integer.parseInt(ie.substring(7, 8))*5;
      int f1 = Integer.parseInt(ie.substring(6, 7))*6;
      int g1 = Integer.parseInt(ie.substring(5, 6))*7;
      int h1 = Integer.parseInt(ie.substring(4, 5))*8;
      int i1 = Integer.parseInt(ie.substring(3, 4))*9;
      int j1 = Integer.parseInt(ie.substring(2, 3))*10;
      int k1 = Integer.parseInt(ie.substring(1, 2))*2;
      int l1 = Integer.parseInt(ie.substring(0, 1))*3;
      // Soma os valores
      total = (b1 + c1 + d1 + e1 + f1 + g1 + h1 + i1 + j1 + k1 + l1);
      // Armazenamos o resto da divisão
      int dig2 = (total % 11);
      aux = Integer.toString(dig2).length();
      // Pegamos o dígito, que é o algarismo mais a direita do resto
      dig2 = Integer.parseInt(Integer.toString(dig2).substring(aux-1,aux));
      // Se o valor encontrado for igual ao dígito..
      return ((dig1 == a) && (dig2 == a1));
    } else {
      // Se o número de digitos for diferente da insc.
      if (ie.length() != 13) {
        return false;
      } // if
      int a1 = Integer.parseInt(ie.substring(9,10));
      int b1 = Integer.parseInt(ie.substring(8,9))*10;
      int c1 = Integer.parseInt(ie.substring(7,8))*8;
      int d1 = Integer.parseInt(ie.substring(6,7))*7;
      int e1 = Integer.parseInt(ie.substring(5,6))*6;
      int f1 = Integer.parseInt(ie.substring(4,5))*5;
      int g1 = Integer.parseInt(ie.substring(3,4))*4;
      int h1 = Integer.parseInt(ie.substring(2,3))*3;
      int i1 = Integer.parseInt(ie.substring(1,2))*1;
      // Soma os valores
      int total = (b1 + c1 + d1 + e1 + f1 + g1 + h1 + i1);
      // Armazenamos o resto da divisão
      int dig1 = (total % 11);
      int aux = Integer.toString(dig1).length();
      // Pegamos o dígito, que é o algarismo mais a direita do resto
      dig1 = Integer.parseInt(Integer.toString(dig1).substring(aux-1,aux));
      // Se o valor encontrado for igual ao dígito..
      return ((dig1 == a1));
    } // if
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Tocantins.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Tocantins.
  */
  static public boolean TOVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if ((ie.length() != 11) && (ie.length() != 9)){
      return false;
    } // if
    int d1 = Integer.parseInt(ie.substring(0, 2));
    // Se os dois primeiros dígitos são diferentes de 29
    if (d1 != 29) {
      return false;
    } // if
    // coloca valores nulos para continuar com o calculo
    if (ie.length() == 9)
      ie = "00" + ie;
    // Calcula o produto dos valores recebidos
    int a = Integer.parseInt(ie.substring(10, 11));
    int b = Integer.parseInt(ie.substring(9, 10))*2;
    int c = Integer.parseInt(ie.substring(8, 9))*3;
    int d = Integer.parseInt(ie.substring(7, 8))*4;
    int e = Integer.parseInt(ie.substring(6, 7))*5;
    int f = Integer.parseInt(ie.substring(5, 6))*6;
    int g = Integer.parseInt(ie.substring(2, 3))*7;
    int h = Integer.parseInt(ie.substring(1, 2))*8;
    int i = Integer.parseInt(ie.substring(0, 1))*9;
    // Soma os valores
    int total = (b + c + d + e + f + g + h + i);
    int dig = 0;
    // Se o resto da divisão for maior que 2
    if (((total % 11) > 2))
      dig = 11 - (total % 11);
    // Se o valor encontrado for igual ao dígito..
    return (dig == a);
  }

  /**
  * Verifica se 'IE' é valido para o Estado do Espirito Santo.
  * @param ie String Insrição Estadual passada pelo cliente.
  * @return boolean Verifica se 'IE' é valido para o Estado do Espirito Santo.
  */
  static public boolean ESVerifier(String ie) {
    // Se o número de digitos for diferente da insc.
    if ( (ie.length() != 9)) {
      return false;
    } // if
    int a = Integer.parseInt(ie.substring(8,9));
    int b = Integer.parseInt(ie.substring(7,8))*2;
    int c = Integer.parseInt(ie.substring(6,7))*3;
    int d = Integer.parseInt(ie.substring(5,6))*4;
    int e = Integer.parseInt(ie.substring(4,5))*5;
    int f = Integer.parseInt(ie.substring(3,4))*6;
    int g = Integer.parseInt(ie.substring(2,3))*7;
    int h = Integer.parseInt(ie.substring(1,2))*8;
    int i = Integer.parseInt(ie.substring(0,1))*9;
    // Soma os valores
    int total = b + c + d + e + f + g + h + i;
    // Calcula o resto
    int resto = (total % 11);
    int dig = 0;
    if (resto > 1)
      dig = (11 - resto);
    return (dig==a);
  }

}
