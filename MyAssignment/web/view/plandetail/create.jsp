<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <title>Plan Detail</title>
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
            <h1>Plan Detail for ${plan.name}</h1>

            <form action="${pageContext.request.contextPath}/plandetails/create" method="post">
                <input type="hidden" name="planId" value="${plan.id}"/>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <c:forEach var="product" items="${products}">
                                <th colspan="3">${product.name}</th>
                            </c:forEach>
                        </tr>
                        <tr>
                            <th></th>
                            <c:forEach var="product" items="${products}">
                                <th>K1</th>
                                <th>K2</th>
                                <th>K3</th>
                            </c:forEach>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="date" items="${sessionScope.dateList}">
                            <tr>
                                <td><fmt:formatDate value="${date}" pattern="dd/MM/yyyy" /></td>
                                <c:forEach var="product" items="${products}">
                                    <c:forEach var="shift" begin="1" end="3">
                                        <td>
                                            <c:set var="key" value="${date}_${product.id}_${shift}" />
                                            <input type="text" name="shift${shift}_quantity_${date}_${product.name}"
                                                   class="form-control"
                                                   value="${detailMap[key] != null ? detailMap[key] : ''}" />
                                        </td>
                                    </c:forEach>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <button type="submit" class="btn btn-primary">Save Shift Quantities</button>
            </form>
            <c:if test="${not empty errors}">
                <div class="alert alert-danger mt-3">
                    <strong>Có lỗi xảy ra:</strong>
                    <p>${errors}</p>
                </div>
            </c:if>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
