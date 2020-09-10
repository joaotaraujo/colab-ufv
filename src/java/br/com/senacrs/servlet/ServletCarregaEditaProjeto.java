package br.com.senacrs.servlet;

import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.GrupoDAO;
import br.com.senacrs.dao.PapelDAO;
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

public class ServletCarregaEditaProjeto extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String idProjetoEscolhido = (String) sessao.getAttribute("idProjetoEscolhido"); 
            
            
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ProjetoBean projeto = projd.pesquisarProjeto(idProjetoEscolhido,2);
            
            PapelDAO pad = DAOFactory.createPapelDAO();
            String papeisStringTabela = pad.retornaTabelaDePapeis(projeto.getNome());
            sessao.setAttribute("papeis", papeisStringTabela);
            
            ArrayList<GrupoBean> gruposProjeto = projeto.getGrupos();
            String gruposString = "";
            for(int i=0;i<gruposProjeto.size();i++){
                gruposString = gruposString + "<option>"+ gruposProjeto.get(i).getNome() +"</option>";
            }
            request.setAttribute("gruposExistentes",gruposString);
            
            GrupoDAO gd = DAOFactory.createGrupoDAO();
            String gruposStringTabela = gd.retornaGruposTabela(gruposProjeto,2);
            sessao.setAttribute("grupos", gruposStringTabela);
            
            String projetoStringTabela = projd.retornarProjetoTabelaEditavel(projeto);
            sessao.setAttribute("informacoesProjeto", projetoStringTabela);
            
            request.setAttribute("redirect","1");
            
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
            Logger.getLogger(ServletCarregaEditaProjeto.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletCarregaEditaProjeto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
