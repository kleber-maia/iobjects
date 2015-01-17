package iobjects.entity;

import iobjects.remote.*;
import java.io.IOException;

/**
 * Representa as op��es de pagina��o de uma consulta.
 */
public class Paginate implements Serializable {

  private int     pageNumber;
  private int     pageSize;
          boolean morePages;

  /**
   * Construtor padr�o
   */
  public Paginate() {
  }

  /**
   * Construtor padr�o.
   * @param pageSize int Tamanho da p�gina de resultados utilizada para pagina��o
   *                     da consulta. Se for igual a 0, n�o haver� pagina��o e
   *                     todos os registros ser�o retornados.
   * @param pageNumber int N�mero da p�gina de resultados que se deseja retornar, onde
   *                       a primeira p�gina � 0.
   * @return Paginate
   */
  public Paginate(int pageSize, int pageNumber) {
    this.pageSize   = pageSize;
    this.pageNumber = pageNumber;
  }

  public void deserialize(SerializeReader reader) throws IOException {
    this.pageSize   = reader.readInt();
    this.pageNumber = reader.readInt();
    this.morePages  = reader.readBoolean();
  }

  public int     getPageNumber() { return pageNumber; }
  public int     getPageSize()   { return pageSize;   }
  public boolean hasMorePages()  { return morePages;  }
  // *
  public void setPageNumber(int     pageNumber) { this.pageNumber = pageNumber; }
  public void setPageSize  (int     pageSize  ) { this.pageSize   = pageSize;   }

  public void serialize(SerializeWriter writer) throws IOException {
    writer.writeInt(pageSize);
    writer.writeInt(pageNumber);
    writer.writeBoolean(morePages);
  }
  
}
