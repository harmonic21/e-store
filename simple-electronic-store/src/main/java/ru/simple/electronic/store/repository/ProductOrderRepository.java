package ru.simple.electronic.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.simple.electronic.store.entity.ProductOrderEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, UUID> {

    Optional<ProductOrderEntity> findProductOrderByStatus(String status);
    List<ProductOrderEntity> findAllProductOrderByStatus(String status);
}
