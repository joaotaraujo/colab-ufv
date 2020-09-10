package br.com.senacrs.servlet;

import br.com.senacrs.bean.PerguntaFrequenteBean;
import br.com.senacrs.bean.TopicoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.PerguntaFrequenteDAO;
import br.com.senacrs.dao.TopicoDAO;
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

public class ServletCarregaInformacoesTopico extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            String topicoEscolhido = request.getParameter("topicoParaCarregar");
            
            sessao.setAttribute("topicoEscolhido", topicoEscolhido);
            
            
            TopicoDAO topd=DAOFactory.createTopicoDAO();
            TopicoBean topico = topd.buscar(topicoEscolhido,1);
            
            sessao.setAttribute("autorTopico",topico.getAutor().getNome());
            
            sessao.setAttribute("idTopicoEscolhido",Integer.toString(topico.getId()));
            
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
