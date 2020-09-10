package br.com.senacrs.servlet;

import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.GrupoDAO;
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

public class ServletCarregaEditaGrupo extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String idGrupoEscolhido = (String) request.getParameter("idGrupoEscolhido"); 
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            sessao.setAttribute("idGrupoEscolhido", idGrupoEscolhido);
            
            GrupoDAO gd=DAOFactory.createGrupoDAO();
            GrupoBean grupo = gd.pesquisarGrupo(idGrupoEscolhido, nomeProjetoEscolhido,2);
            
            String grupoStringTabela = gd.retornarGrupoTabelaEditavel(grupo, nomeProjetoEscolhido, (String)sessao.getAttribute("redirect"));
            sessao.setAttribute("informacoesGrupo", grupoStringTabela);
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesGrupo.jsp");
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
