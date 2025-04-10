package ru.simple.store.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.simple.store.authorization.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);
}
