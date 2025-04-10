package ru.simple.electronic.store.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.entity.UserEntity;

import java.util.UUID;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, UUID> {

    Mono<UserEntity> findByUsername(String username);
}
