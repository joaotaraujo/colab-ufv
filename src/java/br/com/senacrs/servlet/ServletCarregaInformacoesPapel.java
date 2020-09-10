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

public class ServletCarregaInformacoesPapel extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String nomePapelEscolhido = (String) request.getParameter("papelParaCarregar"); 
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            sessao.setAttribute("mensagemCadastrarEnvolvidoPapel", "");
            
            PapelDAO pad=DAOFactory.createPapelDAO();
            
            ArrayList<PapelBean> papeisProjeto = pad.listarPapeis(nomeProjetoEscolhido);
            PapelBean papelEscolhido = new PapelBean();
            for(int i=0 ; i<papeisProjeto.size();i++){
                if(papeisProjeto.get(i).getFuncao().equalsIgnoreCase(nomePapelEscolhido))
                    papelEscolhido = papeisProjeto.get(i);
            }
            
            String papelStringTabela = pad.retornarPapelTabela(papelEscolhido, nomeProjetoEscolhido);
            sessao.setAttribute("informacoesPapel", papelStringTabela);
            
            UsuarioDAO ud=DAOFactory.createUsuarioDAO();
            ArrayList<UsuarioBean> integrantes =  pad.listarEnvolvidosPapel(papelEscolhido.getId()); 
            
            
            String tabelaDeEnvolvidos = ud.retornaTabelaDeEnvolvidos(integrantes, Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),4);
            
            
            String integrantesString = "";
            for(int i=0; i<integrantes.size();i++)
                integrantesString = integrantesString + "<option>" + integrantes.get(i).getNome() + "</option>";
            
            sessao.setAttribute("envolvidosParaDeletar",integrantesString);
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ArrayList<UsuarioBean> usuariosSemPapel = projd.retornaUsuariosSistemaSemPapel();
            String usuariosSemPapelString = "";
            for(int i=0; i<usuariosSemPapel.size();i++)
                usuariosSemPapelString = usuariosSemPapelString + "<option>"+usuariosSemPapel.get(i).getNome()+"</option>";
            
            
            sessao.setAttribute("idPapelEscolhido", Integer.toString(papelEscolhido.getId()));
            sessao.setAttribute("envolvidosParaCadastrar", usuariosSemPapelString);
            sessao.setAttribute("envolvidos", tabelaDeEnvolvidos);
            sessao.setAttribute("papelEscolhido", nomePapelEscolhido);
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
            Logger.getLogger(ServletCarregaInformacoesGrupo.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletCarregaInformacoesGrupo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
