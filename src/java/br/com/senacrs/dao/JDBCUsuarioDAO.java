
package br.com.senacrs.dao;

import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.PapelBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.TarefaBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.ConnectionFactory;
import br.com.senacrs.connections.DAOFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCUsuarioDAO implements UsuarioDAO{

    Connection connection;
    public JDBCUsuarioDAO( ){
        connection = null;
    }
    
    @Override
    /*tipoUsuario:
      1- Aluno
      2- Professor  
    */
    public void cadastrar(UsuarioBean usuario, int tipoUsuario) {
        
        try {
            
            String SQL = "SELECT id FROM usuario ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idUsuario = 0;
            while(rs.next()){
                idUsuario = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO usuario (nome, email, login, senha, matricula, id,tipoUsuario) VALUES"
                    + "(?,?,?,?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setString(1,usuario.getNome());
            ps.setString(2,usuario.getEmail());
            ps.setString(3,usuario.getLogin());
            ps.setString(4,usuario.getSenha());
            ps.setString(5,usuario.getMatricula());
            ps.setInt(6, idUsuario);
            
            if(tipoUsuario == 1)
                ps.setString(7, "aluno");
            else 
                ps.setString(7, "professor");
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

                                
    @Override
    public void deletar(String nome) {
        try {
            
            UsuarioBean usuario = this.buscar(nome, 1);
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "DELETE FROM usuarioxatividade WHERE FK_idUsuario = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM usuarioxtarefa WHERE FK_idUsuario = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM usuarioxalerta WHERE idUsuario = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM usuarioxpapel WHERE idUsuario = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            
            SQL = "DELETE FROM usuarioxgrupo WHERE idUsuario = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            
            SQL = "DELETE FROM mensagem WHERE idRemetente = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            
            SQL = "DELETE FROM email WHERE idRemetente = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM topico WHERE idAutor = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM atividade WHERE FK_responsavel = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM alerta WHERE idAutor = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM comentario WHERE idAutor = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM tarefa WHERE FK_responsavel = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM usuario WHERE nome= ?";
            ps = connection.prepareStatement(SQL);
            ps.setString(1, nome);
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover um usuario", ex);
        }
        
    }

    @Override
    public ArrayList<UsuarioBean> listarAlunos(String nomeProjeto) {
        ArrayList<UsuarioBean> alunos = new ArrayList<UsuarioBean>();
        PapelDAO pd = DAOFactory.createPapelDAO();
        ArrayList<PapelBean> papeisProjeto = pd.listarPapeis(nomeProjeto);
        for(int i=0;i<papeisProjeto.size();i++){
            ArrayList<UsuarioBean> envolvidosPapel = papeisProjeto.get(i).getEnvolvidos();
            for(int j=0;j<envolvidosPapel.size();j++){
                if(envolvidosPapel.get(j).getTipoUsuario().equalsIgnoreCase("aluno"))
                    alunos.add(envolvidosPapel.get(j));
            }
        }
        return alunos;
    }

    @Override
    public ArrayList<UsuarioBean> listarProfessores(String nomeProjeto) {
        ArrayList<UsuarioBean> professores = new ArrayList<UsuarioBean>();
        PapelDAO pd = DAOFactory.createPapelDAO();
        ArrayList<PapelBean> papeisProjeto = pd.listarPapeis(nomeProjeto);
        for(int i=0;i<papeisProjeto.size();i++){
            ArrayList<UsuarioBean> envolvidosPapel = papeisProjeto.get(i).getEnvolvidos();
            for(int j=0;j<envolvidosPapel.size();j++){
                if(envolvidosPapel.get(j).getTipoUsuario().equalsIgnoreCase("professor"))
                    professores.add(envolvidosPapel.get(j));
            }
        }
        return professores;
    }
    
    /*Posso buscar por:
        (1) nome
        (2) matricula
        (3) login
        (4) email
        (5) id
    */
    @Override
    public UsuarioBean buscar(String informacao, int tipo) {
        try {
            UsuarioBean usuario = new UsuarioBean();
            String SQL = null;
            PreparedStatement ps = null;
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            if(tipo==1){
                SQL = "SELECT * FROM usuario WHERE nome = ?";
                ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);
            }
            else if(tipo==2){
                SQL = "SELECT * FROM usuario WHERE matricula = ?";
                ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);     
            }
            else if(tipo==3){
                SQL = "SELECT * FROM usuario WHERE login = ?";
                ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);
            }
            else if(tipo==4){
                SQL = "SELECT * FROM usuario WHERE email = ?";
                ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);
            }
            else if(tipo==5){
                SQL = "SELECT * FROM usuario WHERE id = ?";
                ps = connection.prepareStatement(SQL);
                ps.setInt(1, Integer.parseInt(informacao));
            }
            
            
            ResultSet rs = ps.executeQuery();
            
            rs.next();
            usuario.setNome(rs.getString("nome"));
            usuario.setEmail(rs.getString("email"));
            usuario.setLogin(rs.getString("login"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setMatricula(rs.getString("matricula"));
            usuario.setId(rs.getInt("id"));
            usuario.setTipoUsuario(rs.getString("tipoUsuario"));
            
            ps.close();
            rs.close();
            connection.close();
            
            return usuario;
            
        } catch (SQLException ex) {
            //Logger.getLogger(JDBCAlunoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    @Override
    public void editar(UsuarioBean usuario) {
        try { 
            String SQL = "update usuario set nome=?, email=?, login=?, senha=?, matricula=?, tipoUsuario=? where id=?";
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getLogin());
            ps.setString(4, usuario.getSenha());
            ps.setString(5, usuario.getMatricula());
            ps.setString(6, usuario.getTipoUsuario());
            ps.setInt(7, usuario.getId());
            
            ps.execute();
            
            ps.close();
            connection.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(JDBCUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao buscar um registro de usuario pelo id", ex);
        }
    
    }
    
    /* Redirect
        1- grupo
        2- tarefa
        3- atividade
        4- papel
        5- projeto
        6- relatorio
    */
    @Override
    public String retornarUsuarioTabela(UsuarioBean usuario, int redirectPage, String informacao){
            
        String tabelaUsuario = "";    
        if(usuario.getTipoUsuario().equalsIgnoreCase("aluno")){
               
                tabelaUsuario = "<table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações do aluno</center></h3></th>"
                          +"</tr></thead><tbody><tr><td WIDTH=\"15%\"><strong>Nome:</strong></td><td WIDTH=\"45%\"> "+usuario.getNome()+" </td></tr><tr><td WIDTH=\"20%\"><strong>Matrícula:</strong></td><td WIDTH=\"20%\"> "+usuario.getMatricula()+" </td></tr><tr><td WIDTH=\"20%\"><strong>Email:</strong></td><td WIDTH=\"40%\">"+usuario.getEmail()+"</td></tr><tr>"
                          +"<td><strong>Login:</strong></td><td  WIDTH=\"20%\"> "+usuario.getLogin()+" </td></tr>"
                          +"</tbody></table>";
            
                if(redirectPage==1){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesGrupo?grupoParaCarregar="+informacao+"\">"
                                          +"       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
                else if(redirectPage==2){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+informacao+"\">"
                                          + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
                else if(redirectPage==3){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesAtividade?atividadeParaCarregar="+informacao+"\">"
                                              + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
                else if(redirectPage==4){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesPapel?papelParaCarregar="+informacao+"\">"
                                              + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
                else if(redirectPage==5){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesProjeto\">"
                                              + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
                else if(redirectPage==6){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaRelatorioGeral\">"
                                              + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
            }
        else if(usuario.getTipoUsuario().equalsIgnoreCase("professor")){
            
            tabelaUsuario = "<table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3>Informações do professor</h3></th>"
                            + "</tr></thead><tbody><tr><td WIDTH=\"20%\">Nome</td><td WIDTH=\"40%\"> "+usuario.getNome()+" </td></tr><tr><td WIDTH=\"20%\">Matrícula</td><td WIDTH=\"20%\"> "+usuario.getMatricula()+" </td></tr><tr><td WIDTH=\"20%\">Email</td><td WIDTH=\"40%\">"+usuario.getEmail()+"</td></tr><tr>"
                            + "<td>Login</td><td WIDTH=\"20%\"> "+usuario.getLogin()+" </td></tr>"
                            + "</tbody></table>";
            
            /*redirect não pode ser 3 porque uma atividade ou tarefa não possui professores, ou seja, ele não poderá ter vindo ver suas 
            informações, a partir da página de atividades e tarefas
            */
            if(redirectPage==1){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesGrupo?grupoParaCarregar="+informacao+"\">"
                                          +"       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
                else if(redirectPage==2){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+informacao+"\">"
                                          + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
                else if(redirectPage==3){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesAtividade?atividadeParaCarregar="+informacao+"\">"
                                              + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
                else if(redirectPage==4){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesPapel?papelParaCarregar="+informacao+"\">"
                                              + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
                else if(redirectPage==5){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesProjeto\">"
                                              + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
                else if(redirectPage==6){
                    tabelaUsuario = tabelaUsuario + "<br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaRelatorioGeral\">"
                                              + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
                }
        }
        
        return tabelaUsuario;
    }
    
    @Override
    /* Informacao:
        1 - icone de remover um envolvido na atividade
        2 - icone de remover um envolvido na tarefa
        3 - icone de remover um envolvido no grupo
        4 - icone de remover um envolvido no papel
        5 - icone de remover um envolvido no projeto
    */
    public String retornaTabelaDeEnvolvidos(ArrayList<UsuarioBean> envolvidos, int idProjeto, int informacao){
        
            String usuariosString = "";
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            
            usuariosString = "<table id=\"usuariosTabela\" onLoad=\"instanciaTabelaAluno()\" cellspacing=\"0\" width=\"100%\"><thead><tr><th>Nome</th><th>Papel</th><th>Email</th>";
                    
            if(informacao ==5)  usuariosString=usuariosString+"<th>Edit</th>";       
                    
            usuariosString=usuariosString + "<th>Del</th>"
                         +"</tr></thead><tbody>";
            for(int i=0;i<envolvidos.size();i++){
               
                
                usuariosString = usuariosString + "<tr>"
                        + "<td style=\"width:30%;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesUsuario?usuarioParaCarregar="+envolvidos.get(i).getNome()+"&tipoUsuario=1\"> "+envolvidos.get(i).getNome()+"</a> </td>"
                        +"<td style=\"width:25%;\">"+projd.retornaPapelUsuarioEmProjeto(envolvidos.get(i), idProjeto)+"</td>"
                        + "<td style=\"width:40%;\"> "+envolvidos.get(i).getEmail()+" </td>";
                
                if(informacao==1)
                    usuariosString = usuariosString+"<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaEnvolvidoAtividade?envolvidoSelecionado="+envolvidos.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                else if(informacao==2)
                    usuariosString = usuariosString+"<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaEnvolvidoTarefa?envolvidoSelecionado="+envolvidos.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                else if(informacao==3)
                    usuariosString = usuariosString+"<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaEnvolvidoGrupo?envolvidoSelecionado="+envolvidos.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                else if(informacao==4)
                    usuariosString = usuariosString+"<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaEnvolvidoPapel?envolvidoSelecionado="+envolvidos.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                else if(informacao==5)
                    usuariosString = usuariosString+ "<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaUsuario?idUsuarioEscolhido="+envolvidos.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"
                            + "<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaEnvolvidoProjeto?envolvidoSelecionado="+envolvidos.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\" onclick=\"javascript:return confirm('Você realmente deseja remover o aluno do sistema? (para remover um aluno de um projeto, basta apenas removê-lo do papel que ele está exercendo neste projeto, se clicar em OK, todas as informações do aluno serão apagadas do sistema)')\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                    
            }
            usuariosString = usuariosString + "</tbody></table>";
            
            return usuariosString;
    
        
    }
    
    @Override
    public String retornarUsuarioTabelaEditavel(UsuarioBean usuario){
        String usuarioString = "";
                
        usuarioString = "<form method=\"post\" action=\"/ProjetoColabUFV/ServletEditaUsuario\">"
                        + "<table border=\"1\" width=\"30%\" cellpadding=\"2\">\n" +
"                            <thead>\n" +
"                                <tr>\n" +
"                                    <th colspan=\"2\"><h3><center>Informações do usuário</center></h3></th>\n" +
"                                </tr>\n" +
"                            </thead>\n" +
"                            <tbody>\n" +
"                                <tr>\n" +
"                                    <td width=\"15%\"><strong>Nome:</strong></td>\n" +
"                                    <td><input size=80 type=\"text\" name=\"nome\" value=\""+usuario.getNome()+"\" /></td>\n" +
"                                </tr>\n" +
"                                <tr>\n" +
"                                    <td width=\"15%\"><strong>Matricula:</strong></td>\n" +
"                                    <td><input size=80 type=\"text\" name=\"matricula\" value=\""+usuario.getMatricula()+"\" /></td>\n" +
"                                </tr>\n" +
"                                <tr>\n" +
"                                    <td width=\"15%\"><strong>Email:</strong></td>\n" +
"                                    <td><input size=80 type=\"text\" name=\"email\" value=\""+usuario.getEmail()+"\" /></td>\n" +
"                                </tr>\n" +
"                                <tr>\n" +
"                                    <td width=\"15%\"><strong>Login:</strong></td>\n" +
"                                    <td><input size=80 type=\"text\" name=\"login\" value=\""+usuario.getLogin()+"\" /></td>\n" +
"                                </tr>\n" +
"                                <tr>\n" +
"                                    <td width=\"15%\"><strong>Senha:</strong></td>\n" +
"                                    <td><input size=80 type=\"password\" name=\"senha\" value=\""+usuario.getSenha()+"\" /></td>\n" +
"                                </tr>\n" +
"                            </tbody>\n" +
"                        </table><br>"
                        + "<button style=\"width: 150px\" type=\"submit\" name=\"Submit\" value=\"Concluir editar usuario\">Concluir edição</button>"    
                        + "</form><br>";
            
            
            usuarioString = usuarioString + "<form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesProjeto\">"
             +"       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
            
            
            return usuarioString;
        }
    
    
    @Override
    public void validarAtividadesETarefas(UsuarioBean usuario, ProjetoBean projeto){
        
            ArrayList<AtividadeBean> atividadesProjeto = projeto.getAtividades();
            
            
            Date data = new Date(System.currentTimeMillis());  
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            String dataAtualString = formato.format(data);
            
            try {
                Date dataAtual = formato.parse((String)dataAtualString);
                AtividadeDAO ad = DAOFactory.createAtividadeDAO();
                for(int i=0;i<atividadesProjeto.size();i++){
                    if(atividadesProjeto.get(i).getDataTermino().compareTo(dataAtual)<0){
                        atividadesProjeto.get(i).setStatus("Atrasada");
                        ad.editar(atividadesProjeto.get(i));
                    }
                    ArrayList<TarefaBean> tarefasAtividade = ad.retornarTarefasAtividade(atividadesProjeto.get(i));
                    TarefaDAO td = DAOFactory.createTarefaDAO();
                    for(int j=0;j<tarefasAtividade.size();j++){
                        if(tarefasAtividade.get(j).getDataTermino().compareTo(dataAtual)<0){
                            tarefasAtividade.get(j).setStatus("Atrasada");
                            td.editar(tarefasAtividade.get(j));
                        }
                    }
                }

            } catch (ParseException ex) {
                System.out.println("Erro ao passar uma data para string em usuarioDAO");
                Logger.getLogger(JDBCUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }
    
    
}
