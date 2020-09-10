package br.com.senacrs.servlet;

import br.com.senacrs.bean.AlertaBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AlertaDAO;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.UsuarioDAO;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletEnviaAlerta extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
           
        response.setContentType("text/html;charset=UTF-8");
        HttpSession sessao = request.getSession();
        
        String projetoEscolhido = (String) sessao.getAttribute("projetoEscolhido");
            
        ProjetoDAO projd=DAOFactory.createProjetoDAO();
            
        ArrayList<UsuarioBean> usuariosEnvolvidosProjeto = projd.listarEnvolvidosNoProjeto(projetoEscolhido);
        String opcoesUsuarios ="";
        for(int i=0; i<usuariosEnvolvidosProjeto.size();i++){
            opcoesUsuarios = opcoesUsuarios+"<option>"+usuariosEnvolvidosProjeto.get(i).getNome()+"</option>";
        }
        
        sessao.setAttribute("envolvidos", opcoesUsuarios);
        
        String descricaoAlerta = request.getParameter("descricao");
        
        //Pega envolvidos do select box para enviar alerta
        UsuarioDAO ud = DAOFactory.createUsuarioDAO();
        ArrayList<UsuarioBean> destinatariosAlerta = new ArrayList<UsuarioBean>();
        int i=0;
        String[] assignedResources = request.getParameterValues("frmSelectedResources");
        if (assignedResources != null) {
            for(String item: assignedResources){
                String keyValue[]= item.split(" ");
                destinatariosAlerta.add(ud.buscar(keyValue[0],1));
                i++;
            }
        }
        
        
        AlertaBean alerta = new AlertaBean();
        
        alerta.setDescricao(descricaoAlerta);
        alerta.setDestinatarios(destinatariosAlerta);
        
	Date date = new Date();
	alerta.setDataEmissao(date);
        
        alerta.setFoiVisto(false);
        alerta.setAutor(ud.buscar((String) sessao.getAttribute("nomeUsuario"), 1));
        alerta.setIdProjeto(projd.pesquisarProjeto(projetoEscolhido, 1).getId());
        
        AlertaDAO alertd = DAOFactory.createAlertaDAO();
        alertd.enviarAlerta(alerta);
        
        RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/envios/enviarAlerta.jsp");
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
