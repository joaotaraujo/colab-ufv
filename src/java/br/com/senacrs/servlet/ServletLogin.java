package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.UsuarioDAO;
import br.com.senacrs.dao.ProjetoDAO;
import java.io.IOException;
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


public class ServletLogin extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException{
        HttpSession sessao = request.getSession();
        String login = request.getParameter("login");    
        String senha = request.getParameter("senha");
        
        UsuarioDAO ud = DAOFactory.createUsuarioDAO();
        
       
    
        int usuarioExiste = -1;
        UsuarioBean usuario= new UsuarioBean();
        if(ud.buscar(login,3)!=null){
            usuarioExiste = 1;
            usuario= ud.buscar(login,3);
        }
        ProjetoDAO pd = DAOFactory.createProjetoDAO();
        if(usuario.getTipoUsuario().equalsIgnoreCase("aluno")){
            
        
            if (usuarioExiste == 1) {
                sessao.setAttribute("login", login);
                if(senha.equals(usuario.getSenha())){
                    sessao.setAttribute("tipoUsuario","aluno");
                    sessao.setAttribute("senha",senha);
                    ArrayList<String> projetosAluno = pd.retornaProjetosUsuario(usuario.getNome());
                    String projetosAlunoString = "";
                    for(int i=0; i<projetosAluno.size();i++){
                        projetosAlunoString = projetosAlunoString + "<option>"+projetosAluno.get(i)+"</option>";
                    }
                    
                    sessao.setAttribute("projetosExistentes",projetosAlunoString);
                    response.sendRedirect("escolhaProjeto.jsp");
                }
                else{
                    request.setAttribute("mensagem","Senha Inválida!");
                    RequestDispatcher dis = request.getRequestDispatcher("index.jsp");
                    dis.forward(request, response);
                }
            } else {
                request.setAttribute("mensagem","Usuário Inválido!");
                RequestDispatcher dis = request.getRequestDispatcher("index.jsp");
                dis.forward(request, response);
            }
        }
        
        else{
            
            if (usuarioExiste == 1) {
                sessao.setAttribute("login", login);
                if(senha.equals(usuario.getSenha())){
                    sessao.setAttribute("tipoUsuario","professor");
                    sessao.setAttribute("senha",senha);
                    
                    
                    ArrayList<String> projetosProfessor = pd.retornaProjetosUsuario(usuario.getNome());
                    String projetosProfessorString = "";
                    for(int i=0; i<projetosProfessor.size();i++){
                        projetosProfessorString = projetosProfessorString + "<option>"+projetosProfessor.get(i)+"</option>";
                    }

                    sessao.setAttribute("projetosExistentes",projetosProfessorString);
                    response.sendRedirect("escolhaProjeto.jsp");
                }
                else{
                    sessao.setAttribute("mensagem","Senha Inválida!");
                    RequestDispatcher dis = request.getRequestDispatcher("index.jsp");
                    dis.forward(request, response);
                }
            } else {
                request.setAttribute("mensagem","Usuário Inválido!");
                RequestDispatcher dis = request.getRequestDispatcher("index.jsp");
                dis.forward(request, response);
            }
        }
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
