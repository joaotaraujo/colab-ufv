package br.com.senacrs.servlet;


import br.com.senacrs.bean.EmailBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.EmailDAO;
import br.com.senacrs.dao.UsuarioDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletEnviaEmail extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            HttpSession sessao = request.getSession();
            response.setContentType("text/html");
            
            String autor = (String) sessao.getAttribute("nomeUsuario");
            String assunto = request.getParameter("assunto");
            String descricao = request.getParameter("descricao");
            String destinatario = request.getParameter("destinatario");

            EmailDAO emd = DAOFactory.createEmailDAO();
            EmailBean emailBd = new EmailBean();
            
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            emailBd.setRemetente(ud.buscar(autor, 1));
            
            emailBd.setConteudo(descricao);
            emailBd.setAssunto(assunto);
            emailBd.setDestinatario(destinatario);
            emailBd.setIdProjeto(Integer.parseInt((String)sessao.getAttribute("idProjetoEscolhido")));
            
            emd.armazenarEmail(emailBd);
            
            Properties props = new Properties();
            /** Parâmetros de conexão com servidor Gmail */
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {
                             protected PasswordAuthentication getPasswordAuthentication()
                             {
                                   return new PasswordAuthentication("colabufv@gmail.com", "colab123");
                             }
                        });

            /** Ativa Debug para sessão */
            session.setDebug(true);

            try {

                  Message message = new MimeMessage(session);
                  message.setFrom(new InternetAddress("ufvcolab@gmail.com")); //Remetente

                  Address[] toUser = InternetAddress //Destinatário(s)
                             .parse(destinatario);  

                  message.setRecipients(Message.RecipientType.TO, toUser);
                  message.setSubject(assunto);//Assunto
                  message.setText("Enviada por "+autor+": " + descricao);
                  /**Método para enviar a mensagem criada*/
                  Transport.send(message);

                  String alert = "<script>window.alert(\"Email enviado com sucesso!\")</script>";
                  sessao.setAttribute("emailEnviado",alert);

                  RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/envios/enviarEmail.jsp");
                  dis.forward(request, response);

             } catch (MessagingException e) {
                  String alert = "<script>window.alert(\"Email não foi enviado, verifique os campos!\")</script>";
                  sessao.setAttribute("emailEnviado",alert);

                  RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/envios/enviarEmail.jsp");
                  dis.forward(request, response);
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
