package com.training.spring.lab.common.schemas;

import com.training.spring.lab.common.annotation.KafkaPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@KafkaPayload(identifier = "__Order_Item__")
public class OrderItem extends BaseSchema {

    private String requesterId;
    private String description;
    private Date deadline;

}
