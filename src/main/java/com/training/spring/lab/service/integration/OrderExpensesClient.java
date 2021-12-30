package com.training.spring.lab.service.integration;

import com.training.spring.lab.common.dto.OrderExpensesDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "order-expenses", url = "${app.orders.external.expenses.url}")
public interface OrderExpensesClient {

    @GetMapping(value = "orders/expenses/{orderId}")
    OrderExpensesDTO getExpenses(@PathVariable String orderId);

    @GetMapping("orders/expenses/multiple")
    List<OrderExpensesDTO> getMultipleExpenses(@RequestParam String orderIds);

}
