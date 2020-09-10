package br.com.senacrs.servlet;

import br.com.senacrs.bean.PerguntaFrequenteBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.PerguntaFrequenteDAO;
import br.com.senacrs.dao.ProjetoDAO;
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

public class ServletEditaPerguntaFrequente extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            String idPergunta = (String) sessao.getAttribute("idPerguntaFrequenteEscolhida");
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido,1);
            
            PerguntaFrequenteDAO pd=DAOFactory.createPerguntaFrequenteDAO();
            
            PerguntaFrequenteBean perguntaFrequenteEditada = new PerguntaFrequenteBean();
            perguntaFrequenteEditada.setPergunta(request.getParameter("perguntaEditada"));
            perguntaFrequenteEditada.setResposta(request.getParameter("respostaEditada"));
            perguntaFrequenteEditada.setId(Integer.parseInt(idPergunta));
            perguntaFrequenteEditada.setIdProjeto(projeto.getId());
            
            pd.editar(perguntaFrequenteEditada);
            
            
            
            
            String perguntaFrequenteStringTabela = pd.retornarInformacaoPeguntaFrequenteTabela(perguntaFrequenteEditada);
            sessao.setAttribute("informacoesPerguntaFrequente", perguntaFrequenteStringTabela);
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesPerguntaFrequente.jsp");
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
