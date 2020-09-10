<html>
    <head>
        <title>ColabUFV</title>
        <link rel='shortcut icon' href="/ProjetoColabUFV/imagens/brasaoUFV.jpg" /> 
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        
    </head>
    <body>
        <%if ((session.getAttribute("login") == null) || (session.getAttribute("login") == "")) {%>
        Você não está logado.<br/>
        <a href="/ProjetoColabUFV/index.jsp">Logar!</a>
        <%} else {%> 
        <center>
            <img src="imagens/brasaoUFV.jpg" width="249" height="202" alt="brasaoUFV"/>
            <br><h1> Escolha um projeto </h1>
            <form name="formCad" action="/ProjetoColabUFV/ServletLogaUsuario" method="POST">
                        <select name = "projetoEscolhido" style="width: 150px">
                        ${projetosExistentes}
                        </select>
                        <button style="width: 120px" type="submit" name="Submit" value="Escolher projeto">Escolher projeto</button>
                        
            </form>
                        
            <strong><a href="/ProjetoColabUFV/ServletLogout">LOGOUT</a></strong>
        </center>
    <%
    }
%>
    </body>
</html>
