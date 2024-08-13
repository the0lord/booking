package com.bakaibank.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Расписание выхода команд. Если запись существует, то для места place в дату date
 * бронирование будет открыто только для тех сотрудников, которые принадлежат к команде, указанной в teams
 */
@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@ToString
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "schedule_teams",
            joinColumns = @JoinColumn(name = "schedule_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "team_id", nullable = false)
    )
    private List<Team> teams = new ArrayList<>();

    public Schedule(Place place, LocalDate date, List<Team> teams) {
        this.place = place;
        this.date = date;
        this.teams = teams;
    }
}
