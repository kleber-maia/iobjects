package iobjects.remote;

import java.io.*;

/**
 * Interface que deve ser implementada pelas classes que terão a capacidade
 * de ser serializadas e deserializadas.
 * <b>A classe que implementar esta interface deve possuir um construtor
 * 'default' (sem parâmetros).</b>
 */
public interface Serializable {

  /**
   * Implementa a deserialização da classe que implementa esta interface.
   * @param reader SerializeReader para leitura das propriedades gravadas
   *              a partir do método serialize().
   * @exception IOException Em caso de exceção na tentativa de ler de 'input'.
   */
  public void deserialize(SerializeReader reader) throws IOException;

  /**
   * Implementa a serialização da classe que implementa esta interface.
   * @param writer SerializeWriter para escrita das propriedades para serem
   *               posteriormente lidas a partir do método deserialize().
   * @exception IOException Em caso de exceção na tentativa de escrever em 'output'.
   */
  public void serialize(SerializeWriter writer) throws IOException;

}
