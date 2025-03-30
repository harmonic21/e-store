package ru.simple.store.payment.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.simple.store.payment.service.PaymentService;
import ru.simple.store.payment.service.api.PaymentApi;
import ru.simple.store.payment.service.model.PaymentPostRequest;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {

    private static final ResponseEntity<Void> OK_RESPONSE = ResponseEntity.ok().build();
    private static final ResponseEntity<Void> BAD_REQUEST = ResponseEntity.badRequest().build();

    private final PaymentService paymentService;

    @Override
    @ResponseBody
    public Mono<ResponseEntity<Void>> paymentPost(Mono<PaymentPostRequest> paymentPostRequest, ServerWebExchange exchange) {
        return paymentPostRequest
                .map(PaymentPostRequest::getOrderAmount)
                .map(paymentService::placeAnOrder)
                .filter(BooleanUtils::isTrue)
                .map(ignore -> OK_RESPONSE)
                .defaultIfEmpty(BAD_REQUEST);
    }
}
