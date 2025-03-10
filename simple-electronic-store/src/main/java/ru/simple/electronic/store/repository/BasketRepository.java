package ru.simple.electronic.store.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.entity.BasketEntity;

import java.util.UUID;

public interface BasketRepository extends R2dbcRepository<BasketEntity, UUID> {

    Mono<BasketEntity> findByProductIdAndOrderId(UUID productId, UUID orderId);
    Flux<BasketEntity> findAllByOrderId(UUID orderId);
}
