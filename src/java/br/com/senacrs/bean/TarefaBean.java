package br.com.senacrs.bean;

import java.io.Serializable;    
import java.util.Date;
import java.util.ArrayList;

public class TarefaBean implements Serializable {
    
    private int id;
    private String nome;
    private Date dataInicio;
    private Date dataTermino;
    private String descricao;
    private UsuarioBean responsavel;
    private int atividade;
    private String complexidade;
    private String status;
    private int prioridade;
    private ArrayList<UsuarioBean> colaboradores = new ArrayList<UsuarioBean>();
    private ArrayList<ArtefatoBean> artefatos = new ArrayList<ArtefatoBean>();
    private int idTarefaAnterior;

    //usada apenas ao gerenciar o cronograma
    private int posicaoCronograma;
    
    public TarefaBean() {
        this.nome = "";
        this.descricao = "";
        this.complexidade = "";
        this.status = "";
    }

    public int getPosicaoCronograma() {
        return posicaoCronograma;
    }

    public void setPosicaoCronograma(int posicaoCronograma) {
        this.posicaoCronograma = posicaoCronograma;
    }

    public int getIdTarefaAnterior() {
        return idTarefaAnterior;
    }

    public void setIdTarefaAnterior(int idTarefaAnterior) {
        this.idTarefaAnterior = idTarefaAnterior;
    }

    public UsuarioBean getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UsuarioBean responsavel) {
        this.responsavel = responsavel;
    }

    public ArrayList<ArtefatoBean> getArtefatos() {
        return artefatos;
    }

    public void setArtefatos(ArrayList<ArtefatoBean> artefatos) {
        this.artefatos = artefatos;
    }

    public String getComplexidade() {
        return complexidade;
    }

    public void setComplexidade(String complexidade) {
        this.complexidade = complexidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getAtividade() {
        return atividade;
    }

    public void setAtividade(int atividade) {
        this.atividade = atividade;
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

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public ArrayList<UsuarioBean> getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(ArrayList<UsuarioBean> colaboradores) {
        this.colaboradores = colaboradores;
    }
    
    
}
