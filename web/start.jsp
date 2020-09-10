<%-- Written by Sukhwinder Singh (ssruprai@hotmail.com --%>

<%@page import="java.util.Date"%>
<%@ page errorPage="error.jsp" import="java.util.Set,java.util.Iterator,java.util.Map,sukhwinder.chat.*"%>
<%
	String roomname = "Java";
	String nickname = (String)session.getAttribute("nickname");
	ChatRoomList roomlist = (ChatRoomList) application.getAttribute("chatroomlist");
	if (nickname == null)
	{
		response.sendRedirect("login.jsp");
	}
	else if (roomname == null)
	{
		response.sendRedirect("listrooms.jsp");
	}
	else
	{
		ChatRoom chatRoom = roomlist.getRoom(roomname);
		if (chatRoom == null)
		{
			out.write("<font color=\"red\" size=\"+1\">Room " + roomname + " não encontrado</font>");
			out.close();
			return;
		}
		if (chatRoom != null)
		{
                    
			Chatter chatter = new Chatter(nickname,"m",30);
			
                        
				chatRoom.addChatter(chatter);
                                
				chatRoom.addMessage(new Message("system", nickname + " entrou na sala.", new java.util.Date().getTime()));
				chatter.setEnteredInRoomAt(new java.util.Date().getTime());

			

			if (session.getAttribute("nickname") == null)
			{
				session.setAttribute("nickname", nickname);
			}
			response.sendRedirect("chat.jsp");
		}
		else
		{
			out.write("<span class=\"error\">Some error occured");
		}
	}	
%>