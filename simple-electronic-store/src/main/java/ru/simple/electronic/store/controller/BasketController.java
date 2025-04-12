package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.service.BasketService;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @ResponseBody
    @PutMapping("/add/{productId}")
    public Mono<UUID> addProductToBasket(@PathVariable("productId") UUID productId,
                                         @AuthenticationPrincipal Principal principal) {
        return basketService.addProduct(productId, principal.getName());
    }

    @ResponseBody
    @PutMapping("/delete/{basketId}")
    public Mono<UUID> deleteProductFromBasket(@PathVariable("basketId") UUID basketId) {
        return basketService.deleteProduct(basketId);
    }

    @ResponseBody
    @PutMapping("/clear/{basketId}")
    public Mono<Void> clearBasket(@PathVariable("basketId") UUID basketId) {
        return basketService.clearBasket(basketId);
    }
}
