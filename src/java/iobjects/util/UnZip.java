package iobjects.util;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import iobjects.*;

/**
 * Implementa as rotinas de descompressão de um arquivo no formato ZIP.
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
   * Construtor padrão.
   * @param zipFileName Nome do arquivo ZIP para descomprimir.
   * @param targetPath Caminho de destino para os arquivos extraídos.
   * @throws Exception Em caso de exceção na tentativa de abrir ou extrair o
   *                   conteúdo do arquivo Zip.
   */
  public UnZip(String zipFileName, String targetPath) throws Exception {
    // nossos valores
    this.zipFileName = zipFileName;
    this.targetPath  = targetPath;
    // extrai
    unZip();
  }

  /**
   * Construtor padrão.
   * @param zipFileName Nome do arquivo ZIP para descomprimir.
   * @param targetPath Caminho de destino para os arquivos extraídos.
   * @param denyDirs True para impedir a extração de pastas contidas no arquivo ZIP.
   * @throws Exception Em caso de exceção na tentativa de abrir ou extrair o
   *                   conteúdo do arquivo Zip.
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
   * @throws Exception Em caso de exceção na tentativa de criação e escrita
   *                   do arquivo descompactado.
   */
  private void extract(ZipEntry zipEntry) throws Exception {
    // cria o arquivo de destino
    File file = new File(targetPath + zipEntry.getName());
    // se é um diretório...
    if (zipEntry.isDirectory()) {
      // se podemos permitir...cria
      if (!denyDirs)
        file.mkdirs();
    }
    // se é um arquivo...
    else {
      // diretório onde o arquivo será extraido
      File directory = new File(file.getParent());
      // se não podemos permitir diretórios...dispara
      if (denyDirs && !directory.exists())
        return;
      // cria o caminho completo
      directory.mkdirs();
      // conteúdo compactado
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
   * Retorna os nomes dos arquivos extraídos.
   * @return Retorna os nomes dos arquivos extraídos.
   */
  public String[] fileNames() {
    return fileNames;
  }
  
  /**
   * Realiza a descompressão do arquivo.
   */
  private void unZip() throws Exception {
    // arquivos extraídos
    Vector vector = new Vector();
    // cria nosso arquivo Zip
    zipFile = new ZipFile(zipFileName);
    try {
      // se não informou o diretório de destino...exceção
      if (targetPath.equals(""))
        throw new ExtendedException(getClass().getName(), "", "Caminho de destino não informado.");
      else
        this.targetPath = FileTools.includeSeparatorChar(targetPath);
      // cria o diretório de destino
      File directory = new File(targetPath);
      directory.mkdirs();
      // loop no conteúdo do Zip
      Enumeration zipEntries = zipFile.entries();
      while(zipEntries.hasMoreElements()) {
        // entrada da vez
        ZipEntry zipEntry = (ZipEntry)zipEntries.nextElement();
        // extrai a entrada
        extract(zipEntry);
        // guarda o nome do arquivo
        vector.add(targetPath + zipEntry.getName());
      } // while
      // guarda os nomes dos arquivos extraídos
      fileNames = new String[vector.size()];
      vector.copyInto(fileNames);
    }
    finally {
      // fecha o arquivo
      zipFile.close();
    } // try-finally
  }
  
}
