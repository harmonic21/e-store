package ru.simple.electronic.store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.simple.electronic.store.entity.ProductEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM product WHERE title LIKE :keyWord OR description LIKE :keyWord")
    List<ProductEntity> findAllAndFilter(@Param("keyWord") String keyWord, Pageable pageable);
}
