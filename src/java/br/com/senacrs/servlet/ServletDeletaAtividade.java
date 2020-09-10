package br.com.senacrs.servlet;

import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.ProjetoDAO;
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

public class ServletDeletaAtividade extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            String login = (String) sessao.getAttribute("login"); 
            response.setContentType("text/html");
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            
                    
            AtividadeDAO ad =DAOFactory.createAtividadeDAO();
            AtividadeBean atividade = ad.buscar((String) request.getParameter("atividadeEscolhida"), 1);
            ad.deletar(atividade);
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido,1);
            
            ArrayList<AtividadeBean> atividadesProjeto =  projeto.getAtividades(); 
            String atividadesStringTabela = ad.retornaAtividadesTabela(atividadesProjeto);
            
            sessao.setAttribute("atividades", atividadesStringTabela);
                       
            
            String atividadesString = "";
            for(int i=0; i<atividadesProjeto.size();i++)
                atividadesString = atividadesString +"<option>"+atividadesProjeto.get(i).getNome()+"</option>";
                
            request.setAttribute("atividadesExistentes",atividadesString);
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarAtividades.jsp");
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
            Logger.getLogger(ServletDeletaAtividade.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletDeletaAtividade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
