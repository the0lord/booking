package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.weekend.CreateWeekendDTO;
import com.bakaibank.booking.dto.weekend.WeekendDTO;
import com.bakaibank.booking.entity.Weekend;
import com.bakaibank.booking.exceptions.ValidationException;
import com.bakaibank.booking.repository.WeekendRepository;
import com.bakaibank.booking.service.WeekendService;
import com.bakaibank.booking.validation.WeekendValidator;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WeekendServiceImpl implements WeekendService {
    private final WeekendRepository weekendRepository;

    private final ModelMapper modelMapper;

    private final WeekendValidator weekendValidator;

    public WeekendServiceImpl(WeekendRepository weekendRepository,
                              ModelMapper modelMapper,
                              WeekendValidator weekendValidator) {
        this.weekendRepository = weekendRepository;
        this.modelMapper = modelMapper;
        this.weekendValidator = weekendValidator;
    }

    @Override
    public List<WeekendDTO> findAll() {
        List<Weekend> weekends = weekendRepository.findAllByOrderByDateAsc();
        return weekends.stream()
                .map(weekend -> modelMapper.map(weekend, WeekendDTO.class))
                .toList();
    }

    @Override
    public Optional<WeekendDTO> findByDate(LocalDate date) {
        return weekendRepository.findByDate(date)
                .map(weekend -> modelMapper.map(weekend, WeekendDTO.class));
    }

    @Override
    @Transactional
    public List<WeekendDTO> saveAll(CreateWeekendDTO createWeekendDTO) {
        Errors errors = new BeanPropertyBindingResult(createWeekendDTO, "weekendErrors");
        weekendValidator.validate(createWeekendDTO, errors);

        if(errors.hasErrors()) throw new ValidationException(errors);

        List<Weekend> weekends = weekendRepository.saveAll(createWeekendDTO
                .getWeekends().stream()
                .map(Weekend::new)
                .toList());

        return weekends.stream()
                .map(weekend -> modelMapper.map(weekend, WeekendDTO.class))
                .toList();
    }

    @Override
    public void deleteByDate(LocalDate date) {
        weekendRepository.deleteByDate(date);
    }
}
