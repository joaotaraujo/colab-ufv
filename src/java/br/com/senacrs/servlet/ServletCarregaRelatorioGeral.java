package br.com.senacrs.servlet;

import br.com.senacrs.bean.AtividadeBean;
import br.com.senacrs.bean.ProjetoBean;
import br.com.senacrs.connections.DAOFactory;
import br.com.senacrs.dao.AlertaDAO;
import br.com.senacrs.dao.EmailDAO;
import br.com.senacrs.dao.GrupoDAO;
import br.com.senacrs.dao.ProjetoDAO;
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

public class ServletCarregaRelatorioGeral extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException{
          
            HttpSession sessao = request.getSession();
        
            String nomeProjetoEscolhido = (String) sessao.getAttribute("projetoEscolhido"); 
        
            ProjetoDAO projd=DAOFactory.createProjetoDAO();
            ProjetoBean projeto = projd.pesquisarProjeto(nomeProjetoEscolhido, 1);
            
            GrupoDAO gd=DAOFactory.createGrupoDAO();
            
            String tipoDeRanking = (String)request.getParameter("ranking");
            
            if(tipoDeRanking.equalsIgnoreCase("Atividades feitas"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,1));
            else if(tipoDeRanking.equalsIgnoreCase("Tarefas feitas"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,2));
            else if(tipoDeRanking.equalsIgnoreCase("Tópicos criados"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,3));
            else if(tipoDeRanking.equalsIgnoreCase("Comentários enviados"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,4));
            else if(tipoDeRanking.equalsIgnoreCase("Mensagens trocadas"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,5));
            else if(tipoDeRanking.equalsIgnoreCase("Alertas enviados"))
                sessao.setAttribute("tabelaRanking",projd.retornaTabelaRankingPorGrupo(projeto,6));
            
            sessao.setAttribute("redirect","redirectRelatorio");
            
            sessao.setAttribute("nomeRanking", tipoDeRanking);
            
            String graficoGeralString = this.montaGraficos(projeto,sessao);
            sessao.setAttribute("graficoGeral", graficoGeralString);
            
            sessao.setAttribute("graficoMenor", "<div id=\"chartContainer1\" style=\"float: left;height: 200px; width: 50%;\"></div><div id=\"chartContainer2\" style=\"float: right;height: 200px; width: 50%;\"></div><br>");
            
            
            RequestDispatcher dis = request.getRequestDispatcher("paginaInicial/vizualizacao/vizualizarRelatorio.jsp");
            dis.forward(request, response);
            
    }
        
    
    public String montaGraficos(ProjetoBean projeto, HttpSession sessao){
        
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
        relatorioTabelaString = relatorioTabelaString + this.montaGraficoAtividadesFeitas(projeto)+ this.montaGraficoAtividadesAtrasadas(projeto)+
"                    var graficoGeral = new CanvasJS.Chart(\"chartContainer\",\n" +
"                    {\n" +
"                        animationEnabled: true,\n" +
"                        title:{\n" +
"                            text: \"Informações do Projeto (Quantidade)\"\n" +
"                        },\n" +
"                        data: \n" +
"                        [{\n" +
"                            type: \"column\", //change type to bar, line, area, pie, etc\n" +
"                            dataPoints: [\n";

            int numTarefas=0;
            for(int i=0; i<projeto.getAtividades().size();i++)
                numTarefas = numTarefas + projeto.getAtividades().get(i).getTarefas().size();
            
            AlertaDAO alertd=DAOFactory.createAlertaDAO();
            
            int numComentarios=0;
            for(int i=0;i<projeto.getForum().getTopicos().size();i++)
                numComentarios++;
            
            EmailDAO emd=DAOFactory.createEmailDAO();
        
relatorioTabelaString = relatorioTabelaString+"{ label: \"Atividades\", y: "+projeto.getAtividades().size()+" },\n";
relatorioTabelaString = relatorioTabelaString+"{ label: \"Tarefas\", y: "+numTarefas+" },\n";
relatorioTabelaString = relatorioTabelaString+"{ label: \"Alertas\", y: "+alertd.listarAlertasProjeto(projeto.getId()).size()+" },\n";
relatorioTabelaString = relatorioTabelaString+"{ label: \"Mensagem (Chat)\", y: "+projeto.getChat().getMensagensEnviadas().size()+" },\n";
relatorioTabelaString = relatorioTabelaString+"{ label: \"Tópicos\", y: "+projeto.getForum().getTopicos().size()+" },\n";
relatorioTabelaString = relatorioTabelaString+"{ label: \"Comentários\", y: "+numComentarios+" },\n";
relatorioTabelaString = relatorioTabelaString+"{ label: \"Emails\", y: "+emd.numEmailsEnviadosProjeto(projeto.getId())+" },\n";
relatorioTabelaString = relatorioTabelaString+"{ label: \"Grupo\", y: "+projeto.getGrupos().size()+" },\n";
relatorioTabelaString = relatorioTabelaString+"{ label: \"Papeis\", y: "+projeto.getPapeis().size()+" },\n";
relatorioTabelaString = relatorioTabelaString+"{ label: \"FAQs\", y: "+projeto.getPerguntasFrequentes().size()+" },\n";


relatorioTabelaString = relatorioTabelaString + "                            ]\n" +
"                        }]\n" +
"                    });\n" +
"                    graficoGeral.render();graficoAtividadesFeitas.render();graficoAtividadesAtrasadas.render();\n" +
"                }\n" +
"            </script>\n" +
"            \n <div id=\"chartContainer\" style=\"height: 300px; width: 100%;\"></div>";
        

                
            sessao.setAttribute("quantAtividades",projeto.getAtividades().size());
            sessao.setAttribute("quantTarefas",numTarefas);
            sessao.setAttribute("quantAlertas",alertd.listarAlertasProjeto(projeto.getId()).size());
            sessao.setAttribute("quantMensagens",projeto.getChat().getMensagensEnviadas().size());
            sessao.setAttribute("quantTopicos",projeto.getForum().getTopicos().size());
            sessao.setAttribute("quantComentarios",numComentarios);
            sessao.setAttribute("quantEmails",emd.numEmailsEnviadosProjeto(projeto.getId()));
            sessao.setAttribute("quantGrupos",projeto.getGrupos().size());
            sessao.setAttribute("quantPapeis",projeto.getPapeis().size());
            sessao.setAttribute("quantFAQs",projeto.getPerguntasFrequentes().size());
            
            sessao.setAttribute("tabelaQuantitativa",montaTabelaQuantitativa(sessao));

        return relatorioTabelaString;
        
    }
    
    public String montaGraficoAtividadesFeitas(ProjetoBean projeto){
        
        ArrayList<AtividadeBean> atividadesFeitas = new ArrayList<AtividadeBean>();
        for(int i=0;i<projeto.getAtividades().size();i++){
            if(projeto.getAtividades().get(i).getStatus().equalsIgnoreCase("Concluida")){
                atividadesFeitas.add(projeto.getAtividades().get(i));
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
"				{  y: "+(1-((float) atividadesFeitas.size()/(float)projeto.getAtividades().size()))*100+", legendText:\"Atv (outras) \", indexLabel: \"Atv (outras) "+(1-(float) atividadesFeitas.size()/(float)projeto.getAtividades().size())*100+"%\" },\n" +
"				{  y: "+(float) (atividadesFeitas.size()/(float)projeto.getAtividades().size())*100+", legendText:\"Atv concluídas \", indexLabel: \"Atv concluídas "+(float) (atividadesFeitas.size()/(float)projeto.getAtividades().size())*100+"%\" },\n" +
"			]\n" +
"		}\n" +
"		]\n" +
"	});\n";
    }
    
    public String montaGraficoAtividadesAtrasadas(ProjetoBean projeto){
        
        ArrayList<AtividadeBean> atividadesAtrasadas = new ArrayList<AtividadeBean>();
        for(int i=0;i<projeto.getAtividades().size();i++){
            if(projeto.getAtividades().get(i).getStatus().equalsIgnoreCase("Atrasada")){
                atividadesAtrasadas.add(projeto.getAtividades().get(i));
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
"				{  y: "+converterDoubleDoisDecimais((1-((float) atividadesAtrasadas.size()/(float)projeto.getAtividades().size()))*100)+", legendText:\"Atv (outras)\", indexLabel: \"Atv (outras) "+converterDoubleDoisDecimais((1-(float) atividadesAtrasadas.size()/(float)projeto.getAtividades().size())*100)+"%\" },\n" +
"				{  y: "+converterDoubleDoisDecimais((float) (atividadesAtrasadas.size()/(float)projeto.getAtividades().size())*100)+", legendText:\"Atv atrasadas\", indexLabel: \"Atv atrasadas "+converterDoubleDoisDecimais((float) (atividadesAtrasadas.size()/(float)projeto.getAtividades().size())*100)+"%\" },\n" +
"			]\n" +
"		}\n" +
"		]\n" +
"	});\n";
    }
    
    public String montaTabelaQuantitativa(HttpSession sessao){
        return "<table style=\"border=\"1\" width=\"100%\" cellpadding=\"4\">\n" +
"                    <thead>\n" +
"                        <tr  >\n" +
"                            <th colspan=\"4\"><h3><center>Informações do projeto (Quantitativa)</center></h3></th>\n" +
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
"                            <td style=\"width:40%;\">Grupos</td>\n" +
"                            <td style=\"width:10%;\"><input size=\"10\" type=\"text\" name=\"grupos\" value=\""+ sessao.getAttribute("quantGrupos")+"\" /></td>\n" +
"                            <td style=\"width:40%;\">Papéis</td>\n" +
"                            <td style=\"width:10%;\"><input size=\"10\" type=\"text\" name=\"papeis\" value=\""+sessao.getAttribute("quantPapeis")+"\" /></td>\n" +
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
