package br.com.senacrs.dao;

import br.com.senacrs.bean.ForumBean;

public interface ForumDAO {
    
    public void cadastrarForum(int idProjeto);
    public ForumBean buscarForum(int idProjeto);
    public void deletarForum(int idProjeto);
}