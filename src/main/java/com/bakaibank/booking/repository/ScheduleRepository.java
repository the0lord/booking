package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Schedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @EntityGraph(attributePaths = {"place", "teams"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT s FROM Schedule s WHERE s.date = :date")
    List<Schedule> findAllByDateWithPlaceAndTeams(LocalDate date);

    @EntityGraph(attributePaths = {"place", "teams"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT s FROM Schedule s WHERE s.id = :id")
    Optional<Schedule> findByIdWithPlaceAndTeams(Long id);

    @EntityGraph(attributePaths = {"teams"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Schedule> findByPlace_IdAndDate(Long placeId, LocalDate date);

    Optional<Schedule> findFirstByPlace_IdAndDateBetweenOrderByDate(Long placeId, LocalDate startDate, LocalDate endDate);

    boolean existsByPlace_IdAndDate(Long placeId, LocalDate date);

    /**
     * Проверка, существует ли уже для этого места в эту дату расписание помимо обновляемого расписания
     * @param placeId ID места
     * @param date Дата
     * @param scheduleId ID обновляемого расписания
     */
    boolean existsByPlace_IdAndDateAndIdNot(Long placeId, LocalDate date, Long scheduleId);
}
