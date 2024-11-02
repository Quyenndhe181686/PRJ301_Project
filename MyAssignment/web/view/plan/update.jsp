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
            <input type="hidden" name="planId" value="${plan.id}"/>

            <table>
                <thead>
                    <tr>
                        <th rowspan="2">Date</th>
                            <c:forEach var="p1" items="${products}">
                            <th colspan="3">${p1.name}</th>
                            </c:forEach>
                    </tr>
                    <tr>
                        <c:forEach var="p2" items="${products}">
                            <th>K1</th>
                            <th>K2</th>
                            <th>K3</th>
                            </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cd" items="${dateList}">
                        <tr>
                            <td><fmt:formatDate value="${cd}" pattern="dd/MM/yyyy" /></td>
                            <c:forEach var="p" items="${products}">
                                <c:forEach var="s" begin="1" end="3">
                                    <td>
                                        <!-- Xây dựng khóa cho từng PlanDetail trong Map -->
                                        <c:set var="key" value="${cd}_${p.id}_${s}"/>
                                        <input type="text" name="quantity_${p.id}_K${s}_${cd.time}" 
                                               value="${detailMap[key] != null ? detailMap[key] : ''}" />
                                    </td>
                                </c:forEach>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <button type="submit">Update</button>
        </form>
        <c:forEach var="date" items="${dateList}">
            <p>${date}</p>
        </c:forEach>
    </body>
</html>

