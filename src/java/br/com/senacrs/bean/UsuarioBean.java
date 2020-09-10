package br.com.senacrs.bean;

import java.io.Serializable;

public class UsuarioBean implements Serializable {
    
    private int id;
    private String nome;
    private String matricula;
    private String email;
    private String login;
    private String senha;
    private String tipoUsuario;
   
    //usadas somente no relatório
    private int numAtividadesFeitas;
    private int numTarefasFeitas;
    private int numTopicosCriados;
    private int numComentariosFeitos;
    private int numMensagensEnviadas;
    private int numAlertasEnviados;
    
    public UsuarioBean (){
        nome ="";
        matricula="";
        email="";
        login="";
        senha="";
        tipoUsuario="";
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

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
