package br.com.senacrs.servlet;

import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.GrupoDAO;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.UsuarioDAO;
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

public class ServletDeletaGrupo extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession sessao = request.getSession();
            
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            GrupoDAO gd=DAOFactory.createGrupoDAO();
            String nomeGrupoEscolhido = (String) request.getParameter("grupoEscolhido"); 
            gd.deletarGrupo(nomeGrupoEscolhido);
            
            
            String gruposStringTabela="";
            if(request.getParameter("veioDeOnde").equalsIgnoreCase("redirectInformacaoProjeto")){
                ProjetoDAO pd =DAOFactory.createProjetoDAO();
                ProjetoBean projeto =  pd.pesquisarProjeto((String) sessao.getAttribute("projetoEscolhido"),1); 
                String projetoStringTabela = pd.retornarProjetoTabela(projeto);
                sessao.setAttribute("informacoesProjeto", projetoStringTabela);

                UsuarioDAO alud = DAOFactory.createUsuarioDAO();
                String alunosStringTabela = alud.retornaTabelaDeEnvolvidos(projeto.getEnvolvidos(), Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),3);
                sessao.setAttribute("envolvidos", alunosStringTabela);
                ArrayList<GrupoBean> grupos =  gd.listarGrupos(nomeProjetoEscolhido); 
                gruposStringTabela = gd.retornaGruposTabela(grupos,2);
                sessao.setAttribute("grupos", gruposStringTabela);

                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesProjeto.jsp");
                dis.forward(request, response);
                
            }else if(request.getParameter("veioDeOnde").equalsIgnoreCase("redirectGrupos")){
                ArrayList<GrupoBean> grupos =  gd.listarGrupos(nomeProjetoEscolhido); 
                gruposStringTabela = gd.retornaGruposTabela(grupos,1);
                sessao.setAttribute("grupos", gruposStringTabela);

                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarGrupos.jsp");
                dis.forward(request, response);
            }
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
