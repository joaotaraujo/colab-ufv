package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.UsuarioDAO;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ServletCadastraProfessor extends HttpServlet {

   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
            HttpSession sessao = request.getSession();
            UsuarioBean professor = new UsuarioBean();
            UsuarioDAO ud=DAOFactory.createUsuarioDAO();
            
            if(ud.buscar(request.getParameter("nome"),1)!=null){
                request.setAttribute("mensagemCadastraProfessor","Nome já existente!");
            }
            else if(ud.buscar(request.getParameter("matricula"),2)!=null){
                request.setAttribute("mensagemCadastraProfessor","Matricula já cadastrada!");
            }    
            else if(ud.buscar(request.getParameter("login"),3)!=null){
                request.setAttribute("mensagemCadastraProfessor","Login já cadastrado!");
            }
            else if(ud.buscar(request.getParameter("email"),4)!=null){
                request.setAttribute("mensagemCadastraProfessor","Email já cadastrado!");
            }   
                    
            else{
                professor.setNome(request.getParameter("nome"));
                professor.setMatricula(request.getParameter("matricula"));
                professor.setEmail(request.getParameter("email"));
                professor.setLogin(request.getParameter("login"));
                professor.setSenha(request.getParameter("senha"));
                    
                ud.cadastrar(professor,2);
                request.setAttribute("mensagemCadastraProfessor","Cadastrado realizado com sucesso!");
            }
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroProfessor.jsp");
            dis.forward(request, response);
        
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
