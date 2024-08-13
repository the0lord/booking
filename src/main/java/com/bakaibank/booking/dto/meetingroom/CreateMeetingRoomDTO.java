package com.bakaibank.booking.dto.meetingroom;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingRoomDTO {
    @NotBlank(message = "Укажите код (название) переговорной комнаты")
    private String code;
}
