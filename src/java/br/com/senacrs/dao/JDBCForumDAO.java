package br.com.senacrs.dao;

import br.com.senacrs.bean.ForumBean;
import br.com.senacrs.connections.ConnectionFactory;
import br.com.senacrs.connections.DAOFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCForumDAO implements ForumDAO{

    Connection connection;
    public JDBCForumDAO() {
        connection = null;
    }
    
    @Override
    public void cadastrarForum(int idProjeto) {
        
        try {
            
            String SQL = "SELECT id FROM forum ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idForum = 0;
            while(rs.next()){
                idForum = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO forum (id, idProjeto) VALUES"
                    + "(?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, idForum);
            ps.setInt(2,idProjeto);
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCForumDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public ForumBean buscarForum(int idProjeto) {
        try {
            ForumBean forum = new ForumBean();
            
            
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "SELECT * FROM forum WHERE idProjeto = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idProjeto);
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            forum.setId(rs.getInt("id"));
            forum.setIdProjeto(rs.getInt("idProjeto"));
            
            ps.close();
            rs.close();
            connection.close();
            
            TopicoDAO topd = DAOFactory.createTopicoDAO();
            forum.setTopicos(topd.listarTopicos(forum.getId()));
            
            return forum;
            
        } catch (SQLException ex) {
            //Logger.getLogger(JDBCForumDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    @Override
    public void deletarForum(int idProjeto){
        try {
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ForumBean forum = projd.pesquisarProjeto(Integer.toString(idProjeto), 2).getForum();
            
            ComentarioDAO comd = DAOFactory.createComentarioDAO();
            TopicoDAO topd = DAOFactory.createTopicoDAO();
            for(int i=0;i<forum.getTopicos().size();i++){
                for(int j=0;j<forum.getTopicos().get(i).getListaComentarios().size();j++){
                    comd.deletarComentarioTopico(forum.getTopicos().get(i).getListaComentarios().get(j).getId());
                }
                topd.deletarTopico(forum.getTopicos().get(i).getId());
            }
            
            String SQL = "DELETE FROM forum WHERE idProjeto= ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idProjeto);
            ps.executeUpdate();
            
            ps.close();
            
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCForumDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover forum", ex);
        }
    }
    
}