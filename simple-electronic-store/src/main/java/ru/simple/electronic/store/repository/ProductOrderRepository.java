package ru.simple.electronic.store.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.entity.ProductOrderEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface ProductOrderRepository extends R2dbcRepository<ProductOrderEntity, UUID> {

    Mono<ProductOrderEntity> findProductOrderByStatusAndUsername(String status, String username);
    Flux<ProductOrderEntity> findAllProductOrderByStatusAndUsername(String status, String username);

    @Query(value = """
            SELECT SUM(b.product_count * p.price) FROM basket b
            LEFT JOIN product p ON p.id = b.id_product
            WHERE b.order_id = :order_id""")
    Mono<BigDecimal> calculateTotalOrderSum(@Param("order_id") UUID orderId);
}
