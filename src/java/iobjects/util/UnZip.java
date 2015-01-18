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
import java.util.zip.*;

import iobjects.*;

/**
 * Implementa as rotinas de descompress�o de um arquivo no formato ZIP.
 */
public class UnZip {

  private boolean   denyDirs    = false;
  private String[]  fileNames   = {};
  private String    targetPath  = "";
  private ZipFile   zipFile     = null;
  private String    zipFileName = "";
  // *
  private final int BUFFER = 2048;
  private final int EOF    = -1;

  /**
   * Construtor padr�o.
   * @param zipFileName Nome do arquivo ZIP para descomprimir.
   * @param targetPath Caminho de destino para os arquivos extra�dos.
   * @throws Exception Em caso de exce��o na tentativa de abrir ou extrair o
   *                   conte�do do arquivo Zip.
   */
  public UnZip(String zipFileName, String targetPath) throws Exception {
    // nossos valores
    this.zipFileName = zipFileName;
    this.targetPath  = targetPath;
    // extrai
    unZip();
  }

  /**
   * Construtor padr�o.
   * @param zipFileName Nome do arquivo ZIP para descomprimir.
   * @param targetPath Caminho de destino para os arquivos extra�dos.
   * @param denyDirs True para impedir a extra��o de pastas contidas no arquivo ZIP.
   * @throws Exception Em caso de exce��o na tentativa de abrir ou extrair o
   *                   conte�do do arquivo Zip.
   */
  public UnZip(String zipFileName, String targetPath, boolean denyDirs) throws Exception {
    // nossos valores
    this.zipFileName = zipFileName;
    this.targetPath  = targetPath;
    this.denyDirs    = denyDirs;
    // extrai
    unZip();
  }

  /**
   * Extrai o arquivo identificado por zipEntry.
   * @param zipEntry Entrada em zipFile para ser extraida.
   * @throws Exception Em caso de exce��o na tentativa de cria��o e escrita
   *                   do arquivo descompactado.
   */
  private void extract(ZipEntry zipEntry) throws Exception {
    // cria o arquivo de destino
    File file = new File(targetPath + zipEntry.getName());
    // se � um diret�rio...
    if (zipEntry.isDirectory()) {
      // se podemos permitir...cria
      if (!denyDirs)
        file.mkdirs();
    }
    // se � um arquivo...
    else {
      // diret�rio onde o arquivo ser� extraido
      File directory = new File(file.getParent());
      // se n�o podemos permitir diret�rios...dispara
      if (denyDirs && !directory.exists())
        return;
      // cria o caminho completo
      directory.mkdirs();
      // conte�do compactado
      InputStream inputStream = zipFile.getInputStream(zipEntry);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
      // descompactando
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
      byte[] data = new byte[BUFFER];
      int count = -1;
      while ((count = bufferedInputStream.read(data, 0, BUFFER)) != EOF)
        bufferedOutputStream.write(data, 0, count);
      bufferedOutputStream.close();
      fileOutputStream.close();
    } // if
  }

  /**
   * Retorna os nomes dos arquivos extra�dos.
   * @return Retorna os nomes dos arquivos extra�dos.
   */
  public String[] fileNames() {
    return fileNames;
  }
  
  /**
   * Realiza a descompress�o do arquivo.
   */
  private void unZip() throws Exception {
    // arquivos extra�dos
    Vector vector = new Vector();
    // cria nosso arquivo Zip
    zipFile = new ZipFile(zipFileName);
    try {
      // se n�o informou o diret�rio de destino...exce��o
      if (targetPath.equals(""))
        throw new ExtendedException(getClass().getName(), "", "Caminho de destino n�o informado.");
      else
        this.targetPath = FileTools.includeSeparatorChar(targetPath);
      // cria o diret�rio de destino
      File directory = new File(targetPath);
      directory.mkdirs();
      // loop no conte�do do Zip
      Enumeration zipEntries = zipFile.entries();
      while(zipEntries.hasMoreElements()) {
        // entrada da vez
        ZipEntry zipEntry = (ZipEntry)zipEntries.nextElement();
        // extrai a entrada
        extract(zipEntry);
        // guarda o nome do arquivo
        vector.add(targetPath + zipEntry.getName());
      } // while
      // guarda os nomes dos arquivos extra�dos
      fileNames = new String[vector.size()];
      vector.copyInto(fileNames);
    }
    finally {
      // fecha o arquivo
      zipFile.close();
    } // try-finally
  }
  
}
