package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class TopicoBean implements Serializable {
    
    private int id;
    private String titulo;
    private String descricao;
    private UsuarioBean autor;
    private Date dataCriacao;
    private ArrayList<ComentarioBean> listaComentarios = new ArrayList<ComentarioBean>();
    private int idForum;

    public TopicoBean() {
        this.titulo = "";
        this.descricao = "";
    }

    public int getIdForum() {
        return idForum;
    }

    public void setIdForum(int idForum) {
        this.idForum = idForum;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public UsuarioBean getAutor() {
        return autor;
    }

    public void setAutor(UsuarioBean autor) {
        this.autor = autor;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public ArrayList<ComentarioBean> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(ArrayList<ComentarioBean> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }
    
}
