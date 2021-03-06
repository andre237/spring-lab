package com.training.spring.lab.common.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data @Builder
public class OrderExpensesDTO {

    private String requesterId;
    private BigDecimal moneyValue;
    private BigDecimal timeValue;

}
