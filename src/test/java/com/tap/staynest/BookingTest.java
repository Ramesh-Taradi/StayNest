package com.tap.staynest;

import com.tap.staynest.dto.request.BookingRequestDTO;
import com.tap.staynest.dto.response.BookingResponseDTO;
import com.tap.staynest.exception.RoomNotFoundException;
import com.tap.staynest.exception.RoomUnavailableException;
import com.tap.staynest.model.Booking;
import com.tap.staynest.model.Room;
import com.tap.staynest.repository.BookingRepository;
import com.tap.staynest.repository.RoomRepository;
import com.tap.staynest.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingService bookingService;

    private Room room;
    private Booking booking;
    private BookingRequestDTO requestDTO;

    // -------------------------------------------------------
    // Setup: Runs before every test to prepare common data
    // -------------------------------------------------------
    @BeforeEach
    void setUp() {
        // Create a sample Room
        room = new Room();
        room.setId(1L);
        room.setType("Single");
        room.setRent(new BigDecimal("5000"));
        room.setTotalRooms(5);
        room.setAvailableRooms(3);

        // Create a sample BookingRequestDTO (like user filling the form)
        requestDTO = new BookingRequestDTO();
        requestDTO.setName("Aryan");
        requestDTO.setPhone("9876543210");
        requestDTO.setRoomId(1L);

        // Create a sample saved Booking
        booking = new Booking();
        booking.setId(1L);
        booking.setName("Aryan");
        booking.setPhone("9876543210");
        booking.setBookingDate(LocalDate.now());
        booking.setStatus("CONFIRMED");
        booking.setRoom(room);
    }

    // -------------------------------------------------------
    // TEST 1: Successful Booking
    // -------------------------------------------------------
    @Test
    void testAddBooking_Success() {
        // Arrange: Mock repo calls
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act: Call the service
        BookingResponseDTO response = bookingService.addBooking(requestDTO);

        // Assert: Verify the booking was successful
        assertNotNull(response);
        assertEquals("Aryan", response.getName());
        assertEquals("9876543210", response.getPhone());
        assertEquals("CONFIRMED", response.getStatus());

        // Verify room count was decremented
        assertEquals(2, room.getAvailableRooms());

        System.out.println("TEST 1 PASSED: Booking created successfully for " + response.getName());
    }

    // -------------------------------------------------------
    // TEST 2: Room Not Found
    // -------------------------------------------------------
    @Test
    void testAddBooking_RoomNotFound() {
        // Arrange: Room does NOT exist in DB
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Should throw RoomNotFoundException
        assertThrows(RoomNotFoundException.class, () -> {
            bookingService.addBooking(requestDTO);
        });

        System.out.println("TEST 2 PASSED: RoomNotFoundException thrown correctly");
    }

    // -------------------------------------------------------
    // TEST 3: Room Not Available (No Rooms Left)
    // -------------------------------------------------------
    @Test
    void testAddBooking_RoomUnavailable() {
        // Arrange: Set available rooms to 0
        room.setAvailableRooms(0);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        // Act & Assert: Should throw RoomUnavailableException
        assertThrows(RoomUnavailableException.class, () -> {
            bookingService.addBooking(requestDTO);
        });

        System.out.println("TEST 3 PASSED: RoomUnavailableException thrown correctly");
    }

    // -------------------------------------------------------
    // TEST 4: Get All Bookings
    // -------------------------------------------------------
    @Test
    void testGetAllBookings() {
        // Arrange: Return a list of bookings
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        // Act
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();

        // Assert
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals("Aryan", bookings.get(0).getName());

        System.out.println("TEST 4 PASSED: Got all bookings, count = " + bookings.size());
    }

    // -------------------------------------------------------
    // TEST 5: Get Booking By ID
    // -------------------------------------------------------
    @Test
    void testGetBookingById_Success() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Act
        BookingResponseDTO response = bookingService.getBookingById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("CONFIRMED", response.getStatus());

        System.out.println("TEST 5 PASSED: Booking found by ID = " + response.getId());
    }

    // -------------------------------------------------------
    // TEST 6: Cancel Booking
    // -------------------------------------------------------
    @Test
    void testCancelBooking_Success() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        BookingResponseDTO response = bookingService.cancelBooking(1L);

        // Assert
        assertNotNull(response);
        assertEquals("CANCELLED", response.getStatus());

        // Verify available rooms were restored
        assertEquals(4, room.getAvailableRooms());

        System.out.println("TEST 6 PASSED: Booking cancelled, status = " + response.getStatus());
    }
}
