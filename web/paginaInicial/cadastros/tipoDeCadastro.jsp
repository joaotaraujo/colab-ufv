<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" media="screen" type="text/css" href="/ProjetoColabUFV/css/style.css"  />
    <title>ColabUFV</title>
    <link rel='shortcut icon' href="/ProjetoColabUFV/imagens/brasaoUFV.jpg" /> 
</head>
<body>
    
    <style>
        #welcome {
            background-color: #F5F5F5;
            width: 700px;
            height: 350px; 
            padding: 100px;
            border: #000000 solid 4px;
            padding-left: 0px;
            padding-top: 0px;
            padding-bottom: 0px;
            padding-right: 10px;
            position:static;
        }
        td {
            padding: 0px 0px;
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
            <div class="buttonChat" bgcolor="BCC5FF"><center><a href="/ProjetoColabUFV/start.jsp">Chat Online!</a></center></div>
        </div>
      
      
        <div id="right">
            <div id="welcome">
            
                    <table border="1" width="45%" cellpadding="2">
                            <thead>
                                <tr>
                                    <th colspan="1"><h3><center>Cadastros</center></h3></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <a href="cadastroAluno.jsp" class="link">Cadastrar Aluno</a>
                                    </td>
                                </tr>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="/ProjetoColabUFV/ServletCarregaCadastroAtividade" class="link">Cadastrar Atividade</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="cadastroGrupo.jsp" class="link">Cadastrar Grupo</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="/ProjetoColabUFV/ServletCarregaCadastroTarefa?veioDeOnde=tipoDeCadastro" class="link">Cadastrar Tarefa</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="cadastroPapel.jsp" class="link">Cadastrar Papel</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="cadastroPerguntaFrequente.jsp" class="link">Cadastrar Pergunta Frequente</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="cadastroProfessor.jsp" class="link">Cadastrar Professor</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="cadastroProjeto.jsp" class="link">Cadastrar Projeto</a>
                                    </td>
                                </tr>
                            </tbody>
                    </table>
      
                <div class="clear"></div>
            </div>
        
            <div id="block_left">
      
            </div>
      
            <div id="block_right">
           
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