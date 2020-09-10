package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.TarefaBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ArtefatoDAO;
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

public class ServletCarregaInformacoesAtividade extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String atividadeEscolhida = (String) request.getParameter("atividadeParaCarregar");
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            sessao.setAttribute("atividadeEscolhida", atividadeEscolhida);
            
            AtividadeDAO ad=DAOFactory.createAtividadeDAO();
            AtividadeBean atividade = ad.buscar(atividadeEscolhida, 1);
            
            sessao.setAttribute("idAtividadeEscolhida", Integer.toString(atividade.getId()));
            
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
           
            ArrayList<UsuarioBean> alunosNaoEnvolvidosAtividade = ad.retornarNaoEnvolvidosAtividade(atividade,projd.pesquisarProjeto(nomeProjetoEscolhido,1));
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
            
            String atividadeStringTabela = ad.retornarInformacoesAtividadeTabela(atividade, (String) sessao.getAttribute("projetoEscolhido"));
            sessao.setAttribute("informacoesAtividade", atividadeStringTabela);
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            String alunosStringTabela = ud.retornaTabelaDeEnvolvidos((ArrayList<UsuarioBean>) alunosEnvolvidosAtividade,Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),1);
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
            
            //carregar tabela de artefatos
            //fazer o deletar artefato e add
                        
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
            Logger.getLogger(ServletCarregaInformacoesAtividade.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletCarregaInformacoesAtividade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
