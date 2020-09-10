package br.com.senacrs.dao;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.TarefaBean;
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

public class JDBCAtividadeDAO implements AtividadeDAO{
    
    Connection connection;
    
    SimpleDateFormat formato;
    
    public JDBCAtividadeDAO(){
        formato = new SimpleDateFormat("yyyy-MM-dd");
        connection = null;
    }
    
    @Override
    public void cadastrar(AtividadeBean atividade) {
        
        try {
            
            String SQL = "SELECT id FROM atividade ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idAtividade = 0;
            while(rs.next()){
                idAtividade = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            
            SQL = "INSERT INTO atividade (id, nome, dataInicio, dataTermino, descricao, FK_responsavel, FK_idProjeto, complexidade, prioridade, status, idAtividadeAnterior) VALUES"
                    + "(?,?,?,?,?,?,?,?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1, idAtividade);
            ps.setString(2,atividade.getNome());
            
            ps.setString(3,formato.format(atividade.getDataInicio()));
            ps.setString(4,formato.format(atividade.getDataTermino()));
            
            ps.setString(5,atividade.getDescricao());
            ps.setInt(6,ud.buscar(atividade.getResponsavel().getNome(),1).getId());
            ps.setInt(7,atividade.getProjeto());
            ps.setString(8,atividade.getComplexidade());
            ps.setInt(9,atividade.getPrioridade());
            ps.setString(10,atividade.getStatus());
            
            
            ps.setInt(11,-1);
            
            ps.executeUpdate();
            
            ps.close();
            
            
            SQL = "SELECT id FROM usuarioxatividade ORDER BY id DESC LIMIT 1";
            ps = connection.prepareStatement(SQL);
            rs = ps.executeQuery();
            int idUsuarioxAtividade =0;
            while(rs.next()){
                idUsuarioxAtividade = rs.getInt("id") + 1;
            }
            ps.close();
            rs.close();
            
            
            SQL = "INSERT INTO usuarioxatividade (id, FK_idUsuario, FK_idAtividade) VALUES"
                    + "(?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1,idUsuarioxAtividade);
            ps.setInt(2,ud.buscar(atividade.getResponsavel().getNome(),1).getId());
            ps.setInt(3,idAtividade);
            
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAtividadeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void deletar(AtividadeBean atividade) {
        try {
            
            ArrayList<TarefaBean> tarefasAtividade = this.retornarTarefasAtividade(atividade);
            
            TarefaDAO td = DAOFactory.createTarefaDAO();
            for(int i=0 ; i<tarefasAtividade.size();i++)
                td.deletar(tarefasAtividade.get(i).getId());
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ArrayList<AtividadeBean> atividadesProjeto = projd.pesquisarProjeto(Integer.toString(atividade.getProjeto()), 2).getAtividades();
            
            //se não tiver atv anterior, não altera nada
            if(atividade.getIdAtividadeAnterior()==-1){
            }
            else{
                for(int i=0; i<atividadesProjeto.size();i++){
                    if(atividadesProjeto.get(i).getIdAtividadeAnterior()== atividade.getId()){
                        atividadesProjeto.get(i).setIdAtividadeAnterior(-1);
                        this.editar(atividadesProjeto.get(i));
                        break;
                    }
                }
            }
            
            String SQL = "DELETE FROM usuarioxatividade WHERE FK_idAtividade= ?";
            if(connection == null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, atividade.getId());
            ps.executeUpdate();
            
            ps.close();
            
            
            SQL = "DELETE FROM atividade WHERE id= ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, atividade.getId());
            ps.executeUpdate();
            
            ps.close();
            
            connection.close();
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAtividadeDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover uma atividade", ex);
        }
        
    }

    @Override
    public ArrayList<AtividadeBean> listarAtividades(int idProjeto) {
        ArrayList<AtividadeBean> atividades = new ArrayList<AtividadeBean>();
        try {
            String SQL = "SELECT * FROM atividade WHERE FK_idProjeto = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, idProjeto);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> atividadesString = new ArrayList<String>();
            while(rs.next()){
                atividadesString.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0;i<atividadesString.size();i++){
                AtividadeBean atividade = this.buscar(atividadesString.get(i),1);
                atividades.add(atividade);
            }
            
            return atividades;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAtividadeDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar atividade em JDBCAtividadeDAO", ex);
        }
    }

    /*Posso buscar por:
        (1) nome
        (2) id
    */
    @Override
    public AtividadeBean buscar(String informacao, int tipo) {
        try {
            
            AtividadeBean atividade = new AtividadeBean();
            String SQL = null;
            PreparedStatement ps = null;
            
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            
            if(tipo==1){
                SQL = "SELECT * FROM atividade WHERE nome = ?";
                ps = connection.prepareStatement(SQL);
                ps.setString(1, informacao);
            }
            else if(tipo==2){
                SQL = "SELECT * FROM atividade WHERE id = ?";
                ps = connection.prepareStatement(SQL);
                ps.setInt(1, Integer.parseInt(informacao));
            }
            ResultSet rs = ps.executeQuery();
            
            rs.next();
            atividade.setNome(rs.getString("nome"));
            atividade.setDescricao(rs.getString("descricao"));
            
            java.util.Date dataInicio=rs.getDate("dataInicio");
            java.util.Date dataTermino = rs.getDate("dataTermino");
            atividade.setDataInicio(dataInicio);
            atividade.setDataTermino(dataTermino);
            
            atividade.setId(rs.getInt("id"));
            
            atividade.setProjeto(rs.getInt("FK_idProjeto"));
            atividade.setComplexidade(rs.getString("complexidade"));
            atividade.setPrioridade(rs.getInt("prioridade"));
            atividade.setStatus(rs.getString("status"));
            atividade.setIdAtividadeAnterior(rs.getInt("idAtividadeAnterior"));
            
            int idResponsavel= rs.getInt("FK_responsavel");
            
            ps.close();
            rs.close();
            connection.close();
            
            atividade.setColaboradores(this.retornarEnvolvidosAtividade(atividade));
            atividade.setTarefas(this.retornarTarefasAtividade(atividade));
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            atividade.setResponsavel(ud.buscar(Integer.toString(idResponsavel),5));
            
            ArtefatoDAO artd = DAOFactory.createArtefatoDAO();
            atividade.setArtefatos(artd.listarArtefatos(atividade.getId()));
            
            return atividade;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAtividadeDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
        
    }

    @Override
    public void editar(AtividadeBean atividade) {
        try { 
            String SQL = "update atividade set nome=?, dataInicio=?, dataTermino=?, descricao=?, FK_responsavel=?, FK_idProjeto=?, complexidade=?, prioridade=?, status=?, idAtividadeAnterior=? where id=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            
            ps.setString(1, atividade.getNome());
            
            ps.setString(2, formato.format(atividade.getDataInicio()));
            ps.setString(3, formato.format(atividade.getDataTermino()));
            
            ps.setString(4, atividade.getDescricao());
            ps.setInt(5, atividade.getResponsavel().getId());
            ps.setInt(6, atividade.getProjeto());
            ps.setString(7, atividade.getComplexidade());
            ps.setInt(8, atividade.getPrioridade());
            ps.setString(9, atividade.getStatus());
            ps.setInt(10, atividade.getIdAtividadeAnterior());
            ps.setInt(11, atividade.getId());
            
            ps.execute();
            
            ps.close();
            connection.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAtividadeDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao buscar um registro de atividade pelo id", ex);
        }
    
    }
    

    @Override
    public String retornaAtividadesTabela(ArrayList<AtividadeBean> atividades) {
        String atividadesString = "";
                
            atividadesString = "<table id=\"example\" ><thead><tr><th>Nome</th><th>Data Final</th><th>Status</th><th style=\"padding-left:0px;\">Edit</th><th style=\"padding-left:0px;\">Del</th>"
                         +"</tr></thead><tbody>";
            for(int i=0;i<atividades.size();i++){
                atividadesString = atividadesString + "<tr><td style=\"width:50%;\"><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesAtividade?atividadeParaCarregar="+atividades.get(i).getNome()+"\"> "+atividades.get(i).getNome()+" </a></td>"
                            +  "<td style=\"width:20%;\">"+this.trocaData(formato.format(atividades.get(i).getDataTermino()))+"</td><td style=\"width:20%;\"> "+atividades.get(i).getStatus()+" </td><td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-left:0px;padding-right:0px;\"><a href=\"/ProjetoColabUFV/ServletCarregaEditaAtividade?idAtividadeEscolhida="+atividades.get(i).getId()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemEditar.jpg\" width=\"30\" height=\"25\" alt=\"imgEditar\"/></a></td>"
                                +"<td style=\"width:5%;padding-top:0px;padding-bottom:0px;padding-right:0px;padding-left:0px;\"><a href=\"/ProjetoColabUFV/ServletDeletaAtividade?atividadeEscolhida="+atividades.get(i).getNome()+"\" style=\"font-size:50px;text-decoration:none;\"><img src=\"imagens/imagemExcluir.jpg\" width=\"30\" height=\"25\" alt=\"imgExcluir\"/></a></td></tr>";
                    
            }
            atividadesString = atividadesString + "</tbody></table>";
            
            return atividadesString;
    }

    @Override
    public String retornarInformacoesAtividadeTabela(AtividadeBean atividade, String nomeProjeto) {
      
        UsuarioDAO ud = DAOFactory.createUsuarioDAO();
        UsuarioBean usuario = ud.buscar(Integer.toString(atividade.getResponsavel().getId()), 5);
        String nomeResponsavel = usuario.getNome();
        
        String atvAnterior = "";
        if(atividade.getIdAtividadeAnterior()==-1)
            atvAnterior = "nenhuma";
        else
            atvAnterior = this.buscar(Integer.toString(atividade.getIdAtividadeAnterior()), 2).getNome();
        
        String atividadeString = "";
                
            atividadeString = "<table><thead><tr><th COLSPAN=\"4\"><h3><center>Informações da atividade selecionada</center></h3></th>"
                         +"</tr></thead><tbody><tr><td WIDTH=\"15%\"><strong>Nome:</strong></td><td WIDTH=\"45%\"> "+atividade.getNome()+" </td><td style=\"width:20%;\"><strong>Data Inicial:</strong></td><td style=\"width:20%;\"> "+this.trocaData(formato.format(atividade.getDataInicio()))+" </td></tr><tr><td><strong>Atv Anterior:</strong></td><td >"+atvAnterior+"</td>"
                            +  "<td><strong>Data Término:</strong></td><td> "+this.trocaData(formato.format(atividade.getDataTermino()))+" </td></tr><tr><td><strong>Responsável:</strong></td><td>"+ nomeResponsavel+"</td><td><strong>Complexidade:</strong></td><td> "+atividade.getComplexidade()+" </td></tr><tr><td><strong>Status:</strong></td><td> "+atividade.getStatus()+" </td><td><strong>Prioridade:</strong></td><td> "+atividade.getPrioridade()+" </td></tr><tr height=85><td COLSPAN=\"1\"><strong>Descrição:</strong></td><td COLSPAN=\"3\"><textarea name=\"\" id=\"\" cols=\"54\" rows=\"4\"> "+atividade.getDescricao()+"</textarea></td>"
                                +"</tr></tbody></table>";
           
            
            return atividadeString;
    
    }
    
    @Override
    public void deletarEnvolvidoAtividade(UsuarioBean usuario, AtividadeBean atividade){
        try {
            String SQL = "DELETE FROM usuarioxatividade WHERE FK_idUsuario = ? AND FK_idAtividade = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
        
            ps.setInt(1, usuario.getId());
            ps.setInt(2, atividade.getId());
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
            TarefaDAO td = DAOFactory.createTarefaDAO();
            ArrayList<TarefaBean> tarefasAtividade = atividade.getTarefas();
            for(int i=0; i<tarefasAtividade.size();i++)
                td.deletarEnvolvidoTarefa(usuario, tarefasAtividade.get(i));
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public ArrayList<UsuarioBean> retornarEnvolvidosAtividade(AtividadeBean atividade){
        try{    
            
            ArrayList<UsuarioBean> usuarios = new ArrayList<UsuarioBean>();
            String SQL = "SELECT * FROM usuarioxatividade ua LEFT JOIN usuario u ON ua.FK_idUsuario = u.id WHERE FK_idAtividade = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, atividade.getId());
            
            
            ResultSet rs = ps.executeQuery();
            
            ArrayList<String> usuariosString = new ArrayList<String>();
            while(rs.next()){
                if(rs.getString("tipoUsuario").equalsIgnoreCase("aluno"))
                    usuariosString.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            for(int i=0;i<usuariosString.size();i++){
                UsuarioBean usuario = ud.buscar(usuariosString.get(i),1);
                usuarios.add(usuario);
            }
            
            return usuarios;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAtividadeDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
 
    @Override
    public void adicionarEnvolvidoAtividade(UsuarioBean usuario, AtividadeBean atividade){
        try {    
            
            ArrayList<UsuarioBean> envolvidosAtividade = atividade.getAlunosEnvolvidos();
            for(int i=0;i<envolvidosAtividade.size();i++)
                if(envolvidosAtividade.get(i).getNome().equalsIgnoreCase(usuario.getNome()))
                    return;
            String SQL = "SELECT * FROM usuarioxatividade ORDER BY id DESC LIMIT 1";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
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
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCAtividadeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public ArrayList<UsuarioBean> retornarNaoEnvolvidosAtividade(AtividadeBean atividade, ProjetoBean projeto){
        
            ArrayList<UsuarioBean>envolvidosProjeto = projeto.getEnvolvidos();
            ArrayList<UsuarioBean> envolvidosAtividade = this.retornarEnvolvidosAtividade(atividade);
            
            ArrayList<UsuarioBean> alunosNaoAtividade = envolvidosProjeto;
            for(int i=0; i<envolvidosAtividade.size();i++){
                for(int j=0; j<envolvidosProjeto.size();j++){
                    if(envolvidosProjeto.get(j).getNome().equalsIgnoreCase(envolvidosAtividade.get(i).getNome())){
                       alunosNaoAtividade.remove(envolvidosProjeto.get(j));
                    }
                }
            }
            ArrayList<UsuarioBean> alunosNaoEnvolvidosAtividade = new ArrayList<UsuarioBean>();
        
            for(int i=0; i<alunosNaoAtividade.size();i++){
                if(alunosNaoAtividade.get(i).getTipoUsuario().equalsIgnoreCase("aluno"))
                    alunosNaoEnvolvidosAtividade.add(alunosNaoAtividade.get(i));
            }

        return alunosNaoEnvolvidosAtividade;
    }
    
    
    @Override
    public ArrayList<TarefaBean> retornarTarefasAtividade(AtividadeBean atividade){
        try{ 
            
            String SQL = "SELECT * FROM tarefa WHERE FK_idAtividade = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, atividade.getId());
            
            
            ResultSet rs = ps.executeQuery();
            ArrayList<TarefaBean> tarefas = new ArrayList<TarefaBean>();
            ArrayList<String> tarefasString = new ArrayList<String>();
            while(rs.next()){
                tarefasString.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            TarefaDAO td = DAOFactory.createTarefaDAO();
            for(int i=0;i<tarefasString.size();i++){
                TarefaBean tarefa = td.buscar(tarefasString.get(i),1);
                tarefas.add(tarefa);
            }
            
            return tarefas;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTarefaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Override
    public String retornarAtividadeTabelaEditavel(AtividadeBean atividade, ProjetoBean projeto) {
      
        UsuarioDAO ud = DAOFactory.createUsuarioDAO();
        UsuarioBean usuario = ud.buscar(Integer.toString(atividade.getResponsavel().getId()), 5);
        String nomeResponsavel = usuario.getNome();
        
        String selectComplexidade = "";
        if(atividade.getComplexidade().equalsIgnoreCase("Simples")){
            selectComplexidade = "<select name = \"complexidadeEditada\" style=\"width: 95px\"><option selected>Simples</option><option>Média</option><option>Complexa</option></select>";
        }
        else if(atividade.getComplexidade().equalsIgnoreCase("Media")){
            selectComplexidade = "<select name = \"complexidadeEditada\" style=\"width: 95px\"><option>Simples</option><option selected>Média</option><option>Complexa</option></select>";
        }
        else if(atividade.getComplexidade().equalsIgnoreCase("Complexa")){
            selectComplexidade = "<select name = \"complexidadeEditada\" style=\"width: 95px\"><option>Simples</option><option>Média</option><option selected>Complexa</option></select>";
        }
        
        
        String selectStatus = "";
        if(atividade.getStatus().equalsIgnoreCase("Nova Atividade")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option selected>Nova Atividade</option><option>Em andamento</option><option>Concluida</option><option>Reaberta</option><option>Cancelada</option><option>Atrasada</option></select>";
        }
        else if(atividade.getStatus().equalsIgnoreCase("Em andamento")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option>Nova Atividade</option><option selected>Em andamento</option><option>Concluida</option><option>Reaberta</option><option>Cancelada</option><option>Atrasada</option></select>";
        }
        else if(atividade.getStatus().equalsIgnoreCase("Concluida")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option>Nova Atividade</option><option>Em andamento</option><option selected>Concluida</option><option>Reaberta</option><option>Cancelada</option><option>Atrasada</option></select>";
        }
        else if(atividade.getStatus().equalsIgnoreCase("Reaberta")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option>Nova Atividade</option><option>Em andamento</option><option>Concluida</option><option selected>Reaberta</option><option>Cancelada</option><option>Atrasada</option></select>";
        }
        else if(atividade.getStatus().equalsIgnoreCase("Cancelada")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option>Nova Atividade</option><option>Em andamento</option><option>Concluida</option><option>Reaberta</option><option selected>Cancelada</option><option>Atrasada</option></select>";
        }
        else if(atividade.getStatus().equalsIgnoreCase("Atrasada")){
            selectStatus = "<select name = \"statusEditado\" style=\"width: 150px\"><option>Nova Atividade</option><option>Em andamento</option><option>Concluida</option><option>Reaberta</option><option>Cancelada</option><option selected>Atrasada</option>";
        }
        
        
        String selectPrioridade = "";
        if(atividade.getPrioridade()==1){
            selectPrioridade = "<select name = \"prioridadeEditada\" style=\"width: 40px\"><option selected>1</option><option>2</option><option>3</option><option>4</option><option>5</option></select>";
        }
        else if(atividade.getPrioridade()==2){
            selectPrioridade = "<select name = \"prioridadeEditada\" style=\"width: 40px\"><option>1</option><option selected>2</option><option>3</option><option>4</option><option>5</option></select>";
        }
        else if(atividade.getPrioridade()==3){
            selectPrioridade = "<select name = \"prioridadeEditada\" style=\"width: 40px\"><option>1</option><option>2</option><option selected>3</option><option>4</option><option>5</option></select>";
        }
        else if(atividade.getPrioridade()==4){
            selectPrioridade = "<select name = \"prioridadeEditada\" style=\"width: 40px\"><option>1</option><option>2</option><option>3</option><option selected>4</option><option>5</option></select>";
        }
        else if(atividade.getPrioridade()==5){
            selectPrioridade = "<select name = \"prioridadeEditada\" style=\"width: 40px\"><option>1</option><option>2</option><option>3</option><option>4</option><option selected>5</option></select>";
        }
        
        String selectAtividadeAnterior = "";
        if(atividade.getIdAtividadeAnterior()==-1)
            selectAtividadeAnterior = "<select name = \"atividadeAnteriorEditada\" style=\"width: 150px\"><option selected>nenhuma</option>";
        else{
            selectAtividadeAnterior = "<select name = \"atividadeAnteriorEditada\" style=\"width: 150px\"><option>nenhuma</option><option selected>"
                            +this.buscar(Integer.toString(atividade.getIdAtividadeAnterior()), 2).getNome()+ "</option>";
        }
        
        ArrayList<AtividadeBean> atividadesPredecessorasPossiveis = this.retornaAtividadesPredecessorasPossiveis(projeto.getAtividades(), atividade);
        for(int i=0;i<atividadesPredecessorasPossiveis.size();i++)
            selectAtividadeAnterior = selectAtividadeAnterior+"<option>"+atividadesPredecessorasPossiveis.get(i).getNome()+"</option>";
        selectAtividadeAnterior = selectAtividadeAnterior + "</select>";
            
        String atividadeString = "<form method=\"post\" action=\"/ProjetoColabUFV/ServletEditaAtividade\">"
            +"<table><thead><tr ><th COLSPAN=\"4\"><h3><center>Informações da atividade selecionada</center></h3></th>"
            +"</tr></thead><tbody><tr><td WIDTH=\"15%\"><strong>Nome</strong></td><td WIDTH=\"30%\"><input type=\"text\" size=25 name=\"nomeEditado\" value=\""+atividade.getNome()+"\"> </td><td WIDTH=\"15%\"><strong>Data Inicial</strong></td><td WIDTH=\"35%\"><input style=\"width: 100px\" size=8 type=\"date\" required=\"required\" maxlength=\"10\" name=\"dataInicioEditada\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}$\" min=\"2016-01-01\" max=\"9999-12-31\" value=\""+this.trocaData(formato.format(atividade.getDataInicio()))+"\"> </td></tr><tr><td><strong>Atv Anterior</strong></td><td>"+selectAtividadeAnterior+"</td>"
            +  "<td><strong>Data Final</strong></td><td><input style=\"width: 100px\" size=8 type=\"date\" required=\"required\" maxlength=\"10\" name=\"dataTerminoEditada\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}$\" min=\"01-01-2016\" max=\"31-12-9999\" value=\""+this.trocaData(formato.format(atividade.getDataTermino()))+"\"> </td></tr><tr><td><strong>Responsável</strong></td><td>"+nomeResponsavel+"</td><td><strong>Complexidade</strong></td><td>"+selectComplexidade+"</td></tr><tr><td><strong>Status</strong></td><td>"+selectStatus+"</td><td><strong>Prioridade</strong></td><td>"+selectPrioridade+"</td></tr><tr height=85><td COLSPAN=\"1\"><strong>Descrição</strong></td><td COLSPAN=\"3\"><input size=65 type=\"text\" name=\"descricaoEditada\"  value=\""+atividade.getDescricao()+"\"></td>"
            +"</tr></tbody></table>"
            + "<button style=\"width: 150px\" type=\"submit\" name=\"Submit\" value=\"Concluir editar atividade\">Concluir edição</button>"    
            + "</form><br>";
           
            
            return atividadeString;
    
    }
    
    @Override
    public String trocaData (String dataAnoMesDia){
        String dataDiaMesAno = dataAnoMesDia.substring(8, 10)+"-"+dataAnoMesDia.substring(5, 7)+"-"+dataAnoMesDia.substring(0, 4);
        return dataDiaMesAno;
    }
    
    @Override
    public ArrayList<AtividadeBean> retornarAtividadesEnvolvido(UsuarioBean usuario, ProjetoBean projeto){
     
            ArrayList<AtividadeBean> atividadesUsuario = new ArrayList<AtividadeBean>();
            
            for(int i=0; i<projeto.getAtividades().size();i++){
                ArrayList<UsuarioBean> envolvidos = this.retornarEnvolvidosAtividade(projeto.getAtividades().get(i));
                for(int j=0; j<envolvidos.size();j++){
                    if(envolvidos.get(j).getNome().equalsIgnoreCase(usuario.getNome())){
                        atividadesUsuario.add(projeto.getAtividades().get(i));
                    }
                }
            }
            return atividadesUsuario;
         
    }
    
    
    @Override
    public ArrayList<AtividadeBean> retornaAtividadesPredecessorasPossiveis(ArrayList<AtividadeBean> atividadesProjeto, AtividadeBean atividadeSelecionada){
        ArrayList<AtividadeBean> atividadesPossiveis = new ArrayList<AtividadeBean>();
        int k;
        for(int i=0; i<atividadesProjeto.size();i++){
            if(atividadesProjeto.get(i).getDataTermino().compareTo(atividadeSelecionada.getDataInicio())<=0){
                k=0;
                for(int j=0; j<atividadesProjeto.size();j++){
                    if(atividadesProjeto.get(j).getIdAtividadeAnterior() == atividadesProjeto.get(i).getId())
                        k=1;
                }
                if(k==0)
                    atividadesPossiveis.add(atividadesProjeto.get(i));
            } 
        }
        
        return atividadesPossiveis;
    }
    
    @Override
    public ArrayList<AtividadeBean> ordenaAtividades(ArrayList<AtividadeBean> atividades) {
        
        for (int i = atividades.size(); i >= 1; i--) {
            for (int j = 1; j < i; j++) {
                if (atividades.get(j - 1).getDataInicio().compareTo(atividades.get(j).getDataInicio())>0) {
                    AtividadeBean aux = atividades.get(j);
                    atividades.set(j, atividades.get(j - 1));
                    atividades.set(j - 1,aux);
                    
                    atividades.get(j).setPosicaoCronograma(j);
                    atividades.get(j-1).setPosicaoCronograma(j-1);
                }
            }
        }
        
        return atividades;
    }
    
}
