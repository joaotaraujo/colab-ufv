package br.com.senacrs.servlet;

import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.UsuarioDAO;
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

public class ServletEditaUsuario extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html;charset=UTF-8");
            String idUsuarioEscolhido =(String) sessao.getAttribute("idUsuarioEscolhido");
            
            UsuarioDAO ud =DAOFactory.createUsuarioDAO();
                
            UsuarioBean usuario =  ud.buscar(idUsuarioEscolhido, 5); 
            UsuarioBean usuarioEditado = new UsuarioBean();
            usuarioEditado.setNome(request.getParameter("nome"));
            usuarioEditado.setEmail(request.getParameter("email"));
            usuarioEditado.setMatricula(request.getParameter("matricula"));
            usuarioEditado.setLogin(request.getParameter("login"));
            usuarioEditado.setSenha(request.getParameter("senha"));
            usuarioEditado.setTipoUsuario(usuario.getTipoUsuario());
            usuarioEditado.setId(usuario.getId());

            ud.editar(usuarioEditado);
            
            ProjetoDAO pd = DAOFactory.createProjetoDAO();
            ProjetoBean projeto = pd.pesquisarProjeto((String) sessao.getAttribute("idProjetoEscolhido"), 2);
            
            String alunosStringTabela = ud.retornaTabelaDeEnvolvidos(pd.retornaEnvolvidosProjeto(projeto), Integer.parseInt((String) sessao.getAttribute("idProjetoEscolhido")), 5);
            sessao.setAttribute("alunos", alunosStringTabela);
            
           
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesProjeto.jsp");
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
