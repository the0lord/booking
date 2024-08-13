package com.bakaibank.booking.core;

import org.springframework.security.core.userdetails.UserDetails;

public interface BookingUserDetails extends UserDetails {
    Long getId();
}
