package com.nightcrawler.spring.hostel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "ALLOCATION")
public class Allocation {
    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "identity_doc", nullable = false)
    private String identityDoc; // e.g., Passport, ID Card, Driver License

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod; // e.g., Cash, Card, Online

    @Column(name = "hostel_room_number", nullable = false)
    private String hostelRoomNumber;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @Column(name = "number_of_bed", nullable = false)
    private Integer numberOfBed = 1;

    @Column(name = "hostel_name")
    private String hostelName;

    @Column(name = "hostel_id")
    private Long hostelId;

}
