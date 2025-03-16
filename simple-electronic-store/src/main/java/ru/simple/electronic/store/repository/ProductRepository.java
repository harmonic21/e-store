package ru.simple.electronic.store.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.simple.electronic.store.entity.ProductEntity;

import java.util.UUID;

@Repository
public interface ProductRepository extends R2dbcRepository<ProductEntity, UUID> {

    Flux<ProductEntity> findByTitleLikeOrDescriptionLike(String titleLike, String descLike, Sort sort);
}
