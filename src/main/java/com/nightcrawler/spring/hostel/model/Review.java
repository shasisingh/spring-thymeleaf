package com.nightcrawler.spring.hostel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false, length = 100)
    private String author;

    @Setter
    @Column
    private Integer rating;

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "hostel_id")
    @Setter
    private Hostel hostel;

    public Review() {
    }

    public Review(String text, String author) {
        this.text = text;
        this.author = author;
    }

    @PrePersist
    protected void onCreate() {
        this.created = java.time.LocalDateTime.now();
    }

}
