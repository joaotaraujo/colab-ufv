package br.com.senacrs.servlet;

import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.GrupoDAO;
import br.com.senacrs.dao.PapelDAO;
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

public class ServletCarregaInformacoesProjeto extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            ProjetoDAO pd=DAOFactory.createProjetoDAO();
            ProjetoBean projeto = pd.pesquisarProjeto(nomeProjetoEscolhido,1);
            
            ArrayList<GrupoBean> gruposProjeto = projeto.getGrupos();
            String gruposString = "";
            for(int i=0; i<gruposProjeto.size(); i++)
                gruposString = gruposString +"<option>"+gruposProjeto.get(i).getNome()+"</option>";
            
            PapelDAO pad = DAOFactory.createPapelDAO();
            String papeisStringTabela = pad.retornaTabelaDePapeis(nomeProjetoEscolhido);
            sessao.setAttribute("papeis", papeisStringTabela);
            
            request.setAttribute("gruposExistentes",gruposString);
            sessao.setAttribute("gruposExistentes",gruposString);
            
            GrupoDAO gd = DAOFactory.createGrupoDAO();
            String gruposStringTabela = gd.retornaGruposTabela(gruposProjeto,2);
            sessao.setAttribute("grupos", gruposStringTabela);
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            String alunosStringTabela = ud.retornaTabelaDeEnvolvidos(pd.retornaEnvolvidosProjeto(projeto), Integer.parseInt((String) sessao.getAttribute("idProjetoEscolhido")), 5);
            sessao.setAttribute("alunos", alunosStringTabela);
            
            String projetoStringTabela = pd.retornarProjetoTabela(projeto);
            sessao.setAttribute("informacoesProjeto", projetoStringTabela);
            
            sessao.setAttribute("idProjetoEscolhido", Integer.toString(projeto.getId()));
            
            sessao.setAttribute("redirect","redirectProjeto");
            
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
