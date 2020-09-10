<html>
    <head>
        <title>ColabUFV</title>
    <link rel='shortcut icon' href="/ProjetoColabUFV/imagens/brasaoUFV.jpg" /> 
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        
    </head>
    <body>
        <center>
            <img src="imagens/brasaoUFV.jpg" width="249" height="202" alt="brasaoUFV"/>

            <form name="formCad" action="ServletLogin" method="POST">
                <table border="0">
                    <thead>
                        <th colspan="2"> Autenticação</th>
                    </thead>
                
                    <tbody>
                        <tr>
                            <td>Login:</td>
                            <td><input type="text" name="login" value="" size="20" /></td>
                        </tr>
                        <tr>
                            <td>Senha:</td>
                            <td><input type="password" name="senha" value="" size="20"/></td>
                        </tr>
                        <tr>
                            <td><input type="submit" value="Entrar" name="Logar" /></td>
                        </tr>
                    </tbody>
                </table>
                <p>${mensagem}</p>
            </form>
        </center>
    </body>
</html>
