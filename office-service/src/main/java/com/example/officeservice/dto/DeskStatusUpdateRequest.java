package com.example.officeservice.dto;

import com.example.officeservice.model.Desk;
import jakarta.validation.constraints.NotNull;

public class DeskStatusUpdateRequest {

    @NotNull(message = "status is required")
    private Desk.Status status;

    public Desk.Status getStatus() { return status; }
    public void setStatus(Desk.Status status) { this.status = status; }
}
