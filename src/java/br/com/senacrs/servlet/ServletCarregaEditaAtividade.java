package br.com.senacrs.servlet;

import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.TarefaBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ArtefatoDAO;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.TarefaDAO;
import br.com.senacrs.dao.UsuarioDAO;
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

public class ServletCarregaEditaAtividade extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String idAtividadeEscolhida =  (String) request.getParameter("idAtividadeEscolhida");
            
            sessao.setAttribute("idAtividadeEscolhida", idAtividadeEscolhida);
            
            AtividadeDAO ad=DAOFactory.createAtividadeDAO();
            AtividadeBean atividade = ad.buscar(idAtividadeEscolhida, 2);
            
            ProjetoDAO pd = DAOFactory.createProjetoDAO();
            ProjetoBean projeto = pd.pesquisarProjeto((String)sessao.getAttribute("projetoEscolhido"),1);
            String atividadeStringTabela = ad.retornarAtividadeTabelaEditavel(atividade, projeto);
            sessao.setAttribute("informacoesAtividade", atividadeStringTabela);
            
            
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
            ArrayList<TarefaBean> tarefas = atividade.getTarefas(); 
            String tarefasStringTabela = td.retornaTarefasTabela(tarefas);
            
            sessao.setAttribute("tarefas", tarefasStringTabela);
            sessao.setAttribute("redirect","redirectAtividade");
            
            
            UsuarioDAO alud = DAOFactory.createUsuarioDAO();
            String alunosStringTabela = alud.retornaTabelaDeEnvolvidos((ArrayList<UsuarioBean>) alunosEnvolvidosAtividade,Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),1);
            sessao.setAttribute("alunos", alunosStringTabela);
            
            String botaoVoltar = "<div id=\"block_left\">"
                   +" <form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesAtividade\">"
                   +" <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>"
               +" </div>";
            sessao.setAttribute("botaoVoltar", botaoVoltar);
            
            ArtefatoDAO Artd = DAOFactory.createArtefatoDAO();
            String artefatosSelect = "";
            ArrayList<String> listaArtefatosUsuarioLogado = Artd.listarArtefatosAluno(Integer.parseInt((String)sessao.getAttribute("idUsuarioLogado")));
            for(int i=0;i<listaArtefatosUsuarioLogado.size();i++)
                artefatosSelect = artefatosSelect +"<option>"+ listaArtefatosUsuarioLogado.get(i)+"</option>";
            sessao.setAttribute("artefatosDoUsuario", artefatosSelect);   
            
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
