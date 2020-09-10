package br.com.senacrs.dao;

import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.ConnectionFactory;
import br.com.senacrs.connections.DAOFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCGrupoDAO implements GrupoDAO{

    Connection connection;

    public JDBCGrupoDAO() {
        connection = null;
    }
    
    @Override
    public void cadastrarGrupo(GrupoBean grupo, String nomeProjeto) {
        
        try {
            
            String SQL = "SELECT id FROM grupo ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idGrupo=0;
            while(rs.next()){
                idGrupo = rs.getInt("id")+1;
            }
            
            ps.close();
            
            SQL = "SELECT id FROM projeto WHERE nome = ?";
            ps = connection.prepareStatement(SQL);
            ps.setString(1, nomeProjeto);
            rs = ps.executeQuery();
            int idProjeto=0;
            rs.next();
            idProjeto = rs.getInt("id");
                
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO grupo (id, nome, descricao, idProjeto) VALUES"
                    + "(?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, idGrupo);
            ps.setString(2,grupo.getNome());
            ps.setString(3,grupo.getDescricao());
            ps.setInt(4,idProjeto);
            
            ps.executeUpdate();
            ps.close();
            connection.close();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCGrupoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void deletarGrupo(String nome) {
        try {
            
            String SQL = "SELECT id FROM grupo WHERE nome = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            int idGrupo=0;
            while(rs.next())
            idGrupo = rs.getInt("id");
                
            ps.close();
            rs.close();
            
            SQL = "DELETE FROM usuarioxgrupo WHERE idGrupo= ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, idGrupo);
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM grupo WHERE nome= ?";
            ps = connection.prepareStatement(SQL);
            ps.setString(1, nome);
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCGrupoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover um grupo", ex);
        }
        
    }

    @Override
    public ArrayList<GrupoBean> listarGrupos(String NomeProjeto) {
        ArrayList<GrupoBean> grupos = new ArrayList<GrupoBean>();
        try {
            String SQL = "SELECT nome FROM grupo WHERE idProjeto = (SELECT id FROM projeto WHERE nome = ?)";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, NomeProjeto);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<String> nomesGrupo = new ArrayList<String>();
            while(rs.next()){
                nomesGrupo.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0;i<nomesGrupo.size();i++){
                GrupoBean grupo = this.pesquisarGrupo(nomesGrupo.get(i), NomeProjeto, 1);
                grupos.add(grupo);
            }
            
            return grupos;
            
        } catch (SQLException ex) {
            ArrayList<GrupoBean> grupoNulo = new ArrayList<GrupoBean>();
            
            return grupoNulo;
        }
    }

    /* tipo:
        1- por nome
        2- por id
    */
    @Override
    public GrupoBean pesquisarGrupo(String informacao, String nomeProjeto, int tipo) {
        try {
            
            if(tipo == 1){

                String SQL = "SELECT * FROM grupo WHERE nome = ?";
                if(connection==null || connection.isClosed())
                connection = ConnectionFactory.getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);
                ResultSet rs = ps.executeQuery();
                GrupoBean grupo = new GrupoBean();
                int count =0 ;
                while(rs.next()){
                    count++;
                grupo.setNome(rs.getString("nome"));
                grupo.setDescricao(rs.getString("descricao"));
                grupo.setId(rs.getInt("id"));
                grupo.setIdProjeto(rs.getInt("idProjeto"));
                }
                
                ps.close();
                rs.close();
                connection.close();
                
                if(count!=0)
                grupo.setIntegrantes(this.listarIntegrantes(grupo.getId()));
                
                return grupo;
            }
            else if(tipo == 2){
                String SQL = "SELECT * FROM grupo WHERE id = ?";
                if(connection==null || connection.isClosed())
                connection = ConnectionFactory.getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);
                ResultSet rs = ps.executeQuery();
                rs.next();
                GrupoBean grupo = new GrupoBean();
                grupo.setNome(rs.getString("nome"));
                grupo.setDescricao(rs.getString("descricao"));
                grupo.setId(rs.getInt("id"));
                grupo.setIdProjeto(rs.getInt("idProjeto"));
                
                ps.close();
                rs.close();
                connection.close();
                
                grupo.setIntegrantes(this.listarIntegrantes(grupo.getId()));
                
                return grupo;
            }
            
            return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCGrupoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    @Override
    public void editarGrupo(GrupoBean grupo) {
        try { 
            String SQL = "update grupo set nome=?, descricao=?, idProjeto=?  where id=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            
            ps.setString(1,grupo.getNome());
            ps.setString(2,grupo.getDescricao());
            ps.setInt(3, grupo.getIdProjeto());
            ps.setInt(4, grupo.getId());
            ps.execute();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCGrupoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao editar um grupo pelo id", ex);
        }
    
    }

    
    @Override
    public String retornarGrupoTabela(GrupoBean grupo, String nomeProjeto, String redirect){
            String grupoString = "<table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações do grupo</center></h3></th>"
                         +"</tr></thead><tbody><tr><td width=\"15%\"><strong>Nome:</strong></td><td width=\"35%\"><strong> "+grupo.getNome()+"</strong> </td><td width=\"15%\"><strong>Projeto:</strong></td><td width=\"35%\" > <strong>"+nomeProjeto+"</strong> </td><tr height=85><td COLSPAN=\"1\"><strong>Descrição:</strong></td><td COLSPAN=\"3\"><textarea name=\"\" id=\"\" cols=\"60\" rows=\"4\"> "+grupo.getDescricao()+"</textarea></td>"
                                +"</tr></tbody></table>";
           if(redirect.equalsIgnoreCase("redirectProjeto"))
                grupoString = grupoString+"       <form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesProjeto\">";
            else if(redirect.equalsIgnoreCase("redirectGrupos"))
                grupoString = grupoString+"       <form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaGruposProjeto\">";
            grupoString = grupoString + "       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
            
            return grupoString;
    }
    
   
    @Override
    public String retornaProjetoGrupo(GrupoBean grupo){
        try {
            String SQL = "SELECT p.nome FROM grupo g RIGHT JOIN projeto p ON g.idProjeto = p.id WHERE g.id = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps= connection.prepareStatement(SQL);
        
            ps.setInt(1,grupo.getId());
            ResultSet rs = ps.executeQuery();
            
            String nomeProjeto = "";
            while(rs.next()){
                nomeProjeto = rs.getString("nome");
            }
            ps.close();
            rs.close();
            connection.close();
            
            return nomeProjeto;
            
     } catch (SQLException ex) {
            Logger.getLogger(JDBCGrupoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
     }
     
     
    @Override
    /* Informacao:
        1- deleta e vai pra tela de grupos
        2- deleta e vai pra tela de informacoes do projeto
    */
    public String retornaGruposTabela(ArrayList<GrupoBean> grupos, int informacao){
        String gruposString = "";
                
            gruposString = "<table id=\"gruposTabela\" class=\"display\" cellspacing=\"0\" width=\"100%\"><thead><tr><th>Nome</th><th>Descrição</th><th>Edit</th><th>Del</th>"
                         +"</tr></thead><tbody>";
            for(int i=0;i<grupos.size();i++){
                gruposString = gruposString + "<tr><td style=\"width:40%;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesGrupo?grupoParaCarregar="+grupos.get(i).getNome()+"\"> "+grupos.get(i).getNome()+" </a></td>"
                            +  "<td style=\"width:50%;\">"+grupos.get(i).getDescricao()+"</td><td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-left:15px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaGrupo?idGrupoEscolhido="+grupos.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"
                        +"<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:15px;\">";
                if(informacao==1)
                    gruposString=gruposString+"<a href=\"/ProjetoColabUFV/ServletDeletaGrupo?grupoEscolhido="+grupos.get(i).getNome()+"&veioDeOnde=redirectGrupos\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                else if (informacao == 2)    
                    gruposString=gruposString+"<a href=\"/ProjetoColabUFV/ServletDeletaGrupo?grupoEscolhido="+grupos.get(i).getNome()+"&veioDeOnde=redirectInformacaoProjeto\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                
            }
            gruposString = gruposString + "</tbody></table>";
            
            return gruposString;
    }
     
    @Override
    public String retornarGrupoTabelaEditavel(GrupoBean grupo, String nomeProjeto, String redirect){
        
        
            String grupoString ="";
            if(redirect.equalsIgnoreCase("redirectProjeto"))
                grupoString = "       <form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesProjeto\">";
            else if(redirect.equalsIgnoreCase("redirectGrupos"))
                grupoString = "       <form method=\"post\" action=\"/ProjetoColabUFV/ServletEditaGrupo\">";
            
            
            grupoString=grupoString+ "<table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações do grupo</center></h3></th>"
                         +"</tr></thead><tbody><tr><td WIDTH=\"15%\"><strong>Nome:</strong></td><td WIDTH=\"35%\"> <input type=\"text\" size=30 name=\"nomeEditado\" value=\""+grupo.getNome()+"\"> </td><td WIDTH=\"15%\"><strong>Projeto:</strong></td><td WIDTH=\"35%\" > "+nomeProjeto+" </td><tr height=85><td COLSPAN=\"1\" WIDTH=\"15%\"><strong>Descrição:</strong></td><td COLSPAN=\"3\"><input size=75 type=\"text\" name=\"descricaoEditada\" value=\""+grupo.getDescricao()+"\"></td>"
                                +"</tr></tbody></table>"
                     + "<button style=\"width: 150px\" type=\"submit\" name=\"Submit\" value=\"Concluir editar grupo\">Concluir edição</button>"    
                        + "</form><br>";
           
            
            return grupoString;
    }
    
    @Override
    public String cadastrarUsuarioEmGrupo(UsuarioBean usuario, GrupoBean grupo, ProjetoBean projeto){
    
        try {
            GrupoDAO gd = DAOFactory.createGrupoDAO();
            grupo.setIntegrantes(gd.retornaEnvolvidosGrupo(grupo, projeto.getNome()));
            for(int i=0; i<grupo.getIntegrantes().size();i++){
                if(grupo.getIntegrantes().get(i).getNome().equals(usuario.getNome())){
                    return "Usuario ja esta no grupo!";
                }
            }
            
            for(int i=0; i<projeto.getGrupos().size();i++){
                for(int j=0; j<projeto.getGrupos().get(i).getIntegrantes().size();j++){
                    if(projeto.getGrupos().get(i).getIntegrantes().get(j).getNome().equals(usuario.getNome())){
                        return "Usuario ja esta em outro grupo!";
                    }
                }
            }
            
            String SQL = "SELECT id FROM usuarioxgrupo ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int quantUsuarioxGrupo=0;
            while(rs.next()){
                quantUsuarioxGrupo = rs.getInt("id")+1;
            }
            
            ps.close();
            
            SQL = "INSERT INTO usuarioxgrupo (idUsuario, idGrupo, id) VALUES"
                    + "(?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1,usuario.getId());
            ps.setInt(2,grupo.getId());
            ps.setInt(3,quantUsuarioxGrupo);
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
            return "";
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        
    }
    
    @Override
    public ArrayList<UsuarioBean> listarIntegrantes(int idGrupo) {
       
        ArrayList<UsuarioBean> integrantes = new ArrayList<UsuarioBean>();
        try {
            
            String SQL = "SELECT idUsuario FROM usuarioxgrupo WHERE idGrupo = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1,idGrupo);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<Integer> integrantesIds = new ArrayList<Integer>();
            while(rs.next()){
                integrantesIds.add(rs.getInt("idUsuario"));
            }
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            for(int i=0; i<integrantesIds.size();i++){
                UsuarioBean usuario = ud.buscar(Integer.toString(integrantesIds.get(i)),5);
                integrantes.add(usuario);
            }
            
            return integrantes;
            
        } catch (SQLException ex) {
            ArrayList<UsuarioBean> semIntegrantes = new ArrayList<UsuarioBean>();
            return semIntegrantes;
        }
    }

    
    public ArrayList<UsuarioBean> retornaEnvolvidosGrupo(GrupoBean grupo, String nomeProjeto){
        try{
            
            String SQL = "SELECT nome, email, login, senha, matricula, idUsuario, tipoUsuario FROM usuarioxgrupo ug RIGHT JOIN usuario u ON ug.idUsuario = u.id WHERE idGrupo = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, grupo.getId());
            ResultSet rs = ps.executeQuery();
            ArrayList<UsuarioBean> envolvidos = new ArrayList<UsuarioBean>();
            
            ArrayList<String> integrantesString = new ArrayList<String>();
            while(rs.next()){
                integrantesString.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            for(int i=0; i<integrantesString.size();i++){
                UsuarioBean usuario = ud.buscar(integrantesString.get(i),1);
                envolvidos.add(usuario);
            }
            
            return envolvidos;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCGrupoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        
    }
    
    @Override
    public void deletarUsuarioGrupo(UsuarioBean usuario, GrupoBean grupo){
        try {
            String SQL = "DELETE FROM usuarioxgrupo WHERE idUsuario = ? AND idGrupo = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
        
            ps.setInt(1, usuario.getId());
            ps.setInt(2, grupo.getId());
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCGrupoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public ArrayList<UsuarioBean> retornarUsuariosSemGrupo(){
         try {
            
            ArrayList<UsuarioBean> usuariosSemGrupo =  new ArrayList<UsuarioBean>();
            String SQL = "SELECT * FROM usuario u LEFT JOIN usuarioxgrupo ug ON u.id = ug.idUsuario WHERE idUsuario IS null";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<String> usuariosSemGrupoString = new ArrayList<String>();
            while(rs.next()){
                usuariosSemGrupoString.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            for(int i=0; i<usuariosSemGrupoString.size();i++){
                UsuarioBean usuario = ud.buscar(usuariosSemGrupoString.get(i),1);
                usuariosSemGrupo.add(usuario);
            }
            
            return usuariosSemGrupo;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCGrupoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }   
    
    @Override
    public ArrayList<UsuarioBean> retornaNaoEnvolvidosGrupo(GrupoBean grupo, String nomeProjeto){
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ArrayList<UsuarioBean> envolvidosProjeto= projd.listarEnvolvidosNoProjeto(nomeProjeto);
            ArrayList<UsuarioBean> integrantesGrupo= this.listarIntegrantes(grupo.getId());
            ArrayList<UsuarioBean> integrantesNaoGrupo = envolvidosProjeto;
            
            
            for(int j=0;j<integrantesGrupo.size();j++){
                for(int i=0;i<envolvidosProjeto.size();i++){
                    if(envolvidosProjeto.get(i).getNome().equalsIgnoreCase(integrantesGrupo.get(j).getNome())){
                        integrantesNaoGrupo.remove(envolvidosProjeto.get(i));
                    }
                }
            }
            return integrantesNaoGrupo;
        
    }
    
    
    @Override
    public boolean usuarioEstaEmGrupo(String nomeUsuario, String nomeGrupo, String nomeProjeto){
        GrupoBean grupo = this.pesquisarGrupo(nomeGrupo,nomeProjeto,1);
        ArrayList<UsuarioBean> integrantes = grupo.getIntegrantes();
        
        for(int i=0; i<integrantes.size();i++){
            if(integrantes.get(i).getNome().equalsIgnoreCase(nomeUsuario)){
                return true;
            }
        }
        return false;
    }
   
}
