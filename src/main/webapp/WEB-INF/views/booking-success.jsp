<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Booking Successful - StayNest</title>
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

<div class="success-container">
    <h1>Booking Successful! 🎉</h1>
    <p>Your booking has been confirmed.</p>
    <hr>

    <p><strong>Booking ID:</strong> ${booking.id}</p>
    <p><strong>Name:</strong> ${booking.name}</p>
    <p><strong>Room ID:</strong> ${booking.roomId}</p>
    <p><strong>Status:</strong> ${booking.status}</p>
    <p><strong>Booking Date:</strong> ${booking.bookingDate}</p>

    <br>

    <form action="/bookings/${booking.id}/cancel" method="post">
        <button type="submit" class="button">Cancel Booking</button>
    </form>

    <br>
    <a href="/" class="button">Back to PGs</a>
</div>

</body>
</html>
