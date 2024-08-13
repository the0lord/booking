package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}
