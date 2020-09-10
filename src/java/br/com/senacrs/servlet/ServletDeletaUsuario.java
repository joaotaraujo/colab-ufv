package br.com.senacrs.servlet;

import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.TarefaBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.UsuarioDAO;
import br.com.senacrs.dao.GrupoDAO;
import br.com.senacrs.dao.PapelDAO;
import br.com.senacrs.dao.ProjetoDAO;
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

public class ServletDeletaUsuario extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            String nomeUsuarioEscolhido = (String) sessao.getAttribute("nomeUsuarioCarregar");
            
            UsuarioDAO ud=DAOFactory.createUsuarioDAO();
            ud.deletar(nomeUsuarioEscolhido);
           
            GrupoDAO gd;
            if(sessao.getAttribute("redirect").equals("redirectGrupo")){
                String nomeGrupoEscolhido = (String) sessao.getAttribute("grupoEscolhido"); 
                gd =DAOFactory.createGrupoDAO();
                GrupoBean grupo = gd.pesquisarGrupo(nomeGrupoEscolhido, nomeProjetoEscolhido,1);
            
                String grupoStringTabela = gd.retornarGrupoTabela(grupo, nomeProjetoEscolhido, (String) sessao.getAttribute("redirect"));
                sessao.setAttribute("informacoesGrupo", grupoStringTabela);
            
                ArrayList<UsuarioBean> integrantes =  grupo.getIntegrantes(); 
                String tabelaDeEnvolvidos = ud.retornaTabelaDeEnvolvidos(integrantes, Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),3);
                sessao.setAttribute("envolvidos", tabelaDeEnvolvidos);
                
                String integrantesString = "";
                for(int i=0; i<integrantes.size();i++)
                    integrantesString = integrantesString + "<option>" + integrantes.get(i).getNome() + "</option>";
                sessao.setAttribute("envolvidosParaDeletar",integrantesString);

                ArrayList<UsuarioBean> integrantesSemGrupo = gd.retornaNaoEnvolvidosGrupo(grupo, nomeProjetoEscolhido);
                String integrantesSemGrupoString = "";
                for(int i=0; i<integrantesSemGrupo.size();i++)
                    integrantesSemGrupoString = integrantesSemGrupoString + "<option>"+integrantesSemGrupo.get(i).getNome()+"</option>";
                sessao.setAttribute("envolvidosParaCadastrar", integrantesSemGrupoString);
                
                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesGrupo.jsp");
                dis.forward(request, response);
            }
            else if(sessao.getAttribute("redirect").equals("redirectAtividade")){
                String atividadeEscolhida = (String) sessao.getAttribute("atividadeEscolhida");

                AtividadeDAO ad=DAOFactory.createAtividadeDAO();
                AtividadeBean atividade = ad.buscar(atividadeEscolhida, 1);

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

                
                UsuarioDAO alud = DAOFactory.createUsuarioDAO();
                String alunosStringTabela = alud.retornaTabelaDeEnvolvidos((ArrayList<UsuarioBean>) alunosEnvolvidosAtividade, Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),1);
                sessao.setAttribute("alunos", alunosStringTabela);

                String botaoVoltar = "<div id=\"block_left\">"
                       +" <form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesAtividade\">"
                       +" <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>"
                   +" </div>";
                sessao.setAttribute("botaoVoltar", botaoVoltar);

                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesAtividade.jsp");
                dis.forward(request, response);
            }
            else if(sessao.getAttribute("redirect").equals("redirectTarefa")){
                String nomeTarefaEscolhida = (String) sessao.getAttribute("nomeTarefaEscolhida");

                TarefaDAO td=DAOFactory.createTarefaDAO();
                TarefaBean tarefa = td.buscar(nomeTarefaEscolhida, 1);

                ArrayList<UsuarioBean> alunosTarefa =  tarefa.getColaboradores(); 
                String alunosStringTabela = ud.retornaTabelaDeEnvolvidos((ArrayList<UsuarioBean>) alunosTarefa, Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")),2);
                sessao.setAttribute("alunos", alunosStringTabela);
                
                String opcoesAlunos ="";
                for(int i=0; i<alunosTarefa.size();i++){
                    opcoesAlunos = opcoesAlunos+"<option>"+alunosTarefa.get(i).getNome()+"</option>";
                }
                sessao.setAttribute("envolvidos", opcoesAlunos);    
                
                ProjetoDAO projd = DAOFactory.createProjetoDAO();
                ArrayList<UsuarioBean> alunosNaoEnvolvidosTarefa = td.retornarNaoEnvolvidosTarefa(tarefa,projd.pesquisarProjeto(nomeProjetoEscolhido,1));
                String opcoesAlunosNaoTarefa ="";
                for(int i=0; i<alunosNaoEnvolvidosTarefa.size();i++){
                    opcoesAlunosNaoTarefa = opcoesAlunosNaoTarefa+"<option>"+alunosNaoEnvolvidosTarefa.get(i).getNome()+"</option>";
                }
                sessao.setAttribute("alunosNaoEnvolvidos", opcoesAlunosNaoTarefa);


                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesTarefa.jsp");
                dis.forward(request, response);
            }
            else if(sessao.getAttribute("redirect").equals("redirectPapel")){
                PapelDAO pad = DAOFactory.createPapelDAO();
                String papeisStringTabela = pad.retornaTabelaDePapeis(nomeProjetoEscolhido);
                sessao.setAttribute("papeis", papeisStringTabela);
                
                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesPapel.jsp");
                dis.forward(request, response);
            }
            
            
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
