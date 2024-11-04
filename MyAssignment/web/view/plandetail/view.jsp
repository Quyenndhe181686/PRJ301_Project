<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Plan Details</title>
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
    </style>
</head>
<body>
<div class="container">
    <h1>Plan Details</h1>

    <table class="table table-bordered">
        <thead>
            <tr>
                <th>Date</th>
                <th>Product ID</th>
                <th>Product Name</th>
                <th>Shift</th>
                <th>Quantity</th>
                <th>Note</th>
                <th>Attendance</th> <!-- Cột Attendance mới -->
            </tr>
        </thead>
        <tbody>
            <c:forEach var="detail" items="${sessionScope.planDetails}" varStatus="status">
                <tr>
                    <td><fmt:formatDate value="${sessionScope.dateList[status.index]}" pattern="dd/MM/yyyy" /></td>
                    <td>${detail.planHeader.product.id}</td>
                    <td>${detail.planHeader.product.name}</td>
                    <td>${detail.shift.id == 1 ? "K1" : detail.shift.id == 2 ? "K2" : "K3"}</td>
                    <td>${detail.quantity}</td>
                    <td>
                        <form action="<c:url value='../work/assign'/>" method="GET">
                            <input type="hidden" name="pdid" value="${detail.planHeader.id}"/>
                            <button type="submit" class="btn btn-primary">Assign</button>
                        </form>
                    </td>
                    <td>
                        <a href="<c:url value='../attendance/take'/>?shift=${detail.shift.id}&date=${sessionScope.dateList[status.index]}" class="btn btn-secondary">Take Attendance</a>
                    </td> 
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
