package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.simple.electronic.store.service.BasketService;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @ResponseBody
    @PutMapping("/add/{productId}")
    public void addProductToBasket(@PathVariable("productId") String productId) {
        basketService.addProduct(UUID.fromString(productId));
    }

    @ResponseBody
    @PutMapping("/delete/{basketId}")
    public void deleteProductFromBasket(@PathVariable("basketId") String basketId) {
        basketService.deleteProduct(fromNullableString(basketId));
    }

    @ResponseBody
    @PutMapping("/clear/{basketId}")
    public void clearBasket(@PathVariable("basketId") String basketId) {
        basketService.clearBasket(UUID.fromString(basketId));
    }

    private UUID fromNullableString(String id) {
        return Optional.ofNullable(id).map(UUID::fromString).orElse(null);
    }
}
