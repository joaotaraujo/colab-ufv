package br.com.senacrs.servlet;

import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AtividadeDAO;
import java.io.IOException;
import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletEditaAtividade extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
          
            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            String idAtividadeEscolhida = (String) sessao.getAttribute("idAtividadeEscolhida");
            
            AtividadeDAO ad=DAOFactory.createAtividadeDAO();
            AtividadeBean atividade = ad.buscar(idAtividadeEscolhida, 2);
            AtividadeBean atividadeEditada = new AtividadeBean();
            
            atividadeEditada.setNome(request.getParameter("nomeEditado"));
            
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            String data = request.getParameter("dataInicioEditada");
            Date dataInicio = (Date) formato.parse(data);
            data = request.getParameter("dataTerminoEditada");
            Date dataTermino = (Date) formato.parse(data);
            atividadeEditada.setDataInicio(dataInicio);
            atividadeEditada.setDataTermino(dataTermino);
            
           
            atividadeEditada.setDescricao(request.getParameter("descricaoEditada"));
            atividadeEditada.setComplexidade(request.getParameter("complexidadeEditada"));
            atividadeEditada.setPrioridade(Integer.parseInt(request.getParameter("prioridadeEditada")));
            atividadeEditada.setStatus(request.getParameter("statusEditado"));
            
            atividadeEditada.setProjeto(atividade.getProjeto());
            atividadeEditada.setResponsavel(atividade.getResponsavel());
            atividadeEditada.setId(Integer.parseInt(idAtividadeEscolhida));
            
            
            String nomeAtividadeAnterior = request.getParameter("atividadeAnteriorEditada");
            if(nomeAtividadeAnterior.equalsIgnoreCase("nenhuma"))
                atividadeEditada.setIdAtividadeAnterior(-1);
            else
                atividadeEditada.setIdAtividadeAnterior(ad.buscar(nomeAtividadeAnterior,1).getId());
            
            ad.editar(atividadeEditada);
            
            String atividadesStringTabela =  ad.retornarInformacoesAtividadeTabela(atividadeEditada, (String) sessao.getAttribute("projetoEscolhido"));
            sessao.setAttribute("informacoesAtividade", atividadesStringTabela);
            
            sessao.setAttribute("atividadeEscolhida", atividadeEditada.getNome());
            
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
            Logger.getLogger(ServletEditaAtividade.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletEditaAtividade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
