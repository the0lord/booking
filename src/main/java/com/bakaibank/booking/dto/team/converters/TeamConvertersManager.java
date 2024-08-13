package com.bakaibank.booking.dto.team.converters;

import com.bakaibank.booking.dto.team.CreateTeamDTO;
import com.bakaibank.booking.dto.team.TeamDTO;
import com.bakaibank.booking.entity.Team;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class TeamConvertersManager {
    public static Converter<Team, TeamDTO> toTeamDTO = new Converter<>() {
        @Override
        public TeamDTO convert(MappingContext<Team, TeamDTO> mappingContext) {
            Team source = mappingContext.getSource();

            return new TeamDTO(
                    source.getId(),
                    source.getName()
            );
        }
    };

    public static Converter<CreateTeamDTO, Team> toTeam = new Converter<>() {
        @Override
        public Team convert(MappingContext<CreateTeamDTO, Team> mappingContext) {
            CreateTeamDTO source = mappingContext.getSource();

            return new Team(
                    source.getName()
            );
        }
    };
}
