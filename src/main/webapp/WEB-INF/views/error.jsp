<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>StayNest | Error</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<nav class="navbar">
    <a href="/" class="logo">StayNest</a>
    <div class="nav-links">
        <a href="/" class="nav-link">Home</a>
        <a href="/bookings" class="nav-link">Bookings</a>
    </div>
</nav>

<div class="container">
    <div class="error-container">
        <div class="error-badge">Error ${status}</div>
        <h1>Something went wrong</h1>
        <p class="error-message"><c:out value="${message}" /></p>
        <p class="error-meta">Time: <c:out value="${timestamp}" /></p>
        <a href="/" class="button">Back to Home</a>
    </div>
</div>

</body>
</html>
