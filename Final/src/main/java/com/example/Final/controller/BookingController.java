package com.example.Final.controller;

import com.example.Final.model.Booking;
import com.example.Final.model.Stadium;
import com.example.Final.repo.Bookingrepo;
import com.example.Final.services.BookingService;
import com.example.Final.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins ="http://localhost:3000/")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private Bookingrepo bookingrepo;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{bookingId}")
    public Optional<Booking> getBookingById(@PathVariable Long bookingId) {
        return bookingService.getBookingById(bookingId);
    }




    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
        String result = bookingService.createBooking(booking);
        if (result.equals("Booking created successfully.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(409).body(result); // 409 Conflict
        }
    }




    @PutMapping("/{bookingId}")
    public Booking updateBooking(@PathVariable Long bookingId, @RequestBody Booking bookingDetails) {
        return bookingService.updateBooking(bookingId, bookingDetails);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteBooking(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
    }



    @GetMapping("/userid/{userId}")
    public List<Booking> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @GetMapping("/username/{username}")
    public List<Booking> getBookingsByUsername(@PathVariable String username) {
        return bookingService.getBookingsByUsername(username);
    }

    @GetMapping("/stadium/{stadiumid}")
    public List<Booking> getBookingsByStadium(@PathVariable Long stadiumid) {
        Stadium stadium = new Stadium();
        stadium.setStadiumid(stadiumid); // Assuming Stadium class has setStadiumId method
        return bookingService.getBookingsByStadium(stadium);
    }


    @GetMapping("/date/{date}")
    public List<Booking> getBookingsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return bookingService.getBookingsByDate(localDate);
    }




    @Autowired
    private EmailService emailService;


    @PutMapping("/{bookingId}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long bookingId) {
        Optional<Booking> optionalBooking = bookingService.confirmBooking(bookingId);

        if (optionalBooking.isPresent()) {
            Booking confirmedBooking = optionalBooking.get();

            // Send confirmation email to the user
            String userEmail = confirmedBooking.getUser().getEmail();
            String subject = "Booking Confirmation";
            String text = "Dear " + confirmedBooking.getUser().getFirstname() + ",\n\nYour booking for " +
                    confirmedBooking.getStadium().getName() + " on " + confirmedBooking.getDate() +
                    " has been confirmed.\n\nBest regards,\n on your Match";

            emailService.sendEmail(userEmail, subject, text);

            return ResponseEntity.ok(confirmedBooking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/count")
    public long countTotalBooking() {
        return bookingService.countTotalBooking();
    }

    @GetMapping("/confirmed")
    public List<Booking> getConfirmedBookings() {
        return bookingrepo.findByStatus("Confirmed");
    }


    @GetMapping("/bookings/by-stadium")
    public ResponseEntity<List<Map<String, Object>>> countBookingsByStadium() {
        List<Map<String, Object>> result = bookingService.countBookingsByStadium();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/bookings/count-per-month")
    public Map<Month, Long> getBookingsPerMonth(@RequestParam int year) {
        return bookingService.getBookingsPerMonth(year);
    }

    @GetMapping("/count-per-year")
    public Map<Integer, Long> getBookingsPerYear() {
        return bookingService.getBookingsPerYear();
    }


    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        boolean isCanceled = bookingService.cancelBooking(bookingId);
        if (isCanceled) {
            return ResponseEntity.ok("Booking canceled successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/bookings/count-by-stadium")
    public Long countBookingsByStadium(@RequestParam Long stadiumId) {
        return bookingService.getTotalBookingsByStadium(stadiumId);
    }




}
