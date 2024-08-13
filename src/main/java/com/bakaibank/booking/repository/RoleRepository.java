package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByNameIn(Collection<String> roles);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END" +
            " FROM Role r")
    boolean existsAny();
}
