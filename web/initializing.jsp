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
