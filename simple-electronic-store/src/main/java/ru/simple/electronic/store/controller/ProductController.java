package ru.simple.electronic.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.dto.FiltrationDto;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.service.CsvReaderService;
import ru.simple.electronic.store.service.ProductOrderService;
import ru.simple.electronic.store.service.ProductService;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

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
                                       @ModelAttribute(value = "filtration") FiltrationDto filtrationDto,
                                       Authentication authentication) {
        Flux<String> authorities = authentication != null ?
                Flux.fromIterable(authentication.getAuthorities()).map(GrantedAuthority::getAuthority) :
                Flux.empty();

        var allProducts = productService.findAll(filtrationDto);
        Mono<ProductOrderDto> currentOrder = authentication != null && authentication.isAuthenticated() ?
                productOrderService.findCurrentOrderOrCreateNew(authentication.getName()) :
                Mono.empty();

        return Mono.defer(() ->
                Mono.just(Rendering.view("products")
                        .modelAttribute("products", allProducts)
                        .modelAttribute("order", currentOrder)
                        .modelAttribute("filtration", filtrationDto)
                        .modelAttribute("authorities", authorities)
                )
        ).map(Rendering.Builder::build);
    }

    @GetMapping("/product/{id}")
    public Mono<Rendering> getProductById(@PathVariable("id") UUID productId,
                                          Authentication authentication,
                                          Model model) {
        var product = productService.findById(productId);
        Mono<ProductOrderDto> currentOrder = authentication != null && authentication.isAuthenticated() ?
                productOrderService.findCurrentOrderOrCreateNew(authentication.getName()) :
                Mono.empty();
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
    public Mono<Rendering> uploadProduct(@RequestPart("file") FilePart csvFile) throws IOException {
        var csvContent = csvFile.content().map(DataBuffer::asInputStream)
                .collectList()
                .map(Collections::enumeration)
                .map(SequenceInputStream::new)
                .flatMapMany(csvReaderService::readCsv);
        return productService.saveNewProduct(csvContent).then(Mono.just(Rendering.redirectTo("/").build()));
    }
}
