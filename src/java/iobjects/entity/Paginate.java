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
