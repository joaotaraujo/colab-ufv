package br.com.senacrs.dao;

import br.com.senacrs.bean.GrupoBean;
import br.com.senacrs.bean.PapelBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import java.util.ArrayList;

public interface ProjetoDAO {
    
    public void cadastrarProjeto(ProjetoBean projeto);
    public void deletarProjeto(int idProjeto);
    public ArrayList<ProjetoBean> listarProjetos();
    /* tipo
        1- por nome
        2- por id
    */
    public ProjetoBean pesquisarProjeto(String informacao, int tipo);
    public void editarProjeto(ProjetoBean projeto);
    public String retornarProjetoTabela(ProjetoBean projeto);
    public String retornarProjetoTabelaEditavel(ProjetoBean projeto);
    public boolean usuarioEstaEmProjeto(String nomeUsuario, String nomeProjeto);
    public ArrayList<UsuarioBean> listarAlunosNoProjeto(String nomeProjeto);
    public ArrayList<UsuarioBean> listarProfessoresNoProjeto(String nomeProjeto);
    public ArrayList<UsuarioBean> listarEnvolvidosNoProjeto(String nomeProjeto);
    public ArrayList<String> retornaProjetosUsuario(String nomeUsuario);
    public String retornaPapelUsuarioEmProjeto(UsuarioBean usuario, int idProjeto);
    public GrupoBean retornaGrupoUsuario(String nomeUsuario, String nomeProjeto);
    public ArrayList<UsuarioBean> retornaUsuariosSistemaSemPapel();
    public String trocaData (String dataAnoMesDia);
    public ArrayList<String> listarNomesProjetosExistentes();
    public ArrayList<PapelBean> retornaPapeisProjeto(int idProjeto);
    public UsuarioBean retornaUsuarioRanking(ProjetoBean projeto);
    public ArrayList<UsuarioBean> retornaEnvolvidosProjeto(ProjetoBean projeto);
    
    /* Informacao:
        1- ranking por atividades feitas
        2- ranking por tarefas feitas
        3- ranking por tópicos criados
        4- ranking por comentários enviados
        5- ranking por mensagens trocadas
        6- ranking por alertas enviados
    */
    public String retornaTabelaRankingPorGrupo(ProjetoBean projeto, int informacao);
    public ArrayList<UsuarioBean> retornaListaUsuarioRanking(GrupoBean grupo,  ProjetoBean projeto, int informacao);
}
