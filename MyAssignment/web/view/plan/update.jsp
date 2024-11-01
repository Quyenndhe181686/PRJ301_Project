<%-- 
    Document   : update
    Created on : Oct 26, 2024, 9:31:21 PM
    Author     : milo9
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Update Plan</title>
    </head>
    <body>
        <form action="update" method="POST">
            <input type="hidden" name="id" value="${plan.id}"/>
            Plan Name: <input type="text" name="name" value="${plan.name}"/> <br/>
            From: <input type="date" name="from" value="${plan.startDate}"/> 
            To: <input type="date" name="to" value="${plan.endDate}"/> <br/>
            Workshop: 
            <select name="did">
                <c:forEach items="${depts}" var="d">
                    <option value="${d.id}" ${d.id == plan.dept.id ? 'selected' : ''}>${d.name}</option>
                </c:forEach>
            </select>
            <br/>

            <table border="1px">
                <tr>
                    <td>Product</td>
                    <td>Quantity</td>
                    <td>Estimated Effort</td>
                </tr>
                <c:forEach items="${products}" var="p">
                    <tr>
                        <td>${p.name}<input type="hidden" name="pid" value="${p.id}"></td>


                        <c:set var="quantityAttr" value="quantity${p.id}" />
                        <c:set var="effortAttr" value="effort${p.id}" />

                        <td><input type="text" name="quantity${p.id}" value="${requestScope[quantityAttr]}"/></td>
                        <td><input type="text" name="effort${p.id}" value="${requestScope[effortAttr]}"/></td>
                    </tr>    
                </c:forEach>
                    
            </table>

            <input type="submit" value="Update"/>
        </form>
            
            
    </body>
</html>
