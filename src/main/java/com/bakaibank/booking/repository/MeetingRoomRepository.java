package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {

}
