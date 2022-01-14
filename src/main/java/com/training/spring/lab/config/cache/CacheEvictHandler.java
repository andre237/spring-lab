package com.training.spring.lab.config.cache;

import com.training.spring.lab.common.entity.OrderItemModel;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import java.util.Optional;

@RepositoryEventHandler
public class CacheEvictHandler {

    private final CacheManager cacheManager;

    public CacheEvictHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @HandleAfterCreate
    @HandleAfterSave
    @HandleAfterDelete
    public void evictOrdersCache(OrderItemModel orderEntity) {
        Optional.ofNullable(cacheManager.getCache("entities"))
                .ifPresent(c -> c.evict(orderEntity.getRequesterId()));

        Optional.ofNullable(cacheManager.getCache("pagedEntities"))
                .ifPresent(Cache::clear);
    }
}
