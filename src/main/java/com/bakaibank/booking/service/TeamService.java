package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.team.CreateTeamDTO;
import com.bakaibank.booking.dto.team.TeamDTO;
import com.bakaibank.booking.dto.team.UpdateTeamDTO;
import com.bakaibank.booking.exceptions.RelatedEntityExistsException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    List<TeamDTO> findAll();
    Optional<TeamDTO> findById(Long id);
    TeamDTO save(@Valid CreateTeamDTO createTeamDTO);
    TeamDTO update(Long teamId, @Valid UpdateTeamDTO updateTeamDTO);
    void deleteById(Long id) throws RelatedEntityExistsException;
}
