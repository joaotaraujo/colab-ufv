package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ForumBean implements Serializable {
    
    private int id;
    private ArrayList<TopicoBean> topicos = new ArrayList<TopicoBean>();
    private int idProjeto;
    
    public ForumBean() {
    }

    public int getId() {
        return id;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<TopicoBean> getTopicos() {
        return topicos;
    }

    public void setTopicos(ArrayList<TopicoBean> topicos) {
        this.topicos = topicos;
    }
    
}
