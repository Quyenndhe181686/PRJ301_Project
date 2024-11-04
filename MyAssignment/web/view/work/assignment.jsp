<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>View Assignments</title>
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
        h2 {
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Assignment List</h2>

    <table class="table table-bordered">
        <thead>
            <tr>
                <th>Employee Id</th>
                <th>Employee Name</th>
                <th>Ordered Quantity</th>
                <th>Action</th> <!-- Thêm cột Action -->
            </tr>
        </thead>
        <tbody>
            <c:forEach var="assignment" items="${assignments}">
                <tr>
                    <td>${assignment.e.id}</td>
                    <td>${assignment.e.name}</td>
                    <td>${assignment.quantity}</td>
                    <td> <!-- Cột cho nút Delete -->
                        <form action="delete" method="POST" style="display: inline;">
                            <input type="hidden" name="assignmentId" value="${assignment.id}"/> 
                            <input type="hidden" name="pdid" value="${pdid}"/> 
                            <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this assignment?');">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
            <strong>${errorMessage}</strong>
        </div>
    </c:if>
    <h2>Add Work Assignment</h2>
    <form action="assign" method="POST">
        <div class="form-group">
            <label for="employeeId">Select Employee:</label>
            <select class="form-control" name="employeeId" id="employeeId">
                <c:forEach var="e" items="${requestScope.employees}">
                    <option value="${e.id}">${e.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="quantity">Ordered Quantity:</label>
            <input type="number" class="form-control" name="quantity" id="quantity" min="1" required/>
        </div>

        <input type="hidden" name="pdid" value="${pdid}"/> <!-- Giả sử bạn cũng cần gửi pdid để xử lý -->
        <button type="submit" class="btn btn-primary">Add</button>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
