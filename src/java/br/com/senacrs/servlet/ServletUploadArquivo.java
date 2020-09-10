package br.com.senacrs.servlet;


import br.com.senacrs.bean.ArtefatoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.ArtefatoDAO;
import br.com.senacrs.dao.ConteudoDAO;
import br.com.senacrs.dao.UsuarioDAO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
 
@WebServlet(name = "ServletUploadArquivo", urlPatterns = {"/upload"})
@MultipartConfig
public class ServletUploadArquivo extends HttpServlet {

    private final static Logger LOGGER = 
            Logger.getLogger(ServletUploadArquivo.class.getCanonicalName());
 
    
    protected void processRequest(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");

    // Create path components to save the file
    final String path = request.getParameter("destination");
    final Part filePart = request.getPart("file");
    final String fileName = getFileName(filePart);

    OutputStream out = null;
    InputStream filecontent = null;
    final PrintWriter writer = response.getWriter();

    try {
        out = new FileOutputStream(new File(path + File.separator
                + fileName));
        filecontent = filePart.getInputStream();

        File arquivo = new File(path + File.separator
                + fileName);
        
        ArtefatoDAO artd = DAOFactory.createArtefatoDAO();
        ArtefatoBean artefato = new ArtefatoBean();
        
        artefato.setArquivo(arquivo);
        
        Date data = new Date(System.currentTimeMillis());  
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        artefato.setDataPost(data);
        
        artefato.setDescricao("teste");
        artefato.setIdAtividade(0);
        artefato.setNome(arquivo.getName());
        
        UsuarioDAO ud = DAOFactory.createUsuarioDAO();
        artefato.setRemetente(ud.buscar("joao", 1));
        
        artd.armazenaArtefato(artefato);
        
        int read = 0;
        final byte[] bytes = new byte[1024];

        while ((read = filecontent.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        writer.println("New file " + fileName + " created at " + path);
        LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", 
                new Object[]{fileName, path});
    } catch (FileNotFoundException fne) {
        writer.println("You either did not specify a file to upload or are "
                + "trying to upload a file to a protected or nonexistent "
                + "location.");
        writer.println("<br/> ERROR: " + fne.getMessage());

        LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", 
                new Object[]{fne.getMessage()});
    } finally {
        if (out != null) {
            out.close();
        }
        if (filecontent != null) {
            filecontent.close();
        }
        if (writer != null) {
            writer.close();
        }
    }
}

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    
     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override   
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    
}