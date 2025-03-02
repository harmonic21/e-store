package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.simple.electronic.store.dto.BasketDto;
import ru.simple.electronic.store.entity.BasketEntity;
import ru.simple.electronic.store.entity.ProductOrderEntity;
import ru.simple.electronic.store.mapper.BasketMapper;
import ru.simple.electronic.store.repository.BasketRepository;
import ru.simple.electronic.store.repository.ProductOrderRepository;
import ru.simple.electronic.store.repository.ProductRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;

    private final BasketMapper basketMapper;

    public List<BasketDto> findAllBasketForOrder(UUID orderId) {
        return Optional.ofNullable(orderId)
                .map(basketRepository::findAllByOrderId)
                .stream()
                .flatMap(Collection::stream)
                .map(basketMapper::mapToDto)
                .toList();
    }

    public BasketDto findBasketForProduct(UUID productId, UUID orderId) {
        return basketMapper.mapToDto(basketRepository.findByProductIdAndOrderId(productId, orderId));
    }

    @Transactional
    public void addProduct(UUID productId) {
        productOrderRepository.findProductOrderByStatus("NEW")
                .ifPresentOrElse(
                        order -> addProductToBasketForCurrentOrder(productId, order),
                        () -> createNewOrderAndAddProduct(productId)
                );
    }

    private void addProductToBasketForCurrentOrder(UUID productId, ProductOrderEntity order) {
        var basket = basketRepository.findByProductId(productId);
        if (Objects.nonNull(basket)) {
            long productCount = basket.getProductCount() + 1;
            basket.setProductCount(productCount);
        } else {
            basket = new BasketEntity();
            basket.setProduct(productRepository.getReferenceById(productId));
            basket.setOrder(order);
            basket.setProductCount(1L);
        }
        basketRepository.save(basket);
    }

    private void createNewOrderAndAddProduct(UUID productId) {
        ProductOrderEntity order = new ProductOrderEntity();
        order.setStatus("NEW");
        productOrderRepository.save(order);

        addProductToBasketForCurrentOrder(productId, order);
    }

    @Transactional
    public void deleteProduct(UUID basketId) {
        basketRepository.findById(basketId).ifPresent(basket -> {
            basket.setProductCount(basket.getProductCount() - 1);
            basketRepository.save(basket);
        });
    }

    @Transactional
    public void clearBasket(UUID basketId) {
        basketRepository.findById(basketId).ifPresent(basket -> {
            basket.setProductCount(0L);
            basketRepository.save(basket);
        });
    }
}
