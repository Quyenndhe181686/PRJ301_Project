<%-- 
    Document   : logout.jsp
    Created on : Oct 29, 2024, 10:44:56 PM
    Author     : milo9
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Xóa session khi người dùng đăng xuất
    session.invalidate();
    response.sendRedirect("login.html");
%>