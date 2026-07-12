package com.example.bookingservice.service;

import com.example.bookingservice.client.OfficeServiceClient;
import com.example.bookingservice.dto.BookingRequest;
import com.example.bookingservice.dto.DeskDto;
import com.example.bookingservice.model.Booking;
import com.example.bookingservice.repository.BookingRepository;
import com.example.bookingservice.websocket.BookingSocketHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final OfficeServiceClient officeServiceClient;
    private final BookingSocketHandler socketHandler;

    public BookingService(BookingRepository bookingRepository,
                           OfficeServiceClient officeServiceClient,
                           BookingSocketHandler socketHandler) {
        this.bookingRepository = bookingRepository;
        this.officeServiceClient = officeServiceClient;
        this.socketHandler = socketHandler;
    }

    public Booking bookDesk(BookingRequest request) {
        // 1. Ask office-service if the desk exists (cross-service call #1)
        DeskDto desk = officeServiceClient.getDesk(request.getDeskId());

        // 2. Tell office-service to flip it to BOOKED (cross-service call #2).
        //    office-service is the one that actually rejects it if it's
        //    already taken — booking-service doesn't duplicate that logic.
        officeServiceClient.markDeskBooked(desk.getId());

        // 3. Only now save our own record, in our own database.
        Booking booking = new Booking(
                UUID.randomUUID().toString(),
                desk.getId(),
                request.getEmployeeName(),
                LocalDateTime.now()
        );
        bookingRepository.save(booking);

        // 4. Broadcast to every connected client in real time.
        String message = """
                {"event":"DESK_BOOKED","deskId":"%s","employeeName":"%s","bookedAt":"%s"}
                """.formatted(desk.getId(), booking.getEmployeeName(), booking.getBookedAt());
        socketHandler.broadcast(message);

        return booking;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
