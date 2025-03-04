package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.service.ProductOrderService;

import java.math.BigDecimal;
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

    @GetMapping("/all")
    public String getAllOrdersInStatusDone(Model model) {
        var ordersInStatusDone = productOrderService.findAllOrdersInStatusDone();
        var totalOrdersSum = ordersInStatusDone.stream()
                .map(ProductOrderDto::getOrderSum)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        model.addAttribute("orders", ordersInStatusDone);
        model.addAttribute("totalSum", totalOrdersSum);
        return "orders-info";
    }

    @PutMapping("/place")
    @ResponseBody
    public void placeAnOrder() {
        productOrderService.placeAnOrder();
    }

    private UUID fromNullableString(String id) {
        return Optional.ofNullable(id).map(UUID::fromString).orElse(null);
    }
}
