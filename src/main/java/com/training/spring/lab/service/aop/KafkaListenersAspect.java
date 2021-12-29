package com.training.spring.lab.service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect @Component
public class KafkaListenersAspect {

    @Before("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void notifyListenerTriggered(JoinPoint jp) {
        log.info("Some kafka consumer has been triggered | Payload = {}", jp.getArgs()[0]);
    }

}
