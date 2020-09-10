package br.com.senacrs.bean;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProjetoBean implements Serializable {
    
    private int id;
    private String nome;
    private String descricao;
    private Date dataInicio;
    private Date dataTermino;
    private int idAutor;

    private ArrayList<UsuarioBean> envolvidos = new ArrayList<UsuarioBean>();
    private ArrayList<GrupoBean> grupos = new ArrayList<GrupoBean>();
    private ArrayList<ConteudoBean> listaConteudo = new ArrayList<ConteudoBean>();
    private ArrayList<PerguntaFrequenteBean> perguntasFrequentes = new ArrayList<PerguntaFrequenteBean>();
    private ArrayList<AtividadeBean> atividades = new ArrayList<AtividadeBean>();
    private ForumBean forum = new ForumBean();
    private ChatBean chat = new ChatBean();
    private ArrayList<PapelBean> papeis = new ArrayList<PapelBean>();
    
    public ProjetoBean (){
        nome ="";
        descricao="";
    }

    public ArrayList<PapelBean> getPapeis() {
        return papeis;
    }

    public void setPapeis(ArrayList<PapelBean> papeis) {
        this.papeis = papeis;
    }

    public ArrayList<UsuarioBean> getEnvolvidos() {
        return envolvidos;
    }

    public void setEnvolvidos(ArrayList<UsuarioBean> envolvidos) {
        this.envolvidos = envolvidos;
    }

    public ArrayList<GrupoBean> getGrupos() {
        return grupos;
    }

    public void setGrupos(ArrayList<GrupoBean> grupos) {
        this.grupos = grupos;
    }

    public ArrayList<ConteudoBean> getListaConteudo() {
        return listaConteudo;
    }

    public void setListaConteudo(ArrayList<ConteudoBean> listaConteudo) {
        this.listaConteudo = listaConteudo;
    }

    public ArrayList<PerguntaFrequenteBean> getPerguntasFrequentes() {
        return perguntasFrequentes;
    }

    public void setPerguntasFrequentes(ArrayList<PerguntaFrequenteBean> perguntasFrequentes) {
        this.perguntasFrequentes = perguntasFrequentes;
    }

    public ArrayList<AtividadeBean> getAtividades() {
        return atividades;
    }

    public void setAtividades(ArrayList<AtividadeBean> atividades) {
        this.atividades = atividades;
    }

    public ForumBean getForum() {
        return forum;
    }

    public void setForum(ForumBean forum) {
        this.forum = forum;
    }

    public ChatBean getChat() {
        return chat;
    }

    public void setChat(ChatBean chat) {
        this.chat = chat;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
    
     public String convertStringToDate(Date indate)
{
   String dateString = null;
   SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
   /*you can also use DateFormat reference instead of SimpleDateFormat 
    * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
    */
   try{
	dateString = sdfr.format( indate );
   }catch (Exception ex ){
	System.out.println(ex);
   }
   return dateString;
}
    
}
