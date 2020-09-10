
package br.com.senacrs.dao;

import br.com.senacrs.bean.PerguntaFrequenteBean;
import br.com.senacrs.connections.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCPerguntaFrequenteDAO implements PerguntaFrequenteDAO{

    Connection connection;
    public JDBCPerguntaFrequenteDAO() {
        connection = null;
    }
    
    @Override
    public void inserir(PerguntaFrequenteBean perguntaFrequente, int idProjeto) {
        
        try {
            
            String SQL = "SELECT id FROM perguntafrequente ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idPerguntaFrequente = 0;
            while(rs.next()){
                idPerguntaFrequente = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO perguntafrequente (id, pergunta, resposta, FK_idProjeto) VALUES"
                    + "(?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, idPerguntaFrequente);
            ps.setString(2,perguntaFrequente.getPergunta());
            ps.setString(3,perguntaFrequente.getResposta());
            ps.setInt(4,idProjeto);
            
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPerguntaFrequenteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void deletar(String pergunta) {
        try {
            String SQL = "DELETE FROM perguntafrequente WHERE pergunta= ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, pergunta);
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPerguntaFrequenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover uma pergunta frequente", ex);
        }
        
    }

    @Override
    public ArrayList<PerguntaFrequenteBean> listarPerguntasFrequentes(String nomeProjeto) {
        ArrayList<PerguntaFrequenteBean> perguntasFrequentes = new ArrayList<PerguntaFrequenteBean>();
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
            
            SQL = "SELECT * FROM perguntafrequente WHERE FK_idProjeto = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, idProjeto);
            rs = ps.executeQuery();
            
            ArrayList<Integer> perguntasFrequentesId = new ArrayList<Integer>();
            while(rs.next()){
                perguntasFrequentesId.add(rs.getInt("id"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0; i<perguntasFrequentesId.size();i++){
                PerguntaFrequenteBean perguntaFrequente = this.buscar(Integer.toString(perguntasFrequentesId.get(i)), 2);
                perguntasFrequentes.add(perguntaFrequente);
            }
            
            return perguntasFrequentes;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPerguntaFrequenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar perguntas frequentes em JDBCPerguntaFrequenteDAO", ex);
        }
    }

    @Override
    /*Se tipo:
        - 1: pesquisa por pergunta
        - 2: pesquisa por id
    */
    public PerguntaFrequenteBean buscar(String informacao, int tipo) {
        try {
            PerguntaFrequenteBean perguntaFrequente = new PerguntaFrequenteBean();
            
            String SQL = "";
            PreparedStatement ps = null; 
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            if(tipo == 1){
                SQL = "SELECT * FROM perguntafrequente WHERE pergunta = ?";
                ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);
            } else if(tipo == 2){
                SQL = "SELECT * FROM perguntafrequente WHERE id = ?";
                ps = connection.prepareStatement(SQL);
                ps.setInt(1, Integer.parseInt(informacao));
            }
            
            
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            perguntaFrequente.setId(rs.getInt("id"));
            perguntaFrequente.setPergunta(rs.getString("pergunta"));
            perguntaFrequente.setResposta(rs.getString("resposta"));
            perguntaFrequente.setIdProjeto(rs.getInt("FK_idProjeto"));
            
            ps.close();
            rs.close();
            connection.close();
            
            return perguntaFrequente;
            
        } catch (SQLException ex) {
            //Logger.getLogger(JDBCPerguntaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    @Override
    public void editar(PerguntaFrequenteBean perguntaFrequente) {
        try { 
            String SQL = "update perguntafrequente set pergunta=?, resposta=?, FK_idProjeto=? where id=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            
            ps.setString(1, perguntaFrequente.getPergunta());
            ps.setString(2, perguntaFrequente.getResposta());
            ps.setInt(3, perguntaFrequente.getIdProjeto());
            ps.setInt(4, perguntaFrequente.getId());
            ps.execute();
            
            ps.close();
            connection.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPerguntaFrequenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao buscar um registro de pergunta frequente pelo id", ex);
        }
    
    }
    
    @Override
    public String retornaPerguntasFrequentesTabela(ArrayList<PerguntaFrequenteBean> perguntasFrequentes) {
        String perguntasFrequentesString = "";
                
            perguntasFrequentesString = "<table id=\"example\" class=\"display\" cellspacing=\"0\" width=\"100%\"><thead><tr><th>Perguntas</th><th>Edit</th><th>Del</th>"
                         +"</tr></thead><tbody>";
            for(int i=0;i<perguntasFrequentes.size();i++){
                perguntasFrequentesString = perguntasFrequentesString + "<tr><td><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesPerguntaFrequente?perguntaFrequenteParaCarregar="+perguntasFrequentes.get(i).getPergunta()+"\"> "+perguntasFrequentes.get(i).getPergunta()+" </a></td>"
                        +"<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaPerguntaFrequente?idPerguntaFrequenteEscolhida="+perguntasFrequentes.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"
                            +  "<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaPerguntaFrequente?perguntaFrequenteEscolhida="+perguntasFrequentes.get(i).getPergunta()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                    
            }
            perguntasFrequentesString = perguntasFrequentesString + "</tbody></table>";
            
            return perguntasFrequentesString;
    }
    
    @Override
    public String retornarInformacaoPeguntaFrequenteTabela(PerguntaFrequenteBean perguntaFrequente) {
      
        String perguntaFrequenteString = "";
                
            perguntaFrequenteString = "<table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações da pergunta frequente</center></h3></th>"
                         +"</tr></thead><tbody><tr><td WIDTH=\"15%\"><strong>Pergunta:</strong></td><td WIDTH=\"85%\"> "+perguntaFrequente.getPergunta()+" </td><tr height=85><td COLSPAN=\"1\"><strong>Resposta:</strong></td><td COLSPAN=\"3\"><textarea name=\"\" id=\"\" cols=\"60\" rows=\"4\"> "+perguntaFrequente.getResposta()+"</textarea></td>"
                                +"</tr></tbody></table>";
           
            
            return perguntaFrequenteString;
    
    }
}