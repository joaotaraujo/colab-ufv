package br.com.senacrs.dao;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.TarefaBean;
import br.com.senacrs.connections.ConnectionFactory;
import br.com.senacrs.connections.DAOFactory;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCTarefaDAO implements TarefaDAO{
    Connection connection;

    SimpleDateFormat formato;
    
    public JDBCTarefaDAO(){
        formato = new SimpleDateFormat("yyyy-MM-dd");
        connection = null;
    }
    
    @Override
    public void cadastrar(TarefaBean tarefa) {
        
        try {
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            UsuarioBean responsavel = ud.buscar(tarefa.getResponsavel().getNome(),1);
            
            String SQL = "SELECT * FROM tarefa ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idTarefa =0;
            while(rs.next()){
                idTarefa = rs.getInt("id") + 1;
            }
            ps.close();
            rs.close();

            SQL = "INSERT INTO tarefa (id, nome, dataInicio, dataTermino, descricao, FK_responsavel, FK_idAtividade, complexidade, prioridade, status, idTarefaAnterior) VALUES"
                    + "(?,?,?,?,?,?,?,?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1,idTarefa);
            ps.setString(2,tarefa.getNome());
            
            ps.setString(3,formato.format(tarefa.getDataInicio()));
            ps.setString(4,formato.format(tarefa.getDataTermino()));
            
            ps.setString(5,tarefa.getDescricao());
            ps.setInt(6,responsavel.getId());
            ps.setInt(7,tarefa.getAtividade());
            ps.setString(8,tarefa.getComplexidade());
            ps.setInt(9,tarefa.getPrioridade());
            ps.setString(10,tarefa.getStatus());
            
            ps.setString(11,"-1");
            
            ps.executeUpdate();
            
            ps.close();
            
            
            SQL = "SELECT id FROM usuarioxtarefa ORDER BY id DESC LIMIT 1";
            ps = connection.prepareStatement(SQL);
            rs = ps.executeQuery();
            int idUsuarioxTarefa =0;
            while(rs.next()){
                idUsuarioxTarefa = rs.getInt("id")+1;
            }
            ps.close();
            rs.close();
            
            
            SQL = "INSERT INTO usuarioxtarefa (id, FK_idUsuario, FK_idTarefa) VALUES"
                    + "(?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1,idUsuarioxTarefa);
            ps.setInt(2,tarefa.getResponsavel().getId());
            ps.setInt(3,tarefa.getId());
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
            AtividadeDAO atvd = DAOFactory.createAtividadeDAO();
            AtividadeBean atividade = atvd.buscar(Integer.toString(tarefa.getAtividade()), 2);
            ArrayList<TarefaBean> tarefasAtividade = atividade.getTarefas();
            ArrayList<TarefaBean> tarefasOrdenadas = this.ordenaTarefas(tarefasAtividade);
                
            for (int i = 0; i<tarefasOrdenadas.size(); i++) {
                this.editar(tarefasOrdenadas.get(i));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void deletar(int idTarefa) {
        try {
            
            TarefaBean tarefa = this.buscar(Integer.toString(idTarefa), 2);
            
            AtividadeDAO atvd = DAOFactory.createAtividadeDAO();
            ArrayList<TarefaBean> tarefasAtividade = atvd.buscar(Integer.toString(tarefa.getAtividade()), 2).getTarefas();
            
            //se não tiver atv anterior, não altera nada
            if(tarefa.getIdTarefaAnterior()==-1){
            }
            else{
                for(int i=0; i<tarefasAtividade.size();i++){
                    if(tarefasAtividade.get(i).getIdTarefaAnterior()== tarefa.getId()){
                        tarefasAtividade.get(i).setIdTarefaAnterior(-1);
                        this.editar(tarefasAtividade.get(i));
                        break;
                    }
                }
            }
            
            
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "DELETE FROM usuarioxtarefa WHERE FK_idTarefa= ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idTarefa);
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "DELETE FROM tarefa WHERE id= ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, idTarefa);
            ps.executeUpdate();
            
            ps.close();
            
            connection.close();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover uma tarefa", ex);
        }
        
    }

    @Override
    public List<TarefaBean> listarTarefas() {
        List<TarefaBean> tarefas = new ArrayList<TarefaBean>();
        try {
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "SELECT * FROM tarefa";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<String> tarefasNome = new ArrayList<String>();
            while(rs.next()){
                tarefasNome.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0; i<tarefasNome.size();i++){
                TarefaBean tarefa = this.buscar(tarefasNome.get(i), 1);
                tarefas.add(tarefa);
            }
            
            
            return tarefas;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar tarefa em JDBCTarefaDAO", ex);
        }
    }

    /*Posso buscar por:
        (1) nome
        (2) id
    */
    @Override
    public TarefaBean buscar(String informacao, int tipo) {
        try {
            
            TarefaBean tarefa = new TarefaBean();
            String SQL = null;
            PreparedStatement ps = null;
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            if(tipo==1){
                SQL = "SELECT * FROM tarefa WHERE nome = ?";
                ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);
            }
            else if(tipo==2){
                SQL = "SELECT * FROM tarefa WHERE id = ?";
                ps = connection.prepareStatement(SQL);
                ps.setInt(1, Integer.parseInt(informacao));
            }
            
            
            
            ResultSet rs = ps.executeQuery();
            
            rs.next();
            tarefa.setNome(rs.getString("nome"));
            tarefa.setDescricao(rs.getString("descricao"));
            
            Date dataInicio = rs.getDate("dataInicio");
            Date dataTermino = rs.getDate("dataTermino");
            tarefa.setDataInicio(dataInicio);
            tarefa.setDataTermino(dataTermino);
            
            tarefa.setId(rs.getInt("id"));
            int idResponsavel = rs.getInt("FK_Responsavel");
            tarefa.setAtividade(rs.getInt("FK_idAtividade"));
            tarefa.setComplexidade(rs.getString("complexidade"));
            tarefa.setPrioridade(rs.getInt("prioridade"));
            tarefa.setStatus(rs.getString("status"));
            tarefa.setIdTarefaAnterior(rs.getInt("idTarefaAnterior"));
           
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            tarefa.setResponsavel(ud.buscar(Integer.toString(idResponsavel),5));
            
            tarefa.setColaboradores(this.retornarEnvolvidosTarefa(tarefa));
            
            return tarefa;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
        
    }

    @Override
    public void editar(TarefaBean tarefa) {
        try { 
            String SQL = "update tarefa set nome=?, dataInicio=?, dataTermino=?, descricao=?, FK_responsavel=?, FK_idAtividade=?, complexidade=?, prioridade=?, status=?, idTarefaAnterior=? where id=?";         
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            
            ps.setString(1, tarefa.getNome());
            
            ps.setString(2, formato.format(tarefa.getDataInicio()));
            ps.setString(3, formato.format(tarefa.getDataTermino()));
            
            ps.setString(4, tarefa.getDescricao());
            ps.setInt(5, tarefa.getResponsavel().getId());
            ps.setInt(6, tarefa.getAtividade());
            ps.setString(7, tarefa.getComplexidade());
            ps.setInt(8, tarefa.getPrioridade());
            ps.setString(9, tarefa.getStatus());
            ps.setInt(10, tarefa.getIdTarefaAnterior());
            ps.setInt(11, tarefa.getId());
            ps.execute();
            
            ps.close();
            connection.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao buscar um registro de tarefa pelo id", ex);
        }
    
    }
    
    
    @Override
    public String retornaTarefasTabela(ArrayList<TarefaBean> tarefas){
        String tarefasString = "";
                
            
            tarefasString = "<table cellspacing=\"0\" width=\"100%\"><thead><tr><th>Nome</th><th>Data Término</th><th>Status</th><th>Edit</th><th>Del</th>"
                         +"</tr></thead><tbody>";
            for(int i=0;i<tarefas.size();i++){
                if(tarefas.get(i).getStatus().endsWith("Nova tarefa")){
                    tarefasString = tarefasString + "<tr><td style=\"width:45%;background-color:#FFFFFF;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+tarefas.get(i).getNome()+"\"> "+tarefas.get(i).getNome()+"</a> </td>"
                           +  "<td style=\"width:20%;background-color:#FFFFFF;\">"+this.trocaData(formato.format(tarefas.get(i).getDataTermino()))+"</td><td style=\"width:25%;background-color:#FFFFFF;\"> "+tarefas.get(i).getStatus()+" </td>"
                            +"<td style=\"background-color:#FFFFFF;width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaTarefa?idTarefaEscolhida="+tarefas.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"    
                            +"<td style=\"background-color:#FFFFFF;width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaTarefa?tarefaEscolhida="+tarefas.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                }else if(tarefas.get(i).getStatus().endsWith("Em andamento")){
                    tarefasString = tarefasString + "<tr><td style=\"width:45%;background-color:#AAA9FF;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+tarefas.get(i).getNome()+"\"> "+tarefas.get(i).getNome()+"</a> </td>"
                            +  "<td style=\"width:20%;background-color:#AAA9FF;\">"+this.trocaData(formato.format(tarefas.get(i).getDataTermino()))+"</td><td style=\"width:25%;background-color:#AAA9FF;\"> "+tarefas.get(i).getStatus()+" </td>"
                            +"<td style=\"background-color:#AAA9FF;width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaTarefa?idTarefaEscolhida="+tarefas.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"    
                            +"<td style=\"background-color:#AAA9FF;width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaTarefa?tarefaEscolhida="+tarefas.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                }else if(tarefas.get(i).getStatus().endsWith("Concluida")){
                    tarefasString = tarefasString + "<tr><td style=\"width:45%;background-color:#B1FFA9;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+tarefas.get(i).getNome()+"\"> "+tarefas.get(i).getNome()+"</a> </td>"
                            +  "<td style=\"width:20%;background-color:#B1FFA9;\">"+this.trocaData(formato.format(tarefas.get(i).getDataTermino()))+"</td><td style=\"width:25%;background-color:#B1FFA9;\"> "+tarefas.get(i).getStatus()+" </td>"
                            +"<td style=\"background-color:#B1FFA9;width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaTarefa?idTarefaEscolhida="+tarefas.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"    
                            +"<td style=\"background-color:#B1FFA9;width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaTarefa?tarefaEscolhida="+tarefas.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                }else if(tarefas.get(i).getStatus().endsWith("Reaberta")){
                    tarefasString = tarefasString + "<tr><td style=\"width:45%;background-color:#FFFC95;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+tarefas.get(i).getNome()+"\"> "+tarefas.get(i).getNome()+"</a> </td>"
                            +  "<td style=\"width:20%;background-color:#FFFC95;\">"+this.trocaData(formato.format(tarefas.get(i).getDataTermino()))+"</td><td style=\"width:25%;background-color:#FFFC95;\"> "+tarefas.get(i).getStatus()+" </td>"
                            +"<td style=\"background-color:#FFFC95;width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaTarefa?idTarefaEscolhida="+tarefas.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"    
                            +"<td style=\"background-color:#FFFC95;width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaTarefa?tarefaEscolhida="+tarefas.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                }else if(tarefas.get(i).getStatus().endsWith("Cancelada")){
                    tarefasString = tarefasString + "<tr><td style=\"width:45%;background-color:#D2D2D2;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+tarefas.get(i).getNome()+"\"> "+tarefas.get(i).getNome()+"</a> </td>"
                            +  "<td style=\"width:20%;background-color:#D2D2D2;\">"+this.trocaData(formato.format(tarefas.get(i).getDataTermino()))+"</td><td style=\"width:25%;background-color:#D2D2D2;\"> "+tarefas.get(i).getStatus()+" </td>"
                            +"<td style=\"background-color:#D2D2D2;width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaTarefa?idTarefaEscolhida="+tarefas.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"    
                            +"<td style=\"background-color:#D2D2D2;width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaTarefa?tarefaEscolhida="+tarefas.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                }else if(tarefas.get(i).getStatus().endsWith("Atrasada")){
                    tarefasString = tarefasString + "<tr><td style=\"width:45%;background-color:#FFA9A9;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+tarefas.get(i).getNome()+"\"> "+tarefas.get(i).getNome()+"</a> </td>"
                            +  "<td style=\"width:20%;background-color:#FFA9A9;\">"+this.trocaData(formato.format(tarefas.get(i).getDataTermino()))+"</td><td style=\"width:25%;background-color:#FFA9A9;\"> "+tarefas.get(i).getStatus()+" </td>"
                            +"<td style=\"background-color:#FFA9A9;width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaTarefa?idTarefaEscolhida="+tarefas.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"    
                            +"<td style=\"background-color:#FFA9A9;width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaTarefa?tarefaEscolhida="+tarefas.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                }
                        
                    
            }
            tarefasString = tarefasString + "</tbody></table>";
            
            return tarefasString;
    }
    
    @Override
    public String retornarInformacoesTarefaTabela(TarefaBean tarefa, String nomeAtividade) {
      
        UsuarioDAO ud = DAOFactory.createUsuarioDAO();
        UsuarioBean usuario = ud.buscar(Integer.toString(tarefa.getResponsavel().getId()), 5);
        String nomeResponsavel = usuario.getNome();
        
        String tarefaAnteriorString = "";
        if(tarefa.getIdTarefaAnterior()==-1)
            tarefaAnteriorString = "nenhuma";
        else
            tarefaAnteriorString = this.buscar(Integer.toString(tarefa.getIdTarefaAnterior()), 2).getNome();
        
        String tarefaString = "";
                
            
            tarefaString = "<table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações da tarefa selecionada</center></h3></th>"
                         +"</tr></thead><tbody><tr><td WIDTH=\"15%\"><strong>Nome:</strong></td><td WIDTH=\"45%\"> "+tarefa.getNome()+" </td><td width=\"20%\"><strong>Data Inicial:</strong></td><td WIDTH=\"20%\"> "+this.trocaData(formato.format(tarefa.getDataInicio()))+" </td></tr><tr><td><strong>Tarefa Anterior:</strong></td><td>"+tarefaAnteriorString+"</td>"
                            +  "<td><strong>Data Final:</strong></td><td> "+this.trocaData(formato.format(tarefa.getDataTermino()))+" </td></tr><tr><td><strong>Responsável:</strong></td><td>"+ nomeResponsavel+"</td><td><strong>Complexidade:</strong></td><td> "+tarefa.getComplexidade()+" </td></tr><tr><td><strong>Status:</strong></td><td> "+tarefa.getStatus()+" </td><td><strong>Prioridade:</strong></td><td> "+tarefa.getPrioridade()+" </td></tr><tr height=85><td COLSPAN=\"1\"><strong>Descrição:</strong></td><td COLSPAN=\"3\"><textarea name=\"\" id=\"\" cols=\"54\" rows=\"4\"> "+tarefa.getDescricao()+"</textarea></td>"
                                +"</tr></tbody></table>"
                    +"       <form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesAtividade?atividadeParaCarregar="+nomeAtividade+"\">"
             +"       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
           
            
            return tarefaString;
    
    }
    
    @Override
    public void deletarEnvolvidoTarefa(UsuarioBean usuario, TarefaBean tarefa){
        try {
            String SQL = "DELETE FROM usuarioxtarefa WHERE FK_idUsuario = ? AND FK_idTarefa = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
        
            ps.setInt(1, usuario.getId());
            ps.setInt(2, tarefa.getId());
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public ArrayList<UsuarioBean> retornarEnvolvidosTarefa(TarefaBean tarefa){
        try{    
            ArrayList<UsuarioBean> envolvidos = new ArrayList<UsuarioBean>();
            String SQL = "SELECT * FROM usuarioxtarefa ut LEFT JOIN usuario u ON ut.FK_idUsuario = u.id WHERE FK_idTarefa = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, tarefa.getId());
            
            ResultSet rs = ps.executeQuery();
            
            
            ArrayList<String> alunosNome = new ArrayList<String>();
            while(rs.next()){
                if(rs.getString("tipoUsuario").equalsIgnoreCase("aluno"))
                    alunosNome.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            for(int i=0; i<alunosNome.size();i++){
                UsuarioBean usuario = ud.buscar(alunosNome.get(i), 1);
                envolvidos.add(usuario);
            }
            
            return envolvidos;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    
    @Override
    public void adicionarEnvolvidoTarefa(UsuarioBean usuario, TarefaBean tarefa, AtividadeBean atividade){
        try {    
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "SELECT * FROM usuarioxtarefa";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idUsuarioxTarefa =0;
            while(rs.next()){
                idUsuarioxTarefa = rs.getInt("id")+1;
            }
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO usuarioxtarefa (id, FK_idUsuario, FK_idTarefa) VALUES"
                    + "(?,?,?)";
            ps = connection.prepareStatement(SQL);
        
            ps.setInt(1, idUsuarioxTarefa);
            ps.setInt(2,usuario.getId());
            ps.setInt(3,tarefa.getId());
            
            ps.executeUpdate();
            
            ps.close();
            
            
            ArrayList<UsuarioBean> envolvidosAtividade = atividade.getColaboradores();
            
            int seEstaEnvolvido = -1;
            for(int i=0; i<envolvidosAtividade.size();i++){
                if(envolvidosAtividade.get(i).getNome().equalsIgnoreCase(usuario.getNome())){
                    seEstaEnvolvido = 1;
                }
            }
            //Se não estiver envolvido na atividade, adiciona o envolvido
            if(seEstaEnvolvido == -1){
                SQL = "SELECT * FROM usuarioxatividade ORDER BY id DESC LIMIT 1";
                ps = connection.prepareStatement(SQL);
                rs = ps.executeQuery();
                int idUsuarioAtividade =0;
                while(rs.next()){
                    idUsuarioAtividade = rs.getInt("id") + 1;
                }
                ps.close();
                rs.close();
            
                SQL = "INSERT INTO usuarioxatividade (id, FK_idUsuario, FK_idAtividade) VALUES"
                    + "(?,?,?)";
                ps = connection.prepareStatement(SQL);
        
                ps.setInt(1, idUsuarioAtividade);
                ps.setInt(2,usuario.getId());
                ps.setInt(3,atividade.getId());
            
                ps.executeUpdate();
            
                ps.close();
                
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @Override
    public ArrayList<UsuarioBean> retornarNaoEnvolvidosTarefa(TarefaBean tarefa, ProjetoBean projeto){
        
        
        ArrayList<UsuarioBean> envolvidosProjeto = projeto.getEnvolvidos();
        ArrayList<UsuarioBean> envolvidosTarefa = this.retornarEnvolvidosTarefa(tarefa);
        ArrayList<UsuarioBean> naoEnvolvidosTarefa = envolvidosProjeto;
        for(int i=0; i<envolvidosTarefa.size();i++){
            for(int j=0; j<envolvidosProjeto.size();j++){
                if(envolvidosProjeto.get(j).getNome().equalsIgnoreCase(envolvidosTarefa.get(i).getNome())){
                    naoEnvolvidosTarefa.remove(envolvidosProjeto.get(j));
                }
            }
        }
        ArrayList<UsuarioBean> alunosNaoEnvolvidosTarefa = new ArrayList<UsuarioBean>();
        
        for(int i=0; i<naoEnvolvidosTarefa.size();i++){
            if(naoEnvolvidosTarefa.get(i).getTipoUsuario().equalsIgnoreCase("aluno"))
                alunosNaoEnvolvidosTarefa.add(naoEnvolvidosTarefa.get(i));
        }
        
        return alunosNaoEnvolvidosTarefa;
    }
    
    @Override
    public int quantTarefas() {
        try {    
            String SQL = "SELECT id FROM tarefa ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
        
            ResultSet rs = ps.executeQuery();
            int idTarefa =0;
            while(rs.next()){
                idTarefa = rs.getInt("id") + 1;
            }
            ps.close();
            rs.close();
            connection.close();
            
            return idTarefa;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    @Override
    public String retornarTarefaTabelaEditavel(TarefaBean tarefa, String nomeProjeto) {
      
        UsuarioDAO ud = DAOFactory.createUsuarioDAO();
        UsuarioBean usuario = ud.buscar(Integer.toString(tarefa.getResponsavel().getId()), 5);
        String nomeResponsavel = usuario.getNome();
        
        String selectComplexidade = "";
        if(tarefa.getComplexidade().equalsIgnoreCase("Simples")){
            selectComplexidade = "<select name = \"complexidadeEditada\" style=\"width: 95px\"><option selected>Simples</option><option>Média</option><option>Complexa</option></select>";
        }
        else if(tarefa.getComplexidade().equalsIgnoreCase("Media")){
            selectComplexidade = "<select name = \"complexidadeEditada\" style=\"width: 95px\"><option>Simples</option><option selected>Média</option><option>Complexa</option></select>";
        }
        else if(tarefa.getComplexidade().equalsIgnoreCase("Complexa")){
            selectComplexidade = "<select name = \"complexidadeEditada\" style=\"width: 95px\"><option>Simples</option><option>Média</option><option selected>Complexa</option></select>";
        }
        
        
        String selectStatus = "";
        if(tarefa.getStatus().equalsIgnoreCase("Nova tarefa")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option selected>Nova Tarefa</option><option>Em andamento</option><option>Concluida</option><option>Reaberta</option><option>Cancelada</option><option>Atrasada</option></select>";
        }
        else if(tarefa.getStatus().equalsIgnoreCase("Em andamento")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option>Nova Tarefa</option><option selected>Em andamento</option><option>Concluida</option><option>Reaberta</option><option>Cancelada</option><option>Atrasada</option></select>";
        }
        else if(tarefa.getStatus().equalsIgnoreCase("Concluida")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option>Nova Tarefa</option><option>Em andamento</option><option selected>Concluida</option><option>Reaberta</option><option>Cancelada</option><option>Atrasada</option></select>";
        }
        else if(tarefa.getStatus().equalsIgnoreCase("Reaberta")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option>Nova Tarefa</option><option>Em andamento</option><option>Concluida</option><option selected>Reaberta</option><option>Cancelada</option><option>Atrasada</option></select>";
        }
        else if(tarefa.getStatus().equalsIgnoreCase("Cancelada")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option>Nova Tarefa</option><option>Em andamento</option><option>Concluida</option><option>Reaberta</option><option selected>Cancelada</option><option>Atrasada</option></select>";
        }
        else if(tarefa.getStatus().equalsIgnoreCase("Atrasada")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option>Nova Tarefa</option><option>Em andamento</option><option>Concluida</option><option>Reaberta</option><option>Cancelada</option><option selected>Atrasada</option>";
        }
        
        
        String selectPrioridade = "";
        if(tarefa.getPrioridade()==1){
            selectPrioridade = "<select name = \"prioridadeEditada\" style=\"width: 40px\"><option selected>1</option><option>2</option><option>3</option><option>4</option><option>5</option></select>";
        }
        else if(tarefa.getPrioridade()==2){
            selectPrioridade = "<select name = \"prioridadeEditada\" style=\"width: 40px\"><option>1</option><option selected>2</option><option>3</option><option>4</option><option>5</option></select>";
        }
        else if(tarefa.getPrioridade()==3){
            selectPrioridade = "<select name = \"prioridadeEditada\" style=\"width: 40px\"><option>1</option><option>2</option><option selected>3</option><option>4</option><option>5</option></select>";
        }
        else if(tarefa.getPrioridade()==4){
            selectPrioridade = "<select name = \"prioridadeEditada\" style=\"width: 40px\"><option>1</option><option>2</option><option>3</option><option selected>4</option><option>5</option></select>";
        }
        else if(tarefa.getPrioridade()==5){
            selectPrioridade = "<select name = \"prioridadeEditada\" style=\"width: 40px\"><option>1</option><option>2</option><option>3</option><option>4</option><option selected>5</option></select>";
        }
        
        String selectTarefaAnterior = "";
        if(tarefa.getIdTarefaAnterior()==-1)
            selectTarefaAnterior = "<select name = \"tarefaAnteriorEditada\" style=\"width: 150px\"><option selected>nenhuma</option>";
        else{
            selectTarefaAnterior = "<select name = \"tarefaAnteriorEditada\" style=\"width: 150px\"><option>nenhuma</option><option selected>"
                            +this.buscar(Integer.toString(tarefa.getIdTarefaAnterior()), 2).getNome()+ "</option>";
        }
        
        AtividadeDAO ad = DAOFactory.createAtividadeDAO();
        ArrayList<TarefaBean> tarefasPredecessorasPossiveis = this.retornaTarefasPredecessorasPossiveis(ad.buscar(Integer.toString(tarefa.getAtividade()), 2).getTarefas(), tarefa);
        for(int i=0;i<tarefasPredecessorasPossiveis.size();i++)
            selectTarefaAnterior = selectTarefaAnterior+"<option>"+tarefasPredecessorasPossiveis.get(i).getNome()+"</option>";
        selectTarefaAnterior = selectTarefaAnterior + "</select>";
            
        
        String tarefaString = "<form method=\"post\" action=\"/ProjetoColabUFV/ServletEditaTarefa\">"
                +"<table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações da tarefa selecionada</center></h3></th>"
                +"</tr></thead><tbody><tr><td WIDTH=\"15%\"><strong>Nome:</strong></td><td WIDTH=\"15%\"><input type=\"text\" name=\"nomeEditado\" size=\"20\" value=\""+tarefa.getNome()+"\"> </td><td width=\"10%\"><strong>Data Inicial:</strong></td><td WIDTH=\"15%\"><input type=\"date\" size=10 required=\"required\" maxlength=\"10\" name=\"dataInicioEditada\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}$\" min=\"01-01-2016\" max=\"31-12-9999\" value=\""+this.trocaData(formato.format(tarefa.getDataInicio()))+"\"> </td></tr><tr><td><strong>Tarefa Anterior:</strong></td><td>"+selectTarefaAnterior+"</td>"
                +  "<td><strong>Data Final:</strong></td><td><input type=\"date\" size=10 required=\"required\" maxlength=\"10\" name=\"dataTerminoEditada\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}$\" min=\"01-01-2016\" max=\"31-12-9999\" value=\""+this.trocaData(formato.format(tarefa.getDataTermino()))+"\"> </td></tr><tr><td><strong>Responsável:</strong></td><td>"+nomeResponsavel+"</td><td><strong>Complexidade:</strong></td><td>"+selectComplexidade+"</td></tr><tr><td><strong>Status:</strong></td><td>"+selectStatus+"</td><td><strong>Prioridade:</strong></td><td>"+selectPrioridade+"</td></tr><tr height=85><td COLSPAN=\"1\"><strong>Descrição:</strong></td><td COLSPAN=\"3\"><input size=75 type=\"text\" name=\"descricaoEditada\" value=\""+tarefa.getDescricao()+"\"></td>"
                +"</tr></tbody></table>"
                + "<button style=\"width: 150px\" type=\"submit\" name=\"Submit\" value=\"Concluir editar tarefa\">Concluir edição</button>"    
                + "</form><br>";    
            
            return tarefaString;
    
    }
    
    @Override
    public ArrayList<UsuarioBean> retornaEnvolvidosTarefa(String nomeTarefa, String nomeAtividade, String nomeProjeto){
        try{    
             
            int idTarefa=this.buscar(nomeTarefa,1).getId();
            
            ArrayList<UsuarioBean> envolvidos = new ArrayList<UsuarioBean>();
            String SQL = "SELECT * FROM usuarioxtarefa ut LEFT JOIN usuario u ON ut.FK_idUsuario = u.id WHERE FK_idTarefa = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1,idTarefa);
            
            
            ResultSet rs = ps.executeQuery();
            ArrayList<String> envolvidosNome = new ArrayList<String>();
            while(rs.next()){
                envolvidosNome.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            for(int i=0; i<envolvidosNome.size();i++){
                UsuarioBean envolvido = ud.buscar(envolvidosNome.get(i), 1);
                envolvidos.add(envolvido);
            }
            
            return envolvidos;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    @Override
    public String trocaData (String dataAnoMesDia){
        String dataDiaMesAno = dataAnoMesDia.substring(8, 10)+"-"+dataAnoMesDia.substring(5, 7)+"-"+dataAnoMesDia.substring(0, 4);
        return dataDiaMesAno;
    }

     
    
    @Override
    public ArrayList<TarefaBean> retornarTarefasEnvolvido(UsuarioBean usuario, ArrayList<AtividadeBean> atividadesProjeto){
        ArrayList<TarefaBean> tarefasUsuarioEmProjeto = new ArrayList<TarefaBean>();

        for(int i=0; i<atividadesProjeto.size();i++){
            for(int j=0;j<atividadesProjeto.get(i).getTarefas().size();j++){
                for(int k=0;k<atividadesProjeto.get(i).getTarefas().get(j).getColaboradores().size();k++){
                    if(atividadesProjeto.get(i).getTarefas().get(j).getColaboradores().get(k).getNome().equalsIgnoreCase(usuario.getNome())){
                        tarefasUsuarioEmProjeto.add(atividadesProjeto.get(i).getTarefas().get(j));
                    }
                }

            }
        }


        return tarefasUsuarioEmProjeto;
                
    }

    @Override
    public ArrayList<TarefaBean> ordenaTarefas(ArrayList<TarefaBean> tarefas) {
        
        for (int i = tarefas.size(); i >= 1; i--) {
            for (int j = 1; j < i; j++) {
                if (tarefas.get(j - 1).getDataInicio().compareTo(tarefas.get(j).getDataInicio())>0) {
                    TarefaBean aux = tarefas.get(j);
                    tarefas.set(j, tarefas.get(j - 1));
                    tarefas.set(j - 1,aux);
                    
                    tarefas.get(j).setPosicaoCronograma(j);
                    tarefas.get(j-1).setPosicaoCronograma(j-1);
                }
            }
        }
        
        return tarefas;
    }
    
    @Override
    public ArrayList<TarefaBean> retornaTarefasPredecessorasPossiveis(ArrayList<TarefaBean> tarefasAtividade, TarefaBean tarefaSelecionada){
        ArrayList<TarefaBean> tarefasPossiveis = new ArrayList<TarefaBean>();
        int k;
        for(int i=0; i<tarefasAtividade.size();i++){
            if(tarefasAtividade.get(i).getDataTermino().compareTo(tarefaSelecionada.getDataInicio())<=0){
                k=0;
                for(int j=0; j<tarefasAtividade.size();j++){
                    if(tarefasAtividade.get(j).getIdTarefaAnterior() == tarefasAtividade.get(i).getId())
                        k=1;
                }
                if(k==0)
                    tarefasPossiveis.add(tarefasAtividade.get(i));
            } 
        }
        
        return tarefasPossiveis;
    }
    
}
