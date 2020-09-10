package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
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

public class ServletCarregaCadastroTarefa extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            ProjetoDAO projd=DAOFactory.createProjetoDAO();
            ArrayList<UsuarioBean> alunosNoProjeto = projd.listarAlunosNoProjeto(nomeProjetoEscolhido);
            ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido,1);
            String alunosString = "";
            for(int i=0; i<alunosNoProjeto.size();i++)
                alunosString = alunosString + "<option>" + alunosNoProjeto.get(i).getNome() + "</option>";
            request.setAttribute("alunosExistentes",alunosString);
            
            response.setContentType("text/html");
            ArrayList<AtividadeBean> atividadesNoProjeto = projeto.getAtividades();
            String atividadesString = "";
            if(sessao.getAttribute("atividadeEscolhida")!=null)
                atividadesString = atividadesString+ "<option>"+sessao.getAttribute("atividadeEscolhida")+"</option>";
            for(int i=0; i<atividadesNoProjeto.size();i++){
                if(!atividadesNoProjeto.get(i).getNome().equalsIgnoreCase((String) sessao.getAttribute("atividadeEscolhida")))
                    atividadesString = atividadesString + "<option>" + atividadesNoProjeto.get(i).getNome() + "</option>";
            }
            request.setAttribute("atividadesExistentes",atividadesString);
                      
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroTarefa.jsp");
            dis.forward(request, response);
            
            
    }
        
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletVizualizaGrupo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCarregaCadastroTarefa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletVizualizaGrupo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCarregaCadastroTarefa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
