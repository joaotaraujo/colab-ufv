package br.com.senacrs.dao;

import br.com.senacrs.bean.TopicoBean;
import java.util.ArrayList;

public interface TopicoDAO {
    public void cadastrarTopico(TopicoBean topico);
    public void deletarTopico(int idTopico);
    public ArrayList<TopicoBean> listarTopicos(int idForum);
    
    /*Se tipo:
        - 1: pesquisa por titulo
        - 2: pesquisa por id
    */
    public TopicoBean buscar(String informacao, int tipo);
    public void editar(TopicoBean topico);
    public String retornaTopicosTabela(ArrayList<TopicoBean> topicos, String nomeUsuarioLogado);
    public String retornarInformacaoTopicoTabela(TopicoBean topico, String nomeUsuarioLogado);
   
}
