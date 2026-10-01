package com.tap.staynest.service;

import com.tap.staynest.dto.request.ReviewRequestDTO;
import com.tap.staynest.dto.response.ReviewResponseDTO;
import com.tap.staynest.exception.PGNotFoundException;
import com.tap.staynest.exception.ReviewNotFoundException;
import com.tap.staynest.model.PG;
import com.tap.staynest.model.Review;
import com.tap.staynest.repository.PGRepository;
import com.tap.staynest.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PGRepository pgRepository;

    public ReviewResponseDTO addReview(Long pgId, ReviewRequestDTO requestDTO) {
        PG pg = pgRepository.findById(pgId)
                .orElseThrow(() -> new PGNotFoundException("PG not found"));

        Review review = createReview(new Review(), requestDTO);
        review.setPg(pg);
        Review savedReview = reviewRepository.save(review);
        return convertToResponseDTO(savedReview);
    }

    public List<ReviewResponseDTO> getReviewsByPG(Long pgId) {
        PG pg = pgRepository.findById(pgId)
                .orElseThrow(() -> new PGNotFoundException("PG not found"));

        return reviewRepository.findByPg(pg)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public ReviewResponseDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
        return convertToResponseDTO(review);
    }

    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO requestDTO) {
        Review oldReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        Review updatedReview = createReview(oldReview, requestDTO);
        Review savedReview = reviewRepository.save(updatedReview);
        return convertToResponseDTO(savedReview);
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
        reviewRepository.delete(review);
    }

    private Review createReview(Review review, ReviewRequestDTO requestDTO) {
        review.setName(requestDTO.getName());
        review.setRating(requestDTO.getRating());
        review.setComment(requestDTO.getComment());
        return review;
    }

    private ReviewResponseDTO convertToResponseDTO(Review review) {
        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(review.getId());
        responseDTO.setName(review.getName());
        responseDTO.setRating(review.getRating());
        responseDTO.setComment(review.getComment());
        responseDTO.setPgId(review.getPg().getId());
        return responseDTO;
    }
}
