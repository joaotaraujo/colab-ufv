<!-- Written by Sukhwinder Singh (ssruprai@hotmail.com -->
<%-- Written by Sukhwinder Singh (ssruprai@hotmail.com --%>

<%@ page session="true" import="sukhwinder.chat.ChatRoomList, sukhwinder.chat.ChatRoom" errorPage="error.jsp"%>
<%
	String nickname = (String)session.getAttribute("nickname");
	if (nickname != null && nickname.length() > 0)
	{
		ChatRoomList roomList = (ChatRoomList) application.getAttribute("chatroomlist");    
		ChatRoom room = roomList.getRoom("Java");
		String roomname = room.getName();
%>
	
<HTML>
<HEAD>
<TITLE>ColabUFV - Chat - <%=nickname%> (<%=roomname%>) </TITLE>
<META name="Author" value="Sukhwinder Singh (ssruprai@hotmail.com)">
</HEAD>
<FRAMESET rows="70%,30%">
<FRAME SRC="displayMessages.jsp#current" name="MessageWin">
<FRAME SRC="sendMessage.jsp" name="TypeWin">
</FRAMESET>
<NOFRAMES>
<H2>Este chat requer suporte para frame!</h2>
</NOFRAMES>
</HTML>
<%
}
else
{
	response.sendRedirect("index.jsp");
}
%>