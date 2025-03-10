package ru.simple.electronic.store.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.simple.electronic.store.entity.ProductEntity;

import java.util.UUID;

@Repository
public interface ProductRepository extends R2dbcRepository<ProductEntity, UUID> {

    @Query(value = "SELECT * FROM product WHERE title LIKE :keyWord OR description LIKE :keyWord")
    Flux<ProductEntity> findAllAndFilter(@Param("keyWord") String keyWord, Sort sort);
}
