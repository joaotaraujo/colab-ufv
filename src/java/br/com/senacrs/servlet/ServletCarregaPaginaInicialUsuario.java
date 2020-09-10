package br.com.senacrs.servlet;

import br.com.senacrs.bean.AlertaBean;
import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.PerguntaFrequenteBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.TopicoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AlertaDAO;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.PerguntaFrequenteDAO;
import br.com.senacrs.dao.UsuarioDAO;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.TarefaDAO;
import br.com.senacrs.dao.TopicoDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletCarregaPaginaInicialUsuario extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
              
            
            response.setContentType("text/html");
            
            ProjetoDAO pd=DAOFactory.createProjetoDAO();
            
            ProjetoBean projeto =  pd.pesquisarProjeto((String) sessao.getAttribute("projetoEscolhido"),1); 
            String login = (String) sessao.getAttribute("login");  
            
            UsuarioDAO ud =DAOFactory.createUsuarioDAO();
            UsuarioBean usuarioLogado = ud.buscar(login, 3);
            
            if(usuarioLogado.getTipoUsuario().equalsIgnoreCase("aluno")){
                
                AtividadeDAO atvd = DAOFactory.createAtividadeDAO();
                String cronogramaAtividades = this.montaCronograma(projeto.getNome(), atvd.retornarAtividadesEnvolvido(usuarioLogado, projeto));
                sessao.setAttribute("cronogramaAluno",cronogramaAtividades);
                
                AlertaDAO alertd = DAOFactory.createAlertaDAO();
                String alertasAlunoString = this.montaAlertasAluno(alertd.listarAlertasRecebidos(projeto.getId(), usuarioLogado.getId()));
                sessao.setAttribute("alertasAluno", alertasAlunoString);
                
                TopicoDAO topd=DAOFactory.createTopicoDAO();
                String topicoForumString = this.montaUltimoTopicoForum(projeto.getForum().getTopicos());
                sessao.setAttribute("ultimoTopicoForum", topicoForumString);
                
                PerguntaFrequenteDAO pergd = DAOFactory.createPerguntaFrequenteDAO();
                String perguntasFrequentesString = this.montaUltimasPerguntasFrequentes(pergd.listarPerguntasFrequentes(projeto.getNome()));
                sessao.setAttribute("ultimasPerguntasFrequentes",perguntasFrequentesString);
                
                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/PaginaInicialAluno.jsp");
                dis.forward(request, response);
            }
            else{
                
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

                String cronogramaTabela = "<table><tr><td><table id=\"example\"><thead><tr><th onClick=\"showHide(this)\">Cronograma&nbsp;(&nbsp;Nome&nbsp;/&nbsp;Data de Início&nbsp;/&nbsp;Data de Término&nbsp;/&nbsp;Status&nbsp;/&nbsp;Antecessora&nbsp;)</th></tr></thead><tbody>"
                                        + "<tr><td><table id=\"example\"><thead><tr><th onClick=\"showHide(this)\">Atividades</th></tr></thead><tbody><tr><td>";

                AtividadeDAO atvd = DAOFactory.createAtividadeDAO();
                TarefaDAO td = DAOFactory.createTarefaDAO();
                projeto.setAtividades(atvd.ordenaAtividades(projeto.getAtividades()));

            

                for(int i=0; i<projeto.getAtividades().size();i++){
                    projeto.getAtividades().get(i).setTarefas(td.ordenaTarefas(projeto.getAtividades().get(i).getTarefas()));
                    cronogramaTabela = cronogramaTabela + "<table id=\"example\"><thead><tr><th onClick=\"showHide(this)\" style=\"width:35%;\">"+projeto.getAtividades().get(i).getNome()+"</th><th onClick=\"showHide(this)\" style=\"width:10%;\">"+formato.format(projeto.getAtividades().get(i).getDataInicio())+"</th><th onClick=\"showHide(this)\" style=\"width:10%;\">"+formato.format(projeto.getAtividades().get(i).getDataTermino())+"</th>";

                    //define a cor da atividade de acordo com o status
                    if(projeto.getAtividades().get(i).getStatus().equalsIgnoreCase("Em andamento")){
                            cronogramaTabela = cronogramaTabela + "<th style=\"color: #2222DA;width:15%;\" onClick=\"showHide(this)\">"; 
                    }else if(projeto.getAtividades().get(i).getStatus().equalsIgnoreCase("Reaberta")){
                        cronogramaTabela = cronogramaTabela + "<th style=\"color: #616000;width:15%;\" onClick=\"showHide(this)\">";
                    }else if(projeto.getAtividades().get(i).getStatus().equalsIgnoreCase("Nova Atividade")){
                        cronogramaTabela = cronogramaTabela + "<th style=\"color: #FFFFFF;width:15%;\" onClick=\"showHide(this)\">";
                    }else if(projeto.getAtividades().get(i).getStatus().equalsIgnoreCase("Concluida")){
                        cronogramaTabela = cronogramaTabela + "<th style=\"color: #006106;width:15%;\" onClick=\"showHide(this)\">";
                    }else if(projeto.getAtividades().get(i).getStatus().equalsIgnoreCase("Cancelada")){
                        cronogramaTabela = cronogramaTabela + "<th style=\"color: #5D5D5D;width:15%;\" onClick=\"showHide(this)\">";
                    }else
                        cronogramaTabela = cronogramaTabela + "<th style=\"color: #E80000;width:15%;\" onClick=\"showHide(this)\">"; 



                    String nomeAtividadeAnterior = "";
                    if(projeto.getAtividades().get(i).getIdAtividadeAnterior()==-1)
                        nomeAtividadeAnterior = "nenhuma";
                    else
                        nomeAtividadeAnterior = atvd.buscar(Integer.toString(projeto.getAtividades().get(i).getIdAtividadeAnterior()),2).getNome();


                    cronogramaTabela=cronogramaTabela +projeto.getAtividades().get(i).getStatus()+"</th><th onClick=\"showHide(this)\" style=\"width:30%;\">"+nomeAtividadeAnterior+"</th></tr></thead><tbody>";
                    for(int j=0; j<projeto.getAtividades().get(i).getTarefas().size();j++){
                        cronogramaTabela = cronogramaTabela + "<tr><td><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+projeto.getAtividades().get(i).getTarefas().get(j).getNome()+"\">"+projeto.getAtividades().get(i).getTarefas().get(j).getNome()+"</a></td><td ><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+projeto.getAtividades().get(i).getTarefas().get(j).getNome()+"\">"+formato.format(projeto.getAtividades().get(i).getTarefas().get(j).getDataInicio())+"</a></td><td><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+projeto.getAtividades().get(i).getTarefas().get(j).getNome()+"\">"+formato.format(projeto.getAtividades().get(i).getTarefas().get(j).getDataTermino())+"</a></td><td>";

                        //define a cor da atividade de acordo com o status
                        if(projeto.getAtividades().get(i).getTarefas().get(j).getStatus().equalsIgnoreCase("Em andamento")){
                            cronogramaTabela = cronogramaTabela + "<a style=\"color: #2222DA;\""; 
                        }else if(projeto.getAtividades().get(i).getStatus().equalsIgnoreCase("Reaberta")){
                            cronogramaTabela = cronogramaTabela + "<a style=\"color: #616000;\""; 
                        }else if(projeto.getAtividades().get(i).getTarefas().get(j).getStatus().equalsIgnoreCase("Nova Tarefa")){
                            cronogramaTabela = cronogramaTabela + "<a style=\"color: #FFFFFF;\"";
                        }else if(projeto.getAtividades().get(i).getTarefas().get(j).getStatus().equalsIgnoreCase("Concluida")){
                            cronogramaTabela = cronogramaTabela + "<a style=\"color: #006106;\"";
                        }else if(projeto.getAtividades().get(i).getTarefas().get(j).getStatus().equalsIgnoreCase("Cancelada")){
                            cronogramaTabela = cronogramaTabela + "<a style=\"color: #5D5D5D;\"";
                        }else
                            cronogramaTabela = cronogramaTabela + "<a style=\"color: #E80000;\""; 

                        String nomeTarefaAnterior = "";
                        if(projeto.getAtividades().get(i).getTarefas().get(j).getIdTarefaAnterior()==-1)
                            nomeTarefaAnterior = "nenhuma";
                        else
                            nomeTarefaAnterior = td.buscar(Integer.toString(projeto.getAtividades().get(i).getTarefas().get(j).getIdTarefaAnterior()),2).getNome();


                        sessao.setAttribute("idChatProjeto", Integer.toString(projeto.getChat().getId()));    
                        cronogramaTabela = cronogramaTabela+" href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+projeto.getAtividades().get(i).getTarefas().get(j).getNome()+"\">"+projeto.getAtividades().get(i).getTarefas().get(j).getStatus()+"</a></td><td ><a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+projeto.getAtividades().get(i).getTarefas().get(j).getNome()+"\">"+nomeTarefaAnterior+"</a></td></tr>";
                    }											
                    cronogramaTabela = cronogramaTabela + "</tbody></table>";
                }

                cronogramaTabela = cronogramaTabela + "</td></tr></tbody></table></td>/<tr></tbody></table></td></tr></table>";

                sessao.setAttribute("cronogramaTabela", cronogramaTabela);
                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/PaginaInicialProfessor.jsp");
                dis.forward(request, response);
            }
    }
    
    public String montaCronograma(String nomeProjeto, ArrayList<AtividadeBean> atividades){
        
        SimpleDateFormat formato= new SimpleDateFormat("dd-MM-yyyy");
        String cronogramaTabela = "<div class=\"container\" bgcolor=\"FF0000\">";
        AtividadeDAO atvd = DAOFactory.createAtividadeDAO();
        TarefaDAO td = DAOFactory.createTarefaDAO();
        atividades = atvd.ordenaAtividades(atividades);
        for(int i=0; i<atividades.size();i++){
            atividades.get(i).setTarefas(td.ordenaTarefas(atividades.get(i).getTarefas()));
            cronogramaTabela = cronogramaTabela + "<div class=\"containerMaior\" bgcolor=\"FF0000\" ><a  href=\"/ProjetoColabUFV/ServletCarregaInformacoesAtividade?atividadeParaCarregar="+atividades.get(i).getNome()+"\">&nbsp;&nbsp;"+atividades.get(i).getNome()+"&nbsp;&nbsp;<p>&nbsp;Data Inicial:&nbsp; "+formato.format(atividades.get(i).getDataInicio())+"<p>&nbsp;Data Final:&nbsp; "+formato.format(atividades.get(i).getDataTermino())+"<p ";
                    
            //define a cor da atividade de acordo com o status
            if(atividades.get(i).getStatus().equalsIgnoreCase("Em andamento")){
                cronogramaTabela = cronogramaTabela + "style=\"color: #2222DA;\""; 
            }else if(atividades.get(i).getStatus().equalsIgnoreCase("Reaberta")){
                cronogramaTabela = cronogramaTabela + "style=\"color: #616000;\""; 
            }else if(atividades.get(i).getStatus().equalsIgnoreCase("Nova Atividade")){
                cronogramaTabela = cronogramaTabela + "style=\"color: #FFFFFF;\"";
            }else if(atividades.get(i).getStatus().equalsIgnoreCase("Concluida")){
                cronogramaTabela = cronogramaTabela + "style=\"color: #006106;\"";
            }else if(atividades.get(i).getStatus().equalsIgnoreCase("Cancelada")){
                cronogramaTabela = cronogramaTabela + "style=\"color: #5D5D5D;\"";
            }else
                cronogramaTabela = cronogramaTabela + "style=\"color: #E80000;\"";
            
            cronogramaTabela = cronogramaTabela + " >&nbsp;Status: "+atividades.get(i).getStatus()+"<p>&nbsp;</a><p>Tarefas<div class=\"containerMenor\" bgcolor=\"FF0000\">";
            
            for(int j=0; j<atividades.get(i).getTarefas().size();j++){
                cronogramaTabela = cronogramaTabela + "<a href=\"/ProjetoColabUFV/ServletCarregaInformacoesTarefa?tarefaParaCarregar="+atividades.get(i).getTarefas().get(j).getNome()+"\" ";
                
                //define a cor da atividade de acordo com o status
            if(atividades.get(i).getTarefas().get(j).getStatus().equalsIgnoreCase("Em andamento")){
                cronogramaTabela = cronogramaTabela + " style=\"color: #2222DA;font-size:15px;\"";   
            }else if(atividades.get(i).getStatus().equalsIgnoreCase("Reaberta")){
                cronogramaTabela = cronogramaTabela + " style=\"color: #616000;font-size:15px;\""; 
            }else if(atividades.get(i).getTarefas().get(j).getStatus().equalsIgnoreCase("Nova Tarefa")){
                cronogramaTabela = cronogramaTabela + " style=\"color: #FFFFFF;font-size:15px;\"";
            }else if(atividades.get(i).getTarefas().get(j).getStatus().equalsIgnoreCase("Concluida")){
                cronogramaTabela = cronogramaTabela + " style=\"color: #006106;font-size:15px;\"";
            }else if(atividades.get(i).getTarefas().get(j).getStatus().equalsIgnoreCase("Cancelada")){
                cronogramaTabela = cronogramaTabela + " style=\"color: #5D5D5D;font-size:15px;\"";
            }else
                cronogramaTabela = cronogramaTabela + " style=\"color: #E80000;font-size:15px;\"";
                
            cronogramaTabela = cronogramaTabela+ ">"+atividades.get(i).getTarefas().get(j).getNome()+"</a><p>";
            }
            
            if(i!=atividades.size()-1)
                cronogramaTabela = cronogramaTabela + "</div></div> --> &nbsp;&nbsp;";
            else
                cronogramaTabela = cronogramaTabela + "</div></div>&nbsp;&nbsp;";
        }
        
        cronogramaTabela = cronogramaTabela + "</div>";
        
        return cronogramaTabela;
    }
    
    
    
    public String montaAlertasAluno(ArrayList<AlertaBean> alertasAluno){
        
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        
        String alertasAlunoString =  "<a href=\"/ProjetoColabUFV/ServletCarregaAlertas\"><h1>Alertas</h1>"
                +"<p><strong>Lista dos últimos alertas recebidos</strong></p><ul>";
          
        for(int i=0; i<alertasAluno.size();i++){
            if(i<2)
                alertasAlunoString = alertasAlunoString+  "<a href=\"/ProjetoColabUFV/ServletCarregaAlertas\"><li style=\"width: 300px;	height: 45px;	overflow: hidden;\"> Enviado por: "+alertasAluno.get(i).getAutor().getNome()+", em "+ formato.format(alertasAluno.get(i).getDataEmissao())+"<br />"
                    +"<span> "+alertasAluno.get(i).getDescricao()+"</span></li></a>";
        }
                
        alertasAlunoString = alertasAlunoString + "</ul> Mais alertas...";
        
        return alertasAlunoString;
    }

    public String montaUltimoTopicoForum(ArrayList<TopicoBean> topicos){
        
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        
        for (int i = 0; i < topicos.size(); i++){
            for (int j = i + 1; j < topicos.size(); j++){
		if (topicos.get(j - 1).getDataCriacao().compareTo(topicos.get(j).getDataCriacao())<0){
			TopicoBean temp = topicos.get(i); 	
			topicos.set(i, topicos.get(j));
			topicos.set(j, temp);		
		}
            } 
        }
        String ultimoTopicoForum="";
        if(topicos.size()!=0)
            ultimoTopicoForum =  "<a href=\"/ProjetoColabUFV/ServletCarregaForum\"><h1> Fórum - Últimos Posts</h1></a>"
                +"<p> <a href=\"ServletCarregaInformacoesTopico?topicoParaCarregar="+topicos.get(0).getTitulo()+"\"><em> "+formato.format(topicos.get(0).getDataCriacao())+ "</em></a><a href=\"ServletCarregaInformacoesTopico?topicoParaCarregar="+topicos.get(0).getTitulo()+"\"><strong>" +topicos.get(0).getTitulo()+ "</strong></a> <br />"
                +" <a   style=\"font-size:11px; width: 200px; height: 25px; overflow: hidden;\" href=\"ServletCarregaInformacoesTopico?topicoParaCarregar="+topicos.get(0).getTitulo()+"\"><li   style=\"font-size:11px; padding-left:40px; width: 200px; height: 25px; overflow: hidden;\">"+topicos.get(0).getDescricao()+"</li></a>"
                +""              
                +"<a href=\"/ProjetoColabUFV/ServletCarregaForum\" class=\"link\"> Visitar o fórum</a></p> ";
        else
            ultimoTopicoForum=  "<a href=\"/ProjetoColabUFV/ServletCarregaForum\" class=\"link\"> Visitar o fórum</a>   ";
                        
        return ultimoTopicoForum;
    }
    
    public String montaUltimasPerguntasFrequentes(ArrayList<PerguntaFrequenteBean> perguntasFrequentes){
        
        String perguntasFrequentesString = "";
        if(perguntasFrequentes.get(0)!=null)
            perguntasFrequentesString = perguntasFrequentesString +" <a href=\"/ProjetoColabUFV/ServletCarregaInformacoesPerguntaFrequente?perguntaFrequenteParaCarregar="+perguntasFrequentes.get(0).getPergunta()+"\"><h1>Perguntas Frequentes</h1><p><em> *</em><strong> "
                    +perguntasFrequentes.get(0).getPergunta() +"</strong> <br /><li  style=\"font-size:11px; padding-left:40px; width: 200px; height: 27px; overflow: hidden;\">"
                    +perguntasFrequentes.get(0).getResposta()+"</li></a><a href=\"/ProjetoColabUFV/ServletCarregaPerguntasFrequentes\" class=\"link\">Mais perguntas</a></p>";
                        
        return perguntasFrequentesString;
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletLogaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletLogaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletLogaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletLogaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
