package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.team.CreateTeamDTO;
import com.bakaibank.booking.dto.team.TeamDTO;
import com.bakaibank.booking.dto.team.UpdateTeamDTO;
import com.bakaibank.booking.entity.Team;
import com.bakaibank.booking.exceptions.RelatedEntityExistsException;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.TeamRepository;
import com.bakaibank.booking.service.TeamService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository,
                           EmployeeRepository employeeRepository,
                           ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<TeamDTO> findAll() {
        List<Team> teams = teamRepository.findAll();

        return teams.stream()
                .map(team -> modelMapper.map(team, TeamDTO.class))
                .toList();
    }

    @Override
    public Optional<TeamDTO> findById(Long id) {
        return teamRepository.findById(id)
                .map(team -> modelMapper.map(team, TeamDTO.class));
    }

    @Override
    public TeamDTO save(@Valid CreateTeamDTO createTeamDTO) {
        Team team = teamRepository.save(modelMapper.map(createTeamDTO, Team.class));
        return modelMapper.map(team, TeamDTO.class);
    }

    @Override
    public TeamDTO update(Long teamId, @Valid UpdateTeamDTO updateTeamDTO) {
        Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        team.setName(updateTeamDTO.getName());

        return modelMapper.map(teamRepository.save(team), TeamDTO.class);
    }

    @Override
    public void deleteById(Long id) throws RelatedEntityExistsException {
        if(employeeRepository.existsByTeam_Id(id))
            throw new RelatedEntityExistsException("Невозможно удалить эту команду," +
                    " т.к. существует как минимум 1 сотрудник, ссылающийся на нее");

        teamRepository.deleteById(id);
    }
}
