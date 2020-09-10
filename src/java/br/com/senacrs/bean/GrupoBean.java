package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class GrupoBean implements Serializable {
    
    private int id;
    private String nome;
    private String descricao;
    private ArrayList<UsuarioBean> integrantes = new ArrayList<UsuarioBean>(); 
    private int idProjeto;

    //usadas somente no relatório
    private int numAtividadesFeitas;
    private int numTarefasFeitas;
    private int numTopicosCriados;
    private int numComentariosFeitos;
    private int numMensagensEnviadas;
    private int numAlertasEnviados;
    
    public GrupoBean() {
        this.nome = "";
        this.descricao = "";
    }

    public int getNumAtividadesFeitas() {
        return numAtividadesFeitas;
    }

    public void setNumAtividadesFeitas(int numAtividadesFeitas) {
        this.numAtividadesFeitas = numAtividadesFeitas;
    }

    public int getNumTarefasFeitas() {
        return numTarefasFeitas;
    }

    public void setNumTarefasFeitas(int numTarefasFeitas) {
        this.numTarefasFeitas = numTarefasFeitas;
    }

    public int getNumTopicosCriados() {
        return numTopicosCriados;
    }

    public void setNumTopicosCriados(int numTopicosCriados) {
        this.numTopicosCriados = numTopicosCriados;
    }

    public int getNumComentariosFeitos() {
        return numComentariosFeitos;
    }

    public void setNumComentariosFeitos(int numComentariosFeitos) {
        this.numComentariosFeitos = numComentariosFeitos;
    }

    public int getNumMensagensEnviadas() {
        return numMensagensEnviadas;
    }

    public void setNumMensagensEnviadas(int numMensagensEnviadas) {
        this.numMensagensEnviadas = numMensagensEnviadas;
    }

    public int getNumAlertasEnviados() {
        return numAlertasEnviados;
    }

    public void setNumAlertasEnviados(int numAlertasEnviados) {
        this.numAlertasEnviados = numAlertasEnviados;
    }

    public ArrayList<UsuarioBean> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(ArrayList<UsuarioBean> integrantes) {
        this.integrantes = integrantes;
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

}
