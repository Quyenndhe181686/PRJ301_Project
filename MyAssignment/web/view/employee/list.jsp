<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Employee List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../view/style/list1.css">
</head>
<body>
    <div class="container mt-5">
        <h1>Employee List</h1>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Department</th>
                    <th>Phone Number</th>
                    <th>Address</th>
                    <th>Salary</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${requestScope.list}" var="l">
                    <tr>
                        <td>${l.id}</td>
                        <td>${l.name}</td>
                        <td>${l.dept.id}</td>
                        <td>${l.phoneNumber}</td>
                        <td>${l.address}</td>
                        <td>${l.salary.id}</td>
                        <td>
                            <a href="update?id=${l.id}" class="btn btn-warning">Edit</a>
                            <input type="button" value="Remove" class="btn btn-danger" onclick="removeEmployee(${l.id})"/>
                            <form id="formRemove${l.id}" action="delete" method="POST" style="display: none;"> 
                                <input type="hidden" name="id" value="${l.id}"/>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <script>
        function removeEmployee(id) {
            var result = confirm("Are you sure you want to remove this employee?");
            if (result) {
                document.getElementById("formRemove" + id).submit();
            }
        }
    </script>
</body>
</html>
