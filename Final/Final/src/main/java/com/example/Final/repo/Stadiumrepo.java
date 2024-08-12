package com.example.Final.repo;

import com.example.Final.model.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Stadiumrepo extends JpaRepository<Stadium,Long> {
}
