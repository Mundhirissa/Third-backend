package com.example.Final.repo;

import com.example.Final.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Categoryrepo extends JpaRepository<Category,Long> {
}
