package br.com.senacrs.dao;

import br.com.senacrs.bean.ProjetoBean;
import java.util.ArrayList;
import br.com.senacrs.bean.UsuarioBean;

public interface UsuarioDAO {
    

    /*tipoUsuario:
      1- Aluno
      2- Professor  
    */
    public void cadastrar(UsuarioBean usuario, int tipoUsuario); 
    public void deletar(String nome); 
    public ArrayList<UsuarioBean> listarAlunos(String nomeProjeto); 
    public ArrayList<UsuarioBean> listarProfessores(String nomeProjeto); 
            
    /*Posso buscar por:
        (1) nome
        (2) matricula
        (3) login
        (4) email
        (5) id
    */
    public UsuarioBean buscar(String informacao, int tipo);
    public void editar(UsuarioBean usuario); 
            
    /* Redirect
        1- grupo
        2- tarefa
        3- atividade
        4- papel
        5- projeto
        6- relatorio
    */
    public String retornarUsuarioTabela(UsuarioBean usuario, int redirectPage, String informacao);
    
    /* Informacao:
        1 - icone de remover remove um envolvido na atividade
        2 - icone de remover remove um envolvido na tarefa
        3 - icone de remover remove um envolvido no grupo
        4 - icone de remover remove um envolvido no papel
    */
    public String retornaTabelaDeEnvolvidos(ArrayList<UsuarioBean> envolvidos, int idProjeto, int informacao);
    public String retornarUsuarioTabelaEditavel(UsuarioBean usuario);
    public void validarAtividadesETarefas(UsuarioBean usuario, ProjetoBean projeto);
    
    
}