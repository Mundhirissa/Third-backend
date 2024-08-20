package com.example.Final.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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

}
