package com.bakaibank.booking.dto.schedule.converters;

import com.bakaibank.booking.dto.place.PlaceDTO;
import com.bakaibank.booking.dto.schedule.AbstractScheduleDTO;
import com.bakaibank.booking.dto.schedule.CreateScheduleDTO;
import com.bakaibank.booking.dto.schedule.ScheduleDTO;
import com.bakaibank.booking.entity.Place;
import com.bakaibank.booking.entity.Schedule;
import com.bakaibank.booking.entity.Team;
import com.bakaibank.booking.repository.PlaceRepository;
import com.bakaibank.booking.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleConvertersManager {
    private final TeamRepository teamRepository;
    private final PlaceRepository placeRepository;

    public static Converter<Schedule, ScheduleDTO> toScheduleDTO = new Converter<>() {
        @Override
        public ScheduleDTO convert(MappingContext<Schedule, ScheduleDTO> mappingContext) {
            Schedule source = mappingContext.getSource();

            return new ScheduleDTO(
                    source.getId(),
                    source.getPlace().getCode(),
                    source.getDate(),
                    source.getTeams().stream()
                            .map(Team::getName)
                            .toList()
            );
        }
    };

    public Converter<CreateScheduleDTO, Schedule> toSchedule = new Converter<>() {
        @Override
        public Schedule convert(MappingContext<CreateScheduleDTO, Schedule> mappingContext) {
            CreateScheduleDTO source = mappingContext.getSource();

            return new Schedule(
                    placeRepository.findById(source.getPlaceId()).orElse(null),
                    source.getDate(),
                    teamRepository.findByIdIn(source.getTeams())
            );
        }
    };

}
