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
