package com.example.Final.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Paymentid;
    private  Long amount;
    private  String paymentstatus;
    private String paymentdate;
    private String controlNumber;

    @OneToOne
    @JoinColumn( name = "Bookingid")
    private Booking booking;



}
