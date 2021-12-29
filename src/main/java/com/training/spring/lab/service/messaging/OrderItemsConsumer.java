package com.training.spring.lab.service.messaging;

import com.training.spring.lab.common.schemas.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j @Service
public class OrderItemsConsumer {

    @KafkaListener(
            topics = "${app.orders.topic.url}",
            groupId = "${app.orders.topic.group-id}"
    )
    public void consume(ConsumerRecord<String, OrderItem> order) {
        log.info("Consuming order {} - {}", order.key(), order.value());
    }

}
