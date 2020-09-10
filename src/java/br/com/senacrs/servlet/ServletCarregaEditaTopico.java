package br.com.senacrs.servlet;

import br.com.senacrs.bean.TopicoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.TopicoDAO;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletCarregaEditaTopico extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            String idTopico = (String) request.getParameter("idTopicoEscolhido");
            
            sessao.setAttribute("idTopicoEscolhido", idTopico);
            
            TopicoDAO topd=DAOFactory.createTopicoDAO();
            TopicoBean topico = topd.buscar(idTopico,2);

            String topicoStringTabela = "<form method=\"post\" action=\"/ProjetoColabUFV/ServletEditaTopico\"><table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações do tópico</center></h3></th>"
                    +"</tr></thead><tbody><tr><td WIDTH=\"10%\">Título</td><td WIDTH=\"40%\"><input size=\"80\" type=\"text\" name=\"tituloEditado\" value=\""
                    +topico.getTitulo()+" \"></td><tr height=83><td COLSPAN=\"1\">Descrição</td><td BGCOLOR=\"WHITE\" COLSPAN=\"3\"><input style=\"height: 90px;\" size=\"80\" type=\"text\" name=\"descricaoEditada\" value=\""
                    +topico.getDescricao()+"\"></td>"
                    +"</tr></tbody></table>"
                    + "<br>"
                    + "<button style=\"width: 150px\" type=\"submit\" name=\"Submit\" value=\"Concluir editar topico\">Concluir edição</button>"    
                    +"</form><br><form method=\"post\" action=\"/ProjetoColabUFV/ServletCarregaInformacoesTopico?topicoParaCarregar="+topico.getTitulo()+"\">"
                    +"       <button type=\"submit\" name=\"Submit\" value=\"voltar\">Voltar</button></form>";
            sessao.setAttribute("informacoesTopico", topicoStringTabela);
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/edicoes/editarTopico.jsp");
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
