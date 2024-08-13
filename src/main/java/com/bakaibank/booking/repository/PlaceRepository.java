package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p, " +
            "CASE WHEN EXISTS (SELECT b FROM Booking b JOIN b.place bp WHERE bp = p AND b.bookingDate = :date) THEN true ELSE false END, " +
            "CASE WHEN EXISTS (SELECT pl " +
            "FROM PlaceLock pl JOIN pl.place plp " +
            "WHERE plp = p AND :date BETWEEN pl.lockStartDate AND pl.lockEndDate) THEN true ELSE false END " +
            "FROM Place p")
    List<Object[]> findAllWithBookingAndLockByDate(@Param("date") LocalDate date);

    Optional<Place> findByCodeIgnoreCase(String code);
}
