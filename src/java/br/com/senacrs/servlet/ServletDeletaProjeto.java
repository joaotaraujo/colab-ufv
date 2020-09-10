package br.com.senacrs.servlet;

import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.UsuarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletDeletaProjeto extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            String login = (String) sessao.getAttribute("login"); 
            response.setContentType("text/html");
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            UsuarioBean usuarioLogado = ud.buscar(login, 3);
            
            
            ProjetoDAO pd =DAOFactory.createProjetoDAO();
            ProjetoBean projeto = pd.pesquisarProjeto((String) sessao.getAttribute("projetoEscolhido"),1);
            if(usuarioLogado.getId()==projeto.getIdAutor()){
            
                pd.deletarProjeto(projeto.getId());
            
            
                UsuarioBean autor = ud.buscar(login,3);
                ArrayList<String> projetosAutor = pd.retornaProjetosUsuario(autor.getNome());
                String projetosAutorString = "";
                for(int i =0; i<projetosAutor.size();i++)
                    projetosAutorString = projetosAutorString + "<option>"+projetosAutor.get(i)+"</option>";
            
                sessao.setAttribute("projetosExistentes",projetosAutorString);
            
                RequestDispatcher dis = request.getRequestDispatcher("escolhaProjeto.jsp");
                dis.forward(request, response);
            }
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/paginaInicialProfessor.jsp");
            dis.forward(request, response);
            
            
    }
        
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCarregaProjetosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletDeletaProjeto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCarregaProjetosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletDeletaProjeto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
