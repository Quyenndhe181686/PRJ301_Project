<%-- 
    Document   : create
    Created on : Oct 20, 2024, 4:29:10 PM
    Author     : milo9
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="styles.css">
    </head>
    <body>
        <h1>Form Create Employee</h1>
        <form action="add" method="POST">
            Name: <input type="text" name="name"/><br/>
            Department<select name="dept">
                <c:forEach items="${requestScope.depts}" var="d" >
                    <option value="${d.id}">${d.name}</option>
                </c:forEach>
            </select><br/>
            Phone number:<input type="text" name="pNumber"/>   <br/>
            Address:<input type="text" name="address"/> <br/>
            Salary: <select name="salary">
                <c:forEach items="${requestScope.salarys}" var="s" >
                    <option value="${s.id}">${s.level} - ${s.salary}</option>
                </c:forEach>
            </select><br/>
            <input type="submit" value="Save"/>
        </form>
    </body>
</html>
