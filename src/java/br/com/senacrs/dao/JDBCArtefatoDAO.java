package br.com.senacrs.dao;

import br.com.senacrs.bean.ArtefatoBean;
import br.com.senacrs.connections.ConnectionFactory;
import br.com.senacrs.connections.DAOFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCArtefatoDAO implements ArtefatoDAO{
    
    Connection connection;
    SimpleDateFormat formato;
    
    public JDBCArtefatoDAO() {
        connection = null;
        formato = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    @Override
    public boolean armazenaArtefato( ArtefatoBean artefato ){
        try {
            
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            
            String SQL = "SELECT id FROM arquivo ORDER BY id DESC LIMIT 1";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idArquivo = 0;
            while(rs.next()){
                idArquivo = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            
            ps = connection.prepareStatement("INSERT INTO artefato( id, idRemetente, nome, descricao, dataPost, conteudo, idAtividade ) VALUES ( ?, ?, ?, ?, ?, ?, ?)");

            //converte o objeto file em array de bytes
            InputStream is = new FileInputStream( artefato.getArquivo() );
            byte[] bytes = new byte[(int)artefato.getArquivo().length() ];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                   && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }

            ps.setInt( 1, idArquivo );
            ps.setInt( 2, artefato.getRemetente().getId() );
            ps.setString( 3, artefato.getArquivo().getName() );
            ps.setString( 4, artefato.getDescricao() );
            ps.setString( 5,  formato.format(artefato.getDataPost()) );
            ps.setString( 6, bytes.toString()  );
            ps.setInt( 7, artefato.getIdAtividade() );
            
            ps.execute();
            
            ps.close();
            connection.close();
            
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    @Override
    public ArtefatoBean recuperaArtefato( String nomeArquivo, String caminhoArquivo ){
       
        File f = null;
        ArtefatoBean artefato = new ArtefatoBean();
        int idRemetente=0;
        String nome = "";
        try {
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT id, idRemetente, nome, descricao, dataPost, conteudo, idAtividade FROM artefato WHERE nome = ?");
            
            ps.setString(1, nomeArquivo);
            ResultSet rs = ps.executeQuery();
            if ( rs.next() ){
                
                artefato.setId(rs.getInt("id"));
                idRemetente = rs.getInt("idRemetente");
                nome = rs.getString("nome");
                artefato.setDescricao(rs.getString("descricao"));
                artefato.setDataPost(rs.getDate("dataPost"));
                byte [] bytes = rs.getBytes("conteudo");
                artefato.setIdAtividade(rs.getInt("idAtividade"));

                //converte o array de bytes em file ex: "/root/Desktop/"
                f = new File( caminhoArquivo + nome );
                FileOutputStream fos = new FileOutputStream( f);
                fos.write( bytes );
                fos.close();
            }
            
            rs.close();
            ps.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            artefato.setRemetente(ud.buscar(Integer.toString(idRemetente), 5));
            artefato.setNome(nome);
            artefato.setArquivo(f);
            
            return artefato;
            
        } catch (SQLException ex) {
        ex.printStackTrace();
        }
        catch (IOException ex) {
        ex.printStackTrace();
        }
        return null;
    }
    
    @Override
    public void deletarArtefato(int idArtefato) {
        try {
            String SQL = "DELETE FROM artefato WHERE id= ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idArtefato);
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover um artefato", ex);
        }
        
    }

    @Override
    public ArrayList<String> listarArtefatos(int idArtefato) {
        try {
            
            String SQL = "SELECT * FROM artefato WHERE id = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idArtefato);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<String> artefatosNome = new ArrayList<String>();
            while(rs.next()){
                artefatosNome.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            
            return artefatosNome;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTopicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar topicos em JDBCComentarioDAO", ex);
        }
    }
    
    @Override
    public ArrayList<String> listarArtefatosAluno(int idAluno) {
        try {
            
            String SQL = "SELECT * FROM artefato WHERE idRemetente = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idAluno);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<String> artefatosNome = new ArrayList<String>();
            while(rs.next()){
                artefatosNome.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            
            return artefatosNome;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTopicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar topicos em JDBCComentarioDAO", ex);
        }
    }
}
