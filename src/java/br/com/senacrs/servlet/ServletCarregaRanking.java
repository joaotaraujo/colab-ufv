package br.com.senacrs.servlet;

import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.GrupoDAO;
import br.com.senacrs.dao.ProjetoDAO;
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

public class ServletCarregaRanking extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
        
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
        
            ProjetoDAO projd=DAOFactory.createProjetoDAO();
            ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido, 1);
            
            GrupoDAO gd=DAOFactory.createGrupoDAO();
            
            String tipoDeRanking = (String)request.getParameter("ranking");
            
            if(tipoDeRanking.equalsIgnoreCase("Atividades feitas"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,1));
            else if(tipoDeRanking.equalsIgnoreCase("Tarefas feitas"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,2));
            else if(tipoDeRanking.equalsIgnoreCase("Tópicos criados"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,3));
            else if(tipoDeRanking.equalsIgnoreCase("Comentários enviados"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,4));
            else if(tipoDeRanking.equalsIgnoreCase("Mensagens trocadas"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,5));
            else if(tipoDeRanking.equalsIgnoreCase("Alertas enviados"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,6));
            
            sessao.setAttribute("nomeRanking", tipoDeRanking);
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarRelatorio.jsp");
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
