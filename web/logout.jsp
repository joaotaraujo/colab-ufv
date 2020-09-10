<!-- Written by Sukhwinder Singh (ssruprai@hotmail.com -->
<%-- Written by Sukhwinder Singh (ssruprai@hotmail.com --%>

<%@ page import="sukhwinder.chat.*" errorPage="error.jsp" %>
<HTML>
<HEAD>
    
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/chat.css">
<META http-equiv="pragma" content="no-cache">
<meta name="Author" content="Sukhwinder Singh (ssruprai@hotmail.com">	
</HEAD>

<BODY>
<%@ include file="header.jsp" %>
<div align="center">
<center>

<%
	String nickname = (String)session.getAttribute("nickname");
	if (nickname != null && nickname.length() > 0)
	{
		ChatRoomList roomlist = (ChatRoomList) application.getAttribute("chatroomlist");
		ChatRoom chatRoom = roomlist.getRoomOfChatter(nickname);
		chatRoom.addMessage(new Message("system", nickname + " saiu da sala.", new java.util.Date().getTime()));
		if ( chatRoom != null)
		{
			chatRoom.removeChatter(nickname);
		}
                String tipoUsuario = (String)session.getAttribute("tipoUsuario");
                if(tipoUsuario.equalsIgnoreCase("professor"))
                    response.sendRedirect("paginaInicial/PaginaInicialProfessor.jsp");
                else
                    response.sendRedirect("paginaInicial/PaginaInicialAluno.jsp");

	}
	else
	{
		response.sendRedirect("login.jsp");
	}
	%>
</center>
</div>
</BODY>
</HTML>