package com.tap.staynest.repository;

import com.tap.staynest.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.user IS NOT NULL AND LOWER(b.user.email) = LOWER(:email) ORDER BY b.bookingDate DESC, b.id DESC")
    List<Booking> findByUserEmail(@Param("email") String email);
}
