package ru.simple.electronic.store.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.simple.store.payment.service.api.BalanceApi;
import ru.simple.store.payment.service.model.BalanceGet200Response;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
class BalanceControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private BalanceController balanceController;
    @MockitoBean
    private BalanceApi balanceApi;

    @Test
    public void shouldReturnBalance() {
        BalanceGet200Response balanceResponse = new BalanceGet200Response();
        balanceResponse.setAmount(100.0);
        when(balanceApi.balanceGet()).thenReturn(Mono.just(balanceResponse));

        webTestClient.get()
                .uri("/balance")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .isEqualTo("100.0");
    }

}