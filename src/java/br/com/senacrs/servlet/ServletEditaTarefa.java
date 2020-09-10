package br.com.senacrs.servlet;

import br.com.senacrs.bean.TarefaBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.TarefaDAO;
import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletEditaTarefa extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            String login = (String) sessao.getAttribute("login"); 
            response.setContentType("text/html");
            String idTarefaEscolhida = (String) sessao.getAttribute("idTarefaEscolhida");
            
            TarefaDAO td=DAOFactory.createTarefaDAO();
            TarefaBean tarefa = td.buscar(idTarefaEscolhida, 2);
            TarefaBean tarefaEditada = new TarefaBean();
            
            tarefaEditada.setNome(request.getParameter("nomeEditado"));
            
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            Date dataInicio = (Date) formato.parse(request.getParameter("dataInicioEditada"));
            Date dataTermino = (Date) formato.parse(request.getParameter("dataTerminoEditada"));
            tarefaEditada.setDataInicio(dataInicio);
            tarefaEditada.setDataTermino(dataTermino);
            
            tarefaEditada.setDescricao(request.getParameter("descricaoEditada"));
            tarefaEditada.setComplexidade(request.getParameter("complexidadeEditada"));
            tarefaEditada.setPrioridade(Integer.parseInt(request.getParameter("prioridadeEditada")));
            tarefaEditada.setStatus(request.getParameter("statusEditado"));
            
            tarefaEditada.setAtividade(tarefa.getAtividade());
            tarefaEditada.setResponsavel(tarefa.getResponsavel());
            tarefaEditada.setId(tarefa.getId());
            
            String nomeTarefaAnterior = request.getParameter("tarefaAnteriorEditada");
            if(nomeTarefaAnterior.equalsIgnoreCase("nenhuma"))
                tarefaEditada.setIdTarefaAnterior(-1);
            else
                tarefaEditada.setIdTarefaAnterior(td.buscar(nomeTarefaAnterior,1).getId());
            
            td.editar(tarefaEditada);
            
            AtividadeDAO ad = DAOFactory.createAtividadeDAO();
            String atividadeParaCarregar = ad.buscar(Integer.toString(tarefaEditada.getAtividade()), 2).getNome();
            String tarefaStringTabela = td.retornarInformacoesTarefaTabela(tarefaEditada, atividadeParaCarregar);
            sessao.setAttribute("informacoesTarefa", tarefaStringTabela);
            
            
            sessao.setAttribute("tarefaEscolhida", tarefaEditada.getNome());
            sessao.setAttribute("atividadeParaCarregar", atividadeParaCarregar);
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesTarefa.jsp");
            dis.forward(request, response);
            
    }
        
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCarregaProjetosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCarregaProjetosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
