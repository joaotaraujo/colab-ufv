package br.com.senacrs.dao;

import java.io.File;

public interface ConteudoDAO {
    public boolean armazenaArquivo(File arquivo);
    public File recuperaArquivo(int idArquivo);
}
   
