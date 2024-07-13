package com.example.Final.repo;

import com.example.Final.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Paymentrepo extends JpaRepository <Payment,Long>{
}
