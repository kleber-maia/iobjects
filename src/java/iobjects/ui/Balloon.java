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
package iobjects.ui;

import javax.servlet.http.*;

import iobjects.*;
import iobjects.servlet.*;
import iobjects.util.*;
import iobjects.ui.ajax.*;

/**
 * Utilitário para criação de balões de exibição de informações em destaque
 * para o usuário. É possível exibir informações pontuais ou seqüenciais como
 * uma apresentação interativa.
 */
public class Balloon {

  static private final String COMMAND_DONT_SHOW_ME_THIS_AGAIN          = "dontShowMeThisAgain";
  static private final String PARAM_BALLOON_ID                         = "balloonId";
  static private final String PARAM_DONT_SHOW_ME_THIS_AGAIN_TEST_VALUE = "dontShowMeThisAgainTestValue";

  private String             actionName                   = "";
  private boolean            dontShowMe                   = false;
  private boolean            dontShowMeThisAgain          = false;
  private String             dontShowMeThisAgainTestValue = "";
  private Facade             facade                       = null;
  private String             id                           = "";
  private HttpServletRequest request                      = null;
  private boolean            standAlone                   = false;
  private int                stepCount                    = 0;

  /**
   * @deprecated
   */
  public Balloon() {
  }

  /**
   * Construtor padrão.
   * @param facade Facade Fachada.
   * @param request HttpServletRequest Requisição.
   * @param id String Identificação do Balloon na página.
   */
  public Balloon(Facade             facade,
                 HttpServletRequest request,
                 String             id) {
    // nossos valores
    this.facade = facade;
    this.request = request;
    this.id = id;
    // obtém o nome do Action da requisição
    this.actionName = request.getParameter(Controller.ACTION);

    // se está executado COMMAND_DONT_SHOW_ME_THIS_AGAIN
    if (Controller.getCommand(request).equals(COMMAND_DONT_SHOW_ME_THIS_AGAIN)) {
      // se somos o Balloon de destino...
      if (request.getParameter(PARAM_BALLOON_ID).equals(id)) {
        // Param em Facade que guarda o nosso valor de teste
        Param param = facade.userParamList().get(id);
        // se ainda não temos...cria
        if (param == null) {
          // cria
          param = new Param(id, request.getParameter(PARAM_DONT_SHOW_ME_THIS_AGAIN_TEST_VALUE));
          // adiciona
          facade.userParamList().add(param);
        }
        // se já temos...atualiza seu valor
        else {
          param.setValue(request.getParameter(PARAM_DONT_SHOW_ME_THIS_AGAIN_TEST_VALUE));
        } // if
      } // if
    } // if
  }

  /**
   * @deprecated
   * @param id String
   * @param testValue String
   */
  public void dontShowMeThisAgain(String id,
                                  String testValue) {
    // Param em Facade que guarda o nosso valor de teste
    Param param = facade.userParamList().get(id);
    // se ainda não temos...cria
    if (param == null) {
      // cria
      param = new Param(id, testValue);
      // adiciona
      facade.userParamList().add(param);
    }
    // se já temos...atualiza seu valor
    else {
      param.setValue(testValue);
    } // if
  }

  /**
   * Retorna a representação HTML do início do roteiro com o Balloon.
   * <b>Este método deve ser chamado no corpo da página, internamente à tag
   * &lt;body&gt;&lt;/body&gt;, caso contrário o Balloon não será exibido no
   * local correto.<b>
   * @return String Retorna a representação HTML do início do roteiro com o Balloon.
   */
  public String scriptBegin() {
    return scriptBegin(false, "");
  }

  /**
   * Retorna a representação HTML do início do roteiro com o Balloon.
   * <b>Este método deve ser chamado no corpo da página, internamente à tag
   * &lt;body&gt;&lt;/body&gt;, caso contrário o Balloon não será exibido no
   * local correto.<b>
   * @param dontShowMeThisAgain boolean True para que seja exibida a caixa de checagem
   *                            com a opção "Não me mostre isso novamente".
   *                            <b>Apenas o último Balloon do roteiro exibirá a
   *                            caixa de checagem.</b>
   * @param dontShowMeThisAgainTestValue String Este valor é testado sempre antes
   *                                     de o Balloon ser exibido. O Balloon somente
   *                                     será exibido se o usuário não tiver marcado
   *                                     a caixa de checagem ou se este valor tiver
   *                                     mudado desde a última vez que o usuário
   *                                     marcou a caixa de checagem.
   * @return String Retorna a representação HTML do início do roteiro com o Balloon.
   */
  public String scriptBegin(boolean dontShowMeThisAgain,
                            String  dontShowMeThisAgainTestValue) {
    // nossos valores
    this.dontShowMeThisAgain          = dontShowMeThisAgain;
    this.dontShowMeThisAgainTestValue = dontShowMeThisAgainTestValue;
    // devemos parar com a exibição do roteiro?
    boolean stop = false;
    // se temos controle de exibição...
    if (dontShowMeThisAgain) {
      // Param em Facade que guarda o nosso valor de teste
      Param param = facade.userParamList().get(id);
      // devemos parar com o roteiro?
      stop = (param != null) && (param.getValue().equals(dontShowMeThisAgainTestValue));
    } // if

    // nosso resultado
    StringBuffer result = new StringBuffer();
    // nosso Balloon
    result.append("<div id=\"" + id + "\" class=\"balloon\" onclick=\"Balloon_hide('" + id + "'); if (this.myClick != '') eval(this.myClick);\" style=\"position:absolute; visibility:hidden;\">"
                +   "<table cellspacing=\"0\" cellpadding=\"0\">"
                +     "<tr>"
                +       "<td align=\"right\" valign=\"bottom\"><img src=\"images/balloon/topleft.gif\" /></td>"
                +       "<td style=\"background-image:url('images/balloon/top.gif'); background-repeat:repeat-x; background-position:bottom;\"><img id=\"" + id + "AnchorTop\" src=\"images/balloon/anchortop.gif\" style=\"visibility:hidden;\" /></td>"
                +       "<td align=\"left\" valign=\"bottom\"><img src=\"images/balloon/topright.gif\" /></td>"
                +     "</tr>"
                +     "<tr>"
                +       "<td style=\"background-image:url('images/balloon/left.gif'); background-repeat:repeat-y; background-position:right;\"></td>"
                +       "<td style=\"background-image:url('images/balloon/middle.gif');\">"
                +         "<table cellpadding=\"2\" style=\"color:#000000;\">"
                +           "<tr>"
                +             "<td id=\"" + id + "Icon\" class=\"BalloonIcon\"></td>"
                +             "<td id=\"" + id + "Caption\" class=\"BalloonCaption\" valign=\"middle\" style=\"font-weight:bold;\"></td>"
                +           "</tr>"
                +           "<tr>"
                +             "<td></td>"
                +             "<td id=\"" + id + "Text\" class=\"BalloonText\"></td>"
                +           "</tr>"
                +           "<tr>"
                +             "<td></td>"
                +             "<td id=\"" + id + "Extra\" class=\"BalloonExtra\"></td>"
                +           "</tr>"
                +         "</table>"
                +       "</td>"
                +       "<td style=\"background-image:url('images/balloon/right.gif'); background-repeat:repeat-y; background-position:left;\"></td>"
                +     "</tr>"
                +     "<tr >"
                +       "<td align=\"right\" valign=\"top\"><img src=\"images/balloon/bottomleft.gif\" /></td>"
                +       "<td style=\"background-image:url('images/balloon/bottom.gif'); background-repeat:repeat-x; background-position:top;\"><img id=\"" + id + "AnchorBottom\" src=\"images/balloon/anchorbottom.gif\" style=\"visibility:hidden;\" /></td>"
                +       "<td align=\"left\" valign=\"top\"><img src=\"images/balloon/bottomright.gif\" /></td>"
                +     "</tr>"
                +   "</table>"
                + "</div>"
                );
    // se temos controle de exibição...adiciona nosso suporte Ajax
    if (dontShowMeThisAgain) {
      Ajax ajax = new Ajax(facade,
                           id + "Ajax",
                           getClass().getName(),
                           false,
                           "dontShowMeThisAgain",
                           new int[]{Ajax.PARAMETER_TYPE_STRING,
                                     Ajax.PARAMETER_TYPE_STRING});
      result.append("<script type=\"text/javascript\">"
                    // variável de controle de roteiro
                 + "var " + id + "Stop = " + stop +";"
                    // função para não mostrar novamente e parar com o roteiro
                  + "function " + id + "DontShowMeThisAgain() {"
                       // faz a requisição Ajax
                  +    ajax.request(new String[]{"\"" + id + "\"", "\"" + dontShowMeThisAgainTestValue + "\""})
                       // se foi bem sucedido...pára com a execução do roteiro
                  +   "if (" + ajax.isResponseStatusOK() + ") {"
                  +     id + "Stop = true;"
                       // se não foi...mostra o erro
                  +   "} else {"
                  +     "alert(" + ajax.responseErrorMessage() + ");"
                  +   "}"
                  + "}");
    } // if
    // retorna
    return result.toString();
  }

  /**
   * Retorna a representação HTML do botão que inicia o roteiro mesmo após o
   * usuário tê-lo parado.
   * @return String Retorna a representação HTML do botão que inicia o roteiro
   *                mesmo após o usuário tê-lo parado.
   */
  public String scriptButton() {
    return Button.script(facade,
                         id + "Button",
                         "",
                         "Inicia a apresentação interativa.",
                         ImageList.IMAGE_BALLOON,
                         "",
                         Button.KIND_TOOLBUTTON,
                         "width:24px; height:23px;",
                         id + "Stop=false; " + id + "Step=0; " + id + "NextStep();",
                         false);
  }

  /**
   * Retorna a representação HTML de uma etapa do roteiro com o Balloon.
   * @param icon String Caminho da imagem para ser exibida indicando o tipo da
   *             mensagem. <b>É recomendado o uso do ImageList.</b>
   * @param caption String Título do Balloon.
   * @param text String Texto do Balloon.
   * @param anchorID String Identificação do objeto ao qual o Balloon será ancorado.
   *                        Se nada for informado ou se o objeto não for encontrado,
   *                        o Balloon será apresentado no centro da tela.
   * @param onClickScript String Script JavaScript para ser executado pelo Balloon.
   * @return String
   */
  public String scriptStep(String  icon,
                           String  caption,
                           String  text,
                           String  anchorID,
                           String  onClickScript) {
    // se não devemos ser exibidos...retorna nada
    if (dontShowMe)
      return "";
    // se não somos StandAlone...
    if (!standAlone) {
      // incrementa nosso contador de etapas
      stepCount++;
      // o evento OnClick exibirá o próximo balão
      onClickScript += id + "NextStep();";
    } // if
    // exibe o balão com o ícone, texto e demais opções desejadas
    return (!standAlone ? "function " + id + "Step" + stepCount + "() {" : "")
         + "if (!" + id + "Stop)"
         +   "Balloon_show(\"" + id + "\", \"" + anchorID + "\", \"" + icon + "\", \"" + caption + "\", \"" + text + "\", \"" + onClickScript + "\", " + dontShowMeThisAgain /*+ (!standAlone ? " && (" + id + "Step == " + id + "StepCount)" : "")*/ + ");"
         + (!standAlone ? "}" : "");
  }

  /**
   * Retorna a representação HTML do término do roteiro com o Balloon.
   * @return String Retorna a representação HTML do término do roteiro com o Balloon.
   */
  public String scriptEnd() {
    // se não devemos ser exibidos...retorna nada
    if (dontShowMe)
      return "";
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // se não somos StandAlone...
    if (!standAlone) {
      // função para próxima etapa
      result.append("function " + id + "NextStep() {"
                  + "  if (" + id + "Step < " + id + "StepCount) {"
                  + "    " + id + "Step++;"
                  + "    var funcName = \"" + id + "Step\" + " + id + "Step + \"();\";"
                  + "    eval(funcName);"
                  + "  }"
                  + "}");
      // variável com a qde de etapas
      result.append("var " + id + "StepCount = " + stepCount + ";");
      // variável de controle da etapa atual
      result.append("var " + id + "Step = 0;");
      // mostra a primeira etapa
      result.append(id + "NextStep();");
    }
    else {
      // variável com a qde de etapas
      result.append("var " + id + "StepCount = 0;");
    } // if
    // finaliza
    result.append("</script>");
    // retorna
    return result.toString();
  }

  /**
   * @deprecated
   * @param facade Facade
   */
  public void setFacade(Facade facade) {
    this.facade = facade;
  }

  /**
   * Retorna a representação HTML do Balloon para ser exibido separadamente.
   * @param icon String Caminho da imagem para ser exibida indicando o tipo da
   *             mensagem. <b>É recomendado o uso do ImageList.</b>
   * @param caption String Título do Balloon.
   * @param text String Texto do Balloon.
   * @param anchorID String Identificação do objeto ao qual o Balloon será ancorado.
   *                        Se nada for informado ou se o objeto não for encontrado,
   *                        o Balloon será apresentado no centro da tela.
   * @param onClickScript String Script JavaScript para ser executado pelo Balloon.
   * @return String
   */
  public String standAlone(String  icon,
                           String  caption,
                           String  text,
                           String  anchorID,
                           String  onClickScript) {
    return standAlone(icon, caption, text, anchorID, onClickScript, false, "");
  }

  /**
   * Retorna a representação HTML do Balloon para ser exibido separadamente.
   * @param icon String Caminho da imagem para ser exibida indicando o tipo da
   *             mensagem. <b>É recomendado o uso do ImageList.</b>
   * @param caption String Título do Balloon.
   * @param text String Texto do Balloon.
   * @param anchorID String Identificação do objeto ao qual o Balloon será ancorado.
   *                        Se nada for informado ou se o objeto não for encontrado,
   *                        o Balloon será apresentado no centro da tela.
   * @param onClickScript String Script JavaScript para ser executado pelo Balloon.
   * @param dontShowMeThisAgain boolean True para que seja exibida a caixa de checagem
   *                            com a opção "Não me mostre isso novamente".
   * @param dontShowMeThisAgainTestValue String Este valor é testado sempre antes
   *                                     de o Balloon ser exibido. O Balloon somente
   *                                     será exibido se o usuário não tiver marcado
   *                                     a caixa de checagem ou se este valor tiver
   *                                     mudado desde a última vez que o usuário
   *                                     marcou a caixa de checagem.
   * @return String
   */
  public String standAlone(String  icon,
                           String  caption,
                           String  text,
                           String  anchorID,
                           String  onClickScript,
                           boolean dontShowMeThisAgain,
                           String  dontShowMeThisAgainTestValue) {
    // nossos valores
    this.standAlone = true;
    // retorna
    return scriptBegin(dontShowMeThisAgain, dontShowMeThisAgainTestValue)
         + scriptStep(icon, caption, text, anchorID, onClickScript)
         + scriptEnd();
  }

}
