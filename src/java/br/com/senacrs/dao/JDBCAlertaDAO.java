package br.com.senacrs.dao;

import br.com.senacrs.bean.AlertaBean;
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

public class JDBCAlertaDAO implements AlertaDAO{

    Connection connection;
    public JDBCAlertaDAO() {
        connection = null;
    }
    
    @Override
    public void enviarAlerta(AlertaBean alerta) {
        
        try {
            
            String SQL = "SELECT id FROM alerta ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idAlerta = 0;
            while(rs.next()){
                idAlerta = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO alerta (id, dataEmissao, descricao, idAutor, foiVisto, idProjeto) VALUES"
                    + "(?,?,?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, idAlerta);
            
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            ps.setString(2, formato.format(alerta.getDataEmissao()));
            
            ps.setString(3,alerta.getDescricao());
            ps.setInt(4,alerta.getAutor().getId());
            ps.setString(5,"0");
            ps.setInt(6,alerta.getIdProjeto());
            
            ps.executeUpdate();
            
            for(int i=0; i<alerta.getDestinatarios().size();i++){
                SQL = "SELECT id FROM usuarioxalerta ORDER BY id DESC LIMIT 1";
                ps = connection.prepareStatement(SQL);
                rs = ps.executeQuery();
                int idUsuarioxAlerta = 0;
                while(rs.next()){
                    idUsuarioxAlerta = rs.getInt("id")+1;
                }

                ps.close();
                rs.close();

                SQL = "INSERT INTO usuarioxalerta (id, idUsuario, idAlerta) VALUES"
                        + "(?,?,?)";
                ps = connection.prepareStatement(SQL);

                ps.setInt(1, idUsuarioxAlerta);
                ps.setInt(2, alerta.getDestinatarios().get(i).getId());
                ps.setInt(3,idAlerta);

                ps.executeUpdate();
            
            }
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAlertaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

     @Override
    public AlertaBean buscar(int idAlerta) {
        try {
            AlertaBean alerta = new AlertaBean();
            
            String SQL = "SELECT * FROM alerta WHERE id = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL); 
            
            ps.setInt(1, idAlerta);
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            
            alerta.setId(rs.getInt("id"));
            alerta.setDescricao(rs.getString("descricao"));
            alerta.setDataEmissao(rs.getDate("dataEmissao"));
            
            int foiVisto = rs.getInt("foiVisto");
            if(foiVisto == 0)
                alerta.setFoiVisto(false);
            else
                alerta.setFoiVisto(true);
            
            alerta.setIdProjeto(rs.getInt("idProjeto"));
            
            int idAutor = rs.getInt("idAutor");
                
            ps.close();
            rs.close();
            
            
            SQL = "SELECT * FROM usuarioxalerta WHERE idAlerta = ?";
            ps = connection.prepareStatement(SQL); 
            ps.setInt(1, idAlerta);
            
            rs = ps.executeQuery();
            ArrayList<UsuarioBean> envolvidos = new ArrayList<UsuarioBean>();
            ArrayList<Integer> envolvidosIds = new ArrayList<Integer>();
            while(rs.next()){
                envolvidosIds.add(rs.getInt("idUsuario"));
            }    
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            alerta.setAutor(ud.buscar(Integer.toString(idAutor),5));
            
            for(int i=0; i<envolvidosIds.size();i++){
                UsuarioBean usuario = ud.buscar(Integer.toString(envolvidosIds.get(i)),5);
                envolvidos.add(usuario);
            }
            
            alerta.setDestinatarios(envolvidos);
            
            return alerta;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAlertaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    @Override
    public void editar(AlertaBean alerta) {
        try { 
            String SQL = "update alerta set foiVisto=? where id=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            
            if(alerta.isFoiVisto())
                ps.setString(1, "1");
            ps.setInt(2, alerta.getId());
            ps.execute();
            
            ps.close();
            connection.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAlertaDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao buscar um registro de alerta pelo id", ex);
        }
    
    }
    
    @Override
    public ArrayList<AlertaBean> listarAlertasRecebidos(int idProjeto, int idUsuario) {
        ArrayList<AlertaBean> alertasProjeto = this.listarAlertasProjeto(idProjeto);
        try {
            
            ArrayList<AlertaBean> alertasRecebidosUsuario = new ArrayList<AlertaBean>();
            
            String SQL = "SELECT * FROM usuarioxalerta ua RIGHT JOIN alerta a ON ua.idAlerta = a.id WHERE idUsuario = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            ArrayList<Integer> alertasIds = new ArrayList<Integer>();
            while(rs.next()){
                alertasIds.add(rs.getInt("idAlerta"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0; i<alertasIds.size();i++){
                AlertaBean alerta = this.buscar(alertasIds.get(i));
                alertasRecebidosUsuario.add(alerta);
            }
            
            ArrayList<AlertaBean> alertasRecebidosUsuarioEmProjeto = new ArrayList<AlertaBean>();
            
            for(int i=0;i<alertasProjeto.size();i++){
                for(int j=0;j<alertasRecebidosUsuario.size();j++){
                    if(alertasProjeto.get(i).getId() == alertasRecebidosUsuario.get(j).getId()){
                        alertasRecebidosUsuarioEmProjeto.add(alertasProjeto.get(i));
                    }
                }
            }
            
            return alertasRecebidosUsuarioEmProjeto;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPerguntaFrequenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar perguntas frequentes em JDBCPerguntaFrequenteDAO", ex);
        }
    }
    
    @Override
    public ArrayList<AlertaBean> listarAlertasEnviados(int idProjeto, int idUsuario) {
        ArrayList<AlertaBean> alertasUsuario = new ArrayList<AlertaBean>();
        try {
            
            String SQL = "SELECT * FROM alerta WHERE idAutor = ? AND idProjeto = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idProjeto);
            ResultSet rs = ps.executeQuery();
            ArrayList<Integer> alertasIds = new ArrayList<Integer>();
            while(rs.next()){
                alertasIds.add(rs.getInt("id"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0; i<alertasIds.size();i++){
                AlertaBean alerta = this.buscar(alertasIds.get(i));
                alertasUsuario.add(alerta);
            }
            
            return alertasUsuario;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAlertaDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar alertas em JDBCAlertaDAO", ex);
        }
    }
    
    
    @Override
    public ArrayList<AlertaBean> listarAlertasProjeto(int idProjeto) {
        ArrayList<AlertaBean> alertasProjeto = new ArrayList<AlertaBean>();
        try {
            
            String SQL = "SELECT * FROM alerta WHERE idProjeto = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idProjeto);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<Integer> alertasIds = new ArrayList<Integer>();
            while(rs.next()){
                alertasIds.add(rs.getInt("id"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0; i<alertasIds.size();i++){
                AlertaBean alerta = this.buscar(alertasIds.get(i));
                alertasProjeto.add(alerta);
            }
            
            return alertasProjeto;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAlertaDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar alertas em JDBCAlertaDAO", ex);
        }
    }
    
    @Override
    public String emitirAlerta(int idUsuario, int idProjeto){
        ArrayList<AlertaBean> alertasUsuario = this.listarAlertasRecebidos(idProjeto, idUsuario);
        
        String alertaString="";
        for(int i=0; i<alertasUsuario.size();i++){
            if(!alertasUsuario.get(i).isFoiVisto()){
                alertaString = alertaString + "<SCRIPT LANGUAGE=\"JavaScript\">"
                        +"window.alert(\"Descricao: "+alertasUsuario.get(i).getDescricao() + " Autor: "+ alertasUsuario.get(i).getAutor().getNome()+" Destinatários: ";
                
                for(int j=0;j<alertasUsuario.get(i).getDestinatarios().size();j++){
                    alertaString = alertaString + " "+ alertasUsuario.get(i).getDestinatarios().get(j).getNome();
                }
                alertaString = alertaString + "\")"
                        +"</SCRIPT>";
                alertasUsuario.get(i).setFoiVisto(true);
                this.editar(alertasUsuario.get(i));
            }
        }
        return alertaString;
    }
    
    @Override
    /*tipo:
        1 - alertas enviados
        2 - alertas recebidos
    */
    public String retornaAlertasTabela(ArrayList<AlertaBean> alertas, int tipo) {
        String alertasString = "";
                
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        
            if(tipo == 1){
                alertasString = "<table id=\"alertasEnviadosTabela\" class=\"display\" cellspacing=\"0\" width=\"100%\"><thead><tr><th>Descrição</th><th>Data de envio</th>"
                         +"</tr></thead><tbody>";
            }
            else{
                alertasString = "<table id=\"alertasRecebidosTabela\" class=\"display\" cellspacing=\"0\" width=\"100%\"><thead><tr><th>Descrição</th><th>Data de envio</th>"
                         +"</tr></thead><tbody>";  
            }
            for(int i=0;i<alertas.size();i++){
                alertasString = alertasString + "<tr><td><a href=\"#\" onClick=\"alert('Descricao: "+alertas.get(i).getDescricao() + " Autor: "+ alertas.get(i).getAutor().getNome()+" Destinatários: ";
                
                for(int j=0;j<alertas.get(i).getDestinatarios().size();j++){
                    alertasString = alertasString + " "+ alertas.get(i).getDestinatarios().get(j).getNome();
                }
                alertasString = alertasString + "')\">"+alertas.get(i).getDescricao() +"</a></td><td>"+formato.format(alertas.get(i).getDataEmissao())+"</td>"
                                +  "</tr>";

            }
            alertasString = alertasString + "</tbody></table>";
            return alertasString;
               
            
    }
    
}