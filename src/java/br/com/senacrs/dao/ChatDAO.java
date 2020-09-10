package br.com.senacrs.dao;

import br.com.senacrs.bean.ChatBean;

public interface ChatDAO {
    public ChatBean buscarChat(int idProjeto);
    public void cadastrarChat(int idProjeto);
    public void deletarChat(int idProjeto);
}
