package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.UsuarioDAO;
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

public class ServletCarregaInformacoesUsuario extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession(); 
            response.setContentType("text/html;charset=UTF-8");
            String tipoUsuario = (String) request.getParameter("tipoUsuario");
            String nomeUsuario = request.getParameter("usuarioParaCarregar"); 
            
        
            
            UsuarioDAO ud =DAOFactory.createUsuarioDAO();
           
                
            if(tipoUsuario.equalsIgnoreCase("1")){
                UsuarioBean aluno =  ud.buscar(nomeUsuario, 1); 
                
                String informacoesUsuario="";
                if(sessao.getAttribute("redirect").equals("redirectTarefa")){
                    String nomeTarefaEscolhida = (String) sessao.getAttribute("nomeTarefaEscolhida"); 
                    informacoesUsuario = ud.retornarUsuarioTabela(aluno,2,nomeTarefaEscolhida);
                } else if(sessao.getAttribute("redirect").equals("redirectGrupo")){
                    String nomeGrupoEscolhido = (String) sessao.getAttribute("grupoEscolhido"); 
                    informacoesUsuario = ud.retornarUsuarioTabela(aluno,1,nomeGrupoEscolhido);
                } else if (sessao.getAttribute("redirect").equals("redirectAtividade")){
                    String nomeAtividadeEscolhida = (String) sessao.getAttribute("atividadeEscolhida"); 
                    informacoesUsuario = ud.retornarUsuarioTabela(aluno,3,nomeAtividadeEscolhida);
                } else if (sessao.getAttribute("redirect").equals("redirectPapel")){
                    String nomePapelEscolhido = (String) sessao.getAttribute("papelEscolhido"); 
                    informacoesUsuario = ud.retornarUsuarioTabela(aluno,4,nomePapelEscolhido);
                } else if (sessao.getAttribute("redirect").equals("redirectProjeto")){
                    informacoesUsuario = ud.retornarUsuarioTabela(aluno,5,"");
                }  else if (sessao.getAttribute("redirect").equals("redirectRelatorio")){
                    informacoesUsuario = ud.retornarUsuarioTabela(aluno,6,"");
                } 
                sessao.setAttribute("idUsuarioEscolhido", Integer.toString(aluno.getId()));
                sessao.setAttribute("nomeUsuarioCarregar",aluno.getNome());
                sessao.setAttribute("tipoUsuario", "1");
                sessao.setAttribute("informacoesUsuario", informacoesUsuario);
            }
            else if(tipoUsuario.equalsIgnoreCase("2")){
                UsuarioBean professor =  ud.buscar(nomeUsuario, 1); 
                
                String informacoesUsuario="";
                if(sessao.getAttribute("redirect").equals("redirectTarefa")){
                    String nomeTarefaEscolhida = (String) sessao.getAttribute("nomeTarefaEscolhida"); 
                    informacoesUsuario = ud.retornarUsuarioTabela(professor,2,nomeTarefaEscolhida);
                } else if(sessao.getAttribute("redirect").equals("redirectGrupo")){
                    String nomeGrupoEscolhido = (String) sessao.getAttribute("grupoEscolhido"); 
                    informacoesUsuario = ud.retornarUsuarioTabela(professor,1,nomeGrupoEscolhido);
                } else if (sessao.getAttribute("redirect").equals("redirectAtividade")){
                    String nomeAtividadeEscolhida = (String) sessao.getAttribute("atividadeEscolhida"); 
                    informacoesUsuario = ud.retornarUsuarioTabela(professor,3,nomeAtividadeEscolhida);
                } else if (sessao.getAttribute("redirect").equals("redirectPapel")){
                    String nomePapelEscolhido = (String) sessao.getAttribute("papelEscolhido"); 
                    informacoesUsuario = ud.retornarUsuarioTabela(professor,4,nomePapelEscolhido);
                } else if (sessao.getAttribute("redirect").equals("redirectProjeto")){
                    informacoesUsuario = ud.retornarUsuarioTabela(professor,5,"");
                }  else if (sessao.getAttribute("redirect").equals("redirectRelatorio")){
                    informacoesUsuario = ud.retornarUsuarioTabela(professor,6,"");
                } 
                sessao.setAttribute("idUsuarioEscolhido", Integer.toString(professor.getId()));
                sessao.setAttribute("nomeUsuarioCarregar",professor.getNome());
                sessao.setAttribute("tipoUsuario", "2");
                sessao.setAttribute("informacoesUsuario", informacoesUsuario);
            }
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesUsuario.jsp");
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
