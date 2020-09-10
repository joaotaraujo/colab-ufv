package br.com.senacrs.dao;

import br.com.senacrs.bean.ArtefatoBean;
import java.util.ArrayList;

public interface ArtefatoDAO {
    public boolean armazenaArtefato( ArtefatoBean artefato );
    public ArtefatoBean recuperaArtefato(String nomeArquivo, String caminhoArquivo );
    public ArrayList<String> listarArtefatos(int idAtividade);
    public void deletarArtefato(int idArtefato);
    public ArrayList<String> listarArtefatosAluno(int idAluno);
}
