package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.UsuarioDAO;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.ProjetoDAO;
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


public class ServletCadastraAtividade extends HttpServlet {

   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ParseException{
            HttpSession sessao = request.getSession();
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            
            ProjetoDAO proj = DAOFactory.createProjetoDAO();
            ProjetoBean projeto = proj.pesquisarProjeto(nomeProjetoEscolhido,1);
            
            
            ArrayList<AtividadeBean> atividadesProjeto = new ArrayList<AtividadeBean>();
            atividadesProjeto = projeto.getAtividades();
            
            int passou = 0;
            for(int i=0; i<atividadesProjeto.size();i++){
                if(atividadesProjeto.get(i).getNome().equalsIgnoreCase(request.getParameter("nome"))){
                    passou=1;
                    
                    //Carrega alunos que estão em um projeto em um select
                    ArrayList<UsuarioBean> alunosNoProjeto = proj.listarAlunosNoProjeto(nomeProjetoEscolhido);
                    String alunosString = "";
                    for(int k=0; k<alunosNoProjeto.size();k++)
                        alunosString = alunosString + "<option>" + alunosNoProjeto.get(k).getNome() + "</option>";
                    request.setAttribute("alunosExistentes",alunosString);
                    
                    request.setAttribute("mensagemCadastraAtividade","Nome de atividade já existente no projeto!");
                    RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroAtividade.jsp");
                    dis.forward(request, response);
                }
            }  
   
            if(passou == 0){
                AtividadeBean atividade = new AtividadeBean();
                AtividadeDAO ativd = DAOFactory.createAtividadeDAO();
                
                atividade.setNome(request.getParameter("nome"));
                
                
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                Date dataInicio = (Date) formato.parse(request.getParameter("dataInicio"));
                Date dataTermino = (Date) formato.parse(request.getParameter("dataTermino"));
                atividade.setDataInicio(dataInicio);
                atividade.setDataTermino(dataTermino);
                
                atividade.setDescricao(request.getParameter("descricao"));
                atividade.setComplexidade(request.getParameter("complexidade"));
                atividade.setPrioridade(Integer.parseInt(request.getParameter("prioridade")));
                atividade.setStatus("Nova Atividade");
            
                atividade.setProjeto(projeto.getId());
            
                UsuarioDAO ud=DAOFactory.createUsuarioDAO();
                UsuarioBean usuario = ud.buscar(request.getParameter("alunoResponsavel"), 1);
                atividade.setResponsavel(usuario);
                    
                ativd.cadastrar(atividade);
            
                
                //Carrega alunos que estão em um projeto em um select
                ArrayList<UsuarioBean> alunosNoProjeto = proj.listarAlunosNoProjeto(nomeProjetoEscolhido);
                String alunosString = "";
                for(int i=0; i<alunosNoProjeto.size();i++)
                    alunosString = alunosString + "<option>" + alunosNoProjeto.get(i).getNome() + "</option>";
                request.setAttribute("alunosExistentes",alunosString);
            
            
                request.setAttribute("mensagemCadastraAtividade","Cadastrado realizado com sucesso!");
                RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroAtividade.jsp");
                dis.forward(request, response);
            }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCadastraAtividade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraAtividade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCadastraAtividade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraAtividade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
