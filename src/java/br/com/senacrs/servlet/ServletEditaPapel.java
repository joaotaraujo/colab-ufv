package br.com.senacrs.servlet;

import br.com.senacrs.bean.PapelBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.PapelDAO;
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

public class ServletEditaPapel extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String idPapelEscolhido =(String) sessao.getAttribute("idPapelEscolhido"); 
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            PapelDAO pad=DAOFactory.createPapelDAO();
            PapelBean papel = pad.buscar(idPapelEscolhido, 2);
            PapelBean papelEditado = new PapelBean();
            
            papelEditado.setFuncao(request.getParameter("funcaoEditada"));
            papelEditado.setDescricao(request.getParameter("descricaoEditada"));
            papelEditado.setId(papel.getId());
            papelEditado.setIdProjeto(papel.getIdProjeto());
            
            pad.editar(papelEditado);
            
            String papelStringTabela = pad.retornarPapelTabela(papelEditado, nomeProjetoEscolhido);
            sessao.setAttribute("informacoesPapel", papelStringTabela);
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesPapel.jsp");
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
