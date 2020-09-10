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


public class ServletCadastraGrupo extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
            HttpSession sessao = request.getSession();
             String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            GrupoDAO gd=DAOFactory.createGrupoDAO();
            
            GrupoBean grupoExiste = null;
            
            grupoExiste = gd.pesquisarGrupo(request.getParameter("nome"), nomeProjetoEscolhido,1);
            if(!grupoExiste.getNome().equalsIgnoreCase("")){
                request.setAttribute("mensagemCadastraGrupo","Nome já existente!");
                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroGrupo.jsp");
                dis.forward(request, response);
            }  
            else{
                GrupoBean grupo = new GrupoBean();
                grupo.setNome(request.getParameter("nome"));
                grupo.setDescricao(request.getParameter("descricao"));
                gd.cadastrarGrupo(grupo, nomeProjetoEscolhido);
                request.setAttribute("mensagemCadastraGrupo","Cadastrado realizado com sucesso!");
                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroGrupo.jsp");
                dis.forward(request, response);
            }
            
        
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraGrupo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraGrupo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
