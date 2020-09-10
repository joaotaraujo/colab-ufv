package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.Date;

public class ConteudoBean implements Serializable {
    
    private int id;
    private UsuarioBean remetente = new UsuarioBean();
    private String descricao;
    private String nome;
    private Date dataPost;
    private byte[] conteudoArquivo;
    private int idProjeto;

    public ConteudoBean() {
        this.descricao = "";
        this.nome = "";
    }

    public UsuarioBean getRemetente() {
        return remetente;
    }

    public byte[] getConteudoArquivo() {
        return conteudoArquivo;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public void setConteudoArquivo(byte[] conteudoArquivo) {
        this.conteudoArquivo = conteudoArquivo;
    }

    public void setRemetente(UsuarioBean remetente) {
        this.remetente = remetente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataPost() {
        return dataPost;
    }

    public void setDataPost(Date dataPost) {
        this.dataPost = dataPost;
    }
    
}
