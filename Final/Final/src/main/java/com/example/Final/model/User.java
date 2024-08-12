package com.example.Final.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String role;

}
