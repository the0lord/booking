package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.MeetingRoomBooking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MeetingRoomBookingRepository extends JpaRepository<MeetingRoomBooking, Long> {
    @EntityGraph(value = "MeetingRoomBooking.withMeetingRoomInfoAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    List<MeetingRoomBooking> findAllByDate(LocalDate date);

    @EntityGraph(value = "MeetingRoomBooking.withMeetingRoomInfoAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    List<MeetingRoomBooking> findAllByMeetingRoom_IdAndDate(Long meetingRoomId, LocalDate date);

    @EntityGraph(value = "MeetingRoomBooking.withMeetingRoomInfoAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT mrb FROM MeetingRoomBooking mrb WHERE mrb.id = :id")
    Optional<MeetingRoomBooking> findByIdWithMeetingRoomInfoAndFullEmployeeInfo(@Param("id") Long id);

    @Query("SELECT emp.username FROM MeetingRoomBooking mrb JOIN mrb.employee emp WHERE mrb.id = :id")
    Optional<String> findBookingCreatorByBookingId(@Param("id") Long id);

    /**
     * Проверяет, пересекается ли планируемая бронь с другими бронями
     * @param roomId ID переговорной, в которой планируется встреча
     * @param date Дата планируемой встречи
     * @param startTime Время начала встречи
     * @param endTime Время конца встречи
     */
    @Query("SELECT COUNT(mrb.id) > 0 " +
            "FROM MeetingRoomBooking mrb " +
            "JOIN mrb.meetingRoom room " +
            "WHERE room.id = :roomId " +
            "AND mrb.date = :date " +
            "AND ((mrb.startTime BETWEEN :startTime AND :endTime) OR (mrb.endTime BETWEEN :startTime AND :endTime))")
    boolean isMeetingIntersectsWithOtherMeetings(@Param("roomId") Long roomId,
                                                 @Param("date") LocalDate date,
                                                 @Param("startTime") LocalTime startTime,
                                                 @Param("endTime") LocalTime endTime);

    /**
     * Проверяет, пересекается ли ОБНОВЛЯЕМАЯ бронь с другими бронями.
     * Здесь добавлено условие, чтобы ID выбираемой брони не совпадало с обновляемой, т.к. если этого не сделать,
     * обновляемая бронь всегда будет считаться конфликтующей
     * @param updatableBookingId ID обновляемой встречи
     * @param roomId ID переговороной, в которой запланирована встреча
     * @param date Дата встречи
     * @param startTime Время начала встречи
     * @param endTime Дата конца встречи
     */
    @Query("SELECT COUNT(mrb.id) > 0 " +
            "FROM MeetingRoomBooking mrb " +
            "JOIN mrb.meetingRoom room " +
            "WHERE mrb.id != :updatableBookingId " +
            "AND room.id = :roomId " +
            "AND mrb.date = :date " +
            "AND ((mrb.startTime BETWEEN :startTime AND :endTime) OR (mrb.endTime BETWEEN :startTime AND :endTime))")
    boolean isUpdatableMeetingIntersectsWithOtherMeetings(@Param("updatableBookingId") Long updatableBookingId,
                                                          @Param("roomId") Long roomId,
                                                          @Param("date") LocalDate date,
                                                          @Param("startTime") LocalTime startTime,
                                                          @Param("endTime") LocalTime endTime);

    /**
     * Выбирает список имен (username) сотрудников, у которых уже есть запланированные встречи, пересекающиеся с планируемой встречей
     * @param participantUsernames Список имен (username) сотрудников
     * @param date Дата планируемой встречи
     * @param startTime Время начала планируемой встречи
     * @param endTime Время конца планируемой встречи
     * @return Список имен (username) сотрудников
     */
    @Query("SELECT p.username " +
            "FROM MeetingRoomBooking mrb " +
            "JOIN mrb.participants p " +
            "WHERE p.username IN :participantUsernames " +
            "AND mrb.date = :date " +
            "AND ((:startTime BETWEEN mrb.startTime AND mrb.endTime) " +
            "OR (:endTime BETWEEN mrb.startTime AND mrb.endTime) " +
            "OR (mrb.startTime BETWEEN :startTime AND :endTime))")
    List<String> findEmployeesWithConflictBooking(@Param("participantUsernames") Collection<String> participantUsernames,
                                                  @Param("date") LocalDate date,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime);

    /**
     * Выбирает список имен (username) сотрудников, у которых уже есть запланнированные встречи,
     * пересекающиеся с любой другой встречей, кроме обновляемой.
     * @param participantUsernames Список имен (username) сотрудников
     * @param updatableBookingId ID обновляемой брони
     * @param date Дата встречи
     * @param startTime Время начала встречи
     * @param endTime Время конца встречи
     * @return Список имен (username) сотрудников
     */
    @Query("SELECT p.username " +
            "FROM MeetingRoomBooking mrb " +
            "JOIN mrb.participants p " +
            "WHERE p.username IN :participantUsernames " +
            "AND mrb.id != :updatableBookingId " +
            "AND mrb.date = :date " +
            "AND ((:startTime BETWEEN mrb.startTime AND mrb.endTime) " +
            "OR (:endTime BETWEEN mrb.startTime AND mrb.endTime) " +
            "OR (mrb.startTime BETWEEN :startTime AND :endTime))")
    List<String> findEmployeesWithConflictBookingExceptUpdatableBooking(@Param("participantUsernames") Collection<String> participantUsernames,
                                                                        @Param("updatableBookingId") Long updatableBookingId,
                                                                        @Param("date") LocalDate date,
                                                                        @Param("startTime") LocalTime startTime,
                                                                        @Param("endTime") LocalTime endTime);
}
