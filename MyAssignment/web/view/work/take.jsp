<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Attendance</title>
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
    <h1>Attendance for ${date} - Shift ${shift}</h1>

    <form action="take" method="POST">
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Employee ID</th>
                    <th>Full Name</th>
                    <th>Product ID</th>
                    <th>Product Name</th>
                    <th>Quantity</th>
                    <th>Actual Quantity</th>
                    <th>Alpha</th>
                    <th>Note</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="assignment" items="${assignments}">
                    <tr>
                        <td>${assignment.e.id}</td>
                        <td>${assignment.e.name}</td>
                        <td>${assignment.product.id}</td>
                        <td>${assignment.product.name}</td>
                        <td>${assignment.quantity}</td>
                        <td>
                            <input type="number" class="form-control" name="actualQuantity_${assignment.id}" min="0" />
                        </td>
                        <td>
                            <input type="text" class="form-control" name="alpha_${assignment.id}" />
                        </td>
                        <td>
                            <input type="text" class="form-control" name="note_${assignment.id}" />
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <input type="hidden" name="date" value="${date}" />
        <input type="hidden" name="shift" value="${shift}" />
        <button type="submit" class="btn btn-primary">Submit Attendance</button>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
