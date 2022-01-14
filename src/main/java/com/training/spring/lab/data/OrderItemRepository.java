package com.training.spring.lab.data;

import com.training.spring.lab.common.entity.OrderItemModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "orders", path = "orders")
public interface OrderItemRepository extends JpaRepository<OrderItemModel, Long> {

    @Override
    @Cacheable("entities")
    Optional<OrderItemModel> findById(Long id);

    @Override
    @Cacheable("pagedEntities")
    Page<OrderItemModel> findAll(Pageable pageable);

}
