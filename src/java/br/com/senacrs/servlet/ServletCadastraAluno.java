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


public class ServletCadastraAluno extends HttpServlet {

   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
            UsuarioBean aluno = new UsuarioBean();
            UsuarioDAO ud =DAOFactory.createUsuarioDAO();
            
            if(ud.buscar(request.getParameter("nome"),1)!=null){
                request.setAttribute("mensagemCadastroAluno","Nome já existente!");
            }
            else if(ud.buscar(request.getParameter("matricula"),2)!=null){
                request.setAttribute("mensagemCadastroAluno","Matricula já cadastrada!");
            }    
            else if(ud.buscar(request.getParameter("login"),3)!=null){
                request.setAttribute("mensagemCadastroAluno","Login já cadastrado!");
            }
            else if(ud.buscar(request.getParameter("email"),4)!=null){
                request.setAttribute("mensagemCadastroAluno","Email já cadastrado!");
            }   
                    
            else{
                aluno.setNome(request.getParameter("nome"));
                aluno.setMatricula(request.getParameter("matricula"));
                aluno.setEmail(request.getParameter("email"));
                aluno.setLogin(request.getParameter("login"));
                aluno.setSenha(request.getParameter("senha"));
                
                ud.cadastrar(aluno,1);
                request.setAttribute("mensagemCadastroAluno","Cadastrado realizado com sucesso!");
            }
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroAluno.jsp");
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
