<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>View Plans</title>
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
    <h1>Plan List</h1>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Department Name</th>
                <th>Product</th>
                <th>Quantity</th>
                <th>Effort</th> <!-- Cột mới cho Effort -->
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="plan" items="${requestScope.plans}">
                <tr>
                    <td>${plan.id}</td>
                    <td>${plan.name}</td>
                    <td><fmt:formatDate value="${plan.startDate}" pattern="dd/MM/yyyy" /></td>
                    <td><fmt:formatDate value="${plan.endDate}" pattern="dd/MM/yyyy" /></td>
                    <td>${plan.dept.name}</td>
                    <td>
                        <c:forEach var="h" items="${plan.headers}">
                            <input type="text" value="${h.product.name}" readonly/>
                            <br/>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="h2" items="${plan.headers}">
                            <input type="text" value="${h2.quantity}" readonly/>
                            <br/>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="h3" items="${plan.headers}">
                            <input type="text" value="${h3.estimatedEffort}" readonly/>
                            <br/>
                        </c:forEach>
                    </td>
                    <td>
                        <form action="<c:url value='../plan/update'/>" method="GET">
                            <input type="hidden" name="planId" value="${plan.id}"/>
                            <button type="submit">Update</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>