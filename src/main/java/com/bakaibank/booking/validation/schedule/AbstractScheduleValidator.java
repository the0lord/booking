package com.bakaibank.booking.validation.schedule;

import com.bakaibank.booking.dto.schedule.AbstractScheduleDTO;
import com.bakaibank.booking.entity.Team;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.repository.PlaceLockRepository;
import com.bakaibank.booking.repository.TeamRepository;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public abstract class AbstractScheduleValidator {
    protected final TeamRepository teamRepository;
    protected final PlaceLockRepository placeLockRepository;
    protected final BookingRepository bookingRepository;

    public AbstractScheduleValidator(TeamRepository teamRepository, PlaceLockRepository placeLockRepository, BookingRepository bookingRepository) {
        this.teamRepository = teamRepository;
        this.placeLockRepository = placeLockRepository;
        this.bookingRepository = bookingRepository;
    }

    public void validate(AbstractScheduleDTO dto, Errors errors) {
        if(!validateTeamList(dto.getTeams(), errors)) return;

        if(isPlaceLocked(dto.getPlaceId(), dto.getDate(), errors)) return;

        if(isPlaceBooked(dto.getPlaceId(), dto.getDate(), errors)) return;

        if(isScheduleAlreadyExists(dto)) {
            errors.reject("scheduleAlreadyExists",
                    "Для этого места в указанную дату уже существует настроенное расписание");
            return;
        }
    }

    protected abstract boolean isScheduleAlreadyExists(AbstractScheduleDTO dto);

    protected boolean isPlaceLocked(Long placeId, LocalDate date, Errors errors) {
        if(placeLockRepository.findNearestPlaceLockByPlaceIdAndDate(placeId, date).isPresent()) {
            errors.reject("placeIsLocked", "Невозможно создать расписание для этого места" +
                    " т.к. оно заблокировано в эту дату");
            return true;
        }

        return false;
    }

    protected boolean isPlaceBooked(Long placeId, LocalDate date, Errors errors) {
        if(bookingRepository.existsByPlace_IdAndBookingDate(placeId, date)) {
            errors.reject("placeIsBooked", "Невозможно создать расписание для этого места" +
                    " т.к. оно забронировано в эту дату");
            return false;
        }

        return true;
    }

    protected boolean validateTeamList(Collection<Long> teamsIds, Errors errors) {
        List<Team> teams = teamRepository.findByIdIn(teamsIds);

        for(Long id : teamsIds) {
            if(teams.stream().noneMatch(team -> team.getId().equals(id))) {
                errors.reject("teamDoesNotExists", "Команда с ID " + id + " не существует");
                return false;
            }
        }

        return true;
    }
}
