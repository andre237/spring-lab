package com.training.spring.lab.service;

import com.training.spring.lab.common.dto.OrderExpensesDTO;
import com.training.spring.lab.common.entity.OrderItemModel;
import com.training.spring.lab.common.pojo.OrderItemPojo;
import com.training.spring.lab.data.OrderItemRepository;
import com.training.spring.lab.service.integration.OrderExpensesClient;
import com.training.spring.lab.utils.tools.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderItemsService {

    private final OrderExpensesClient orderExpensesClient;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemsService(OrderExpensesClient orderExpensesClient,
                             OrderItemRepository orderItemRepository) {
        this.orderExpensesClient = orderExpensesClient;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void processOrderBuffer(List<OrderItemPojo> orderBuffer) {
        log.info("Starting new order item thread at {}", new Date());

        try {
            var orderIds = orderBuffer.stream()
                    .map(OrderItemPojo::getRequesterId)
                    .collect(Collectors.joining(","));

            var expensesByOrder = orderExpensesClient.getMultipleExpenses(orderIds).stream()
                    .collect(Collectors.toMap(OrderExpensesDTO::getRequesterId, Function.identity()));

            var orderModelList = orderBuffer.stream()
                    .filter(orderPojo -> expensesByOrder.containsKey(orderPojo.getRequesterId()))
                    .map(orderPojo -> {
                        var expenses = expensesByOrder.get(orderPojo.getRequesterId());
                        return ApplicationUtils.copyProperties(orderPojo, new OrderItemModel())
                                .withMoneyValue(expenses.getMoneyValue())
                                .withTimeValue(expenses.getTimeValue());
                    }).collect(Collectors.toList());

            orderItemRepository.saveAll(orderModelList);
        } catch (Exception ex) {
            log.error("Failed to process order buffer | Cause = {}", ex.getMessage());
        }
    }

}
