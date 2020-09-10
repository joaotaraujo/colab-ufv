package br.com.senacrs.dao;

import br.com.senacrs.bean.PerguntaFrequenteBean;
import java.util.ArrayList;

public interface PerguntaFrequenteDAO {
    
    public void inserir(PerguntaFrequenteBean perguntaFrequente, int idProjeto);
    public void deletar(String pergunta);
    public ArrayList<PerguntaFrequenteBean> listarPerguntasFrequentes(String nomeProjeto);
    /*Se tipo:
        - 1: pesquisa por pergunta
        - 2: pesquisa por id
    */
    public PerguntaFrequenteBean buscar(String informacao, int tipo);
    public void editar(PerguntaFrequenteBean perguntaFrequente);
    public String retornaPerguntasFrequentesTabela(ArrayList<PerguntaFrequenteBean> perguntasFrequentes);
    public String retornarInformacaoPeguntaFrequenteTabela(PerguntaFrequenteBean perguntaFrequente);
    
}
