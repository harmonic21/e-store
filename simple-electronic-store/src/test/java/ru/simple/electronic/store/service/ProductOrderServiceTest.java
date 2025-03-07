package ru.simple.electronic.store.service;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.entity.ProductOrderEntity;
import ru.simple.electronic.store.repository.ProductOrderRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
        productOrderRepository.deleteAll();
    }

    @Test
    @Order(1)
    void findCurrentOrderOrCreateNew() {
        assertTrue(CollectionUtils.isEmpty(productOrderRepository.findAll()));

        productOrderService.findCurrentOrderOrCreateNew();
        List<ProductOrderEntity> after = productOrderRepository.findAll();
        assertTrue(CollectionUtils.isNotEmpty(after));
        assertEquals(1, after.size());

        ProductOrderEntity createdOrder = after.get(0);
        assertEquals("NEW", createdOrder.getStatus());
        assertNotNull(createdOrder.getOrderSum());

        orderId = createdOrder.getId();
    }

    @Test
    @Order(2)
    void placeAnOrder() {
        productOrderRepository.findAll().forEach(order -> assertNotEquals("DONE", order.getStatus()));
        productOrderService.placeAnOrder();
        productOrderRepository.findAll().forEach(order -> assertEquals("DONE", order.getStatus()));
    }

    @Test
    @Order(3)
    void findAllOrdersInStatusDone() {
        List<ProductOrderDto> doneOrder = productOrderService.findAllOrdersInStatusDone();

        assertEquals(1, doneOrder.size());
        assertEquals(orderId.toString(), doneOrder.get(0).getId());
    }

    @Test
    @Order(4)
    void getDetailInfoById() {
        ProductOrderDto order = productOrderService.getDetailInfoById(orderId);
        assertEquals("DONE", order.getStatus());
        assertNotNull(order.getOrderSum());
    }
}