package iobjects.security;

import iobjects.*;

/**
 * Representa o serviço de demonstração da aplicação.
 * <b>Esta interface deve ser implementada por apenas uma classe descendente
 * de BusinessObject na aplicação.</b>
 */
public interface DemoService {

  /**
   * Requisita o acesso a demonstração para o visitante identificado por 'name' 
   * e 'email' e retorna a mensagem que será apresentada ao usuário.
   * @param name String Nome do visitante.
   * @param email String Email do visitante.
   * @return 
   * @throws Exception Em caso de exceção na tentativa de realizar a operação.
   */
  public String requestDemo(String name,
                            String email) throws Exception;

}
