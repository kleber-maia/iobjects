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
package iobjects.util;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Classe utilitária responsável por ler um arquivo, recebido através de uma
 * requisição de um formulário HTML, e salvá-lo localmente.
 */
public class Uploader {

  private String savePath, filepath, filename, contentType;
  private Dictionary fields;

  public String getFilename() {
    return filename;
  }

  public String getFilepath() {
    return filepath;
  }

  public void setSavePath(String savePath) {
    this.savePath = savePath;
  }

  public String getContentType() {
    return contentType;
  }

  public String getFieldValue(String fieldName) {
    if (fields == null || fieldName == null)
      return null;
    return (String) fields.get(fieldName);
  }

  private void setFilename(String s) {
    if (s==null)
      return;

    int pos = s.indexOf("filename=\"");
    if (pos != -1) {
      filepath = s.substring(pos+10, s.length()-1);
      pos = filepath.lastIndexOf("\\");
      if (pos != -1)
        filename = filepath.substring(pos + 1);
      else
        filename = filepath;
    } // if
  }

  private void setContentType(String s) {
    if (s==null)
      return;

    int pos = s.indexOf(": ");
    if (pos != -1)
      contentType = s.substring(pos+2, s.length());
  }

  /**
   * Faz o "upload", salva o arquivo localmente, dos arquivos existentes em
   * 'request'. O formulário HTML deve conter seu método (method) igual a 'POST'.
   * @param request HttpServletRequest contendo o(s) arquivo(s) que foram
   *                enviados ao servidor.
   * @throws IOException Em caso de exceção na tentativa de ler/escrever da
   *                     requisição ou em disco.
   */
  public void doUpload(HttpServletRequest request) throws IOException {
    ServletInputStream in = request.getInputStream();

    byte[] line = new byte[128];
    int i = in.readLine(line, 0, 128);
    if (i < 3)
      return;
    int boundaryLength = i - 2;

    String boundary = new String(line, 0, boundaryLength);
    fields = new Hashtable();

    while (i != -1) {
      String newLine = new String(line, 0, i);
      if (newLine.startsWith("Content-Disposition: form-data; name=\"")) {
        if (newLine.indexOf("filename=\"") != -1) {
          setFilename(new String(line, 0, i-2));
          if (filename==null)
            return;
          i = in.readLine(line, 0, 128);
          setContentType(new String(line, 0, i-2));
          i = in.readLine(line, 0, 128);
          i = in.readLine(line, 0, 128);
          newLine = new String(line, 0, i);
          PrintWriter pw = new PrintWriter(new BufferedWriter(new
              FileWriter((savePath==null? "" : savePath) + filename)));
          while (i != -1 && !newLine.startsWith(boundary)) {
            i = in.readLine(line, 0, 128);
            if ((i==boundaryLength+2 || i==boundaryLength+4) // + 4 is eof
                && (new String(line, 0, i).startsWith(boundary)))
              pw.print(newLine.substring(0, newLine.length()-2));
            else
              pw.print(newLine);
            newLine = new String(line, 0, i);
          }
          pw.close();
        }
        else {
          int pos = newLine.indexOf("name=\"");
          String fieldName = newLine.substring(pos+6, newLine.length()-3);
          i = in.readLine(line, 0, 128);
          i = in.readLine(line, 0, 128);
          newLine = new String(line, 0, i);
          StringBuffer fieldValue = new StringBuffer(128);
          while (i != -1 && !newLine.startsWith(boundary)) {
            i = in.readLine(line, 0, 128);
            if ((i==boundaryLength+2 || i==boundaryLength+4) // + 4 is eof
                && (new String(line, 0, i).startsWith(boundary)))
              fieldValue.append(newLine.substring(0, newLine.length()-2));
            else
              fieldValue.append(newLine);
            newLine = new String(line, 0, i);
          }
          fields.put(fieldName, fieldValue.toString());
        } // if
      } // if
      i = in.readLine(line, 0, 128);
    } // while
  }

}
