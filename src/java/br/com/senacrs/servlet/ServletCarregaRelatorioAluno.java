package br.com.senacrs.servlet;

import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.bean.UsuarioBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AlertaDAO;
import br.com.senacrs.dao.AtividadeDAO;
import br.com.senacrs.dao.EmailDAO;
import br.com.senacrs.dao.MensagemDAO;
import br.com.senacrs.dao.ProjetoDAO;
import br.com.senacrs.dao.TarefaDAO;
import br.com.senacrs.dao.UsuarioDAO;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletCarregaRelatorioAluno extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
        
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
            String nomeAlunoSelecionado = (String) request.getParameter("alunoParaCarregar");
        
            UsuarioDAO ud = DAOFactory.createUsuarioDAO();
            
            ProjetoDAO projd=DAOFactory.createProjetoDAO();
            
            ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido, 1);
            
            String graficoGeralString = this.montaGraficos(projeto,ud.buscar(nomeAlunoSelecionado, 1),sessao);
            sessao.setAttribute("graficoGeral", graficoGeralString);
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarRelatorioAluno.jsp");
            dis.forward(request, response);
            
    }
        
    
    public String montaGraficos(ProjetoBean projeto, UsuarioBean usuario, HttpSession sessao){
        
        AtividadeDAO atvd=DAOFactory.createAtividadeDAO();
        ArrayList<AtividadeBean> atividadesAluno = atvd.retornarAtividadesEnvolvido(usuario, projeto);
        int numAtividadesAluno = atividadesAluno.size();
        
        String relatorioTabelaString =  "\n" +
"            <script type=\"text/javascript\" src=\"/ProjetoColabUFV/chatCanvas.javascript/canvasjs.min.js\"></script>\n" +
"            <script type=\"text/javascript\">\n" +
"                window.onload = function () {CanvasJS.addColorSet(\"corAtvAtrasada\",\n" +
"                [//colorSet Array\n" +
"\n" +
"                \"#92C9FF\",\n" +
"                \"#FF0000\"                \n" +
"                ]);CanvasJS.addColorSet(\"corAtvConcluida\",\n" +
"                [//colorSet Array\n" +
"\n" +
"                \"#92C9FF\",\n" +
"                \"#28F200\"                \n" +
"                ]);\n";
        relatorioTabelaString = relatorioTabelaString + this.montaGraficoAtividadesFeitas(atividadesAluno)+ this.montaGraficoAtividadesAtrasadas(atividadesAluno)+
"                    var graficoGeral = new CanvasJS.Chart(\"chartContainer\",\n" +
"                    {\n" +
"                        animationEnabled: true,\n" +
"                        title:{\n" +
"                            text: \"Informações de "+usuario.getNome()+" (Quantitativa)\"\n" +
"                        },\n" +
"                        data: \n" +
"                        [{\n" +
"                            type: \"column\", //change type to bar, line, area, pie, etc\n" +
"                            dataPoints: [\n";

            EmailDAO emd=DAOFactory.createEmailDAO();
            int numEmailsAluno = emd.numEmailsEnviadosAluno(projeto.getId(), usuario.getId());
            
            MensagemDAO mend = DAOFactory.createMensagemDAO();
            int numMensagensAluno = mend.listarMensagensUsuario(projeto.getChat().getId(), usuario.getId()).size();
            
            int numTopicosAluno = 0, numComentariosAluno = 0;
            for(int i=0;i<projeto.getForum().getTopicos().size();i++){
                if(projeto.getForum().getTopicos().get(i).getAutor().getNome().equalsIgnoreCase(usuario.getNome()))
                    numTopicosAluno++;
                
                for(int j=0;j<projeto.getForum().getTopicos().get(i).getListaComentarios().size();j++)
                    if(projeto.getForum().getTopicos().get(i).getListaComentarios().get(j).getAutor().getNome().equalsIgnoreCase(usuario.getNome()))
                        numComentariosAluno++;
            }
            
            
            
            TarefaDAO td = DAOFactory.createTarefaDAO();
            int numTarefasAluno=td.retornarTarefasEnvolvido(usuario, projeto.getAtividades()).size();
            
            AlertaDAO alertd=DAOFactory.createAlertaDAO();
            int numAlertasAluno = alertd.listarAlertasEnviados(projeto.getId(), usuario.getId()).size();
            
            
            
            relatorioTabelaString = relatorioTabelaString+"{ label: \"Atividades\", y: "+numAtividadesAluno+" },\n";
            relatorioTabelaString = relatorioTabelaString+"{ label: \"Tarefas\", y: "+numTarefasAluno+" },\n";
            relatorioTabelaString = relatorioTabelaString+"{ label: \"Alertas\", y: "+numAlertasAluno+" },\n";
            relatorioTabelaString = relatorioTabelaString+"{ label: \"Mensagem (Chat)\", y: "+numMensagensAluno+" },\n";
            relatorioTabelaString = relatorioTabelaString+"{ label: \"Tópicos\", y: "+numTopicosAluno+" },\n";
            relatorioTabelaString = relatorioTabelaString+"{ label: \"Comentários\", y: "+numComentariosAluno+" },\n";
            relatorioTabelaString = relatorioTabelaString+"{ label: \"Emails\", y: "+numEmailsAluno+" },\n";


            relatorioTabelaString = relatorioTabelaString + "                            ]\n" +
"                        }]\n" +
"                    });\n" +
"                    graficoGeral.render();graficoAtividadesFeitas.render();graficoAtividadesAtrasadas.render();\n" +
"                }\n" +
"            </script>\n" +
"            \n" +
"            <div id=\"chartContainer\" style=\"height: 300px; width: 100%;\"></div> <br><div id=\"chartContainer1\" style=\"float: left;height: 200px; width: 50%;\"></div><div id=\"chartContainer2\" style=\"float: right;height: 200px; width: 50%;\"></div>";
        

                
            sessao.setAttribute("quantAtividades",numAtividadesAluno);
            sessao.setAttribute("quantTarefas",numTarefasAluno);
            sessao.setAttribute("quantAlertas",numAlertasAluno);
            sessao.setAttribute("quantMensagens",numMensagensAluno);
            sessao.setAttribute("quantTopicos",numTopicosAluno);
            sessao.setAttribute("quantComentarios",numComentariosAluno);
            sessao.setAttribute("quantEmails",numEmailsAluno);
                        
            sessao.setAttribute("tabelaQuantitativa",montaTabelaQuantitativa(sessao,usuario.getNome()));
            

            return relatorioTabelaString;
        
    }
    
    public String montaGraficoAtividadesFeitas(ArrayList<AtividadeBean> atividades){
        
        ArrayList<AtividadeBean> atividadesFeitas = new ArrayList<AtividadeBean>();
        for(int i=0;i<atividades.size();i++){
            if(atividades.get(i).getStatus().equalsIgnoreCase("Concluida")){
                atividadesFeitas.add(atividades.get(i));
            }
        }
        
        return "var graficoAtividadesFeitas = new CanvasJS.Chart(\"chartContainer1\",\n" +
"	{\n" +
"		colorSet: \"corAtvConcluida\",title:{\n" +
"			text: \"Atividades concluídas\",\n" +
"			fontFamily: \"Impact\",\n" +
"			fontWeight: \"normal\"\n" +
"		},\n" +
"\n" +
"		legend:{\n" +
"			verticalAlign: \"bottom\",\n" +
"			horizontalAlign: \"center\"\n" +
"		},\n" +
"		data: [\n" +
"		{\n" +
"			//startAngle: 45,\n" +
"			indexLabelFontSize: 13,\n" +
"			indexLabelFontFamily: \"Garamond\",\n" +
"			indexLabelFontColor: \"black\",\n" +
"			indexLabelLineColor: \"black\",\n" +
"			indexLabelPlacement: \"outside\",\n" +
"			type: \"doughnut\",\n" +
"			showInLegend: true,\n" +
"			dataPoints: [\n" +
"				{  y: "+converterDoubleDoisDecimais((1-((float) atividadesFeitas.size()/(float)atividades.size()))*100)+", legendText:\"Atv (outras)\", indexLabel: \"Atv (outras) "+converterDoubleDoisDecimais((1-(float) atividadesFeitas.size()/(float)atividades.size())*100)+" %\" },\n" +
"				{  y: "+converterDoubleDoisDecimais((float) (atividadesFeitas.size()/(float)atividades.size())*100)+", legendText:\"Atv concluídas\", indexLabel: \"Atv concluídas "+converterDoubleDoisDecimais((float) (atividadesFeitas.size()/(float)atividades.size())*100)+"%\" },\n" +
"			]\n" +
"		}\n" +
"		]\n" +
"	});\n";
    }
    
    public String montaGraficoAtividadesAtrasadas(ArrayList<AtividadeBean> atividades){
        
        ArrayList<AtividadeBean> atividadesAtrasadas = new ArrayList<AtividadeBean>();
        for(int i=0;i<atividades.size();i++){
            if(atividades.get(i).getStatus().equalsIgnoreCase("Atrasada")){
                atividadesAtrasadas.add(atividades.get(i));
            }
        }
        
        return "var graficoAtividadesAtrasadas = new CanvasJS.Chart(\"chartContainer2\",\n" +
"	{\n" +
"		colorSet: \"corAtvAtrasada\",title:{\n" +
"			text: \"Atividades atrasadas\",\n" +
"			fontFamily: \"Impact\",\n" +
"			fontWeight: \"normal\"\n" +
"		},\n" +
"\n" +
"		legend:{\n" +
"			verticalAlign: \"bottom\",\n" +
"			horizontalAlign: \"center\"\n" +
"		},\n" +
"		data: [\n" +
"		{\n" +
"			//startAngle: 45,\n" +
"			indexLabelFontSize: 13,\n" +
"			indexLabelFontFamily: \"Garamond\",\n" +
"			indexLabelFontColor: \"black\",\n" +
"			indexLabelLineColor: \"black\",\n" +
"			indexLabelPlacement: \"outside\",\n" +
"			type: \"doughnut\",\n" +
"			showInLegend: true,\n" +
"			dataPoints: [\n" +
"				{  y: "+converterDoubleDoisDecimais((1-((float) atividadesAtrasadas.size()/(float)atividades.size()))*100)+", legendText:\"Atv (outras)\", indexLabel: \"Atv (outras) "+converterDoubleDoisDecimais((1-(float) atividadesAtrasadas.size()/(float)atividades.size())*100)+" %\" },\n" +
"				{  y: "+converterDoubleDoisDecimais((float) (atividadesAtrasadas.size()/(float)atividades.size())*100)+", legendText:\"Atv atrasadas\", indexLabel: \"Atv atrasadas "+converterDoubleDoisDecimais((float) (atividadesAtrasadas.size()/(float)atividades.size())*100)+"%\" },\n" +
"			]\n" +
"		}\n" +
"		]\n" +
"	});\n";
    }
    
    public String montaTabelaQuantitativa(HttpSession sessao, String nomeAluno){
        return "<table style=\"padding-top:20px;\" border=\"1\" width=\"30%\" cellpadding=\"4\">\n" +
"                    <thead>\n" +
"                        <tr>\n" +
"                            <th colspan=\"4\"><h3><center>Informações de "+nomeAluno+" (Quantitativa)</center></h3></th>\n" +
"                        </tr>\n" +
"                    </thead>\n" +
"                    <tbody>\n" +
"                        <tr>\n" +
"                            <td style=\"width:40%;\">Atividades</td>\n" +
"                            <td style=\"width:10%;\"><input size=\"10\" type=\"text\" name=\"atividades\" value=\""+ sessao.getAttribute("quantAtividades")+"\" /></td>\n" +
"                            <td style=\"width:40%;\">Tarefas</td>\n" +
"                            <td style=\"width:10%;\"><input size=\"10\" type=\"text\" name=\"tarefas\" value=\""+ sessao.getAttribute("quantTarefas")+"\" /></td>\n" +
"                        </tr>\n" +
"                        <tr>\n" +
"                            <td style=\"width:40%;\">Tópicos</td>\n" +
"                            <td style=\"width:10%;\"><input size=\"10\" type=\"text\" name=\"topicos\" value=\""+ sessao.getAttribute("quantTopicos")+"\" /></td>\n" +
"                            <td style=\"width:40%;\">Comentários</td>\n" +
"                            <td style=\"width:10%;\"><input size=\"10\" type=\"text\" name=\"comentarios\" value=\""+ sessao.getAttribute("quantComentarios")+"\" /></td>\n" +
"                        </tr>\n" +
"                        <tr>\n" +
"                            <td style=\"width:40%;\">Alertas</td>\n" +
"                            <td style=\"width:10%;\"><input size=\"10\" type=\"text\" name=\"alertas\" value=\""+ sessao.getAttribute("quantAlertas")+"\" /></td>\n" +
"                            <td style=\"width:40%;\">Perguntas Frequentes</td>\n" +
"                            <td style=\"width:10%;\"><input size=\"10\" type=\"text\" name=\"faqs\" value=\""+ sessao.getAttribute("quantFAQs")+"\" /></td>\n" +
"                        </tr>\n" +
"                        <tr>\n" +
"                            <td style=\"width:40%;\">Mensagens (Chat)</td>\n" +
"                            <td style=\"width:10%;\"><input size=\"10\" type=\"text\" name=\"mensagens\" value=\""+ sessao.getAttribute("quantMensagens")+"\" /></td>\n" +
"                            <td style=\"width:40%;\">Emails</td>\n" +
"                            <td style=\"width:10%;\"><input size=\"10\" type=\"text\" name=\"emails\" value=\""+ sessao.getAttribute("quantEmails")+"\" /></td>\n" +
"                        </tr>\n" +
"                    </tbody>\n" +
"                </table>    ";
    }
    
    public static double converterDoubleDoisDecimais(double precoDouble) {
        DecimalFormat fmt = new DecimalFormat("0.00");      
        String string = fmt.format(precoDouble);
        String[] part = string.split("[,]");
        String string2 = part[0]+"."+part[1];
            double preco = Double.parseDouble(string2);
        return preco;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCarregaProjetosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ServletCarregaProjetosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
