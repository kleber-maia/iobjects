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

/**
 * Implementa as rotinas de compressão de um arquivo no formato ZIP.
 */
public class Zip {

  private ZipOutputStream zipOutputStream;
  // *
  private final int BUFFER = 2048;
  private final int EOF    = -1;

  /**
   * Construtor padrão. Comprime os arquivos referenciados por 'files' em
   * 'zipFileName' desprezando informações de diretórios.
   * @param zipFileName Nome do arquivo ZIP que será gerado.
   * @param files File[] referente aos arquivos que serão incluídos no arquivo ZIP.
   * @throws Exception Em caso de exceção na tentativa de criar o arquivo Zip.
   */
  public Zip(String zipFileName, File[] files) throws Exception {
    // certificando que o caminho do arquivo Zip existe
    String zipPathName = zipFileName.substring(0, zipFileName.lastIndexOf(File.separatorChar));
    File zipDir = new File(zipPathName);
    zipDir.mkdirs();
    // nosso arquivo de saída
    FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
    // nossa saida comprimida
    zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
    zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
    for (File file : files) {
      compress(file);
    } // for
    // fecha o arquivo zip
    zipOutputStream.close();
  }

  /**
   * Comprime o arquivo informado por 'file'.
   * @param file File Arquivo para ser comprimido e incluído no ZIP.
   * @throws Exception Em caso de exceção na tentativa de acesso ao arquivo
   *                   a ser comprimido ou ao arquvo Zip.
   */
  private void compress(File file) throws Exception {
    // acesso ao arquivo da vez
    FileInputStream inputStream = new FileInputStream(file);
    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, BUFFER);
    // nova entrada Zip
    ZipEntry zipEntry = new ZipEntry(file.getName());
    zipOutputStream.putNextEntry(zipEntry);
    // lê o conteúdo do arquivo e adiciona ao Zip
    byte data[] = new byte[BUFFER];
    int count = -1;
    while((count = bufferedInputStream.read(data, 0, BUFFER)) != EOF)
      zipOutputStream.write(data, 0, count);
    // fecha o arquivo lido
    bufferedInputStream.close();
  }

}
