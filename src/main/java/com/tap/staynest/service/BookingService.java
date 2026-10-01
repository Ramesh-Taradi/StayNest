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

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Transactional
    public BookingResponseDTO addBooking(BookingRequestDTO requestDTO) {
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
        responseDTO.setRoomId(booking.getRoom().getId());
        return responseDTO;
    }
}
