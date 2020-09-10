package br.com.senacrs.servlet;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.UsuarioDAO;
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

public class ServletCarregaAlunos extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException{
            
            HttpSession sessao = request.getSession();
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
        
            response.setContentType("text/html");
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            ArrayList<UsuarioBean> alunos =  ud.listarAlunos(nomeProjetoEscolhido); 
            String alunosStringTabela = this.retornaTabelaDeAlunos(alunos, Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")));
            request.setAttribute("alunos",alunosStringTabela);
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarAlunosParaRelatorios.jsp");
            dis.forward(request, response);
    }
        
    
    public String retornaTabelaDeAlunos(ArrayList<UsuarioBean> envolvidos, int idProjeto){
        
            String usuariosString = "";
            ProjetoDAO projd = DAOFactory.createProjetoDAO();
            
            usuariosString = "<table id=\"usuariosTabela\" class=\"display\" cellspacing=\"0\" width=\"100%\"><thead><tr><th>Nome</th><th>Papel</th><th>Email</th>"
                         +"</tr></thead><tbody>";
            for(int i=0;i<envolvidos.size();i++){
               
                
                usuariosString = usuariosString + "<tr>"
                        + "<td><a href=\"/ProjetoColabUFV/ServletCarregaRelatorioAluno?alunoParaCarregar="+envolvidos.get(i).getNome()+"\"> "+envolvidos.get(i).getNome()+"</a> </td>"
                        +"<td><a href=\"/ProjetoColabUFV/ServletCarregaRelatorioAluno?alunoParaCarregar="+envolvidos.get(i).getNome()+"\"> "+projd.retornaPapelUsuarioEmProjeto(envolvidos.get(i), idProjeto)+"</a></td>"
                        + "<td><a href=\"/ProjetoColabUFV/ServletCarregaRelatorioAluno?alunoParaCarregar="+envolvidos.get(i).getNome()+"\"> "+envolvidos.get(i).getEmail()+" </a></td></tr>";
                    
            }
            usuariosString = usuariosString + "</tbody></table>";
            
            return usuariosString;
    
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraProjeto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCarregaGruposProjeto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCadastraProjeto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletCarregaGruposProjeto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
