package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.service.ProductOrderService;
import ru.simple.store.payment.service.api.PaymentApi;
import ru.simple.store.payment.service.model.PaymentPostRequest;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final ProductOrderService productOrderService;
    private final PaymentApi paymentApi;

    @GetMapping("/current/info")
    public Mono<Rendering> getCurrentOrderInfo(Model model,
                                               @AuthenticationPrincipal Principal principal) {
        return Mono.just(
                Rendering.view("order-basket-info")
                        .modelAttribute("order", productOrderService.findCurrentOrderOrCreateNew(principal.getName()))
                        .build()
        );
    }

    @GetMapping("/all")
    public Mono<Rendering> getAllOrdersInStatusDone(Model model) {
        var ordersInStatusDone = productOrderService.findAllOrdersInStatusDone();
        var totalOrdersSum = ordersInStatusDone
                .map(ProductOrderDto::getOrderSum)
                .reduce(BigDecimal::add)
                .defaultIfEmpty(BigDecimal.ZERO);
        return Mono.just(
                Rendering.view("orders-info")
                        .modelAttribute("orders", ordersInStatusDone)
                        .modelAttribute("totalSum", totalOrdersSum)
                        .build()
        );
    }

    @PutMapping("/place")
    @ResponseBody
    public Mono<UUID> placeAnOrder(@RequestBody PaymentPostRequest paymentPostRequest,
                                   @AuthenticationPrincipal Principal principal) {
        return paymentApi.paymentPostWithHttpInfo(paymentPostRequest)
                .then(productOrderService.placeAnOrder(principal.getName()));
    }

    @GetMapping("/detail/{id}")
    public Mono<Rendering> getDetailInfo(Model model,
                                         @PathVariable("id") UUID id) {
        model.addAttribute("order", productOrderService.getDetailInfoById(id));
        return Mono.just(
                Rendering.view("order-detail-info")
                        .modelAttribute("order", productOrderService.getDetailInfoById(id))
                        .build()
        );
    }
}
