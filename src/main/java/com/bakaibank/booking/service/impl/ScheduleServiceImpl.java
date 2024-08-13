package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.schedule.CreateScheduleDTO;
import com.bakaibank.booking.dto.schedule.ScheduleDTO;
import com.bakaibank.booking.dto.schedule.UpdateScheduleDTO;
import com.bakaibank.booking.entity.Schedule;
import com.bakaibank.booking.exceptions.ValidationException;
import com.bakaibank.booking.repository.PlaceRepository;
import com.bakaibank.booking.repository.ScheduleRepository;
import com.bakaibank.booking.repository.TeamRepository;
import com.bakaibank.booking.service.ScheduleService;
import com.bakaibank.booking.validation.schedule.CreateScheduleValidator;
import com.bakaibank.booking.validation.schedule.UpdateScheduleValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PlaceRepository placeRepository;
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final CreateScheduleValidator createScheduleValidator;
    private final UpdateScheduleValidator updateScheduleValidator;

    @Override
    public List<ScheduleDTO> findAllByDate(LocalDate date) {
        List<Schedule> schedules = scheduleRepository.findAllByDateWithPlaceAndTeams(date);

        return schedules.stream()
                .map(schedule -> modelMapper.map(schedule, ScheduleDTO.class))
                .toList();
    }

    @Override
    public Optional<ScheduleDTO> findById(Long id) {
        return scheduleRepository.findByIdWithPlaceAndTeams(id)
                .map(schedule -> modelMapper.map(schedule, ScheduleDTO.class));
    }

    @Override
    public ScheduleDTO save(@Valid CreateScheduleDTO createScheduleDTO) throws ValidationException {
        Errors errors = new BeanPropertyBindingResult(createScheduleDTO, "createScheduleErrors");
        createScheduleValidator.validate(createScheduleDTO, errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        Schedule schedule = modelMapper.map(createScheduleDTO, Schedule.class);
        return modelMapper.map(scheduleRepository.save(schedule), ScheduleDTO.class);
    }

    @Override
    public ScheduleDTO update(Long scheduleId, @Valid UpdateScheduleDTO updateScheduleDTO) throws ValidationException {
        Schedule schedule = scheduleRepository.findByIdWithPlaceAndTeams(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Errors errors = new BeanPropertyBindingResult(updateScheduleDTO, "updateScheduleErrors");
        updateScheduleValidator.setUpdatableSchedule(schedule);
        updateScheduleValidator.validate(updateScheduleDTO, errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        schedule.setDate(updateScheduleDTO.getDate());
        schedule.setTeams(teamRepository.findByIdIn(updateScheduleDTO.getTeams()));
        schedule.setPlace(placeRepository.findById(updateScheduleDTO.getPlaceId()).orElse(null));

        return modelMapper.map(scheduleRepository.save(schedule), ScheduleDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return scheduleRepository.existsById(id);
    }
}
