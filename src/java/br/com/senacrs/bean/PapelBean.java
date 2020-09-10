package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class PapelBean implements Serializable {
    
    private int id;
    private String funcao;
    private String descricao;
    private int idProjeto;
    private ArrayList<UsuarioBean> envolvidos = new ArrayList<UsuarioBean>();

    public PapelBean() {
        this.funcao = "";
        this.descricao = "";
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<UsuarioBean> getEnvolvidos() {
        return envolvidos;
    }

    public void setEnvolvidos(ArrayList<UsuarioBean> envolvidos) {
        this.envolvidos = envolvidos;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
}
