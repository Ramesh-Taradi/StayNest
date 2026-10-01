package com.tap.staynest.repository;

import com.tap.staynest.model.PG;
import com.tap.staynest.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPg(PG pg);
}
