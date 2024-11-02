<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Update Plan Detail</title>
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
<h1>Update Plan Detail</h1>

<form action="update" method="POST">
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
            <c:forEach var="detail" items="${planDetails}">
                <tr>
                    <td><fmt:formatDate value="${detail.date}" pattern="dd/MM/yyyy" /></td>
                    <c:forEach var="product" items="${products}">
                        <td>
                            <input type="hidden" name="pdid_${product.id}_K1" value="${detail.id}" />
                            <input type="number" name="quantity_${product.id}_K1" 
                                   value="${detail.shift.id == 1 ? detail.quantity : ''}" min="0" />
                        </td>
                        <td>
                            <input type="hidden" name="pdid_${product.id}_K2" value="${detail.id}" />
                            <input type="number" name="quantity_${product.id}_K2" 
                                   value="${detail.shift.id == 2 ? detail.quantity : ''}" min="0" />
                        </td>
                        <td>
                            <input type="hidden" name="pdid_${product.id}_K3" value="${detail.id}" />
                            <input type="number" name="quantity_${product.id}_K3" 
                                   value="${detail.shift.id == 3 ? detail.quantity : ''}" min="0" />
                        </td>
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