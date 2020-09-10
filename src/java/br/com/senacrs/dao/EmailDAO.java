package br.com.senacrs.dao;

import br.com.senacrs.bean.EmailBean;

public interface EmailDAO {
    public int numEmailsEnviadosProjeto(int idProjeto);
    public void armazenarEmail(EmailBean email);
    public int numEmailsEnviadosAluno(int idProjeto, int idUsuario);
}
