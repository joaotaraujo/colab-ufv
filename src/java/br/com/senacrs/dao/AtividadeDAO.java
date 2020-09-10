package br.com.senacrs.dao;

import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.TarefaBean;
import java.util.ArrayList;

public interface AtividadeDAO {
    public void cadastrar(AtividadeBean atividade);
    public void deletar(AtividadeBean atividade); 
    public ArrayList<AtividadeBean> listarAtividades(int idProjeto); 
    public AtividadeBean buscar(String informacao, int tipo); 
    public void editar(AtividadeBean atividade); 
    public String retornaAtividadesTabela(ArrayList<AtividadeBean> atividades);
    public String retornarInformacoesAtividadeTabela(AtividadeBean atividade, String nomeProjeto);
    public void deletarEnvolvidoAtividade(UsuarioBean usuario, AtividadeBean atividade);
    public ArrayList<UsuarioBean> retornarEnvolvidosAtividade(AtividadeBean atividade);
    public void adicionarEnvolvidoAtividade(UsuarioBean usuario, AtividadeBean atividade);
    public ArrayList<UsuarioBean> retornarNaoEnvolvidosAtividade(AtividadeBean atividade, ProjetoBean projeto);
    public ArrayList<TarefaBean> retornarTarefasAtividade(AtividadeBean atividade);
    public String retornarAtividadeTabelaEditavel(AtividadeBean atividade, ProjetoBean projeto); 
    public String trocaData (String dataAnoMesDia);
    public ArrayList<AtividadeBean> retornarAtividadesEnvolvido(UsuarioBean usuario, ProjetoBean projeto);
     
    public ArrayList<AtividadeBean> retornaAtividadesPredecessorasPossiveis(ArrayList<AtividadeBean> atividadesProjeto, AtividadeBean atividadeSelecionada);
    
    public ArrayList<AtividadeBean> ordenaAtividades(ArrayList<AtividadeBean> atividades);
    

}
