package com.example.Final.services;

import com.example.Final.model.Booking;
import com.example.Final.model.Category;
import com.example.Final.model.Stadium;
import com.example.Final.model.User;
import com.example.Final.repo.Bookingrepo;
import com.example.Final.repo.Categoryrepo;
import com.example.Final.repo.Stadiumrepo;
import com.example.Final.repo.Userepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private Bookingrepo bookingrepo;

    @Autowired
    private Categoryrepo categoryrepo;

    @Autowired
    private Stadiumrepo stadiumrepo;

    @Autowired
    private Userepo userepo;

    public List<Booking> getAllBookings() {
        return bookingrepo.findAll();
    }

    public Optional<Booking> getBookingById(Long bookingId) {
        return bookingrepo.findById(bookingId);
    }

    public long countTotalBooking() {
        return bookingrepo.count();
    }



    public String createBooking(Booking booking) {
        if (isBookingOverlap(booking.getStadium(), booking.getDate(), booking.getStartTime(), booking.getEndTime())) {
            return "Booking already exists for the specified stadium, date, and time range.";
        } else {
            bookingrepo.save(booking);
            return "Booking created successfully.";
        }
    }

    private boolean isBookingOverlap(Stadium stadium, LocalDate date, String startTime, String endTime) {
        return bookingrepo.findOverlappingBooking(stadium, date, startTime, endTime).isPresent();
    }




    public Booking updateBooking(Long bookingId, Booking bookingDetails) {
        Booking booking = bookingrepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setCategory(bookingDetails.getCategory());
        booking.setUser(bookingDetails.getUser());
        booking.setDate(bookingDetails.getDate());
        booking.setStartTime(bookingDetails.getStartTime());
        booking.setEndTime(bookingDetails.getEndTime());
        booking.setStadium(bookingDetails.getStadium());
        booking.setStatus(bookingDetails.getStatus());
        // Update booking details here if needed
        return bookingrepo.save(booking);
    }

    public void deleteBooking(Long bookingId) {
        bookingrepo.deleteById(bookingId);
    }



    public List<Booking> getBookingsByUserId(Long userId) {
        User user = userepo.findById(userId).orElse(null);
        if (user != null) {
            return bookingrepo.findByUser(Optional.of(user));
        }
        return null;
    }


    public List<Booking> getBookingsByUsername(String username) {
        Optional<User> user = userepo.findByUsername(username);
        if (user != null) {
            return bookingrepo.findByUser(user);
        }
        return null;
    }

    public List<Booking> getBookingsByDate(LocalDate date) {
        return bookingrepo.findByDate(date);
    }

    public List<Booking> getBookingsByStadium(Stadium stadium) {
        return bookingrepo.findByStadium(stadium);
    }


    public Optional<Booking> confirmBooking(Long bookingId) {
        Optional<Booking> optionalBooking = bookingrepo.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setStatus("Confirmed");
            bookingrepo.save(booking);
            return Optional.of(booking);
        } else {
            return Optional.empty();
        }
    }
    public List<Map<String, Object>> countBookingsByStadium() {
        return bookingrepo.countBookingsByStadium();
    }

    public Map<Month, Long> getBookingsPerMonth(int year) {
        List<Object[]> results = bookingrepo.countBookingsPerMonth(year);

        Map<Month, Long> bookingsPerMonth = new HashMap<>();
        for (Object[] result : results) {
            Integer monthInt = (Integer) result[0];
            Long count = (Long) result[1];

            Month month = Month.of(monthInt);  // Convert integer to Month enum
            bookingsPerMonth.put(month, count);
        }

        return bookingsPerMonth;
    }



    public Map<Integer, Long> getBookingsPerYear() {
        List<Object[]> results = bookingrepo.countBookingsPerYear();

        Map<Integer, Long> bookingsPerYear = new HashMap<>();
        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            Long count = (Long) result[1];

            bookingsPerYear.put(year, count);
        }

        return bookingsPerYear;
    }


    public boolean cancelBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingrepo.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            booking.cancelBooking();
            bookingrepo.save(booking);
            return true;
        }
        return false; // Booking not found
    }


    public Long getTotalBookingsByStadium(Long stadiumId) {
        return bookingrepo.countBookingsByStadium(stadiumId);
    }

}
