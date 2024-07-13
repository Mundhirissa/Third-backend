package com.example.Final.controller;

import com.example.Final.model.Booking;
import com.example.Final.model.Stadium;
import com.example.Final.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins ="http://localhost:3000/")
public class BookingController {

    @Autowired
    private BookingService bookingService;

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



    @PutMapping("/{bookingId}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long bookingId) {
        Optional<Booking> optionalBooking = bookingService.confirmBooking(bookingId);
        if (optionalBooking.isPresent()) {
            return ResponseEntity.ok(optionalBooking.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/count")
    public long countTotalBooking() {
        return bookingService.countTotalBooking();
    }


}
