package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Employee> findByUsernameIgnoreCase(String username);

    @EntityGraph(attributePaths = {"position", "team"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT e FROM Employee e")
    List<Employee> findAllWithPositionAndTeam();

    @EntityGraph(attributePaths = {"position", "team"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT e FROM Employee e WHERE e.id = :id")
    Optional<Employee> findByIdWithPositionAndTeam(@Param("id") Long id);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT e FROM Employee e WHERE e.id = :id")
    Optional<Employee> findByIdWithRoles(Long id);

    List<Employee> findAllByUsernameIn(Collection<String> usernames);

    @Query("SELECT e.username FROM Employee e WHERE e.username IN :usernames")
    List<String> findExistingUsernames(@Param("usernames") Collection<String> usernames);

    boolean existsByPosition_Id(Long id);

    boolean existsByTeam_Id(Long id);

    boolean existsByRoles(List<Role> roles);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END" +
            " FROM Employee e")
    boolean existsAny();
}
