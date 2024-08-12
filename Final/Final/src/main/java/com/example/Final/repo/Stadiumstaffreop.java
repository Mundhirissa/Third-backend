package com.example.Final.repo;

import com.example.Final.model.Stadiumstaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Stadiumstaffreop extends JpaRepository<Stadiumstaff,Long> {
    Optional<Stadiumstaff> findByUsername(String username);
}
