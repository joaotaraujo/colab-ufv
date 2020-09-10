package br.com.senacrs.bean;

import java.io.Serializable;

public class PerguntaFrequenteBean implements Serializable {
    
    private int id;
    private String pergunta;
    private String resposta;
    private int idProjeto;

    public PerguntaFrequenteBean() {
        this.pergunta = "";
        this.resposta = "";
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

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
    
}
