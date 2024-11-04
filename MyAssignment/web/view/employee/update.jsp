<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Update Employee</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../view/style/newcss.css"> <!-- Kết nối CSS tùy chỉnh -->
</head>
<body>
    <div class="container mt-5">
        <h1>Update Employee</h1>
        <form action="update" method="POST">
            <input type="hidden" name="eid" value="${requestScope.e.id}"/>
            
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" class="form-control" name="name" value="${requestScope.e.name}"/>
            </div>

            <div class="form-group">
                <label for="dept">Department:</label>
                <select class="form-control" name="dept">
                    <c:forEach items="${requestScope.depts}" var="d">
                        <option value="${d.id}" ${requestScope.e.dept.id eq d.id ? "selected=\"selected\"" : ""}>
                            ${d.name}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="pNumber">Phone number:</label>
                <input type="text" class="form-control" name="pNumber" value="${requestScope.e.phoneNumber}"/>
            </div>

            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" class="form-control" name="address" value="${requestScope.e.address}"/>
            </div>

            <div class="form-group">
                <label for="salary">Salary:</label>
                <select class="form-control" name="salary">
                    <c:forEach items="${requestScope.salarys}" var="s">
                        <option value="${s.id}" ${requestScope.e.salary.id eq s.id ? "selected=\"selected\"" : ""}>
                            ${s.level} - ${s.salary}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit" class="btn btn-primary">Save</button>
            <a href="../employee/view" class="btn btn-secondary">Cancel</a>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
