package com.tap.staynest.service;

import com.tap.staynest.dto.request.BookingRequestDTO;
import com.tap.staynest.dto.response.BookingResponseDTO;
import com.tap.staynest.exception.BookingNotFoundException;
import com.tap.staynest.exception.RoomNotFoundException;
import com.tap.staynest.exception.RoomUnavailableException;
import com.tap.staynest.model.Booking;
import com.tap.staynest.model.Room;
import com.tap.staynest.repository.BookingRepository;
import com.tap.staynest.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private com.tap.staynest.repository.UserRepository userRepository;

    @Transactional
    public BookingResponseDTO addBooking(BookingRequestDTO requestDTO) {
        return addBooking(requestDTO, null);
    }

    @Transactional
    public BookingResponseDTO addBooking(BookingRequestDTO requestDTO, String userEmail) {
        Room room = roomRepository.findById(requestDTO.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        if (room.getAvailableRooms() <= 0) {
            throw new RoomUnavailableException("Room is not available");
        }

        Booking booking = new Booking();
        booking.setName(requestDTO.getName());
        booking.setPhone(requestDTO.getPhone());
        booking.setBookingDate(LocalDate.now());
        booking.setStatus("CONFIRMED");
        booking.setRoom(room);

        if (userEmail != null) {
            userRepository.findByEmail(userEmail).ifPresent(booking::setUser);
        }

        // Decrement available room count
        room.setAvailableRooms(room.getAvailableRooms() - 1);
        roomRepository.save(room);

        Booking savedBooking = bookingRepository.save(booking);
        return convertToResponseDTO(savedBooking);
    }

    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public List<BookingResponseDTO> getBookingsByUserEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return List.of();
        }
        return bookingRepository.findByUserEmail(email.trim())
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
        return convertToResponseDTO(booking);
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new BookingNotFoundException("Booking is already cancelled");
        }

        Room room = booking.getRoom();
        if (room.getAvailableRooms() < room.getTotalRooms()) {
            room.setAvailableRooms(room.getAvailableRooms() + 1);
        }

        booking.setStatus("CANCELLED");
        roomRepository.save(room);
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToResponseDTO(updatedBooking);
    }

    private BookingResponseDTO convertToResponseDTO(Booking booking) {
        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setId(booking.getId());
        responseDTO.setName(booking.getName());
        responseDTO.setPhone(booking.getPhone());
        responseDTO.setBookingDate(booking.getBookingDate());
        responseDTO.setStatus(booking.getStatus());
        if (booking.getRoom() != null) {
            responseDTO.setRoomId(booking.getRoom().getId());
            responseDTO.setRoomType(booking.getRoom().getType());
            responseDTO.setRent(booking.getRoom().getRent());
            if (booking.getRoom().getPg() != null) {
                responseDTO.setPgId(booking.getRoom().getPg().getId());
                responseDTO.setPgName(booking.getRoom().getPg().getName());
                responseDTO.setPgLocation(booking.getRoom().getPg().getLocation());
                responseDTO.setPgImageUrl(booking.getRoom().getPg().getImageUrl());
            }
        }
        if (booking.getUser() != null) {
            responseDTO.setEmail(booking.getUser().getEmail());
            if (responseDTO.getName() == null || responseDTO.getName().isEmpty()) {
                responseDTO.setName(booking.getUser().getName());
            }
        }
        String pgName = responseDTO.getPgName() != null ? responseDTO.getPgName().toLowerCase() : "";
        if (pgName.contains("sunrise")) {
            responseDTO.setPhotoCount(5);
            responseDTO.setRent(new BigDecimal("8000"));
            responseDTO.setRoomType("Double Sharing");
            responseDTO.setPgImageUrl("https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?w=700");
        } else if (pgName.contains("green valley")) {
            responseDTO.setPhotoCount(6);
            responseDTO.setRent(new BigDecimal("9500"));
            responseDTO.setRoomType("Single Sharing");
            responseDTO.setPgImageUrl("https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700");
        } else if (pgName.contains("city view")) {
            responseDTO.setPhotoCount(4);
            responseDTO.setRent(new BigDecimal("11000"));
            responseDTO.setRoomType("Double Sharing");
            responseDTO.setPgImageUrl("https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700");
        } else if (pgName.contains("royal")) {
            responseDTO.setPhotoCount(5);
            responseDTO.setRent(new BigDecimal("10000"));
            responseDTO.setRoomType("Single Sharing");
        } else {
            long idVal = booking.getId() != null ? booking.getId() : 1L;
            responseDTO.setPhotoCount((int) (4 + (idVal % 3)));
        }
        return responseDTO;
    }
}
