<%-- 
    Document   : userdata
    Created on : Aug 14, 2018, 11:38:13 AM
    Author     : KALINDU
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Endpoint</title>
    </head>
    <body>
        <%
            
            String UserName = (String)request.getAttribute("UserName");
            String friendsCount = (String)request.getAttribute("friendsCount");
            String ID = (String)request.getAttribute("UserId");
            
            out.print("<H2>Hello "+UserName+" </H2>");
            out.println("<H1>You have <I>"+friendsCount+"</I> friends on your Facebook account</H1>");            
            out.println("<H3>Your facebook ID is : "+ID+"</H3>");
        %>
        
    </body>
</html>
