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

public class ServletEditaProjeto extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            
            ProjetoDAO pd =DAOFactory.createProjetoDAO();
            ProjetoBean projeto =  pd.pesquisarProjeto((String)sessao.getAttribute("idProjetoEscolhido"),2); 
            
            ProjetoBean projetoEditado = new ProjetoBean();
            projetoEditado.setNome(request.getParameter("nome"));
            projetoEditado.setDescricao(request.getParameter("descricao"));
            
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            Date dataInicio = (Date) formato.parse(request.getParameter("dataInicioEditada"));
            Date dataTermino = (Date) formato.parse(request.getParameter("dataTerminoEditada"));
            projetoEditado.setDataInicio(dataInicio);
            projetoEditado.setDataTermino(dataTermino);
            
            projetoEditado.setId(projeto.getId());
            
            UsuarioDAO profd = DAOFactory.createUsuarioDAO();
            UsuarioBean professor = profd.buscar(request.getParameter("nomeAutor"), 1);
            projetoEditado.setIdAutor(professor.getId());   
            
            pd.editarProjeto(projetoEditado);
            projeto = pd.pesquisarProjeto(projetoEditado.getNome(),1); 
            
            String projetoStringTabela = pd.retornarProjetoTabela(projeto);
            sessao.setAttribute("informacoesProjeto", projetoStringTabela);
            
            request.setAttribute("redirect","1");
            sessao.setAttribute("redirect","1");
            
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
        } catch (SQLException ex) {
            Logger.getLogger(ServletEditaProjeto.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletEditaProjeto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
