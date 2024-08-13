package com.bakaibank.booking.dto.booking.rooms;

import com.bakaibank.booking.entity.MeetingRoom;
import com.bakaibank.booking.validation.annotations.ForeignKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractMeetingRoomBookingDTO {
    @NotNull(message = "Укажите переговорную")
    @ForeignKey(message = "Переговорная комната с таким ID не найдена", entity = MeetingRoom.class)
    protected Long meetingRoomId;

    @NotBlank(message = "Укажите тему собрания")
    protected String topic;

    @NotNull(message = "Укажите дату собрания")
    protected LocalDate date;

    @NotNull(message = "Укажите время начала собрания")
    protected LocalTime startTime;

    @NotNull(message = "Укажите время конца собрания")
    protected LocalTime endTime;

    @NotNull(message = "Выберите участников собрания")
    protected Set<String> participants;
}
