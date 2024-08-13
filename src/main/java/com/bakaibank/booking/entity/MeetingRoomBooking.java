package com.bakaibank.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meeting_rooms_bookings")
@Data
@ToString
@NoArgsConstructor
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "MeetingRoomBooking.withMeetingRoomInfoAndFullEmployeeInfo",
                attributeNodes = {
                        @NamedAttributeNode("meetingRoom"),
                        @NamedAttributeNode(value = "employee", subgraph = "employee-position-team")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "employee-position-team",
                                attributeNodes = {
                                        @NamedAttributeNode("position"),
                                        @NamedAttributeNode("team")
                                }
                        )
                }
        )
})
public class MeetingRoomBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_room_id")
    private MeetingRoom meetingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meeting_rooms_bookings_participants",
            joinColumns = @JoinColumn(name = "meeting_room_booking_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "employee_id", nullable = false)
    )
    private List<Employee> participants = new ArrayList<>();

    public MeetingRoomBooking(MeetingRoom meetingRoom, Employee employee, String topic, LocalDate date,
                              LocalTime startTime, LocalTime endTime, List<Employee> participants) {
        this.meetingRoom = meetingRoom;
        this.employee = employee;
        this.topic = topic;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
    }
}
