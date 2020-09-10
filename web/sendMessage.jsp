<%@page import="br.com.senacrs.dao.MensagemDAO"%>
<%@page import="br.com.senacrs.dao.UsuarioDAO"%>
<%@page import="br.com.senacrs.connections.DAOFactory"%>
<%@page import="br.com.senacrs.bean.MensagemBean"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<!-- Written by Sukhwinder Singh (ssruprai@hotmail.com -->
<%-- Written by Sukhwinder Singh (ssruprai@hotmail.com --%>

<%@ page isErrorPage="false" errorPage="error.jsp" import="java.util.Set,java.util.Iterator,java.util.Map,sukhwinder.chat.*"%>
<% 
	
	String nickname = (String)session.getAttribute("nickname");
	
	if (nickname != null && nickname.length() > 0)
	{
		ChatRoomList roomList = (ChatRoomList)application.getAttribute("chatroomlist");
		ChatRoom chatRoom = roomList.getRoom("Java");
		if ( chatRoom != null)
		{
			String msg = request.getParameter("messagebox");
			
			if ( msg != null && msg.length() > 0)
			{   
                            MensagemBean mensagemChat = new MensagemBean();
                            Date date = new Date();
                            DateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
                            mensagemChat.setHoraEnvio(horaFormat.format(date));
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            mensagemChat.setDataEnvio(dateFormat.parse((String)dateFormat.format(date)));
                            mensagemChat.setDescricao(msg);
                            
                            UsuarioDAO ud = DAOFactory.createUsuarioDAO(); 
                            mensagemChat.setRemetente(ud.buscar(nickname, 1));
                            mensagemChat.setIdChat(Integer.parseInt((String)session.getAttribute("idChatProjeto")));
                            
                            MensagemDAO mend = DAOFactory.createMensagemDAO();
                            mend.armazenarMensagem(Integer.parseInt((String)session.getAttribute("idChatProjeto")), mensagemChat);
                            
                            msg = msg.trim();
                            chatRoom.addMessage(new Message(nickname, msg, new java.util.Date().getTime()));
			}
	
%>


<HTML>
<HEAD>
<LINK rel="stylesheet" type="text/css" href="chat.css">
<META http-equiv="pragma" content="no-cache">
<meta name="Author" content="Sukhwinder Singh (ssruprai@hotmail.com">	

<SCRIPT language="JavaScript" type="text/javascript">
<!--

function winopen(path)
{
	chatterinfo = window.open(path,"chatterwin","scrollbars=no,resizable=yes, width=400, height=300, location=no, toolbar=no, status=no");
	chatterinfo.focus();

}

//-->
</SCRIPT>
</HEAD>
<BODY onLoad="document.msg.messagebox.focus();" bgcolor="#FFFFFF">
<TABLE width="100%" cellpadding="3" cellspacing="0">
	<TR> 
		<TD width="50%" align="left" valign="top"> 
			<TABLE>
				<TR> 
					<FORM name="msg" action="sendMessage.jsp" method="post">
						<TD width="100%"> 
							<INPUT type="text" name="messagebox" maxlength="500" style="height: 100px;" size=65">
							<INPUT type="hidden" name="nickname" value="<%=session.getAttribute("nickname")%>">
							<INPUT name="submit" type="submit" style="height: 50px;" value="Enviar">
						</TD>
					</FORM>
				</TR>
			</TABLE>
		</TD>
		<TD> 
			<TABLE style="width:10px;" border="0" cellpadding="0" cellspacing="0" class="panel">
				<TR align="left" valign="top"> 
					<FORM name="logout" action="/ProjetoColabUFV/logout.jsp" method="post" target="_top">
						<TD width="100%"> 
							<input type="Submit" value="Sair">
						</TD>
					</FORM>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</BODY>
</HTML>
<%
		}
		else
		{
			out.write("<h2 class=\"error\">Your room couldn't be found. You can't send message</h2>");
		}
	}
	else
	{
		response.sendRedirect("login.jsp");
	}
%>
