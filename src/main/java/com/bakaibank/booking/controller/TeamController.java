package com.bakaibank.booking.controller;

import com.bakaibank.booking.core.security.AdminRoleRequired;
import com.bakaibank.booking.dto.team.CreateTeamDTO;
import com.bakaibank.booking.dto.team.TeamDTO;
import com.bakaibank.booking.dto.team.UpdateTeamDTO;
import com.bakaibank.booking.exceptions.RelatedEntityExistsException;
import com.bakaibank.booking.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<TeamDTO> findAllTeams() {
        return teamService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> findTeamById(@PathVariable Long id) {
        return teamService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AdminRoleRequired
    public TeamDTO createTeam(@RequestBody CreateTeamDTO createTeamDTO) {
        return teamService.save(createTeamDTO);
    }

    @PutMapping("/{id}")
    @AdminRoleRequired
    public TeamDTO updateTeamById(@PathVariable Long id, @RequestBody UpdateTeamDTO updateTeamDTO) {
        return teamService.update(id, updateTeamDTO);
    }

    @DeleteMapping("/{id}")
    @AdminRoleRequired
    public ResponseEntity<?> deleteTeamById(@PathVariable Long id) throws RelatedEntityExistsException {
        return teamService.findById(id)
                .map(team -> {
                    teamService.deleteById(id);
                    return ResponseEntity.ok(team);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
