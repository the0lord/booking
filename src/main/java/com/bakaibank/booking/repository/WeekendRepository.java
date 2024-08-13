package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Weekend;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WeekendRepository extends JpaRepository<Weekend, Long> {
    List<Weekend> findAllByOrderByDateAsc();
    List<Weekend> findAllByDateIsGreaterThanEqualOrderByDateAsc(LocalDate date);
    Optional<Weekend> findByDate(LocalDate date);
    @Modifying
    @Transactional
    @Query("DELETE FROM Weekend w where w.date = :date")
    void deleteByDate(@Param("date") LocalDate date);
    Optional<Weekend> findFirstByDateIn(Set<LocalDate> dates);
    boolean existsByDate(LocalDate date);
}
