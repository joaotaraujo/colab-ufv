package br.com.senacrs.servlet;

import br.com.senacrs.bean.TopicoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.TopicoDAO;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletDeletaTopico extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            
            TopicoDAO topd = DAOFactory.createTopicoDAO();
            TopicoBean topicoParaDeletar = topd.buscar((String) request.getParameter("topicoEscolhido"),1);
            
            topd.deletarTopico(topicoParaDeletar.getId());
            
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            ProjetoDAO projd=DAOFactory.createProjetoDAO();
            ArrayList<TopicoBean> topicos =  projd.pesquisarProjeto(nomeProjetoEscolhido, 1).getForum().getTopicos(); 
            
            String topicosStringTabela = topd.retornaTopicosTabela(topicos, (String)sessao.getAttribute("nomeUsuario"));
            sessao.setAttribute("topicos", topicosStringTabela);
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarForum.jsp");
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
