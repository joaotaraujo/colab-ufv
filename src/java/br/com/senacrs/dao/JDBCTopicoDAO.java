package br.com.senacrs.dao;

import br.com.senacrs.bean.TopicoBean;
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

public class JDBCTopicoDAO implements TopicoDAO{

    Connection connection;
     SimpleDateFormat formato;
        
    public JDBCTopicoDAO() {
        connection = null;
        formato = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    @Override
    public void cadastrarTopico(TopicoBean topico) {
        
        try {
            
            String SQL = "SELECT id FROM topico ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idTopico = 0;
            while(rs.next()){
                idTopico = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO topico (id, titulo, descricao, dataCriacao, idAutor, idForum) VALUES"
                    + "(?,?,?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, idTopico);
            ps.setString(2,topico.getTitulo());
            ps.setString(3,topico.getDescricao());
            ps.setString(4, formato.format(topico.getDataCriacao()));
            ps.setInt(5,topico.getAutor().getId());
            ps.setInt(6,topico.getIdForum());
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTopicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void deletarTopico(int idTopico) {
        try {
            String SQL = "DELETE FROM comentario WHERE idTopico= ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idTopico);
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM topico WHERE id= ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, idTopico);
            ps.executeUpdate();
            
            ps.close();
            
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTopicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover um topico", ex);
        }
        
    }

    @Override
    public ArrayList<TopicoBean> listarTopicos(int idForum) {
        ArrayList<TopicoBean> topicos = new ArrayList<TopicoBean>();
        try {
            
            String SQL = "SELECT * FROM topico WHERE idForum = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idForum);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<Integer> topicosId = new ArrayList<Integer>();
            while(rs.next()){
                topicosId.add(rs.getInt("id"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0; i<topicosId.size();i++){
                TopicoBean topico = this.buscar(Integer.toString(topicosId.get(i)),2);
                topicos.add(topico);
            }
            
            return topicos;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTopicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar topicos em JDBCTopicoDAO", ex);
        }
    }

    @Override
    /*Se tipo:
        - 1: pesquisa por titulo
        - 2: pesquisa por id
    */
    public TopicoBean buscar(String informacao, int tipo) {
        try {
            TopicoBean topico = new TopicoBean();
            
            String SQL = "";
            PreparedStatement ps = null; 
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            if(tipo == 1){
                SQL = "SELECT * FROM topico WHERE titulo = ?";
                ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);
            } else if(tipo == 2){
                SQL = "SELECT * FROM topico WHERE id = ?";
                ps = connection.prepareStatement(SQL);
                ps.setInt(1, Integer.parseInt(informacao));
            }
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            topico.setId(rs.getInt("id"));
            topico.setTitulo(rs.getString("titulo"));
            topico.setDescricao(rs.getString("descricao"));
            topico.setDataCriacao(rs.getDate("dataCriacao"));
            int idAutor = rs.getInt("idAutor");
            topico.setIdForum(rs.getInt("idForum"));
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            topico.setAutor(ud.buscar(Integer.toString(idAutor), 5));
            
            ComentarioDAO cod = DAOFactory.createComentarioDAO();
            topico.setListaComentarios(cod.listarComentarios(topico.getId()));
            
            return topico;
            
        } catch (SQLException ex) {
            //Logger.getLogger(JDBCTopicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    @Override
    public void editar(TopicoBean topico) {
        try { 
            String SQL = "update topico set titulo=?, descricao=?, dataCriacao=?, idAutor=?, idForum=? where id=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            
            ps.setString(1, topico.getTitulo());
            ps.setString(2, topico.getDescricao());
            ps.setDate(3, (Date) topico.getDataCriacao());
            ps.setInt(4, topico.getAutor().getId());
            ps.setInt(5, topico.getIdForum());
            
            ps.setInt(6, topico.getId());
            ps.execute();
            
            ps.close();
            
            connection.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTopicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao buscar um registro de topico pelo id", ex);
        }
    
    }
    
    @Override
    public String retornaTopicosTabela(ArrayList<TopicoBean> topicos, String nomeUsuarioLogado) {
        String topicosString = "";
             
        for (int i = topicos.size(); i >= 1; i--) {
            for (int j = 1; j < i; j++) {
                if (topicos.get(j - 1).getDataCriacao().compareTo(topicos.get(j).getDataCriacao())<0) {
                    TopicoBean aux = topicos.get(j);
                    topicos.set(j, topicos.get(j - 1));
                    topicos.set(j - 1,aux);
                }
            }
        }
        
        formato = new SimpleDateFormat("dd-MM-yyyy");
        topicosString = "<table class=\"display\" cellspacing=\"0\" width=\"100%\"><thead></thead><tbody>";
        for(int i=0;i<topicos.size();i++){
            topicosString = topicosString + "<tr><td style=\"width:50%;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTopico?topicoParaCarregar="+topicos.get(i).getTitulo()+"\"> "+topicos.get(i).getTitulo()+" </a></td>"
                    +"<td style=\"width:20%;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTopico?topicoParaCarregar="+topicos.get(i).getTitulo()+"\"> "+topicos.get(i).getAutor().getNome()+" </a></td>"
                    + "<td style=\"width:20%;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTopico?topicoParaCarregar="+topicos.get(i).getTitulo()+"\"> "+formato.format(topicos.get(i).getDataCriacao())+" </a></td>";
                        
            if(nomeUsuarioLogado.equalsIgnoreCase(topicos.get(i).getAutor().getNome())){
                topicosString = topicosString + "<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaTopico?idTopicoEscolhido="+topicos.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"
                                +"<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaTopico?topicoEscolhido="+topicos.get(i).getTitulo()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";;
            }
            else{
                topicosString = topicosString + "<td></td><td></td></tr>";
            }    
                

        }
        topicosString = topicosString + "</tbody></table>";


        formato = new SimpleDateFormat("yyyy-MM-dd");
        return topicosString;
    }
    
    @Override
    public String retornarInformacaoTopicoTabela(TopicoBean topico, String nomeUsuarioLogado) {
        formato = new SimpleDateFormat("dd-MM-yyyy");
        String topicoString = "<table border=\"1\"><thead><tr><th COLSPAN=\"4\"><h3><center>"+topico.getTitulo()+"</center></h3></th>"
                         +"</tr></thead><tbody></tbody></thead></table><div class=\"container\"><table border=\"1\"><thead><tr><th COLSPAN=\"1\"  style=\"background: #DAD9FF;\">Autor</th><th COLSPAN=\"1\"  style=\"background: #DAD9FF;\">Comentário</th>"
                         +"</tr></thead><tbody><tr><td style=\"font-size:16px;width:30%;\"><strong>"+topico.getAutor().getNome()+"<p>"+topico.getAutor().getTipoUsuario()+"<p>"+formato.format(topico.getDataCriacao())+"</strong></td><td style=\"padding-top:0px;padding-left:0px;padding-right:0px;padding-bottom:0px;width:70%;\"><strong><textarea name=\"\" id=\"\" cols=\"60\" rows=\"6\">"+topico.getDescricao()+"</textarea></strong></td></tr>";

        for(int i=0; i<topico.getListaComentarios().size();i++){
            topicoString = topicoString + "<tr><td style=\"font-size:16px;width:30%;\">"+topico.getListaComentarios().get(i).getAutor().getNome()+"<p>"+topico.getListaComentarios().get(i).getAutor().getTipoUsuario()+"<p>"+formato.format(topico.getListaComentarios().get(i).getDataComentario())+"<p>";
                
        
            //se o usuarioLogado for o autor, ele poderá editar e excluir o comentário
            if(topico.getListaComentarios().get(i).getAutor().getNome().equalsIgnoreCase(nomeUsuarioLogado))
                topicoString = topicoString + "<a style=\"color:#FF0000;\" href=\"/ProjetoColabUFV/ServletDeletaComentario?comentarioParaDeletar="+topico.getListaComentarios().get(i).getId()+"\">Excluir</a></form>";
            
            topicoString=topicoString  +"</td><td style=\"padding-top:0px;padding-left:0px;padding-right:0px;padding-bottom:0px;width:70%;\"><textarea name=\"\" id=\"\" cols=\"60\" rows=\"6\"> "+topico.getListaComentarios().get(i).getConteudo()+"</textarea></td></tr>";
        }
        topicoString=topicoString+"</tbody></table></div>";
        formato = new SimpleDateFormat("yyyy-MM-yyyy");  
            
        return topicoString;
    
    }
}