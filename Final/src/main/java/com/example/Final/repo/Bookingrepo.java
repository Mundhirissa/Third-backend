package com.example.Final.repo;

import com.example.Final.model.Booking;
import com.example.Final.model.Stadium;
import com.example.Final.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface Bookingrepo extends JpaRepository<Booking, Long> {
    long count();
    List<Booking> findByStadium(Stadium stadium);
    List<Booking> findByDate(LocalDate date);
    List<Booking> findByUser(Optional<User> user);

    @Query("SELECT b FROM Booking b WHERE b.stadium = :stadium AND b.date = :date AND " +
            "((b.startTime <= :startTime AND b.endTime > :startTime) OR " +
            "(b.startTime < :endTime AND b.endTime >= :endTime) OR " +
            "(b.startTime >= :startTime AND b.endTime <= :endTime))")
    Optional<Booking> findOverlappingBooking(Stadium stadium, LocalDate date, String startTime, String endTime);
}
