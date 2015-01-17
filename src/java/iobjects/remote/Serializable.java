package iobjects.remote;

import java.io.*;

/**
 * Interface que deve ser implementada pelas classes que ter�o a capacidade
 * de ser serializadas e deserializadas.
 * <b>A classe que implementar esta interface deve possuir um construtor
 * 'default' (sem par�metros).</b>
 */
public interface Serializable {

  /**
   * Implementa a deserializa��o da classe que implementa esta interface.
   * @param reader SerializeReader para leitura das propriedades gravadas
   *              a partir do m�todo serialize().
   * @exception IOException Em caso de exce��o na tentativa de ler de 'input'.
   */
  public void deserialize(SerializeReader reader) throws IOException;

  /**
   * Implementa a serializa��o da classe que implementa esta interface.
   * @param writer SerializeWriter para escrita das propriedades para serem
   *               posteriormente lidas a partir do m�todo deserialize().
   * @exception IOException Em caso de exce��o na tentativa de escrever em 'output'.
   */
  public void serialize(SerializeWriter writer) throws IOException;

}
