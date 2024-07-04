package com.example.demo.bookings;

import com.example.demo.bookings.dto.BookingDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.location.id = :locationId " +
            "AND (:startTime < b.endTime and :endTime > b.startTime)")
    boolean existsOverlappingBooking(@Param("locationId") Long locationId,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.id <> :bookingId " +
            "AND b.location.id = :locationId " +
            "AND (:startTime < b.endTime AND :endTime > b.startTime)")
    boolean existsOverlappingBookingForUpdate(@Param("bookingId") Long bookingId,
                                              @Param("locationId") Long locationId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    @Transactional(readOnly = true)
    @Query("SELECT new com.example.demo.bookings.dto.BookingDTO(b.id, b.userId, b.location.id, b.startTime, b.endTime) FROM Booking b")
    List<BookingDTO> getAll();
}

