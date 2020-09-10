package br.com.senacrs.servlet;

import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.PapelBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.GrupoDAO;
import br.com.senacrs.dao.PapelDAO;
import br.com.senacrs.dao.ProjetoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletDeletaPapel extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession sessao = request.getSession();
            
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            PapelDAO pad=DAOFactory.createPapelDAO();
            String funcaoPapelEscolhido = (String) request.getParameter("papelEscolhido"); 
            
            ProjetoDAO pd =DAOFactory.createProjetoDAO();
            ProjetoBean projeto =  pd.pesquisarProjeto((String) sessao.getAttribute("projetoEscolhido"),1); 
            
            pad.deletar(pad.buscar(funcaoPapelEscolhido,1).getId());
            
            ArrayList<PapelBean> papeis =  pad.listarPapeis(projeto.getNome()); 
            String papeisStringTabela = pad.retornaTabelaDePapeis(projeto.getNome());
            sessao.setAttribute("papeis", papeisStringTabela);
            
            sessao.setAttribute("redirect", "redirectProjeto");
            GrupoDAO gd = DAOFactory.createGrupoDAO();
            ArrayList<GrupoBean> grupos =  gd.listarGrupos(nomeProjetoEscolhido); 
            String gruposStringTabela = gd.retornaGruposTabela(grupos,2);
            sessao.setAttribute("grupos", gruposStringTabela);
            
            
            String projetoStringTabela = pd.retornarProjetoTabela(projeto);
            sessao.setAttribute("informacoesProjeto", projetoStringTabela);
            
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesProjeto.jsp");
            dis.forward(request, response);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletDeletaGrupo.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletDeletaGrupo.class.getName()).log(Level.SEVERE, null, ex);
        }
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
