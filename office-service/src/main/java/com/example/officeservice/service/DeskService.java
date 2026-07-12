package com.example.officeservice.service;

import com.example.officeservice.model.Desk;
import com.example.officeservice.repository.DeskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskService {

    private final DeskRepository deskRepository;

    public DeskService(DeskRepository deskRepository) {
        this.deskRepository = deskRepository;
    }

    public List<Desk> getAllDesks() {
        return deskRepository.findAll();
    }

    public Desk getDesk(String id) {
        return deskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Desk not found: " + id));
    }

    public Desk updateStatus(String id, Desk.Status status) {
        Desk desk = getDesk(id);

        if (status == Desk.Status.BOOKED && desk.getStatus() == Desk.Status.BOOKED) {
            throw new IllegalStateException("Desk already booked: " + id);
        }

        desk.setStatus(status);
        return deskRepository.save(desk);
    }
}
