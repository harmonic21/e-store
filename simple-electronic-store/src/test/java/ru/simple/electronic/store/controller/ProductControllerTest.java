package ru.simple.electronic.store.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.simple.electronic.store.dto.BasketDto;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.service.CsvReaderService;
import ru.simple.electronic.store.service.ProductOrderService;
import ru.simple.electronic.store.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ProductController productController;
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private ProductOrderService productOrderService;
    @MockitoBean
    private CsvReaderService csvReaderService;

    @Test
    void getIndexPageTest() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products"));
    }

    @Test
    void getProductsTest() throws Exception {
        when(productService.findAll(
                any(), any(), any(), any(), any(), any(), any())
        ).thenReturn(List.of());
        when(productOrderService.findCurrentOrderOrCreateNew()).thenReturn(new ProductOrderDto());
        mvc.perform(get("/products").param("key-word", "key"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("basket"))
                .andExpect(model().attribute("currentPageNum", 0))
                .andExpect(model().attribute("currentPageSize", 10))
                .andExpect(model().attributeExists("currentKeyWord"));
    }

    @Test
    void getProductByIdTest() throws Exception {
        var product = new ProductDto().setId("019fed39-d868-4e59-9093-1b2f83db2154").setTitle("").setPrice(BigDecimal.ONE).setDescription("").setImage("");
        when(productService.findById(any())).thenReturn(product);

        var productOrderDto = new ProductOrderDto().setOrderSum(BigDecimal.ZERO).setStatus("").setOrderItems(List.of(new BasketDto().setProductInfo(new ProductDto().setId("019fed39-d868-4e59-9093-1b2f83db2154"))));
        when(productOrderService.findCurrentOrderOrCreateNew()).thenReturn(productOrderDto);
        mvc.perform(get("/product/019fed39-d868-4e59-9093-1b2f83db2154"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product", "order", "basket"))
                .andExpect(model().attribute("product", product))
                .andExpect(model().attribute("order", productOrderDto));
    }

    @Test
    void uploadProduct() throws Exception {
        doNothing().when(productService).saveNewProduct(any());
        when(csvReaderService.readCsv(any())).thenReturn(List.of(new ProductDto()));
        mvc.perform(multipart("/product/upload")
                .file(new MockMultipartFile("file", new byte[]{})))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}