

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>


<html>
<head>
    <title>Create Plan Detail</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            padding: 5px;
            text-align: center;
        }
    </style>
</head>
<body>
<h1>Create Plan Detail</h1>

<form action="create" method="POST">
    
    <table>
        <thead>
            <tr>
                <th rowspan="2">Date</th>
                <c:forEach var="product" items="${products}">
                    <th colspan="3">${product.name}</th>
                </c:forEach>
            </tr>
            <tr>
                <c:forEach var="product" items="${products}">
                    <th>K1</th>
                    <th>K2</th>
                    <th>K3</th>
                </c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="currentDate" items="${dateList}">
                <tr>
                    <td><fmt:formatDate value="${currentDate}" pattern="dd/MM/yyyy" /></td>
                    <c:forEach var="product" items="${products}">
                        <td><input type="text" name="quantity_${product.id}_K1" min="0" /></td>
                        <td><input type="text" name="quantity_${product.id}_K2" min="0" /></td>
                        <td><input type="text" name="quantity_${product.id}_K3" min="0" /></td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <input type="hidden" name="planId" value="${plan.id}"/>
    <button type="submit">Save</button>
</form>
</body>
</html>