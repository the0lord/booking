package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.PlaceLock;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlaceLockRepository extends JpaRepository<PlaceLock, Long> {
    @EntityGraph(value = "PlaceLock.withPlaceAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT pl FROM PlaceLock pl")
    List<PlaceLock> findAllWithPlaceAndEmployeeInfo();


    @EntityGraph(value = "PlaceLock.withPlaceAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT pl FROM PlaceLock pl WHERE pl.id = :id")
    Optional<PlaceLock> findByIdWithPlaceAndEmployeeInfo(Long id);


    /**
     * Выбирает ближайшую (по полю lockStartDate) к дате date блокировку места с ID placeId
     * @param placeId ID места
     * @param date Дата
     */
    @Query("SELECT pl " +
            "FROM PlaceLock pl JOIN pl.place place " +
            "WHERE place.id = :placeId AND :date BETWEEN pl.lockStartDate AND pl.lockEndDate " +
            "ORDER BY pl.lockStartDate LIMIT 1")
    Optional<PlaceLock> findNearestPlaceLockByPlaceIdAndDate(Long placeId, LocalDate date);


    /**
     * Проверяет, существует ли блокировка для этого места,
     * которая пересекается по датам с планируемой блокировкой
     * @param placeId ID места
     * @param lockStartDate Дата начала планируемой блокировки
     * @param lockEndDate Дата конца планируемой блокировки
     */
    @Query("SELECT COUNT(pl.id) > 0 " +
            "FROM PlaceLock pl JOIN pl.place place " +
            "WHERE place.id = :placeId " +
            "AND ((pl.lockStartDate BETWEEN :lockStartDate AND :lockEndDate) OR (pl.lockEndDate BETWEEN :lockStartDate AND :lockEndDate))")
    boolean isPlaceAlreadyLocked(Long placeId, LocalDate lockStartDate, LocalDate lockEndDate);


    /**
     * Проверяет, пересекается ли ОБНОВЛЯЕМАЯ запись о блокировке места с другими блокировками этого же места
     * @param updatablePlaceLockId ID обновляемой записи о блокировке
     * @param placeId ID места
     * @param lockStartDate Дата начала блокировки
     * @param lockEndDate Дата конца блокировки
     */
    @Query("SELECT COUNT(pl.id) > 0 " +
            "FROM PlaceLock pl JOIN pl.place place " +
            "WHERE pl.id != :updatablePlaceLockId " +
            "AND place.id = :placeId " +
            "AND ((pl.lockStartDate BETWEEN :lockStartDate AND :lockEndDate) OR (pl.lockEndDate BETWEEN :lockStartDate AND :lockEndDate))")
    boolean isUpdatablePlaceLockIntersectsWithOtherPlaceLock(Long updatablePlaceLockId, Long placeId, LocalDate lockStartDate, LocalDate lockEndDate);


    /**
     * Проверяет, закреплен ли сотрудник employeeId в указанные даты за другим местом (отличным, от переданного placeId)
     * @param placeId ID места, за которым планируется закрепить сотрудника
     * @param employeeId ID сотрудника
     * @param lockStartDate Дата начала блокировки места
     * @param lockEndDate Дата конца блокировки места
     */
    @Query("SELECT COUNT(pl.id) > 0 " +
            "FROM PlaceLock pl JOIN pl.place p JOIN pl.assignedEmployee emp " +
            "WHERE p.id != :placeId " +
            "AND emp.id = :employeeId " +
            "AND ((pl.lockStartDate BETWEEN :lockStartDate AND :lockEndDate) OR (pl.lockEndDate BETWEEN :lockStartDate AND :lockEndDate))")
    boolean isEmployeeAlreadyAssignedToAnotherPlace(Long placeId, Long employeeId, LocalDate lockStartDate, LocalDate lockEndDate);
}
