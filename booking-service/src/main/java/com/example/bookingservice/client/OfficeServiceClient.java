package com.example.bookingservice.client;

import com.example.bookingservice.dto.DeskDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

// This is THE microservice boundary: booking-service never reads or writes
// office-service's database directly. It only ever talks over HTTP, the
// same way any external client would. If office-service is down, booking
// fails gracefully instead of corrupting shared data.
@Component
public class OfficeServiceClient {

    private final RestTemplate restTemplate;
    private final String officeServiceUrl;

    public OfficeServiceClient(RestTemplate restTemplate,
                                @Value("${office-service.base-url}") String officeServiceUrl) {
        this.restTemplate = restTemplate;
        this.officeServiceUrl = officeServiceUrl;
    }

    public DeskDto getDesk(String deskId) {
        try {
            return restTemplate.getForObject(officeServiceUrl + "/api/desks/" + deskId, DeskDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Desk not found: " + deskId);
        }
    }

    public void markDeskBooked(String deskId) {
        try {
            restTemplate.patchForObject(
                    officeServiceUrl + "/api/desks/" + deskId + "/status",
                    Map.of("status", "BOOKED"),
                    Void.class
            );
        } catch (HttpClientErrorException.Conflict e) {
            throw new IllegalStateException("Desk already booked: " + deskId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Desk not found: " + deskId);
        }
    }
}
