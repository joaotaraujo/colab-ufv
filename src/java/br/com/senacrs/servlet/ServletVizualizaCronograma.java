package br.com.senacrs.servlet;

import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.TarefaDAO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletVizualizaCronograma extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession sessao = request.getSession();
        String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
        
        ProjetoDAO projd = DAOFactory.createProjetoDAO();
        ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido, 1);
        
        
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
        
        RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarCronogramaProjeto.jsp");
        dis.forward(request, response);
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
