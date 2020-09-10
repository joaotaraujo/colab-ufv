package br.com.senacrs.servlet;

import br.com.senacrs.bean.AlertaBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AlertaDAO;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.UsuarioDAO;
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

public class ServletCarregaAlertas extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            String nomeUsuarioLogado = (String) sessao.getAttribute("nomeUsuario"); 
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            
            UsuarioBean usuario = ud.buscar(nomeUsuarioLogado, 1);
            ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido, 1);
            
            AlertaDAO alertd = DAOFactory.createAlertaDAO();
            ArrayList<AlertaBean> alertasRecebidos = alertd.listarAlertasRecebidos(projeto.getId(), usuario.getId());
            ArrayList<AlertaBean> alertasEnviados = alertd.listarAlertasEnviados(projeto.getId(), usuario.getId());
            
            String alertasRecebidosTabela = alertd.retornaAlertasTabela(alertasRecebidos,2);
            String alertasEnviadosTabela = alertd.retornaAlertasTabela(alertasEnviados,1);
            
            sessao.setAttribute("alertasRecebidos", alertasRecebidosTabela);
            
            sessao.setAttribute("alertasEnviados",alertasEnviadosTabela);
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarAlertas.jsp");
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
            Logger.getLogger(ServletCarregaInformacoesProjeto.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletCarregaInformacoesProjeto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
