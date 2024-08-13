package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.dto.booking.rooms.CreateMeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.MeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.UpdateMeetingRoomBookingDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.MeetingRoomBooking;
import com.bakaibank.booking.exceptions.ValidationException;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.MeetingRoomBookingRepository;
import com.bakaibank.booking.repository.MeetingRoomRepository;
import com.bakaibank.booking.service.MeetingRoomBookingService;
import com.bakaibank.booking.validation.CreateMeetingRoomBookingValidator;
import com.bakaibank.booking.validation.UpdateMeetingRoomBookingValidator;
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
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class MeetingRoomBookingServiceImpl implements MeetingRoomBookingService {
    private final MeetingRoomBookingRepository meetingRoomBookingRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final CreateMeetingRoomBookingValidator createMeetingRoomBookingValidator;
    private final UpdateMeetingRoomBookingValidator updateMeetingRoomBookingValidator;

    @Override
    public List<MeetingRoomBookingDTO> findAllByDate(LocalDate date) {
        List<MeetingRoomBooking> meetingRoomBookings = meetingRoomBookingRepository.findAllByDate(date);

        return meetingRoomBookings.stream()
                .map(meetingRoomBooking -> modelMapper.map(meetingRoomBooking, MeetingRoomBookingDTO.class))
                .toList();
    }

    @Override
    public List<MeetingRoomBookingDTO> findAllByMeetingRoomIdAndDate(Long meetingRoomId, LocalDate date) {
        List<MeetingRoomBooking> meetingRoomBookings = meetingRoomBookingRepository
                .findAllByMeetingRoom_IdAndDate(meetingRoomId, date);

        return meetingRoomBookings.stream()
                .map(meetingRoomBooking -> modelMapper.map(meetingRoomBooking, MeetingRoomBookingDTO.class))
                .toList();
    }

    @Override
    public Optional<MeetingRoomBookingDTO> findById(Long id) {
        return meetingRoomBookingRepository.findByIdWithMeetingRoomInfoAndFullEmployeeInfo(id)
                .map(meetingRoomBooking -> modelMapper.map(meetingRoomBooking, MeetingRoomBookingDTO.class));
    }

    @Override
    public MeetingRoomBookingDTO save(@Valid CreateMeetingRoomBookingDTO createMeetingRoomBookingDTO,
                                      BookingUserDetails userDetails) {
        createMeetingRoomBookingDTO.setEmployeeId(userDetails.getId());
        //Добавляем организатора встречи в список участников, чтобы не валидировать его отдельно.
        //Если он уже был добавлен, ничего не произойдет, т.к. это Set
        createMeetingRoomBookingDTO.getParticipants().add(userDetails.getUsername());

        Errors errors = new BeanPropertyBindingResult(createMeetingRoomBookingDTO, "meetingRoomBookingErrors");
        createMeetingRoomBookingValidator.validate(createMeetingRoomBookingDTO, errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        MeetingRoomBooking meetingRoomBooking = meetingRoomBookingRepository.save(
                modelMapper.map(createMeetingRoomBookingDTO, MeetingRoomBooking.class));

        return modelMapper.map(meetingRoomBooking, MeetingRoomBookingDTO.class);
    }

    @Override
    public MeetingRoomBookingDTO update(Long meetingRoomBookingId, UpdateMeetingRoomBookingDTO updateMeetingRoomBookingDTO)
            throws ResponseStatusException, ValidationException {
        MeetingRoomBooking meetingRoomBooking = meetingRoomBookingRepository
                .findByIdWithMeetingRoomInfoAndFullEmployeeInfo(meetingRoomBookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Errors errors = new BeanPropertyBindingResult(updateMeetingRoomBookingDTO, "updateMeetingRoomBookingErrors");
        updateMeetingRoomBookingValidator.setTargetMeetingRoomBooking(meetingRoomBooking);
        updateMeetingRoomBookingValidator.validate(updateMeetingRoomBookingDTO, errors);

        if (errors.hasErrors())
            throw new ValidationException(errors);

        //Делаем запрос к БД, только если переговорная изменилась
        if(!meetingRoomBooking.getMeetingRoom().getId().equals(updateMeetingRoomBookingDTO.getMeetingRoomId())) {
            meetingRoomBooking.setMeetingRoom(meetingRoomRepository
                    .findById(updateMeetingRoomBookingDTO.getMeetingRoomId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
            );
        }

        meetingRoomBooking.setTopic(updateMeetingRoomBookingDTO.getTopic());
        meetingRoomBooking.setDate(updateMeetingRoomBookingDTO.getDate());
        meetingRoomBooking.setStartTime(updateMeetingRoomBookingDTO.getStartTime());
        meetingRoomBooking.setEndTime(updateMeetingRoomBookingDTO.getEndTime());
        meetingRoomBooking.setParticipants(getEmployeesFromParticipantsUsernamesList(updateMeetingRoomBookingDTO.getParticipants()));

        return modelMapper.map(meetingRoomBookingRepository.save(meetingRoomBooking), MeetingRoomBookingDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        meetingRoomBookingRepository.deleteById(id);
    }

    @Override
    public List<Employee> getEmployeesFromParticipantsUsernamesList(Collection<String> participantsUsernames) {
        return employeeRepository.findAllByUsernameIn(participantsUsernames);
    }
}
