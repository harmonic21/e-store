package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.simple.electronic.store.dto.BasketDto;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.entity.BasketEntity;
import ru.simple.electronic.store.entity.ProductOrderEntity;
import ru.simple.electronic.store.mapper.BasketMapper;
import ru.simple.electronic.store.repository.BasketRepository;
import ru.simple.electronic.store.repository.ProductOrderRepository;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductService productService;
    private final ProductOrderRepository productOrderRepository;

    private final BasketMapper basketMapper;

    public Flux<BasketDto> findAllBasketForOrder(UUID orderId) {
        return Mono.just(orderId)
                .flatMapMany(basketRepository::findAllByOrderId)
                .flatMap(this::enrichProductInfo)
                .map(basketMapper::mapToDto);
    }

    private Mono<Tuple2<BasketEntity, ProductDto>> enrichProductInfo(BasketEntity basketEntity) {
        return Mono.just(basketEntity)
                .zipWith(productService.findById(basketEntity.getProductId()));
    }

    @Transactional
    public Mono<UUID> addProduct(UUID productId) {
        return productOrderRepository.findProductOrderByStatus("NEW")
                .switchIfEmpty(createNewOrder())
                .flatMap(order -> addProductToBasketForCurrentOrder(productId, order));
    }

    private Mono<UUID> addProductToBasketForCurrentOrder(UUID productId, ProductOrderEntity order) {
        return basketRepository.findByProductIdAndOrderId(productId, order.getId())
                .defaultIfEmpty(new BasketEntity().withId(UUID.randomUUID()).withProductId(productId).withOrderId(order.getId()))
                .doOnNext(basket -> basket.setProductCount(basket.getProductCount() + 1))
                .flatMap(basketRepository::save)
                .map(BasketEntity::getId);
    }

    private Mono<ProductOrderEntity> createNewOrder() {
        ProductOrderEntity order = new ProductOrderEntity().withId(UUID.randomUUID());
        order.setStatus("NEW");
        return productOrderRepository.save(order);
    }

    @Transactional
    public Mono<UUID> deleteProduct(UUID basketId) {
        return basketRepository.findById(basketId)
                .doOnNext(basketEntity -> basketEntity.setProductCount(basketEntity.getProductCount() - 1))
                .flatMap(basketEntity -> basketEntity.getProductCount() > 0 ? basketRepository.save(basketEntity) : basketRepository.delete(basketEntity))
                .thenReturn(basketId);
    }

    @Transactional
    public Mono<Void> clearBasket(UUID basketId) {
        return basketRepository.deleteById(basketId);
    }
}
