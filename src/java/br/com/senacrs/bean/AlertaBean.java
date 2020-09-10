package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

public class AlertaBean implements Serializable {
    
    private int id;
    private ArrayList<UsuarioBean> destinatarios = new ArrayList<>();
    private String descricao;
    private Date dataEmissao;
    private UsuarioBean autor = new UsuarioBean();
    private boolean foiVisto;
    private int idProjeto;

    public AlertaBean() {
        this.descricao = "";
        this.foiVisto = false;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public boolean isFoiVisto() {
        return foiVisto;
    }

    public void setFoiVisto(boolean foiVisto) {
        this.foiVisto = foiVisto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<UsuarioBean> getDestinatarios() {
        return destinatarios;
    }

    public UsuarioBean getAutor() {
        return autor;
    }

    public void setAutor(UsuarioBean autor) {
        this.autor = autor;
    }

    public void setDestinatarios(ArrayList<UsuarioBean> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
    
}
