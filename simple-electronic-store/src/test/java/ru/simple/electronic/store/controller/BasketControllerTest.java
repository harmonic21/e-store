package ru.simple.electronic.store.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.service.BasketService;

import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureWebTestClient
class BasketControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BasketController basketController;
    @MockitoBean
    private BasketService basketService;

    @Test
    void addProductToBasketTest() throws Exception {
        UUID productId = UUID.fromString("4409ff9f-153f-43fe-b373-11e059d78b3c");
        doReturn(Mono.just(productId)).when(basketService).addProduct(eq(productId));
        webTestClient.put()
                .uri("/basket/add/4409ff9f-153f-43fe-b373-11e059d78b3c")
                .exchange()
                .expectStatus().isOk();
        verify(basketService, times(1)).addProduct(eq(UUID.fromString("4409ff9f-153f-43fe-b373-11e059d78b3c")));
    }

    @Test
    void deleteProductFromBasketTest() throws Exception {
        var uuid = UUID.fromString("4409ff9f-153f-43fe-b373-11e059d78b3c");
        doReturn(Mono.just(uuid)).when(basketService).deleteProduct(eq(uuid));
        webTestClient.put()
                .uri("/basket/delete/4409ff9f-153f-43fe-b373-11e059d78b3c")
                .exchange()
                .expectStatus().isOk();
        verify(basketService, times(1)).deleteProduct(eq(uuid));
    }

    @Test
    void clearBasketTest() throws Exception {
        var uuid = UUID.fromString("4409ff9f-153f-43fe-b373-11e059d78b3c");
        doReturn(Mono.empty()).when(basketService).clearBasket(eq(uuid));
        webTestClient.put()
                .uri("/basket/clear/4409ff9f-153f-43fe-b373-11e059d78b3c")
                .exchange()
                .expectStatus().isOk();
        verify(basketService, times(1)).clearBasket(eq(uuid));
    }
}