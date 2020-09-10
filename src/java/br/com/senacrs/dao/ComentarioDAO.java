package br.com.senacrs.dao;

import br.com.senacrs.bean.ComentarioBean;
import java.util.ArrayList;

public interface ComentarioDAO {
    public void comentarTopico(ComentarioBean comentario, int idTopico);
    public void deletarComentarioTopico(int idComentario);
    public ArrayList<ComentarioBean> listarComentarios(int idTopico);
    public ComentarioBean buscar(int idComentario);
    public void editar(ComentarioBean comentario);
    public String retornaComentariosTabela(ArrayList<ComentarioBean> comentarios);
}
