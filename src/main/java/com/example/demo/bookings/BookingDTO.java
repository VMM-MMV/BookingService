package com.example.demo.bookings;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private Long id;
    private Long userId;
    private Long locationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

