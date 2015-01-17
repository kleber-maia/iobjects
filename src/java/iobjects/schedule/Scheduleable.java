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
