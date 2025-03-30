package ru.simple.store.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.simple.store.payment.service.BalanceService;
import ru.simple.store.payment.service.api.BalanceApi;
import ru.simple.store.payment.service.model.BalanceGet200Response;

@RestController
@RequiredArgsConstructor
public class BalanceController implements BalanceApi {

    private final BalanceService balanceService;

    @Override
    public Mono<ResponseEntity<BalanceGet200Response>> balanceGet(ServerWebExchange exchange) {
        return Mono.just(new BalanceGet200Response())
                .doOnNext(rs -> rs.setAmount(balanceService.getCurrentBalance()))
                .map(ResponseEntity::ok);
    }
}
