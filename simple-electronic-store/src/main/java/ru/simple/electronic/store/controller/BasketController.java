package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.simple.electronic.store.service.BasketService;

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
    @PutMapping("/delete/{productId}")
    public void deleteProductFromBasket(@PathVariable("productId") String productId) {
        basketService.deleteProduct(UUID.fromString(productId));
    }

    @PostMapping("/clear/{productId}")
    public String clearBasket(@PathVariable("productId") String productId) {
        basketService.clearBasket(UUID.fromString(productId));
        return "redirect:/product/%s".formatted(productId);
    }
}
