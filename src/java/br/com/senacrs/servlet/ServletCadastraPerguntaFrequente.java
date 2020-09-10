package br.com.senacrs.servlet;

import br.com.senacrs.bean.PerguntaFrequenteBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.PerguntaFrequenteDAO;
import br.com.senacrs.dao.ProjetoDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ServletCadastraPerguntaFrequente extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
            HttpSession sessao = request.getSession();
            
            PerguntaFrequenteDAO pd=DAOFactory.createPerguntaFrequenteDAO();
            
            PerguntaFrequenteBean perguntaFrequenteExiste = null;
             
            
            perguntaFrequenteExiste = pd.buscar(request.getParameter("pergunta"),1);
            if(perguntaFrequenteExiste!=null){
                request.setAttribute("mensagemCadastraPerguntaFrequente","Esta pergunta já existe!");
            }  
            else{
                PerguntaFrequenteBean perguntaFrequente = new PerguntaFrequenteBean();
                
                perguntaFrequente.setPergunta(request.getParameter("pergunta"));
                perguntaFrequente.setResposta(request.getParameter("resposta"));
                
                String projetoLogado = (String) sessao.getAttribute("projetoEscolhido");
                ProjetoDAO prod = DAOFactory.createProjetoDAO();
                ProjetoBean projeto = prod.pesquisarProjeto(projetoLogado,1);
                
                perguntaFrequente.setIdProjeto(projeto.getId());
                
                pd.inserir(perguntaFrequente, projeto.getId());
                request.setAttribute("mensagemCadastraPerguntaFrequente","Cadastrado realizado com sucesso!");
            }
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroPerguntaFrequente.jsp");
            dis.forward(request, response);
            
        
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraGrupo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCadastraPerguntaFrequente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraGrupo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCadastraPerguntaFrequente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
