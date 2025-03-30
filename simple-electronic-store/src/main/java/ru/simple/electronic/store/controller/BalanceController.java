package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.simple.store.payment.service.api.BalanceApi;
import ru.simple.store.payment.service.model.BalanceGet200Response;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceApi balanceApi;

    @GetMapping("/balance")
    public Mono<Double> getCurrentBalance() {
        return balanceApi.balanceGet()
                .map(BalanceGet200Response::getAmount)
                .onErrorReturn(0.0);

    }
}
