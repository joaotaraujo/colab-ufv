<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" media="screen" type="text/css" href="/ProjetoColabUFV/css/style.css"  />
    <title>ColabUFV</title>
    <link rel='shortcut icon' href="/ProjetoColabUFV/imagens/brasaoUFV.jpg" /> 
</head>
<body>
    <link rel="stylesheet" type="text/css" href="/ProjetoColabUFV/DataTables-1.10.12/media/css/jquery.dataTables.css">
	
    <script type="text/javascript" language="javascript" src="//code.jquery.com/jquery-1.12.3.min.js">
    </script>
    <script type="text/javascript" language="javascript" src="/ProjetoColabUFV/DataTables-1.10.12/media/js/jquery.dataTables.js">
    </script>
        <script type="text/javascript" src="http://code.jquery.com/jquery-1.7.2.min.js"></script> 
    <script type="text/javascript" language="javascript" class="init">

        $(document).ready(function() {
            $("#conteudoAbas div:nth-child(1)").show();
                                          $(".abas li:first div").addClass("selected");           
                                          $(".aba").click(function(){
                                                  $(".aba").removeClass("selected");
                                                  $(this).addClass("selected");
                                                  var indice = $(this).parent().index();
                                                  indice++;
                                                  $("#conteudoAbas div").hide();
                                                  $("#conteudoAbas div:nth-child("+indice+")").show();
                                          });

                                          $(".aba").hover(
                                                  function(){$(this).addClass("ativa")},
                                                  function(){$(this).removeClass("ativa")}
                                          ); 

        } );
        
    </script>
    <style>
         body{
		font-family:Calibri, Tahoma, Arial
	}

        .TabControl{ 
            padding-top:0px;
	width:101%; 
	overflow:hidden; 
	height:250px
	}

        .TabControl #headerAbas{ 
	width:101%; 
	border: solid 1px; 
	overflow-x: auto;
	cursor:hand
	}

        .TabControl #conteudoAbas{ 
	width:101%; 
	border: solid 1px;
	overflow-y: auto;
	height:240px; 
	}

        .TabControl .abas{
	display:inline;
	}

        .TabControl .abas li{
	float:left
	}

        .aba{
	width:150px; 
	height:20px; 
	border:solid 1px; 
	border-radius:5px 5px 0 0;
	text-align:center; 
	padding-top:0px; 
	background:#CBCADF
	}
        .ativa{
	width:150px; 
	height:20px; 
	border:solid 1 px; 
	border-radius:5px 5px 0 0;
	text-align:center; 
	padding-top:0px; 
	background:#27408B;
	}
        .ativa span, .selected span{
	color:#fff
	}
        .TabControl #conteudoAbas{
	background:#27408B
	}
        .TabControl .conteudo{
	width:101%;  
	background:#F4F4FF; 
	display:none; 
	height:100%;
	color:#fff
	}
        .selected{
	width:150px; 
	height:20px; 
	border:solid 1 px; 
	border-radius:5px 5px 0 0;
	text-align:center; 
	padding-top:0px; 
	background:#27408B
	}
        #welcome {
	background-color: #F5F5F5;
        width: 700px;
        height: 612px; 
	padding: 100px;
	border: #000000 solid 4px;
        padding-left: 0px;
        padding-top: 0px;
        padding-bottom: 0px;
        padding-right: 10px;
        position:static;
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
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaAlunos">Relatórios</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/paginaInicial/cadastros/tipoDeCadastro.jsp">Cadastros</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaAtividades">Atividades Tarefas</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaForum">Fórum</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaAlertas">Alertas</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/paginaInicial/vizualizacao/vizualizarBibliotecaVirtual.jsp">Biblioteca Virtual</a></li>
    </ul>
    <div class="clear"></div>
    <!-- MENU END -->
    
    
    <!-- CONTENT -->
    <div id="content">
        <div id="left">
            <div>
                <strong><a href="/ProjetoColabUFV/ServletLogout">LOGOUT</a></strong><br><br>
                <a href="/ProjetoColabUFV/ServletCarregaInformacoesProjeto"><h1>Informações do projeto</h1></a>
                <a href="/ProjetoColabUFV/ServletVizualizaCronograma"><h1>Vizualizar Cronograma</h1></a>
                <a href="/ProjetoColabUFV/ServletCarregaAlunos"><h1>Gerar um Relatório</h1></a>
                <a href="/ProjetoColabUFV/ServletCarregaGruposProjeto"><h1>Grupos</h1></a>
                <a href="/ProjetoColabUFV/ServletCarregaForum"><h1>Fórum</h1></a>
                <a href="/ProjetoColabUFV/ServletCarregaEnviaAlerta"><h1>Enviar um alerta</h1></a>
                <a href="/ProjetoColabUFV/paginaInicial/envios/enviarEmail.jsp"><h1>Enviar um email</h1></a>
                <a href="/ProjetoColabUFV/paginaInicial/cadastros/tipoDeCadastro.jsp"><h1>Cadastros</h1></a>         
                    <a href="/ProjetoColabUFV/paginaInicial/cadastros/cadastroAluno.jsp" class="link">Aluno</a> 
                    <a href="/ProjetoColabUFV/ServletCarregaCadastroAtividade" class="link">Atividade</a>
                    <a href="/ProjetoColabUFV/paginaInicial/cadastros/cadastroGrupo.jsp" class="link">Grupo</a>
                    <a href="/ProjetoColabUFV/paginaInicial/cadastros/cadastroPerguntaFrequente.jsp" class="link">Pergunta Frequente</a>
                    <a href="/ProjetoColabUFV/ServletCarregaCadastroTarefa" class="link">Tarefa</a>
                <a href="/ProjetoColabUFV/ServletCarregaPerguntasFrequentes"><h1>Perguntas Frequentes</h1></a>
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
                
                    ${informacoesProjeto}  
                    <table><head></head>
                    <body>
                        <tr>
                            <td WIDTH="50%"><center>
                                <form method="post" action="/ProjetoColabUFV/ServletCarregaEditaProjeto">
                                    <button type="submit" name="Submit" value="Editar projeto">Editar projeto</button>    
                                </form>
                            </center></td>
                            <td WIDTH="50%"><center>
                                <form method="post" action="/ProjetoColabUFV/ServletDeletaProjeto">
                                    <button type="submit" name="Submit" value="Deletar projeto">Deletar projeto</button>    
                                </form>
                            </center></td>
                    </body>
                    </table>
                                
                    <br><br>
                            
                    <div class="TabControl">
                    <div id="headerAbas">
                        <ul class="abas">
                            <li>
                                    <div class="aba">
                                            <span>Papéis</span>
                                    </div>
                            </li>
                            <li>
                                    <div class="aba">
                                            <span>Grupos</span>
                                    </div>
                            </li>
                            <li>
                                    <div class="aba">
                                            <span>Envolvidos</span>
                                    </div>
                            </li>
                        </ul>
                    </div>
                    <div id="conteudoAbas">
                        <div class="conteudo">            
                            <table  height="20" style="width: 710px;" class="display" cellspacing="0" width="100%">
                                <thead>
                                    <tr>
                                        <th style="background:#818181;color:#ffffff;">Papeis</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                            <br>
                            <form method="post" action="/ProjetoColabUFV/paginaInicial/cadastros/cadastroPapel.jsp">
                                    <button type="submit" name="Submit" value="Adicionar papel">Cadastrar papel</button>    
                                </form>    
                            <br>
                            ${papeis}
                        </div>
                        <div class="conteudo"> 
                            <table  height="20" style="width: 710px;" class="display" cellspacing="0" width="100%">
                                <thead>
                                    <tr>
                                        <th style="background:#818181;color:#ffffff;">Grupos</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                            <br>
                            <form method="post" action="/ProjetoColabUFV/paginaInicial/cadastros/cadastroGrupo.jsp">
                                <button type="submit" name="Submit" value="Adicionar grupo">Cadastrar grupo</button>    
                            </form>      
                            <br>
                            ${grupos}
                        </div>
                        <div class="conteudo">            
                            <table  height="20" style="width: 710px;" class="display" cellspacing="0" width="100%">
                                <thead>
                                    <tr>
                                        <th style="background:#818181;color:#ffffff;">Nome</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                            <br>
                            <table   width="540px"><head></head>
                                <body>
                                    <tr>
                                        <td style="width: 140px;">
                                            <form method="post" action="/ProjetoColabUFV/paginaInicial/cadastros/cadastroAluno.jsp">
                                                <center><button type="submit" name="Submit" value="Adicionar aluno">Cadastrar aluno</button></center>    
                                            </form>  
                                        </td>
                                        <td style="width: 140px;">
                                            <form method="post" action="/ProjetoColabUFV/paginaInicial/cadastros/cadastroProfessor.jsp">
                                                <center><button type="submit" name="Submit" value="Adicionar aluno">Cadastrar professor</button></center>    
                                            </form> 
                                        </td>
                                </body>
                            </table>
                              
                            <br>
                            ${alunos}
                        </div>
                    </div>  
                </div>
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
<% } %>

</body>
</html>