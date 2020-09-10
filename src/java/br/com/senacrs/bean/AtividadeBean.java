package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

public class AtividadeBean implements Serializable {
    
    private int id;
    private String nome;
    private Date dataInicio;
    private Date dataTermino;
    private String descricao;
    private int projeto;
    private UsuarioBean responsavel;
    private String complexidade;
    private String status;
    private int prioridade;
    private ArrayList<UsuarioBean> colaboradores = new ArrayList<UsuarioBean>();
    private ArrayList<TarefaBean> tarefas = new ArrayList<TarefaBean>();
    private ArrayList<String> artefatos = new ArrayList<String>();
    private int idAtividadeAnterior;
    
    //usada apenas ao gerenciar o cronograma
    private int posicaoCronograma;
    
    public AtividadeBean() {

        this.nome = "";
        this.descricao = "";
        this.complexidade = "";
        this.status = "";
    }

    public int getIdAtividadeAnterior() {
        return idAtividadeAnterior;
    }

    public void setIdAtividadeAnterior(int idAtividadeAnterior) {
        this.idAtividadeAnterior = idAtividadeAnterior;
    }


    public int getPosicaoCronograma() {
        return posicaoCronograma;
    }

    public void setPosicaoCronograma(int posicaoCronograma) {
        this.posicaoCronograma = posicaoCronograma;
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

    public int getProjeto() {
        return projeto;
    }

    public void setProjeto(int projeto) {
        this.projeto = projeto;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<UsuarioBean> getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(ArrayList<UsuarioBean> colaboradores) {
        this.colaboradores = colaboradores;
    }

    public ArrayList<String> getArtefatos() {
        return artefatos;
    }

    public void setArtefatos(ArrayList<String> artefatos) {
        this.artefatos = artefatos;
    }

    public UsuarioBean getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UsuarioBean responsavel) {
        this.responsavel = responsavel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<UsuarioBean> getAlunosEnvolvidos() {
        return colaboradores;
    }

    public void setAlunosEnvolvidos(ArrayList<UsuarioBean> alunosEnvolvidos) {
        this.colaboradores = alunosEnvolvidos;
    }

    public ArrayList<TarefaBean> getTarefas() {
        return tarefas;
    }

    public void setTarefas(ArrayList<TarefaBean> tarefas) {
        this.tarefas = tarefas;
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

    
}
