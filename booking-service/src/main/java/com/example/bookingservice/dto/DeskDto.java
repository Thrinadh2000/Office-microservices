package com.example.bookingservice.dto;

// This mirrors just the fields booking-service actually needs from
// office-service's Desk. Each service owns its own model — booking-service
// never imports office-service's Desk class directly, only this DTO shape.
public class DeskDto {

    private String id;
    private String name;
    private String officeRoom;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOfficeRoom() { return officeRoom; }
    public void setOfficeRoom(String officeRoom) { this.officeRoom = officeRoom; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
