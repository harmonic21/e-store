package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.dto.FiltrationDto;
import ru.simple.electronic.store.entity.ProductEntity;
import ru.simple.electronic.store.repository.ProductRepository;

import java.time.Duration;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductRedisCacheService {

    private static final Comparator<ProductEntity> NO_SORT = (first, second) -> 0;

    private final ReactiveRedisTemplate<String, ProductEntity> redisTemplate;
    private final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory;
    private final ProductRepository productRepository;

    @Value("${redis.minute-expire}")
    private int minuteExpire;

    @Scheduled(fixedDelayString = "${redis.minute-expire}", timeUnit = TimeUnit.MINUTES)
    public void refresh() {
        refreshNow().subscribe();
    }

    public Flux<ProductEntity> refreshNow() {
        return reactiveRedisConnectionFactory.getReactiveConnection().serverCommands().flushAll()
                .thenMany(productRepository.findAll().flatMap(this::putToCache));
    }

    public Flux<ProductEntity> getAll(FiltrationDto filtrationDto) {
        return redisTemplate.keys("*")
                .flatMap(id -> redisTemplate.opsForValue().get(id))
                .sort(createComparator(filtrationDto));
    }

    public Mono<ProductEntity> getById(UUID uuid) {
        return redisTemplate.opsForValue().get(uuid.toString())
                .switchIfEmpty(productRepository.findById(uuid).flatMap(this::putToCache));
    }

    private Comparator<ProductEntity> createComparator(FiltrationDto filtrationDto) {
        Comparator<ProductEntity> comparator = NO_SORT;
        if (BooleanUtils.isTrue(filtrationDto.isPriceSortAsc())) {
            comparator = comparator.thenComparing(ProductEntity::getPrice);
        }
        if (BooleanUtils.isTrue(filtrationDto.isAbcSortAsc())) {
            comparator = comparator.thenComparing(ProductEntity::getTitle);
        }
        if (BooleanUtils.isTrue(filtrationDto.isPriceSortDesc())) {
            comparator = comparator.thenComparing(ProductEntity::getPrice, Comparator.reverseOrder());
        }
        if (BooleanUtils.isTrue(filtrationDto.isAbcSortDesc())) {
            comparator = comparator.thenComparing(ProductEntity::getTitle, Comparator.reverseOrder());
        }
        return comparator;
    }

    private Mono<ProductEntity> putToCache(ProductEntity product) {
        return redisTemplate.opsForValue()
                .set(product.getId().toString(), product, Duration.ofMinutes(minuteExpire))
                .then(Mono.just(product));
    }
}
