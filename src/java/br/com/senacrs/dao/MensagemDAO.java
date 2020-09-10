package br.com.senacrs.dao;

import br.com.senacrs.bean.MensagemBean;
import java.util.ArrayList;

public interface MensagemDAO {
    public ArrayList<MensagemBean> listarMensagensUsuario(int idChat, int idUsuario);
    public ArrayList<MensagemBean> listarMensagensChat(int idChat);
    public void armazenarMensagem(int idChat, MensagemBean mensagem);
    public void deletarMensagens(int idChat);
}
