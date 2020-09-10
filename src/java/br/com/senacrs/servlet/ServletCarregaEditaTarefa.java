package br.com.senacrs.servlet;

import br.com.senacrs.bean.TarefaBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.TarefaDAO;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletCarregaEditaTarefa extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String idTarefaEscolhida = (String)request.getParameter("idTarefaEscolhida");
            
            sessao.setAttribute("idTarefaEscolhida", idTarefaEscolhida);
            
            TarefaDAO td;
            td =DAOFactory.createTarefaDAO();
            TarefaBean tarefa = td.buscar(idTarefaEscolhida, 2);
            
            String tarefaStringTabela = td.retornarTarefaTabelaEditavel(tarefa, (String) sessao.getAttribute("projetoEscolhido"));
            sessao.setAttribute("informacoesTarefa", tarefaStringTabela);
            
            
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
