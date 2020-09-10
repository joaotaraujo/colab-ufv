package br.com.senacrs.dao;

import br.com.senacrs.bean.ComentarioBean;
import br.com.senacrs.connections.ConnectionFactory;
import br.com.senacrs.connections.DAOFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCComentarioDAO implements ComentarioDAO{

    Connection connection;
     SimpleDateFormat formato;
        
    public JDBCComentarioDAO() {
        connection = null;
        formato = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    @Override
    public void comentarTopico(ComentarioBean comentario, int idTopico) {
        
        try {
            
            String SQL = "SELECT id FROM comentario ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idComentario = 0;
            while(rs.next()){
                idComentario = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO comentario (id, idAutor, conteudo, dataComentario, idTopico) VALUES"
                    + "(?,?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, idComentario);
            ps.setInt(2,comentario.getAutor().getId());
            ps.setString(3,comentario.getConteudo());
            ps.setString(4, formato.format(comentario.getDataComentario()));
            ps.setInt(5,idTopico);
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void deletarComentarioTopico(int idComentario) {
        try {
            String SQL = "DELETE FROM comentario WHERE id= ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idComentario);
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover um comentario", ex);
        }
        
    }

    @Override
    public ArrayList<ComentarioBean> listarComentarios(int idTopico) {
        ArrayList<ComentarioBean> comentarios = new ArrayList<ComentarioBean>();
        try {
            
            String SQL = "SELECT * FROM comentario WHERE idTopico = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idTopico);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<Integer> comentariosId = new ArrayList<Integer>();
            while(rs.next()){
                comentariosId.add(rs.getInt("id"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0; i<comentariosId.size();i++){
                ComentarioBean comentario = this.buscar(comentariosId.get(i));
                comentarios.add(comentario);
            }
            
            return comentarios;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTopicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar topicos em JDBCComentarioDAO", ex);
        }
    }

    @Override
    public ComentarioBean buscar(int idComentario) {
        try {
            ComentarioBean comentario = new ComentarioBean();
            
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "SELECT * FROM comentario WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idComentario);
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            comentario.setId(rs.getInt("id"));
            int idAutor = rs.getInt("idAutor");
            comentario.setConteudo(rs.getString("conteudo"));
            comentario.setDataComentario(rs.getDate("dataComentario"));
            comentario.setIdTopico(rs.getInt("idTopico"));
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            comentario.setAutor(ud.buscar(Integer.toString(idAutor), 5));
            
            return comentario;
            
        } catch (SQLException ex) {
            //Logger.getLogger(JDBCComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    @Override
    public void editar(ComentarioBean comentario) {
        try { 
            String SQL = "update comentario set idAutor=?, conteudo=?, dataComentario=?, idTopico=? where id=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, comentario.getAutor().getId());
            ps.setString(2, comentario.getConteudo());
            ps.setDate(3, (Date) comentario.getDataComentario());
            ps.setInt(4, comentario.getIdTopico());
            ps.setInt(5, comentario.getId());
            ps.execute();
            
            ps.close();
            
            connection.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(JDBCComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao buscar um registro de comentario pelo id", ex);
        }
    
    }
    
    @Override
    public String retornaComentariosTabela(ArrayList<ComentarioBean> comentarios) {
        String comentariosString = "";
                
            comentariosString = "<table id=\"tabelaComentarios\" class=\"display\" cellspacing=\"0\" width=\"100%\"><thead><tr><th>Tópicos</th>"
                         +"</tr></thead><tbody>";
            for(int i=0;i<comentarios.size();i++){
                comentariosString = comentariosString + "<tr><td> "+comentarios.get(i).getConteudo()+" </a></td>"
                            +  "</tr>";
                    
            }
            comentariosString = comentariosString + "</tbody></table>";
            
            return comentariosString;
    }
}