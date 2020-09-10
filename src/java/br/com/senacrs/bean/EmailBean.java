package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.Date;

public class EmailBean implements Serializable {
    
    private int id;
    private UsuarioBean remetente= new UsuarioBean();
    private String assunto;
    private String conteudo;
    private Date dataEnvio;
    private String destinatario;
    private int idProjeto;

    public EmailBean() {
        this.assunto = "";
        this.conteudo = "";
        destinatario="";
    }

    public String getDestinatario() {
        return destinatario;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsuarioBean getRemetente() {
        return remetente;
    }

    public void setRemetente(UsuarioBean remetente) {
        this.remetente = remetente;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
    
    
}
