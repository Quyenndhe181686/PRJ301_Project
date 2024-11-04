<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update Plan Detail</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background-color: rgb(235, 235, 235);
        }
        .container {
            margin-top: 20px;
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
        }
        table th, table td {
            text-align: center;
            vertical-align: middle;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Update Plan Detail</h1>

        <form action="update" method="POST">
            <input type="hidden" name="planId" value="${plan.id}"/>

            <table class="table table-bordered">
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
                                        <input type="text" class="form-control" name="quantity_${p.id}_K${s}_${cd.time}" 
                                               value="${detailMap[key] != null ? detailMap[key] : ''}" />
                                    </td>
                                </c:forEach>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <button type="submit" class="btn btn-primary">Update</button>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
