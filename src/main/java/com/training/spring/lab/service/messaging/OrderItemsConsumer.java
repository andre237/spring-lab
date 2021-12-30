package com.training.spring.lab.service.messaging;

import com.training.spring.lab.common.pojo.OrderItemPojo;
import com.training.spring.lab.common.schemas.OrderItem;
import com.training.spring.lab.service.OrderItemsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j @Service
public class OrderItemsConsumer {

    private static final int ORDER_BUFFER_MAX_SIZE = 10;
    private static final long IDLE_BUFFER_MAX_TIME_MS = 60 * 1000;

    private final OrderItemsService orderItemsService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(ORDER_BUFFER_MAX_SIZE / 2);
    private final ScheduledExecutorService bufferPurgeExecutor = Executors.newSingleThreadScheduledExecutor();

    private final List<OrderItemPojo> orderItemBuffer = new CopyOnWriteArrayList<>();
    private final AtomicLong lastBufferWrite = new AtomicLong(0L);

    @Autowired
    public OrderItemsConsumer(OrderItemsService orderItemsService) {
        this.orderItemsService = orderItemsService;
    }

    @PostConstruct
    public void startBufferPurgeRoutine() {
        bufferPurgeExecutor.scheduleAtFixedRate(() -> {
            if (lastBufferWrite.get() != 0L) {
                long timeSinceLastRecord = System.currentTimeMillis() - lastBufferWrite.get();
                if (timeSinceLastRecord >= IDLE_BUFFER_MAX_TIME_MS && !orderItemBuffer.isEmpty()) {
                    log.info("Releasing buffer after exceeding consumer idle time");
                    this.dispatchOrderBuffer();
                }
            }
        }, 0, 15, TimeUnit.SECONDS);
    }

    @KafkaListener(
            topics = "${app.orders.topic.url}",
            groupId = "${app.orders.topic.group-id}",
            concurrency = "1"
    )
    public void consume(ConsumerRecord<String, OrderItem> order) {
        log.info("Consuming order {}", order.key());

        if (order.value() == null) {
            log.error("Null order, skipping");
            return;
        }

        orderItemBuffer.add(buildOrderPojo(order.value()));
        lastBufferWrite.set(System.currentTimeMillis());
        if (orderItemBuffer.size() == ORDER_BUFFER_MAX_SIZE) {
            this.dispatchOrderBuffer();
        }
    }

    private void dispatchOrderBuffer() {
        final var copyBuffer = new ArrayList<>(orderItemBuffer);
        executorService.submit(() -> orderItemsService.processOrderBuffer(copyBuffer));
        orderItemBuffer.clear();
    }

    private OrderItemPojo buildOrderPojo(OrderItem kafkaRecord) {
        return OrderItemPojo.builder()
                .requesterId(kafkaRecord.getRequesterId())
                .description(kafkaRecord.getDescription())
                .deadline(kafkaRecord.getDeadline())
                .build();
    }

}
