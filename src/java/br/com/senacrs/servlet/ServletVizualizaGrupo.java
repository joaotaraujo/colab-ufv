package br.com.senacrs.servlet;

import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.GrupoDAO;
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

public class ServletVizualizaGrupo extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            String login = (String) sessao.getAttribute("login"); 
            response.setContentType("text/html");
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            GrupoDAO gd=DAOFactory.createGrupoDAO();
            ArrayList<GrupoBean> grupos =  gd.listarGrupos(nomeProjetoEscolhido); 
            String gruposStringTabela = gd.retornaGruposTabela(grupos,1);
            sessao.setAttribute("grupos", gruposStringTabela);
            
            sessao.setAttribute("redirect","2");
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesProjeto.jsp");
            dis.forward(request, response);
            
            
    }
        
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletVizualizaGrupo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletVizualizaGrupo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
