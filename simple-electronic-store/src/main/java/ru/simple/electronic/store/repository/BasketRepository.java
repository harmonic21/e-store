package ru.simple.electronic.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.simple.electronic.store.entity.BasketEntity;

import java.util.List;
import java.util.UUID;

public interface BasketRepository extends JpaRepository<BasketEntity, UUID> {

    BasketEntity findByProductIdAndOrderId(UUID productId, UUID orderId);
    List<BasketEntity> findAllByOrderId(UUID orderId);
}
