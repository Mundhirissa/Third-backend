package com.example.Final.services;

import com.example.Final.model.Booking;
import com.example.Final.model.Payment;
import com.example.Final.model.Stadium;
import com.example.Final.repo.Bookingrepo;
import com.example.Final.repo.Paymentrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private Bookingrepo bookingRepository;

    @Autowired
    private Paymentrepo paymentRepository;



    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setAmount(paymentDetails.getAmount());
        payment.setPaymentstatus(paymentDetails.getPaymentstatus());
        payment.setPaymentdate(paymentDetails.getPaymentdate());
        payment.setBooking(paymentDetails.getBooking());
        // Update booking if needed
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }




    public String generateControlNumber(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));

        if (!"confirmed".equalsIgnoreCase(booking.getStatus())) {
            throw new IllegalStateException("Booking is not confirmed.");
        }

        String controlNumber = generateRandomControlNumber();
        Long amount = determineAmount(booking.getStadium(), booking.getStartTime());

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentstatus("pending");
        payment.setPaymentdate(LocalDate.now());  // Use LocalDate
        payment.setControlNumber(controlNumber);

        paymentRepository.save(payment);

        return controlNumber;
    }

    private String generateRandomControlNumber() {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()); // Use LocalDate
        String randomString = new Random().ints(6, 0, 36)
                .mapToObj(i -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".substring(i, i + 1))
                .collect(Collectors.joining());
        return "MU" + timestamp + randomString;
    }


    private Long determineAmount(Stadium stadium, String startTime) {
        boolean isDayTime = Integer.parseInt(startTime.split(":")[0]) >= 6 && Integer.parseInt(startTime.split(":")[0]) < 18;
        return isDayTime ? stadium.getDaytimePrice() : stadium.getNighttimePrice();
    }


    public Long getTotalAmountForPaidPayments() {
        return paymentRepository.findTotalAmountForPaidPayments();
    }


    public Payment getPaymentDetailsByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }



    public Long getTotalAmountByStadiumIdAndStatusPaid(Long stadiumid) {
        return paymentRepository.findTotalAmountByStadiumIdAndStatusPaid(stadiumid);
    }


    public List<Map<String, Object>> getTotalAmountPerYear() {
        return paymentRepository.findTotalAmountPerYear();
    }


    public List<Map<String, Object>> getAmountPerYearPerStadium() {
        return paymentRepository.findAmountPerYearPerStadium();
    }


    public List<Payment> getPaymentsByStadiumId(Long stadiumId) {
        return paymentRepository.findByBooking_Stadium_Stadiumid(stadiumId);
    }
}
