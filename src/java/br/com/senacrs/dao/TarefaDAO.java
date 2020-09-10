package br.com.senacrs.dao;

import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.TarefaBean;
import java.util.ArrayList;
import java.util.List;

public interface TarefaDAO {
    
    public void cadastrar(TarefaBean tarefa);
    public void deletar(int idTarefa);
    public List<TarefaBean> listarTarefas(); 
            
    /*Posso buscar por:
        (1) nome
        (2) id
    */
    public TarefaBean buscar(String informacao, int tipo); 
    public void editar(TarefaBean tarefa); 
    public String retornaTarefasTabela(ArrayList<TarefaBean> tarefas);
    public String retornarInformacoesTarefaTabela(TarefaBean tarefa, String nomeAtividade); 
    public void deletarEnvolvidoTarefa(UsuarioBean usuario, TarefaBean tarefa);
    public ArrayList<UsuarioBean> retornarEnvolvidosTarefa(TarefaBean tarefa);
    public void adicionarEnvolvidoTarefa(UsuarioBean usuario, TarefaBean tarefa, AtividadeBean atividade);
    
    public ArrayList<UsuarioBean> retornarNaoEnvolvidosTarefa(TarefaBean tarefa, ProjetoBean projeto);
    public int quantTarefas(); 
    public String retornarTarefaTabelaEditavel(TarefaBean tarefa, String nomeProjeto); 
    public ArrayList<UsuarioBean> retornaEnvolvidosTarefa(String nomeTarefa, String nomeAtividade, String nomeProjeto);
    public String trocaData (String dataAnoMesDia);
    
    public ArrayList<TarefaBean> retornarTarefasEnvolvido(UsuarioBean usuario,  ArrayList<AtividadeBean> atividadesProjeto);

    public ArrayList<TarefaBean> ordenaTarefas(ArrayList<TarefaBean> tarefas);
    public ArrayList<TarefaBean> retornaTarefasPredecessorasPossiveis(ArrayList<TarefaBean> tarefasAtividade, TarefaBean tarefaSelecionada);
    
    
    
}
