<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>View Plans</title>
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
        <h1>Plan List</h1>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Department Name</th>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Effort</th>
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
                                <input type="text" value="${h.product.name}" readonly class="form-control"/>
                                <br/>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach var="h2" items="${plan.headers}">
                                <input type="text" value="${h2.quantity}" readonly class="form-control"/>
                                <br/>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach var="h3" items="${plan.headers}">
                                <input type="text" value="${h3.estimatedEffort}" readonly class="form-control"/>
                                <br/>
                            </c:forEach>
                        </td>
                        <td>
                            <div class="btn-group" role="group">
                                <form action="<c:url value='../plan/update'/>" method="GET" style="display:inline;">
                                    <input type="hidden" name="planId" value="${plan.id}"/>
                                    <button type="submit" class="btn btn-warning">Update</button>
                                </form>
                                <form action="<c:url value='../plandetails/view'/>" method="POST" style="display:inline;">
                                    <input type="hidden" name="planId" value="${plan.id}"/>
                                    <button type="submit" class="btn btn-info">Edit</button>
                                </form>
                                <form action="<c:url value='../plandetails/view'/>" method="GET" style="display:inline;">
                                    <input type="hidden" name="planId" value="${plan.id}"/>
                                    <button type="submit" class="btn btn-success">List Plan Detail</button>
                                </form>
                            </div>
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
