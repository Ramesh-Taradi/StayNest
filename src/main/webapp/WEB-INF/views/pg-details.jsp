<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${pg.name} - StayNest</title>
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
    <!-- PG details -->
    <div class="pg-details">
        <h1>${pg.name}</h1>
        <img src="${pg.imageUrl}" alt="${pg.name}" class="pg-details-image">
        <p class="location">📍 ${pg.location}</p>
        <p class="rent">₹${pg.rent} / month</p>
    </div>

    <!-- Rooms -->
    <h2>Available Rooms</h2>
    <div class="room-grid">
        <c:forEach var="room" items="${rooms}">
            <div class="room-card">
                <img src="${room.imageUrl}" alt="${room.type}" class="room-image">
                <div class="room-content">
                    <h3>${room.type} Sharing</h3>
                    <p class="rent">₹${room.rent} / month</p>
                    <p class="available">Available: ${room.availableRooms} / ${room.totalRooms}</p>
                    <c:choose>
                        <c:when test="${room.availableRooms > 0}">
                            <a href="/bookings/new?roomId=${room.id}" class="button">Book Now</a>
                        </c:when>
                        <c:otherwise>
                            <p>Not Available</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Reviews -->
    <div class="review-section">
        <h2>Reviews</h2>
        <c:choose>
            <c:when test="${not empty reviews}">
                <c:forEach var="review" items="${reviews}">
                    <div class="review-card">
                        <h3>${review.name}</h3>
                        <p>⭐ ${review.rating} / 5</p>
                        <p>${review.comment}</p>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>No reviews yet.</p>
            </c:otherwise>
        </c:choose>

        <a href="/pgs/${pg.id}/reviews/new" class="button">Write a Review</a>
    </div>

    <br>
    <a href="/" class="button">Back to PGs</a>
</div>

</body>
</html>
