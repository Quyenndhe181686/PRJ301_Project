<%-- 
    Document   : list
    Created on : Oct 20, 2024, 11:29:18 PM
    Author     : milo9
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <table border="1px">
            <tr>
                <td>Id</td>
                <td>Name</td>
                <td>Department</td>
                <td>Phone Number</td>
                <td>Address</td>
                <td>Salary</td>
            </tr>
            <c:forEach items="${requestScope.list}" var="l">
                <tr>
                    <td>${l.id}</td>
                    <td>${l.name}</td>
                    <td>${l.dept.id}</td>
                    <td>${l.phoneNumber}</td>
                    <td>${l.address}</td>
                    <td>${l.salary.id}</td>
                </tr>
                
            </c:forEach>
            
        </table>
    </body>
</html>
