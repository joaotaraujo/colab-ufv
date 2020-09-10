package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.TarefaBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.UsuarioDAO;
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

public class ServletCarregaInformacoesTarefa extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String nomeTarefaEscolhida = (String) request.getParameter("tarefaParaCarregar");
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            TarefaDAO td=DAOFactory.createTarefaDAO();
            TarefaBean tarefa = td.buscar(nomeTarefaEscolhida, 1);
            
            AtividadeDAO atvd=DAOFactory.createAtividadeDAO();
            String tarefaStringTabela = td.retornarInformacoesTarefaTabela(tarefa, atvd.buscar(Integer.toString(tarefa.getAtividade()), 2).getNome());
            sessao.setAttribute("informacoesTarefa", tarefaStringTabela);
            
            UsuarioDAO ud=DAOFactory.createUsuarioDAO();
            ArrayList<UsuarioBean> alunosTarefa =  tarefa.getColaboradores(); 

            String alunosStringTabela = ud.retornaTabelaDeEnvolvidos((ArrayList<UsuarioBean>) alunosTarefa,Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),2);
            
            String opcoesAlunos ="";
            for(int i=0; i<alunosTarefa.size();i++){
                opcoesAlunos = opcoesAlunos+"<option>"+alunosTarefa.get(i).getNome()+"</option>";
            }
            
            ProjetoDAO pd=DAOFactory.createProjetoDAO();
            ArrayList<UsuarioBean> alunosNaoEnvolvidosTarefa = td.retornarNaoEnvolvidosTarefa(tarefa,pd.pesquisarProjeto(nomeProjetoEscolhido,1));
            String opcoesAlunosNaoTarefa ="";
            for(int i=0; i<alunosNaoEnvolvidosTarefa.size();i++){
                opcoesAlunosNaoTarefa = opcoesAlunosNaoTarefa+"<option>"+alunosNaoEnvolvidosTarefa.get(i).getNome()+"</option>";
            }
            sessao.setAttribute("alunosNaoEnvolvidos", opcoesAlunosNaoTarefa);
            
            sessao.setAttribute("envolvidos", opcoesAlunos);
            sessao.setAttribute("alunos", alunosStringTabela);
            sessao.setAttribute("nomeTarefaEscolhida", nomeTarefaEscolhida);
            sessao.setAttribute("idTarefaEscolhida", Integer.toString(tarefa.getId())); 
            sessao.setAttribute("redirect", "redirectTarefa");
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesTarefa.jsp");
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
