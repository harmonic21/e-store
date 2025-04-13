package ru.simple.electronic.store.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.service.TokenService;
import ru.simple.store.payment.service.ApiClient;
import ru.simple.store.payment.service.api.BalanceApi;
import ru.simple.store.payment.service.model.BalanceGet200Response;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
class BalanceControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private BalanceController balanceController;
    @MockitoBean
    private ApiClient apiClient;
    @MockitoBean
    private ExchangeFunction exchangeFunction;
    @MockitoBean
    public ReactiveOAuth2AuthorizedClientManager manager;
    @MockitoBean
    public TokenService tokenService;
    private WebClient webClient;

    @BeforeEach
    void init() {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();
        this.webClient = webClient;
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnBalance() {
        when(tokenService.getAccessToken()).thenReturn(Mono.just("token"));
        when(apiClient.getWebClient()).thenReturn(webClient);
        when(apiClient.getBasePath()).thenReturn("http://localhost:8080");
        when(exchangeFunction.exchange(any(ClientRequest.class)))
                .thenReturn(Mono.just(ClientResponse.create(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).body("{\"amount\":100.0}").build()));

        webTestClient.get()
                .uri("/balance")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .isEqualTo("100.0");
    }
}