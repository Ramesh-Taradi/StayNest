package com.tap.staynest.controller;

import com.tap.staynest.dto.request.BookingRequestDTO;
import com.tap.staynest.dto.request.ReviewRequestDTO;
import com.tap.staynest.dto.response.BookingResponseDTO;
import com.tap.staynest.dto.response.PGResponseDTO;
import com.tap.staynest.dto.response.ReviewResponseDTO;
import com.tap.staynest.dto.response.RoomResponseDTO;
import com.tap.staynest.service.BookingService;
import com.tap.staynest.service.PGService;
import com.tap.staynest.service.ReviewService;
import com.tap.staynest.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private PGService pgService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public String home(Model model) {
        List<PGResponseDTO> pgs = pgService.getAllPGs();
        model.addAttribute("pgs", pgs);
        return "home";
    }

    @GetMapping("/search")
    public String searchPGs(@RequestParam String location, Model model) {
        List<PGResponseDTO> pgs = pgService.searchByLocation(location);
        model.addAttribute("pgs", pgs);
        model.addAttribute("searchLocation", location);
        return "home";
    }

    @GetMapping("/pgs/{id}")
    public String pgDetails(@PathVariable Long id, Model model) {
        PGResponseDTO pg = pgService.getPGById(id);
        List<RoomResponseDTO> rooms = roomService.getRoomsByPG(id);
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByPG(id);

        model.addAttribute("pg", pg);
        model.addAttribute("rooms", rooms);
        model.addAttribute("reviews", reviews);
        return "pg-details";
    }

    @GetMapping("/bookings/new")
    public String bookingForm(@RequestParam Long roomId, Model model) {
        RoomResponseDTO room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);
        return "booking";
    }

    @PostMapping("/bookings")
    public String submitBooking(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam Long roomId,
            Model model) {

        BookingRequestDTO requestDTO = new BookingRequestDTO();
        requestDTO.setName(name);
        requestDTO.setPhone(phone);
        requestDTO.setRoomId(roomId);

        BookingResponseDTO booking = bookingService.addBooking(requestDTO);
        model.addAttribute("booking", booking);
        return "booking-success";
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "bookings";
    }

    @GetMapping("/pgs/{pgId}/reviews/new")
    public String reviewForm(@PathVariable Long pgId, Model model) {
        PGResponseDTO pg = pgService.getPGById(pgId);
        model.addAttribute("pg", pg);
        return "review";
    }

    @PostMapping("/pgs/{pgId}/reviews")
    public String submitReview(
            @PathVariable Long pgId,
            @RequestParam String name,
            @RequestParam Integer rating,
            @RequestParam String comment) {

        ReviewRequestDTO requestDTO = new ReviewRequestDTO();
        requestDTO.setName(name);
        requestDTO.setRating(rating);
        requestDTO.setComment(comment);

        reviewService.addReview(pgId, requestDTO);
        return "redirect:/pgs/" + pgId;
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/bookings";
    }
}
