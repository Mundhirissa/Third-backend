package com.example.Final.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Paymentid;
    private  Long amount;
    private  String paymentstatus;

    private LocalDate paymentdate;
    private String controlNumber;

    @OneToOne
    @JoinColumn( name = "Bookingid")
    private Booking booking;



}
