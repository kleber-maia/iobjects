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
package iobjects.ui.ajax;

import iobjects.*;

/**
 * Representa uma barra de progresso para indicar dinamicamente o andamento do 
 * processamento no lado servidor. O funcionamento consiste em enviar mensagens
 * periódicas ao objeto no servidor que está realizando o processamento e obter
 * o percentual do progresso alcançado.
 */
public class ProgressBar {

  /**
   * Interface que deve ser implementada pela classe que tiver a habilidade de
   * informar o progresso do processamento.
   */
  static public interface ProgressListener {
    /**
     * Deve retornar um valor entre 0 e 100 informando o percentual do progresso
     * alcançado.
     * @return Deve retornar um valor entre 0 e 100 informando o percentual do 
     *         progresso alcançado.
     */
    public byte onProgress();
  }

  /**
   * Intervalo, em milesegundos, em que o progresso do processamento é atualizado.
   */
  static public int PROGRESS_TIME_OUT = 5000;
  
  private Facade  facade           = null;
  private String  id               = "";
  private String  className        = "";
  private boolean isBusinessObject = false;
  
  /**
   * Construtor padrão.
   * @param facade Facade
   * @param id Identificação do ProgressBar na página.
   * @param className Nome da classe no servidor que implementa a interface
   *                  ProgressListener e que irá retornar o andamento do 
   *                  processamento.
   * @param isBusinessObject True para informar que a classe identificada em
   *                         'className' é descendente de BusinessObject.
   */
  public ProgressBar(Facade  facade,
                     String  id,
                     String  className,
                     boolean isBusinessObject) {
    // nossos valores
    this.facade = facade;
    this.id = id;
    this.className = className;
    this.isBusinessObject = isBusinessObject;
  }

  /**
   * Retorna o código JavaScript que inicia as chamadas periódicas ao servidor
   * para obter o progresso do processamento.
   * @return Retorna o código JavaScript que inicia as chamadas periódicas ao 
   *         servidor para obter o progresso do processamento.
   */
  public String progress() {
    return id + "GetProgress();";
  }
  
  /**
   * Retorna o script HTML representando o ProgressBar.
   * @param width Largura do ProgressBar ou 0 (zero) para que ele se auto ajuste
   *              ao seu container.
   * @param style Estilo de formatação do ProgressBar.
   * @param onProgressScript Código JavaScript para ser executado quando o 
   *                         ProgressBar for atualizado. Utilize a variável
   *                         local 'value' para ter acesso ao progresso atual
   *                         do processamento em percentual.
   * @param visible True para que o ProgressBar seja exibido na página ou False
   *                para que fique oculto até o início do processamento.
   * @return Retorna o script HTML representando o ProgressBar.
   */
  public String script(String  caption,
                       int     width,
                       String  style,
                       String  onProgressScript,
                       boolean visible) {
    // nosso objeto Ajax
    Ajax ajax = new Ajax(facade, id + "Ajax", className, isBusinessObject, "onProgress", new int[]{});
    // retorna
    return  "<script type=\"text/javascript\">" +
              "function " + id + "GetProgress() {" +
                ajax.request(new String[]{}, id + "GetProgressCallback") +
                "var _progress      = document.getElementById(\"" + id + "\");" +
                "var _progressLabel = document.getElementById(\"" + id + "Label\");" +
                "var _progressBar   = document.getElementById(\"" + id + "Bar\");" +
                "if (_progress.style.display != \"block\") {" +
                  "_progressBar.style.width = \"0%\";" +
                  "_progressLabel.innerHTML = \"0%\";" +
                  "_progress.style.display = \"block\";" +
                "}" +
              "}" +
              "function " + id + "GetProgressCallback() {" +
                "if (!" + ajax.isResponseReady() + ")" +
                  "return;" +
                "var _progressBar   = document.getElementById(\"" + id + "Bar\");" +
                "var _progressLabel = document.getElementById(\"" + id + "Label\");" +
                "if (" + ajax.isResponseStatusOK() + ") {" +
                  "var value = " + ajax.responseText() + ";" +
                  "_progressBar.style.width = value + \"%\";" +
                  "_progressLabel.innerHTML = value + \"%\";" +
                  onProgressScript +
                "}" +
                "else {" +
                "}" +
                "setTimeout(" + id + "GetProgress, " + PROGRESS_TIME_OUT + ");" +
              "}" +
            "</script>" +
            "<table id=\"" + id + "\" style=\"display:" + (visible ? "block" : "none") + "; width:" + (width > 0 ? width + "px" : "100%") + "; " + style + "\">" +
              "<tr>" +
                "<td>" +
                  caption +
                "</td>" +
                "<td id=\"" + id + "Label\" align=\"right\">" +
                  "0%" +
                "</td>" +
              "</tr>" +
              "<tr>" +
                "<td colspan=\"2\">" +
                  "<div class=\"frameCaption\" style=\"width:100%; height:18px;\">" +
                    "<div id=\"" + id + "Bar\" class=\"frameBar\" style=\"width:0%; height:100%;\"></div>" +
                  "</div>" +
                "</td>" +
              "</tr>" +
            "</table>";
  }
  
}
