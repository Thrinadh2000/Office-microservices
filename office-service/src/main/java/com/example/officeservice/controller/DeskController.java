package com.example.officeservice.controller;

import com.example.officeservice.dto.DeskStatusUpdateRequest;
import com.example.officeservice.model.Desk;
import com.example.officeservice.service.DeskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desks")
public class DeskController {

    private final DeskService deskService;

    public DeskController(DeskService deskService) {
        this.deskService = deskService;
    }

    @GetMapping
    public List<Desk> getAllDesks() {
        return deskService.getAllDesks();
    }

    @GetMapping("/{id}")
    public Desk getDesk(@PathVariable String id) {
        return deskService.getDesk(id);
    }

    // Called by booking-service (not by end users directly) whenever a
    // booking is made or cancelled, so office-service stays the single
    // source of truth for desk status.
    @PatchMapping("/{id}/status")
    public Desk updateStatus(@PathVariable String id, @Valid @RequestBody DeskStatusUpdateRequest request) {
        return deskService.updateStatus(id, request.getStatus());
    }
}
