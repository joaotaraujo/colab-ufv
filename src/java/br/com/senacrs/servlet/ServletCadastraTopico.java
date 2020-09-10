package br.com.senacrs.servlet;

import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.TopicoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.TopicoDAO;
import br.com.senacrs.dao.UsuarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ServletCadastraTopico extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
            HttpSession sessao = request.getSession();
            
            UsuarioDAO ud=DAOFactory.createUsuarioDAO();
            
            
            TopicoDAO topd=DAOFactory.createTopicoDAO();
            
            TopicoBean topicoExiste = null;
             
            
            topicoExiste = topd.buscar(request.getParameter("titulo"),1);
            if(topicoExiste!=null){
                request.setAttribute("mensagemCadastraTopico","Este topico já existe!");
            }  
            else{
                TopicoBean topico = new TopicoBean();
                
                topico.setDescricao(request.getParameter("descricao"));
                topico.setTitulo(request.getParameter("titulo"));
                topico.setAutor(ud.buscar((String) sessao.getAttribute("nomeUsuario"), 1));
                
                
                String projetoLogado = (String) sessao.getAttribute("projetoEscolhido");
                ProjetoDAO prod = DAOFactory.createProjetoDAO();
                ProjetoBean projeto = prod.pesquisarProjeto(projetoLogado,1);
                
                topico.setIdForum(projeto.getForum().getId());
                
                Date data = new Date(System.currentTimeMillis());  
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String dataAtualString = formato.format(data);
                Date dataAtual = formato.parse((String)dataAtualString);
                topico.setDataCriacao(dataAtual);
                
                topd.cadastrarTopico(topico);
                request.setAttribute("mensagemCadastraTopico","Cadastrado realizado com sucesso!");
            }
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/cadastros/cadastroTopico.jsp");
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
