package com.example.Final.controller;

import com.example.Final.model.Payment;
import com.example.Final.repo.Paymentrepo;
import com.example.Final.services.EmailService;
import com.example.Final.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins ="http://localhost:3000/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Paymentrepo paymentrepo;


    @Autowired
    private EmailService emailService;


    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/getbyid/{id}")
    public Optional<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    @PutMapping("/update/{id}")
    public Payment updatePayment(@PathVariable Long id, @RequestBody Payment paymentDetails) {
        return paymentService.updatePayment(id, paymentDetails);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }

    @PostMapping("/generate-control-number")
    public ResponseEntity<?> generateControlNumber(@RequestBody Map<String, Long> request) {
        Long bookingId = request.get("bookingId");
        if (bookingId == null) {
            return ResponseEntity.badRequest().body("bookingId is missing");
        }
        String controlNumber = paymentService.generateControlNumber(bookingId);
        Payment payment = paymentrepo.findByControlNumber(controlNumber);
        Map<String, Object> response = new HashMap<>();
        response.put("controlNumber", controlNumber);
        response.put("amount", payment.getAmount());
        response.put("paymentstatus", payment.getPaymentstatus());
        response.put("paymentdate", payment.getPaymentdate());  // Adjust to LocalDate
        response.put("bookingId", payment.getBooking().getBookingId());
        response.put("stadium", payment.getBooking().getStadium());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/make-payment")
    public ResponseEntity<?> makePayment(@RequestBody Map<String, String> request) {
        String controlNumber = request.get("controlNumber");
        Long paymentAmount = Long.parseLong(request.get("amount"));

        // Validate input
        if (controlNumber == null || controlNumber.isEmpty()) {
            return ResponseEntity.badRequest().body("Control number is missing");
        }

        // Find payment by control number
        Payment payment = paymentrepo.findByControlNumber(controlNumber);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid control number");
        }

        // Check if payment has already been made
        if ("paid".equalsIgnoreCase(payment.getPaymentstatus())) {
            return ResponseEntity.badRequest().body("Payment has already been made");
        }

        // Validate payment amount
        if (!payment.getAmount().equals(paymentAmount)) {
            return ResponseEntity.badRequest().body("Incorrect payment amount");
        }

        // Update payment status
        payment.setPaymentstatus("paid");
        payment.setPaymentdate(LocalDate.now());  // Use LocalDate
        paymentrepo.save(payment);

        // Send email notification
        String userEmail = payment.getBooking().getUser().getEmail();
        String subject = "Payment Confirmation";
        String text = "Dear " + payment.getBooking().getUser().getFirstname() + ",\n\nYour payment for " +
                payment.getBooking().getStadium().getName() + " on " + payment.getBooking().getDate() +
                " has been successfully completed.\n\nBest regards,\nYour match";

        emailService.sendEmail(userEmail, subject, text);

        // Return success response
        return ResponseEntity.ok("Payment successful");
    }

    @GetMapping("/total-paid-amount")
    public Long getTotalPaidAmount() {
        return paymentService.getTotalAmountForPaidPayments();
    }


    @GetMapping("/{bookingId}")
    public Payment getPaymentDetails(@PathVariable Long bookingId) {
        return paymentService.getPaymentDetailsByBookingId(bookingId);
    }

    @GetMapping("/total-amount/{stadiumid}")
    public ResponseEntity<Long> getTotalAmountByStadiumIdAndStatusPaid(@PathVariable Long stadiumid) {
        Long totalAmount = paymentService.getTotalAmountByStadiumIdAndStatusPaid(stadiumid);
        if (totalAmount != null) {
            return ResponseEntity.ok(totalAmount);
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if no data found
        }
    }


    @GetMapping("/total-amount-per-year")
    public List<Map<String, Object>> getTotalAmountPerYear() {
        return paymentService.getTotalAmountPerYear();
    }

    @GetMapping("/amount-per-year-per-stadium")
    public List<Map<String, Object>> getAmountPerYearPerStadium() {
        return paymentService.getAmountPerYearPerStadium();
    }


    @GetMapping("/payment/{controlNumber}")
    public Payment findPaymentByControlNumber(@PathVariable String controlNumber) {
        return paymentrepo.findByControlNumber(controlNumber);
    }


}
