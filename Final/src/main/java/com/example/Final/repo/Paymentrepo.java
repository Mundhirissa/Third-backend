package com.example.Final.repo;

import com.example.Final.model.Booking;
import com.example.Final.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface Paymentrepo extends JpaRepository <Payment,Long>{
    List<Payment> findByPaymentstatus(String paymentstatus);
    Payment findByControlNumber(String controlNumber);

    Optional<Payment> findByBooking(Booking booking);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentstatus = 'paid'")
    Long findTotalAmountForPaidPayments();


    @Query("SELECT p FROM Payment p WHERE p.booking.bookingId = :bookingId")
    Payment findByBookingId(@Param("bookingId") Long bookingId);


    @Query("SELECT SUM(p.amount) " +
            "FROM Payment p " +
            "JOIN p.booking b " +
            "WHERE p.paymentstatus = 'paid' " +
            "AND b.stadium.stadiumid = :stadiumid")
    Long findTotalAmountByStadiumIdAndStatusPaid(@Param("stadiumid") Long stadiumid);




    @Query(value = "SELECT YEAR(paymentdate) AS year, SUM(amount) AS totalAmount " +
            "FROM Payment " +
            "WHERE paymentstatus = 'paid' " +
            "GROUP BY YEAR(paymentdate)", nativeQuery = true)
    List<Map<String, Object>> findTotalAmountPerYear();




    @Query("SELECT b.stadium.name AS stadiumName, " +
            "YEAR(p.paymentdate) AS year, " +
            "SUM(p.amount) AS totalAmount " +
            "FROM Payment p " +
            "JOIN p.booking b " +
            "WHERE p.paymentstatus = 'paid' " +
            "GROUP BY b.stadium.name, YEAR(p.paymentdate)")
    List<Map<String, Object>> findAmountPerYearPerStadium();

    List<Payment> findByBooking_Stadium_Stadiumid(Long stadiumId);


}
