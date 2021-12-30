package com.training.spring.lab.data;

import com.training.spring.lab.common.entity.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemModel, Long> {
}
