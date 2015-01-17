package iobjects.util;

import java.io.*;
import java.math.*;
import java.net.*;
import java.security.*;
import java.util.*;

/**
 * Contém uma série de ferramentas para facilitar as tarefas mais comuns
 * do dia-a-dia com arquivos.
 */
public class FileTools {

  
  static public void main(String[] args) {
    try {
      System.out.println(FileTools.md5FromFile("/Volumes/Dados/Temp/PROGRAMAS INSTALAÇÃO RADIOMEMORY VERSÃO NOVA.rar"));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  static public final String FILE_NAME_DECODED_STR = "áéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕçÇ";
  static public final String FILE_NAME_ENCODED_STR = "'a'e'i'o'u'A'E'I'O'U^a^e^i^o^u^A^E^I^O^U~a~o~A~O'c'C";
  
  static public final int ORDER_BY_NONE = 0;
  static public final int ORDER_BY_NAME = 1;
  static public final int ORDER_BY_SIZE = 2;

  static private final int EOF = -1;

  static class FileToolsComparator implements Comparator {
    int orderBy = ORDER_BY_NONE;
    public FileToolsComparator(int orderBy) {
      this.orderBy = orderBy;
    }
    public int compare(Object o1, Object o2) {
      File file1 = (File)o1;
      File file2 = (File)o2;
      switch (orderBy) {
        case ORDER_BY_NAME: return file1.getName().compareTo(file2.getName());
        case ORDER_BY_SIZE: return (file1.length() < file2.length() ? -1 : file1.length() > file2.length() ? 1 : 0);
        default:
          return 0;
      } // switch
    }
    public boolean equals(Object obj) {return false;}
  }

  /**
   * Copia o arquivo de origem no arquivo de destino informado.
   * @param sourceFileURL String URL onde se encontra o arquivo de origem.
   * @param targetFilePath String Caminho local do arquivo de destino.
   * @throws Exception Em caso de exceção na tentativa de acesso à URL de origem
   *                   ou de cópia do arquivo.
   */
  static public void copyFile(String sourceFileURL,
                              String targetFilePath) throws Exception {
    // arquivo de destino
    File target = new File(targetFilePath);
    // cria o diretório onde o arquivo será copiado
    File directory = new File(target.getParent());
    directory.mkdirs();
    // URL de origem
    URL sourceURL = new URL(sourceFileURL);
    // conteúdo de origem
    InputStream fileInputStream = sourceURL.openStream();
    // conteúdo de destino
    FileOutputStream fileOutputStream = new FileOutputStream(target, false);
    // copiando
    byte[] buffer = new byte[1024 * 32]; // 32 KB
    int count = EOF;
    while ((count = fileInputStream.read(buffer)) != EOF)
      fileOutputStream.write(buffer, 0, count);
    // libera recursos
    fileInputStream.close();
    fileOutputStream.close();
  }

  /**
   * Copia o arquivo de origem no arquivo de destino informado.
   * @param source File Arquivo de origem.
   * @param target File Arquivo de destino.
   * @throws Exception Em caso de exceção na tentativa de copiar o arquivo.
   */
  static public void copyFile(File source,
                              File target) throws Exception {
    // cria o diretório onde o arquivo será copiado
    File directory = new File(target.getParent());
    directory.mkdirs();
    // conteúdo de origem
    InputStream fileInputStream = new FileInputStream(source);
    // conteúdo de destino
    FileOutputStream fileOutputStream = new FileOutputStream(target, false);
    // copiando
    byte[] buffer = new byte[1024 * 32]; // 32 KB
    int count = EOF;
    while ((count = fileInputStream.read(buffer)) != EOF)
      fileOutputStream.write(buffer, 0, count);
    // libera recursos
    fileInputStream.close();
    fileOutputStream.close();
  }

  /**
   * Copia os arquivos do diretório de origem para o diretório destino informados.
   * @param sourcePath String Diretório de origem.
   * @param targetPath String Diretório de destino.
   * @param recursive boolean Informa se os arquivos contidos nos subdiretórios
   *                          também serão copiados.
   * @throws Exception Em caso de exceção na tentativa de copiar os arquivos.
   */
  static public void copyFiles(String  sourcePath,
                               String  targetPath,
                               boolean recursive) throws Exception {
    // adiciona as '/' no final
    sourcePath = includeSeparatorChar(sourcePath);
    targetPath = includeSeparatorChar(targetPath);
    // arquivos de origem
    String[] extensions = {"*"};
    String[] sourceFiles = getFileNames(sourcePath, extensions, recursive);
    // loop nos arquivos
    for (int i=0; i<sourceFiles.length; i++) {
      // arquivo da vez
      String sourceFileName = sourceFiles[i];
      // retira o a parte inicial do diretório de origem
      sourceFileName = sourceFileName.substring(sourcePath.length(), sourceFileName.length());
      // monta o nome do arquivo de destino
      String targetFileName = targetPath + sourceFileName;
      // remonta o nome completo do arquivo de origem
      sourceFileName = sourcePath + sourceFileName;
      // arquivos de origem e destino
      File sourceFile = new File(sourceFileName);
      File targetFile = new File(targetFileName);
      // copia o arquivo
      copyFile(sourceFile, targetFile);
    } // for
  }

  /**
   * Retorna 'fileName' decodificado a partir da forma codificada válida.
   * @param fileName Nome do arquivo.
   * @return Retorna 'fileName' decodificado a partir da forma codificada válida.
   */
  static public String decodeFileName(String fileName) {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // loop no resultado
    for (int i=0; i<fileName.length(); i++) {
      // conjunto da vez
      String str = fileName.substring(i, (fileName.length() > i+2 ? i+2 : i+1));
      // se é um caractereve codificado...
      int pos = FILE_NAME_ENCODED_STR.indexOf(str);
      if ((pos >= 0) && (pos % 2 == 0)) {
        result.append(FILE_NAME_DECODED_STR.substring(pos/2, (pos/2)+1));
        i++;
      }
      else
        result.append(str.charAt(0));
    } // for
    // retorna
    return result.toString();
  }

  /**
   * Retorna 'fileName' codificado de forma que os caracteres inválidos, como
   * os contendo acentos, sejam substituídos.
   * @param fileName Nome do arquivo.
   * @return Retorna 'fileName' codificado de forma que os caracteres inválidos, 
   *         como os contendo acentos, sejam substituídos.
   */
  static public String encodeFileName(String fileName) {
    // nosso resultado
    StringBuffer result = new StringBuffer();
    // loop nos caracteres do nome do arquivo
    for (int i=0; i<fileName.length(); i++) {
      // caractere da vez
      char ch = fileName.charAt(i);
      // se é um caractereve decodificado...
      int pos = FILE_NAME_DECODED_STR.indexOf(ch);
      if (pos >= 0)
        result.append(FILE_NAME_ENCODED_STR.substring(pos*2, (pos*2)+2));
      else
        result.append(ch);
    } // for
    // retorna
    return result.toString();
  }
  
  /**
   * Retorna um array de File contendo a lista de diretórios em 'path'.
   * @param path Indica o caminho cujos diretórios serão listados.
   * @param recursive Informe true para que a pesquisa seja realizada em todos
   *                  os subdiretórios encontrados em 'path'.
   * @return Retorna um array de File contendo a lista de diretórios em 'path'.
   */
  static public File[] getDirectories(String   path,
                                      boolean  recursive) {
    // organiza o caminho
    path = FileTools.includeSeparatorChar(path);
    // onde iremos buscar os arquivos
    File filePath = new File(path);
    // se o caminho não existe...dispara
    if (!filePath.exists())
      return new File[0];
    // arquivos existentes
    File[] files = filePath.listFiles();
    // vetor para os arquivos selecionados
    Vector vectorDirs = new Vector();
    // loop nos arquivos
    for (int i=0; i<files.length; i++) {
      // arquivo da vez
      File file = files[i];
      // se é um diretório...
      if (file.isDirectory()) {
        // adiciona na lista
        vectorDirs.add(file);
        // se a pesquisa é recursiva...
        if (recursive) {
          // pega os arquivos
          File[] subFiles = getDirectories(path + file.getName(), recursive);
          // adiciona no nosso vetor
          for (int j=0; j<subFiles.length; j++)
            vectorDirs.add(subFiles[j]);
        } // if
      } // if
    } // for
    // copia o vetor de arquivos selecionados para o resultado
    File[] arrayDirs = new File[vectorDirs.size()];
    vectorDirs.copyInto(arrayDirs);
    return arrayDirs;
  }

  /**
   * Retorna um array de String contendo a lista de arquivos em 'path' com as
   * extensões informadas em 'extensions'.
   * @param path Indica o caminho cujos arquivos serão listados.
   * @param extensions Indica as extensões que se deseja retornar. As extensões
   *                   devem vir precedidas do caracter '.'. Para retornar
   *                   todos os arquivos utilize '*'.
   * @param recursive Informe true para que a pesquisa seja realizada em todos
   *                  os diretórios e subdiretórios encontrados em 'path'. Neste
   *                  caso o nome do arquivo será precedido do seu caminho.
   * @return Retorna um array de String contendo a lista de arquivos em 'path'
   *         com as extensões informadas em 'extensions'.
   */
  static public String[] getFileNames(String   path,
                                      String[] extensions,
                                      boolean  recursive) {
    return getFileNames(path, extensions, recursive, ORDER_BY_NONE);
  }

  /**
   * Retorna um array de String contendo a lista de arquivos em 'path' com as
   * extensões informadas em 'extensions'.
   * @param path Indica o caminho cujos arquivos serão listados.
   * @param extensions Indica as extensões que se deseja retornar. As extensões
   *                   devem vir precedidas do caracter '.'. Para retornar
   *                   todos os arquivos utilize '*'.
   * @param recursive Informe true para que a pesquisa seja realizada em todos
   *                  os diretórios e subdiretórios encontrados em 'path'. Neste
   *                  caso o nome do arquivo será precedido do seu caminho.
   * @param orderBy Tipo de ordenação dos arquivos.
   * @return Retorna um array de String contendo a lista de arquivos em 'path'
   *         com as extensões informadas em 'extensions'.
   */
  static public String[] getFileNames(String   path,
                                      String[] extensions,
                                      boolean  recursive,
                                      int      orderBy) {
    // lista os arquivos
    File[] files = getFiles(path, extensions, recursive, orderBy);
    // copia apenas os nomes
    String [] arrayNames = new String[files.length];
    for (int i=0; i<files.length; i++)
      if (recursive)
        arrayNames[i] = files[i].getAbsolutePath();
      else
        arrayNames[i] = files[i].getName();
    // retorna-os
    return arrayNames;
  }

  /**
   * Retorna um array de File contendo a lista de arquivos em 'path' com as
   * extensões informadas em 'extensions'.
   * @param path Indica o caminho cujos arquivos serão listados.
   * @param extensions Indica as extensões que se deseja retornar. As extensões
   *                   devem vir precedidas do caracter '.'. Para retornar
   *                   todos os arquivos utilize '*'.
   * @param recursive Informe true para que a pesquisa seja realizada em todos
   *                  os diretórios e subdiretórios encontrados em 'path'.
   * @return Retorna um array de File contendo a lista de arquivos em 'path'
   *         com as extensões informadas em 'extensions'.
   */
  static public File[] getFiles(String   path,
                                String[] extensions,
                                boolean  recursive) {
    return getFiles(path, extensions, recursive, ORDER_BY_NONE);
  }

  /**
   * Retorna um array de File contendo a lista de arquivos em 'path' com as
   * extensões informadas em 'extensions'.
   * @param path Indica o caminho cujos arquivos serão listados.
   * @param extensions Indica as extensões que se deseja retornar. As extensões
   *                   devem vir precedidas do caracter '.'. Para retornar
   *                   todos os arquivos utilize '*'.
   * @param recursive Informe true para que a pesquisa seja realizada em todos
   *                  os diretórios e subdiretórios encontrados em 'path'.
   * @param orderBy Tipo de ordenação dos arquivos.
   * @return Retorna um array de File contendo a lista de arquivos em 'path'
   *         com as extensões informadas em 'extensions'.
   */
  static public File[] getFiles(String   path,
                                String[] extensions,
                                boolean  recursive,
                                int      orderBy) {
    return getFiles(path, extensions, null, recursive, orderBy);
  }

  /**
   * Retorna um array de File contendo a lista de arquivos em 'path' com as
   * extensões informadas em 'extensions'.
   * @param path Indica o caminho cujos arquivos serão listados.
   * @param extensions Indica as extensões que se deseja retornar. As extensões
   *                   devem vir precedidas do caracter '.'. Para retornar
   *                   todos os arquivos utilize '*'.
   * @param filter FilenameFilter Implementação de FilenameFilter responsável
   *               pela filtragem dos arquivos incluídos no resultado final.
   * @param recursive Informe true para que a pesquisa seja realizada em todos
   *                  os diretórios e subdiretórios encontrados em 'path'.
   * @param orderBy Tipo de ordenação dos arquivos.
   * @return Retorna um array de File contendo a lista de arquivos em 'path'
   *         com as extensões informadas em 'extensions'.
   */
  static public File[] getFiles(String         path,
                                String[]       extensions,
                                FilenameFilter filter,
                                boolean        recursive,
                                int            orderBy) {
    // organiza o caminho
    path = FileTools.includeSeparatorChar(path);
    // onde iremos buscar os arquivos
    File filePath = new File(path);
    // se o caminho não existe...dispara
    if (!filePath.exists())
      return new File[0];
    // arquivos existentes
    File[] files = filePath.listFiles(filter);
    // vetor para os arquivos selecionados
    Vector vectorFiles = new Vector();
    // loop nos arquivos
    for (int i=0; i<files.length; i++) {
      // arquivo da vez
      File file = files[i];
      // se é um diretório...
      if (file.isDirectory()) {
        // se a pesquisa é recursiva...
        if (recursive) {
          // pega os arquivos
          File[] subFiles = getFiles(path + file.getName(),
                                     extensions,
                                     recursive);
          // adiciona no nosso vetor
          for (int j=0; j<subFiles.length; j++)
            vectorFiles.add(subFiles[j]);
        } // if
      }
      // se é um arquivo...
      else {
        // loop nas extensões
        for (int w=0; w<extensions.length; w++) {
          // se a extensão combina com a do arquivo atual...adiciona a lista final
          String extension = extensions[w].toLowerCase();
          if (extension.equals("*") || file.getName().toLowerCase().endsWith(extension)) {
            vectorFiles.add(file);
            break;
          } // if
        } // for
      } // if
    } // for
    // copia o vetor de arquivos selecionados para o resultado
    File[] result = new File[vectorFiles.size()];
    vectorFiles.copyInto(result);
    // se devemos ordenar
    if (orderBy != ORDER_BY_NONE)
      Arrays.sort(result, new FileToolsComparator(orderBy));
    // retorna
    return result;
  }

  /**
   * Retorna o caminho informado certificando-se que o mesmo termina pelo
   * caracter separador (java.io.File.separatorChar).
   * @param path Caminho para ser verificado.
   * @return Retorna o caminho informado certificando-se que o mesmo termina pelo
   *         caracter separador (java.io.File.separatorChar).
   */
  static public String includeSeparatorChar(String path) {
    if (path.charAt(path.length()-1) != File.separatorChar)
      return path + File.separatorChar;
    else
      return path;
  }

  /**
   * Retorna o conteúdo do arquivo texto informado.
   * @param fileName Nome do arquivo para ser carregado.
   * @return Retorna o conteúdo do arquivo texto informado.
   * @throws IOException Em caso de exceção na tentativa de acesso ao arquivo.
   */
  static public String[] loadTextFile(String fileName) throws IOException {
    return loadTextFile(fileName, -1, -1);
  }

  /**
   * Retorna o conteúdo do arquivo texto informado.
   * @param fileName Nome do arquivo para ser carregado.
   * @param offset Linha inicial para ser lida.
   * @param length Quantidade de linhas para serem lidas.
   * @return Retorna o conteúdo do arquivo texto informado.
   * @throws IOException Em caso de exceção na tentativa de acesso ao arquivo.
   */
  static public String[] loadTextFile(String fileName, int offset, int length) throws IOException {
    // arquivo de origem
    File file = new File(fileName);
    // se o arquivo não existe...dispara
    if (!file.exists())
      return new String[0];
    // abre o arquivo para leitura
    FileReader fileReader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    try {
      // vetor para armazenamento temporário
      Vector vectorLines = new Vector();
      // vai para a linha inicial desejada
      if (offset > 0)
        bufferedReader.skip(offset);
      // lê suas linhas
      while (true) {
        // linha atual
        String line = bufferedReader.readLine();
        // se chegamos ao fim do arquivo...dispara
        if (line == null)
          break;
        // se não...adiciona à saída
        else
          vectorLines.add(line);
        // se já lemos tudo...dispara
        if ((length > 0) && (vectorLines.size() >= length))
          break;
      } // for
      // retorna o conteúdo lido
      String[] lines = new String[vectorLines.size()];
      vectorLines.copyInto(lines);
      return lines;
    }
    finally {
      if (bufferedReader != null)
        bufferedReader.close();
      if (fileReader != null)
        fileReader.close();
    } // try-finally
  }

  /**
   * Retorna o conteúdo do arquivo texto informado.
   * @param fileName Nome do arquivo para ser carregado.
   * @param charset Nome do charset a ser utilizado.
   * @return Retorna o conteúdo do arquivo texto informado.
   * @throws IOException Em caso de exceção na tentativa de acesso ao arquivo.
   */
  static public String loadTextFile(String fileName, String charset) throws IOException {
    // arquivo de origem
    File file = new File(fileName);
    // se o arquivo não existe...dispara
    if (!file.exists())
      return "";
    // abre o arquivo
    FileInputStream   inputStream = new FileInputStream(file.getPath());
    InputStreamReader reader      = new InputStreamReader(inputStream, charset.isEmpty() ? "ISO-8859-1" : charset);
    StringBuffer      content     = new StringBuffer();
    // lê o conteúdo
    while (true) {
      char[] buffer = new char[32 * 1024]; // 32 KB
      int count = reader.read(buffer);
      if (count <= 0)
        break;
      content.append(buffer, 0, count);
    } // while
    // fecha
    inputStream.close();
    reader.close();
    // retorna em linhas
    return content.toString();
  }
  
  /**
   * Retorna a representação MD5 do arquivo indicado por 'fileName'.
   * @param fileName Nome do arquivo.
   * @return Retorna a representação MD5 do arquivo indicado por 'fileName'.
   * @throws Exception Em caso de exceção na tentativa de acesso ao arquivo.
   */
  static public String md5FromFile(String fileName) throws Exception {
    // nosso arquivo
    File file = new File(fileName);
    FileInputStream fis = new FileInputStream(file);
    try {
      // nosso gerador
      MessageDigest md = MessageDigest.getInstance("MD5");
      // lê o conteúdo do arquivo
      byte[] buf = new byte[255];
      int length;
      while ((length = fis.read(buf)) > 0)
        md.update(buf, 0, length);
      // gera o código MD5
      byte[] md5 = md.digest();
      // gera a representação hexadecimal
      BigInteger bi = new BigInteger(1, md5);
      String result = bi.toString(16);
      // retorna completando com 0 a esquerda
      return StringTools.stringOfChar('0', 32 - result.length()) + result;
    }
    finally {
      // libera recursos
      fis.close();
    } // try-finally
  }
  
  /**
   * Salva o texto em 'lines' em 'fileName'.
   * @param fileName Nome do arquivo para ser salvo.
   * @param lines Texto para ser salvo.
   * @throws IOException Em caso de exceção na tentativa de acesso ao arquivo.
   */
  static public void saveTextFile(String   fileName,
                                  String[] lines) throws IOException {
    // arquivo de destino
    File file = new File(fileName);
    // cria o diretório onde o arquivo será copiado
    File directory = new File(file.getParent());
    directory.mkdirs();
    // se o arquivo não existe...cria-o
    if (!file.exists())
      file.createNewFile();
    // abre o arquivo para escrita
    FileWriter fileWriter = new FileWriter(file);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    try {
      // salva suas linhas
      for (int i = 0; i < lines.length; i++) {
        // salva linha atual
        bufferedWriter.write(lines[i]);
        // adiciona quebra de linha
        bufferedWriter.newLine();
      } // for
    }
    finally {
      // fecha o arquivo
      if (bufferedWriter != null)
        bufferedWriter.close();
      if (fileWriter != null)
        fileWriter.close();
    } // try-finally
  }

  /**
   * Salva o texto em 'lines' em 'fileName'.
   * @param fileName Nome do arquivo para ser salvo.
   * @param lines Texto para ser salvo.
   * @throws IOException Em caso de exceção na tentativa de acesso ao arquivo.
   */
  static public void saveTextFile(String   fileName,
                                  String   content,
                                  String   charset) throws IOException {
    // arquivo de destino
    File file = new File(fileName);
    // se não existe...cria
    if (!file.exists()) {
      if (!file.getParentFile().exists())
        file.getParentFile().mkdirs();
      file.createNewFile();
    } // if
    // salva o arquivo
    FileOutputStream   outputStream = new FileOutputStream(file.getPath());
    OutputStreamWriter writer       = new OutputStreamWriter(outputStream, charset.isEmpty() ? "ISO-8859-1" : charset);
    // conteúdo
    writer.write(content);
    // fecha
    writer.close();
    outputStream.close();
  }
  
}
