package com.training.spring.lab.service.messaging;

import com.training.spring.lab.common.dto.OrderExpensesDTO;
import com.training.spring.lab.common.schemas.OrderItem;
import com.training.spring.lab.service.integration.OrderExpensesClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j @Service
public class OrderItemsConsumer {

    private final OrderExpensesClient orderExpensesClient;

    @Autowired
    public OrderItemsConsumer(OrderExpensesClient orderExpensesClient) {
        this.orderExpensesClient = orderExpensesClient;
    }

    @KafkaListener(
            topics = "${app.orders.topic.url}",
            groupId = "${app.orders.topic.group-id}"
    )
    public void consume(ConsumerRecord<String, OrderItem> order) {
        log.info("Consuming order {}", order.key());

        if (order.value() == null) {
            log.error("Null order, skipping");
            return;
        }

        OrderExpensesDTO expenses = orderExpensesClient.getExpenses(order.value().getRequesterId());
        log.info("Order data = {} | Expenses = {}", order.value(), expenses);
    }

}
