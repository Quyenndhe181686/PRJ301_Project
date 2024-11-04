<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <header class="bg-primary text-white p-3">
        <div class="container">
            <h1 class="h3">Welcome, ${userName}!</h1>
            <nav class="nav">
                <a href="../employee/view" class="nav-link text-white">View Employee</a>
                <a href="../employee/create" class="nav-link text-white">Create Employee</a>
                <a href="../plan/add" class="nav-link text-white">Plan Create</a>
                <a href="profile.jsp" class="nav-link text-white">Profile</a>
                <a href="logout.jsp" class="nav-link text-white">Logout</a>
            </nav>
        </div>
    </header>

    <main class="container my-5">
        <section class="products card shadow-sm p-4">
            <h2 class="card-title text-center text-primary mb-4">Product List</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Product Name</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${products}">
                        <tr>
                            <td>${product.name}</td>
                            <td>${product.description}</td> <!-- Giả sử product có thuộc tính description -->
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>
    </main>

    <footer class="bg-primary text-white text-center py-3">
        <p>&copy; 2024 Your Company. All rights reserved.</p>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
