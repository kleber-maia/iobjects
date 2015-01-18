
<%--
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
--%>
<html>
  <head>
    <title>Inicialização em andamento</title>
  </head>

  <style type="text/css">
    body {
      background-color: #FFFFFF;
      color:            #000000;
      font-family:      tahoma, verdana, helvetica, sans-serif;
      font-size:        9pt;
    }

    table {
      font-family:      tahoma, verdana, helvetica, sans-serif;
      font-size:        9pt;
    }

    .Title {
      background-color: #FFFFFF;
      color:            rgb(54, 95, 145);
      font-family:      tahoma, verdana, helvetica, sans-serif;
      font-size:        14pt;
      font-weight:      bold;
    }

    .Title1 {
      background-color: #FFFFFF;
      border-bottom:    1px solid #4f81bd;
      color:            rgb(79, 129, 189);
      font-family:      tahoma, verdana, helvetica, sans-serif;
      font-size:        13pt;
      font-weight:      bold;
    }
  </style>

  <body onselectstart="return false;" oncontextmenu="return false;">

    <table style="width:70%">
      <tr>
        <td style="width:48px;" valign="top"><img alt="" src="images/logoff32x32.png" /></td>
        <td style="width:auto;" valign="top">
          <p class="Title">Inicialização em andamento</p>
          <p class="Title1"></p>
          <p>
            Por favor, aguarde alguns instantes enquanto a aplicação é inicializada.
            Após o término deste processo, a aplicação será exibida automaticamente.
          </p>
          <p>
            Tentando novamente em <span id="spanTimeout">10</span> segundos...
          </p>
        </td>
      </tr>
    </table>

    <script type="text/javascript">
      // verifica nova tentativa a cada 1 seg
      setInterval("checkTimeout();", 1000);
      // tentativas restantes
      var timeout = 10;

      function checkTimeout() {
        // decrementa o timeout
        timeout = timeout-1;
        // mostra na página
        document.all.spanTimeout.innerText = timeout;
        // se chegamos no zero...recarrega a página
        if (timeout == 0)
          window.location.reload();
      }
    </script>

  </body>
</html>
