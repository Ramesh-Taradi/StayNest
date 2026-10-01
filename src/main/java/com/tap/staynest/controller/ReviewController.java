package com.tap.staynest.controller;

import com.tap.staynest.dto.request.ReviewRequestDTO;
import com.tap.staynest.dto.response.ReviewResponseDTO;
import com.tap.staynest.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/pgs/{pgId}/reviews")
    public ResponseEntity<ReviewResponseDTO> addReview(
            @PathVariable Long pgId,
            @Valid @RequestBody ReviewRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(pgId, requestDTO));
    }

    @GetMapping("/pgs/{pgId}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByPG(@PathVariable Long pgId) {
        return ResponseEntity.ok(reviewService.getReviewsByPG(pgId));
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequestDTO requestDTO) {
        return ResponseEntity.ok(reviewService.updateReview(id, requestDTO));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
