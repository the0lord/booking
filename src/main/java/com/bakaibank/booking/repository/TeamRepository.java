package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByIdIn(Collection<Long> ids);
}
