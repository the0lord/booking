package com.bakaibank.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meeting_rooms")
@Data
@NoArgsConstructor
@ToString
public class MeetingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "meetingRoom", fetch = FetchType.LAZY)
    private List<MeetingRoomBooking> bookings = new ArrayList<>();

    public MeetingRoom(String code) {
        this.code = code;
    }
}
