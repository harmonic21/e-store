package ru.simple.electronic.store.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.repository.ProductOrderRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductOrderServiceTest {

    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private ProductOrderRepository productOrderRepository;

    private UUID orderId;

    @BeforeAll
    public void before() {
        productOrderRepository.deleteAll().block();
    }

    @Test
    @Order(1)
    void findCurrentOrderOrCreateNew() {
//        assertTrue(CollectionUtils.isEmpty(productOrderRepository.findAll().collectList().block()));
//
//        productOrderService.findCurrentOrderOrCreateNew().block();
//        List<ProductOrderEntity> after = productOrderRepository.findAll().collectList().block();
//        assertTrue(CollectionUtils.isNotEmpty(after));
//        assertEquals(1, after.size());
//
//        ProductOrderEntity createdOrder = after.get(0);
//        assertEquals("NEW", createdOrder.getStatus());
//        assertNotNull(createdOrder.getOrderSum());
//
//        orderId = createdOrder.getId();
    }

    @Test
    @Order(2)
    void placeAnOrder() {
//        productOrderRepository.findAll().collectList().block()
//                .forEach(order -> assertNotEquals("DONE", order.getStatus()));
//        Mono.just("ignore")
//                .flatMap(ignore -> productOrderService.placeAnOrder())
//                .doOnNext(ignore -> {
//                    System.out.println(ignore);
//                    productOrderRepository.findAll()
//                            .doOnNext(order -> assertEquals("DONE", order.getStatus()));
//                })
//                .block();
    }

    @Test
    @Order(3)
    void findAllOrdersInStatusDone() {
        List<ProductOrderDto> doneOrder = productOrderService.findAllOrdersInStatusDone().collectList().block();

        assertEquals(1, doneOrder.size());
        assertEquals(orderId, doneOrder.get(0).getId());
    }

    @Test
    @Order(4)
    void getDetailInfoById() {
        ProductOrderDto order = productOrderService.getDetailInfoById(orderId).block();
        assertEquals("DONE", order.getStatus());
        assertNotNull(order.getOrderSum());
    }
}