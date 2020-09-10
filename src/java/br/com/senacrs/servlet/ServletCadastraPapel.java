package br.com.senacrs.servlet;

import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.PapelBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.GrupoDAO;
import br.com.senacrs.dao.PapelDAO;
import br.com.senacrs.dao.ProjetoDAO;
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


public class ServletCadastraPapel extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
            HttpSession sessao = request.getSession();
            
            int idProjeto = Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido"));
             
            PapelDAO pd=DAOFactory.createPapelDAO();
            PapelBean papelExiste = null;
            
            papelExiste = pd.buscar(request.getParameter("funcao"), 1);
            if(papelExiste!=null){
                request.setAttribute("mensagemCadastraPapel","Funcao já existente!");
            }  
            else{
                PapelBean papel = new PapelBean();
                papel.setFuncao(request.getParameter("funcao"));
                papel.setDescricao(request.getParameter("descricao"));
                pd.cadastrar(papel, idProjeto);
                request.setAttribute("mensagemCadastraPapel","Cadastrado realizado com sucesso!");
            }
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroPapel.jsp");
            dis.forward(request, response);
            
        
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
