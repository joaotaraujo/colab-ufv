<%@page import="br.com.senacrs.dao.JDBCProjetoDAO"%>
<%@page import="javax.swing.JTextPane"%>
<%@page import="java.util.List"%>
<%@page import="br.com.senacrs.dao.ProjetoDAO"%>
<%@page import="br.com.senacrs.connections.DAOFactory"%>
<%@page import="br.com.senacrs.bean.ProjetoBean"%>
<%@page import="java.util.ArrayList"%>
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
        height: 310px; 
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
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaAlunos">Relat�rios</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/paginaInicial/cadastros/tipoDeCadastro.jsp">Cadastros</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaAtividades">Atividades Tarefas</a></li>
        <li class="menu_link"><a href="/ProjetoColabUFV/ServletCarregaForum">F�rum</a></li>
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
                <a href="/ProjetoColabUFV/ServletCarregaInformacoesProjeto"><h1>Informa��es do projeto</h1></a>
                <a href="/ProjetoColabUFV/ServletVizualizaCronograma"><h1>Vizualizar Cronograma</h1></a>
                <a href="/ProjetoColabUFV/ServletCarregaAlunos"><h1>Gerar um Relat�rio</h1></a>
                <a href="/ProjetoColabUFV/ServletCarregaGruposProjeto"><h1>Grupos</h1></a>
                <a href="/ProjetoColabUFV/ServletCarregaForum"><h1>F�rum</h1></a>
                <a href="/ProjetoColabUFV/ServletCarregaEnviaAlerta"><h1>Enviar um alerta</h1></a>
                <a href="/ProjetoColabUFV/paginaInicial/envios/enviarEmail.jsp"><h1>Enviar um email</h1></a>
                <a href="/ProjetoColabUFV/paginaInicial/cadastros/tipoDeCadastro.jsp"><h1>Cadastros</h1></a>         
                    <a href="/ProjetoColabUFV/paginaInicial/cadastros/cadastroAluno.jsp" class="link">Aluno</a> 
                    <a href="/ProjetoColabUFV/ServletCarregaCadastroTarefa" class="link">Atividade</a>
                    <a href="/ProjetoColabUFV/paginaInicial/cadastros/cadastroGrupo.jsp" class="link">Grupo</a>
                    <a href="/ProjetoColabUFV/paginaInicial/cadastros/cadastroPerguntaFrequente.jsp" class="link">Pergunta Frequente</a>
                    <a href="/ProjetoColabUFV/ServletCarregaCadastroTarefa" class="link">Tarefa</a>
                <a href="/ProjetoColabUFV/ServletCarregaPerguntasFrequentes"><h1>Perguntas Frequentes</h1></a>
            </div><br>
            <div class="containerRanking"><center style="padding-top:10px;">
                    <strong>Aluno Destaque</strong></center><br> 
                        Nome: ${nomeAlunoRanking}<br>
                        Atv Conclu�das: ${quantAtividadesConcluidas}
                    </a>
                    </div>
            <div class="buttonChat" bgcolor=\"FF0000\"><center><a href="/ProjetoColabUFV/start.jsp">Chat Online!</a></center></div>
        </div>
      
        <div id="right">
            <div id="welcome">
                    <form method="post" action="/ProjetoColabUFV/ServletCadastraPerguntaFrequente">
                        <table border="1" width="45%" cellpadding="5">
                            <thead>
                                <tr>
                                    <th colspan="2"><h3>Cadastrar pergunta frequente</h3></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td style="width:20%;">Pergunta</td>
                                    <td style="width:80%;"><input size=75 type="text" name="pergunta" value="" /></td>
                                </tr>
                                <tr height=85>
                                    <td style="width:20%;">Resposta</td>
                                    <td style="width:80%;"><textarea rows="5" cols="55" maxlength="500" type="text" name="resposta" value="" /></textarea></td>
                                </tr>
                                <tr>
                                <td><button type="submit" name="Submit" value="Cadastrar pergunta frequente">Cadastrar</button></td>  
                                <td><button type="reset">Resetar</button></td>
                                </tr>
                            </tbody>
                        </table>
                        <p>${mensagemCadastraPerguntaFrequente}</p>
                    </form>
                    <br>
                        
                    <form method="post" action="/ProjetoColabUFV/paginaInicial/cadastros/tipoDeCadastro.jsp">
                    <button type="submit" name="Submit" value="voltar">Voltar</button></form> 
                    
            </div> 
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
