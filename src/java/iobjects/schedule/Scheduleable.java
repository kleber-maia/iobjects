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
package iobjects.schedule;

/**
 * Interface que deve ser implementada por um BusinessObject tornando-o capaz
 * de ser executado a partir de um trabalho agendado.
 * @since 2006
 */
public interface Scheduleable {

  /**
   * Representa o status de execução de um trabalho agendado.
   */
  public class RunStatus {

    private boolean sucess  = false;
    private String  message = "";

    /**
     * Construtor padrão.
     * @param sucess boolean True para informar que a execução do trabalho
     *               agendado foi bem sucedida ou false para informar que
     *               a execução falhou.
     * @param message String Mensagem para complementar o status de execução.
     */
    public RunStatus(boolean sucess,
                     String  message) {
      this.sucess = sucess;
      this.message = message;
    }

    public String getMessage() {
      return message;
    }

    public boolean getSucess() {
      return sucess;
    }

  }

  /**
   * Executa o trabalho agendado e retorna um RunStatus contendo o status
   * desta execução.
   * @return RunStatus Executa o trabalho agendado e retorna um RunStatus
   *         contendo o status desta execução.
   * @throws Exception Em caso de exceção na tentativa de executar a tarefa
   *                   agendada.
   */
  public RunStatus runScheduledTask() throws Exception;

}
