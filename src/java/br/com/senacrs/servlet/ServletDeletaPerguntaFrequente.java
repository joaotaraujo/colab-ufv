package br.com.senacrs.servlet;

import br.com.senacrs.bean.PerguntaFrequenteBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.PerguntaFrequenteDAO;
import java.io.IOException;
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

public class ServletDeletaPerguntaFrequente extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            PerguntaFrequenteDAO pd = DAOFactory.createPerguntaFrequenteDAO();
            pd.deletar((String) request.getParameter("perguntaFrequenteEscolhida"));
            
            ArrayList<PerguntaFrequenteBean> perguntasFrequentes =  pd.listarPerguntasFrequentes(nomeProjetoEscolhido); 
            String perguntasFrequentesStringTabela = pd.retornaPerguntasFrequentesTabela(perguntasFrequentes);
            
            sessao.setAttribute("perguntasFrequentes", perguntasFrequentesStringTabela);
            
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarPerguntasFrequentes.jsp");
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
