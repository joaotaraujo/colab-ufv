package br.com.senacrs.dao;

import br.com.senacrs.bean.EmailBean;
import br.com.senacrs.connections.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JDBCEmailDAO implements EmailDAO{

    Connection connection;
        
    public JDBCEmailDAO() {
        connection = null;
    }
    
    
    @Override
    public void armazenarEmail(EmailBean email){
        
        try {
            
            String SQL = "SELECT id FROM email ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idEmail = 0;
            while(rs.next()){
                idEmail = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO email (id, idRemetente, assunto, conteudo, dataEnvio, emailDestinatario, idProjeto) VALUES"
                    + "(?,?,?,?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, idEmail);
            ps.setInt(2,email.getRemetente().getId());
            ps.setString(3,email.getAssunto());
            ps.setString(4, email.getConteudo());
            
            Date data = new Date(System.currentTimeMillis());  
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            ps.setString(5, formato.format(data));
            
            ps.setString(6,email.getDestinatario());
            ps.setInt(7,email.getIdProjeto());
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCEmailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public int numEmailsEnviadosAluno(int idProjeto, int idUsuario) {
        try{    
            String SQL = "SELECT id FROM email WHERE idProjeto=? AND idRemetente=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idProjeto);
            ps.setInt(2, idUsuario);
            ResultSet rs = ps.executeQuery();
            int numEmailsEnviados = 0;
            while(rs.next()){
                numEmailsEnviados++;
            }
            
            ps.close();
            rs.close();
            connection.close();
        
            return numEmailsEnviados;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCEmailDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    
    @Override
    public int numEmailsEnviadosProjeto(int idProjeto) {
        try{    
            String SQL = "SELECT id FROM email WHERE idProjeto=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idProjeto);
            ResultSet rs = ps.executeQuery();
            int numEmailsEnviados = 0;
            while(rs.next()){
                numEmailsEnviados++;
            }
            
            ps.close();
            rs.close();
            connection.close();
        
            return numEmailsEnviados;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCEmailDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    
}
