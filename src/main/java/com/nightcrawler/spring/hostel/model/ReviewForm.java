package com.nightcrawler.spring.hostel.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewForm {
    @NotBlank(message = "Your name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String author;

    @NotBlank(message = "Your review is required")
    @Size(max = 500, message = "Review must be at most 500 characters")
    private String text;
}
