package iobjects.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

import org.w3c.tidy.*;
import org.xhtmlrenderer.pdf.*;

public class PDFWriter {

  static public void main(String[] args) {
    try {
      new PDFWriter(new URL("http://www.faceimagem.com.br"), "/Volumes/Dados/faceimagem.pdf");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Gera um PDF em 'targetFile' com o contedo obtido a partir de 'sourceFile'.
   * @param sourceFile Caminho completo do arquivo de origem.
   * @param targetFile Caminho completo do arquivo de destino.
   * @throws Exception
   */
  public PDFWriter(String sourceFile, String targetFile) throws Exception {
    // gera o PDF
    generatePDF(new FileInputStream(sourceFile), new FileOutputStream(targetFile));
  }

  /**
   * Gera um PDF em 'targetFile' com o contedo obtido a partir de 'sourceURL'.
   * @param sourceURL Caminho completo do contedo de origem.
   * @param targetFile Caminho completo do arquivo de destino.
   * @throws Exception
   */
  public PDFWriter(URL sourceURL, String targetFile) throws Exception{
    // conexo com a origem
    URLConnection conn = sourceURL.openConnection();
    // gera o PDF
    generatePDF(conn.getInputStream(), new FileOutputStream(targetFile));
  }

  private void generatePDF(InputStream input, OutputStream out) throws Exception{
    Tidy tidy = new Tidy();
    org.w3c.dom.Document doc = tidy.parseDOM(input, null);
    ITextRenderer renderer = new ITextRenderer();
    renderer.setDocument(doc, null);
    renderer.layout();
    renderer.createPDF(out);
  }

}
