package com.nightcrawler.spring.hostel.service;

import com.nightcrawler.spring.hostel.model.Hostel;
import com.nightcrawler.spring.hostel.model.Review;
import com.nightcrawler.spring.hostel.model.ReviewForm;
import com.nightcrawler.spring.hostel.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createForHostel(ReviewForm form, Hostel hostel) {
        Review review = new Review(form.getText(), form.getAuthor());
        review.setRating(form.getRating());
        review.setHostel(hostel);
        reviewRepository.save(review);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createWithoutHostel(ReviewForm form) {
        Review review = new Review(form.getText(), form.getAuthor());
        review.setRating(form.getRating());
        reviewRepository.save(review);
    }

    public long countByHostelId(Long hostelId) {
        return reviewRepository.countByHostel_Id(hostelId);
    }

    public Page<Review> pageByHostel(Long hostelId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page), size, Sort.by(Sort.Direction.DESC, "created"));
        return reviewRepository.findByHostel_Id(hostelId, pageable);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));
    }
}
