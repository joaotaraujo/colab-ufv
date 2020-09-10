package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.TarefaBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.UsuarioDAO;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.TarefaDAO;
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

public class ServletCadastraEnvolvidoAtividade extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            String login = (String) sessao.getAttribute("login"); 
            response.setContentType("text/html");
            String atividadeEscolhida = (String) sessao.getAttribute("atividadeEscolhida");
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            UsuarioBean alunoParaCadastrarAtividade = ud.buscar(request.getParameter("alunoSelecionado"),1);
            AtividadeDAO ad=DAOFactory.createAtividadeDAO();
            AtividadeBean atividade = ad.buscar(atividadeEscolhida, 1);
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido,1);
            
            ad.adicionarEnvolvidoAtividade(alunoParaCadastrarAtividade, atividade);
            
            ArrayList<UsuarioBean> alunosNaoEnvolvidosAtividade = ad.retornarNaoEnvolvidosAtividade(atividade,projeto);
            String opcoesAlunosNaoAtividade ="";
            for(int i=0; i<alunosNaoEnvolvidosAtividade.size();i++){
                opcoesAlunosNaoAtividade = opcoesAlunosNaoAtividade+"<option>"+alunosNaoEnvolvidosAtividade.get(i).getNome()+"</option>";
            }
            sessao.setAttribute("alunosNaoEnvolvidos", opcoesAlunosNaoAtividade);
            
            
            ArrayList<UsuarioBean> alunosEnvolvidosAtividade = ad.retornarEnvolvidosAtividade(atividade);
            String opcoesAlunos ="";
            for(int i=0; i<alunosEnvolvidosAtividade.size();i++){
                opcoesAlunos = opcoesAlunos+"<option>"+alunosEnvolvidosAtividade.get(i).getNome()+"</option>";
            }
            sessao.setAttribute("envolvidos", opcoesAlunos);
            
            TarefaDAO td=DAOFactory.createTarefaDAO();
            ArrayList<TarefaBean> tarefas =  ad.retornarTarefasAtividade(ad.buscar((String) sessao.getAttribute("atividadeEscolhida"),1)); 
            String tarefasStringTabela = td.retornaTarefasTabela(tarefas);
            
            sessao.setAttribute("tarefas", tarefasStringTabela);
            
            String alunosStringTabela = ud.retornaTabelaDeEnvolvidos(alunosEnvolvidosAtividade,projeto.getId(),1);
            sessao.setAttribute("alunos", alunosStringTabela);
            
            String atividadeStringTabela = ad.retornarInformacoesAtividadeTabela(atividade, (String) sessao.getAttribute("projetoEscolhido"));
            sessao.setAttribute("informacoesAtividade", atividadeStringTabela);
            
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
        } catch (SQLException ex) {
            Logger.getLogger(ServletCadastraEnvolvidoAtividade.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletCadastraEnvolvidoAtividade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
