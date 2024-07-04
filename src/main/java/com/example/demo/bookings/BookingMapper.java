package com.example.demo.bookings;

public class BookingMapper {

    public static BookingDTO toDTO(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getUserId(),
                booking.getLocation().getId(),
                booking.getStartTime(),
                booking.getEndTime()
        );
    }
}
