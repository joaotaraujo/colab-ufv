package br.com.senacrs.dao;

import br.com.senacrs.bean.MensagemBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.ConnectionFactory;
import br.com.senacrs.connections.DAOFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCMensagemDAO implements MensagemDAO{
    
    Connection connection;
    SimpleDateFormat formato;
    
    public JDBCMensagemDAO() {
        formato = new SimpleDateFormat("yyyy-MM-dd");
        connection = null;
    }
    
    @Override
    public void armazenarMensagem(int idChat, MensagemBean mensagem) {
        
        try {
            
            String SQL = "SELECT id FROM mensagem ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idMensagem = 0;
            while(rs.next()){
                idMensagem = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO mensagem (dataEnvio, horaEnvio, idRemetente, escopo, id, idChat) VALUES"
                    + "(?,?,?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setString(1, formato.format(mensagem.getDataEnvio()));
            
            ps.setString(2, mensagem.getHoraEnvio());
            ps.setInt(3,mensagem.getRemetente().getId());
            ps.setString(4, mensagem.getDescricao());
            ps.setInt(5,idMensagem);
            ps.setInt(6,mensagem.getIdChat());
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCMensagemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public ArrayList<MensagemBean> listarMensagensUsuario(int idChat, int idUsuario) {
        ArrayList<MensagemBean> mensagens = new ArrayList<MensagemBean>();
        try {
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            UsuarioBean remetente = ud.buscar(Integer.toString(idUsuario), 5);
            
            String SQL = "SELECT * FROM mensagem WHERE idChat=? AND idRemetente=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idChat);
            ps.setInt(2, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                MensagemBean mensagem = new MensagemBean();
                mensagem.setDataEnvio(rs.getDate("dataEnvio"));
                mensagem.setDescricao(rs.getString("escopo"));
                mensagem.setHoraEnvio(rs.getString("horaEnvio"));
                mensagem.setId(rs.getInt("id"));
                mensagem.setIdChat(rs.getInt("idChat"));
                mensagem.setRemetente(remetente);
                mensagens.add(mensagem);
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            
            return mensagens;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCMensagemDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar mensagens em JDBCMensagemDAO", ex);
        }
    }
    
    @Override
    public ArrayList<MensagemBean> listarMensagensChat(int idChat) {
        ArrayList<MensagemBean> mensagens = new ArrayList<MensagemBean>();
        try {
            
            String SQL = "SELECT * FROM mensagem WHERE idChat=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idChat);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<Integer> idsRemetentes = new ArrayList<Integer>();
            while(rs.next()){
                MensagemBean mensagem = new MensagemBean();
                mensagem.setDataEnvio(rs.getDate("dataEnvio"));
                mensagem.setDescricao(rs.getString("escopo"));
                mensagem.setHoraEnvio(rs.getString("horaEnvio"));
                mensagem.setId(rs.getInt("id"));
                mensagem.setIdChat(rs.getInt("idChat"));
                idsRemetentes.add(rs.getInt("idRemetente"));
                mensagens.add(mensagem);
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            for(int i=0; i<mensagens.size();i++)
                mensagens.get(i).setRemetente(ud.buscar(Integer.toString(idsRemetentes.get(i)), 5));
                
            return mensagens;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCMensagemDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar mensagens em JDBCMensagemDAO", ex);
        }
    }
    
    @Override
    public void deletarMensagens(int idChat) {
        try {
            String SQL = "DELETE FROM mensagem WHERE idChat= ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idChat);
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCMensagemDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover uma mensagem", ex);
        }
        
    }
}
