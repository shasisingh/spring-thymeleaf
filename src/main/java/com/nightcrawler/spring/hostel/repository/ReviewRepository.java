package com.nightcrawler.spring.hostel.repository;

import com.nightcrawler.spring.hostel.model.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r ORDER BY r.created DESC")
    List<Review> findTop5ByOrderByCreatedDesc(Pageable pageable);
}
