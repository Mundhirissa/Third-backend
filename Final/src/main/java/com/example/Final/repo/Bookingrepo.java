package com.example.Final.repo;

import com.example.Final.model.Booking;
import com.example.Final.model.Stadium;
import com.example.Final.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface Bookingrepo extends JpaRepository<Booking, Long> {

    @Query("SELECT b.stadium, COUNT(b) as totalBookings " +
            "FROM Booking b " +
            "GROUP BY b.stadium")
    List<Map<String, Object>> countBookingsByStadium();
    List<Booking> findByStatus(String status);
    long count();

    @Query("SELECT FUNCTION('MONTH', b.date) AS month, COUNT(b) AS count " +
            "FROM Booking b " +
            "WHERE FUNCTION('YEAR', b.date) = :year " +
            "GROUP BY FUNCTION('MONTH', b.date)")
    List<Object[]> countBookingsPerMonth(@Param("year") int year);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.stadium.stadiumid = :stadiumId")
    Long countBookingsByStadium(@Param("stadiumId") Long stadiumId);


    @Query("SELECT FUNCTION('YEAR', b.date) AS year, COUNT(b) AS count " +
            "FROM Booking b " +
            "GROUP BY FUNCTION('YEAR', b.date)")
    List<Object[]> countBookingsPerYear();


    List<Booking> findByStadium(Stadium stadium);
    List<Booking> findByDate(LocalDate date);
    List<Booking> findByUser(Optional<User> user);

    @Query("SELECT b FROM Booking b WHERE b.stadium = :stadium AND b.date = :date AND " +
            "((b.startTime <= :startTime AND b.endTime > :startTime) OR " +
            "(b.startTime < :endTime AND b.endTime >= :endTime) OR " +
            "(b.startTime >= :startTime AND b.endTime <= :endTime))")
    Optional<Booking> findOverlappingBooking(Stadium stadium, LocalDate date, String startTime, String endTime);



    List<Booking> findByStatusAndBookedAtBefore(String status, LocalDateTime cutoffTime);

}
