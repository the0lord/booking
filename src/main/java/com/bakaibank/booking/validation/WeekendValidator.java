package com.bakaibank.booking.validation;


import com.bakaibank.booking.dto.weekend.CreateWeekendDTO;
import com.bakaibank.booking.repository.WeekendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class WeekendValidator implements Validator {
    private final WeekendRepository weekendRepository;

    @Autowired
    public WeekendValidator(WeekendRepository weekendRepository) {
        this.weekendRepository = weekendRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateWeekendDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateWeekendDTO createWeekendDTO = (CreateWeekendDTO) target;

        if(createWeekendDTO.getWeekends() == null) {
            errors.reject("noDatesProvided", "Список дат не может быть пустым");
        }

        weekendRepository.findFirstByDateIn(createWeekendDTO.getWeekends())
                .ifPresent(
                        firstDate -> errors.reject(
                                "dateAlreadyExists",
                                "Дата " + firstDate.getDate() + " уже существует в списке выходных дней"
                        )
                );
    }


}
