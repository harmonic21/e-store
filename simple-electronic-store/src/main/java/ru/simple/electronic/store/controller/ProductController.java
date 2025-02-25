package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.service.ProductService;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public String getIndexPage() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String getProducts(Model model,
                              @RequestParam(required = false, defaultValue = "10", name = "page-size") Integer pageSize,
                              @RequestParam(required = false, defaultValue = "0", name = "page-num") Integer pageNum,
                              @RequestParam(required = false, name = "key-word") String keyWord,
                              @RequestParam(required = false, name = "price-sort-asc") Boolean priceSortAsc,
                              @RequestParam(required = false, name = "abc-sort-asc") Boolean abcSortAsc,
                              @RequestParam(required = false, name = "price-sort-desc") Boolean priceSortDesc,
                              @RequestParam(required = false, name = "abc-sort-desc") Boolean abcSortDesc) {
        var allProducts = productService.findAll(
                pageNum, pageSize, keyWord, priceSortAsc, abcSortAsc, priceSortDesc, abcSortDesc
        );
        model.addAttribute("products", allProducts);
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("currentPageSize", pageSize);
        model.addAttribute("currentKeyWord", keyWord);
        return "products";
    }


    @PostMapping("product/add")
    public String addProduct(@RequestParam(name = "title") String title,
                             @RequestParam(name = "price") BigDecimal price,
                             @RequestParam(name = "description") String description,
                             @RequestParam(name = "image") MultipartFile image) {
        productService.addProduct(new ProductDto()
                        .setTitle(title)
                        .setPrice(price)
                        .setDescription(description),
                image
                );
        return "redirect:/";
    }
}
