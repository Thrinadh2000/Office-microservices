package com.example.bookingservice.dto;

import jakarta.validation.constraints.NotBlank;

public class BookingRequest {

    @NotBlank(message = "deskId is required")
    private String deskId;

    @NotBlank(message = "employeeName is required")
    private String employeeName;

    public String getDeskId() { return deskId; }
    public void setDeskId(String deskId) { this.deskId = deskId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
}
