<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>StayNest | Find Your Perfect Stay</title>
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
    <section class="hero">
        <span class="hero-badge">Comfort • Safety • Value</span>
        <h1>Find a PG that feels like home</h1>
        <p>Discover verified rooms, trusted stays, and comfortable spaces near your preferred location.</p>

        <form action="/search" method="get" class="search-form">
           <input type="text"
                  name="location"
                  placeholder="Enter location..."
                  value="${searchLocation}"
                  class="search-input"
                  required>
           <button type="submit" class="button">Search</button>
        </form>
    </section>

    <div class="info-bar">
        <div class="info-pill">
           <strong>1200+</strong>
           <span>Verified listings</span>
        </div>
        <div class="info-pill">
           <strong>4.8/5</strong>
           <span>User ratings</span>
        </div>
        <div class="info-pill">
           <strong>24/7</strong>
           <span>Support</span>
        </div>
    </div>

    <div class="section-header">
        <c:choose>
           <c:when test="${not empty searchLocation}">
               <h2>PGs in "${searchLocation}"</h2>
           </c:when>
           <c:otherwise>
               <h2>Available PGs</h2>
           </c:otherwise>
        </c:choose>
    </div>

    <div class="pg-grid">
        <c:choose>
           <c:when test="${not empty pgs}">
               <c:forEach var="pg" items="${pgs}">
                   <article class="pg-card">
                       <img src="${pg.imageUrl}" alt="${pg.name}" class="pg-image">
                       <div class="pg-content">
                           <h3 class="pg-name">${pg.name}</h3>
                           <p class="location">📍 ${pg.location}</p>
                           <p class="rent">₹${pg.rent} / month</p>
                           <a href="/pgs/${pg.id}" class="button">View PG</a>
                       </div>
                   </article>
               </c:forEach>
           </c:when>
           <c:otherwise>
               <div class="empty-state">
                   <p>No PGs found in this location. Try another city or area.</p>
               </div>
           </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>
