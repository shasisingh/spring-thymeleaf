package com.nightcrawler.spring.hostel.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Please select a rating")
    @Min(value = 1, message = "Minimum rating is 1")
    @Max(value = 5, message = "Maximum rating is 5")
    private Integer rating;
}
