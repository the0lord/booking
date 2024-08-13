package com.bakaibank.booking.dto.meetingroom.converters;

import com.bakaibank.booking.dto.meetingroom.CreateMeetingRoomDTO;
import com.bakaibank.booking.dto.meetingroom.MeetingRoomDTO;
import com.bakaibank.booking.entity.MeetingRoom;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class MeetingRoomConvertersManager {
    public static Converter<MeetingRoom, MeetingRoomDTO> meetingRoomToMeetingRoomDTOConverter() {
        return new Converter<>() {
            @Override
            public MeetingRoomDTO convert(MappingContext<MeetingRoom, MeetingRoomDTO> mappingContext) {
                MeetingRoom meetingRoom = mappingContext.getSource();
                return new MeetingRoomDTO(
                        meetingRoom.getId(),
                        meetingRoom.getCode()
                );
            }
        };
    }

    public static Converter<CreateMeetingRoomDTO, MeetingRoom> createMeetingRoomDTOToMeetingRoomConverter() {
        return new Converter<>() {
            @Override
            public MeetingRoom convert(MappingContext<CreateMeetingRoomDTO, MeetingRoom> mappingContext) {
                CreateMeetingRoomDTO createMeetingRoomDTO = mappingContext.getSource();
                return new MeetingRoom(
                        createMeetingRoomDTO.getCode()
                );
            }
        };
    }
}
