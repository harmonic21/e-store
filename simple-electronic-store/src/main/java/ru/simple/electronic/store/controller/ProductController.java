package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.simple.electronic.store.service.CsvReaderService;
import ru.simple.electronic.store.service.ProductOrderService;
import ru.simple.electronic.store.service.ProductService;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductOrderService productOrderService;
    private final CsvReaderService csvReaderService;

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
        var currentOrder = productOrderService.findCurrentOrderOrCreateNew();
        var currentBasketByProductId = CollectionUtils.emptyIfNull(currentOrder.getOrderItems()).stream()
                        .collect(Collectors.toMap(basket -> basket.getProductInfo().getId(), Function.identity()));

        model.addAttribute("products", allProducts);
        model.addAttribute("order", currentOrder);
        model.addAttribute("basket", currentBasketByProductId);
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("currentPageSize", pageSize);
        model.addAttribute("currentKeyWord", keyWord);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable("id") String id,
                                 Model model) {
        var productId = UUID.fromString(id);
        var product = productService.findById(productId);
        var currentOrder = productOrderService.findCurrentOrderOrCreateNew();
        var basketForProduct = CollectionUtils.emptyIfNull(currentOrder.getOrderItems()).stream()
                .filter(basket -> Objects.equals(basket.getProductInfo().getId(), id))
                .findFirst()
                .orElse(null);

        model.addAttribute("product", product);
        model.addAttribute("order", currentOrder);
        model.addAttribute("basket", basketForProduct);
        return "product-detailed";
    }

    @GetMapping("/product/upload")
    public String getUploadPage() {
        return "upload-product";
    }

    @PostMapping("/product/upload")
    public String uploadProduct(@RequestParam("file") MultipartFile csvFile) throws IOException {
        productService.saveNewProduct(csvReaderService.readCsv(csvFile.getBytes()));
        return "redirect:/";
    }
}
