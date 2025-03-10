//package ru.simple.electronic.store.service;
//
//import org.apache.commons.collections4.CollectionUtils;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.simple.electronic.store.entity.ProductEntity;
//import ru.simple.electronic.store.entity.ProductOrderEntity;
//import ru.simple.electronic.store.repository.BasketRepository;
//import ru.simple.electronic.store.repository.ProductOrderRepository;
//import ru.simple.electronic.store.repository.ProductRepository;
//
//import java.util.UUID;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class BasketServiceTest {
//
//    @Autowired
//    private BasketService basketService;
//    @Autowired
//    private BasketRepository basketRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private ProductOrderRepository productOrderRepository;
//    private UUID productId;
//    private UUID orderId;
//
//    @BeforeAll
//     void before() {
//        basketRepository.deleteAll();
//        productRepository.deleteAll();
//
//        var orderEntity = new ProductOrderEntity();
//        orderEntity.setStatus("NEW");
//        var order = productOrderRepository.save(orderEntity);
//        orderId = order.getId();
//        var productEntity = new ProductEntity();
//        productEntity.setTitle("Title");
//        var save = productRepository.save(productEntity);
//        productId = save.getId();
//    }
//
//    @Test
//    @Order(1)
//    void addProduct() {
//        var before = basketRepository.findAll();
//        Assertions.assertTrue(CollectionUtils.isEmpty(before));
//        basketService.addProduct(productId);
//        var after = basketRepository.findAll();
//        Assertions.assertTrue(CollectionUtils.isNotEmpty(after));
//        Assertions.assertEquals(1, after.size());
//        basketService.addProduct(productId);
//        after = basketRepository.findAll();
//        Assertions.assertEquals(1, after.size());
//    }
//
//    @Test
//    @Order(2)
//    void findAllBasketForOrder() {
//        var allBasketForOrder = basketService.findAllBasketForOrder(orderId);
//        Assertions.assertTrue(CollectionUtils.isNotEmpty(allBasketForOrder));
//        Assertions.assertEquals(1, allBasketForOrder.size());
//    }
//
//    @Test
//    @Order(3)
//    void findBasketForProduct() {
//        var basketForProduct = basketService.findBasketForProduct(productId, orderId);
//        Assertions.assertNotNull(basketForProduct);
//        Assertions.assertEquals(2, basketForProduct.getProductCount());
//
//    }
//
//    @Test
//    @Order(4)
//    void deleteProduct() {
//        var id = basketRepository.findAll().get(0).getId();
//        basketService.deleteProduct(id);
//        basketRepository.findById(id).ifPresent(
//                basketEntity -> Assertions.assertEquals(1, basketEntity.getProductCount()));
//    }
//
//    @Test
//    @Order(5)
//    void clearBasket() {
//        var before = basketRepository.findAll();
//        Assertions.assertTrue(CollectionUtils.isNotEmpty(before));
//        var id = before.get(0).getId();
//        basketService.clearBasket(id);
//        var basketEntities = basketRepository.findAll();
//        Assertions.assertTrue(CollectionUtils.isEmpty(basketEntities));
//    }
//}