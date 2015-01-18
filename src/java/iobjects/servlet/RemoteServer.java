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
package iobjects.servlet;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import iobjects.*;
import iobjects.misc.*;
import iobjects.remote.*;
import iobjects.schedule.*;
import iobjects.util.*;
import iobjects.xml.ui.*;

/**
 * Representa o Servlet RemoteServer da aplicação responável pela execução
 * de métodos remotos.
 */
public class RemoteServer extends HttpServlet {

  static public final String CLASS_NAME = "iobjects.servlet.RemoteServer";

  static public final String METHOD_LOGOFF = "logoff";
  static public final String METHOD_PING   = "ping";

  static public final String HTTP_HEADER_CONTENT_TYPE_BINARY = "text/plain;charset=UTF-8";

  /**
   * Processa as requisições Get recebidas.
   * @param request HttpServletRequest referente à requisição.
   * @param response HttpServletResponse referente à resposta.
   * @throws ServletException Em caso de exceção na tentativa de realizar uma
   *                          das operações de redirecionamento e/ou execução
   *                          de comandos.
   * @throws IOException Em caso de exceção na tentativa de realizar uma
   *                     das operações de redirecionamento e/ou execução
   *                     de comandos.
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * Processa as requisições Post recebidas.
   * @param request HttpServletRequest referente à requisição.
   * @param response HttpServletResponse referente à resposta.
   * @throws ServletException Em caso de exceção na tentativa de realizar uma
   *                          das operações de redirecionamento e/ou execução
   *                          de comandos.
   * @throws IOException Em caso de exceção na tentativa de realizar uma
   *                     das operações de redirecionamento e/ou execução
   *                     de comandos.
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      // obtém os dados
      InputStream inputStream = request.getInputStream();
      // obtém a estrutura do método remoto
      RemoteMethod remoteMethod = (RemoteMethod)Serializer.deserialize(inputStream);

      // verifica a configuração de trabalho para esta instância da aplicação
      Controller.checkWorkConfiguration(request, remoteMethod.getWorkConfiguration());

      // nosso resultado
      Object result = null;

      // se quer efetuar um ping...
      if (remoteMethod.getClassName().equals(CLASS_NAME) && remoteMethod.getMethodName().equals(METHOD_PING)) {
        // retorna a mesma chave recebida
        result = remoteMethod.getParameterValues()[0];
      }
      // se quer efetuar logoff...
      else if (remoteMethod.getClassName().equals(CLASS_NAME) && remoteMethod.getClassName().equals(METHOD_LOGOFF)) {
        // verifica o Facade
        Facade facade = Controller.getFacade(request);
        // efetua logoff
        facade.logoff();
      }
      // se quer outro método...
      else {
        // verifica o Facade
        Facade facade = Controller.getFacade(request);
        // se mudou o nome do usuário...logoff
        if ((facade.getLoggedUser() != null) && (!facade.getLoggedUser().getName().equals(remoteMethod.getUsername())))
          facade.logoff();
        // se não fez logon...faz
        if (facade.getLoggedUser() == null)
          facade.logon(remoteMethod.getDefaultConnectionName(), remoteMethod.getUsername(), remoteMethod.getPassword());
        // define os valores da relação mestre
        if (remoteMethod.getMasterRelationValues().length > 0)
          facade.masterRelation().setValues(remoteMethod.getMasterRelationValues());
        if (remoteMethod.getMasterRelationUserValues().length > 0)
          facade.masterRelation().setUserValues(remoteMethod.getMasterRelationUserValues());

        // obtém o objeto de negócio desejado
        BusinessObject businessObject = facade.getBusinessObject(remoteMethod.getClassName());
        // se não encontramos objeto...exceção
        if (businessObject == null)
          throw new ExtendedException(getClass().getName(), "doPost", "Objeto não encontrado: " + remoteMethod.getClassName() + ".");

        // obtém o método no objeto de negócio que iremos executar
        Class[] parameterTypes = new Class[remoteMethod.getParameterValues().length];
        for (int i=0; i<parameterTypes.length; i++) {
          Class type = remoteMethod.getParameterValues()[i].getClass();
          if (type.isAssignableFrom(Boolean.class))
            parameterTypes[i] = boolean.class;
          else if (type.isAssignableFrom(Integer.class))
            parameterTypes[i] = int.class;
          else if (type.isAssignableFrom(Double.class))
            parameterTypes[i] = double.class;
          else
            parameterTypes[i] = type;
        } // for
        Method businessObjectMethod = businessObject.getClass().getMethod(remoteMethod.getMethodName(), parameterTypes);

        try {
          // invoca o método e retorna o resultado
          result = businessObjectMethod.invoke(businessObject, remoteMethod.getParameterValues());
        }
        catch (InvocationTargetException e) {
          // imprime a exceção
          e.printStackTrace();
          // repassa a exceção original da classe RemoteException
          throw (Exception)e.getTargetException();
        } // try-catch
      } // if

      // stream temporário para a resposta
      ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
      // serializa a resposta na memória
      Serializer.serialize(result, memoryStream);
      // libera parte da memória
      result = null;
      System.gc();
      // preenche nossa resposta
      response.setContentType(HTTP_HEADER_CONTENT_TYPE_BINARY);
      response.setStatus(response.SC_ACCEPTED);
      response.setContentLength(memoryStream.size());
      response.getOutputStream().write(memoryStream.toByteArray());
      // libera o restante da memória
      memoryStream.reset();
      memoryStream = null;
      System.gc();
    }
    catch (Exception e) {
      try {
        // serializa a exceção genérica
        response.reset();
        response.setContentType(HTTP_HEADER_CONTENT_TYPE_BINARY);
        response.setStatus(response.SC_ACCEPTED);
        Serializer.serialize(new RemoteException(e), response.getOutputStream());
      }
      catch (Exception ex) {
        response.reset();
        response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
      } // try-catch
    } // try-catch
  }

}
