package br.com.senacrs.bean;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class ArtefatoBean implements Serializable {
    
    private int id;
    private int idAtividade;
    private UsuarioBean remetente;
    private String descricao;
    private String nome;
    private Date dataPost;
    private File arquivo;

    public ArtefatoBean() {
        this.descricao = "";
        this.nome = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(int idAtividade) {
        this.idAtividade = idAtividade;
    }

    public UsuarioBean getRemetente() {
        return remetente;
    }

    public void setRemetente(UsuarioBean remetente) {
        this.remetente = remetente;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataPost() {
        return dataPost;
    }

    public void setDataPost(Date dataPost) {
        this.dataPost = dataPost;
    }

    public File getArquivo() {
        return arquivo;
    }

    public void setArquivo(File arquivo) {
        this.arquivo = arquivo;
    }

    
}
