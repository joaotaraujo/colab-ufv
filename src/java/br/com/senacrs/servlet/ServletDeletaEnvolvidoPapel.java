package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.PapelBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.UsuarioDAO;
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

public class ServletDeletaEnvolvidoPapel extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String nomePapelEscolhido = (String) sessao.getAttribute("papelEscolhido"); 
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            PapelDAO pad =DAOFactory.createPapelDAO();
            PapelBean papel = pad.buscar(nomePapelEscolhido, 1);
            
            
            
            String papelStringTabela = pad.retornarPapelTabela(papel, nomeProjetoEscolhido);
            sessao.setAttribute("informacoesPapel", papelStringTabela);
            
            UsuarioDAO ud=DAOFactory.createUsuarioDAO();
            
            String nomeUsuario = request.getParameter("envolvidoSelecionado");
            
            UsuarioBean usuarioParaTirarPapel = ud.buscar(nomeUsuario, 1);
            if(usuarioParaTirarPapel != null)
                pad.deletarUsuarioPapel(usuarioParaTirarPapel, papel);
                    
            
            ArrayList<UsuarioBean> usuarios =  pad.listarEnvolvidosPapel(papel.getId());
            
            String tabela = ud.retornaTabelaDeEnvolvidos(usuarios, Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),4);
            
            
            String envolvidosString = "";
            for(int i=0; i<usuarios.size();i++)
                envolvidosString = envolvidosString + "<option>" + usuarios.get(i).getNome() + "</option>";
            
            sessao.setAttribute("envolvidosParaDeletar",envolvidosString);
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ArrayList<UsuarioBean> integrantesSemPapel = projd.retornaUsuariosSistemaSemPapel();
            String integrantesSemPapelString = "";
            for(int i=0; i<integrantesSemPapel.size();i++)
                integrantesSemPapelString = integrantesSemPapelString + "<option>"+integrantesSemPapel.get(i).getNome()+"</option>";
            
            sessao.setAttribute("envolvidosParaCadastrar", integrantesSemPapelString);
            
            
            sessao.setAttribute("envolvidos", tabela);
            sessao.setAttribute("grupoEscolhido", nomePapelEscolhido);
            sessao.setAttribute("redirect", "redirectPapel");
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesPapel.jsp");
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
            Logger.getLogger(ServletDeletaEnvolvidoGrupo.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletDeletaEnvolvidoGrupo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
