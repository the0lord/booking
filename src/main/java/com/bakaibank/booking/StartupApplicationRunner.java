package com.bakaibank.booking;

import com.bakaibank.booking.service.impl.InitializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StartupApplicationRunner implements ApplicationRunner  {
    private final InitializationService initializationService;


    @Override
    public void run(ApplicationArguments args) {
        initializationService.initialize();
    }
}
