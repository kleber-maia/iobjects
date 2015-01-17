package iobjects.entity;

import iobjects.remote.*;
import java.io.IOException;

/**
 * Representa as opções de paginação de uma consulta.
 */
public class Paginate implements Serializable {

  private int     pageNumber;
  private int     pageSize;
          boolean morePages;

  /**
   * Construtor padrão
   */
  public Paginate() {
  }

  /**
   * Construtor padrão.
   * @param pageSize int Tamanho da página de resultados utilizada para paginação
   *                     da consulta. Se for igual a 0, não haverá paginação e
   *                     todos os registros serão retornados.
   * @param pageNumber int Número da página de resultados que se deseja retornar, onde
   *                       a primeira página é 0.
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
