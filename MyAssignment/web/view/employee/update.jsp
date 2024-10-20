<%-- 
    Document   : update.jsp
    Created on : Oct 20, 2024, 9:59:34 PM
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
        <form action="update" method="POST">
            Id: ${requestScope.e.id} <br/>
            <input type="hidden" name="eid" value="${requestScope.e.id}"/>
            
            Name: <input type="text" name="name" value="${requestScope.e.name}"/><br/>

            Department:
            <select name="dept">
                <c:forEach items="${requestScope.depts}" var="d">
                    <option value="${d.id}" ${requestScope.e.dept.id eq d.id ? "selected=\"selected\"" : ""}>
                        ${d.name}
                    </option>
                </c:forEach>
            </select><br/>

            Phone number: <input type="text" name="pNumber" value="${requestScope.e.phoneNumber}"/> <br/>
            Address: <input type="text" name="address" value="${requestScope.e.address}"/> <br/>

            Salary: 
            <select name="salary">
                <c:forEach items="${requestScope.salarys}" var="s">
                    <option value="${s.id}" ${requestScope.e.salary.id eq s.id ? "selected=\"selected\"" : ""}>
                        ${s.level} - ${s.salary}
                    </option>
                </c:forEach>
            </select><br/>

            <input type="submit" value="Save"/>
        </form>
    </body>
</html>
