package iobjects.security;

import iobjects.*;

/**
 * Representa o servi�o de demonstra��o da aplica��o.
 * <b>Esta interface deve ser implementada por apenas uma classe descendente
 * de BusinessObject na aplica��o.</b>
 */
public interface DemoService {

  /**
   * Requisita o acesso a demonstra��o para o visitante identificado por 'name' 
   * e 'email' e retorna a mensagem que ser� apresentada ao usu�rio.
   * @param name String Nome do visitante.
   * @param email String Email do visitante.
   * @return 
   * @throws Exception Em caso de exce��o na tentativa de realizar a opera��o.
   */
  public String requestDemo(String name,
                            String email) throws Exception;

}
