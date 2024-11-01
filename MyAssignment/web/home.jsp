<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <header class="bg-primary text-white p-3">
        <div class="container">
            <h1 class="h3">Welcome, ${userName}!</h1>
            <p class="lead">Role-based Access Control System</p>
            <nav class="nav">
                <a href="home" class="nav-link text-white">Home</a>
                <a href="profile.jsp" class="nav-link text-white">Profile</a>
                <a href="logout.jsp" class="nav-link text-white">Logout</a>
            </nav>
        </div>
    </header>

    <main class="container my-5">
        <section class="features card shadow-sm p-4">
            <h2 class="card-title text-center text-primary mb-4">Accessible Features</h2>
            <p class="text-muted text-center">Below are the accessible features based on your roles:</p>
            <ul class="list-group list-group-flush mt-3">
                <c:forEach var="feature" items="${features}">
                    <li class="list-group-item">
                        <a href="${feature.url}" class="text-decoration-none text-dark">${feature.name}</a>
                    </li>
                </c:forEach>
            </ul>
        </section>
    </main>

    <footer class="bg-primary text-white text-center py-3">
        <p>&copy; 2024 Your Company. All rights reserved.</p>
    </footer>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
