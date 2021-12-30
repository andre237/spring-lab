package com.training.spring.lab.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Data @With
@AllArgsConstructor
@Entity @Table(name = "ORDER_ITEM")
public class OrderItemModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REQUESTER_ID")
    private String requesterId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DEADLINE")
    @Temporal(TemporalType.DATE)
    private Date deadline;

    @Column(name = "MONEY_VALUE")
    private BigDecimal moneyValue;

    @Column(name = "TIME_VALUE")
    private BigDecimal timeValue;

    public OrderItemModel() {}
}
