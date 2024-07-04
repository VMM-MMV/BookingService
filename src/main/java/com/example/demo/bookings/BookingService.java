package com.example.demo.bookings;

import com.example.demo.bookings.dto.BookingDTO;
import com.example.demo.bookings.dto.BookingMapper;
import com.example.demo.locations.Location;
import com.example.demo.locations.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final LocationRepository locationRepository;

    @Transactional
    @Retryable(value = ObjectOptimisticLockingFailureException.class, maxAttempts = 3)
    public BookingDTO createBooking(Booking booking) {
        Location location = locationRepository.findById(booking.getLocation().getId())
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        location.setVersion(location.getVersion()+1);

        boolean bookingEndsBeforeItStarts = booking.getEndTime().isBefore(booking.getStartTime());
        if (bookingEndsBeforeItStarts) throw new IllegalArgumentException("Booking can't end before it starts.");

        boolean bookingIsOverlappedWithAnotherBooking = bookingRepository.existsOverlappingBooking(location.getId(), booking.getStartTime(), booking.getEndTime());
        if (bookingIsOverlappedWithAnotherBooking) throw new IllegalArgumentException("Overlapping booking exists for the given location and time.");

        booking.setLocation(location);
        Booking savedBooking = bookingRepository.save(booking);

        return BookingMapper.toDTO(savedBooking);
    }

    @Transactional
    @Retryable(value = ObjectOptimisticLockingFailureException.class, maxAttempts = 3)
    public BookingDTO updateBooking(Long bookingId, Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        Location location = locationRepository.findById(updatedBooking.getLocation().getId())
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        location.setVersion(location.getVersion() + 1);

        boolean bookingEndsBeforeItStarts = updatedBooking.getEndTime().isBefore(updatedBooking.getStartTime());
        if (bookingEndsBeforeItStarts) throw new IllegalArgumentException("Booking can't end before it starts.");

        boolean bookingIsOverlappedWithAnotherBooking = bookingRepository.existsOverlappingBookingForUpdate(bookingId, location.getId(), updatedBooking.getStartTime(), updatedBooking.getEndTime());
        if (bookingIsOverlappedWithAnotherBooking) throw new IllegalArgumentException("Overlapping booking exists for the given location and time.");

        existingBooking.setStartTime(updatedBooking.getStartTime());
        existingBooking.setEndTime(updatedBooking.getEndTime());
        existingBooking.setLocation(location);

        Booking savedBooking = bookingRepository.save(existingBooking);

        return BookingMapper.toDTO(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.getAll();
    }

    @Transactional(readOnly = true)
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        return BookingMapper.toDTO(booking);
    }

    @Transactional
    public void deleteBookingById(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Transactional
    public void deleteAllBookings() {
        bookingRepository.deleteAll();
    }
}
