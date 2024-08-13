package com.bakaibank.booking.dto.booking.rooms;


import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateMeetingRoomBookingDTO extends AbstractMeetingRoomBookingDTO {
    @Null
    private Long employeeId;
}
