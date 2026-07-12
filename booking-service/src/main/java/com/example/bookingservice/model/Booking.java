package com.example.bookingservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    private String id;

    private String deskId;

    private String employeeName;

    private LocalDateTime bookedAt;

    protected Booking() {} // required by JPA

    public Booking(String id, String deskId, String employeeName, LocalDateTime bookedAt) {
        this.id = id;
        this.deskId = deskId;
        this.employeeName = employeeName;
        this.bookedAt = bookedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDeskId() { return deskId; }
    public void setDeskId(String deskId) { this.deskId = deskId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }
}
