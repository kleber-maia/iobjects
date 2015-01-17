package iobjects.util;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Classe utilit�ria respons�vel por ler um arquivo, recebido atrav�s de uma
 * requisi��o de um formul�rio HTML, e salv�-lo localmente.
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
   * 'request'. O formul�rio HTML deve conter seu m�todo (method) igual a 'POST'.
   * @param request HttpServletRequest contendo o(s) arquivo(s) que foram
   *                enviados ao servidor.
   * @throws IOException Em caso de exce��o na tentativa de ler/escrever da
   *                     requisi��o ou em disco.
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
