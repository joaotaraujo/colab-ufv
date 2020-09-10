package br.com.senacrs.servlet;

import br.com.senacrs.bean.PerguntaFrequenteBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.PerguntaFrequenteDAO;
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

public class ServletCarregaEditaPerguntaFrequente extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
            String idPergunta = (String) request.getParameter("idPerguntaFrequenteEscolhida");
            
            sessao.setAttribute("idPerguntaFrequenteEscolhida", idPergunta);
            
            PerguntaFrequenteDAO pd=DAOFactory.createPerguntaFrequenteDAO();
            PerguntaFrequenteBean perguntaFrequente = pd.buscar(idPergunta,2);

            String perguntaFrequenteStringTabela = "<form method=\"post\" action=\"/ProjetoColabUFV/ServletEditaPerguntaFrequente\"><table border=\"1\" cellpadding=\"5\"><thead><tr><th COLSPAN=\"4\"><h3><center>Informações da pergunta frequente</center></h3></th>"
                    +"</tr></thead><tbody><tr><td WIDTH=\"15%\"><strong>Pergunta:</strong></td><td WIDTH=\"85%\"><input size=\"74\" type=\"text\" name=\"perguntaEditada\" value=\""
                    +perguntaFrequente.getPergunta()+" \"></td><tr height=85><td COLSPAN=\"1\"><strong>Resposta:</strong></td><td COLSPAN=\"3\"><input style=\"height: 97px;\" size=\"74\" type=\"text\" name=\"respostaEditada\" value=\""
                    +perguntaFrequente.getResposta()+"\"></td>"
                    +"</tr></tbody></table>"
                    + "<br>"
                    + "<button style=\"width: 150px\" type=\"submit\" name=\"Submit\" value=\"Concluir editar grupo\">Concluir edição</button>"    
                    +"</form><br>";
            sessao.setAttribute("informacoesPerguntaFrequente", perguntaFrequenteStringTabela);
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarInformacoesPerguntaFrequente.jsp");
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
