package com.nightcrowler.spring_hostel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ROOM")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hostel_id", nullable = false)
    private Hostel hostel;

    @Column(name = "allocated", nullable = false)
    private boolean allocated = false;

}
