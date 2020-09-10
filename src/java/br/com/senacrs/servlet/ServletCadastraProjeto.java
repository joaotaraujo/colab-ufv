package br.com.senacrs.servlet;

import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.UsuarioDAO;
import java.io.IOException;
import java.util.Date;
import java.sql.SQLException;
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


public class ServletCadastraProjeto extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
            HttpSession sessao = request.getSession();
            ProjetoBean projeto = new ProjetoBean();
            ProjetoDAO ad=DAOFactory.createProjetoDAO();
            
            if(ad.pesquisarProjeto(request.getParameter("nome"),1)!=null){
                request.setAttribute("mensagemCadastraProjeto","Nome já existente!");
            }  
            else{
                projeto.setNome(request.getParameter("nome"));
                projeto.setDescricao(request.getParameter("descricao"));
                
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                Date dataInicio = (Date) formato.parse(request.getParameter("dataInicio"));
                Date dataTermino = (Date) formato.parse(request.getParameter("dataTermino"));
                projeto.setDataInicio(dataInicio);
                projeto.setDataTermino(dataTermino);
                
                String nomeAutor = (String) sessao.getAttribute("nomeUsuario");
                UsuarioDAO profd = DAOFactory.createUsuarioDAO();
                UsuarioBean professorAutor = profd.buscar(nomeAutor, 1);
                projeto.setIdAutor(professorAutor.getId());
                    
                ad.cadastrarProjeto(projeto);
                request.setAttribute("mensagemCadastraProjeto","Cadastrado realizado com sucesso!");
            }
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroProjeto.jsp");
            dis.forward(request, response);
            
        
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraProjeto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCadastraProjeto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraProjeto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCadastraProjeto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
