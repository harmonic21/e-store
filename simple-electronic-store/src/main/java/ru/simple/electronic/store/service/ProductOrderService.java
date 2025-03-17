package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.entity.ProductOrderEntity;
import ru.simple.electronic.store.mapper.ProductOrderMapper;
import ru.simple.electronic.store.repository.ProductOrderRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final BasketService basketService;
    private final ProductOrderMapper productOrderMapper;

    @Transactional
    public Mono<ProductOrderDto> findCurrentOrderOrCreateNew() {
        return productOrderRepository.findProductOrderByStatus("NEW")
                .switchIfEmpty(createNewOrder())
                .map(productOrderMapper::mapToDto)
                .flatMap(this::enrichOrderRelations);
    }

    private Mono<ProductOrderDto> enrichOrderRelations(ProductOrderDto orderDto) {
        return Mono.just(orderDto.getId())
                .flatMapMany(basketService::findAllBasketForOrder)
                .collectList()
                .doOnNext(orderDto::setOrderItems)
                .thenReturn(orderDto);
    }

    public Flux<ProductOrderDto> findAllOrdersInStatusDone() {
        return productOrderRepository.findAllProductOrderByStatus("DONE")
                .map(productOrderMapper::mapToDto);
    }

    @Transactional
    public Mono<UUID> placeAnOrder() {
        return productOrderRepository.findProductOrderByStatus("NEW")
                .doOnNext(order -> order.setStatus("DONE"))
                .flatMap(this::calculateTotalSumAndMap)
                .flatMap(productOrderRepository::save)
                .map(ProductOrderEntity::getId);
    }

    private Mono<ProductOrderEntity> calculateTotalSumAndMap(ProductOrderEntity productOrder) {
        return Mono.just(productOrder.getId())
                .flatMap(productOrderRepository::calculateTotalOrderSum)
                .map(productOrder::withOrderSum)
                .defaultIfEmpty(productOrder);
    }

    public Mono<ProductOrderDto> getDetailInfoById(UUID id) {
        return productOrderRepository.findById(id)
                .map(productOrderMapper::mapToDto)
                .flatMap(this::enrichOrderRelations);
    }

    private Mono<ProductOrderEntity> createNewOrder() {
        ProductOrderEntity orderEntity = new ProductOrderEntity().withId(UUID.randomUUID());
        orderEntity.setStatus("NEW");
        return productOrderRepository.save(orderEntity);
    }
}
