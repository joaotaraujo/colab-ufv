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
import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ServletCadastraTarefa extends HttpServlet {

   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ParseException{
            HttpSession sessao = request.getSession();
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            
            TarefaDAO td=DAOFactory.createTarefaDAO();
            ArrayList<TarefaBean> tarefasAtividade = new ArrayList<TarefaBean>();
            
            AtividadeDAO ativd = DAOFactory.createAtividadeDAO();
            AtividadeBean atividade = ativd.buscar(request.getParameter("atividadeDaTarefa"), 1);
            tarefasAtividade = atividade.getTarefas();
            
            ProjetoDAO projd=DAOFactory.createProjetoDAO();
            
            int passou = 0;
            for(int i=0; i<tarefasAtividade.size();i++){
                if(tarefasAtividade.get(i).getNome().equalsIgnoreCase(request.getParameter("nome"))){
                    passou=1;
                    
                    //Carrega alunos que estão em um projeto na forma de select
                    ArrayList<UsuarioBean> alunosNoProjeto = projd.listarAlunosNoProjeto(nomeProjetoEscolhido);
                    String alunosString = "";
                    for(int j=0; j<alunosNoProjeto.size();j++)
                        alunosString = alunosString + "<option>" + alunosNoProjeto.get(j).getNome() + "</option>";
                    request.setAttribute("alunosExistentes",alunosString);
            
                    //Carrega atividades que estão em um projeto na forma de select
                    ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido,1);
                    ArrayList<AtividadeBean> atividadesNoProjeto = ativd.listarAtividades(projeto.getId());
                    String atividadesString = "";
                    for(int k=0; k<atividadesNoProjeto.size();k++)
                        atividadesString = atividadesString + "<option>" + atividadesNoProjeto.get(k).getNome() + "</option>";
                    request.setAttribute("atividadesExistentes",atividadesString);
                    
                    request.setAttribute("mensagemCadastraTarefa","Nome de tarefa já existente nesta atividade!");
                }
            }  
            
            if(passou==0){
                TarefaBean tarefa = new TarefaBean();
                TarefaDAO tard = DAOFactory.createTarefaDAO();
                
                tarefa.setId(tard.quantTarefas());
                tarefa.setNome(request.getParameter("nome"));
                
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                Date dataInicio = (Date) formato.parse(request.getParameter("dataInicio"));
                Date dataTermino = (Date) formato.parse(request.getParameter("dataTermino"));
                tarefa.setDataInicio(dataInicio);
                tarefa.setDataTermino(dataTermino);
                
                tarefa.setDescricao(request.getParameter("descricao"));
                tarefa.setComplexidade(request.getParameter("complexidade"));
                tarefa.setPrioridade(Integer.parseInt(request.getParameter("prioridade")));
                tarefa.setStatus("Nova tarefa");
             
                UsuarioDAO ud=DAOFactory.createUsuarioDAO();
                UsuarioBean aluno = ud.buscar(request.getParameter("alunoResponsavel"), 1);
                tarefa.setResponsavel(aluno);
                    
                tarefa.setAtividade(atividade.getId());
                
                td.cadastrar(tarefa);
                ativd.adicionarEnvolvidoAtividade(aluno, atividade);
            
                //Carrega alunos que estão em um projeto na forma de select
                ArrayList<UsuarioBean> alunosNoProjeto = projd.listarAlunosNoProjeto(nomeProjetoEscolhido);
                String alunosString = "";
                for(int i=0; i<alunosNoProjeto.size();i++)
                    alunosString = alunosString + "<option>" + alunosNoProjeto.get(i).getNome() + "</option>";
                request.setAttribute("alunosExistentes",alunosString);
            
                //Carrega atividades que estão em um projeto na forma de select
                ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido,1);
                ArrayList<AtividadeBean> atividadesNoProjeto = ativd.listarAtividades(projeto.getId());
                String atividadesString = "";
                for(int i=0; i<atividadesNoProjeto.size();i++)
                    atividadesString = atividadesString + "<option>" + atividadesNoProjeto.get(i).getNome() + "</option>";
                request.setAttribute("atividadesExistentes",atividadesString);
            
                request.setAttribute("mensagemCadastraTarefa","Cadastrado realizado com sucesso!");
            }
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroTarefa.jsp");
            dis.forward(request, response);
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCadastraTarefa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraTarefa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCadastraTarefa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraTarefa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
