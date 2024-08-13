package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Booking;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    @EntityGraph(value = "Booking.withPlaceAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Booking> findByPlace_IdAndBookingDate(Long placeId, LocalDate date);

    @EntityGraph(value = "Booking.withPlaceAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAllByBookingDate(LocalDate date);

    @EntityGraph(value = "Booking.withPlaceAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Booking b WHERE b.id = :id")
    Optional<Booking> findByIdWithFullEmployeeInfo(@Param("id") Long id);

    @Query("SELECT emp.username FROM Booking b JOIN b.employee emp WHERE b.id = :id")
    Optional<String> findBookingCreatorByBookingId(@Param("id") Long id);

    @EntityGraph(value = "Booking.withPlaceAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAll(Specification<Booking> spec);

    boolean existsByPlace_IdAndBookingDate(Long placeId, LocalDate bookingDate);

    boolean existsByEmployee_IdAndBookingDate(Long employeeId, LocalDate bookingDate);

    boolean existsByPlace_IdAndBookingDateBetween(Long placeId, LocalDate startDate, LocalDate endDate);
}
