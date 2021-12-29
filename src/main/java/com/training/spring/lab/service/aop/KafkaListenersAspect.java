package com.training.spring.lab.service.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect @Component
public class KafkaListenersAspect {

    @Around("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public Object notifyListenerTriggered(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.nanoTime();
        Object object = pjp.proceed();
        double execTime =  (System.nanoTime() - startTime) / 1e6;

        ConsumerRecord<String, ?> payload = (ConsumerRecord<String, ?>) pjp.getArgs()[0];
        log.info("Kafka consumer triggered. Schema = {} | Execution = {}ms", payload.value().getClass().getSimpleName(), execTime);

        return object;
    }

}
