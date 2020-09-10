package br.com.senacrs.dao;

import br.com.senacrs.bean.PapelBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import java.util.ArrayList;

public interface PapelDAO {
    public void cadastrar(PapelBean papel, int idProjeto);
    public void deletar(int idPapel);
    public ArrayList<PapelBean> listarPapeis(String nomeProjeto);
    /*Se tipo:
        - 1: pesquisa por funcao
        - 2: pesquisa por id
    */
    public PapelBean buscar(String informacao, int tipo);
    public void editar(PapelBean papel);
    public ArrayList<UsuarioBean> listarEnvolvidosPapel(int idPapel);
    public String retornaTabelaDePapeis(String nomeProjeto);
    
    public String retornarPapelTabela(PapelBean papel, String nomeProjeto);
    public ArrayList<UsuarioBean> retornaNaoEnvolvidosPapel(PapelBean papel, String nomeProjeto);
    public String retornarPapelTabelaEditavel(PapelBean papel, String nomeProjeto);
    public String cadastrarUsuarioEmPapel(UsuarioBean usuario, PapelBean papel, ProjetoBean projeto);
    public void deletarUsuarioPapel(UsuarioBean usuario, PapelBean papel);
    public ArrayList<UsuarioBean> retornarUsuariosSemPapel();
    public boolean usuarioEstaEmPapel(String nomeUsuario, String funcaoPapel, String nomeProjeto);
}
