package br.com.senacrs.servlet;

import br.com.senacrs.bean.AtividadeBean;
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

public class ServletCadastraEnvolvidoTarefa extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String nomeTarefaEscolhida = (String) sessao.getAttribute("nomeTarefaEscolhida");
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            String nomeAtividadeEscolhida = (String) sessao.getAttribute("atividadeEscolhida"); 
            
            
            AtividadeDAO ad = DAOFactory.createAtividadeDAO();
            AtividadeBean atividade = ad.buscar(nomeAtividadeEscolhida, 1);
            
            TarefaDAO td=DAOFactory.createTarefaDAO();
            TarefaBean tarefa = td.buscar(nomeTarefaEscolhida, 1);
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            td.adicionarEnvolvidoTarefa(ud.buscar(request.getParameter("alunoSelecionado"), 1), tarefa,atividade);
            
            ProjetoDAO pd = DAOFactory.createProjetoDAO();
            ArrayList<UsuarioBean> alunosNaoEnvolvidosTarefa = td.retornarNaoEnvolvidosTarefa(tarefa,pd.pesquisarProjeto(nomeProjetoEscolhido,1));
            String opcoesAlunosNaoTarefa ="";
            for(int i=0; i<alunosNaoEnvolvidosTarefa.size();i++){
                opcoesAlunosNaoTarefa = opcoesAlunosNaoTarefa+"<option>"+alunosNaoEnvolvidosTarefa.get(i).getNome()+"</option>";
            }
            sessao.setAttribute("alunosNaoEnvolvidos", opcoesAlunosNaoTarefa);
            
            String tarefaStringTabela = td.retornarInformacoesTarefaTabela(tarefa, (String) sessao.getAttribute("atividadeEscolhida"));
            sessao.setAttribute("informacoesTarefa", tarefaStringTabela);
            
            
            ArrayList<UsuarioBean> envolvidos = td.retornaEnvolvidosTarefa(nomeTarefaEscolhida, nomeAtividadeEscolhida, nomeProjetoEscolhido);

            String envolvidosStringTabela = ud.retornaTabelaDeEnvolvidos((ArrayList<UsuarioBean>) envolvidos,Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),2);
            
           
            
            sessao.setAttribute("alunos", envolvidosStringTabela);
            sessao.setAttribute("nomeTarefaEscolhida", nomeTarefaEscolhida);
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
