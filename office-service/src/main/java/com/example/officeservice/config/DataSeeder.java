package com.example.officeservice.config;

import com.example.officeservice.model.Desk;
import com.example.officeservice.repository.DeskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final DeskRepository deskRepository;

    public DataSeeder(DeskRepository deskRepository) {
        this.deskRepository = deskRepository;
    }

    @Override
    public void run(String... args) {
        // Only seed on first run — the H2 file persists between restarts,
        // so this won't wipe real bookings/status on every startup.
        if (deskRepository.count() == 0) {
            deskRepository.save(new Desk("D1", "Desk 1", "Room A", Desk.Status.AVAILABLE));
            deskRepository.save(new Desk("D2", "Desk 2", "Room A", Desk.Status.AVAILABLE));
            deskRepository.save(new Desk("D3", "Desk 3", "Room B", Desk.Status.AVAILABLE));
        }
    }
}
