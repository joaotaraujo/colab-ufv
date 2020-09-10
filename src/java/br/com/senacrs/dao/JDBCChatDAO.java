package br.com.senacrs.dao;

import br.com.senacrs.bean.ChatBean;
import br.com.senacrs.connections.ConnectionFactory;
import br.com.senacrs.connections.DAOFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCChatDAO implements ChatDAO{
    Connection connection;
    public JDBCChatDAO() {
        connection = null;
    }
    
    @Override
    public void cadastrarChat(int idProjeto) {
        
        try {
            
            String SQL = "SELECT id FROM chat ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idChat = 0;
            while(rs.next()){
                idChat = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO chat (id, idProjeto) VALUES"
                    + "(?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, idChat);
            ps.setInt(2,idProjeto);
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCChatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
        public ChatBean buscarChat(int idProjeto) {
        try {
            ChatBean chat = new ChatBean();
            
            
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "SELECT * FROM chat WHERE idProjeto = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idProjeto);
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            chat.setId(rs.getInt("id"));
            chat.setIdProjeto(rs.getInt("idProjeto"));
            
            ps.close();
            rs.close();
            connection.close();
            
            MensagemDAO mend = DAOFactory.createMensagemDAO();
            chat.setMensagensEnviadas(mend.listarMensagensChat(chat.getId()));
            
            return chat;
            
        } catch (SQLException ex) {
            //Logger.getLogger(JDBCChatDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Override
    public void deletarChat(int idProjeto){
        try {
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ChatBean chat = projd.pesquisarProjeto(Integer.toString(idProjeto), 2).getChat();
            
            MensagemDAO mend = DAOFactory.createMensagemDAO();
            mend.deletarMensagens(chat.getId());
            
            String SQL = "DELETE FROM chat WHERE idProjeto= ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idProjeto);
            ps.executeUpdate();
            
            ps.close();
            
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCChatDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover chat", ex);
        }
    }
}
