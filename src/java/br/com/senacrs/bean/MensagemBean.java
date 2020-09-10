package br.com.senacrs.bean;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class MensagemBean implements Serializable {
    
    private int id;
    private Date dataEnvio;
    private String horaEnvio;
    private UsuarioBean remetente;
    private String descricao;
    private int idChat;

    public MensagemBean() {
        this.descricao = "";
    }

    public int getId() {
        return id;
    }

    public int getIdChat() {
        return idChat;
    }

    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getHoraEnvio() {
        return horaEnvio;
    }

    public void setHoraEnvio(String horaEnvio) {
        this.horaEnvio = horaEnvio;
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
    
}