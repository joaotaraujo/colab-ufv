package br.com.senacrs.servlet;

import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.TopicoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.TopicoDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletEditaTopico extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            String idTopico = (String) sessao.getAttribute("idTopicoEscolhido");
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido,1);
            
            TopicoDAO topd=DAOFactory.createTopicoDAO();
            
            TopicoBean topicoEditado = topd.buscar(idTopico, 2);
            topicoEditado.setTitulo(request.getParameter("tituloEditado"));
            topicoEditado.setDescricao(request.getParameter("descricaoEditada"));
            
            topd.editar(topicoEditado);
            
            
            
            
            String topicoStringTabela = topd.retornarInformacaoTopicoTabela(topicoEditado, (String) sessao.getAttribute("nomeUsuario"));
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
            Logger.getLogger(ServletEditaPerguntaFrequente.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletEditaPerguntaFrequente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
