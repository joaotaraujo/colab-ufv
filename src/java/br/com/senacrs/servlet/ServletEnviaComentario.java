package br.com.senacrs.servlet;

import br.com.senacrs.bean.ComentarioBean;
import br.com.senacrs.bean.TopicoBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ComentarioDAO;
import br.com.senacrs.dao.TopicoDAO;
import br.com.senacrs.dao.UsuarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletEnviaComentario extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            String idTopico = (String) sessao.getAttribute("idTopicoEscolhido");
            
            ComentarioBean comentario = new ComentarioBean();
            
            UsuarioDAO ud=DAOFactory.createUsuarioDAO();
            comentario.setAutor(ud.buscar((String) sessao.getAttribute("nomeUsuario"),1));
            comentario.setConteudo(request.getParameter("comentarioTopico"));
            comentario.setIdTopico(Integer.parseInt(idTopico));
            Date data = new Date(System.currentTimeMillis());  
            comentario.setDataComentario(data);
            
            ComentarioDAO cod = DAOFactory.createComentarioDAO();
            cod.comentarTopico(comentario, Integer.parseInt(idTopico));
            TopicoDAO topd=DAOFactory.createTopicoDAO();
            TopicoBean topico = topd.buscar(idTopico,2);
            
            String topicoStringTabela = topd.retornarInformacaoTopicoTabela(topico, (String) sessao.getAttribute("nomeUsuario"));
            sessao.setAttribute("informacoesTopico", topicoStringTabela);
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesTopico.jsp");
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
            Logger.getLogger(ServletCarregaInformacoesPerguntaFrequente.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletCarregaInformacoesPerguntaFrequente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
