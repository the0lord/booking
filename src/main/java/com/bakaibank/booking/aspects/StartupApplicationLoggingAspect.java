package com.bakaibank.booking.aspects;

import com.bakaibank.booking.StartupApplicationRunner;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class StartupApplicationLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(StartupApplicationRunner.class);

    @Pointcut("execution(* com.bakaibank.booking.StartupApplicationRunner.run(..))")
    public void applicationRunnerPointcut() {}

    @Pointcut("execution(* com.bakaibank.booking.service.impl.InitializationService.initialize())")
    public void initializePointCut() {}

    @Before("applicationRunnerPointcut()")
    public void logBefore() {
        logger.info("Запущена инициализация ролей и учетной записи администратора");
    }

    @AfterReturning("applicationRunnerPointcut()")
    public void logAfter() {
        logger.info("Завершена инициализация ролей и учетной записи администратора");
    }

    @Before("initializePointCut()")
    public void logBeforeInitialize() {
        logger.info("Начало создания ролей и учетной записи администратора");
    }

    @AfterReturning("initializePointCut()")
    public void logAfterInitialize() {
        logger.info("Роли и учетная запись администратора успешно созданы");
    }

    @AfterThrowing(value = "initializePointCut()")
    public void logAfterInitializeThrowing() {
        logger.error("При создании ролей и учетной записи администратора произошла ошибка");
    }

    @AfterThrowing(pointcut = "applicationRunnerPointcut()")
    public void logAfterThrowing() {
        logger.error("При инициализации произошла ошибка");
    }
}
