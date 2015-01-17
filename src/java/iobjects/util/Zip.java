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
