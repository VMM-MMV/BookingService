package com.example.demo.bookings;

import com.example.demo.locations.Location;
import com.example.demo.locations.LocationRepository;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public BookingDTO createBooking(Booking booking) {
        Location location = locationRepository.findById(booking.getLocation().getId())
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        boolean bookingEndsBeforeItStarts = booking.getEndTime().isBefore(booking.getStartTime());
        if (bookingEndsBeforeItStarts) throw new IllegalArgumentException("Booking can't end before it starts.");

        if (bookingRepository.existsOverlappingBooking(location.getId(), booking.getStartTime())) {
            throw new IllegalArgumentException("Overlapping booking exists for the given location and time.");
        }

        try {
            booking.setLocation(location);
            Booking savedBooking = bookingRepository.save(booking);
            return BookingMapper.toDTO(savedBooking);
        } catch (OptimisticLockException ex) {
            throw new IllegalStateException("Optimistic locking failure occurred", ex);
        }
    }

    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.getAll();
    }

    @Transactional
    public void deleteBookingById(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Transactional
    public void deleteAllBookings() {
        bookingRepository.deleteAll();
    }
//    @Transactional
//    public BookingDTO updateBooking(Long id, Booking updatedBooking) {
//        Location location = locationRepository.findById(updatedBooking.getLocation().getId())
//                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
//        return bookingRepository.findById(id)
//                .map(existingBooking -> {
//                    if (bookingRepository.existsOverlappingBooking(updatedBooking.getLocation().getId(),
//                            updatedBooking.getStartTime(),
//                            updatedBooking.getEndTime())) {
//                        throw new IllegalArgumentException("Overlapping booking exists for the given location and time.");
//                    }
//                    existingBooking.setUserId(updatedBooking.getUserId());
//                    existingBooking.setLocation(location);
//                    existingBooking.setStartTime(updatedBooking.getStartTime());
//                    existingBooking.setEndTime(updatedBooking.getEndTime());
//                    Booking savedBooking = bookingRepository.save(existingBooking);
//                    return BookingMapper.toDTO(savedBooking);
//                })
//                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
//    }
}
