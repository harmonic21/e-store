package ru.simple.electronic.store.service;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Flux;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.entity.ProductEntity;
import ru.simple.electronic.store.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReactiveRedisTemplate<String, ProductEntity> redisTemplate;
    @Autowired
    private ReactiveRedisConnectionFactory reactiveRedisConnectionFactory;

    @Test
    @Order(1)
    public void shouldLoadProductToCache() {
        reactiveRedisConnectionFactory.getReactiveConnection().serverCommands().flushAll().block();

        List<String> cacheKeysBefore = redisTemplate.keys("*").collectList().block();
        assertTrue(CollectionUtils.isEmpty(cacheKeysBefore));

        ProductDto productDto = new ProductDto().setTitle("One").setPrice(BigDecimal.TEN);
        productService.saveNewProduct(Flux.fromIterable(List.of(productDto))).block();

        List<String> cacheKeysAfter = redisTemplate.keys("*").collectList().block();
        assertTrue(CollectionUtils.isNotEmpty(cacheKeysAfter));
    }

    @Test
    @Order(2)
    public void shouldLoadProductToCacheIfNotExistById() {
        reactiveRedisConnectionFactory.getReactiveConnection().serverCommands().flushAll().block();

        List<String> cacheKeysBefore = redisTemplate.keys("*").collectList().block();
        assertTrue(CollectionUtils.isEmpty(cacheKeysBefore));

        ProductEntity productEntity = productRepository.findAll().blockFirst();
        assertFalse(cacheKeysBefore.contains(productEntity.getId().toString()));

        productService.findById(productEntity.getId()).block();

        List<String> cacheKeysAfter = redisTemplate.keys("*").collectList().block();
        assertTrue(CollectionUtils.isNotEmpty(cacheKeysAfter));
        assertTrue(cacheKeysAfter.contains(productEntity.getId().toString()));
    }
}