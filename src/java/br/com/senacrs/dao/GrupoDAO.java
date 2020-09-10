package br.com.senacrs.dao;

import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import java.util.ArrayList;

public interface GrupoDAO {
    
    public void cadastrarGrupo(GrupoBean grupo, String nomeProjeto);
    public void deletarGrupo(String nome); 
    public ArrayList<GrupoBean> listarGrupos(String NomeProjeto);
    /*tipo:
        1- por nome
        2- por id
    */
    public GrupoBean pesquisarGrupo(String nomeGrupo, String nomeProjeto, int tipo); 
    public void editarGrupo(GrupoBean grupo);
    /* Se redirect:
        redirectProjeto - veio de projetos
        redirectGrupos - veio de grupos
    */
    public String retornarGrupoTabela(GrupoBean grupo, String nomeProjeto, String redirect);
    public String retornaProjetoGrupo(GrupoBean grupo);
    
    /* Informacao:
        1- deleta e vai pra tela de grupos
        2- deleta e vai pra tela de informacoes do projeto
    */
    public String retornaGruposTabela(ArrayList<GrupoBean> grupos, int informacao);
    public String retornarGrupoTabelaEditavel(GrupoBean grupo, String nomeProjeto, String redirect);
    public String cadastrarUsuarioEmGrupo(UsuarioBean usuario, GrupoBean grupo, ProjetoBean projeto);
    public ArrayList<UsuarioBean> listarIntegrantes(int idGrupo);
    public ArrayList<UsuarioBean> retornaEnvolvidosGrupo(GrupoBean grupo, String nomeProjeto);
    public void deletarUsuarioGrupo(UsuarioBean usuario, GrupoBean grupo);
    public ArrayList<UsuarioBean> retornarUsuariosSemGrupo();
    public ArrayList<UsuarioBean> retornaNaoEnvolvidosGrupo(GrupoBean grupo, String nomeProjeto);
    public boolean usuarioEstaEmGrupo(String nomeUsuario, String nomeGrupo, String nomeProjeto);
            
}
