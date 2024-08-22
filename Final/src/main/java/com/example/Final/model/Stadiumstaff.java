package com.example.Final.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Stadiumstaff extends User {


    @ManyToOne
    @JoinColumn(name = "stadiumid")
    private Stadium stadium;



}
