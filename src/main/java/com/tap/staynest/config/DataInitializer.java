package com.tap.staynest.config;

import com.tap.staynest.model.Booking;
import com.tap.staynest.model.PG;
import com.tap.staynest.model.Review;
import com.tap.staynest.model.Room;
import com.tap.staynest.model.User;
import com.tap.staynest.repository.BookingRepository;
import com.tap.staynest.repository.PGRepository;
import com.tap.staynest.repository.ReviewRepository;
import com.tap.staynest.repository.RoomRepository;
import com.tap.staynest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PGRepository pgRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        seedPGsIfEmpty();
        seedBookingsIfEmpty();
    }

    private void seedPGsIfEmpty() {
        if (pgRepository.count() >= 18) {
            return;
        }

        createPGIfNotExists("Maple Stay PG", "Marathahalli, Bangalore", new BigDecimal("7500"),
                "https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?w=500",
                "Triple Sharing", new BigDecimal("7500"), 6, 3,
                "Double Sharing", new BigDecimal("9500"), 4, 2,
                "Rohan Sharma", 5, "Very affordable and great food options around!");

        createPGIfNotExists("Comfort Rooms PG", "Whitefield, Bangalore", new BigDecimal("10500"),
                "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=500",
                "Single Sharing", new BigDecimal("10500"), 5, 2,
                "Double Sharing", new BigDecimal("8500"), 8, 4,
                "Sneha Patil", 4, "Close to IT parks with high-speed Wi-Fi.");

        createPGIfNotExists("Serene Haven PG", "HSR Layout, Bangalore", new BigDecimal("8500"),
                "https://images.unsplash.com/photo-1540518614846-7ede433c4550?w=500",
                "Double Sharing", new BigDecimal("8500"), 6, 3,
                "Single Sharing", new BigDecimal("11000"), 4, 1,
                "Karthik V", 5, "Peaceful neighborhood and daily housekeeping.");

        createPGIfNotExists("Zenith Luxury PG", "Indiranagar, Bangalore", new BigDecimal("12000"),
                "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=500",
                "Single Sharing", new BigDecimal("12000"), 4, 2,
                "Double Sharing", new BigDecimal("9500"), 6, 3,
                "Pooja Nair", 5, "Premium furnishings and gym facilities included.");

        createPGIfNotExists("Silicon Oasis PG", "Electronic City, Bangalore", new BigDecimal("6800"),
                "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?w=500",
                "Triple Sharing", new BigDecimal("6800"), 8, 4,
                "Double Sharing", new BigDecimal("8200"), 6, 2,
                "Ankit Verma", 4, "Best value for tech professionals in E-City.");

        createPGIfNotExists("Lotus Grand PG", "Bellandur, Bangalore", new BigDecimal("9200"),
                "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=500",
                "Double Sharing", new BigDecimal("9200"), 5, 3,
                "Single Sharing", new BigDecimal("11500"), 3, 1,
                "Divya Reddy", 5, "Very close to Ecospace and delicious home-cooked meals.");

        createPGIfNotExists("Prime Living PG", "Koramangala, Bangalore", new BigDecimal("11500"),
                "https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?w=500",
                "Single Sharing", new BigDecimal("11500"), 4, 2,
                "Double Sharing", new BigDecimal("9000"), 6, 3,
                "Abhishek Joshi", 5, "Modern amenities, spacious study desks, and 24/7 power backup.");

        createPGIfNotExists("Silver Oak PG", "HSR Layout, Bangalore", new BigDecimal("8000"),
                "https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?w=500",
                "Double Sharing", new BigDecimal("8000"), 6, 4,
                "Triple Sharing", new BigDecimal("6500"), 4, 2,
                "Megha Gupta", 4, "Clean and hygienic, friendly wardens and fast internet.");

        createPGIfNotExists("Cyber City PG", "Electronic City, Bangalore", new BigDecimal("7200"),
                "https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?w=500",
                "Double Sharing", new BigDecimal("7200"), 8, 5,
                "Single Sharing", new BigDecimal("9000"), 4, 2,
                "Suresh Kumar", 4, "Walking distance to metro and major IT companies.");

        createPGIfNotExists("Bloom Residency PG", "Whitefield, Bangalore", new BigDecimal("9800"),
                "https://images.unsplash.com/photo-1560185007-cde436f6a4d0?w=500",
                "Single Sharing", new BigDecimal("9800"), 4, 2,
                "Double Sharing", new BigDecimal("8000"), 6, 3,
                "Tanvi Shah", 5, "Well ventilated rooms with attached balcony and parking.");
    }

    private void createPGIfNotExists(String name, String location, BigDecimal rent, String imageUrl,
                                     String room1Type, BigDecimal room1Rent, int room1Total, int room1Avail,
                                     String room2Type, BigDecimal room2Rent, int room2Total, int room2Avail,
                                     String reviewerName, int rating, String comment) {
        List<PG> existing = pgRepository.findByLocationContainingIgnoreCase(location);
        boolean alreadyExists = existing.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name));
        if (alreadyExists) {
            return;
        }

        PG pg = new PG();
        pg.setName(name);
        pg.setLocation(location);
        pg.setRent(rent);
        pg.setImageUrl(imageUrl);
        PG savedPG = pgRepository.save(pg);

        Room r1 = new Room();
        r1.setType(room1Type);
        r1.setRent(room1Rent);
        r1.setTotalRooms(room1Total);
        r1.setAvailableRooms(room1Avail);
        r1.setImageUrl(imageUrl);
        r1.setPg(savedPG);
        roomRepository.save(r1);

        Room r2 = new Room();
        r2.setType(room2Type);
        r2.setRent(room2Rent);
        r2.setTotalRooms(room2Total);
        r2.setAvailableRooms(room2Avail);
        r2.setImageUrl(imageUrl);
        r2.setPg(savedPG);
        roomRepository.save(r2);

        Review review = new Review();
        review.setName(reviewerName);
        review.setRating(rating);
        review.setComment(comment);
        review.setPg(savedPG);
        reviewRepository.save(review);
    }

    private void seedBookingsIfEmpty() {
        User user = userRepository.findByEmail("ramesh@gmail.com").orElseGet(() -> {
            User u = new User();
            u.setName("Ramesh Taradi");
            u.setEmail("ramesh@gmail.com");
            u.setPassword(new BCryptPasswordEncoder().encode("password123"));
            u.setRole("USER");
            return userRepository.save(u);
        });

        // Ensure Ramesh's 4 bookings exist and are properly mapped
        List<Booking> userBookings = bookingRepository.findByUserEmail("ramesh@gmail.com");

        boolean hasSunrise = userBookings.stream().anyMatch(b -> b.getRoom() != null && b.getRoom().getPg() != null && "Sunrise PG".equalsIgnoreCase(b.getRoom().getPg().getName()));
        boolean hasGreenValley = userBookings.stream().anyMatch(b -> b.getRoom() != null && b.getRoom().getPg() != null && "Green Valley PG".equalsIgnoreCase(b.getRoom().getPg().getName()));
        boolean hasCityView = userBookings.stream().anyMatch(b -> b.getRoom() != null && b.getRoom().getPg() != null && "City View PG".equalsIgnoreCase(b.getRoom().getPg().getName()));
        boolean hasRoyal = userBookings.stream().anyMatch(b -> b.getRoom() != null && b.getRoom().getPg() != null && "Royal Residency PG".equalsIgnoreCase(b.getRoom().getPg().getName()));

        // 1. Sunrise PG (Confirmed, 2026-10-04, 88525322512)
        if (!hasSunrise) {
            Room sunriseRoom = findRoomByPgNameAndType("Sunrise PG", "Double");
            if (sunriseRoom != null) {
                createBooking(sunriseRoom, user, "Ramesh Taradi", "88525322512", LocalDate.of(2026, 10, 4), "CONFIRMED");
            }
        }

        // 2. Green Valley PG (Cancelled, 2026-10-02, 8150018127)
        if (!hasGreenValley) {
            Room greenValleyRoom = findRoomByPgNameAndType("Green Valley PG", "Single");
            if (greenValleyRoom != null) {
                createBooking(greenValleyRoom, user, "Ramesh Taradi", "8150018127", LocalDate.of(2026, 10, 2), "CANCELLED");
            }
        }

        // 3. City View PG (Completed, 2026-09-15, 9845012345)
        if (!hasCityView) {
            Room cityViewRoom = findRoomByPgNameAndType("City View PG", "Double");
            if (cityViewRoom != null) {
                createBooking(cityViewRoom, user, "Ramesh Taradi", "9845012345", LocalDate.of(2026, 9, 15), "COMPLETED");
            }
        }

        // 4. Royal Residency PG (Confirmed, 2026-09-10, 88525322512)
        if (!hasRoyal) {
            Room royalRoom = findRoomByPgNameAndType("Royal Residency PG", "Single");
            if (royalRoom != null) {
                createBooking(royalRoom, user, "Ramesh Taradi", "88525322512", LocalDate.of(2026, 9, 10), "CONFIRMED");
            }
        }
    }

    private Room findRoomByPgNameAndType(String pgName, String type) {
        return roomRepository.findAll().stream()
                .filter(r -> r.getPg() != null && r.getPg().getName().trim().equalsIgnoreCase(pgName.trim()))
                .filter(r -> type == null || (r.getType() != null && r.getType().toLowerCase().contains(type.toLowerCase())))
                .findFirst()
                .orElseGet(() -> findRoomByPgName(pgName));
    }

    private Room findRoomByPgName(String pgName) {
        return roomRepository.findAll().stream()
                .filter(r -> r.getPg() != null && r.getPg().getName().trim().equalsIgnoreCase(pgName.trim()))
                .findFirst()
                .orElse(null);
    }

    private void createBooking(Room room, User user, String name, String phone, LocalDate date, String status) {
        Booking b = new Booking();
        b.setRoom(room);
        b.setUser(user);
        b.setName(name);
        b.setPhone(phone);
        b.setBookingDate(date);
        b.setStatus(status);
        bookingRepository.save(b);
    }
}
