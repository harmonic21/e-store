package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.service.TokenService;
import ru.simple.store.payment.service.ApiClient;
import ru.simple.store.payment.service.model.BalanceGet200Response;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final ApiClient apiClient;
    private final TokenService tokenService;

    @GetMapping("/balance")
    public Mono<Double> getCurrentBalance() {
        return tokenService.getAccessToken()
                .flatMap(accessToken -> apiClient.getWebClient().get()
                        .uri(apiClient.getBasePath() + "/balance")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(BalanceGet200Response.class)
                )
                .map(BalanceGet200Response::getAmount)
                .onErrorReturn(0.0);
    }
}
