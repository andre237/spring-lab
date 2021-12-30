package com.training.spring.lab.common.pojo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data @Builder
public class OrderItemPojo {

    private String requesterId;
    private String description;
    private Date deadline;
    private BigDecimal moneyValue;
    private BigDecimal timeValue;

}
