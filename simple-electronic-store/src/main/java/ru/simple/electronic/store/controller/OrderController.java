package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.simple.electronic.store.service.ProductOrderService;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final ProductOrderService productOrderService;

    @GetMapping("/current/info")
    public String getCurrentOrderInfo(Model model) {
        var order = productOrderService.findCurrentOrderOrCreateNew();
        model.addAttribute("order", order);
        model.addAttribute("totalSum", order.getCurrentSum());
        return "order-basket-info";
    }

    private UUID fromNullableString(String id) {
        return Optional.ofNullable(id).map(UUID::fromString).orElse(null);
    }
}
