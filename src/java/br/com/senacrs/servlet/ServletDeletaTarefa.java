package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.TarefaBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.UsuarioDAO;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.TarefaDAO;
import java.io.IOException;
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

public class ServletDeletaTarefa extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String atividadeEscolhida = (String) sessao.getAttribute("atividadeEscolhida");
            
            TarefaDAO td=DAOFactory.createTarefaDAO();
            TarefaBean tarefa = td.buscar((String)request.getParameter("tarefaEscolhida"), 1);
            td.deletar(tarefa.getId());
            AtividadeDAO ad=DAOFactory.createAtividadeDAO();
            AtividadeBean atividade = ad.buscar(atividadeEscolhida, 1);
            ArrayList<TarefaBean> tarefas =  atividade.getTarefas(); 
            String tarefasStringTabela = td.retornaTarefasTabela(tarefas);
            
            sessao.setAttribute("tarefas", tarefasStringTabela);
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            ArrayList<UsuarioBean> alunosEnvolvidosAtividade = atividade.getAlunosEnvolvidos();
            String alunosStringTabela = ud.retornaTabelaDeEnvolvidos(alunosEnvolvidosAtividade,  Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),2);
            sessao.setAttribute("alunos", alunosStringTabela);
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesAtividade.jsp");
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
