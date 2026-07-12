package com.example.officeservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "desks")
public class Desk {

    public enum Status { AVAILABLE, BOOKED }

    @Id
    private String id;

    private String name;

    private String officeRoom;

    @Enumerated(EnumType.STRING)
    private Status status;

    protected Desk() {} // required by JPA

    public Desk(String id, String name, String officeRoom, Status status) {
        this.id = id;
        this.name = name;
        this.officeRoom = officeRoom;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOfficeRoom() { return officeRoom; }
    public void setOfficeRoom(String officeRoom) { this.officeRoom = officeRoom; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
