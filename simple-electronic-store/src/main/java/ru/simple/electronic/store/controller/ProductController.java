package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.simple.electronic.store.service.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public String getIndexPage() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        var allProducts = productService.findAll();
        model.addAttribute("products", allProducts);
        return "products";
    }
}
