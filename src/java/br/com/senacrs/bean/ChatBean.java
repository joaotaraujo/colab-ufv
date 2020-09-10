package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatBean implements Serializable {
    
    private int id;
    private int idProjeto;
    private ArrayList<MensagemBean> mensagensEnviadas = new ArrayList<>();

    public ChatBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public ArrayList<MensagemBean> getMensagensEnviadas() {
        return mensagensEnviadas;
    }

    public void setMensagensEnviadas(ArrayList<MensagemBean> mensagensEnviadas) {
        this.mensagensEnviadas = mensagensEnviadas;
    }
    
}
