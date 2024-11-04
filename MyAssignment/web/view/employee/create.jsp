<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create Employee</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../view/style/newcss.css">
</head>
<body>
    <div class="container mt-5">
        <h1>Create Employee</h1>
        <form action="add" method="POST">
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" class="form-control" name="name" required/>
            </div>
            <div class="form-group">
                <label for="dept">Department:</label>
                <select class="form-control" name="dept" required>
                    <c:forEach items="${requestScope.depts}" var="d">
                        <option value="${d.id}">${d.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="pNumber">Phone Number:</label>
                <input type="text" class="form-control" name="pNumber" required/>
            </div>
            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" class="form-control" name="address" required/>
            </div>
            <div class="form-group">
                <label for="salary">Salary:</label>
                <select class="form-control" name="salary" required>
                    <c:forEach items="${requestScope.salarys}" var="s">
                        <option value="${s.id}">${s.level} - ${s.salary}</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </form>
    </div>
</body>
</html>
