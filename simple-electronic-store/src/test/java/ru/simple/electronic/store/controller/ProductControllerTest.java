package ru.simple.electronic.store.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.dto.BasketDto;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.service.CsvReaderService;
import ru.simple.electronic.store.service.ProductOrderService;
import ru.simple.electronic.store.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ProductController productController;
    @Autowired
    private CsvReaderService csvReaderService;
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private ProductOrderService productOrderService;

    @Test
    void getIndexPageTest() throws Exception {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader()
                .location("/products")
                .expectBody()
                .isEmpty();
    }

    @Test
    void getProductsTest() throws Exception {
        when(productService.findAll(any())).thenReturn(Mono.just(List.of()));
        when(productOrderService.findCurrentOrderOrCreateNew()).thenReturn(Mono.just(new ProductOrderDto()));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/products").queryParam("key-work", "key").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result -> {
                    String renderedHtml = result.getResponseBody();
                    assertTrue(renderedHtml.contains("<input type=\"number\" min=\"0\" required=\"true\" value=\"0\" id=\"pageNum\" name=\"pageNum\"/>"));
                    assertTrue(renderedHtml.contains("<input type=\"number\" min=\"0\" required=\"true\" value=\"10\" id=\"pageSize\" name=\"pageSize\"/>"));
                    assertFalse(renderedHtml.contains("<div>"));
                });
    }

    @Test
    void getProductByIdTest() throws Exception {
        var product = new ProductDto()
                .setId(UUID.fromString("019fed39-d868-4e59-9093-1b2f83db2154"))
                .setTitle("")
                .setPrice(BigDecimal.ONE)
                .setDescription("")
                .setImage("");
        when(productService.findById(any())).thenReturn(Mono.just(product));

        var productOrderDto = new ProductOrderDto()
                .setOrderSum(BigDecimal.ZERO)
                .setStatus("")
                .setOrderItems(List.of(new BasketDto().setProductInfo(new ProductDto().setId(UUID.fromString("019fed39-d868-4e59-9093-1b2f83db2154")))));
        when(productOrderService.findCurrentOrderOrCreateNew()).thenReturn(Mono.just(productOrderDto));

        webTestClient.get()
                .uri("/product/019fed39-d868-4e59-9093-1b2f83db2154")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result ->{
                    String renderedHtml = result.getResponseBody();
                    assertTrue(renderedHtml.contains("<p id=\"019fed39-d868-4e59-9093-1b2f83db2154_count\">0</p>"));
                });
    }

    @Test
    void uploadProduct() throws Exception {
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder
                .part("file", new ClassPathResource("test.csv"))
                .contentType(MediaType.MULTIPART_FORM_DATA);

        doReturn(Mono.empty()).when(productService).saveNewProduct(any());

        webTestClient.post()
                .uri("/product/upload")
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader()
                .location("/")
                .expectBody()
                .isEmpty();
    }
}