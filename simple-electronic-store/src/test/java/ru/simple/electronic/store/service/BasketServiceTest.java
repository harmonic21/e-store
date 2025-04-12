package ru.simple.electronic.store.service;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.simple.electronic.store.entity.ProductEntity;
import ru.simple.electronic.store.entity.ProductOrderEntity;
import ru.simple.electronic.store.repository.BasketRepository;
import ru.simple.electronic.store.repository.ProductOrderRepository;
import ru.simple.electronic.store.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BasketServiceTest {

    @Autowired
    private BasketService basketService;
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOrderRepository productOrderRepository;

    private final UUID productId = UUID.randomUUID();
    private final UUID orderId = UUID.randomUUID();

    @BeforeAll
     void before() {
        basketRepository.deleteAll().block();
        productRepository.deleteAll().block();

        var orderEntity = new ProductOrderEntity().withId(orderId);
        orderEntity.setStatus("NEW");
        productOrderRepository.save(orderEntity).block();
        var productEntity = new ProductEntity().withId(productId);
        productEntity.setTitle("Title");
        productEntity.setPrice(BigDecimal.TEN);
        productRepository.save(productEntity).block();
    }

    @Test
    @Order(1)
    void addProduct() {
//        var before = basketRepository.findAll().collectList().block();
//        Assertions.assertTrue(CollectionUtils.isEmpty(before));
//        basketService.addProduct(productId).block();
//        var after = basketRepository.findAll().collectList().block();
//        Assertions.assertTrue(CollectionUtils.isNotEmpty(after));
//        Assertions.assertEquals(1, after.size());
//        basketService.addProduct(productId).block();
//        after = basketRepository.findAll().collectList().block();
//        Assertions.assertEquals(1, after.size());
    }

    @Test
    @Order(2)
    void findAllBasketForOrder() {
        var allBasketForOrder = basketService.findAllBasketForOrder(orderId).collectList().block();
        Assertions.assertTrue(CollectionUtils.isNotEmpty(allBasketForOrder));
        Assertions.assertEquals(1, allBasketForOrder.size());
    }

    @Test
    @Order(3)
    void deleteProduct() {
        var id = basketRepository.findAll().blockFirst().getId();
        basketService.deleteProduct(id).block();
        basketRepository.findById(id).blockOptional().ifPresent(
                basketEntity -> Assertions.assertEquals(1, basketEntity.getProductCount()));
    }

    @Test
    @Order(4)
    void clearBasket() {
        var before = basketRepository.findAll().collectList().block();
        Assertions.assertTrue(CollectionUtils.isNotEmpty(before));
        var id = before.get(0).getId();
        basketService.clearBasket(id).block();
        var basketEntities = basketRepository.findAll().collectList().block();
        Assertions.assertTrue(CollectionUtils.isEmpty(basketEntities));
    }
}