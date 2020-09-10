package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.UsuarioDAO;
import br.com.senacrs.dao.GrupoDAO;
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

public class ServletDeletaEnvolvidoGrupo extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String nomeGrupoEscolhido = (String) sessao.getAttribute("grupoEscolhido"); 
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            GrupoDAO gd;
            gd =DAOFactory.createGrupoDAO();
            GrupoBean grupo = gd.pesquisarGrupo(nomeGrupoEscolhido, nomeProjetoEscolhido,1);
            
            
            
            String grupoStringTabela = gd.retornarGrupoTabela(grupo, nomeProjetoEscolhido, (String) sessao.getAttribute("redirect"));
            sessao.setAttribute("informacoesGrupo", grupoStringTabela);
            
            UsuarioDAO ud=DAOFactory.createUsuarioDAO();
            
            String nomeUsuario = request.getParameter("envolvidoSelecionado");
            
            UsuarioBean usuarioParaTirarGrupo = ud.buscar(nomeUsuario, 1);
            if(usuarioParaTirarGrupo != null)
                gd.deletarUsuarioGrupo(usuarioParaTirarGrupo, grupo);
                    
            
            ArrayList<UsuarioBean> usuarios =  gd.retornaEnvolvidosGrupo(grupo, nomeUsuario);
            
            String tabela = ud.retornaTabelaDeEnvolvidos(usuarios, Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),3);
            
            
            String envolvidosString = "";
            for(int i=0; i<usuarios.size();i++)
                envolvidosString = envolvidosString + "<option>" + usuarios.get(i).getNome() + "</option>";
            
            sessao.setAttribute("envolvidosParaDeletar",envolvidosString);
            
            ArrayList<UsuarioBean> integrantesSemGrupo = gd.retornaNaoEnvolvidosGrupo(grupo, nomeProjetoEscolhido);
            String integrantesSemGrupoString = "";
            for(int i=0; i<integrantesSemGrupo.size();i++)
                integrantesSemGrupoString = integrantesSemGrupoString + "<option>"+integrantesSemGrupo.get(i).getNome()+"</option>";
            
            sessao.setAttribute("envolvidosParaCadastrar", integrantesSemGrupoString);
            
            
            sessao.setAttribute("envolvidos", tabela);
            sessao.setAttribute("grupoEscolhido", nomeGrupoEscolhido);
            sessao.setAttribute("redirect", "redirectGrupo");
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesGrupo.jsp");
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
