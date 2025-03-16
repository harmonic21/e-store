package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.service.CsvReaderService;
import ru.simple.electronic.store.service.ProductOrderService;
import ru.simple.electronic.store.service.ProductService;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.Collections;
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
    public Mono<Rendering> getProducts(Model model,
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
        var currentBasketByProductId = currentOrder.map(ProductOrderDto::getOrderItems)
                .map(baskets -> baskets.stream().collect(Collectors.toMap(basket -> basket.getProductInfo().getId(), Function.identity())));

        return Mono.defer(() ->
            Mono.just(Rendering.view("products")
                    .modelAttribute("products", allProducts)
                    .modelAttribute("order", currentOrder)
                    .modelAttribute("basket", currentBasketByProductId)
                    .modelAttribute("currentPageNum", pageNum)
                    .modelAttribute("currentPageSize", pageSize)
                    .modelAttribute("currentKeyWord", keyWord)
            )
        ).map(Rendering.Builder::build);
    }

    @GetMapping("/product/{id}")
    public Mono<Rendering> getProductById(@PathVariable("id") UUID productId,
                                          Model model) {
        var product = productService.findById(productId);
        var currentOrder = productOrderService.findCurrentOrderOrCreateNew();
        var basketForProduct = currentOrder.map(ProductOrderDto::getOrderItems)
                .flatMapMany(Flux::fromIterable)
                .filter(basketDto -> Objects.equals(basketDto.getProductInfo().getId(), productId))
                .next();

        model.addAttribute("product", product);
        model.addAttribute("order", currentOrder);
        model.addAttribute("basket", basketForProduct);
        return Mono.just(
                Rendering.view("product-detailed")
                        .modelAttribute("product", product)
                        .modelAttribute("order", currentOrder)
                        .modelAttribute("basket", basketForProduct)
                        .build()
        );

    }

    @GetMapping("/product/upload")
    public String getUploadPage() {
        return "upload-product";
    }

    @PostMapping("/product/upload")
    public String uploadProduct(@RequestPart("file") FilePart csvFile) throws IOException {
        var csvContent = csvFile.content().map(DataBuffer::asInputStream)
                .collectList()
                .map(Collections::enumeration)
                .map(SequenceInputStream::new)
                .flatMapMany(csvReaderService::readCsv);
        productService.saveNewProduct(csvContent).subscribe();
        return "redirect:/";
    }
}
