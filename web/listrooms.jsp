<!-- Written by Sukhwinder Singh (ssruprai@hotmail.com -->
<%-- Written by Sukhwinder Singh (ssruprai@hotmail.com --%>

<%@ page session="true" errorPage="error.jsp" import="java.util.Set,java.util.Iterator,java.util.Map,sukhwinder.chat.*"%>
<HTML>
<HEAD>
<TITLE>ColabUFV - Chat</TITLE>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/chat.css">
<meta name="Author" content="Sukhwinder Singh (ssruprai@hotmail.com">	
<SCRIPT language="JavaScript">
	 <!--
	 if(window.top != window.self)
	 {
		 window.top.location = window.location;
	 }
	 //-->
</SCRIPT>
</HEAD>

<BODY bgcolor="#FFFFFF">
<%


String nickname = (String)session.getAttribute("nickname");
if (nickname == null || nickname == "")
{
	response.sendRedirect("login.jsp");
	//System.out.println("Redirecting");
}
else
{
	String roomname = request.getParameter("rn");	
	String descr = request.getParameter("sd");
	boolean see = false;
	if (descr != null && descr.equals("y"))
	{
		see = true;
	}
%>
<%@ include file="header.jsp" %>
<TABLE width="80%" align="center">	
	<!--<tr>
		<td class="normal">Welcome <span class="chattername"><%=nickname%></span></td>
	</tr>	
	-->
	<TR>
		<TD width="100%">Selecione a sala e entre!
		</TD>
	</TR>
</TABLE>
<BR>
	<%
				
		
		try
		{
			ChatRoomList roomlist = (ChatRoomList)application.getAttribute("chatroomlist");
			ChatRoom[] chatrooms = roomlist.getRoomListArray();
			if(roomname == null)
			{
				roomname = "Java";
			}
			roomname = roomname.trim();

	%>
<DIV align="center">
<CENTER>
	<FORM name="chatrooms" action="<%=request.getContextPath()%>/start.jsp" method="post">
	<TABLE width="80%" border="1" cellspacing="1" cellpadding="1" align="center">
	<TR>
	<TD colspan="2" class="pagetitle">Sala</TD>
	</TR>
	<%
			for (int i = 0; i < 1; i++)
			{
				if (chatrooms[i].getName().equalsIgnoreCase("StartUp"))
					continue;
	%>
		<TR>
		<TD>
		<INPUT type=radio name="rn" value="<%=chatrooms[i].getName()%>"
		<%if (chatrooms[i].getName().equals(roomname))
			out.write("checked");%>><%=chatrooms[i].getName()%>
		</TD>
		</TR>
	<%
			}
		}
		catch (Exception e)
		{
			System.out.println("Unable to get handle to Servlet Context: " + e.getMessage());
			e.printStackTrace();
		}
%>
<TR>
	<TD><INPUT type="Submit" value="Entrar"></TD>
</TR>
</TABLE>
</FORM>
</CENTER>
</DIV>
<%
	}
%>
<%@ include file="/footer.jsp"%>
</BODY>
</HTML>