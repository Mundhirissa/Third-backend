package com.example.Final.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String status;
    private LocalDateTime bookedAt;


    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "stadiumid")
    private Stadium stadium;

    @ManyToOne
    @JoinColumn(name = "categoryid")
    private Category category;



    public void cancelBooking() {
        this.status = "canceled";
    }

    @PrePersist
    protected void onCreate() {
        this.bookedAt = LocalDateTime.now();
    }
    public void rejectBooking() {
        this.status = "rejected";
    }



}
