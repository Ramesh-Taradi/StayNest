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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private com.tap.staynest.repository.UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<PGResponseDTO> pgs = pgService.getAllPGs();
        model.addAttribute("pgs", pgs);
        return "home";
    }

    @GetMapping("/pgs")
    public String allPGs(Model model) {
        List<PGResponseDTO> pgs = pgService.getAllPGs();
        model.addAttribute("pgs", pgs);
        return "pgs";
    }

    @GetMapping("/favorites")
    public String favorites(Model model) {
        return "favorites";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String userEmail = auth.getName();
            userRepository.findByEmail(userEmail).ifPresent(user -> model.addAttribute("profileUser", user));
            try {
                List<BookingResponseDTO> bookings = bookingService.getBookingsByUserEmail(userEmail);
                model.addAttribute("bookings", bookings);
                model.addAttribute("userBookings", bookings);
                long confirmed = bookings.stream().filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus())).count();
                long cancelled = bookings.stream().filter(b -> "CANCELLED".equalsIgnoreCase(b.getStatus())).count();
                model.addAttribute("totalBookingsCount", bookings.size());
                model.addAttribute("confirmedBookingsCount", confirmed);
                model.addAttribute("cancelledBookingsCount", cancelled);
            } catch (Exception ignored) {}
        }
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String preferredArea,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String aboutMe,
            Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            userRepository.findByEmail(auth.getName()).ifPresent(user -> {
                if (name != null && !name.trim().isEmpty()) {
                    user.setName(name.trim());
                }
                if (city != null && !city.trim().isEmpty()) {
                    user.setCity(city.trim());
                }
                if (preferredArea != null && !preferredArea.trim().isEmpty()) {
                    user.setPreferredArea(preferredArea.trim());
                }
                if (phone != null && !phone.trim().isEmpty()) {
                    user.setPhone(phone.trim());
                }
                if (aboutMe != null) {
                    user.setAboutMe(aboutMe.trim());
                }
                userRepository.save(user);
            });
        }
        return "redirect:/profile?updated=true";
    }

    @GetMapping("/search")
    public String searchPGs(@RequestParam String location, Model model) {
        List<PGResponseDTO> pgs = pgService.searchByLocation(location);
        model.addAttribute("pgs", pgs);
        model.addAttribute("searchLocation", location);
        return "pgs";  // show results on the pgs listing page
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
    public String bookingForm(
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Long pgId,
            Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }
        RoomResponseDTO room = null;
        if (roomId != null) {
            try {
                room = roomService.getRoomById(roomId);
            } catch (Exception ignored) {}
        }
        if (room == null && pgId != null) {
            try {
                List<RoomResponseDTO> rooms = roomService.getRoomsByPG(pgId);
                if (rooms != null && !rooms.isEmpty()) {
                    room = rooms.get(0);
                }
            } catch (Exception ignored) {}
        }
        if (room == null) {
            return "redirect:/pgs";
        }
        model.addAttribute("room", room);
        return "booking";
    }

    @PostMapping("/bookings")
    public String submitBooking(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam Long roomId,
            Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }

        BookingRequestDTO requestDTO = new BookingRequestDTO();
        requestDTO.setName(name);
        requestDTO.setPhone(phone);
        requestDTO.setRoomId(roomId);

        BookingResponseDTO booking = bookingService.addBooking(requestDTO, auth.getName());
        model.addAttribute("booking", booking);
        return "booking-success";
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }
        String userEmail = auth.getName();
        List<BookingResponseDTO> bookings = bookingService.getBookingsByUserEmail(userEmail);
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }
        bookingService.cancelBooking(id);
        return "redirect:/bookings";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }
}
