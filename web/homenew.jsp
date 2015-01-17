<%@include file='include/beans.jsp'%>

<%@page import='java.util.*'%>
<%@page import='java.sql.*'%>

<%@page import='iobjects.xml.ui.*'%>

<%@page contentType='text/html' pageEncoding='ISO-8859-1'%>
<!DOCTYPE html>
<html>
  <head>
    <title><%=facade.applicationInformation().getName()%></title>
    <link href='<%=facade.getStyle().userInterface()%>' rel='stylesheet' type='text/css'>
    <link href='<%=facade.iconURL()%>' rel='shortcut icon' />
    <script src='include/scripts.js' type='text/javascript'></script>
    <!-- iPod, iPhone e iPad -->
    <meta name='apple-mobile-web-app-capable' content='yes' />
    <meta name='apple-mobile-web-app-status-bar-style' content='black' />
    <meta name='viewport' content='initial-scale=1 maximum-scale=1 user-scalable=0' />
    <meta name='apple-touch-fullscreen' content='yes' />
    <link rel='apple-touch-icon' href='<%=facade.iconURL()%>' />
    <style>
      html, body {
        background-color:     rgb(35, 35, 35);
        height:               100%;
        margin:               0px;
      }
      
      a, b, body, button, div, p, table, td, th, span, input, select, option, h1, h2, h3, textarea {
        color:                white;
        font-family:          Trebuchet, Arial, Tahoma, Verdana;
        font-size:            10pt;
        line-height:          120%;
      }
      
      .bgCyan {
        background-color:    rgb(0, 175, 240);
      }
      
      .bgOrange {
        background-color:    #F6BD0F;
      }
      
      .bgGray {
        background-color:    gray;
      }
      
      .bgBlack {
        background-color:    black;
      }
      
      .IconHome {
        background-image:     url('images/home48.png');
        background-position:  top center;
        background-repeat:    no-repeat;
      }
      
      .IconMap {
        background-image:     url('images/map48.png');
        background-position:  top center;
        background-repeat:    no-repeat;
      }
      
      .IconModule {
        background-image:     url('images/module48.png');
        background-position:  top center;
        background-repeat:    no-repeat;
      }
      
      .IconReport{
        background-image:     url('images/report48.png');
        background-position:  top center;
        background-repeat:    no-repeat;
      }
      
      .IconProcess{
        background-image:     url('images/process48.png');
        background-position:  top center;
        background-repeat:    no-repeat;
      }
      
      .IconEntity {
        background-image:     url('images/entity48.png');
        background-position:  top center;
        background-repeat:    no-repeat;
      }
      
      .MenuItem {
        color:                silver;
        height:               64px;
        vertical-align:       bottom;
        text-align:           center;
      }
      
      .MetroGuide {
        background-color:     rgb(9, 79, 184);
        background-image:     url('images/module24.png');
        background-position:  12px center;
        background-repeat:    no-repeat;
        height:               100%;
        width:                75px;
        padding-left:         40px;
        padding-right:        8px;
      }
      
      .MetroTile {
        background-image:     url('images/user64.png');
        background-position:  center 12px;
        background-repeat:    no-repeat;
        height:               100px;
        width:                110px;
        display:              table-cell;
        vertical-align:       bottom;
        padding:              8px;
      }
      
      .MetroTileBig {
        background-image:     url('images/user64.png');
        background-position:  center 12px;
        background-repeat:    no-repeat;
        height:               100px;
        width:                244px;
        display:              table-cell;
        vertical-align:       bottom;
        padding:              8px;
      }
      
      img {
        vertical-align:       middle;
      }
      
      h1 {
        font-size:           64px;
        font-weight:         lighter;
        margin:              0px;
      }

      #MetroBar {
        background-color:     rgb(25, 25, 25);
        width:                100px;
      }
      
      #MetroContent {
        height:               auto; 
        padding-top:          32px; 
        padding-left:         32px; 
        vertical-align:       top;
      }
      
      #MetroHeader {
        height:               75px;
        padding-left:         64px; 
        padding-right:        32px; 
      }      
    </style>
  </head>
  <body>

    <script type='text/javascript'>
      
      $(document).ready(function(event) {
      });
      
    </script>
    
    <!-- cabeçalho e conteúdo -->
    <table cellpadding="0" cellspacing="0" style="width:100%; height:100%; table-layout:fixed;">
      <tr>
        <td id="MetroBar">

          <!-- barra de menu -->
          <div style="overflow-x:hidden; overflow-y:auto;">
            <table id="MenuBar" align='center' cellpadding='0' cellspacing='8'>
              <tr>
                <td class="MenuItem IconHome">Início</td>
              </tr>
              <tr>
                <td class="MenuItem IconMap">Guias</td>
              </tr>
              <tr>
                <td class="MenuItem IconModule">BI</td>
              </tr>
              <tr>
                <td class="MenuItem IconModule">Contato</td>
              </tr>
              <tr>
                <td class="MenuItem IconModule">Estoque</td>
              </tr>
              <tr>
                <td class="MenuItem IconModule">Faturamento</td>
              </tr>
              <tr>
                <td class="MenuItem IconModule">Financeiro</td>
              </tr>
              <tr>
                <td class="MenuItem IconModule">Fiscal</td>
              </tr>
              <tr>
                <td class="MenuItem IconModule">Global</td>
              </tr>
              <tr>
                <td class="MenuItem IconModule">Serviço</td>
              </tr>
            </table>
          </div>
          
        </td>
        <td style="width:auto;">
          
          <table cellpadding='0' cellspacing='0' style='width:100%; height:100%; table-layout:fixed;'>
            <tr>
              <td id="MetroHeader">

                <!-- logo e master relation -->
                <table cellpadding='0' cellspacing='0' style='width:100%; height:100%; table-layout:fixed;'>
                  <tr>
                    <td style="width:50%;">
                      <!-- logo -->
                      <%// se temos uma logo para a aplicação...
                        if (!facade.applicationLogo().getFileName().equals("")) {
                          // tamanho da logo
                          int logoWidth = facade.applicationLogo().getWidth();
                          int logoHeight = facade.applicationLogo().getHeight();
                          // razão entre a altura de exibição (48 pixels) com a altura da imagem
                          int logoHeightProportion = 48 * 100 / logoHeight;
                          int newLogoWidth = logoWidth * logoHeightProportion / 100;
                      %>
                        <img src="<%=facade.logoURL()%>" height="48" width="<%=newLogoWidth%>" align="absmiddle" alt=""><%if (!facade.applicationInformation().getTitle().equals("")) {%><span class="BackgroundText" style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span><%} // if%>
                      <%}
                        // se não temos uma logo...usa a padrão
                        else {%>                        <img src="images/iobjects.png" width="48" height="48" align="absmiddle" alt=""><span class="BackgroundText" style="font-size:30pt;">&nbsp;<%=facade.applicationInformation().getTitle()%></span>
                      <%} // if%>
                    </td>
                    <td style="width:50%;">
                      <!-- master relation -->
                      <table align="right">
                        <tr>
                          <td class="menuItem">Comteq</td><td><img src="images/masterrelation48.png" /></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>

              </td>
            </tr>
            <tr>
              <td id='MetroContent'>

                <!-- atalhos -->
                <table cellpadding="0" cellspacing="4" style="float:left;">
                  <tr>
                    <td colspan="2"><div class='MetroTileBig bgCyan'>Atendimento</div></td>
                  </tr>
                  <tr>
                    <td colspan="2"><div class='MetroTileBig bgCyan'>Tarefa</div></td>
                  </tr>
                  <tr>
                    <td colspan="2"><div class='MetroTileBig bgCyan'>Oportunidade</div></td>
                  </tr>
                  <tr>
                    <td><div class='MetroTile bgCyan'>Atendimento Frequante</div></td>
                    <td><div class='MetroTile bgCyan'>Assunto</div></td>
                  </tr>
                </table>
                
                <table cellpadding="0" cellspacing="0" style="float:left; padding-left:0px;">
                  <tr>
                    <td><div class='MetroTile bgCyan'>Fase</div></td>
                  </tr>
                  <tr>
                    <td><div class='MetroTile bgCyan'>Campanha</div></td>
                  </tr>
                  <tr>
                    <td><div class='MetroTile bgCyan'>Meio</div></td>
                  </tr>
                </table>
                
                <!-- atalhos -->
                <table cellpadding="0" cellspacing="4" style="float:left; margin-left:16px;">
                  <tr>
                    <td colspan="2"><div class='MetroTileBig bgOrange'>Central de Atendimento</div></td>
                  </tr>
                </table>
                
                <!-- atalhos -->
                <table cellpadding="0" cellspacing="0" style="float:left; margin-left:16px;">
                  <tr>
                    <td colspan="2"><div class='MetroTileBig bgOrange'>Central de Atendimento</div></td>
                  </tr>
                </table>
                
                
              </td>
            </tr>
          </table>

        </td>
      </tr>
    </table>
    
  </body>
</html>
