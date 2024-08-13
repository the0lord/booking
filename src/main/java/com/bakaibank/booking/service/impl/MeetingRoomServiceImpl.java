package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.meetingroom.CreateMeetingRoomDTO;
import com.bakaibank.booking.dto.meetingroom.MeetingRoomDTO;
import com.bakaibank.booking.entity.MeetingRoom;
import com.bakaibank.booking.repository.MeetingRoomRepository;
import com.bakaibank.booking.service.MeetingRoomService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class MeetingRoomServiceImpl implements MeetingRoomService {
    private final MeetingRoomRepository meetingRoomRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public MeetingRoomServiceImpl(MeetingRoomRepository meetingRoomRepository, ModelMapper modelMapper) {
        this.meetingRoomRepository = meetingRoomRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<MeetingRoomDTO> findAll() {
        List<MeetingRoom> meetingRooms = meetingRoomRepository.findAll();
        return meetingRooms.stream()
                .map(meetingRoom -> modelMapper.map(meetingRoom, MeetingRoomDTO.class))
                .toList();
    }

    @Override
    public Optional<MeetingRoomDTO> findById(Long id) {
        return meetingRoomRepository.findById(id)
                .map(meetingRoom -> modelMapper.map(meetingRoom, MeetingRoomDTO.class));
    }

    @Override
    public MeetingRoomDTO save(@Valid CreateMeetingRoomDTO createMeetingRoomDTO) {
        MeetingRoom meetingRoom = meetingRoomRepository.save(modelMapper.map(createMeetingRoomDTO, MeetingRoom.class));
        return modelMapper.map(meetingRoom, MeetingRoomDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        meetingRoomRepository.deleteById(id);
    }
}
