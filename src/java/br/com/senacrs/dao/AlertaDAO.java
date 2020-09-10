package br.com.senacrs.dao;

import br.com.senacrs.bean.AlertaBean;
import java.util.ArrayList;

public interface AlertaDAO {
    
    
    public void enviarAlerta(AlertaBean alerta);
    public AlertaBean buscar(int idAlerta);
    public ArrayList<AlertaBean> listarAlertasRecebidos(int idProjeto, int idUsuario);
    public ArrayList<AlertaBean> listarAlertasEnviados(int idProjeto, int idUsuario);
    public ArrayList<AlertaBean> listarAlertasProjeto(int idProjeto); 
    public String emitirAlerta(int idUsuario, int idProjeto);
    public void editar(AlertaBean alerta);
    /*tipo:
        1 - alertas enviados
        2 - alertas recebidos
    */
    public String retornaAlertasTabela(ArrayList<AlertaBean> alertas, int tipo);
}
