# 🏠 StayNest

LIVE DEMO LINK : https://staynest-eeak.onrender.com


StayNest is a web-based PG (Paying Guest) accommodation management and discovery application built using Java and Spring Boot.

The application allows users to browse PGs, search PGs by location, view rooms and availability, register and log in, book rooms, view booking history, cancel bookings, and submit reviews.

The project demonstrates practical backend development using Spring Boot, Spring MVC, Spring Data JPA, Hibernate, PostgreSQL, Spring Security, JWT authentication, JSP, DTOs, validation, exception handling, and unit testing.

---

## 🎯 Project Objective

The main objective of StayNest is to build a real-world accommodation management application while applying backend development concepts.

The application focuses on:

- PG listing and searching
- Room management
- Room availability
- User registration and login
- JWT-based authentication
- Role-based authorization
- Booking management
- Booking cancellation
- Booking history
- PG reviews
- REST APIs
- JSP-based frontend
- DTO-based data transfer
- Jakarta Bean Validation
- Global exception handling
- Transaction management
- Unit testing

---

## 🚀 Features

### 1. PG Management

Users can:

- View all PGs
- View individual PG details
- Search PGs by location
- View PG images
- View PG rent and location

Each PG contains:

- Name
- Location
- Rent
- Image URL

---

### 2. Room Management

Each PG can have multiple rooms.

Example:

```text
StayNest PG
│
├── Single Sharing
├── Double Sharing
├── Triple Sharing
├── Four Sharing
└── Six Sharing
```

Each Room contains:

- Room type (Single, Double, Triple, etc.)
- Rent per month
- Total rooms
- Available rooms
- Image URL

---

### 3. User Management

- Users can register with name, email, and password.
- Passwords are encrypted using **BCrypt**.
- Users can log in and receive a **JWT token** stored as an HTTP-only cookie.
- Role-based access: `USER` and `ADMIN`.

---

### 4. Booking Management

Users can:

- Book an available room.
- View all their bookings.
- Cancel an existing booking.
- View booking details (Booking ID, Date, Status, Room).

Room availability automatically decreases on booking and increases on cancellation.

---

### 5. Review Management

Users can:

- Submit a review for any PG.
- Rate a PG from 1 to 5 stars.
- Write a comment/feedback.
- View all reviews on the PG details page.

---

### 6. Search

Users can search PGs by location using a search bar on the home page.

---

## 🛠️ Technology Stack

| Category | Technology |
|:---|:---|
| **Language** | Java 21 |
| **Framework** | Spring Boot 4.x |
| **Web Layer** | Spring MVC |
| **ORM** | Spring Data JPA + Hibernate |
| **Database** | PostgreSQL |
| **Security** | Spring Security + JWT (JJWT 0.12.6) |
| **Frontend** | JSP (JavaServer Pages) + JSTL |
| **Styling** | CSS (Custom) |
| **Build Tool** | Maven |
| **Server** | Embedded Apache Tomcat |
| **Validation** | Jakarta Bean Validation |
| **Testing** | JUnit 5 + Selenium |

---

## 📁 Project Structure

```text
StayNest/
├── src/
│   ├── main/
│   │   ├── java/com/tap/staynest/
│   │   │   ├── config/
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   ├── PGController.java
│   │   │   │   ├── ReviewController.java
│   │   │   │   ├── RoomController.java
│   │   │   │   └── ViewController.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── WebExceptionHandler.java
│   │   │   │   └── (Custom Exceptions)
│   │   │   ├── model/
│   │   │   │   ├── Booking.java
│   │   │   │   ├── PG.java
│   │   │   │   ├── Review.java
│   │   │   │   ├── Room.java
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── BookingService.java
│   │   │   │   ├── JwtService.java
│   │   │   │   ├── PGService.java
│   │   │   │   ├── ReviewService.java
│   │   │   │   ├── RoomService.java
│   │   │   │   └── UserService.java
│   │   │   └── StayNestApplication.java
│   │   ├── resources/
│   │   │   ├── static/css/
│   │   │   └── application.properties
│   │   └── webapp/WEB-INF/views/
│   │       ├── home.jsp
│   │       ├── pg-details.jsp
│   │       ├── booking.jsp
│   │       ├── booking-success.jsp
│   │       ├── bookings.jsp
│   │       ├── review.jsp
│   │       ├── login.jsp
│   │       ├── register.jsp
│   │       └── error.jsp
│   └── test/
├── pom.xml
└── README.md
```

---

## ⚙️ Setup & Run

### Prerequisites

- Java 21
- Maven 3.x
- PostgreSQL 14+
- Eclipse IDE (or IntelliJ IDEA)

### 1. Clone the Repository

```bash
git clone https://github.com/Ramesh-Taradi/StayNest.git
cd StayNest
```

### 2. Create PostgreSQL Database

Open pgAdmin or SQL Shell (psql) and run:

```sql
CREATE DATABASE staynestdb;
```

### 3. Configure `application.properties`

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/staynestdb
spring.datasource.username=postgres
spring.datasource.password=YOUR_DB_PASSWORD
```

### 4. Run the Application

In Eclipse:
- Right-click `StayNestApplication.java`
- Select **Run As** ➔ **Spring Boot App**

Or via Maven:

```bash
mvn spring-boot:run
```

### 5. Open in Browser

```
http://localhost:8080/
```

---

## 🌐 Application Pages

| Page | URL |
|:---|:---|
| Home | `http://localhost:8080/` |
| Register | `http://localhost:8080/register` |
| Login | `http://localhost:8080/login` |
| PG Details | `http://localhost:8080/pgs/{id}` |
| Book a Room | `http://localhost:8080/bookings/new?roomId={id}` |
| Booking History | `http://localhost:8080/bookings` |
| Write a Review | `http://localhost:8080/pgs/{pgId}/reviews/new` |

---

## 🔐 REST API Endpoints

### Auth
| Method | Endpoint | Description |
|:---|:---|:---|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login and receive JWT |

### PG
| Method | Endpoint | Description |
|:---|:---|:---|
| GET | `/api/pgs` | Get all PGs |
| GET | `/api/pgs/{id}` | Get PG by ID |
| POST | `/api/pgs` | Add a new PG |
| PUT | `/api/pgs/{id}` | Update a PG |
| DELETE | `/api/pgs/{id}` | Delete a PG |

### Rooms
| Method | Endpoint | Description |
|:---|:---|:---|
| GET | `/api/pgs/{pgId}/rooms` | Get rooms of a PG |
| POST | `/api/pgs/{pgId}/rooms` | Add room to a PG |
| PUT | `/api/rooms/{id}` | Update a room |
| DELETE | `/api/rooms/{id}` | Delete a room |

### Bookings
| Method | Endpoint | Description |
|:---|:---|:---|
| GET | `/api/bookings` | Get all bookings |
| POST | `/api/bookings` | Create a booking |
| PUT | `/api/bookings/{id}/cancel` | Cancel a booking |

### Reviews
| Method | Endpoint | Description |
|:---|:---|:---|
| GET | `/api/pgs/{pgId}/reviews` | Get reviews of a PG |
| POST | `/api/pgs/{pgId}/reviews` | Add a review |
| PUT | `/api/reviews/{id}` | Update a review |
| DELETE | `/api/reviews/{id}` | Delete a review |

---

## 👨‍💻 Author

**Ramesh Taradi**  
GitHub: [@Ramesh-Taradi](https://github.com/Ramesh-Taradi)

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
