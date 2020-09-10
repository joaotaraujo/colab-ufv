package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.Date;

public class ComentarioBean implements Serializable {
    
    private int id;
    private String conteudo;
    private UsuarioBean autor;
    private Date dataComentario;
    private int idTopico;

    public ComentarioBean() {
        this.conteudo = "";
    }

    public int getIdTopico() {
        return idTopico;
    }

    public void setIdTopico(int idTopico) {
        this.idTopico = idTopico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public UsuarioBean getAutor() {
        return autor;
    }

    public void setAutor(UsuarioBean autor) {
        this.autor = autor;
    }

    public Date getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(Date dataComentario) {
        this.dataComentario = dataComentario;
    }
    
}
