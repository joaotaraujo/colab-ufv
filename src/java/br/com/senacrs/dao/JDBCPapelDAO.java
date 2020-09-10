package br.com.senacrs.dao;

import br.com.senacrs.bean.PapelBean;
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

public class JDBCPapelDAO implements PapelDAO{
    
    Connection connection;
    public JDBCPapelDAO() {
        connection = null;
    }
    
    @Override
    public void cadastrar(PapelBean papel, int idProjeto) {
        
        try {
            
            String SQL = "SELECT id FROM papel ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idPapel = 0;
            while(rs.next()){
                idPapel = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO papel (id, idProjeto, funcao, descricao) VALUES"
                    + "(?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, idPapel);
            ps.setInt(2,idProjeto);
            ps.setString(3,papel.getDescricao());
            ps.setString(4,papel.getFuncao());
             
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPapelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void deletar(int idPapel) {
        try {
            
            String SQL = "DELETE FROM usuarioxpapel WHERE idPapel = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idPapel);
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM papel WHERE id= ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, idPapel);
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPapelDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover um papel", ex);
        }
        
    }

    @Override
    public ArrayList<PapelBean> listarPapeis(String nomeProjeto) {
        ArrayList<PapelBean> papeis = new ArrayList<PapelBean>();
        try {
            
            String SQL = "SELECT id FROM projeto WHERE nome = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, nomeProjeto);
            ResultSet rs = ps.executeQuery();
            int idProjeto =0 ;
            while(rs.next()){
                idProjeto = rs.getInt("id");
            }
            
            ps.close();
            rs.close();
            
            SQL = "SELECT * FROM papel WHERE idProjeto = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, idProjeto);
            rs = ps.executeQuery();
            ArrayList<Integer> papeisId = new ArrayList<Integer>();
            while(rs.next()){
                papeisId.add(rs.getInt("id"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0; i<papeisId.size();i++){
                PapelBean papel = this.buscar(Integer.toString(papeisId.get(i)), 2);
                papeis.add(papel);
            }
            
            return papeis;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPerguntaFrequenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar perguntas frequentes em JDBCPerguntaFrequenteDAO", ex);
        }
    }

    @Override
    /*Se tipo:
        - 1: pesquisa por funcao
        - 2: pesquisa por id
    */
    public PapelBean buscar(String informacao, int tipo) {
        try {
            PapelBean papel = new PapelBean();
            
            String SQL = "";
            PreparedStatement ps = null; 
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            if(tipo == 1){
                SQL = "SELECT * FROM papel WHERE funcao = ?";
                ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);
            } else if(tipo == 2){
                SQL = "SELECT * FROM papel WHERE id = ?";
                ps = connection.prepareStatement(SQL);
                ps.setInt(1, Integer.parseInt(informacao));
            }
            
            
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            papel.setId(rs.getInt("id"));
            papel.setIdProjeto(rs.getInt("idProjeto"));
            papel.setFuncao(rs.getString("funcao"));
            papel.setDescricao(rs.getString("descricao"));
            
            ps.close();
            rs.close();
            connection.close();
            papel.setEnvolvidos(this.listarEnvolvidosPapel(papel.getId()));
            
            return papel;
            
        } catch (SQLException ex) {
            //Logger.getLogger(JDBCPapelDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    @Override
    public void editar(PapelBean papel) {
        try { 
            String SQL = "update papel set idProjeto=?, funcao=?, descricao=? where id=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, papel.getIdProjeto());
            ps.setString(2, papel.getFuncao());
            ps.setString(3, papel.getDescricao());
            ps.setInt(4, papel.getId());
            ps.execute();
            
            ps.close();
            connection.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPapelDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao buscar um registro de papel pelo id", ex);
        }
    
    }

    @Override
    public ArrayList<UsuarioBean> listarEnvolvidosPapel(int idPapel) {
        ArrayList<UsuarioBean> envolvidos = new ArrayList<UsuarioBean>();
        try {
            
            String SQL = "SELECT * FROM usuarioxpapel WHERE idPapel = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idPapel);
            ResultSet rs = ps.executeQuery();
            
            
            ArrayList<Integer> envolvidosId = new ArrayList<Integer>();
            while(rs.next()){
                envolvidosId.add(rs.getInt("idUsuario"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            for(int i=0; i<envolvidosId.size();i++){
                UsuarioBean usuario = ud.buscar(Integer.toString(envolvidosId.get(i)), 5);
                envolvidos.add(usuario);
            }
            
            return envolvidos;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPerguntaFrequenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar envolvidos em JDBCPapelDAO", ex);
        }
    }
    
    @Override
    public String retornaTabelaDePapeis(String nomeProjeto){
        
            String papeisString = "";
            ArrayList<PapelBean> papeisProjeto = this.listarPapeis(nomeProjeto);
            
            papeisString = "<table id=\"papeisTabela\" class=\"display\" cellspacing=\"0\" width=\"100%\"><thead><tr><th>Função</th><th>Descrição</th><th>Edit</th><th>Del</th>"
                         +"</tr></thead><tbody>";
            for(int i=0;i<papeisProjeto.size();i++){
               
                
                papeisString = papeisString + "<tr>"
                        + "<td><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesPapel?papelParaCarregar="+papeisProjeto.get(i).getFuncao()+"&tipoUsuario=1\"> "+papeisProjeto.get(i).getFuncao()+"</a> </td>"
                          +  "<td style=\"width:50%\">"+papeisProjeto.get(i).getDescricao()+"</td>"
                        +"<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaPapel?idPapelEscolhido="+papeisProjeto.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"
                       +"<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaPapel?papelEscolhido="+papeisProjeto.get(i).getFuncao()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                    
            }
            papeisString = papeisString + "</tbody></table>";
            
            return papeisString;
    
        
    }
    @Override
    public String retornarPapelTabela(PapelBean papel, String nomeProjeto){
        
            String papelString = "<table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações do papel</center></h3></th>"
                            +"</tr></thead><tbody><tr><td width=\"15%\"><strong>Função:</strong></td><td width=\"35%\"> "+papel.getFuncao()+" </td><td width=\"15%\"><strong>Projeto:</strong></td><td width=\"35%\" > "+nomeProjeto+" </td><tr height=85><td COLSPAN=\"1\"><strong>Descrição:</strong></td><td COLSPAN=\"3\"><textarea name=\"\" id=\"\" cols=\"60\" rows=\"4\"> "+papel.getDescricao()+"</textarea></td>"
                            +"</tr></tbody></table>";
           
            
            return papelString;
    }
    
    @Override
    public String retornarPapelTabelaEditavel(PapelBean papel, String nomeProjeto){
        
        
            String grupoString ="<form method=\"post\" action=\"/ProjetoColabUFV/ServletEditaPapel\">"            
                    +"<table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações do papel</center></h3></th>"
                         +"</tr></thead><tbody><tr><td width=\"15%\"><strong>Função:</strong></td><td width=\"35%\"> <input type=\"text\" name=\"funcaoEditada\" value=\""+papel.getFuncao()+"\"> </td><td width=\"15%\"><strong>Projeto:</strong></td><td width=\"35%\" > "+nomeProjeto+" </td><tr height=85><td COLSPAN=\"1\"><strong>Descrição:</strong></td><td COLSPAN=\"3\"><input type=\"text\" name=\"descricaoEditada\" size=\"80\" value=\""+papel.getDescricao()+"\"></td>"
                                +"</tr></tbody></table>"
                     + "<button style=\"width: 150px\" type=\"submit\" name=\"Submit\" value=\"Concluir editar papel\">Concluir edição</button>"    
                        + "</form><br>";
           
            
            return grupoString;
    }
    
    @Override
    public String cadastrarUsuarioEmPapel(UsuarioBean usuario, PapelBean papel, ProjetoBean projeto){
    
        try {
            
            papel.setEnvolvidos(this.listarEnvolvidosPapel(papel.getId()));
            for(int i=0; i<papel.getEnvolvidos().size();i++){
                if(papel.getEnvolvidos().get(i).getNome().equals(usuario.getNome())){
                    return "Usuario ja esta no papel!";
                }
            }
            
            for(int i=0; i<projeto.getPapeis().size();i++){
                for(int j=0; j<projeto.getPapeis().get(i).getEnvolvidos().size();j++){
                    if(projeto.getPapeis().get(i).getEnvolvidos().get(j).getNome().equals(usuario.getNome())){
                        return "Usuario ja esta em outro papel!";
                    }
                }
            }
            
            String SQL = "SELECT id FROM usuarioxpapel ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int quantUsuarioxPapel=0;
            while(rs.next()){
                quantUsuarioxPapel = rs.getInt("id")+1;
            }
            
            ps.close();
            
            SQL = "INSERT INTO usuarioxpapel (id, idUsuario, idPapel) VALUES"
                    + "(?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1,quantUsuarioxPapel);
            ps.setInt(2,usuario.getId());
            ps.setInt(3,papel.getId());
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
            return "";
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPapelDAO.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        
    }
    

    
    @Override
    public void deletarUsuarioPapel(UsuarioBean usuario, PapelBean papel){
        try {
            String SQL = "DELETE FROM usuarioxpapel WHERE idUsuario = ? AND idPapel = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
        
            ps.setInt(1, usuario.getId());
            ps.setInt(2, papel.getId());
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPapelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public ArrayList<UsuarioBean> retornarUsuariosSemPapel(){
         try {
            
            ArrayList<UsuarioBean> usuariosSemPapel =  new ArrayList<UsuarioBean>();
            String SQL = "SELECT * FROM usuario u LEFT JOIN usuarioxpapel up ON u.id = up.idUsuario WHERE idUsuario IS null";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<String> usuarioSemPapelNome = new ArrayList<String>();
            while(rs.next()){
                usuarioSemPapelNome.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            for(int i=0; i<usuarioSemPapelNome.size();i++){
                UsuarioBean usuario = ud.buscar(usuarioSemPapelNome.get(i), 1);
                usuariosSemPapel.add(usuario);
            }
            
            return usuariosSemPapel;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPapelDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }   
    
    @Override
    public ArrayList<UsuarioBean> retornaNaoEnvolvidosPapel(PapelBean papel, String nomeProjeto){
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ArrayList<UsuarioBean> envolvidosProjeto= projd.listarEnvolvidosNoProjeto(nomeProjeto);
            ArrayList<UsuarioBean> integrantesPapel= this.listarEnvolvidosPapel(papel.getId());
            ArrayList<UsuarioBean> integrantesNaoPapel = envolvidosProjeto;
            
            
            for(int j=0;j<integrantesPapel.size();j++){
                for(int i=0;i<envolvidosProjeto.size();i++){
                    if(envolvidosProjeto.get(i).getNome().equalsIgnoreCase(integrantesPapel.get(j).getNome())){
                        integrantesNaoPapel.remove(envolvidosProjeto.get(i));
                    }
                }
            }
            return integrantesNaoPapel;
        
    }
    
    
    @Override
    public boolean usuarioEstaEmPapel(String nomeUsuario, String funcaoPapel, String nomeProjeto){
        PapelBean papel = this.buscar(funcaoPapel, 1);
        ArrayList<UsuarioBean> integrantes = papel.getEnvolvidos();
        
        for(int i=0; i<integrantes.size();i++){
            if(integrantes.get(i).getNome().equalsIgnoreCase(nomeUsuario)){
                return true;
            }
        }
        return false;
    }
}
