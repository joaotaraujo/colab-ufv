package br.com.senacrs.dao;

import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.PapelBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.ConnectionFactory;
import br.com.senacrs.connections.DAOFactory;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCProjetoDAO implements ProjetoDAO{

    Connection connection;
    
    SimpleDateFormat formato;
    
    public JDBCProjetoDAO(){
        formato = new SimpleDateFormat("yyyy-MM-dd");
        connection = null;
    }
    
    @Override
    public void cadastrarProjeto(ProjetoBean projeto) {
        
        try {
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "SELECT id FROM projeto ORDER BY id DESC LIMIT 1";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            int idProjeto=0;
            while(rs.next()){
                idProjeto = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO projeto (nome, dataInicio, dataTermino, descricao, id, idAutor) VALUES"
                    + "(?,?,?,?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setString(1,projeto.getNome());
            
            ps.setString(2,formato.format(projeto.getDataInicio()));
            ps.setString(3,formato.format(projeto.getDataTermino()));
            
            ps.setString(4,projeto.getDescricao());
            ps.setInt(5, idProjeto);
            ps.setInt(6, projeto.getIdAutor());
            
            ps.executeUpdate();
            
            ps.close();
            
            SQL = "SELECT id FROM papel ORDER BY id DESC LIMIT 1";
            ps = connection.prepareStatement(SQL);
            rs = ps.executeQuery();
            int idPapel=0;
            while(rs.next()){
                idPapel = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            UsuarioBean autor = ud.buscar(Integer.toString(projeto.getIdAutor()), 5);
            
            SQL = "INSERT INTO papel (id, idProjeto, funcao, descricao) VALUES"
                    + "(?,?,?,?)";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1,idPapel);
            ps.setInt(2,idProjeto);
            ps.setString(3,"Autor");
            ps.setString(4,"Professor autor do projeto.");
            
            ps.executeUpdate();
            
            ps.close();
            
            
            SQL = "SELECT id FROM usuarioxpapel ORDER BY id DESC LIMIT 1";
            ps = connection.prepareStatement(SQL);
            rs = ps.executeQuery();
            int idUsuarioxPapel=0;
            while(rs.next()){
                idUsuarioxPapel = rs.getInt("id")+1;
            }
            
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO usuarioxpapel (id, idUsuario, idPapel) VALUES"
                    + "(?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1,idUsuarioxPapel);
            ps.setInt(2,autor.getId());
            ps.setInt(3,idPapel);
            
            ps.executeUpdate();
            
            ps.close();
            
            
            SQL = "SELECT id FROM cronograma ORDER BY id DESC LIMIT 1";
            ps = connection.prepareStatement(SQL);
            rs = ps.executeQuery();
            int idCronograma=0;
            while(rs.next()){
                idCronograma = rs.getInt("id")+1;
            }
            ps.close();
            rs.close();
            
            SQL = "INSERT INTO cronograma (id, idProjeto, descricao) VALUES"
                    + "(?,?,?)";
            ps = connection.prepareStatement(SQL);
            
            ps.setInt(1,idCronograma);
            ps.setInt(2,idProjeto);
            ps.setString(3,"Cronograma ainda não editado.");
            
            ps.executeUpdate();
            
            ps.close();
            
            
            connection.close();
            
            ForumDAO fod = DAOFactory.createForumDAO();
            fod.cadastrarForum(idProjeto);
            
            ChatDAO chatd = DAOFactory.createChatDAO();
            chatd.cadastrarChat(idProjeto);
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCProjetoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void deletarProjeto(int idProjeto) {
        try {
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "SELECT * FROM projeto WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1,idProjeto);
            ResultSet rs = ps.executeQuery();
            String nomeProjeto = "";
            rs.next();
            nomeProjeto = rs.getString("nome");
            
            ps.close();
            rs.close();
            connection.close();
            
            ProjetoBean projeto = this.pesquisarProjeto(nomeProjeto,1);
            
            AtividadeDAO ad = DAOFactory.createAtividadeDAO();
            //deleta as atividades do projeto
            for(int i=0; i<projeto.getAtividades().size();i++)
                ad.deletar(projeto.getAtividades().get(i));
                
            GrupoDAO gd = DAOFactory.createGrupoDAO();
            //deleta os grupos do projeto
            for(int i=0; i<projeto.getGrupos().size();i++)
                gd.deletarGrupo(projeto.getGrupos().get(i).getNome());
            
            PerguntaFrequenteDAO pfd = DAOFactory.createPerguntaFrequenteDAO();
            //deleta as perguntas frequentes do projeto
            for(int i=0; i<projeto.getPerguntasFrequentes().size();i++)
                pfd.deletar(projeto.getPerguntasFrequentes().get(i).getPergunta());
            
            PapelDAO pad = DAOFactory.createPapelDAO();
            //deleta os papeis do projeto
            for(int i=0; i<projeto.getPapeis().size();i++)
                pad.deletar(projeto.getPapeis().get(i).getId());
            
            ForumDAO fd = DAOFactory.createForumDAO();
            fd.deletarForum(projeto.getId());
            
            ChatDAO chatd = DAOFactory.createChatDAO();
            chatd.deletarChat(projeto.getId());
            
            SQL = "DELETE FROM projeto WHERE nome= ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            ps = connection.prepareStatement(SQL);
            ps.setString(1, nomeProjeto);
            ps.executeUpdate();
            
            ps.close();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCProjetoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao remover um projeto", ex);
        }
        
    }

    @Override
    public ArrayList<ProjetoBean> listarProjetos() {
        ArrayList<ProjetoBean> projetos = new ArrayList<ProjetoBean>();
        try {
            String SQL = "SELECT * FROM projeto";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<String> projetosNome = new ArrayList<String>();
            while(rs.next()){
                projetosNome.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            for(int i=0;i<projetosNome.size();i++){
                ProjetoBean projeto = this.pesquisarProjeto(projetosNome.get(i),1);
                projetos.add(projeto);
            }
            
            return projetos;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCProjetoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar projetos em JDBCProjetoDAO", ex);
        }
    }

    
    @Override
    /* tipo
        1- por nome
        2- por id
    */
    public ProjetoBean pesquisarProjeto(String informacao, int tipo) {
        try {
            ProjetoBean projeto = new ProjetoBean();
            
            String SQL = "";
            if(tipo==1)
                SQL = "SELECT * FROM projeto WHERE nome = ?";
            else if(tipo==2)
                SQL = "SELECT * FROM projeto WHERE id = ?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, informacao);
            ResultSet rs = ps.executeQuery();
            rs.next();
            projeto.setNome(rs.getString("nome"));
            projeto.setDescricao(rs.getString("descricao"));
            
            Date dataInicio=rs.getDate("dataInicio");
            Date dataTermino = rs.getDate("dataTermino");
            projeto.setDataInicio(dataInicio);
            projeto.setDataTermino(dataTermino);
            
            projeto.setIdAutor(rs.getInt("idAutor"));
            projeto.setId(rs.getInt("id"));
            
            ps.close();
            rs.close();
            connection.close();
            
            AtividadeDAO ad = DAOFactory.createAtividadeDAO();
            projeto.setAtividades(ad.listarAtividades(projeto.getId()));
            
            GrupoDAO gd = DAOFactory.createGrupoDAO();
            projeto.setGrupos(gd.listarGrupos(projeto.getNome()));
            
            PerguntaFrequenteDAO pfd = DAOFactory.createPerguntaFrequenteDAO();
            projeto.setPerguntasFrequentes(pfd.listarPerguntasFrequentes(projeto.getNome()));
            
            PapelDAO pad = DAOFactory.createPapelDAO();
            projeto.setPapeis(pad.listarPapeis(projeto.getNome()));
            
            ForumDAO fod = DAOFactory.createForumDAO();
            projeto.setForum(fod.buscarForum(projeto.getId()));
            
            ChatDAO chatd = DAOFactory.createChatDAO();
            projeto.setChat(chatd.buscarChat(projeto.getId()));
            
            projeto.setEnvolvidos(this.listarEnvolvidosNoProjeto(projeto.getNome()));
            
            
            return projeto;
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCProjetoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    @Override
    public void editarProjeto(ProjetoBean projeto) {
        try { 
            String SQL = "update projeto set nome=?, dataInicio=?, dataTermino=?, descricao=?, idAutor=? where id=?";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            
            ps.setString(1,projeto.getNome());
            
            ps.setString(2,formato.format(projeto.getDataInicio()));
            ps.setString(3,formato.format(projeto.getDataTermino()));
            
            ps.setString(4,projeto.getDescricao());
            ps.setInt(5, projeto.getIdAutor());
            ps.setInt(6, projeto.getId());
            ps.execute();
            
            ps.close();
            connection.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(JDBCProjetoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao buscar um registro de projeto pelo nome", ex);
        }
    
    }
    
    
    @Override
    public String retornarProjetoTabela(ProjetoBean projeto){
        try{
            String projetoString = "";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "SELECT nome FROM usuario WHERE id = ?";
            PreparedStatement ps= connection.prepareStatement(SQL);
        
            ps.setInt(1, projeto.getIdAutor());
            ResultSet rs = ps.executeQuery();
            String nomeAutor="";
            while(rs.next()){
                nomeAutor = rs.getString("nome");
            }
            ps.close();
            rs.close();
            
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            projetoString = "<table cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações do projeto</center></h3></th>"
                         +"</tr></thead><tbody><tr><td WIDTH=\"15%\"><strong>Nome:</strong></td><td WIDTH=\"45%\"> "+projeto.getNome()+" </td><td WIDTH=\"20%\"><strong>Data Inicial:</strong></td><td WIDTH=\"20%\"> "+this.trocaData(formato.format(projeto.getDataInicio()))+" </td></tr><tr><td WIDTH=\"20%\"><strong>Autor:</strong></td><td WIDTH=\"45%\">"+nomeAutor+"</td>"
                            +  "<td width=\"15%\"><strong>Data Final:</strong></td><td WIDTH=\"20%\"> "+this.trocaData(formato.format(projeto.getDataTermino()))+" </td></tr><tr height=85><td COLSPAN=\"1\"><strong>Descrição:</strong></td><td COLSPAN=\"3\"><textarea name=\"\" id=\"\" cols=\"57\" rows=\"4\">"+projeto.getDescricao()+"</textarea></td>"
                                +"</tr></tbody></table>";
            
            connection.close();
            
            return projetoString;
            
            } catch (SQLException ex) {
            return "";
        }
    }
    
    @Override
    public String retornarProjetoTabelaEditavel(ProjetoBean projeto){
        try{
            String projetoString = "";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection(); 
            String SQL = "SELECT nome FROM usuario WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
        
            ps.setInt(1, projeto.getIdAutor());
            ResultSet rs = ps.executeQuery();
            String nomeAutor="";
            while(rs.next()){
                nomeAutor = rs.getString("nome");
            }
            ps.close();
            rs.close();
            
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            projetoString = "<form method=\"post\" action=\"/ProjetoColabUFV/ServletEditaProjeto?nomeAutor="+nomeAutor+"\">"
                        + "<table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações do projeto</center></h3></th>"
                         +"</tr></thead><tbody><tr><td WIDTH=\"15%\"><strong>Nome</strong></td><td WIDTH=\"50%\"><input size=\"30\" type=\"text\" name=\"nome\" value=\""+projeto.getNome()+"\"> </td><td WIDTH=\"15%\"><strong>Data Inicial:</strong></td><td WIDTH=\"20%\"><input size=\"10\" type=\"date\" required=\"required\" maxlength=\"10\" name=\"dataInicioEditada\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}$\" min=\"01-01-2016\" max=\"31-12-9999\" value=\""+this.trocaData(formato.format(projeto.getDataInicio()))+"\"> </td></tr><tr><td WIDTH=\"20%\"><strong>Autor:</strong></td><td WIDTH=\"40%\">"+nomeAutor+"</td>"
                            +  "<td><strong>Data Final:</strong></td><td WIDTH=\"20%\"><input type=\"date\" required=\"required\" size=\"10\" maxlength=\"10\" name=\"dataTerminoEditada\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}$\" min=\"01-01-2016\" max=\"31-12-9999\" value=\""+this.trocaData(formato.format(projeto.getDataTermino()))+"\"> </td></tr><tr height=85><td COLSPAN=\"1\"><strong>Descrição:</strong></td><td COLSPAN=\"3\"><input type=\"text\" size=70 name=\"descricao\" value=\""+projeto.getDescricao()+"\"></td>"
                                +"</tr></tbody></table>"
                        + "<button style=\"width: 150px\" type=\"submit\" name=\"Submit\" value=\"Concluir editar projeto\">Concluir edição</button>"    
                        + "</form><br>";
            
            connection.close();
            return projetoString;
            
            } catch (SQLException ex) {
            return "";
        }
    }
    
    
    @Override
    public boolean usuarioEstaEmProjeto(String nomeUsuario, String nomeProjeto){
        ProjetoBean projeto = this.pesquisarProjeto(nomeProjeto,1);
        ArrayList<PapelBean> papeisProjeto = projeto.getPapeis();
        PapelDAO pad = DAOFactory.createPapelDAO();
        
        for(int i=0; i<papeisProjeto.size();i++){
            ArrayList<UsuarioBean> envolvidosPapel = pad.listarEnvolvidosPapel(papeisProjeto.get(i).getId());
            for(int j=0; j<envolvidosPapel.size();j++){
                if(envolvidosPapel.get(j).getNome().equalsIgnoreCase(nomeUsuario)){
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public ArrayList<UsuarioBean> listarAlunosNoProjeto(String nomeProjeto) {
        ProjetoBean projeto = this.pesquisarProjeto(nomeProjeto,1);
        ArrayList<PapelBean> papeisProjeto = projeto.getPapeis();
        ArrayList<UsuarioBean> alunosEnvolvidosProjeto = new ArrayList<UsuarioBean>();
        PapelDAO pad = DAOFactory.createPapelDAO();
        
        for(int i=0; i<papeisProjeto.size();i++){
            ArrayList<UsuarioBean> envolvidosPapel = pad.listarEnvolvidosPapel(papeisProjeto.get(i).getId());
            for(int j=0;j<envolvidosPapel.size();j++)
                if(envolvidosPapel.get(j).getTipoUsuario().equalsIgnoreCase("aluno"))
                    alunosEnvolvidosProjeto.add(envolvidosPapel.get(j));
        }
        return alunosEnvolvidosProjeto;
        
    }

    @Override
    public ArrayList<UsuarioBean> listarProfessoresNoProjeto(String nomeProjeto) {
        ProjetoBean projeto = this.pesquisarProjeto(nomeProjeto,1);
        ArrayList<UsuarioBean> professores = new ArrayList<UsuarioBean>();
        for(int i=0; i<projeto.getEnvolvidos().size();i++){
            if(projeto.getEnvolvidos().get(i).getTipoUsuario().equalsIgnoreCase("professor"))
                professores.add(projeto.getEnvolvidos().get(i));
        }
        return professores;
    }
    
    @Override
    public ArrayList<UsuarioBean> listarEnvolvidosNoProjeto(String nomeProjeto) {
        PapelDAO pad = DAOFactory.createPapelDAO();
        ArrayList<PapelBean> papeisProjeto = pad.listarPapeis(nomeProjeto);
        ArrayList<UsuarioBean> envolvidosProjeto = new ArrayList<UsuarioBean>();
        for(int i=0; i<papeisProjeto.size();i++){
            ArrayList<UsuarioBean> envolvidosPapel = papeisProjeto.get(i).getEnvolvidos();
            for(int j=0; j<envolvidosPapel.size();j++)
                envolvidosProjeto.add(envolvidosPapel.get(j));
        }
        return envolvidosProjeto;
    }
    
    @Override
    public ArrayList<String> retornaProjetosUsuario(String nomeUsuario){
            ArrayList<String> projetosUsuario = new ArrayList<String>();
            ArrayList<String> projetosExistentes = this.listarNomesProjetosExistentes();
            
            for(int i=0;i<projetosExistentes.size();i++){
                if(this.usuarioEstaEmProjeto(nomeUsuario, projetosExistentes.get(i))){
                    projetosUsuario.add(projetosExistentes.get(i));
                }
            }
            return projetosUsuario;
    }
    
    @Override
    public String retornaPapelUsuarioEmProjeto(UsuarioBean usuario, int idProjeto){
        
        ArrayList<PapelBean> papeisProjeto = this.retornaPapeisProjeto(idProjeto);
        
        for(int i=0; i<papeisProjeto.size();i++){
            ArrayList<UsuarioBean> envolvidosPapel = papeisProjeto.get(i).getEnvolvidos();
            for(int j=0; j<envolvidosPapel.size();j++){
                if(envolvidosPapel.get(j).getNome().equalsIgnoreCase(usuario.getNome()))
                    return papeisProjeto.get(i).getFuncao();
            }
        }
        return "";
    }
    
    @Override
    public ArrayList<PapelBean> retornaPapeisProjeto(int idProjeto){
        try{
            
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection(); 
            String SQL = "SELECT funcao FROM papel WHERE idProjeto = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);

            ps.setInt(1, idProjeto);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> papeis = new ArrayList<String>();
            while(rs.next()){
                papeis.add(rs.getString("funcao"));
            }
            ps.close();
            rs.close();
            connection.close();

            PapelDAO papd=DAOFactory.createPapelDAO();
            ArrayList<PapelBean> papeisProjeto = new ArrayList<PapelBean>();
            for(int i=0;i<papeis.size();i++)
                papeisProjeto.add(papd.buscar(papeis.get(i), 1));
            
            return papeisProjeto;
        
        } catch (SQLException ex) {
            return null;
        }
    }
    
    
     
    @Override
    public GrupoBean retornaGrupoUsuario(String nomeUsuario, String nomeProjeto){
            ProjetoBean projeto = this.pesquisarProjeto(nomeProjeto,1);
            ArrayList<GrupoBean> gruposProjeto = projeto.getGrupos();
            GrupoDAO gd = DAOFactory.createGrupoDAO();
            
            for(int i=0;i<gruposProjeto.size();i++){
                if(gd.usuarioEstaEmGrupo(nomeUsuario, gruposProjeto.get(i).getNome(),nomeProjeto)){
                    return gruposProjeto.get(i);
                }
            }
            return null;
    }
    
    @Override
    public ArrayList<UsuarioBean> retornaUsuariosSistemaSemPapel(){
        try{
            ArrayList<UsuarioBean> usuariosSemPapel = new ArrayList<UsuarioBean>();
            
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            String SQL = "SELECT * FROM usuario u LEFT JOIN usuarioxpapel up ON u.id = up.idUsuario WHERE idPapel is null";
            PreparedStatement ps= connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<String> usuariosSemPapelNome = new ArrayList<String>();
            while(rs.next()){
                usuariosSemPapelNome.add(rs.getString("nome"));
            }
            ps.close();
            rs.close();
            connection.close();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            for(int i=0; i<usuariosSemPapelNome.size();i++){
                UsuarioBean usuario = ud.buscar(usuariosSemPapelNome.get(i), 1);
                usuariosSemPapel.add(usuario);
            }
            
            return usuariosSemPapel;
            
            } catch (SQLException ex) {
            Logger.getLogger(JDBCProjetoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Erro ao buscar um registro de projeto pelo nome", ex);
        }
    }
    @Override
    public String trocaData (String dataAnoMesDia){
        String dataDiaMesAno = dataAnoMesDia.substring(8, 10)+"-"+dataAnoMesDia.substring(5, 7)+"-"+dataAnoMesDia.substring(0, 4);
        return dataDiaMesAno;
    }
    
    
    @Override
    public ArrayList<String> listarNomesProjetosExistentes() {
        ArrayList<ProjetoBean> projetos = new ArrayList<ProjetoBean>();
        try {
            String SQL = "SELECT * FROM projeto";
            if(connection==null || connection.isClosed())
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<String> projetosNome = new ArrayList<String>();
            while(rs.next()){
                projetosNome.add(rs.getString("nome"));
            }
            
            ps.close();
            rs.close();
            connection.close();
            
            return projetosNome;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCProjetoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException ("Falha ao listar projetos em JDBCProjetoDAO", ex);
        }
    }
    
    @Override
    public UsuarioBean retornaUsuarioRanking(ProjetoBean projeto){
        AtividadeDAO atvd = DAOFactory.createAtividadeDAO();
        ArrayList<Integer> numAtividadesConcluidas = new ArrayList<Integer>();
       
        
        for(int k=0;k<projeto.getEnvolvidos().size();k++)
            numAtividadesConcluidas.add(0);
            
        int quantAtividadesConcluidas = 0;
        for(int i=0;i<projeto.getEnvolvidos().size();i++){
            ArrayList<AtividadeBean> atividadesAluno = atvd.retornarAtividadesEnvolvido(projeto.getEnvolvidos().get(i), projeto);
            for(int j=0; j<atividadesAluno.size();j++){
                if(atividadesAluno.get(j).getStatus().equalsIgnoreCase("Concluida"))
                    quantAtividadesConcluidas++;
            }
            numAtividadesConcluidas.set(i, quantAtividadesConcluidas);
            quantAtividadesConcluidas = 0;
        }
        
        if(numAtividadesConcluidas.size()==0)
            return null;
        
        UsuarioBean usuarioRanking = projeto.getEnvolvidos().get(0);
        for(int i=1; i<numAtividadesConcluidas.size();i++)
            if(numAtividadesConcluidas.get(i)>numAtividadesConcluidas.get(i-1))
                usuarioRanking = projeto.getEnvolvidos().get(i);
        
        return usuarioRanking;
    }
    
    @Override
    public ArrayList<UsuarioBean> retornaEnvolvidosProjeto(ProjetoBean projeto){
        ArrayList<UsuarioBean> envolvidosNoProjeto = new ArrayList<UsuarioBean>();
        
        for(int i=0;i<projeto.getPapeis().size();i++)
            for(int j=0; j<projeto.getPapeis().get(i).getEnvolvidos().size();j++)
                envolvidosNoProjeto.add(projeto.getPapeis().get(i).getEnvolvidos().get(j));
        
        return envolvidosNoProjeto;
    }
    
    @Override
    /* Informacao:
        1- ranking por atividades feitas
        2- ranking por tarefas feitas
        3- ranking por tópicos criados
        4- ranking por comentários enviados
        5- ranking por mensagens trocadas
        6- ranking por alertas enviados
    */
    public String retornaTabelaRankingPorGrupo(ProjetoBean projeto, int informacao){
        
        String tabelaRankingString = "";
        
        AtividadeDAO ad=DAOFactory.createAtividadeDAO();
        if(informacao == 1){
            
            for(int i=0;i<projeto.getGrupos().size();i++){
                projeto.getGrupos().get(i).setNumAtividadesFeitas(0);
                int quantAtividadesConcluidas=0;
                for(int j=0;j<projeto.getGrupos().get(i).getIntegrantes().size();j++){
                    ArrayList<AtividadeBean> atividadesAluno = ad.retornarAtividadesEnvolvido(projeto.getGrupos().get(i).getIntegrantes().get(j), projeto);
                    for(int k=0; k<atividadesAluno.size();k++){
                        if(atividadesAluno.get(k).getStatus().equalsIgnoreCase("Concluida"))
                            quantAtividadesConcluidas++;
                    }
                }
                projeto.getGrupos().get(i).setNumAtividadesFeitas(quantAtividadesConcluidas);
            }
            
            ArrayList<GrupoBean> gruposOrganizadosPorRanking = projeto.getGrupos();
            for (int i = gruposOrganizadosPorRanking.size(); i >= 1; i--) {
                for (int j = 1; j < i; j++) {
                    if (gruposOrganizadosPorRanking.get(j - 1).getNumAtividadesFeitas() < gruposOrganizadosPorRanking.get(j).getNumAtividadesFeitas()) {
                        GrupoBean aux = gruposOrganizadosPorRanking.get(j);
                        gruposOrganizadosPorRanking.set(j, gruposOrganizadosPorRanking.get(j - 1));
                        gruposOrganizadosPorRanking.set(j - 1,aux);
                    }
                }
            }
            
            
            tabelaRankingString = "<table><tr><td><table id=\"example\"><thead><tr><th onClick=\"showHide(this)\">Ranking&nbsp;(&nbsp;Nome&nbsp;/&nbsp;Integrantes&nbsp;/&nbsp;Atividades feitas&nbsp;)</th></tr></thead><tbody>"
                                + "<tr><td><table id=\"example\"><thead><tr><th onClick=\"showHide(this)\">Grupos</th></tr></thead><tbody><tr><td>";

        
            for(int i=0; i<gruposOrganizadosPorRanking.size();i++){
                tabelaRankingString = tabelaRankingString + "<table id=\"example\"><thead><tr><th onClick=\"showHide(this)\" style=\"width:60%;\"><strong style=\"font-size:18px;\">"+(i+1)+" - </strong>"+gruposOrganizadosPorRanking.get(i).getNome()+"</th><th onClick=\"showHide(this)\" style=\"width:20%;\">Num integrantes: "+gruposOrganizadosPorRanking.get(i).getIntegrantes().size()+"</th><th onClick=\"showHide(this)\" style=\"width:20%;\">Num atv feitas: "+gruposOrganizadosPorRanking.get(i).getNumAtividadesFeitas()+"</th>"
                                                    + "</tr></thead><tbody>";
                
                ArrayList<UsuarioBean> usuariosPorRanking = this.retornaListaUsuarioRanking(gruposOrganizadosPorRanking.get(i),projeto,1);
                for(int j=0; j<usuariosPorRanking.size();j++){
                    
                    if(usuariosPorRanking.get(j).getTipoUsuario().equalsIgnoreCase("aluno"))
                        tabelaRankingString = tabelaRankingString + "<tr><td style=\"width:60%;\"><a href=\"/ProjetoColabUFV/ServletCarregaRelatorioAluno?alunoParaCarregar="+usuariosPorRanking.get(j).getNome()+"\">"+usuariosPorRanking.get(j).getNome()+"</a></td>"
                            + "<td style=\"width:20%;\"><a href=\"/ProjetoColabUFV/ServletCarregaRelatorioAluno?alunoParaCarregar="+usuariosPorRanking.get(j).getNome()+"\">"+usuariosPorRanking.get(j).getTipoUsuario()+"</a></td><td style=\"width:20%;\"><a href=\"/ProjetoColabUFV/ServletCarregaRelatorioAluno?alunoParaCarregar="+usuariosPorRanking.get(j).getNome()+"\">Num atv feitas: "+usuariosPorRanking.get(j).getNumAtividadesFeitas()+"</a></td></tr>";
                    else
                        tabelaRankingString = tabelaRankingString + "<tr><td style=\"width:60%;\">"+usuariosPorRanking.get(j).getNome()+"</td>"
                            + "<td style=\"width:20%;\">"+usuariosPorRanking.get(j).getTipoUsuario()+"</td><td style=\"width:20%;\">Num atv feitas: "+usuariosPorRanking.get(j).getNumAtividadesFeitas()+"</td></tr>";
                    
                }											
                tabelaRankingString = tabelaRankingString + "</tbody></table>";
            }

            tabelaRankingString = tabelaRankingString + "</td></tr></tbody></table></td>/<tr></tbody></table></td></tr></table>";
            
            /*
            tabelaRankingString = "<table id=\"gruposTabela\" class=\"display\" cellspacing=\"0\" width=\"100%\"><thead><tr><th>Nome</th><th>Integrantes</th><th>Atv feitas</th>"
                         +"</tr></thead><tbody>";
            for(int i=0;i<gruposOrganizadosPorRanking.size();i++){
                tabelaRankingString = tabelaRankingString + "<tr><td style=\"width:60%;\"><a href=\"/ProjetoColabUFV/ServletCarregaRelatorioGeral?grupoParaCarregar="+gruposOrganizadosPorRanking.get(i).getNome()+"&ranking=Atividades feitas por aluno\"> "+gruposOrganizadosPorRanking.get(i).getNome()+" </a></td>"
                            +  "<td style=\"width:20%;\">"+gruposOrganizadosPorRanking.get(i).getIntegrantes().size()+"</td><td style=\"width:20%;\">"+gruposOrganizadosPorRanking.get(i).getNumAtividadesFeitas()+"</td>";
                
                
            }
            tabelaRankingString = tabelaRankingString + "</tbody></table>";*/
            
            return tabelaRankingString;
                
        }else if(informacao == 2){
            
        }else if(informacao == 3){
            
        }else if(informacao == 4){
            
        }else if(informacao == 5){
            
        }else if(informacao == 6){
            
        }
        
        return tabelaRankingString;
        
    }
    
    @Override
    /* Informacao:
        1- ranking por atividades feitas
        2- ranking por tarefas feitas
        3- ranking por tópicos criados
        4- ranking por comentários enviados
        5- ranking por mensagens trocadas
        6- ranking por alertas enviados
    */
    public ArrayList<UsuarioBean> retornaListaUsuarioRanking(GrupoBean grupo,  ProjetoBean projeto, int informacao){
        
        AtividadeDAO ad=DAOFactory.createAtividadeDAO();
        GrupoDAO gd=DAOFactory.createGrupoDAO();
        
        ArrayList<UsuarioBean> envolvidosGrupo = gd.listarIntegrantes(grupo.getId());
        
        
        if(informacao == 1){
            
            for(int i=0;i<envolvidosGrupo.size();i++){
                int quantAtividadesConcluidas=0;
                ArrayList<AtividadeBean> atividadesAluno = ad.retornarAtividadesEnvolvido(envolvidosGrupo.get(i), projeto);
                for(int j=0; j<atividadesAluno.size();j++){
                    if(atividadesAluno.get(j).getStatus().equalsIgnoreCase("Concluida")){
                        quantAtividadesConcluidas++;
                    }
                }
                
                envolvidosGrupo.get(i).setNumAtividadesFeitas(quantAtividadesConcluidas);
            }
            
            ArrayList<UsuarioBean> alunosOrganizadosPorRanking = envolvidosGrupo;
            for (int i = alunosOrganizadosPorRanking.size(); i >= 1; i--) {
                for (int j = 1; j < i; j++) {
                    if (alunosOrganizadosPorRanking.get(j - 1).getNumAtividadesFeitas() < alunosOrganizadosPorRanking.get(j).getNumAtividadesFeitas()) {
                        UsuarioBean aux = alunosOrganizadosPorRanking.get(j);
                        alunosOrganizadosPorRanking.set(j, alunosOrganizadosPorRanking.get(j - 1));
                        alunosOrganizadosPorRanking.set(j - 1,aux);
                    }
                }
            }
            
            return alunosOrganizadosPorRanking;
                
        }else if(informacao == 2){
            
        }else if(informacao == 3){
            
        }else if(informacao == 4){
            
        }else if(informacao == 5){
            
        }else if(informacao == 6){
            
        }
        
        return null;
        
    }
    
}