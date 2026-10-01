<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Review - StayNest</title>
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

<div class="form-container">
    <h2>Review ${pg.name}</h2>

    <form action="/pgs/${pg.id}/reviews" method="post">
        <div class="form-group">
            <label>Name</label>
            <input type="text" name="name" class="form-input" required>
        </div>

        <div class="form-group">
            <label>Rating</label>
            <select name="rating" class="form-input" required>
                <option value="5">⭐⭐⭐⭐⭐</option>
                <option value="4">⭐⭐⭐⭐</option>
                <option value="3">⭐⭐⭐</option>
                <option value="2">⭐⭐</option>
                <option value="1">⭐</option>
            </select>
        </div>

        <div class="form-group">
            <label>Comment</label>
            <textarea name="comment" class="form-input" rows="5" required></textarea>
        </div>

        <button type="submit" class="button">Submit Review</button>
    </form>

    <br>
    <a href="/pgs/${pg.id}">Back to PG</a>
</div>

</body>
</html>
