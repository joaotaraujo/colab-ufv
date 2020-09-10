<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" media="screen" type="text/css" href="/ProjetoColabUFV/css/style.css"   />
    <title>ColabUFV</title>
    <link rel='shortcut icon' href="/ProjetoColabUFV/imagens/brasaoUFV.jpg" /> 
    ${alertasPendentes}
</head>
<body>
    <style>
        .container {
            width: 705px;
            height:290px;
            overflow-x: auto;
            white-space: nowrap;
            background-color: #FFECF7;
            padding-top:15px;
            padding-left:10px;
            border-radius: 28px;
            padding-right:10px;
        }
        .containerMenor {
            width: 265px;
            height:105px;
            overflow-x: auto;
            white-space: nowrap;
            background-color: #E6E6E6;
            border-radius: 5px;
            padding-left:15px;
        }
        .containerMaior {
            background: #F5A9E1;
            background-image: -webkit-linear-gradient(top, #B8C1FF, #D1D7FF);
            background-image: -moz-linear-gradient(top, #B8C1FF, #D1D7FF);
            background-image: -ms-linear-gradient(top, #B8C1FF, #D1D7FF);
            background-image: -o-linear-gradient(top, #B8C1FF, #D1D7FF);
            border-radius: 28px;
            font-family: Arial;
            color: #ffffff;
            font-size: 22px;
            padding: 1px 2px 1px 2px;
            text-decoration: none;
            width:280px;
            height: 250px;
            display: inline-block;
            padding-top:10px;
            overflow-x: auto;
        }
        button {
            background: #F5A9E1;
            background-image: -webkit-linear-gradient(top, #FA5858, #F5A9A9);
            background-image: -moz-linear-gradient(top, #FA5858, #F5A9A9);
            background-image: -ms-linear-gradient(top, #FA5858, #F5A9A9);
            background-image: -o-linear-gradient(top, #FA5858, #F5A9A9);
            border-radius: 28px;
            color: #ffffff;
            font-family: Comic Sans MS;
            font-size: 30px;
            padding: 1px 2px 1px 2px;
            text-decoration: none;
            width:270px;
            height: 350px;
        }
        
        #welcome {
            background-color: #C9C9C9;
            width: 715px;
            height: 460px; 
            padding: 100px;
            border: #000000 solid 4px;
            padding-left: 0px;
            padding-top: 0px;
            padding-bottom: 0px;
            padding-right: 10px;
            position:static;
        }
        textoLimitado {
  max-width: 15ch;
}
    .containerRanking {
            width: 200px;
            height:100px;
            white-space: nowrap;
            background-color: #ADFF2F;
            border: #A61818 solid 3px;
            border-radius: 10px;
        }
        .buttonChat {
            background: #9C9CFF;
            background-image: -webkit-linear-gradient(top, #FF7C7C, #FFA8A8);
            background-image: -moz-linear-gradient(top, #FF7C7C, #FFA8A8);
            background-image: -ms-linear-gradient(top, #FF7C7C, #FFA8A8);
            background-image: -o-linear-gradient(top, #FF7C7C, #FFA8A8);
            border-radius: 5px;
            font-family: Arial;
            color: #ffffff;
            font-size: 18px;
            text-decoration: none;
            width:200px;
            height: 25px;
            display: inline-block;
        }
    </style>
    
    <%if ((session.getAttribute("login") == null) || (session.getAttribute("login") == "")) {%>
    You are not logged in<br/>
    <a href="/ProjetoColabUFV/index.jsp">Please Login</a>
    <%} else {%> 
    
    <!--HEADER-->
    <div id="header">
        <div id="img"></div>
        <a href= "/ProjetoColabUFV/ServletCarregaPaginaInicialUsuario">
        <div id="logo"> <img src="/ProjetoColabUFV/imagens/brasaoUFV.jpg" width="40" height="40" alt="brasaoImgUFV"/><br>ColabUFV<br />
        <span id="slogan"> Ambiente Colaborativo</span></div>
        </a>
    </div>
    <div class="clear"></div>
    <!-- HEADER END -->
    
    <!-- MENU -->
    <ul id="menu">
        <li id="loggedAs">
            <a href="#">Logged as: ${nomeUsuario}<br> 
                    Projeto: ${projetoUsuario}<br>
                Papel: ${papelUsuario}
            </a>
        </li>
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaGruposProjeto">Grupos</a></li>
         <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaAtividades">Atividades Tarefas</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaForum">Fórum</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaAlertas">Alertas</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaPerguntasFrequentes">Perguntas Frequentes</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/paginaInicial/vizualizacao/vizualizarBibliotecaVirtual.jsp">Biblioteca Virtual</a></li>
    </ul>
    <div class="clear"></div>
    <!-- MENU END -->
    
    <!-- CONTENT -->
    <div id="content">
        <div id="left">
            <strong><a href="/ProjetoColabUFV/ServletLogout">LOGOUT</a></strong><br><br>
                <a href="/ProjetoColabUFV/ServletCarregaInformacoesProjeto"><h1>Informações do projeto</h1></a>
                <a href="/ProjetoColabUFV/ServletVizualizaCronograma"><h1>Vizualizar Cronograma</h1></a>
                <a href="/ProjetoColabUFV/paginaInicial/envios/enviarEmail.jsp"><h1>Enviar um email</h1></a>
                <a href="/ProjetoColabUFV/ServletCarregaEnviaAlerta"><h1>Enviar um alerta</h1></a>

            <div>
                ${ultimoTopicoForum}
            </div>
            <div>
                ${ultimasPerguntasFrequentes}
            </div><br>
            <div class="containerRanking"><center style="padding-top:10px;">
                    <strong>Aluno Destaque</strong></center><br> 
                        Nome: ${nomeAlunoRanking}<br>
                        Atv Concluídas: ${quantAtividadesConcluidas}
                    </a>
                    </div>
            <div class="buttonChat" bgcolor=\"FF0000\"><center><a href="/ProjetoColabUFV/start.jsp">Chat Online!</a></center></div>
        </div>
      
        <div id="right">
            <div id="welcome">
                <table style="width:725px;">
                            <thead>
                                <tr>
                                    <th colspan="6"><h3><center>Suas Atividades</center></h3></th>
                                </tr>
                            </thead>
                        </table>
                    ${cronogramaAluno}
                    
            <table style="width:725px;" style="background-color:#C1C1C1;">
                <thead>
                    <tr>
                        <th style="background:#BCC5FF;color:#000000;" colspan="6"><center>Cores (Status)</center></th>
                    </tr>
                </thead>
                <tbody>
                    <tr style="height:10px;">
                        <td width="18%" style="font-size:13px;"><strong><center>Nova Atividade</center></strong></td>
                        <td width="18%" style="font-size:13px;"><strong><center>Em Andamento</center></strong></td>
                        <td width="16%" style="font-size:13px;"><strong><center>Concluida</center></strong></td>
                        <td width="16%" style="font-size:13px;"><strong><center>Reaberta</center></strong></td>
                        <td width="16%" style="font-size:13px;"><strong><center>Cancelada</center></strong></td>
                        <td width="16%" style="font-size:13px;"><strong><center>Atrasada</center></strong></td>
                    </tr>
                    <tr>
                        <td width="18%" style="font-size:13px;background-color:#FFFFFF;"></td>
                        <td width="18%" style="font-size:13px;background-color:#2222DA;"></td>
                        <td width="16%" style="font-size:13px;background-color:#006106;"></td>
                        <td width="16%" style="font-size:13px;background-color:#616000;"></td>
                        <td width="16%" style="font-size:13px;background-color:#5D5D5D;"></td>
                        <td width="16%" style="font-size:13px;background-color:#E80000;"></td>
                    </tr>
                </tbody>
            </table>
                    
                <div class="clear"></div>
            </div>
            <div class="clear"></div>
            <div id="block_left">
                ${alertasAluno}
            </div>
            
            <div id="block_right">
                ${graficoAtvAlunoAtrasada}
            </div>
        </div>
        <div class="clear"></div>
        <!-- CONTENT END -->
        
        
  <!-- FOOTER -->
        <div id="footer"> portal.ufv.br/florestal &copy; 2016 <a href="#" > Privaty Policy</a> | <a href="#"> Terms of use</a>
            <br><a href="#" >Contato: teixeira.araujo@gmail.com</a>
        </div>
  <!-- FOOTER END -->
</div>
<%
    }
%>
</body>
</html>

