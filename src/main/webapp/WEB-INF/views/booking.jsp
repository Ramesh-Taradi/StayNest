<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Room - StayNest</title>
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
    <h2>Book ${room.type} Sharing</h2>
    <img src="${room.imageUrl}" alt="${room.type}" class="room-image">
    <p class="rent">₹${room.rent} / month</p>
    <p>Available: ${room.availableRooms} / ${room.totalRooms}</p>

    <hr>

    <form action="/bookings" method="post">
        <input type="hidden" name="roomId" value="${room.id}">

        <div class="form-group">
            <label>Name</label>
            <input type="text" name="name" class="form-input" required>
        </div>

        <div class="form-group">
            <label>Phone</label>
            <input type="text" name="phone" class="form-input" required>
        </div>

        <button type="submit" class="button">Book Now</button>
    </form>

    <br>
    <a href="/pgs/${room.pgId}">Back to PG</a>
</div>

</body>
</html>
