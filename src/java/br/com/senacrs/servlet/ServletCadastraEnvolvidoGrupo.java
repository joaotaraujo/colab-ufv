package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.UsuarioDAO;
import br.com.senacrs.dao.GrupoDAO;
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

public class ServletCadastraEnvolvidoGrupo extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String nomeGrupoEscolhido = (String) sessao.getAttribute("grupoEscolhido"); 
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            GrupoDAO gd=DAOFactory.createGrupoDAO();
            GrupoBean grupo = gd.pesquisarGrupo(nomeGrupoEscolhido, nomeProjetoEscolhido,1);
            
            ProjetoDAO pd=DAOFactory.createProjetoDAO();
            ProjetoBean projeto = pd.pesquisarProjeto(nomeProjetoEscolhido,1);
            
            String grupoStringTabela = gd.retornarGrupoTabela(grupo, nomeProjetoEscolhido, (String) sessao.getAttribute("redirect"));
            sessao.setAttribute("informacoesGrupo", grupoStringTabela);
            
            UsuarioDAO ud=DAOFactory.createUsuarioDAO();
            
            String nomeUsuario = request.getParameter("envolvidoParaCadastrarGrupo");
            
            UsuarioBean usuarioParaCadastrar = ud.buscar(nomeUsuario, 1);
            String mensagemCadastroEnvolvidoGrupo = "";
            if(usuarioParaCadastrar != null){
                mensagemCadastroEnvolvidoGrupo = gd.cadastrarUsuarioEmGrupo(usuarioParaCadastrar, grupo, projeto);
            }
                
            sessao.setAttribute("mensagemCadastrarEnvolvidoGrupo", mensagemCadastroEnvolvidoGrupo);
            
            ArrayList<UsuarioBean> integrantes = gd.listarIntegrantes(grupo.getId());
            
            String tabelaDeEnvolvidos = ud.retornaTabelaDeEnvolvidos(integrantes, projeto.getId(),3);
            
            
            String integrantesString = "";
            for(int i=0; i<integrantes.size();i++)
                integrantesString = integrantesString + "<option>" + integrantes.get(i).getNome() + "</option>";
            
            sessao.setAttribute("envolvidosParaDeletar",integrantesString);
            
            
            ArrayList<UsuarioBean> integrantesSemGrupo = gd.retornaNaoEnvolvidosGrupo(grupo, nomeProjetoEscolhido);
            String integrantesSemGrupoString = "";
            for(int i=0; i<integrantesSemGrupo.size();i++)
                integrantesSemGrupoString = integrantesSemGrupoString + "<option>"+integrantesSemGrupo.get(i).getNome()+"</option>";
            
            sessao.setAttribute("envolvidosParaCadastrar", integrantesSemGrupoString);
            
            sessao.setAttribute("envolvidos", tabelaDeEnvolvidos);
            sessao.setAttribute("grupoEscolhido", nomeGrupoEscolhido);
            sessao.setAttribute("redirect", "redirectGrupos");
            
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
            Logger.getLogger(ServletCadastraEnvolvidoGrupo.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletCadastraEnvolvidoGrupo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
