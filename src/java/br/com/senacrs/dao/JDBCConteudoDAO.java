package br.com.senacrs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import br.com.senacrs.connections.ConnectionFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JDBCConteudoDAO implements ConteudoDAO{
    
    Connection connection;
    public JDBCConteudoDAO() {
        connection = null;
    }
    
    @Override
    public boolean armazenaArquivo( File f ){
        try {
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "SELECT id FROM arquivo ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idArquivo = 0;
            while(rs.next()){
                idArquivo = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            
            ps = connection.prepareStatement("INSERT INTO arquivo( id, nome, conteudo ) VALUES ( ?, ?, ? )");

            //converte o objeto file em array de bytes
            InputStream is = new FileInputStream( f );
            byte[] bytes = new byte[(int)f.length() ];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                   && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }

            ps.setInt( 1, idArquivo );
            ps.setString( 2, f.getName() );
            ps.setString( 3, bytes.toString() );
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
    public File recuperaArquivo( int id ){
       
        File f = null;
        try {
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT id, nome, conteudo FROM arquivo WHERE id = ?");
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if ( rs.next() ){
                byte [] bytes = rs.getBytes("conteudo");
                String nome = rs.getString("nome");

                //converte o array de bytes em file
                f = new File( "/root/Desktop/Certificados/" + nome );
                FileOutputStream fos = new FileOutputStream( f);
                fos.write( bytes );
                fos.close();
            }
            rs.close();
            ps.close();
            connection.close();
            return f;
    } catch (SQLException ex) {
    ex.printStackTrace();
    }
    catch (IOException ex) {
    ex.printStackTrace();
    }
    return null;
    }
    /*
    @Override
    public boolean add(ConteudoBean filebean) {
        int added = 0;
        try {
            con = ConnectionFactory.getConnection();
            if (con != null) {
                
                String SQL = "SELECT id FROM conteudo ORDER BY id DESC LIMIT 1";
                PreparedStatement ps = con.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                int idConteudo = 0;
                while(rs.next()){
                    idConteudo = rs.getInt("id")+1;
                }

                ps.close();
                rs.close();
                
                
                String sql = "INSERT INTO conteudo (id, name, descricao, dataPost, idRemetente, conteudoArquivo, idProjeto) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement st = con.prepareStatement(sql);
                st.setInt(1, idConteudo);
                st.setString(2, filebean.getNome());
                st.setString(3, filebean.getDescricao());
                st.setDate(4, (java.sql.Date) filebean.getDataPost());
                st.setInt(5, filebean.getRemetente().getId());
                st.setBytes(6, filebean.getConteudoArquivo());
                st.setInt(7, filebean.getIdProjeto());
                
                
                added = st.executeUpdate();
                st.close();
                con.close();
            }
        } catch (SQLException e) {
            //tratar erro
        }

        return added > 0;
    }

    @Override
    public ArrayList<ConteudoBean> list() {
        ArrayList<ConteudoBean> files = new ArrayList<ConteudoBean>();
        try {
            con = ConnectionFactory.getConnection();
            if (con != null) {
                String sql = "SELECT * FROM conteudo";
                PreparedStatement st = con.prepareStatement(sql);
                ResultSet rs = st.executeQuery(sql);

                while(rs.next()){
                    ConteudoBean filebean = this.createFileBean(rs);
                    files.add(filebean);
                }

                st.close();
                con.close();
            }
        } catch (SQLException e) {
            //tratar erro
        }

        return files;
    }
    
    @Override
    public ConteudoBean getFile(int id) {
        ConteudoBean filebean = null;
        try {
            Connection con = ConnectionFactory.getConnection();
            if (con != null) {
                String sql = "SELECT * FROM conteudo WHERE id=?";
                PreparedStatement st = con.prepareStatement(sql);
                st.setLong(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    filebean = createFileBean(rs);
                }
                rs.close();
                st.close();
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filebean;
    }

    @Override
    public ConteudoBean createFileBean(ResultSet rs) throws SQLException {
        
        ConteudoBean filebean = new ConteudoBean();
        
        filebean.setId(rs.getInt("id"));
        filebean.setNome(rs.getString("nome"));
        filebean.setDescricao(rs.getString("descricao"));
        filebean.setDataPost(rs.getDate("dataPost"));
        
        UsuarioDAO ud = DAOFactory.createUsuarioDAO();
        filebean.setRemetente(ud.buscar(Integer.toString(rs.getInt("idRemetente")),5));
        
        filebean.setConteudoArquivo(rs.getBytes("conteudoArquivo"));
        filebean.setIdProjeto(rs.getInt("idProjeto"));

        return filebean;
    }*/
}
