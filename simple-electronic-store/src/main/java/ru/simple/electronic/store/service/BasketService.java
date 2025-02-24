package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.simple.electronic.store.entity.BasketEntity;
import ru.simple.electronic.store.repository.BasketRepository;
import ru.simple.electronic.store.repository.ProductRepository;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addProduct(UUID productId) {
        var basket = basketRepository.findByProductId(productId);
        if (Objects.nonNull(basket)) {
            long productCount = basket.getProductCount() + 1;
            basket.setProductCount(productCount);
        } else {
            basket = new BasketEntity();
            basket.setProduct(productRepository.getReferenceById(productId));
            basket.setProductCount(1L);
        }
        basketRepository.save(basket);
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        var basket = basketRepository.findByProductId(productId);
        long productCount = basket.getProductCount() - 1;
        basket.setProductCount(productCount);
        basketRepository.save(basket);
    }
}
