<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create Production Plan</title>
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
        <h1>Create Production Plan</h1>
        <form action="create" method="POST">
            <div class="form-group">
                <label for="name">Plan Name:</label>
                <input type="text" class="form-control" name="name" required/>
            </div>

            <div class="form-group">
                <label for="from">From:</label>
                <input type="date" class="form-control" name="from" required/>
            </div>

            <div class="form-group">
                <label for="to">To:</label>
                <input type="date" class="form-control" name="to" required/>
            </div>

            <div class="form-group">
                <label for="did">Workshop:</label>
                <select class="form-control" name="did" required>
                    <c:forEach items="${requestScope.depts}" var="d">
                        <option value="${d.id}">${d.name}</option>
                    </c:forEach>
                </select>
            </div>

            <h4>Products</h4>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Estimated Effort</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${requestScope.products}" var="p">
                        <tr>
                            <td>${p.name}<input type="hidden" name="pid" value="${p.id}"></td>
                            <td><input type="number" class="form-control" name="quantity${p.id}" required/></td>
                            <td><input type="number" class="form-control" name="effort${p.id}" required/></td>
                        </tr>    
                    </c:forEach>
                </tbody>
            </table>
            <button type="submit" class="btn btn-primary">Save</button>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
