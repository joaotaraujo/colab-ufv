package br.com.senacrs.connections;

import br.com.senacrs.dao.AlertaDAO;
import br.com.senacrs.dao.ArtefatoDAO;
import br.com.senacrs.dao.UsuarioDAO;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.ChatDAO;
import br.com.senacrs.dao.ComentarioDAO;
import br.com.senacrs.dao.ConteudoDAO;
import br.com.senacrs.dao.EmailDAO;
import br.com.senacrs.dao.ForumDAO;
import br.com.senacrs.dao.GrupoDAO;
import br.com.senacrs.dao.JDBCAlertaDAO;
import br.com.senacrs.dao.JDBCArtefatoDAO;
import br.com.senacrs.dao.JDBCAtividadeDAO;
import br.com.senacrs.dao.JDBCChatDAO;
import br.com.senacrs.dao.JDBCComentarioDAO;
import br.com.senacrs.dao.JDBCConteudoDAO;
import br.com.senacrs.dao.JDBCEmailDAO;
import br.com.senacrs.dao.JDBCForumDAO;
import br.com.senacrs.dao.JDBCGrupoDAO;
import br.com.senacrs.dao.JDBCMensagemDAO;
import br.com.senacrs.dao.JDBCPapelDAO;
import br.com.senacrs.dao.JDBCPerguntaFrequenteDAO;
import br.com.senacrs.dao.JDBCProjetoDAO;
import br.com.senacrs.dao.JDBCTarefaDAO;
import br.com.senacrs.dao.JDBCTopicoDAO;
import br.com.senacrs.dao.JDBCUsuarioDAO;
import br.com.senacrs.dao.MensagemDAO;
import br.com.senacrs.dao.PapelDAO;
import br.com.senacrs.dao.PerguntaFrequenteDAO;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.TarefaDAO;
import br.com.senacrs.dao.TopicoDAO;

public class DAOFactory {
    public static UsuarioDAO createUsuarioDAO(){
        return new JDBCUsuarioDAO();
    }
    public static ProjetoDAO createProjetoDAO(){
        return new JDBCProjetoDAO();
    }
    public static GrupoDAO createGrupoDAO() {
        return new JDBCGrupoDAO();
    }
    public static AtividadeDAO createAtividadeDAO(){
        return new JDBCAtividadeDAO();
    }
    public static TarefaDAO createTarefaDAO(){
        return new JDBCTarefaDAO();
    }
    public static PerguntaFrequenteDAO createPerguntaFrequenteDAO(){
        return new JDBCPerguntaFrequenteDAO();
    }
    public static PapelDAO createPapelDAO(){
        return new JDBCPapelDAO();
    }
    public static ConteudoDAO createConteudoDAO(){
        return new JDBCConteudoDAO();
    }
    public static AlertaDAO createAlertaDAO(){
        return new JDBCAlertaDAO();
    }
    public static ForumDAO createForumDAO(){
        return new JDBCForumDAO();
    }
    public static ComentarioDAO createComentarioDAO(){
        return new JDBCComentarioDAO();
    }
    public static TopicoDAO createTopicoDAO(){
        return new JDBCTopicoDAO();
    }
    public static ChatDAO createChatDAO(){
        return new JDBCChatDAO();
    }
    public static MensagemDAO createMensagemDAO(){
        return new JDBCMensagemDAO();
    }
    public static EmailDAO createEmailDAO(){
        return new JDBCEmailDAO();
    }
    public static ArtefatoDAO createArtefatoDAO(){
        return new JDBCArtefatoDAO();
    }
}
