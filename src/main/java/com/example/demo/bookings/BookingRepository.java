package com.example.demo.bookings;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
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
            "AND ((:startTime >= b.startTime AND :startTime < b.endTime) " +
            "     OR (:endTime > b.startTime AND :endTime <= b.endTime))")
    boolean existsOverlappingBooking(@Param("locationId") Long locationId,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.location.id = :locationId " +
            "AND b.id != :id " +
            "AND ((:startTime >= b.startTime AND :startTime < b.endTime) " +
            "     OR (:endTime > b.startTime AND :endTime <= b.endTime))")
    boolean existsOverlappingBookingAndNotSameEntry(@Param("locationId") Long locationId,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime,
                                     @Param("endTime") Long id);

    @Transactional(readOnly = true)
    @Query("SELECT new com.example.demo.bookings.BookingDTO(b.id, b.userId, b.location.id, b.startTime, b.endTime) FROM Booking b")
    List<BookingDTO> getAll();

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    <S extends Booking> S save(S entity);
}

